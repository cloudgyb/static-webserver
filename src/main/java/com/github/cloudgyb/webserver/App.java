package com.github.cloudgyb.webserver;

import com.github.cloudgyb.webserver.config.Log4jConfigurator;
import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.config.WebServerConfigFactory;

/**
 * The app entry point
 *
 * @author cloudgyb
 */
public class App {
    public static void main(String[] args) {
        Log4jConfigurator.config();
        WebServerConfig config = new WebServerConfigFactory().getConfig(args);
        WebServer.start(config);
    }
}
