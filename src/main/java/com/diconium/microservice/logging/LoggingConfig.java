package com.diconium.microservice.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Value("${diconium.rootloglevel}")
    String rootLogLevel;

    @Value("${diconium.apploglevel}")
    String appLogLevel;

    @Value("diconium.appendername")
    String appenderName;

    @Bean
    public void configureLogging(){
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory
            .newConfigurationBuilder();
        builder.setStatusLevel(Level.getLevel(rootLogLevel));

        builder.setConfigurationName("Builder");


        AppenderComponentBuilder appenderBuilder = builder.newAppender(appenderName, "CONSOLE")
            .addAttribute("target",
                ConsoleAppender.Target.SYSTEM_OUT);

        appenderBuilder.add(builder.newLayout("PatternLayout")
            .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));

        builder.add(appenderBuilder);

        builder.add(builder.newAsyncLogger("com.diconium.microservice", Level.getLevel(appLogLevel))
            .add(builder.newAppenderRef(appenderName)).addAttribute("additivity", false));

        builder.add(builder.newRootLogger(Level.getLevel(rootLogLevel)).add(builder.newAppenderRef(appenderName)));

        LoggerContext ctx = Configurator.initialize(builder.build());
        ctx.setConfiguration(builder.build());
    }

}
