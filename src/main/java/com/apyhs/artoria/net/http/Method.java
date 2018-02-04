package com.apyhs.artoria.net.http;

public enum Method {
    GET(      "GET"      , false ),
    POST(     "POST"     , true  ),
    PUT(      "PUT"      , true  ),
    DELETE(   "DELETE"   , false ),
    PATCH(    "PATCH"    , true  ),
    HEAD(     "HEAD"     , false ),
    OPTIONS(  "OPTIONS"  , false ),
    TRACE(    "TRACE"    , false )
    ;

    private String name;
    private boolean hasBody;

    Method(String name, boolean hasBody) {
        this.name = name;
        this.hasBody = hasBody;
    }

    public String getName() {
        return name;
    }

    public boolean getHasBody() {
        return hasBody;
    }

    @Override
    public String toString() {
        return name;
    }

}
