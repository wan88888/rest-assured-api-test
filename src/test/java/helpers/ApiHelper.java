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
     * 通用的请求执行方法
     * @param method HTTP方法
     * @param endpoint API端点
     * @param requestBody 请求体（可选）
     * @param spec 请求规范
     * @param queryParams 查询参数（可选）
     * @return Response对象
     */
    private static Response executeRequest(String method, String endpoint, Object requestBody, 
                                         RequestSpecification spec, Map<String, Object> queryParams) {
        String jsonBody = requestBody != null ? JsonUtils.toJson(requestBody) : null;
        LogUtils.logApiRequest(method, ApiConfig.getBaseUrl() + endpoint, jsonBody);
        
        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }
        
        if (jsonBody != null) {
            spec.body(jsonBody);
        }
        
        Response response = spec.when().request(method, endpoint);
        LogUtils.logApiResponse(response.getStatusCode(), response.getTime(), response.getBody().asString());
        
        return response;
    }
    
    /**
     * 重载方法：不带查询参数的请求执行
     */
    private static Response executeRequest(String method, String endpoint, Object requestBody, RequestSpecification spec) {
        return executeRequest(method, endpoint, requestBody, spec, null);
    }
    
    /**
     * 执行GET请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response get(String endpoint) {
        return executeRequest("GET", endpoint, null, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行带参数的GET请求
     * @param endpoint API端点
     * @param queryParams 查询参数
     * @return Response对象
     */
    public static Response get(String endpoint, Map<String, Object> queryParams) {
        return executeRequest("GET", endpoint, null, ApiConfig.getBaseRequestSpec(), queryParams);
    }
    
    /**
     * 执行POST请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response post(String endpoint, Object requestBody) {
        return executeRequest("POST", endpoint, requestBody, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行POST请求（JSON字符串）
     * @param endpoint API端点
     * @param jsonBody JSON字符串请求体
     * @return Response对象
     */
    public static Response post(String endpoint, String jsonBody) {
        return executeRequest("POST", endpoint, jsonBody, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行PUT请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response put(String endpoint, Object requestBody) {
        return executeRequest("PUT", endpoint, requestBody, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行PATCH请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response patch(String endpoint, Object requestBody) {
        return executeRequest("PATCH", endpoint, requestBody, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行DELETE请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response delete(String endpoint) {
        return executeRequest("DELETE", endpoint, null, ApiConfig.getBaseRequestSpec());
    }
    
    /**
     * 执行带认证的GET请求
     * @param endpoint API端点
     * @return Response对象
     */
    public static Response getWithAuth(String endpoint) {
        return executeRequest("GET", endpoint, null, ApiConfig.getAuthenticatedRequestSpec());
    }
    
    /**
     * 执行带认证的POST请求
     * @param endpoint API端点
     * @param requestBody 请求体对象
     * @return Response对象
     */
    public static Response postWithAuth(String endpoint, Object requestBody) {
        return executeRequest("POST", endpoint, requestBody, ApiConfig.getAuthenticatedRequestSpec());
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
        RequestSpecification spec = ApiConfig.getBaseRequestSpec();
        
        if (headers != null && !headers.isEmpty()) {
            spec.headers(headers);
        }
        
        return executeRequest(method, endpoint, requestBody, spec);
    }
}