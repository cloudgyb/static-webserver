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
        HashMap<String, String> map = new HashMap<>();
        map.put(WebServerConfig.CONFIG_HOST, serverHost);
        map.put(WebServerConfig.CONFIG_PORT, serverPort);
        map.put(WebServerConfig.CONFIG_WEB_ROOT, webRoot);
        return validateConfig(map);
    }
}
