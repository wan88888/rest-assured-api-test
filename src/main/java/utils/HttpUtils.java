package utils;

import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP工具类，用于处理HTTP相关的通用操作
 */
public class HttpUtils {
    
    /**
     * 检查HTTP状态码是否为成功状态
     * @param statusCode 状态码
     * @return 是否为成功状态
     */
    public static boolean isSuccessStatusCode(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * 检查HTTP状态码是否为客户端错误
     * @param statusCode 状态码
     * @return 是否为客户端错误
     */
    public static boolean isClientError(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }
    
    /**
     * 检查HTTP状态码是否为服务器错误
     * @param statusCode 状态码
     * @return 是否为服务器错误
     */
    public static boolean isServerError(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }
    
    /**
     * 获取状态码描述
     * @param statusCode 状态码
     * @return 状态码描述
     */
    public static String getStatusCodeDescription(int statusCode) {
        Map<Integer, String> statusCodeMap = new HashMap<>();
        statusCodeMap.put(200, "OK");
        statusCodeMap.put(201, "Created");
        statusCodeMap.put(204, "No Content");
        statusCodeMap.put(400, "Bad Request");
        statusCodeMap.put(401, "Unauthorized");
        statusCodeMap.put(403, "Forbidden");
        statusCodeMap.put(404, "Not Found");
        statusCodeMap.put(405, "Method Not Allowed");
        statusCodeMap.put(409, "Conflict");
        statusCodeMap.put(422, "Unprocessable Entity");
        statusCodeMap.put(500, "Internal Server Error");
        statusCodeMap.put(502, "Bad Gateway");
        statusCodeMap.put(503, "Service Unavailable");
        
        return statusCodeMap.getOrDefault(statusCode, "Unknown Status Code");
    }
    
    /**
     * 从响应中提取特定头部信息
     * @param response 响应对象
     * @param headerName 头部名称
     * @return 头部值
     */
    public static String extractHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }
    
    /**
     * 检查响应是否包含特定头部
     * @param response 响应对象
     * @param headerName 头部名称
     * @return 是否包含该头部
     */
    public static boolean hasHeader(Response response, String headerName) {
        return StringUtils.isNotBlank(response.getHeader(headerName));
    }
    
    /**
     * 获取响应的Content-Type
     * @param response 响应对象
     * @return Content-Type值
     */
    public static String getContentType(Response response) {
        return extractHeader(response, "Content-Type");
    }
    
    /**
     * 检查响应是否为JSON格式
     * @param response 响应对象
     * @return 是否为JSON格式
     */
    public static boolean isJsonResponse(Response response) {
        String contentType = getContentType(response);
        return contentType != null && contentType.contains("application/json");
    }
    
    /**
     * 检查响应是否为XML格式
     * @param response 响应对象
     * @return 是否为XML格式
     */
    public static boolean isXmlResponse(Response response) {
        String contentType = getContentType(response);
        return contentType != null && (contentType.contains("application/xml") || contentType.contains("text/xml"));
    }
    
    /**
     * 构建查询参数字符串
     * @param params 参数Map
     * @return 查询参数字符串
     */
    public static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(entry.getKey()).append("=").append(entry.getValue());
        }
        
        return queryString.toString();
    }
    
    /**
     * 验证响应时间是否在可接受范围内
     * @param response 响应对象
     * @param maxResponseTime 最大响应时间（毫秒）
     * @return 是否在可接受范围内
     */
    public static boolean isResponseTimeAcceptable(Response response, long maxResponseTime) {
        return response.getTime() <= maxResponseTime;
    }
    
    /**
     * 获取响应大小（字节）
     * @param response 响应对象
     * @return 响应大小
     */
    public static long getResponseSize(Response response) {
        String responseBody = response.getBody().asString();
        return responseBody != null ? responseBody.getBytes().length : 0;
    }
    
    /**
     * 检查响应体是否为空
     * @param response 响应对象
     * @return 响应体是否为空
     */
    public static boolean isResponseBodyEmpty(Response response) {
        String responseBody = response.getBody().asString();
        return StringUtils.isBlank(responseBody);
    }
}