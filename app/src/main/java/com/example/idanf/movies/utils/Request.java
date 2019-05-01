package com.example.idanf.movies.utils;

import org.json.JSONObject;

public final class Request {

    public final String url;
    public final HttpRequest.Method method;
    public final JSONObject data;

    public Request(String url, HttpRequest.Method method, JSONObject data) {
        this.url = url;
        this.method = method;
        this.data = data;
    }

}