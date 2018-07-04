package com.money.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "common")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CommonProp {
    private String serverUrlPrefix;
    private Integer readTimeOut;
    private Integer connectTimeOut;



}
