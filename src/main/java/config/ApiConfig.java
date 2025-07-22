package config;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * API配置类，用于设置Rest Assured的全局配置
 */
public class ApiConfig {
    private static final ConfigManager configManager = ConfigManager.getInstance();
    
    static {
        setupRestAssured();
    }
    
    /**
     * 设置Rest Assured全局配置
     */
    private static void setupRestAssured() {
        RestAssured.baseURI = configManager.getBaseUrl();
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", configManager.getTimeout())
                        .setParam("http.socket.timeout", configManager.getTimeout()));
        
        // 启用请求和响应日志
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    
    /**
     * 获取基础请求规范
     * @return RequestSpecification
     */
    public static RequestSpecification getBaseRequestSpec() {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }
    
    /**
     * 获取带认证的请求规范
     * @return RequestSpecification
     */
    public static RequestSpecification getAuthenticatedRequestSpec() {
        RequestSpecification spec = getBaseRequestSpec();
        
        String token = configManager.getAuthToken();
        if (token != null && !token.isEmpty()) {
            spec.header("Authorization", "Bearer " + token);
        }
        
        return spec;
    }
    
    /**
     * 获取基础URL
     * @return String
     */
    public static String getBaseUrl() {
        return configManager.getBaseUrl();
    }
    
    /**
     * 获取超时时间
     * @return int
     */
    public static int getTimeout() {
        return configManager.getTimeout();
    }
}