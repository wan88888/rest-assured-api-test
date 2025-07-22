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
    
    /**
     * 通用的类型转换方法
     * @param key 属性键
     * @param type 目标类型
     * @param defaultValue 默认值
     * @param <T> 泛型类型
     * @return 转换后的值
     */
    @SuppressWarnings("unchecked")
    public <T> T getTypedProperty(String key, Class<T> type, T defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            if (type == String.class) {
                return (T) value;
            } else if (type == Integer.class || type == int.class) {
                return (T) Integer.valueOf(value);
            } else if (type == Boolean.class || type == boolean.class) {
                return (T) Boolean.valueOf(value);
            } else if (type == Long.class || type == long.class) {
                return (T) Long.valueOf(value);
            } else if (type == Double.class || type == double.class) {
                return (T) Double.valueOf(value);
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        
        return defaultValue;
    }
    
    public int getIntProperty(String key) {
        return getTypedProperty(key, Integer.class, 0);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        return getTypedProperty(key, Integer.class, defaultValue);
    }
    
    public boolean getBooleanProperty(String key) {
        return getTypedProperty(key, Boolean.class, false);
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return getTypedProperty(key, Boolean.class, defaultValue);
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