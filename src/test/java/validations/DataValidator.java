package validations;

import io.restassured.response.Response;
import models.Comment;
import models.Post;
import models.User;
import org.testng.Assert;
import utils.JsonUtils;
import utils.LogUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据验证类，用于验证具体的业务数据模型
 */
public class DataValidator {
    
    // 邮箱格式正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    
    /**
     * 验证Post对象的基本属性
     * @param post Post对象
     */
    public static void validatePost(Post post) {
        LogUtils.logStep(1, "验证Post对象基本属性");
        
        Assert.assertNotNull(post, "Post对象不应为null");
        Assert.assertNotNull(post.getId(), "Post ID不应为null");
        Assert.assertTrue(post.getId() > 0, "Post ID应大于0");
        Assert.assertNotNull(post.getUserId(), "User ID不应为null");
        Assert.assertTrue(post.getUserId() > 0, "User ID应大于0");
        Assert.assertNotNull(post.getTitle(), "Post标题不应为null");
        Assert.assertFalse(post.getTitle().trim().isEmpty(), "Post标题不应为空");
        Assert.assertNotNull(post.getBody(), "Post内容不应为null");
        Assert.assertFalse(post.getBody().trim().isEmpty(), "Post内容不应为空");
        
        LogUtils.logValidation("Post验证", "有效", "有效", true);
    }
    
    /**
     * 验证User对象的基本属性
     * @param user User对象
     */
    public static void validateUser(User user) {
        LogUtils.logStep(1, "验证User对象基本属性");
        
        Assert.assertNotNull(user, "User对象不应为null");
        Assert.assertNotNull(user.getId(), "User ID不应为null");
        Assert.assertTrue(user.getId() > 0, "User ID应大于0");
        Assert.assertNotNull(user.getName(), "User名称不应为null");
        Assert.assertFalse(user.getName().trim().isEmpty(), "User名称不应为空");
        Assert.assertNotNull(user.getUsername(), "Username不应为null");
        Assert.assertFalse(user.getUsername().trim().isEmpty(), "Username不应为空");
        Assert.assertNotNull(user.getEmail(), "User邮箱不应为null");
        Assert.assertTrue(isValidEmail(user.getEmail()), "User邮箱格式应正确");
        
        LogUtils.logValidation("User验证", "有效", "有效", true);
    }
    
    /**
     * 验证Comment对象的基本属性
     * @param comment Comment对象
     */
    public static void validateComment(Comment comment) {
        LogUtils.logStep(1, "验证Comment对象基本属性");
        
        Assert.assertNotNull(comment, "Comment对象不应为null");
        Assert.assertNotNull(comment.getId(), "Comment ID不应为null");
        Assert.assertTrue(comment.getId() > 0, "Comment ID应大于0");
        Assert.assertNotNull(comment.getPostId(), "Post ID不应为null");
        Assert.assertTrue(comment.getPostId() > 0, "Post ID应大于0");
        Assert.assertNotNull(comment.getName(), "Comment名称不应为null");
        Assert.assertFalse(comment.getName().trim().isEmpty(), "Comment名称不应为空");
        Assert.assertNotNull(comment.getEmail(), "Comment邮箱不应为null");
        Assert.assertTrue(isValidEmail(comment.getEmail()), "Comment邮箱格式应正确");
        Assert.assertNotNull(comment.getBody(), "Comment内容不应为null");
        Assert.assertFalse(comment.getBody().trim().isEmpty(), "Comment内容不应为空");
        
        LogUtils.logValidation("Comment验证", "有效", "有效", true);
    }
    
    /**
     * 验证Post列表
     * @param posts Post列表
     * @param expectedMinSize 期望的最小列表大小
     */
    public static void validatePostList(List<Post> posts, int expectedMinSize) {
        LogUtils.logStep(1, "验证Post列表");
        
        Assert.assertNotNull(posts, "Post列表不应为null");
        Assert.assertTrue(posts.size() >= expectedMinSize, 
                String.format("Post列表大小应至少为%d，实际为%d", expectedMinSize, posts.size()));
        
        for (Post post : posts) {
            validatePost(post);
        }
        
        LogUtils.logValidation("Post列表验证", "有效", "有效", true);
    }
    
