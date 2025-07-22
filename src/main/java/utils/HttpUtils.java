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
     * 检查HTTP状态码类型
     * @param statusCode 状态码
     * @param statusType 状态类型："success", "client_error", "server_error"
     * @return 是否匹配指定类型
     */
    public static boolean isStatusCodeType(int statusCode, String statusType) {
        switch (statusType.toLowerCase()) {
            case "success":
                return statusCode >= 200 && statusCode < 300;
            case "client_error":
                return statusCode >= 400 && statusCode < 500;
            case "server_error":
                return statusCode >= 500 && statusCode < 600;
            default:
                return false;
        }
    }
    
    /**
     * 检查HTTP状态码是否为成功状态
     * @param statusCode 状态码
     * @return 是否为成功状态
     */
    public static boolean isSuccessStatusCode(int statusCode) {
        return isStatusCodeType(statusCode, "success");
    }
    
    /**
     * 检查HTTP状态码是否为客户端错误
     * @param statusCode 状态码
     * @return 是否为客户端错误
     */
    public static boolean isClientError(int statusCode) {
        return isStatusCodeType(statusCode, "client_error");
    }
    
    /**
     * 检查HTTP状态码是否为服务器错误
     * @param statusCode 状态码
     * @return 是否为服务器错误
     */
    public static boolean isServerError(int statusCode) {
        return isStatusCodeType(statusCode, "server_error");
    }
    
    // 状态码描述映射表（静态常量，避免重复创建）
    private static final Map<Integer, String> STATUS_CODE_MAP = new HashMap<Integer, String>() {{
        put(200, "OK");
        put(201, "Created");
        put(204, "No Content");
        put(400, "Bad Request");
        put(401, "Unauthorized");
        put(403, "Forbidden");
        put(404, "Not Found");
        put(405, "Method Not Allowed");
        put(409, "Conflict");
        put(422, "Unprocessable Entity");
        put(500, "Internal Server Error");
        put(502, "Bad Gateway");
        put(503, "Service Unavailable");
    }};
    
    /**
     * 获取状态码描述
     * @param statusCode 状态码
     * @return 状态码描述
     */
    public static String getStatusCodeDescription(int statusCode) {
        return STATUS_CODE_MAP.getOrDefault(statusCode, "Unknown Status Code");
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