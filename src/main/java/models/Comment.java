package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Comment模型类，用于表示JSONPlaceholder API中的Comment数据结构
 */
public class Comment {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("postId")
    private Integer postId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("body")
    private String body;
    
    // 默认构造函数
    public Comment() {}
    
    // 带参数的构造函数
    public Comment(Integer postId, String name, String email, String body) {
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }
    
    public Comment(Integer id, Integer postId, String name, String email, String body) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }
    
    // Getter和Setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getPostId() {
        return postId;
    }
    
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Comment comment = (Comment) o;
        
        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (postId != null ? !postId.equals(comment.postId) : comment.postId != null) return false;
        if (name != null ? !name.equals(comment.name) : comment.name != null) return false;
        if (email != null ? !email.equals(comment.email) : comment.email != null) return false;
        return body != null ? body.equals(comment.body) : comment.body == null;
    }
    
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}