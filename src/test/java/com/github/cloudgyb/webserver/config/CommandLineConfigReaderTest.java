package com.github.cloudgyb.webserver.config;

import org.junit.Test;

import java.util.Map;

public class CommandLineConfigReaderTest {
    @Test
    public void test() {
        String[] args = new String[]{"--server-host=localhost", "--server-port=80"};
        CommandLineConfigReader commandLineConfigReader = new CommandLineConfigReader(args);
        Map<String, Object> stringObjectMap = commandLineConfigReader.readConfig();
        stringObjectMap.forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
