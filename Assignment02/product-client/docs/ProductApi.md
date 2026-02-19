# ProductApi

All URIs are relative to *https://api.example.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createProduct**](ProductApi.md#createProduct) | **POST** /product | Create a product, with the provided details |
| [**createProductWithHttpInfo**](ProductApi.md#createProductWithHttpInfo) | **POST** /product | Create a product, with the provided details |
| [**getProduct**](ProductApi.md#getProduct) | **GET** /products/{productId} | Get product by ID |
| [**getProductWithHttpInfo**](ProductApi.md#getProductWithHttpInfo) | **GET** /products/{productId} | Get product by ID |



## createProduct

> CreateProduct201Response createProduct(product)

Create a product, with the provided details

Create a product, with the provided details. Server creates the new product_id.

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.example.com/v1");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        ProductApi apiInstance = new ProductApi(defaultClient);
        Product product = new Product(); // Product | 
        try {
            CreateProduct201Response result = apiInstance.createProduct(product);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#createProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **product** | [**Product**](Product.md)|  | |

### Return type

[**CreateProduct201Response**](CreateProduct201Response.md)


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Product created successfully |  -  |
| **400** | Invalid input data |  -  |
| **500** | Internal server error |  -  |

## createProductWithHttpInfo

> ApiResponse<CreateProduct201Response> createProduct createProductWithHttpInfo(product)

Create a product, with the provided details

Create a product, with the provided details. Server creates the new product_id.

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.example.com/v1");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        ProductApi apiInstance = new ProductApi(defaultClient);
        Product product = new Product(); // Product | 
        try {
            ApiResponse<CreateProduct201Response> response = apiInstance.createProductWithHttpInfo(product);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#createProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **product** | [**Product**](Product.md)|  | |

### Return type

ApiResponse<[**CreateProduct201Response**](CreateProduct201Response.md)>


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Product created successfully |  -  |
| **400** | Invalid input data |  -  |
| **500** | Internal server error |  -  |


## getProduct

> Product getProduct(productId)

Get product by ID

Retrieve a product&#39;s details using its unique identifier

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.example.com/v1");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        ProductApi apiInstance = new ProductApi(defaultClient);
        Integer productId = 56; // Integer | Unique identifier for the product
        try {
            Product result = apiInstance.getProduct(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#getProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **Integer**| Unique identifier for the product | |

### Return type

[**Product**](Product.md)


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Product found successfully |  -  |
| **404** | Product not found |  -  |
| **500** | Internal server error |  -  |

## getProductWithHttpInfo

> ApiResponse<Product> getProduct getProductWithHttpInfo(productId)

Get product by ID

Retrieve a product&#39;s details using its unique identifier

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.example.com/v1");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        ProductApi apiInstance = new ProductApi(defaultClient);
        Integer productId = 56; // Integer | Unique identifier for the product
        try {
            ApiResponse<Product> response = apiInstance.getProductWithHttpInfo(productId);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#getProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **Integer**| Unique identifier for the product | |

### Return type

ApiResponse<[**Product**](Product.md)>


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Product found successfully |  -  |
| **404** | Product not found |  -  |
| **500** | Internal server error |  -  |

