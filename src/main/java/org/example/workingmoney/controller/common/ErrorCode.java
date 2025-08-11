package org.example.workingmoney.controller.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNKNOWN(10001, "unknown");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
