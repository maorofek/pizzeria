package org.example.pizzeria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class StageConfig {

    @Bean("doughChefExecutor")
    public Executor doughChefExecutor() {
        var exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(2);      // dough chefs
        exec.setMaxPoolSize(2);
        exec.setQueueCapacity(50);
        exec.setThreadNamePrefix("dough-");
        exec.initialize();
        return exec;
    }

    @Bean("toppingChefExecutor")
    public Executor toppingChefExecutor() {
        var exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(3);      // topping chefs
        exec.setMaxPoolSize(3);
        exec.setQueueCapacity(50);
        exec.setThreadNamePrefix("topping-");
        exec.initialize();
        return exec;
    }

    @Bean("ovenExecutor")
    public Executor ovenExecutor() {
        var exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(1);      // single oven
        exec.setMaxPoolSize(1);
        exec.setQueueCapacity(50);
        exec.setThreadNamePrefix("oven-");
        exec.initialize();
        return exec;
    }

    @Bean("waiterExecutor")
    public Executor waiterExecutor() {
        var exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(2);      // waiters
        exec.setMaxPoolSize(2);
        exec.setQueueCapacity(50);
        exec.setThreadNamePrefix("waiter-");
        exec.initialize();
        return exec;
    }
}
