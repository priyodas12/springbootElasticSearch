package com.elastic.springbootElasticSearch.controller;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elastic.springbootElasticSearch.model.Product;
import com.elastic.springbootElasticSearch.service.ProductService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping ("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping
  public ResponseEntity<Product> createProduct (@RequestBody Product product) throws IOException {
    Product savedProduct = productService.save (product);
    return ResponseEntity.ok (savedProduct);
  }

  @PostMapping ("/bulk")
  public ResponseEntity<Product> createBulkProduct () {
    productService.saveBulkProducts ();
    return ResponseEntity.noContent ().build ();
  }

  @GetMapping
  public ResponseEntity<Object> getAllProducts () {
    var result = productService.findAll ();
    var productSet = result.stream ().collect (Collectors.toSet ());
    productSet.forEach (i -> log.info ("product:: {}", i));
    return ResponseEntity.ok (productSet);
  }

  @GetMapping ("/search/{fieldName}/query/{searchTerm}")
  public ResponseEntity<Set<Product>> getProductMetaData (
      @PathVariable ("fieldName") String fieldName,
      @PathVariable ("searchTerm") String searchTerm
                                                         ) throws IOException {
    log.info ("Searching with fieldName: {}, queryString: {}", fieldName, searchTerm);
    var results = productService.findProductByMetadata (fieldName, searchTerm);
    //results.forEach (i -> log.info ("customized search result: {}", i));
    var productSet =
        results.stream ().map (i -> {
          assert i.source () != null;
          return i.source ().to (Product.class);
        }).collect (Collectors.toSet ());
    return ResponseEntity.ok (productSet);
  }


  @DeleteMapping ("/{id}")
  public ResponseEntity<Void> deleteProduct (@PathVariable UUID id) {
    productService.deleteById (id);
    return ResponseEntity.noContent ().build ();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteProducts () {
    productService.deleteAllProducts ();
    return ResponseEntity.noContent ().build ();
  }

  @GetMapping ("/{id}")
  public ResponseEntity<Product> getProduct (@PathVariable UUID id) {
    Product product = productService.findById (id);
    return ResponseEntity.ok (product);
  }


}
