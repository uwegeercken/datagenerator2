package com.datamelt.utilities.datagenerator.application;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Optional;

public class CustomLog4jConfig
{
    public static void setupLog4j2Config(Level logLevel)
    {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");

        LayoutComponentBuilder standardLayout = builder.newLayout("PatternLayout");
        standardLayout.addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n");
        console.add(standardLayout);

        builder.add(console);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(logLevel);
        rootLogger.add(builder.newAppenderRef("stdout"));

        builder.add(rootLogger);
        Configurator.initialize(builder.build());
    }
}
