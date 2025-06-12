package com.example.runity.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    private static final String TOKEN = "XA874fc15KCgubAhrwEvz9fsxxR1N8tAn7NgEapmLjGv-KWbTAS4MGSGblfz9irzezWJBYW4kwY1p2XaJtvdMQ==";
    private static final String ORG = "myorg";
    private static final String BUCKET = "running_data";
    private static final String URL = "http://localhost:8086";

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
