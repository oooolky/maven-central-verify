# ShoppingCartApi

All URIs are relative to *https://api.example.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addItemsToCart**](ShoppingCartApi.md#addItemsToCart) | **POST** /shopping-carts/{shoppingCartId}/addItem | Add items to shopping cart |
| [**addItemsToCartWithHttpInfo**](ShoppingCartApi.md#addItemsToCartWithHttpInfo) | **POST** /shopping-carts/{shoppingCartId}/addItem | Add items to shopping cart |
| [**checkoutCart**](ShoppingCartApi.md#checkoutCart) | **POST** /shopping-carts/{shoppingCartId}/checkout | Checkout shopping cart |
| [**checkoutCartWithHttpInfo**](ShoppingCartApi.md#checkoutCartWithHttpInfo) | **POST** /shopping-carts/{shoppingCartId}/checkout | Checkout shopping cart |
| [**createShoppingCart**](ShoppingCartApi.md#createShoppingCart) | **POST** /shopping-cart | Create a new shopping cart |
| [**createShoppingCartWithHttpInfo**](ShoppingCartApi.md#createShoppingCartWithHttpInfo) | **POST** /shopping-cart | Create a new shopping cart |



## addItemsToCart

> void addItemsToCart(shoppingCartId, addItemsToCartRequest)

Add items to shopping cart

Add products with specified quantities to a shopping cart

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        Integer shoppingCartId = 56; // Integer | Unique identifier for the shopping cart
        AddItemsToCartRequest addItemsToCartRequest = new AddItemsToCartRequest(); // AddItemsToCartRequest | 
        try {
            apiInstance.addItemsToCart(shoppingCartId, addItemsToCartRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#addItemsToCart");
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
| **shoppingCartId** | **Integer**| Unique identifier for the shopping cart | |
| **addItemsToCartRequest** | [**AddItemsToCartRequest**](AddItemsToCartRequest.md)|  | |

### Return type


null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Items added to cart successfully |  -  |
| **400** | Invalid input data |  -  |
| **404** | Shopping cart or product not found |  -  |
| **500** | Internal server error |  -  |

## addItemsToCartWithHttpInfo

> ApiResponse<Void> addItemsToCart addItemsToCartWithHttpInfo(shoppingCartId, addItemsToCartRequest)

Add items to shopping cart

Add products with specified quantities to a shopping cart

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        Integer shoppingCartId = 56; // Integer | Unique identifier for the shopping cart
        AddItemsToCartRequest addItemsToCartRequest = new AddItemsToCartRequest(); // AddItemsToCartRequest | 
        try {
            ApiResponse<Void> response = apiInstance.addItemsToCartWithHttpInfo(shoppingCartId, addItemsToCartRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#addItemsToCart");
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
| **shoppingCartId** | **Integer**| Unique identifier for the shopping cart | |
| **addItemsToCartRequest** | [**AddItemsToCartRequest**](AddItemsToCartRequest.md)|  | |

### Return type


ApiResponse<Void>

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Items added to cart successfully |  -  |
| **400** | Invalid input data |  -  |
| **404** | Shopping cart or product not found |  -  |
| **500** | Internal server error |  -  |


## checkoutCart

> CheckoutCart200Response checkoutCart(shoppingCartId, checkoutCartRequest)

Checkout shopping cart

Process checkout for a shopping cart

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        Integer shoppingCartId = 56; // Integer | Unique identifier for the shopping cart
        CheckoutCartRequest checkoutCartRequest = new CheckoutCartRequest(); // CheckoutCartRequest | 
        try {
            CheckoutCart200Response result = apiInstance.checkoutCart(shoppingCartId, checkoutCartRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#checkoutCart");
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
| **shoppingCartId** | **Integer**| Unique identifier for the shopping cart | |
| **checkoutCartRequest** | [**CheckoutCartRequest**](CheckoutCartRequest.md)|  | |

### Return type

[**CheckoutCart200Response**](CheckoutCart200Response.md)


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Checkout processed successfully |  -  |
| **400** | Invalid shopping cart state |  -  |
| **404** | Shopping cart not found |  -  |
| **500** | Internal server error |  -  |

## checkoutCartWithHttpInfo

> ApiResponse<CheckoutCart200Response> checkoutCart checkoutCartWithHttpInfo(shoppingCartId, checkoutCartRequest)

Checkout shopping cart

Process checkout for a shopping cart

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        Integer shoppingCartId = 56; // Integer | Unique identifier for the shopping cart
        CheckoutCartRequest checkoutCartRequest = new CheckoutCartRequest(); // CheckoutCartRequest | 
        try {
            ApiResponse<CheckoutCart200Response> response = apiInstance.checkoutCartWithHttpInfo(shoppingCartId, checkoutCartRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#checkoutCart");
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
| **shoppingCartId** | **Integer**| Unique identifier for the shopping cart | |
| **checkoutCartRequest** | [**CheckoutCartRequest**](CheckoutCartRequest.md)|  | |

### Return type

ApiResponse<[**CheckoutCart200Response**](CheckoutCart200Response.md)>


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Checkout processed successfully |  -  |
| **400** | Invalid shopping cart state |  -  |
| **404** | Shopping cart not found |  -  |
| **500** | Internal server error |  -  |


## createShoppingCart

> CreateShoppingCart201Response createShoppingCart(createShoppingCartRequest)

Create a new shopping cart

Create a new shopping cart for a customer

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        CreateShoppingCartRequest createShoppingCartRequest = new CreateShoppingCartRequest(); // CreateShoppingCartRequest | 
        try {
            CreateShoppingCart201Response result = apiInstance.createShoppingCart(createShoppingCartRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#createShoppingCart");
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
| **createShoppingCartRequest** | [**CreateShoppingCartRequest**](CreateShoppingCartRequest.md)|  | |

### Return type

[**CreateShoppingCart201Response**](CreateShoppingCart201Response.md)


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Shopping cart created successfully |  -  |
| **400** | Invalid input data |  -  |
| **500** | Internal server error |  -  |

## createShoppingCartWithHttpInfo

> ApiResponse<CreateShoppingCart201Response> createShoppingCart createShoppingCartWithHttpInfo(createShoppingCartRequest)

Create a new shopping cart

Create a new shopping cart for a customer

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.ShoppingCartApi;

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

        ShoppingCartApi apiInstance = new ShoppingCartApi(defaultClient);
        CreateShoppingCartRequest createShoppingCartRequest = new CreateShoppingCartRequest(); // CreateShoppingCartRequest | 
        try {
            ApiResponse<CreateShoppingCart201Response> response = apiInstance.createShoppingCartWithHttpInfo(createShoppingCartRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ShoppingCartApi#createShoppingCart");
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
| **createShoppingCartRequest** | [**CreateShoppingCartRequest**](CreateShoppingCartRequest.md)|  | |

### Return type

ApiResponse<[**CreateShoppingCart201Response**](CreateShoppingCart201Response.md)>


### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth), [BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Shopping cart created successfully |  -  |
| **400** | Invalid input data |  -  |
| **500** | Internal server error |  -  |

