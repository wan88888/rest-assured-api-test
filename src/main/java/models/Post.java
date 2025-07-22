package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Post模型类，用于表示JSONPlaceholder API中的Post数据结构
 */
public class Post {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("userId")
    private Integer userId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("body")
    private String body;
    
    // 默认构造函数
    public Post() {}
    
    // 带参数的构造函数
    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
    
    public Post(Integer id, Integer userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
    
    // Getter和Setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Post post = (Post) o;
        
        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (userId != null ? !userId.equals(post.userId) : post.userId != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        return body != null ? body.equals(post.body) : post.body == null;
    }
    
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}