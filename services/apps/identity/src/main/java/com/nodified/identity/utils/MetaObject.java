package com.nodified.identity.utils;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nodified.identity.config.AppInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaObject {

    @Builder.Default
    private Instant timestamp = Instant.now();

    @Builder.Default
    private String version = AppInfo.getVersion();

    private String requestId;
}
