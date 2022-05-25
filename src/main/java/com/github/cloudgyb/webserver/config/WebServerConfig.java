package com.github.cloudgyb.webserver.config;

/**
 * WebServer 全局配置参数
 *
 * @author cloudgyb
 */
public class WebServerConfig {
    public static final String CONFIG_HOST = "server.host";
    public static final String CONFIG_PORT = "server.port";
    public static final String CONFIG_WEB_ROOT = "web.root";
    public static final String CONFIG_TCP_BACKLOG = "tcp.backlog";
    private String host = "localhost";
    private int port = 80;
    private int tcpBacklog = 10;
    private String webRoot = "WEBROOT";

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
}
