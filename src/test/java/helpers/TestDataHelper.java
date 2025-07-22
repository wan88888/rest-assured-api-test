package helpers;

import models.Comment;
import models.Post;
import models.User;

import java.util.Random;

/**
 * 测试数据助手类，用于生成和管理测试数据
 */
public class TestDataHelper {
    private static final Random random = new Random();
    
    /**
     * 创建测试用的Post对象
     * @return Post对象
     */
    public static Post createTestPost() {
        return new Post(
                random.nextInt(10) + 1, // userId: 1-10
                "Test Post Title " + System.currentTimeMillis(),
                "This is a test post body created at " + System.currentTimeMillis()
        );
    }
    
    /**
     * 创建指定用户ID的测试Post对象
     * @param userId 用户ID
     * @return Post对象
     */
    public static Post createTestPost(Integer userId) {
        return new Post(
                userId,
                "Test Post Title for User " + userId + " - " + System.currentTimeMillis(),
                "This is a test post body for user " + userId + " created at " + System.currentTimeMillis()
        );
    }
    
    /**
     * 创建用于更新的Post对象
     * @param id Post ID
     * @param userId 用户ID
     * @return Post对象
     */
    public static Post createUpdatePost(Integer id, Integer userId) {
        return new Post(
                id,
                userId,
                "Updated Post Title " + System.currentTimeMillis(),
                "This is an updated post body at " + System.currentTimeMillis()
        );
    }
    
    /**
     * 创建测试用的User对象
     * @return User对象
     */
    public static User createTestUser() {
        long timestamp = System.currentTimeMillis();
        return new User(
                "Test User " + timestamp,
                "testuser" + timestamp,
                "testuser" + timestamp + "@example.com"
        );
    }
    
    /**
     * 创建测试用的Comment对象
     * @return Comment对象
     */
    public static Comment createTestComment() {
        return new Comment(
                random.nextInt(100) + 1, // postId: 1-100
                "Test Comment " + System.currentTimeMillis(),
                "testcommenter" + System.currentTimeMillis() + "@example.com",
                "This is a test comment created at " + System.currentTimeMillis()
        );
    }
    
    /**
     * 创建指定Post ID的测试Comment对象
     * @param postId Post ID
     * @return Comment对象
     */
    public static Comment createTestComment(Integer postId) {
        return new Comment(
                postId,
                "Test Comment for Post " + postId + " - " + System.currentTimeMillis(),
                "testcommenter" + System.currentTimeMillis() + "@example.com",
                "This is a test comment for post " + postId + " created at " + System.currentTimeMillis()
        );
    }
    
    /**
     * 创建无效的Post对象（用于负面测试）
     * @return Post对象
     */
    public static Post createInvalidPost() {
        return new Post(
                null, // 无效的userId
                "", // 空标题
                null // 空内容
        );
    }
    
    /**
     * 创建无效的User对象（用于负面测试）
     * @return User对象
     */
    public static User createInvalidUser() {
        return new User(
                "", // 空名称
                null, // 空用户名
                "invalid-email" // 无效邮箱
        );
    }
    
    /**
     * 创建无效的Comment对象（用于负面测试）
     * @return Comment对象
     */
    public static Comment createInvalidComment() {
        return new Comment(
                null, // 无效的postId
                "", // 空名称
                "invalid-email", // 无效邮箱
                null // 空内容
        );
    }
    
    /**
     * 生成随机字符串
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return result.toString();
    }
    
    /**
     * 生成随机邮箱地址
     * @return 随机邮箱地址
     */
    public static String generateRandomEmail() {
        return generateRandomString(8) + "@" + generateRandomString(5) + ".com";
    }
    
    /**
     * 生成随机整数
     * @param min 最小值
     * @param max 最大值
     * @return 随机整数
     */
    public static int generateRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * 生成长文本（用于测试边界条件）
     * @param length 文本长度
     * @return 长文本
     */
    public static String generateLongText(int length) {
        StringBuilder longText = new StringBuilder();
        String baseText = "This is a long text for testing purposes. ";
        
        while (longText.length() < length) {
            longText.append(baseText);
        }
        
        return longText.substring(0, length);
    }
}