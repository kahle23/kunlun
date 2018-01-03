package com.apyhs.artoria.exception;

public enum SystemCode implements ErrorCode {
    MEMORY_OVERFLOW("1", "MEMORY OVERFLOW"),
    ABNORMAL_SHUTDOWN("2", "ABNORMAL SHUTDOWN");

    private String code;
    private String content;

    SystemCode(String code, String content) {
        this.code = code;
        this.content = content;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getContent() {
        return content;
    }


}
