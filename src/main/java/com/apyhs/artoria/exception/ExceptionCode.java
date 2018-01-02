package com.apyhs.artoria.exception;

/**
 * Error code
 * @author Kahle
 */
public interface ExceptionCode {

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
