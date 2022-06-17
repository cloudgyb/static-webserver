package com.github.cloudgyb.webserver.config;

import org.apache.commons.lang.StringUtils;

import java.io.File;
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
        String sslKey = origin.get(WebServerConfig.CONFIG_SSL_KEY);
        String sslCert = origin.get(WebServerConfig.CONFIG_SSL_CERT);
        String h2 = origin.get(WebServerConfig.CONFIG_H2);
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
        if (sslKey != null && sslCert != null) {
            File sslCertFile = new File(sslCert);
            File sslKeyFile = new File(sslKey);
            boolean sslKeyFileExists = sslKeyFile.exists();
            if (!sslKeyFileExists) {
                throw new WebServerConfigException(String.format("SSL Private Key file(%s) not exists!",
                        sslKeyFile.getAbsoluteFile()));
            }
            boolean sslKeyCertExists = sslCertFile.exists();
            if (!sslKeyCertExists) {
                throw new WebServerConfigException(String.format("SSL cert file(%s) not exists!",
                        sslCertFile.getAbsoluteFile()));
            }
            map.put(WebServerConfig.CONFIG_SSL_KEY, sslKeyFile);
            map.put(WebServerConfig.CONFIG_SSL_CERT, sslCertFile);
        } else if (sslKey == null && sslCert != null) {
            throw new WebServerConfigException("The param " + WebServerConfig.CONFIG_SSL_KEY + " is not configured");
        } else if (sslKey != null) {
            throw new WebServerConfigException("The param " + WebServerConfig.CONFIG_SSL_CERT + " is not configured");
        }
        if ("true".equals(h2)) {
            map.put(WebServerConfig.CONFIG_H2, true);
        }
        return map;
    }
}
