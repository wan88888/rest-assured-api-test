package validations;

import io.restassured.response.Response;
import org.testng.Assert;
import utils.HttpUtils;
import utils.JsonUtils;
import utils.LogUtils;

import java.util.List;

/**
 * 响应验证类，用于验证API响应的各种属性
 */
public class ResponseValidator {
    
    /**
     * 验证响应状态码
     * @param response 响应对象
     * @param expectedStatusCode 期望的状态码
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        LogUtils.logValidation("状态码", expectedStatusCode, actualStatusCode, actualStatusCode == expectedStatusCode);
        Assert.assertEquals(actualStatusCode, expectedStatusCode, 
                String.format("状态码验证失败。期望: %d, 实际: %d", expectedStatusCode, actualStatusCode));
    }
    
    /**
     * 验证响应时间
     * @param response 响应对象
     * @param maxResponseTime 最大响应时间（毫秒）
     */
    public static void validateResponseTime(Response response, long maxResponseTime) {
        long actualResponseTime = response.getTime();
        boolean isValid = actualResponseTime <= maxResponseTime;
        LogUtils.logValidation("响应时间", "<= " + maxResponseTime + "ms", actualResponseTime + "ms", isValid);
        Assert.assertTrue(isValid, 
                String.format("响应时间验证失败。期望: <= %dms, 实际: %dms", maxResponseTime, actualResponseTime));
    }
    
    /**
     * 验证响应头部
     * @param response 响应对象
     * @param headerName 头部名称
     * @param expectedValue 期望值
     */
    public static void validateHeader(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        LogUtils.logValidation("响应头 " + headerName, expectedValue, actualValue, expectedValue.equals(actualValue));
        Assert.assertEquals(actualValue, expectedValue, 
                String.format("响应头验证失败。头部: %s, 期望: %s, 实际: %s", headerName, expectedValue, actualValue));
    }
    
    /**
     * 验证响应头部是否存在
     * @param response 响应对象
     * @param headerName 头部名称
     */
    public static void validateHeaderExists(Response response, String headerName) {
        boolean exists = HttpUtils.hasHeader(response, headerName);
        LogUtils.logValidation("响应头存在性 " + headerName, "存在", exists ? "存在" : "不存在", exists);
        Assert.assertTrue(exists, String.format("响应头 %s 不存在", headerName));
    }
    
    /**
     * 验证Content-Type
     * @param response 响应对象
     * @param expectedContentType 期望的Content-Type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = HttpUtils.getContentType(response);
        boolean isValid = actualContentType != null && actualContentType.contains(expectedContentType);
        LogUtils.logValidation("Content-Type", expectedContentType, actualContentType, isValid);
        Assert.assertTrue(isValid, 
                String.format("Content-Type验证失败。期望包含: %s, 实际: %s", expectedContentType, actualContentType));
    }
    
    /**
     * 验证响应体不为空
     * @param response 响应对象
     */
    public static void validateResponseBodyNotEmpty(Response response) {
        boolean isEmpty = HttpUtils.isResponseBodyEmpty(response);
        LogUtils.logValidation("响应体非空", "非空", isEmpty ? "空" : "非空", !isEmpty);
        Assert.assertFalse(isEmpty, "响应体不应为空");
    }
    
    /**
     * 验证响应体为空
     * @param response 响应对象
     */
    public static void validateResponseBodyEmpty(Response response) {
        boolean isEmpty = HttpUtils.isResponseBodyEmpty(response);
        LogUtils.logValidation("响应体为空", "空", isEmpty ? "空" : "非空", isEmpty);
        Assert.assertTrue(isEmpty, "响应体应为空");
    }
    
    /**
     * 验证JSON响应中的字段值
     * @param response 响应对象
     * @param jsonPath JSON路径
     * @param expectedValue 期望值
     */
    public static void validateJsonField(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        LogUtils.logValidation("JSON字段 " + jsonPath, expectedValue, actualValue, expectedValue.equals(actualValue));
        Assert.assertEquals(actualValue, expectedValue, 
                String.format("JSON字段验证失败。路径: %s, 期望: %s, 实际: %s", jsonPath, expectedValue, actualValue));
    }
    
