package tests;

import helpers.ApiHelper;
import utils.LogUtils;
import validations.DataValidator;
import validations.ResponseValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.lang.reflect.Method;

/**
 * 测试基类
 * 提供通用的测试设置和清理方法
 */
public abstract class BaseTest {
    
    protected ApiHelper apiHelper;
    protected ResponseValidator responseValidator;
    protected DataValidator dataValidator;
    
    @BeforeClass
    public void setUpClass() {
        LogUtils.logTestStart("Test Suite: " + this.getClass().getSimpleName(), "测试套件开始");
        initializeHelpers();
    }
    
    @AfterClass
    public void tearDownClass() {
        LogUtils.logTestEnd("Test Suite: " + this.getClass().getSimpleName(), "测试套件结束");
    }
    
    @BeforeMethod
    public void setUpMethod(Method method) {
        LogUtils.logTestStart("Test Method: " + method.getName(), "测试方法开始");
    }
    
    @AfterMethod
    public void tearDownMethod(Method method) {
        LogUtils.logTestEnd("Test Method: " + method.getName(), "测试方法结束");
    }
    
    /**
     * 初始化测试辅助类
     */
    private void initializeHelpers() {
        this.apiHelper = new ApiHelper();
        this.responseValidator = new ResponseValidator();
        this.dataValidator = new DataValidator();
    }
    
    /**
     * 通用的API响应验证
     * @param response API响应
     * @param expectedStatusCode 期望的状态码
     */
    protected void validateBasicResponse(io.restassured.response.Response response, int expectedStatusCode) {
        ResponseValidator.validateStatusCode(response, expectedStatusCode);
        ResponseValidator.validateResponseTime(response, 5000); // 默认5秒超时
        ResponseValidator.validateContentType(response, "application/json");
    }
    
    /**
     * 验证成功响应（200状态码）
     * @param response API响应
     */
    protected void validateSuccessResponse(io.restassured.response.Response response) {
        validateBasicResponse(response, 200);
        ResponseValidator.validateResponseBodyNotEmpty(response);
    }
    
    /**
     * 验证创建成功响应（201状态码）
     * @param response API响应
     */
    protected void validateCreatedResponse(io.restassured.response.Response response) {
        validateBasicResponse(response, 201);
        ResponseValidator.validateResponseBodyNotEmpty(response);
    }
    
    /**
     * 验证未找到响应（404状态码）
     * @param response API响应
     */
    protected void validateNotFoundResponse(io.restassured.response.Response response) {
        validateBasicResponse(response, 404);
    }
    
    /**
     * 验证错误请求响应（400状态码）
     * @param response API响应
     */
    protected void validateBadRequestResponse(io.restassured.response.Response response) {
        validateBasicResponse(response, 400);
    }
}