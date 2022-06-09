package com.github.cloudgyb.webserver.util;

/**
 * 文件工具类
 *
 * @author cloudgyb
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    public static String getSuffix(String filename) {
        int i = filename.lastIndexOf('.');
        if (i == -1)
            return "";
        return filename.substring(i);
    }
}
