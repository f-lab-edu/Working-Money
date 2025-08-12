package org.example.workingmoney.service.common.exception;

public enum CommonExceptionDescription implements ExceptionDescribable {
    UNKNOWN(10001, "unknown");

    private final int code;
    private final String defaultMessage;

    CommonExceptionDescription(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int code() { return code; }
    public String defaultMessage() { return defaultMessage; }
}
