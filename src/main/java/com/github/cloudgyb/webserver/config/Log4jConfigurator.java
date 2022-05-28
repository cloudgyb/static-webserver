package com.github.cloudgyb.webserver.config;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * log4j配置器
 *
 * @author cloudgyb
 */
public class Log4jConfigurator {
    static {
        File file = new File("../config/log4j.properties");
        if (file.exists()) {
            PropertyConfigurator.configure(file.getAbsolutePath());
        }
    }

    public static void config() {
        //do nothing
    }
}
