package com.github.cloudgyb.webserver.http.request;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Http请求接口
 *
 * @author cloudgyb
 */
public interface HttpRequest {
    HttpHeaders getAllHeaders();

    HttpMethod getMethod();

    String getUri();

    byte[] getBodyBytes();

    String getBody();
}
