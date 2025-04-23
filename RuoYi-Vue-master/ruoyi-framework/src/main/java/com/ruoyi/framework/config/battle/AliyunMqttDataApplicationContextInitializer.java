package com.ruoyi.framework.config.battle;

import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.DefaultPropertiesPropertySource;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author hongjiasen
 */
public class AliyunMqttDataApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        AliyunMqttSign2PropertySource.addToEnvironment(environment);
        DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
        ConfigDataEnvironmentPostProcessor.applyTo(environment, applicationContext, bootstrapContext);
        bootstrapContext.close(applicationContext);
        DefaultPropertiesPropertySource.moveToEnd(environment);
    }
}
