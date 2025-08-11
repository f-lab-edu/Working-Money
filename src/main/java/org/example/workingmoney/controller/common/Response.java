package org.example.workingmoney.controller.common;

public record Response<T>(Integer code, String message, T value) {

    public static <T> Response<T> ok(T value) {
        return new Response<>(0, "ok", value);
    }

    public static <T> Response<T> error(ErrorCode errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
