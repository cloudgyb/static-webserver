package com.github.cloudgyb.webserver.http.request;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http2.Http2HeadersFrame;

import java.util.Map;

/**
 * http2 请求
 *
 * @author geng
 * @since 2022/6/17 15:44
 */
public class Http2RequestWrapper implements HttpRequest {
    private final Http2HeadersFrame headersFrame;
    private final HttpHeaders headers = new DefaultHttpHeaders();

    public Http2RequestWrapper(Http2HeadersFrame http2HeadersFrame) {
        this.headersFrame = http2HeadersFrame;
        extractHeaders();
    }

    /**
     * 从headersFrame中提取请求头
     */
    private void extractHeaders() {
        for (Map.Entry<CharSequence, CharSequence> next : headersFrame.headers()) {
            CharSequence key = next.getKey();
            CharSequence value = next.getValue();
            this.headers.add(key, value);
        }
    }

    @Override
    public HttpHeaders getAllHeaders() {
        return this.headers;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.headersFrame.headers().method().toString());
    }

    @Override
    public String getUri() {
        return this.headersFrame.headers().path().toString();
    }

    @Override
    public byte[] getBodyBytes() {
        return new byte[0];
    }

    @Override
    public String getBody() {
        return null;
    }
}