    /**
     * 验证User列表
     * @param users User列表
     * @param expectedMinSize 期望的最小列表大小
     */
    public static void validateUserList(List<User> users, int expectedMinSize) {
        LogUtils.logStep(1, "验证User列表");
        
        Assert.assertNotNull(users, "User列表不应为null");
        Assert.assertTrue(users.size() >= expectedMinSize, 
                String.format("User列表大小应至少为%d，实际为%d", expectedMinSize, users.size()));
        
        for (User user : users) {
            validateUser(user);
        }
        
        LogUtils.logValidation("User列表验证", "有效", "有效", true);
    }
    
    /**
     * 验证Comment列表
     * @param comments Comment列表
     * @param expectedMinSize 期望的最小列表大小
     */
    public static void validateCommentList(List<Comment> comments, int expectedMinSize) {
        LogUtils.logStep(1, "验证Comment列表");
        
        Assert.assertNotNull(comments, "Comment列表不应为null");
        Assert.assertTrue(comments.size() >= expectedMinSize, 
                String.format("Comment列表大小应至少为%d，实际为%d", expectedMinSize, comments.size()));
        
        for (Comment comment : comments) {
            validateComment(comment);
        }
        
        LogUtils.logValidation("Comment列表验证", "有效", "有效", true);
    }
    
    /**
     * 从响应中验证Post对象
     * @param response 响应对象
     * @return Post对象
     */
    public static Post validateAndExtractPost(Response response) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        Post post = JsonUtils.fromJson(responseBody, Post.class);
        validatePost(post);
        
        return post;
    }
    
    /**
     * 从响应中验证User对象
     * @param response 响应对象
     * @return User对象
     */
    public static User validateAndExtractUser(Response response) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        User user = JsonUtils.fromJson(responseBody, User.class);
        validateUser(user);
        
        return user;
    }
    
    /**
     * 从响应中验证Comment对象
     * @param response 响应对象
     * @return Comment对象
     */
    public static Comment validateAndExtractComment(Response response) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        Comment comment = JsonUtils.fromJson(responseBody, Comment.class);
        validateComment(comment);
        
        return comment;
    }
    
    /**
     * 从响应中验证Post列表
     * @param response 响应对象
     * @param expectedMinSize 期望的最小列表大小
     * @return Post列表
     */
    public static List<Post> validateAndExtractPostList(Response response, int expectedMinSize) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        List<Post> posts = JsonUtils.fromJsonToList(responseBody, Post.class);
        validatePostList(posts, expectedMinSize);
        
        return posts;
    }
    
    /**
     * 从响应中验证User列表
     * @param response 响应对象
     * @param expectedMinSize 期望的最小列表大小
     * @return User列表
     */
    public static List<User> validateAndExtractUserList(Response response, int expectedMinSize) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        List<User> users = JsonUtils.fromJsonToList(responseBody, User.class);
        validateUserList(users, expectedMinSize);
        
        return users;
    }
    
    /**
     * 从响应中验证Comment列表
     * @param response 响应对象
     * @param expectedMinSize 期望的最小列表大小
     * @return Comment列表
     */
    public static List<Comment> validateAndExtractCommentList(Response response, int expectedMinSize) {
        ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
        
        String responseBody = response.getBody().asString();
        List<Comment> comments = JsonUtils.fromJsonToList(responseBody, Comment.class);
        validateCommentList(comments, expectedMinSize);
        
        return comments;
    }
    
    /**
     * 验证两个Post对象是否相等（忽略ID）
     * @param expected 期望的Post
     * @param actual 实际的Post
     */
    public static void validatePostEquals(Post expected, Post actual) {
        Assert.assertEquals(actual.getUserId(), expected.getUserId(), "User ID不匹配");
        Assert.assertEquals(actual.getTitle(), expected.getTitle(), "标题不匹配");
        Assert.assertEquals(actual.getBody(), expected.getBody(), "内容不匹配");
        
        LogUtils.logValidation("Post对象比较", "匹配", "匹配", true);
    }
    
    /**
     * 验证邮箱格式
     * @param email 邮箱地址
     * @return 是否为有效邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证ID是否有效
     * @param id ID值
     * @param fieldName 字段名称
     */
    public static void validateId(Integer id, String fieldName) {
        Assert.assertNotNull(id, fieldName + "不应为null");
        Assert.assertTrue(id > 0, fieldName + "应大于0");
    }
    
    /**
     * 验证字符串字段不为空
     * @param value 字符串值
     * @param fieldName 字段名称
     */
    public static void validateStringNotEmpty(String value, String fieldName) {
        Assert.assertNotNull(value, fieldName + "不应为null");
        Assert.assertFalse(value.trim().isEmpty(), fieldName + "不应为空");
    }
}