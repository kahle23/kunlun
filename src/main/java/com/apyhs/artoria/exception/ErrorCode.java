package com.apyhs.artoria.exception;

public interface ErrorCode {

    /**
     * The code
     * @return The code string
     */
    String getCode();

    /**
     * The code description
     * @return The code description
     */
    String getContent();

}
