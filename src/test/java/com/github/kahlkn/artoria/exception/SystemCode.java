package com.github.kahlkn.artoria.exception;

public enum SystemCode implements ErrorCode {
    code1("code1", "Username is null! "),
    code2("code2", "Password is null! ");

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
