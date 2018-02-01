package com.apyhs.artoria.net.http;

/**
 * Http method.
 * @author Kahle
 */
public enum Method {
    GET(     "GET"      ),
    POST(    "POST"     ),
    PUT(     "PUT"      ),
    DELETE(  "DELETE"   ),
    PATCH(   "PATCH"    ),
    HEAD(    "HEAD"     ),
    OPTIONS( "OPTIONS"  ),
    TRACE(   "TRACE"    ),
    ;

    private String name;

    Method(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
