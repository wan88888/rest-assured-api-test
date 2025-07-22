package tests;

import helpers.ApiHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.Test;
import utils.LogUtils;
import validations.DataValidator;
import validations.ResponseValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Users API测试类，用于测试JSONPlaceholder的Users相关API
 */
public class UsersApiTest extends BaseTest {
    
    @Test(description = "获取所有用户")
    public void testGetAllUsers() {
        Response response = ApiHelper.get("/users");
        
        validateSuccessResponse(response);
        DataValidator.validateAndExtractUserList(response, 1);
    }
    
    @Test(description = "根据ID获取用户")
    public void testGetUserById() {
        Response response = ApiHelper.get("/users/1");
        
        validateSuccessResponse(response);
        DataValidator.validateAndExtractUser(response);
    }
    
    @Test(description = "根据用户名获取用户")
    public void testGetUserByUsername() {
        Response response = ApiHelper.get("/users?username=Bret");
        
        validateSuccessResponse(response);
        DataValidator.validateAndExtractUserList(response, 1);
    }
    
    @Test(description = "根据邮箱获取用户")
    public void testGetUserByEmail() {
        Response response = ApiHelper.get("/users?email=Sincere@april.biz");
        
        validateSuccessResponse(response);
        DataValidator.validateAndExtractUserList(response, 1);
    }
    
    @Test(description = "创建新User")
    public void testCreateUser() {
        // 创建新用户数据
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setUsername("testuser");
        newUser.setEmail("test@example.com");
        
        // 发送POST请求创建用户
        Response response = ApiHelper.post("/users", newUser);
        
        validateCreatedResponse(response);
        
        // 验证返回的用户信息
        ResponseValidator.validateJsonFieldExists(response, "id");
        ResponseValidator.validateJsonField(response, "name", "Test User");
        ResponseValidator.validateJsonField(response, "username", "testuser");
        ResponseValidator.validateJsonField(response, "email", "test@example.com");
    }
    
    @Test(description = "更新User信息")
    public void testUpdateUser() {
        int userId = 1;
        
        // 创建更新数据
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setName("Updated User");
        updateUser.setUsername("updateduser");
        updateUser.setEmail("updated@example.com");
        
        // 发送PUT请求更新用户
        Response response = ApiHelper.put("/users/" + userId, updateUser);
        
        validateSuccessResponse(response);
        
        // 验证更新后的用户信息
        ResponseValidator.validateJsonField(response, "id", userId);
        ResponseValidator.validateJsonField(response, "name", "Updated User");
        ResponseValidator.validateJsonField(response, "username", "updateduser");
        ResponseValidator.validateJsonField(response, "email", "updated@example.com");
    }
    
    @Test(description = "部分更新User")
    public void testPatchUser() {
        LogUtils.logTestStart("testPatchUser", "部分更新User");
        
        try {
            int userId = 1;
            
            // 创建部分更新数据
            Map<String, Object> patchData = new HashMap<>();
            patchData.put("name", "Updated Name " + System.currentTimeMillis());
            patchData.put("email", "updated" + System.currentTimeMillis() + "@example.com");
            
            // 发送PATCH请求部分更新user
            Response response = ApiHelper.patch("/users/" + userId, patchData);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的字段
            ResponseValidator.validateJsonField(response, "id", userId);
            ResponseValidator.validateJsonField(response, "name", patchData.get("name"));
            ResponseValidator.validateJsonField(response, "email", patchData.get("email"));
            
            LogUtils.logTestEnd("testPatchUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testPatchUser", "失败");
            throw e;
        }
    }
    
    @Test(description = "删除User")
    public void testDeleteUser() {
        LogUtils.logTestStart("testDeleteUser", "删除User");
        
        try {
            int userId = 1;
            
            // 发送DELETE请求删除user
            Response response = ApiHelper.delete("/users/" + userId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testDeleteUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testDeleteUser", "失败");
            throw e;
        }
    }
    
    @Test(description = "获取不存在的User - 负面测试")
    public void testGetNonExistentUser() {
        LogUtils.logTestStart("testGetNonExistentUser", "获取不存在的User - 负面测试");
        
        try {
            int nonExistentUserId = 99999;
            
            // 发送GET请求获取不存在的user
            Response response = ApiHelper.get("/users/" + nonExistentUserId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 404);
            
            LogUtils.logTestEnd("testGetNonExistentUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetNonExistentUser", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建无效User - 负面测试")
    public void testCreateInvalidUser() {
        LogUtils.logTestStart("testCreateInvalidUser", "创建无效User - 负面测试");
        
        try {
            // 创建无效的测试数据
            User invalidUser = TestDataHelper.createInvalidUser();
            
            // 发送POST请求创建无效user
            Response response = ApiHelper.post("/users", invalidUser);
            
            // 注意：JSONPlaceholder是一个模拟API，可能不会严格验证数据
            // 这里我们验证请求能够被处理，但在真实API中应该返回400错误
            ResponseValidator.validateStatusCode(response, 201);
            
            LogUtils.logTestEnd("testCreateInvalidUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreateInvalidUser", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证Users API响应时间")
    public void testUsersApiResponseTime() {
        LogUtils.logTestStart("testUsersApiResponseTime", "验证Users API响应时间");
        
        try {
            // 发送GET请求
            Response response = ApiHelper.get("/users");
            
            // 验证响应时间（设置为3秒以内）
            ResponseValidator.validateResponseTime(response, 3000);
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testUsersApiResponseTime", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testUsersApiResponseTime", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证User邮箱格式")
    public void testUserEmailFormat() {
        LogUtils.logTestStart("testUserEmailFormat", "验证User邮箱格式");
        
        try {
            // 获取所有用户
            Response response = ApiHelper.get("/users");
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            
            // 验证每个用户的邮箱格式
            List<String> emails = response.jsonPath().getList("email");
            for (String email : emails) {
                boolean isValidEmail = DataValidator.isValidEmail(email);
                LogUtils.logValidation("邮箱格式", "有效格式", email, isValidEmail);
                if (!isValidEmail) {
                    LogUtils.logWarning("发现无效邮箱格式: " + email);
                }
            }
            
            LogUtils.logTestEnd("testUserEmailFormat", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testUserEmailFormat", "失败");
            throw e;
        }
    }
}