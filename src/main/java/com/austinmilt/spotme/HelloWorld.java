package com.austinmilt.spotme;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class HelloWorld implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        final String who = System.getenv("HELLO_WHO");
        response.getWriter().write(String.format("Hello, %s\n", who));
    }
}
