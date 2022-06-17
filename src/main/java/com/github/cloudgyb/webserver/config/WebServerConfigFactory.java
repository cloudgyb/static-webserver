package com.github.cloudgyb.webserver.config;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置工厂，支持从命令行、系统环境变量和系统配置属性中读取配置
 * <p>配置优先级:<br>
 * 命令行 > 系统环境变量 > 系统配置属性
 * </p>
 *
 * @author cloudgyb
 */
public class WebServerConfigFactory {

    public WebServerConfig getConfig(String[] args) {
        CommandLineConfigReader commandLineConfigReader = new CommandLineConfigReader(args);
        Map<String, Object> propParam = new SystemPropertiesConfigReader().readConfig();
        Map<String, Object> envParam = new SystemEnvVarConfigReader().readConfig();
        Map<String, Object> cliParam = commandLineConfigReader.readConfig();
        HashMap<String, Object> mergeMap = new HashMap<>(propParam);
        mergeMap.putAll(envParam);
        mergeMap.putAll(cliParam);
        WebServerConfig webServerConfig = new WebServerConfig();
        String host = (String) mergeMap.get(WebServerConfig.CONFIG_HOST);
        if (StringUtils.isNotBlank(host))
            webServerConfig.setHost(host);
        Integer port = (Integer) mergeMap.get(WebServerConfig.CONFIG_PORT);
        if (port != null)
            webServerConfig.setPort(port);
        String webRoot = (String) mergeMap.get(WebServerConfig.CONFIG_WEB_ROOT);
        if (StringUtils.isNotBlank(webRoot))
            webServerConfig.setWebRoot(webRoot);
        File sslKeyFile = (File) mergeMap.get(WebServerConfig.CONFIG_SSL_KEY);
        File sslCertFile = (File) mergeMap.get(WebServerConfig.CONFIG_SSL_CERT);
        if (sslKeyFile != null && sslCertFile != null) {
            SSLConfig sslConfig = new SSLConfig(sslKeyFile, sslCertFile);
            webServerConfig.setSslConfig(sslConfig);
        }
        boolean h2 = (boolean) mergeMap.get(WebServerConfig.CONFIG_H2);
        webServerConfig.enableHttp2(h2);
        if (webServerConfig.enableHttp2() && webServerConfig.getSslConfig() == null) {
            throw new WebServerConfigException("h2依赖于ssl，要想使用h2协议必须配置ssl相关选项！");
        }
        return webServerConfig;
    }
}
