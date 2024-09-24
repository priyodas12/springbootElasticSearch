package com.elastic.springbootElasticSearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
public class FakerConfig {

  @Bean
  public Faker getFakerInstance () {
    return Faker.instance ();
  }
}