package com.money;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication(scanBasePackages={"com.money"})
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = "com.money.mapper")
public class InitServer extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(InitServer.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InitServer.class);
    }


    @Bean
    public Executor mySimpleAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(500);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("http-callback-");
        executor.initialize();
        return executor;
    }


}
