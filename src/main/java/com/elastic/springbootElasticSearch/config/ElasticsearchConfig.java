package com.elastic.springbootElasticSearch.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {

  @Bean
  public ElasticsearchClient elasticsearchClient ()
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    SSLContext sslContext = SSLContextBuilder.create ()
        .loadTrustMaterial ((chain, authType) -> true) // Trust all certificates
        .build ();

    // Create the low-level REST client
    RestClient restClient = RestClient.builder (
            new HttpHost ("172.27.117.192", 9200, "http")
                                               ).setHttpClientConfigCallback (
            httpClientBuilder -> httpClientBuilder
                .setSSLContext (sslContext)
                .setSSLHostnameVerifier (NoopHostnameVerifier.INSTANCE))
        .build ();

    // Create the transport layer with Jackson for JSON handling
    RestClientTransport transport = new RestClientTransport (
        restClient, new JacksonJsonpMapper ()
    );

    // Create the Elasticsearch client using the transport layer
    return new ElasticsearchClient (transport);
  }
}
