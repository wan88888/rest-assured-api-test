# Rest Assured API 测试自动化框架

基于 Rest Assured 构建的可扩展 API 测试自动化框架，用于测试 RESTful API 接口。

## 项目结构

```
rest-assured-api-test/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── config/          # 配置类
│   │       │   ├── ApiConfig.java
│   │       │   └── ConfigManager.java
│   │       ├── utils/           # 工具类
│   │       │   ├── HttpUtils.java
│   │       │   ├── JsonUtils.java
│   │       │   └── LogUtils.java
│   │       └── models/          # 数据模型
│   │           ├── Comment.java
│   │           ├── Post.java
│   │           └── User.java
│   └── test/
│       ├── java/
│       │   ├── tests/           # 测试类
│       │   │   ├── CommentsApiTest.java
│       │   │   ├── PostsApiTest.java
│       │   │   └── UsersApiTest.java
│       │   ├── helpers/         # 辅助类
│       │   │   ├── ApiHelper.java
│       │   │   └── TestDataHelper.java
│       │   └── validations/     # 验证类
│       │       ├── DataValidator.java
│       │       └── ResponseValidator.java
│       └── resources/
│           ├── config.properties # 配置文件
│           ├── logback.xml      # 日志配置
│           └── testng.xml       # TestNG配置
├── logs/                        # 日志文件目录
├── reports/                     # 测试报告目录
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 功能特性

### 🚀 核心功能
- **可扩展架构**: 模块化设计，易于扩展和维护
- **配置管理**: 统一的配置管理，支持多环境配置
- **日志系统**: 完整的日志记录，包括请求/响应日志
- **数据验证**: 丰富的响应验证和数据验证方法
- **测试报告**: 详细的测试报告和结果分析

### 🛠️ 技术栈
- **Rest Assured**: API 测试核心框架
- **TestNG**: 测试执行框架
- **Jackson**: JSON 数据处理
- **SLF4J + Logback**: 日志框架
- **Maven**: 项目构建工具

### 📊 测试覆盖
- **CRUD 操作**: 完整的增删改查测试
- **参数化测试**: 支持查询参数和路径参数
- **负面测试**: 错误场景和边界条件测试
- **性能测试**: 响应时间验证
- **数据验证**: 业务数据完整性验证

## 快速开始

### 环境要求
- Java 8 或更高版本
- Maven 3.6 或更高版本
- 网络连接（用于访问 JSONPlaceholder API）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd rest-assured-api-test
   ```

2. **安装依赖**
   ```bash
   mvn clean install
   ```

3. **运行测试**
   ```bash
   # 运行所有测试
   mvn test
   
   # 运行特定测试类
   mvn test -Dtest=PostsApiTest
   
   # 运行特定测试方法
   mvn test -Dtest=PostsApiTest#testGetAllPosts
   ```

4. **使用 TestNG 配置运行**
   ```bash
   mvn test -DsuiteXmlFile=src/test/resources/testng.xml
   ```

## 配置说明

### 主配置文件 (config.properties)

```properties
# API 配置
api.base.url=https://jsonplaceholder.typicode.com
api.timeout.connection=10000
api.timeout.socket=10000

# 环境配置
test.environment=dev

# 认证配置（如需要）
api.auth.username=
api.auth.password=
api.auth.token=

# 测试数据配置
test.data.path=src/test/resources/testdata

# 日志配置
log.level=INFO
log.path=logs

# 报告配置
report.path=reports
report.name=api-test-report

# 重试配置
test.retry.count=2
test.retry.delay=1000
```

### 日志配置 (logback.xml)

框架提供了详细的日志配置，包括：
- **控制台输出**: 实时查看测试执行情况
- **文件日志**: 完整的测试执行记录
- **API 日志**: Rest Assured 请求/响应详情
- **错误日志**: 单独记录错误信息
- **测试结果**: 测试结果汇总

### TestNG 配置 (testng.xml)

```xml
<suite name="API Test Suite" parallel="methods" thread-count="3">
    <test name="API Tests">
        <classes>
            <class name="tests.PostsApiTest"/>
            <class name="tests.UsersApiTest"/>
            <class name="tests.CommentsApiTest"/>
        </classes>
    </test>
</suite>
```

## 使用指南

### 编写新的测试

1. **创建数据模型** (如需要)
   ```java
   // 在 models 包中创建 POJO 类
   public class YourModel {
       private int id;
       private String name;
       // getters, setters, constructors
   }
   ```

