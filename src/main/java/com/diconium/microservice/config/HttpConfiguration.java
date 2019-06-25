package com.diconium.microservice.config;

import com.diconium.microservice.utils.RestCallTemplate;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfiguration {

    @Value("${diconium.http.timeout}")
    private Integer seconds;

    @Bean
    public HttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(seconds * 1000)
            .build();
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    @Bean
    public RestCallTemplate getRestTemplate() {
        return new RestCallTemplate();
    }

}
