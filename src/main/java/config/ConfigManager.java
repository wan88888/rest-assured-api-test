package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理类，用于读取和管理配置文件
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("配置文件 config.properties 未找到");
            }
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败: " + e.getMessage(), e);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // API相关配置
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }
    
    public int getTimeout() {
        return getIntProperty("api.timeout", 30000);
    }
    
    public String getEnvironment() {
        return getProperty("environment", "test");
    }
    
    public String getAuthToken() {
        return getProperty("auth.token");
    }
    
    public String getUsername() {
        return getProperty("auth.username");
    }
    
    public String getPassword() {
        return getProperty("auth.password");
    }
    
    public int getMaxRetryCount() {
        return getIntProperty("max.retry.count", 3);
    }
    
    public int getRetryDelay() {
        return getIntProperty("retry.delay.ms", 1000);
    }
}