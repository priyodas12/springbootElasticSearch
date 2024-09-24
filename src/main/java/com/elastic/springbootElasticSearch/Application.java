package com.elastic.springbootElasticSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.elastic.springbootElasticSearch.service.ProductService;


@SpringBootApplication
public class Application {

  @Autowired
  private ProductService productService;


  public static void main (String[] args) {
    SpringApplication.run (Application.class, args);
  }

  @Bean
  CommandLineRunner initDatabase () {
    return args -> System.out.println ("starting");
    //productService.saveBulkProducts ();
  }

}
