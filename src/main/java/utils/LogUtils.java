package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 日志工具类，用于统一管理测试日志记录
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    
    /**
     * 记录测试开始信息
     * @param testName 测试名称
     * @param description 测试描述
     */
    public static void logTestStart(String testName, String description) {
        MDC.put("testName", testName);
        logger.info("========== 测试开始 ===========");
        logger.info("测试名称: {}", testName);
        logger.info("测试描述: {}", description);
        logger.info("开始时间: {}", java.time.LocalDateTime.now());
    }
    
    /**
     * 记录测试结束信息
     * @param testName 测试名称
     * @param result 测试结果
     */
    public static void logTestEnd(String testName, String result) {
        logger.info("测试结果: {}", result);
        logger.info("结束时间: {}", java.time.LocalDateTime.now());
        logger.info("========== 测试结束 ===========");
        MDC.remove("testName");
    }
    
    /**
     * 记录API请求信息
     * @param method HTTP方法
     * @param url 请求URL
     * @param requestBody 请求体
     */
    public static void logApiRequest(String method, String url, String requestBody) {
        logger.info("API请求 - 方法: {}, URL: {}", method, url);
        if (requestBody != null && !requestBody.isEmpty()) {
            logger.info("请求体: {}", requestBody);
        }
    }
    
    /**
     * 记录API响应信息
     * @param statusCode 状态码
     * @param responseTime 响应时间
     * @param responseBody 响应体
     */
    public static void logApiResponse(int statusCode, long responseTime, String responseBody) {
        logger.info("API响应 - 状态码: {}, 响应时间: {}ms", statusCode, responseTime);
        if (responseBody != null && !responseBody.isEmpty()) {
            logger.info("响应体: {}", responseBody);
        }
    }
    
    /**
     * 记录验证信息
     * @param field 验证字段
     * @param expected 期望值
     * @param actual 实际值
     * @param result 验证结果
     */
    public static void logValidation(String field, Object expected, Object actual, boolean result) {
        String status = result ? "通过" : "失败";
        logger.info("验证 [{}] - 期望: {}, 实际: {}, 结果: {}", field, expected, actual, status);
    }
    
    /**
     * 记录错误信息
     * @param message 错误消息
     * @param throwable 异常对象
     */
    public static void logError(String message, Throwable throwable) {
        logger.error("错误: {}", message, throwable);
    }
    
    /**
     * 记录警告信息
     * @param message 警告消息
     */
    public static void logWarning(String message) {
        logger.warn("警告: {}", message);
    }
    
    /**
     * 记录调试信息
     * @param message 调试消息
     */
    public static void logDebug(String message) {
        logger.debug("调试: {}", message);
    }
    
    /**
     * 记录步骤信息
     * @param stepNumber 步骤号
     * @param stepDescription 步骤描述
     */
    public static void logStep(int stepNumber, String stepDescription) {
        logger.info("步骤 {}: {}", stepNumber, stepDescription);
    }
    
    /**
     * 记录性能信息
     * @param operation 操作名称
     * @param duration 持续时间（毫秒）
     */
    public static void logPerformance(String operation, long duration) {
        logger.info("性能 - 操作: {}, 耗时: {}ms", operation, duration);
    }
    
    /**
     * 记录数据库操作信息
     * @param operation 操作类型
     * @param query 查询语句
     * @param result 操作结果
     */
    public static void logDatabaseOperation(String operation, String query, String result) {
        logger.info("数据库操作 - 类型: {}, 查询: {}, 结果: {}", operation, query, result);
    }
}