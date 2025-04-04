/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.data.CodeDefinition;

/**
 * The built-in error codes.
 * @author Kahle
 */
public class Errors {

    public static CodeDefinition ok = new SimpleCode(200, "OK. ");
    public static CodeDefinition badRequest = new SimpleCode(400, "Bad Request. ");
    public static CodeDefinition unauthorized = new SimpleCode(401, "Unauthorized. ");
    public static CodeDefinition forbidden = new SimpleCode(403, "Forbidden. ");
    public static CodeDefinition notFound = new SimpleCode(404,"Not Found. ");
    public static CodeDefinition internalServerError = new SimpleCode(500, "Internal Server Error. ");
    public static CodeDefinition serviceUnavailable = new SimpleCode(503, "Service Unavailable. ");

    // ====

    public static CodeDefinition paramIsRequired = new SimpleCode(400, "Parameter is required. ");
    public static CodeDefinition paramFormatError = new SimpleCode(400, "Parameter format error. ");

    // ====

    public static CodeDefinition noLogin = new SimpleCode(401, "No login. ");
    public static CodeDefinition invalidToken = new SimpleCode(401, "Invalid token. ");
    public static CodeDefinition invalidUser = new SimpleCode(401, "Invalid user. ");
    public static CodeDefinition noPermission = new SimpleCode(403, "No permission. ");

    // ====

    public static CodeDefinition recordIdNotNull = new SimpleCode(500, "Record id must not null. ");
    public static CodeDefinition recordExist     = new SimpleCode(500, "Record already exist. ");
    public static CodeDefinition recordNotExist  = new SimpleCode(500, "Record not exist. ");
    public static CodeDefinition recordSaveFailure   = new SimpleCode(500, "Record save failure. ");
    public static CodeDefinition recordUpdateFailure = new SimpleCode(500, "Record update failure. ");
    public static CodeDefinition recordDeleteFailure = new SimpleCode(500, "Record delete failure. ");

    // ====

    private Errors() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
