-- ==============================================================================
-- Nodified - Supabase / PostgreSQL Row-Level Security (RLS) Setup
-- ==============================================================================
-- Description:
--   Automated Multi-Tenant Row-Level Security configuration.
--   - Scans tables in 'identity' and 'monitor' schemas.
--   - Automatically applies Pattern C RLS to ANY table containing 'tenant_id'.
--   - Supports Pattern A (Global data - no tenant_id column).
--   - Supports 'app.is_auth_flow' = 'true' for authentication/login lookups.
--   - Installs an Event Trigger (on CREATE TABLE only) to prevent recursion.
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. Ensure Required Schemas Exist
-- ------------------------------------------------------------------------------
CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS monitor;

-- ------------------------------------------------------------------------------
-- 2. Drop Any Previous Event Trigger First (Prevents recursion during setup)
-- ------------------------------------------------------------------------------
DROP EVENT TRIGGER IF EXISTS auto_tenant_rls_trigger;

-- ------------------------------------------------------------------------------
-- 3. Optional: Clean Up Legacy Tables
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS identity.role_authorities CASCADE;
DROP TABLE IF EXISTS identity.user_roles CASCADE;
DROP TABLE IF EXISTS identity.authorities CASCADE;
DROP TABLE IF EXISTS identity.roles CASCADE;

-- ------------------------------------------------------------------------------
-- 4. Core Function: Apply Pattern C RLS to a Table (if tenant_id exists)
-- ------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION apply_tenant_rls_to_table(p_schema text, p_table text)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    v_has_tenant_id boolean;
    v_is_uuid boolean;
    v_rls_enabled boolean;
    v_policy_sql text;
BEGIN
    -- Check if tenant_id column exists in this table
    SELECT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = p_schema
          AND table_name = p_table
          AND column_name = 'tenant_id'
    ) INTO v_has_tenant_id;

    IF v_has_tenant_id THEN
        -- Check if RLS is already enabled to avoid redundant ALTER statements
        SELECT c.relrowsecurity
        FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE n.nspname = p_schema AND c.relname = p_table
        INTO v_rls_enabled;

        IF NOT coalesce(v_rls_enabled, false) THEN
            EXECUTE format('ALTER TABLE %I.%I ENABLE ROW LEVEL SECURITY;', p_schema, p_table);
            EXECUTE format('ALTER TABLE %I.%I FORCE ROW LEVEL SECURITY;', p_schema, p_table);
        END IF;

        -- Check if tenant_id data type is UUID
        SELECT (data_type = 'uuid')
        FROM information_schema.columns
        WHERE table_schema = p_schema
          AND table_name = p_table
          AND column_name = 'tenant_id'
        INTO v_is_uuid;

        -- Drop existing tenant policy if present to avoid conflicts
        EXECUTE format('DROP POLICY IF EXISTS tenant_isolation_policy ON %I.%I;', p_schema, p_table);

        -- Create Pattern C Policy (supports tenant_id matching AND is_auth_flow bypass)
        v_policy_sql := format(
            'CREATE POLICY tenant_isolation_policy ON %I.%I
             AS PERMISSIVE
             FOR ALL
             USING (
                 NULLIF(current_setting(''app.is_auth_flow'', true), '''') = ''true''
                 OR
                 tenant_id = NULLIF(current_setting(''app.current_tenant_id'', true), '''')%s
             )
             WITH CHECK (
                 NULLIF(current_setting(''app.is_auth_flow'', true), '''') = ''true''
                 OR
                 tenant_id = NULLIF(current_setting(''app.current_tenant_id'', true), '''')%s
             );',
            p_schema, p_table,
            CASE WHEN v_is_uuid THEN '::uuid' ELSE '' END,
            CASE WHEN v_is_uuid THEN '::uuid' ELSE '' END
        );

        EXECUTE v_policy_sql;
        RAISE NOTICE 'Auto-applied Pattern C RLS to %.%', p_schema, p_table;
    END IF;
END;
$$;

-- ------------------------------------------------------------------------------
-- 5. Batch Function: Scan and Apply RLS to All Tables in Target Schemas
-- ------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION apply_tenant_rls_to_all(p_schemas text[] DEFAULT ARRAY['identity', 'monitor'])
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (
        SELECT table_schema, table_name
        FROM information_schema.tables
        WHERE table_schema = ANY(p_schemas)
          AND table_type = 'BASE TABLE'
    ) LOOP
        PERFORM apply_tenant_rls_to_table(r.table_schema, r.table_name);
    END LOOP;
END;
$$;

-- ------------------------------------------------------------------------------
-- 6. Event Trigger: Auto-Apply RLS ONLY on 'CREATE TABLE' (Prevents recursion)
-- ------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION trg_auto_apply_tenant_rls()
RETURNS event_trigger
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    obj record;
BEGIN
    FOR obj IN SELECT * FROM pg_event_trigger_ddl_commands() WHERE command_tag = 'CREATE TABLE'
    LOOP
        IF obj.schema_name IN ('identity', 'monitor') THEN
            PERFORM apply_tenant_rls_to_table(obj.schema_name, substring(obj.object_identity from '[^.]+$'));
        END IF;
    END LOOP;
END;
$$;

CREATE EVENT TRIGGER auto_tenant_rls_trigger
    ON ddl_command_end
    WHEN TAG IN ('CREATE TABLE')
    EXECUTE FUNCTION trg_auto_apply_tenant_rls();

-- ------------------------------------------------------------------------------
-- 7. Execute Immediately on Existing Tables
-- ------------------------------------------------------------------------------
SELECT apply_tenant_rls_to_all();


-- ==============================================================================
-- VERIFICATION EXAMPLES (Uncomment to test in SQL Editor)
-- ==============================================================================
/*
-- 1. Insert a test tenant (Pattern A - Global table)
INSERT INTO identity.tenant_accounts (id, key, name, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', 'acme-corp', 'Acme Corporation', NOW())
ON CONFLICT DO NOTHING;

-- 2. Insert test user for Tenant 11111111... using auth bypass
SET LOCAL app.is_auth_flow = 'true';
INSERT INTO identity.user_accounts (id, username, password, tenant_id, created_at)
VALUES ('22222222-2222-2222-2222-222222222222', 'alex', 'hashed_pwd', '11111111-1111-1111-1111-111111111111', NOW());

-- 3. Query with NO tenant session (Expect 0 rows)
SET LOCAL app.is_auth_flow = 'false';
SET LOCAL app.current_tenant_id = '';
SELECT * FROM identity.user_accounts;

-- 4. Query with Tenant 11111111... context (Expect 1 row)
SET LOCAL app.current_tenant_id = '11111111-1111-1111-1111-111111111111';
SELECT * FROM identity.user_accounts;

-- 5. Query with is_auth_flow = 'true' for login (Expect all matching users)
SET LOCAL app.is_auth_flow = 'true';
SELECT * FROM identity.user_accounts WHERE username = 'alex';
*/
