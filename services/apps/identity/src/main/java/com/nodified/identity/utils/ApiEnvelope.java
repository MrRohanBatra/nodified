package com.nodified.identity.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiEnvelope<T> {

    @Builder.Default
    private String status = "success";

    private String message;
    private T data;
    private ApiError error;

    @Builder.Default
    private MetaObject meta = new MetaObject();

    public static <T> ApiEnvelope<T> success(T data) {
        return ApiEnvelope.<T>builder()
                .status("success")
                .data(data)
                .meta(new MetaObject())
                .build();
    }

    public static <T> ApiEnvelope<T> success(T data, String message) {
        return ApiEnvelope.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .meta(new MetaObject())
                .build();
    }

    public static <T> ApiEnvelope<T> error(String code, String message) {
        return ApiEnvelope.<T>builder()
                .status("error")
                .error(new ApiError(code, message))
                .meta(new MetaObject())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiError {
        private String code;
        private String message;
    }
}
