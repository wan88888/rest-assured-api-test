package tests;

import helpers.ApiHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import models.Post;
import org.testng.annotations.Test;
import utils.LogUtils;
import validations.DataValidator;
import validations.ResponseValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Posts API测试类，用于测试JSONPlaceholder的Posts相关API
 */
public class PostsApiTest {
    
    @Test(description = "获取所有Posts")
    public void testGetAllPosts() {
        LogUtils.logTestStart("testGetAllPosts", "获取所有Posts列表");
        
        try {
            // 发送GET请求获取所有posts
            Response response = ApiHelper.get("/posts");
            
            // 验证响应
            List<Post> posts = DataValidator.validateAndExtractPostList(response, 1);
            
            // 验证列表不为空且包含预期数量的posts
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            LogUtils.logTestEnd("testGetAllPosts", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetAllPosts", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据ID获取单个Post")
    public void testGetPostById() {
        LogUtils.logTestStart("testGetPostById", "根据ID获取单个Post");
        
        try {
            int postId = 1;
            
            // 发送GET请求获取指定ID的post
            Response response = ApiHelper.get("/posts/" + postId);
            
            // 验证响应并提取Post对象
            Post post = DataValidator.validateAndExtractPost(response);
            
            // 验证Post ID是否正确
            ResponseValidator.validateJsonField(response, "id", postId);
            
            LogUtils.logTestEnd("testGetPostById", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetPostById", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据用户ID获取Posts")
    public void testGetPostsByUserId() {
        LogUtils.logTestStart("testGetPostsByUserId", "根据用户ID获取Posts");
        
        try {
            int userId = 1;
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("userId", userId);
            
            // 发送GET请求获取指定用户的posts
            Response response = ApiHelper.get("/posts", queryParams);
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证所有返回的posts都属于指定用户
            List<Integer> userIds = response.jsonPath().getList("userId");
            for (Integer id : userIds) {
                ResponseValidator.validateJsonField(response, "find { it.userId == " + id + " }.userId", userId);
            }
            
            LogUtils.logTestEnd("testGetPostsByUserId", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetPostsByUserId", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建新的Post")
    public void testCreatePost() {
        LogUtils.logTestStart("testCreatePost", "创建新的Post");
        
        try {
            // 创建测试数据
            Post newPost = TestDataHelper.createTestPost();
            
            // 发送POST请求创建新post
            Response response = ApiHelper.post("/posts", newPost);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 201);
            ResponseValidator.validateContentType(response, "application/json");
            ResponseValidator.validateResponseBodyNotEmpty(response);
            
            // 验证创建的post数据
            ResponseValidator.validateJsonFieldExists(response, "id");
            ResponseValidator.validateJsonField(response, "userId", newPost.getUserId());
            ResponseValidator.validateJsonField(response, "title", newPost.getTitle());
            ResponseValidator.validateJsonField(response, "body", newPost.getBody());
            
            LogUtils.logTestEnd("testCreatePost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreatePost", "失败");
            throw e;
        }
    }
    
    @Test(description = "更新Post")
    public void testUpdatePost() {
        LogUtils.logTestStart("testUpdatePost", "更新Post");
        
        try {
            int postId = 1;
            
            // 创建更新数据
            Post updatePost = TestDataHelper.createUpdatePost(postId, 1);
            
            // 发送PUT请求更新post
            Response response = ApiHelper.put("/posts/" + postId, updatePost);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的post数据
            ResponseValidator.validateJsonField(response, "id", postId);
            ResponseValidator.validateJsonField(response, "userId", updatePost.getUserId());
            ResponseValidator.validateJsonField(response, "title", updatePost.getTitle());
            ResponseValidator.validateJsonField(response, "body", updatePost.getBody());
            
            LogUtils.logTestEnd("testUpdatePost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testUpdatePost", "失败");
            throw e;
        }
    }
    
    @Test(description = "部分更新Post")
    public void testPatchPost() {
        LogUtils.logTestStart("testPatchPost", "部分更新Post");
        
        try {
            int postId = 1;
            
            // 创建部分更新数据
            Map<String, Object> patchData = new HashMap<>();
            patchData.put("title", "Updated Title " + System.currentTimeMillis());
            
            // 发送PATCH请求部分更新post
            Response response = ApiHelper.patch("/posts/" + postId, patchData);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的字段
            ResponseValidator.validateJsonField(response, "id", postId);
            ResponseValidator.validateJsonField(response, "title", patchData.get("title"));
            
            LogUtils.logTestEnd("testPatchPost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testPatchPost", "失败");
            throw e;
        }
    }
    
    @Test(description = "删除Post")
    public void testDeletePost() {
        LogUtils.logTestStart("testDeletePost", "删除Post");
        
        try {
            int postId = 1;
            
            // 发送DELETE请求删除post
            Response response = ApiHelper.delete("/posts/" + postId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testDeletePost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testDeletePost", "失败");
            throw e;
        }
    }
    
    @Test(description = "获取不存在的Post - 负面测试")
    public void testGetNonExistentPost() {
        LogUtils.logTestStart("testGetNonExistentPost", "获取不存在的Post - 负面测试");
        
        try {
            int nonExistentPostId = 99999;
            
            // 发送GET请求获取不存在的post
            Response response = ApiHelper.get("/posts/" + nonExistentPostId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 404);
            
            LogUtils.logTestEnd("testGetNonExistentPost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetNonExistentPost", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建无效Post - 负面测试")
    public void testCreateInvalidPost() {
        LogUtils.logTestStart("testCreateInvalidPost", "创建无效Post - 负面测试");
        
        try {
            // 创建无效的测试数据
            Post invalidPost = TestDataHelper.createInvalidPost();
            
            // 发送POST请求创建无效post
            Response response = ApiHelper.post("/posts", invalidPost);
            
            // 注意：JSONPlaceholder是一个模拟API，可能不会严格验证数据
            // 这里我们验证请求能够被处理，但在真实API中应该返回400错误
            ResponseValidator.validateStatusCode(response, 201);
            
            LogUtils.logTestEnd("testCreateInvalidPost", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreateInvalidPost", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证Posts API响应时间")
    public void testPostsApiResponseTime() {
        LogUtils.logTestStart("testPostsApiResponseTime", "验证Posts API响应时间");
        
        try {
            // 发送GET请求
            Response response = ApiHelper.get("/posts");
            
            // 验证响应时间（设置为3秒以内）
            ResponseValidator.validateResponseTime(response, 3000);
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testPostsApiResponseTime", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testPostsApiResponseTime", "失败");
            throw e;
        }
    }
}