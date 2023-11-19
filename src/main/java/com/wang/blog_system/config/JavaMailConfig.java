package com.wang.blog_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.mail")
    public JavaMailSenderImpl mailSender(){
        return new JavaMailSenderImpl();
    }
}
