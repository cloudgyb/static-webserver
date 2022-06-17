package com.github.cloudgyb.webserver.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统属性配置读取
 *
 * @author cloudgyb
 */
public class SystemPropertiesConfigReader implements ConfigReader {
    @Override
    public Map<String, Object> readConfig() {
        String serverHost = System.getProperty(WebServerConfig.CONFIG_HOST);
        String serverPort = System.getProperty(WebServerConfig.CONFIG_PORT);
        String webRoot = System.getProperty(WebServerConfig.CONFIG_WEB_ROOT);
        String sslCertFilePath = System.getProperty(WebServerConfig.CONFIG_SSL_CERT);
        String sslKeyFilePath = System.getProperty(WebServerConfig.CONFIG_SSL_KEY);
        String h2 = System.getProperty(WebServerConfig.CONFIG_H2);
        HashMap<String, String> map = new HashMap<>();
        map.put(WebServerConfig.CONFIG_HOST, serverHost);
        map.put(WebServerConfig.CONFIG_PORT, serverPort);
        map.put(WebServerConfig.CONFIG_WEB_ROOT, webRoot);
        map.put(WebServerConfig.CONFIG_SSL_KEY, sslKeyFilePath);
        map.put(WebServerConfig.CONFIG_SSL_CERT, sslCertFilePath);
        map.put(WebServerConfig.CONFIG_H2, h2 != null ? "true" : "false");
        return validateConfig(map);
    }
}
