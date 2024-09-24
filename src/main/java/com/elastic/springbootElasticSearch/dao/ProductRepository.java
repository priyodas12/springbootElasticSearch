package com.elastic.springbootElasticSearch.dao;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.elastic.springbootElasticSearch.model.Product;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, UUID> {
}
