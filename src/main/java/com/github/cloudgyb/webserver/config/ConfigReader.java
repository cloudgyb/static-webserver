package com.github.cloudgyb.webserver.config;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置读取器接口
 *
 * @author cloudgyb
 */
public interface ConfigReader {
    Map<String, Object> readConfig() throws Exception;

    default Map<String, Object> validateConfig(Map<String, String> origin) {
        String serverHost = origin.get(WebServerConfig.CONFIG_HOST);
        String serverPort = origin.get(WebServerConfig.CONFIG_PORT);
        String webRoot = origin.get(WebServerConfig.CONFIG_WEB_ROOT);
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(serverHost))
            map.put(WebServerConfig.CONFIG_HOST, serverHost);
        if (StringUtils.isNotBlank(serverPort)) {
            try {
                int port = Integer.parseInt(serverPort);
                if (port <= 0 || port > 65535) {
                    throw new RuntimeException("环境变量中发现非法的端口配置：" + port);
                }
                map.put(WebServerConfig.CONFIG_PORT, port);
            } catch (NumberFormatException ignore) {
            }
        }
        if (StringUtils.isNotBlank(webRoot)) {
            map.put(WebServerConfig.CONFIG_WEB_ROOT, webRoot);
        }
        return map;
    }
}
