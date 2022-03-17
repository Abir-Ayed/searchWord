package com.example.elasticsearchWord.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.elasticsearchWord.entities.Documents;


@Repository
public interface FileRepository extends ElasticsearchRepository<Documents, Integer> {
	
	
}
