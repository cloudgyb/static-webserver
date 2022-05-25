package com.github.cloudgyb.webserver.config;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

public class CommonsCLITest {

    @Test
    public void testPrintHelp() {
        Options options = new Options();
        options.addOption("h", "help", false, "Output all help message.");
        options.addOption(Option.builder()
                .option("a").required(false)
                .hasArg(false).build());
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ls [option] [FILE]", options);
    }


    @Test
    public void testCLIParse() throws ParseException {
        String[] args = {"--server-port=80"};
        DefaultParser defaultParser = new DefaultParser();
        Options options = new Options();
        options.addOption(Option.builder()
                .longOpt("server-port")
                .valueSeparator('=')
                .hasArg()
                .argName("port").build());
        new HelpFormatter().printHelp("WebServer", options);
        CommandLine cli = defaultParser.parse(options, args);
        boolean b = cli.hasOption("server-port");
        if (b) {
            String optionValue = cli.getOptionValue("server-port");
            int port = Integer.parseInt(optionValue);
            System.out.println(port);
            Assert.assertEquals(80, port);
        }
    }

}
