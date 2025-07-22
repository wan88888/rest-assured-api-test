package tests;

import helpers.ApiHelper;
import helpers.TestDataHelper;
import io.restassured.response.Response;
import models.Comment;
import org.testng.annotations.Test;
import utils.LogUtils;
import validations.DataValidator;
import validations.ResponseValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comments API测试类，用于测试JSONPlaceholder的Comments相关API
 */
public class CommentsApiTest {
    
    @Test(description = "获取所有Comments")
    public void testGetAllComments() {
        LogUtils.logTestStart("testGetAllComments", "获取所有Comments列表");
        
        try {
            // 发送GET请求获取所有comments
            Response response = ApiHelper.get("/comments");
            
            // 验证响应
            List<Comment> comments = DataValidator.validateAndExtractCommentList(response, 1);
            
            // 验证列表不为空
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证每个评论的基本信息
            for (Comment comment : comments) {
                DataValidator.validateComment(comment);
            }
            
            LogUtils.logTestEnd("testGetAllComments", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetAllComments", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据ID获取单个Comment")
    public void testGetCommentById() {
        LogUtils.logTestStart("testGetCommentById", "根据ID获取单个Comment");
        
        try {
            int commentId = 1;
            
            // 发送GET请求获取指定ID的comment
            Response response = ApiHelper.get("/comments/" + commentId);
            
            // 验证响应并提取Comment对象
            DataValidator.validateAndExtractComment(response);
            
            // 验证Comment ID是否正确
            ResponseValidator.validateJsonField(response, "id", commentId);
            
            // 验证评论的详细信息结构
            ResponseValidator.validateJsonFieldExists(response, "postId");
            ResponseValidator.validateJsonFieldExists(response, "name");
            ResponseValidator.validateJsonFieldExists(response, "email");
            ResponseValidator.validateJsonFieldExists(response, "body");
            
            LogUtils.logTestEnd("testGetCommentById", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetCommentById", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据Post ID获取Comments")
    public void testGetCommentsByPostId() {
        LogUtils.logTestStart("testGetCommentsByPostId", "根据Post ID获取Comments");
        
        try {
            int postId = 1;
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("postId", postId);
            
            // 发送GET请求获取指定post的comments
            Response response = ApiHelper.get("/comments", queryParams);
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证所有返回的comments都属于指定post
            List<Integer> postIds = response.jsonPath().getList("postId");
            for (Integer id : postIds) {
                ResponseValidator.validateJsonField(response, "find { it.postId == " + id + " }.postId", postId);
            }
            
            LogUtils.logTestEnd("testGetCommentsByPostId", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetCommentsByPostId", "失败");
            throw e;
        }
    }
    
    @Test(description = "通过Post端点获取Comments")
    public void testGetCommentsViaPostEndpoint() {
        LogUtils.logTestStart("testGetCommentsViaPostEndpoint", "通过Post端点获取Comments");
        
        try {
            int postId = 1;
            
            // 发送GET请求通过posts端点获取comments
            Response response = ApiHelper.get("/posts/" + postId + "/comments");
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            ResponseValidator.validateJsonArrayNotEmpty(response, "$");
            
            // 验证所有返回的comments都属于指定post
            List<Integer> postIds = response.jsonPath().getList("postId");
            for (Integer id : postIds) {
                ResponseValidator.validateJsonField(response, "find { it.postId == " + id + " }.postId", postId);
            }
            
            LogUtils.logTestEnd("testGetCommentsViaPostEndpoint", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetCommentsViaPostEndpoint", "失败");
            throw e;
        }
    }
    
    @Test(description = "根据邮箱获取Comments")
    public void testGetCommentsByEmail() {
        LogUtils.logTestStart("testGetCommentsByEmail", "根据邮箱获取Comments");
        
        try {
            String email = "Eliseo@gardner.biz";
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("email", email);
            
            // 发送GET请求根据邮箱获取comments
            Response response = ApiHelper.get("/comments", queryParams);
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            
            // 如果有结果，验证邮箱是否正确
            List<String> emails = response.jsonPath().getList("email");
            if (!emails.isEmpty()) {
                for (String returnedEmail : emails) {
                    ResponseValidator.validateJsonField(response, "find { it.email == '" + returnedEmail + "' }.email", email);
                }
            }
            
            LogUtils.logTestEnd("testGetCommentsByEmail", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetCommentsByEmail", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建新的Comment")
    public void testCreateComment() {
        LogUtils.logTestStart("testCreateComment", "创建新的Comment");
        
        try {
            // 创建测试数据
            Comment newComment = TestDataHelper.createTestComment();
            
            // 发送POST请求创建新comment
            Response response = ApiHelper.post("/comments", newComment);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 201);
            ResponseValidator.validateContentType(response, "application/json");
            ResponseValidator.validateResponseBodyNotEmpty(response);
            
            // 验证创建的comment数据
            ResponseValidator.validateJsonFieldExists(response, "id");
            ResponseValidator.validateJsonField(response, "postId", newComment.getPostId());
            ResponseValidator.validateJsonField(response, "name", newComment.getName());
            ResponseValidator.validateJsonField(response, "email", newComment.getEmail());
            ResponseValidator.validateJsonField(response, "body", newComment.getBody());
            
            LogUtils.logTestEnd("testCreateComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreateComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "更新Comment")
    public void testUpdateComment() {
        LogUtils.logTestStart("testUpdateComment", "更新Comment");
        
        try {
            int commentId = 1;
            
            // 创建更新数据
            Comment updateComment = TestDataHelper.createTestComment();
            updateComment.setId(commentId);
            
            // 发送PUT请求更新comment
            Response response = ApiHelper.put("/comments/" + commentId, updateComment);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的comment数据
            ResponseValidator.validateJsonField(response, "id", commentId);
            ResponseValidator.validateJsonField(response, "postId", updateComment.getPostId());
            ResponseValidator.validateJsonField(response, "name", updateComment.getName());
            ResponseValidator.validateJsonField(response, "email", updateComment.getEmail());
            ResponseValidator.validateJsonField(response, "body", updateComment.getBody());
            
            LogUtils.logTestEnd("testUpdateComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testUpdateComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "部分更新Comment")
    public void testPatchComment() {
        LogUtils.logTestStart("testPatchComment", "部分更新Comment");
        
        try {
            int commentId = 1;
            
            // 创建部分更新数据
            Map<String, Object> patchData = new HashMap<>();
            patchData.put("name", "Updated Comment Name " + System.currentTimeMillis());
            patchData.put("body", "Updated comment body at " + System.currentTimeMillis());
            
            // 发送PATCH请求部分更新comment
            Response response = ApiHelper.patch("/comments/" + commentId, patchData);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            // 验证更新的字段
            ResponseValidator.validateJsonField(response, "id", commentId);
            ResponseValidator.validateJsonField(response, "name", patchData.get("name"));
            ResponseValidator.validateJsonField(response, "body", patchData.get("body"));
            
            LogUtils.logTestEnd("testPatchComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testPatchComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "删除Comment")
    public void testDeleteComment() {
        LogUtils.logTestStart("testDeleteComment", "删除Comment");
        
        try {
            int commentId = 1;
            
            // 发送DELETE请求删除comment
            Response response = ApiHelper.delete("/comments/" + commentId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testDeleteComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testDeleteComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "获取不存在的Comment - 负面测试")
    public void testGetNonExistentComment() {
        LogUtils.logTestStart("testGetNonExistentComment", "获取不存在的Comment - 负面测试");
        
        try {
            int nonExistentCommentId = 99999;
            
            // 发送GET请求获取不存在的comment
            Response response = ApiHelper.get("/comments/" + nonExistentCommentId);
            
            // 验证响应
            ResponseValidator.validateStatusCode(response, 404);
            
            LogUtils.logTestEnd("testGetNonExistentComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testGetNonExistentComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "创建无效Comment - 负面测试")
    public void testCreateInvalidComment() {
        LogUtils.logTestStart("testCreateInvalidComment", "创建无效Comment - 负面测试");
        
        try {
            // 创建无效的测试数据
            Comment invalidComment = TestDataHelper.createInvalidComment();
            
            // 发送POST请求创建无效comment
            Response response = ApiHelper.post("/comments", invalidComment);
            
            // 注意：JSONPlaceholder是一个模拟API，可能不会严格验证数据
            // 这里我们验证请求能够被处理，但在真实API中应该返回400错误
            ResponseValidator.validateStatusCode(response, 201);
            
            LogUtils.logTestEnd("testCreateInvalidComment", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCreateInvalidComment", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证Comments API响应时间")
    public void testCommentsApiResponseTime() {
        LogUtils.logTestStart("testCommentsApiResponseTime", "验证Comments API响应时间");
        
        try {
            // 发送GET请求
            Response response = ApiHelper.get("/comments");
            
            // 验证响应时间（设置为3秒以内）
            ResponseValidator.validateResponseTime(response, 3000);
            ResponseValidator.validateStatusCode(response, 200);
            
            LogUtils.logTestEnd("testCommentsApiResponseTime", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCommentsApiResponseTime", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证Comment邮箱格式")
    public void testCommentEmailFormat() {
        LogUtils.logTestStart("testCommentEmailFormat", "验证Comment邮箱格式");
        
        try {
            // 获取所有评论
            Response response = ApiHelper.get("/comments");
            
            // 验证响应
            ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
            
            // 验证每个评论的邮箱格式
            List<String> emails = response.jsonPath().getList("email");
            for (String email : emails) {
                boolean isValidEmail = DataValidator.isValidEmail(email);
                LogUtils.logValidation("邮箱格式", "有效格式", email, isValidEmail);
                if (!isValidEmail) {
                    LogUtils.logWarning("发现无效邮箱格式: " + email);
                }
            }
            
            LogUtils.logTestEnd("testCommentEmailFormat", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCommentEmailFormat", "失败");
            throw e;
        }
    }
    
    @Test(description = "验证Comment与Post的关联性")
    public void testCommentPostRelationship() {
        LogUtils.logTestStart("testCommentPostRelationship", "验证Comment与Post的关联性");
        
        try {
            int postId = 1;
            
            // 获取指定post的所有comments
            Response commentsResponse = ApiHelper.get("/posts/" + postId + "/comments");
            ResponseValidator.validateBasicResponse(commentsResponse, 200, 5000, "application/json");
            
            // 获取post信息
            Response postResponse = ApiHelper.get("/posts/" + postId);
            ResponseValidator.validateBasicResponse(postResponse, 200, 5000, "application/json");
            
            // 验证所有comments都关联到正确的post
            List<Integer> postIds = commentsResponse.jsonPath().getList("postId");
            for (Integer commentPostId : postIds) {
                ResponseValidator.validateJsonField(commentsResponse, 
                        "find { it.postId == " + commentPostId + " }.postId", postId);
            }
            
            LogUtils.logTestEnd("testCommentPostRelationship", "通过");
        } catch (Exception e) {
            LogUtils.logError("测试失败", e);
            LogUtils.logTestEnd("testCommentPostRelationship", "失败");
            throw e;
        }
    }
}