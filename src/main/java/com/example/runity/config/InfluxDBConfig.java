package com.example.runity.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.token}")
    private String TOKEN;

    @Value("${influxdb.org}")
    private String ORG;

    @Value("${influxdb.bucket}")
    private String BUCKET;

    @Value("${influxdb.url}")
    private String URL;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(URL, TOKEN.toCharArray(), ORG, BUCKET);
    }

    @Bean
    public String influxOrg() {
        return ORG;
    }

    @Bean
    public String influxBucket() {
        return BUCKET;
    }
}
