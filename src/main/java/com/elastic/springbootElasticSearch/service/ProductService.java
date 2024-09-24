package com.elastic.springbootElasticSearch.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.elastic.springbootElasticSearch.dao.ProductRepository;
import com.elastic.springbootElasticSearch.model.Product;
import com.github.javafaker.Faker;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Faker faker;
  @Autowired
  private ElasticsearchClient elasticsearchClient;

  public Product save (Product product) throws IOException {
    saveProduct (product);
    return product;
  }

  public void saveProduct (Product product) throws IOException {
    // Index the product into the "products" index
    IndexResponse response = elasticsearchClient.index (i -> i.index ("products")
        .id (String.valueOf (product.getId ()))
        .document (product));

  }

  public Product findById (UUID id) {
    return productRepository.findById (id).orElse (null);
  }

  public void deleteAllProducts () {
    productRepository.deleteAll ();
  }

  public PageImpl<Product> findAll () {
    return (PageImpl<Product>) productRepository.findAll ();
  }

  public void deleteById (UUID id) {
    productRepository.deleteById (id);
  }

  public void saveBulkProducts () {
    getRandomProducts ().forEach (p -> {
      try {
        saveProduct (p);
      }
      catch (IOException e) {
        throw new RuntimeException (e);
      }
    });
  }

  private List<Product> getRandomProducts () {
    return IntStream.rangeClosed (1, 1090).mapToObj (i -> {
      return Product
          .builder ()
          .id (UUID.randomUUID ())
          .name (faker.beer ().name ())
          .category (faker.beer ().malt ())
          .description (faker.beer ().style ())
          .price (new Random ().nextDouble (100, 900))
          .build ();
    }).toList ();
  }

  public List<Hit<JsonData>> findProductByMetadata (String fieldName, String searchTerm)
      throws IOException {
    var indexName = "products";
    log.info ("Searching with index: {} fieldName: {}, queryString: {}", indexName, fieldName,
              searchTerm
             );
    return search (indexName, fieldName, searchTerm);
  }

  public List<Hit<JsonData>> search (String indexName, String fieldName, String searchTerm)
      throws IOException {
    SearchRequest request = SearchRequest.of (s -> s
                                                  .index (indexName)
                                                  .query (q -> q
                                                              .wildcard (w -> w
                                                                             .field (fieldName)
                                                                             .value ("*" + searchTerm + "*")
                                                                        )
                                                         )
                                             );

    SearchResponse<JsonData> response = elasticsearchClient.search (request, JsonData.class);
    log.info ("Exactly Total Hits: {}", response.hits ().total ());
    return response.hits ().hits ();
  }

  public List<Hit<JsonData>> exactSearch (String indexName, String fieldName, String searchTerm)
      throws IOException {
    SearchRequest request = SearchRequest.of (s -> s
                                                  .index (indexName)
                                                  .query (q -> q
                                                              .match (m -> m
                                                                          .field (fieldName)
                                                                          .query (searchTerm)
                                                                     )
                                                         )
                                             );

    SearchResponse<JsonData> response = elasticsearchClient.search (request, JsonData.class);
    log.info ("Total Hits: {}", response.hits ().total ());
    return response.hits ().hits ();
  }
}


