package com.gklx.travel.config;

import io.github.imfangs.dify.client.DifyClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.model.DifyConfig;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class DFConfig {


    @Bean
    public DifyClient difyClient() {

        DifyConfig config = DifyConfig.builder()
                .baseUrl("https://api.dify.ai/v1")
                .apiKey("your-api-key")
                .connectTimeout(5000)
                .readTimeout(60000)
                .writeTimeout(30000)
                .build();

        return DifyClientFactory.createClient(config);
    }
}
