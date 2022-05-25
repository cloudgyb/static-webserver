package com.github.cloudgyb.webserver.http;

/**
 * 支持的媒体类型
 *
 * @author cloudgyb
 */
public enum MediaType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_SVG("image/svg"),
    IMAGE_webp("image/webp"),
    APPLICATION_OCTET_STREAM("application/octet-stream");

    MediaType(String value) {
        this.value = value;
    }

    final String value;

    public static MediaType getMediaType(String fileSuffix) {
        if (".txt".equalsIgnoreCase(fileSuffix)) {
            return TEXT_PLAIN;
        }
        if (".html".equalsIgnoreCase(fileSuffix) || ".htm".equalsIgnoreCase(fileSuffix)) {
            return TEXT_HTML;
        }
        if (".css".equalsIgnoreCase(fileSuffix)) {
            return TEXT_CSS;
        }
        if (".js".equalsIgnoreCase(fileSuffix)) {
            return APPLICATION_JAVASCRIPT;
        }
        if (".png".equalsIgnoreCase(fileSuffix)) {
            return IMAGE_PNG;
        }
        if (".jpg".equalsIgnoreCase(fileSuffix) || ".jpeg".equalsIgnoreCase(fileSuffix)) {
            return IMAGE_JPEG;
        }
        if (".gif".equalsIgnoreCase(fileSuffix)) {
            return IMAGE_GIF;
        }
        if (".svg".equalsIgnoreCase(fileSuffix)) {
            return IMAGE_SVG;
        }
        if (".webp".equalsIgnoreCase(fileSuffix)) {
            return IMAGE_webp;
        }
        return APPLICATION_OCTET_STREAM;
    }
}
