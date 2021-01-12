package com.chrisgya.springsecurity.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, CustomThreadProperties.class, MailTemplateProperties.class, MailSubjectProperties.class,
        FrontEndUrlProperties.class})
public class PropertiesConfig {
}
