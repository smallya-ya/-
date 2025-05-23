package com.ruoyi.framework.config;

//public class RestTemplateConfig {
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig  {

//    @Bean("frameworkRestTemplateConfig")
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
