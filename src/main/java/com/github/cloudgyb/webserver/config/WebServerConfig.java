package com.github.cloudgyb.webserver.config;

/**
 * WebServer 全局配置参数
 *
 * @author cloudgyb
 */
public class WebServerConfig {
    public static final String CONFIG_HOST = "server.host";
    public static final String CONFIG_PORT = "server.port";
    public static final String CONFIG_SSL_KEY = "server.ssl.key";
    public static final String CONFIG_SSL_CERT = "server.ssl.cert";
    public static final String CONFIG_WEB_ROOT = "web.root";
    public static final String CONFIG_H2 = "h2";
    public static final String CONFIG_TCP_BACKLOG = "tcp.backlog";
    private String host = "localhost";
    private int port = 80;
    private int tcpBacklog = 10;
    private String webRoot = "WEBROOT";

    private SSLConfig sslConfig;

    private boolean enableHttp2;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTcpBacklog() {
        return tcpBacklog;
    }

    public void setTcpBacklog(int tcpBacklog) {
        this.tcpBacklog = tcpBacklog;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public SSLConfig getSslConfig() {
        return sslConfig;
    }

    public void setSslConfig(SSLConfig sslConfig) {
        sslConfig.validate();
        this.sslConfig = sslConfig;
    }

    public void enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
    }

    public boolean enableHttp2() {
        return this.enableHttp2;
    }
}
