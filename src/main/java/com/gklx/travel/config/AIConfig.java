package com.gklx.travel.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import io.github.imfangs.dify.client.DifyClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.model.DifyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {


    @Bean
    public DifyClient difyClient() {

        DifyConfig config = DifyConfig.builder()
                .baseUrl("http://localhost/v1")
                .apiKey("app-nyDWpAfsQ4waWgHRoOFxu4I9")
                .connectTimeout(5000)
                .readTimeout(60000)
                .writeTimeout(30000)
                .build();

        return DifyClientFactory.createClient(config);
    }

    @Bean
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.builder()
                .baseUrl("https://api.deepseek.com/")
                .apiKey("sk-bfb4c14168b341128d8028f1b1bc249c")
                .build();
    }
}
