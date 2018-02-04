package com.apyhs.artoria.net.http;

import java.io.IOException;

public interface Handler {

    Response execute(Request request) throws IOException;

}
