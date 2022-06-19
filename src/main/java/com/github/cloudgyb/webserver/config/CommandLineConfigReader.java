package com.github.cloudgyb.webserver.config;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 从命令行读取配置属性
 * <p>参数格式：
 * <pre>--property=argv</pre>
 * <p/>
 *
 * @author cloudgyb
 */
public class CommandLineConfigReader implements ConfigReader {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String PARMA_BIND_HOST = "server-host";
    private final static String PARMA_BIND_PORT = "server-port";
    private final static String PARMA_WEB_ROOT = "web-root";
    private final static String PARMA_SERVER_SSL_KEY = "server-ssl-key";
    private final static String PARMA_SERVER_SSL_CERT = "server-ssl-cert";
    private final static String PARMA_H2 = "h2";
    private final String[] args;
    private final Options options;

    public CommandLineConfigReader(String[] args) {
        this.args = args;
        this.options = new Options();
        this.options.addOption(Option.builder()
                .longOpt(PARMA_BIND_HOST)
                .hasArg()
                .argName("hostname/ip").build());
        this.options.addOption(Option.builder()
                .longOpt(PARMA_BIND_PORT)
                .hasArg()
                .argName("port").build());
        this.options.addOption(Option.builder()
                .longOpt(PARMA_WEB_ROOT)
                .hasArg()
                .argName("filepath").build());
        this.options.addOption(Option.builder()
                .longOpt(PARMA_SERVER_SSL_KEY)
                .hasArg()
                .argName("filepath").build());
        this.options.addOption(Option.builder()
                .longOpt(PARMA_SERVER_SSL_CERT)
                .hasArg()
                .argName("filepath").build());
        this.options.addOption(Option.builder()
                .longOpt(PARMA_H2)
                .hasArg(false)
                .build());
    }

    @Override
    public Map<String, Object> readConfig() {
        logger.info("开始解析命令行参数...");
        DefaultParser defaultParser = new DefaultParser();
        CommandLine cli;
        try {
            cli = defaultParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String bindHost = cli.getOptionValue(PARMA_BIND_HOST);
        String bindPort = cli.getOptionValue(PARMA_BIND_PORT);
        String webRoot = cli.getOptionValue(PARMA_WEB_ROOT);
        String sslKey = cli.getOptionValue(PARMA_SERVER_SSL_KEY);
        String sslCert = cli.getOptionValue(PARMA_SERVER_SSL_CERT);
        boolean h2 = cli.hasOption(PARMA_H2);
        HashMap<String, String> map = new HashMap<>();
        map.put(WebServerConfig.CONFIG_HOST, bindHost);
        map.put(WebServerConfig.CONFIG_PORT, bindPort);
        map.put(WebServerConfig.CONFIG_WEB_ROOT, webRoot);
        map.put(WebServerConfig.CONFIG_SSL_KEY, sslKey);
        map.put(WebServerConfig.CONFIG_SSL_CERT, sslCert);
        map.put(WebServerConfig.CONFIG_H2, h2 ? "true" : "false");
        return validateConfig(map);
    }
}
