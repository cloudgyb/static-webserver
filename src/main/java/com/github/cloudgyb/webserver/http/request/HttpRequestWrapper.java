package com.github.cloudgyb.webserver.http.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Http请求包装器，包装Netty{@link FullHttpRequest}
 *
 * @author cloudgyb
 */
public class HttpRequestWrapper implements HttpRequest {
    private final FullHttpRequest request;

    public HttpRequestWrapper(FullHttpRequest request) {
        this.request = request;
    }

    @Override
    public HttpHeaders getAllHeaders() {
        return request.headers();
    }

    @Override
    public HttpMethod getMethod() {
        return request.method();
    }

    @Override
    public String getUri() {
        return request.uri();
    }

    @Override
    public byte[] getBodyBytes() {
        return request.content().array();
    }

    @Override
    public String getBody() {
        return request.content().toString();
    }
}
