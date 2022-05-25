package com.github.cloudgyb.webserver.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统环境变量配置读取
 *
 * @author cloudgyb
 */
public class SystemEnvVarConfigReader implements ConfigReader {
    @Override
    public Map<String, Object> readConfig() {
        String serverHost = System.getenv(WebServerConfig.CONFIG_HOST);
        String serverPort = System.getenv(WebServerConfig.CONFIG_PORT);
        String webRoot = System.getenv(WebServerConfig.CONFIG_WEB_ROOT);
        HashMap<String, String> map = new HashMap<>();
        map.put(WebServerConfig.CONFIG_HOST, serverHost);
        map.put(WebServerConfig.CONFIG_PORT, serverPort);
        map.put(WebServerConfig.CONFIG_WEB_ROOT, webRoot);
        return validateConfig(map);
    }
}
