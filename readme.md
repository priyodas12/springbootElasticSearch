# Spin up ElasticSearch Docker instance

- https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

### Development purpose using http:

go inside elastic container
search for elasticSearch.yml
update: xpack.security.enabled: false

post update of kibana dashboard
configure manually with http;//localhost:9200 not https

## Get Product By ID

- GET - http://localhost:12800/api/products/d2870395-5476-437f-8e04-57130ded853c

## GET All Products:

- GET - http://localhost:12800/api/products

## Search Products By field names(id,name,description,price):

- GET - http://localhost:12800/api/products/search/{fieldName}/query/{searchString}

## Delete All Products:

- DELETE - http://localhost:12800/api/products

## Create Bulk Amount Products:

- POST - http://localhost:12800/api/products/bulk