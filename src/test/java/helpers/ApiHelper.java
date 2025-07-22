package helpers;

import config.ApiConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.JsonUtils;
import utils.LogUtils;

import java.util.Map;



/**
 * API助手类，用于封装通用的API请求操作
 */
public class ApiHelper {
    
    /**
     * 执行GET请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response get(String endpoint) {
        LogUtils.logApiRequest("GET", ApiConfig.getBaseUrl() + endpoint, null);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .when()
                .get(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行带参数的GET请求
     * @param endpoint API端点
     * @param queryParams 查询参数
     * @return Response对象
     */
    public static Response get(String endpoint, Map<String, Object> queryParams) {
        LogUtils.logApiRequest("GET", ApiConfig.getBaseUrl() + endpoint, null);
        
        RequestSpecification spec = ApiConfig.getBaseRequestSpec();
        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }
        
        Response response = spec.when().get(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行POST请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response post(String endpoint, Object requestBody) {
        String jsonBody = JsonUtils.toJson(requestBody);
        LogUtils.logApiRequest("POST", ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .body(jsonBody)
                .when()
                .post(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行POST请求（JSON字符串）
     * @param endpoint API端点
     * @param jsonBody JSON字符串请求体
     * @return Response对象
     */
    public static Response post(String endpoint, String jsonBody) {
        LogUtils.logApiRequest("POST", ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .body(jsonBody)
                .when()
                .post(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行PUT请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response put(String endpoint, Object requestBody) {
        String jsonBody = JsonUtils.toJson(requestBody);
        LogUtils.logApiRequest("PUT", ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .body(jsonBody)
                .when()
                .put(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行PATCH请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response patch(String endpoint, Object requestBody) {
        String jsonBody = JsonUtils.toJson(requestBody);
        LogUtils.logApiRequest("PATCH", ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .body(jsonBody)
                .when()
                .patch(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行DELETE请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response delete(String endpoint) {
        LogUtils.logApiRequest("DELETE", ApiConfig.getBaseUrl() + endpoint, null);
        
        Response response = ApiConfig.getBaseRequestSpec()
                .when()
                .delete(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行带认证的GET请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response getWithAuth(String endpoint) {
        LogUtils.logApiRequest("GET", ApiConfig.getBaseUrl() + endpoint, null);
        
        Response response = ApiConfig.getAuthenticatedRequestSpec()
                .when()
                .get(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行带认证的POST请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response postWithAuth(String endpoint, Object requestBody) {
        String jsonBody = JsonUtils.toJson(requestBody);
        LogUtils.logApiRequest("POST", ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        Response response = ApiConfig.getAuthenticatedRequestSpec()
                .body(jsonBody)
                .when()
                .post(endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
    
    /**
     * 执行自定义请求
     * @param method HTTP方法
     * @param endpoint API端点
     * @param requestBody 请求体（可选）
     * @param headers 自定义头部（可选）
     * @return Response对象
     */
    public static Response customRequest(String method, String endpoint, Object requestBody, Map<String, String> headers) {
        String jsonBody = requestBody != null ? JsonUtils.toJson(requestBody) : null;
        LogUtils.logApiRequest(method, ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        RequestSpecification spec = ApiConfig.getBaseRequestSpec();
        
        if (headers != null && !headers.isEmpty()) {
            spec.headers(headers);
        }
        
        if (jsonBody != null) {
            spec.body(jsonBody);
        }
        
        Response response = spec.when().request(method, endpoint);
        
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        return response;
    }
}