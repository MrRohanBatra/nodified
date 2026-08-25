package com.nodified.identity.utils;

import java.util.UUID;

public class PublicIdGenerator {

    public static String generate(Class<?> entity){
        PublicIdPrefix publicIdPrefix=entity.getAnnotation(PublicIdPrefix.class);
        String prefix=publicIdPrefix.value();
        StringBuilder publicId=new StringBuilder();
        publicId.append(prefix).append("_").append(UUID.randomUUID().toString());
        return publicId.toString();
    }
}