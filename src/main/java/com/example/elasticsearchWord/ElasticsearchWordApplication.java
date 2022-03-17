package com.example.elasticsearchWord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.example.elasticsearchWord.config.Config;

@SpringBootApplication

public class ElasticsearchWordApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchWordApplication.class, args);
	}

}
