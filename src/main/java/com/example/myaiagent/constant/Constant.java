package com.example.myaiagent.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "we")
public class Constant {

    public static String OPENAI_API_KEY;
}
