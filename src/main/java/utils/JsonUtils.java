package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类，用于处理JSON数据的序列化和反序列化
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 将对象转换为JSON字符串
     * @param object 要转换的对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("对象转换为JSON失败: {}", e.getMessage());
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为指定类型的对象
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("JSON转换为对象失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为List
     * @param json JSON字符串
     * @param clazz 列表元素类型
     * @param <T> 泛型类型
     * @return List对象
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            logger.error("JSON转换为List失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化为List失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为Map
     * @param json JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("JSON转换为Map失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化为Map失败", e);
        }
    }
    
    /**
     * 获取JSON节点
     * @param json JSON字符串
     * @return JsonNode
     */
    public static JsonNode getJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            logger.error("解析JSON节点失败: {}", e.getMessage());
            throw new RuntimeException("JSON解析失败", e);
        }
    }
    
    /**
     * 从JSON中提取指定路径的值
     * @param json JSON字符串
     * @param path JSON路径（如："user.name"）
     * @return 提取的值
     */
    public static String extractValue(String json, String path) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String[] pathParts = path.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String part : pathParts) {
                currentNode = currentNode.get(part);
                if (currentNode == null) {
                    return null;
                }
            }
            
            return currentNode.asText();
        } catch (IOException e) {
            logger.error("提取JSON值失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 验证JSON格式是否正确
     * @param json JSON字符串
     * @return 是否为有效JSON
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * 美化JSON字符串
     * @param json JSON字符串
     * @return 格式化后的JSON字符串
     */
    public static String prettyPrint(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (IOException e) {
            logger.error("JSON美化失败: {}", e.getMessage());
            return json;
        }
    }
}