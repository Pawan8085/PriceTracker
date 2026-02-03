package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);      // number of concurrent threads
        executor.setMaxPoolSize(20);       // max threads if load increases
        executor.setQueueCapacity(50);     // queue for pending tasks
        executor.setThreadNamePrefix("AsyncNotifier-");
        executor.initialize();
        return executor;
    }

}
