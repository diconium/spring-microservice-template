package com.diconium.microservice;

import com.diconium.microservice.utils.RestCallTemplate;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }

    @Bean
    public HttpClient getHttpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    public RestCallTemplate getRestTemplate() {
        return new RestCallTemplate();
    }

}