2. **创建测试类**
   ```java
   @Test(description = "测试描述")
   public void testYourApi() {
       LogUtils.logTestStart("testYourApi", "测试说明");
       
       try {
           // 发送请求
           Response response = ApiHelper.get("/your-endpoint");
           
           // 验证响应
           ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
           
           LogUtils.logTestEnd("testYourApi", "通过");
       } catch (Exception e) {
           LogUtils.logError("测试失败", e);
           LogUtils.logTestEnd("testYourApi", "失败");
           throw e;
       }
   }
   ```

3. **添加数据验证**
   ```java
   // 使用现有验证方法
   ResponseValidator.validateJsonField(response, "fieldName", expectedValue);
   ResponseValidator.validateJsonFieldExists(response, "fieldName");
   
   // 或创建自定义验证
   DataValidator.validateYourModel(yourModel);
   ```

### 扩展框架

1. **添加新的工具类**
   - 在 `utils` 包中创建新的工具类
   - 提供静态方法供测试使用

2. **添加新的验证方法**
   - 在 `validations` 包中扩展验证类
   - 创建可重用的验证逻辑

3. **添加新的辅助方法**
   - 在 `helpers` 包中扩展辅助类
   - 封装复杂的测试操作

## 测试示例

### Posts API 测试
```java
// 获取所有文章
@Test
public void testGetAllPosts() {
    Response response = ApiHelper.get("/posts");
    ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
    List<Post> posts = DataValidator.validateAndExtractPostList(response, 1);
}

// 创建新文章
@Test
public void testCreatePost() {
    Post newPost = TestDataHelper.createTestPost();
    Response response = ApiHelper.post("/posts", newPost);
    ResponseValidator.validateStatusCode(response, 201);
}
```

### Users API 测试
```java
// 根据用户名查询用户
@Test
public void testGetUserByUsername() {
    Map<String, Object> params = new HashMap<>();
    params.put("username", "Bret");
    Response response = ApiHelper.get("/users", params);
    ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
}
```

### Comments API 测试
```java
// 获取文章的评论
@Test
public void testGetCommentsByPostId() {
    Response response = ApiHelper.get("/posts/1/comments");
    ResponseValidator.validateBasicResponse(response, 200, 5000, "application/json");
    List<Comment> comments = DataValidator.validateAndExtractCommentList(response, 1);
}
```

## 报告和日志

### 测试报告
- **TestNG 报告**: `target/surefire-reports/index.html`
- **详细报告**: `reports/` 目录下的自定义报告

### 日志文件
- **主日志**: `logs/api-test.log`
- **API 请求日志**: `logs/api-requests.log`
- **错误日志**: `logs/errors.log`
- **测试结果**: `logs/test-results.log`

## 最佳实践

### 测试设计
1. **单一职责**: 每个测试方法只验证一个功能点
2. **独立性**: 测试之间不应有依赖关系
3. **可读性**: 使用描述性的测试名称和注释
4. **数据驱动**: 使用测试数据文件或数据提供者

### 错误处理
1. **异常捕获**: 适当处理和记录异常
2. **失败重试**: 对不稳定的测试启用重试机制
3. **详细日志**: 记录足够的信息用于问题诊断

### 性能考虑
1. **并行执行**: 合理配置并行测试数量
2. **连接池**: 复用 HTTP 连接
3. **超时设置**: 设置合适的超时时间

## 故障排除

### 常见问题

1. **连接超时**
   - 检查网络连接
   - 调整超时配置
   - 验证 API 端点可用性

2. **测试失败**
   - 查看详细日志
   - 验证测试数据
   - 检查 API 响应变化

3. **配置问题**
   - 验证配置文件路径
   - 检查配置项拼写
   - 确认环境变量设置

### 调试技巧

1. **启用详细日志**
   ```properties
   log.level=DEBUG
   ```

2. **单独运行测试**
   ```bash
   mvn test -Dtest=SpecificTest#specificMethod
   ```

3. **查看 Rest Assured 日志**
   - 检查 `logs/api-requests.log`
   - 分析请求和响应详情

## 贡献指南

1. **代码规范**: 遵循 Java 编码规范
2. **测试覆盖**: 为新功能添加相应测试
3. **文档更新**: 更新相关文档和注释
4. **版本控制**: 使用有意义的提交信息

## 许可证

本项目采用 MIT 许可证，详情请参阅 LICENSE 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件
- 创建 Pull Request

---

**注意**: 本框架使用 JSONPlaceholder (https://jsonplaceholder.typicode.com) 作为示例 API，这是一个免费的在线 REST API，用于测试和原型开发。在生产环境中，请替换为实际的 API 端点。