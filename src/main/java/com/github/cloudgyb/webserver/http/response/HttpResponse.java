package com.github.cloudgyb.webserver.http.response;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Http 响应顶级接口
 *
 * @author cloudgyb
 */
public interface HttpResponse {
    void setStatus(HttpResponseStatus status);

    default void setStatusCode(int code) {
        setStatus(HttpResponseStatus.valueOf(code));
    }

    default void write(String content) {
        write(content.getBytes(Charset.defaultCharset()));
    }

    void write(byte[] content);

    default void write(File file) {
        write(file, 0, file.length());
    }

    void write(File file, long start, long length);

    default void setContentType(String type) {
        addHeader(HttpHeaderNames.CONTENT_TYPE.toString(), type);
    }

    default void setContentLength(long length) {
        addHeader(HttpHeaderNames.CONTENT_LENGTH.toString(), length);
    }

    void addHeader(String header, Object value);

    void setCookie(Cookie cookie);

    void end();

}
