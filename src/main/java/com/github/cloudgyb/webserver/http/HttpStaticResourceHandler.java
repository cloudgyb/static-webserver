package com.github.cloudgyb.webserver.http;

import com.github.cloudgyb.webserver.http.request.HttpRequest;
import com.github.cloudgyb.webserver.http.response.HttpResponse;
import com.github.cloudgyb.webserver.util.FileUtils;
import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * http静态资源处理器
 *
 * @author cloudgyb
 */
public class HttpStaticResourceHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String webRoot;

    public HttpStaticResourceHandler(String webRoot) {
        this.webRoot = webRoot;
        File file = new File(webRoot);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (mkdir)
                logger.info("WEBROOT目录已创建！");
        }
    }

    public void handle(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();
        String uriStr = request.getUri();
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            resp404(response);
            return;
        }
        String path = uri.getPath();
        if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.HEAD)) {
            HttpHeaders headers = request.getAllHeaders();
            serveStaticResource(path, method, headers, response);
        } else {
            resp405(method, response);
        }

    }

    private void serveStaticResource(String path, HttpMethod method, HttpHeaders headers, HttpResponse response) {
        if (path.equals("/"))
            path = "/index.html";
        File file = new File(webRoot, path);
        boolean exists = file.exists();
        if (!exists) {
            resp404(response);
            return;
        }
        if (file.isDirectory()) {
            resp403(response);
            return;
        }
        respFile(response, file, method.equals(HttpMethod.HEAD), headers);
    }


    public void respFile(HttpResponse response, File file, boolean isHeadMethod, HttpHeaders headers) {
        boolean canRead = file.canRead();
        if (!canRead) {
            resp403(response);
            return;
        }

        if (!isCacheExpired(headers, file)) {
            resp304(response);
            return;
        }
        String fileName = file.getName();
        long fileLastModified = file.lastModified();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[0];
            if (!isHeadMethod)
                bytes = fis.readAllBytes();
            response.setStatusCode(200);
            response.setContentLength(file.length());
            MediaType mediaType = MediaType.getMediaType(FileUtils.getSuffix(fileName));
            response.setContentType(mediaType.value);
            response.addHeader(HttpHeaderNames.LAST_MODIFIED.toString(), new Date(fileLastModified));
            response.addHeader(HttpHeaderNames.ETAG.toString(), generateFileEtag(file));
            if (!isHeadMethod)
                response.write(bytes);
            response.end();
        } catch (IOException e) {
            logger.error("", e);
            resp500(response);
        }
    }

    private boolean isCacheExpired(HttpHeaders headers, File file) {
        String ifModifiedSince = headers.get(HttpHeaderNames.IF_MODIFIED_SINCE);
        if (ifModifiedSince == null)
            return true;
        Date date = DateFormatter.parseHttpDate(ifModifiedSince);
        if (date == null)
            return true;
        String etag = headers.get(HttpHeaderNames.IF_NONE_MATCH);
        if (etag == null)
            return true;
        return isCacheExpired(date, etag, file);
    }


    private boolean isCacheExpired(Date date, String etag, File file) {
        long ifModifiedSince = file.lastModified() / 1000;
        long fileModifiedTime = date.getTime() / 1000;
        String newEtag = generateFileEtag(file);
        return ifModifiedSince < fileModifiedTime || !newEtag.equals(etag);
    }

    private String generateFileEtag(File file) {
        long l = file.lastModified();
        long length = file.length();
        return "W/\"" + l + "/" + length + "\"";
    }

    private void resp304(HttpResponse response) {
        response.setStatusCode(304);
        response.end();
    }

    private void resp403(HttpResponse response) {
        String respContent = "禁止访问403";
        byte[] bytes = respContent.getBytes(Charset.defaultCharset());
        response.setStatusCode(403);
        response.setContentLength(bytes.length);
        response.setContentType("text/html; charset=utf-8");
        response.write(bytes);
        response.end();
    }

    private void resp404(HttpResponse response) {
        String respContent = "Not Found!404";
        byte[] bytes = respContent.getBytes(Charset.defaultCharset());
        response.setStatusCode(404);
        response.setContentLength(bytes.length);
        response.setContentType("text/html; charset=utf-8");
        response.write(bytes);
        response.end();
    }

    private void resp405(HttpMethod method, HttpResponse response) {
        String respContent = String.format("'%s'方法不允许!", method.name());
        byte[] bytes = respContent.getBytes(Charset.defaultCharset());
        response.setStatusCode(405);
        response.setContentLength(bytes.length);
        response.setContentType("text/html; charset=utf-8");
        response.write(bytes);
        response.end();
    }

    private void resp500(HttpResponse response) {
        String respContent = "系统内部错误500";
        byte[] bytes = respContent.getBytes(Charset.defaultCharset());
        response.setStatusCode(500);
        response.setContentLength(bytes.length);
        response.setContentType("text/html; charset=utf-8");
        response.write(bytes);
        response.end();
    }

}