    /**
     * 验证JSON响应中的字段存在
     * @param response 响应对象
     * @param jsonPath JSON路径
     */
    public static void validateJsonFieldExists(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        boolean exists = value != null;
        LogUtils.logValidation("JSON字段存在性 " + jsonPath, "存在", exists ? "存在" : "不存在", exists);
        Assert.assertNotNull(value, String.format("JSON字段 %s 不存在", jsonPath));
    }
    
    /**
     * 验证JSON响应中的字段不存在
     * @param response 响应对象
     * @param jsonPath JSON路径
     */
    public static void validateJsonFieldNotExists(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        boolean notExists = value == null;
        LogUtils.logValidation("JSON字段不存在性 " + jsonPath, "不存在", notExists ? "不存在" : "存在", notExists);
        Assert.assertNull(value, String.format("JSON字段 %s 不应存在", jsonPath));
    }
    
    /**
     * 验证JSON数组长度
     * @param response 响应对象
     * @param jsonPath JSON路径
     * @param expectedSize 期望的数组长度
     */
    public static void validateJsonArraySize(Response response, String jsonPath, int expectedSize) {
        List<Object> array = response.jsonPath().getList(jsonPath);
        int actualSize = array != null ? array.size() : 0;
        LogUtils.logValidation("JSON数组长度 " + jsonPath, expectedSize, actualSize, actualSize == expectedSize);
        Assert.assertEquals(actualSize, expectedSize, 
                String.format("JSON数组长度验证失败。路径: %s, 期望: %d, 实际: %d", jsonPath, expectedSize, actualSize));
    }
    
    /**
     * 验证JSON数组不为空
     * @param response 响应对象
     * @param jsonPath JSON路径
     */
    public static void validateJsonArrayNotEmpty(Response response, String jsonPath) {
        List<Object> array = response.jsonPath().getList(jsonPath);
        boolean notEmpty = array != null && !array.isEmpty();
        LogUtils.logValidation("JSON数组非空 " + jsonPath, "非空", notEmpty ? "非空" : "空", notEmpty);
        Assert.assertTrue(notEmpty, String.format("JSON数组 %s 不应为空", jsonPath));
    }
    
    /**
     * 验证JSON响应格式是否正确
     * @param response 响应对象
     */
    public static void validateJsonFormat(Response response) {
        String responseBody = response.getBody().asString();
        boolean isValidJson = JsonUtils.isValidJson(responseBody);
        LogUtils.logValidation("JSON格式", "有效", isValidJson ? "有效" : "无效", isValidJson);
        Assert.assertTrue(isValidJson, "响应不是有效的JSON格式");
    }
    
    /**
     * 验证响应体包含指定文本
     * @param response 响应对象
     * @param expectedText 期望包含的文本
     */
    public static void validateResponseBodyContains(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        boolean contains = responseBody.contains(expectedText);
        LogUtils.logValidation("响应体包含文本", expectedText, contains ? "包含" : "不包含", contains);
        Assert.assertTrue(contains, String.format("响应体不包含期望的文本: %s", expectedText));
    }
    
    /**
     * 验证响应体不包含指定文本
     * @param response 响应对象
     * @param unexpectedText 不应包含的文本
     */
    public static void validateResponseBodyNotContains(Response response, String unexpectedText) {
        String responseBody = response.getBody().asString();
        boolean notContains = !responseBody.contains(unexpectedText);
        LogUtils.logValidation("响应体不包含文本", "不包含 " + unexpectedText, notContains ? "不包含" : "包含", notContains);
        Assert.assertTrue(notContains, String.format("响应体不应包含文本: %s", unexpectedText));
    }
    
    /**
     * 验证多个条件（组合验证）
     * @param response 响应对象
     * @param expectedStatusCode 期望状态码
     * @param maxResponseTime 最大响应时间
     * @param expectedContentType 期望Content-Type
     */
    public static void validateBasicResponse(Response response, int expectedStatusCode, 
                                           long maxResponseTime, String expectedContentType) {
        validateStatusCode(response, expectedStatusCode);
        validateResponseTime(response, maxResponseTime);
        validateContentType(response, expectedContentType);
        validateResponseBodyNotEmpty(response);
    }
}