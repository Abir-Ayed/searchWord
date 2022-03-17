package com.example.elasticsearchWord.config;

import java.net.UnknownHostException;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.elasticsearch.client.transport.TransportClient;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.elasticsearchWord.repository")
@ComponentScan(basePackages = {"com.example.elasticsearchWord"})
@ComponentScan(basePackages = { "com.example.elasticsearchWord.Service" })

public class Config extends AbstractElasticsearchConfiguration{

	
	@Value("${elasticsearch.url}")
    public String elasticsearchUrl;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
      

    	final ClientConfiguration config = ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .build();

        return RestClients.create(config).rest();
    }

   
}
