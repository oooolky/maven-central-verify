# PaymentsApi

All URIs are relative to *https://api.example.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**processPayment**](PaymentsApi.md#processPayment) | **POST** /credit-card-authorizer/authorize | Authorize and process credit card payment, called by shoppinCart.checkout. Internal API |
| [**processPaymentWithHttpInfo**](PaymentsApi.md#processPaymentWithHttpInfo) | **POST** /credit-card-authorizer/authorize | Authorize and process credit card payment, called by shoppinCart.checkout. Internal API |



## processPayment

> void processPayment(processPaymentRequest)

Authorize and process credit card payment, called by shoppinCart.checkout. Internal API

Process payment for a shopping cart using credit card information

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.PaymentsApi;

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

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest(); // ProcessPaymentRequest | 
        try {
            apiInstance.processPayment(processPaymentRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#processPayment");
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
| **processPaymentRequest** | [**ProcessPaymentRequest**](ProcessPaymentRequest.md)|  | |

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
| **200** | Payment authorized successfully |  -  |
| **400** | Invalid payment information |  -  |
| **402** | Payment declined |  -  |
| **500** | Internal server error |  -  |

## processPaymentWithHttpInfo

> ApiResponse<Void> processPayment processPaymentWithHttpInfo(processPaymentRequest)

Authorize and process credit card payment, called by shoppinCart.checkout. Internal API

Process payment for a shopping cart using credit card information

### Example

```java
// Import classes:
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.auth.*;
import io.swagger.client.models.*;
import io.swagger.client.api.PaymentsApi;

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

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest(); // ProcessPaymentRequest | 
        try {
            ApiResponse<Void> response = apiInstance.processPaymentWithHttpInfo(processPaymentRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#processPayment");
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
| **processPaymentRequest** | [**ProcessPaymentRequest**](ProcessPaymentRequest.md)|  | |

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
| **200** | Payment authorized successfully |  -  |
| **400** | Invalid payment information |  -  |
| **402** | Payment declined |  -  |
| **500** | Internal server error |  -  |

