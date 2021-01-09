package com.chrisgya.springsecurity.config;

import com.chrisgya.springsecurity.config.properties.CustomThreadProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Primary
    @Bean
    Executor asyncExecutor(CustomThreadProperties customThreadProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(customThreadProperties.getCorePoolSize());
        executor.setMaxPoolSize(customThreadProperties.getMaxPoolSize());
        executor.setQueueCapacity(customThreadProperties.getQueueCapacity());
        executor.setThreadNamePrefix("MyAsync-");
        executor.initialize();
        return executor;
    }

    @Bean
    ExecutorService executorService(CustomThreadProperties customThreadProperties){
        ExecutorService executorService = Executors.newFixedThreadPool(customThreadProperties.getMaxPoolSize());
        return executorService;
    }
}
