package com.elastic.springbootElasticSearch.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document (indexName = "products")

public class Product {

  @Id
  private UUID id;
  private String name;
  private String description;
  private double price;
  private String category;
}
