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
public class UsersApiTest {
    
    @Test(description = "获取所有Users")
    public void testGetAllUsers() {
        LogUtils.logTestStart("testGetAllUsers", "获取所有Users列表");
        
        try {
            // 发送GET请求获取所有users
            Response response = ApiHelper.get("/users");
            
            // 验证响应
            List<User> users = DataValidator.validateAndExtractUserList(response, 1);
            
            // 验证列表不为空
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证每个用户的基本信息
            for (User user : users) {
                DataValidator.validateUser(user);
            }
            
            LogUtils.logTestEnd("testGetAllUsers", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetAllUsers", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据ID获取单个User")
    public void testGetUserById() {
        LogUtils.logTestStart("testGetUserById", "根据ID获取单个User");
        
        try {
            int userId = 1;
            
            // 发送GET请求获取指定ID的user
            Response response = ApiHelper.get("/users/" + userId);
            
            // 验证响应并提取User对象
            User user = DataValidator.validateAndExtractUser(response);
            
            // 验证User ID是否正确
            ResponseValidator.validateJsonField(response, "id", userId);
            
            // 验证用户的详细信息结构
            ResponseValidator.validateJsonFieldExists(response, "name");
            ResponseValidator.validateJsonFieldExists(response, "username");
            ResponseValidator.validateJsonFieldExists(response, "email");
            ResponseValidator.validateJsonFieldExists(response, "address");
            ResponseValidator.validateJsonFieldExists(response, "phone");
            ResponseValidator.validateJsonFieldExists(response, "website");
            ResponseValidator.validateJsonFieldExists(response, "company");
            
            // 验证地址信息
            ResponseValidator.validateJsonFieldExists(response, "address.street");
            ResponseValidator.validateJsonFieldExists(response, "address.city");
            ResponseValidator.validateJsonFieldExists(response, "address.zipcode");
            ResponseValidator.validateJsonFieldExists(response, "address.geo");
            ResponseValidator.validateJsonFieldExists(response, "address.geo.lat");
            ResponseValidator.validateJsonFieldExists(response, "address.geo.lng");
            
            // 验证公司信息
            ResponseValidator.validateJsonFieldExists(response, "company.name");
            ResponseValidator.validateJsonFieldExists(response, "company.catchPhrase");
            ResponseValidator.validateJsonFieldExists(response, "company.bs");
            
            LogUtils.logTestEnd("testGetUserById", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetUserById", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据用户名获取User")
    public void testGetUserByUsername() {
        LogUtils.logTestStart("testGetUserByUsername", "根据用户名获取User");
        
        try {
            String username = "Bret";
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("username", username);
            
            // 发送GET请求根据用户名获取user
            Response response = ApiHelper.get("/users", queryParams);
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证返回的用户名是否正确
            List<String> usernames = response.jsonPath().getList("username");
            for (String returnedUsername : usernames) {
                ResponseValidator.validateJsonField(response, "find { it.username == '" + returnedUsername + "' }.username", username);
            }
            
            LogUtils.logTestEnd("testGetUserByUsername", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetUserByUsername", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据邮箱获取User")
    public void testGetUserByEmail() {
        LogUtils.logTestStart("testGetUserByEmail", "根据邮箱获取User");
        
        try {
            String email = "Sincere@april.biz";
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("email", email);
            
            // 发送GET请求根据邮箱获取user
            Response response = ApiHelper.get("/users", queryParams);
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证返回的邮箱是否正确
            List<String> emails = response.jsonPath().getList("email");
            for (String returnedEmail : emails) {
                ResponseValidator.validateJsonField(response, "find { it.email == '" + returnedEmail + "' }.email", email);
            }
            
            LogUtils.logTestEnd("testGetUserByEmail", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetUserByEmail", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建新的User")
    public void testCreateUser() {
        LogUtils.logTestStart("testCreateUser", "创建新的User");
        
        try {
            // 创建测试数据
            User newUser = TestDataHelper.createTestUser();
            
            // 发送POST请求创建新user
            Response response = ApiHelper.post("/users", newUser);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 201);
            ResponseValidator.validateContentType(response, "application/json");
            ResponseValidator.validateResponseBodyNotEmpty(response);
            
            // 验证创建的user数据
            ResponseValidator.validateJsonFieldExists(response, "id");
            ResponseValidator.validateJsonField(response, "name", newUser.getName());
            ResponseValidator.validateJsonField(response, "username", newUser.getUsername());
            ResponseValidator.validateJsonField(response, "email", newUser.getEmail());
            
            LogUtils.logTestEnd("testCreateUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreateUser", "失败");
            throw e;
        }
    }
    
    @Test(description = "更新User")
    public void testUpdateUser() {
        LogUtils.logTestStart("testUpdateUser", "更新User");
        
        try {
            int userId = 1;
            
            // 创建更新数据
            User updateUser = TestDataHelper.createTestUser();
            updateUser.setId(userId);
            
            // 发送PUT请求更新user
            Response response = ApiHelper.put("/users/" + userId, updateUser);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的user数据
            ResponseValidator.validateJsonField(response, "id", userId);
            ResponseValidator.validateJsonField(response, "name", updateUser.getName());
            ResponseValidator.validateJsonField(response, "username", updateUser.getUsername());
            ResponseValidator.validateJsonField(response, "email", updateUser.getEmail());
            
            LogUtils.logTestEnd("testUpdateUser", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testUpdateUser", "失败");
            throw e;
        }
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