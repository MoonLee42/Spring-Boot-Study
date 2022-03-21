# Spring Boot 技術筆記

## 目錄

- [簡介](#簡介)
- [特性](#特性)
- [產生專案](#產生專案)
- [設定檔](#設定檔)
    - [環境變數調整設定檔](#環境變數調整設定檔)
    - [應用程式內取得設定檔](#應用程式內取得設定檔)
        - [參數設置輔助 Configuration Processor](#參數設置輔助-configuration-processor)
- [監控 Actuator](#監控-actuator)
    - [設定](#設定-1)
- [Logback](#logback)
- [連接資料庫](#連接資料庫)
    - [MySQL Connector/J](#mysql-connectorj)
    - [HikariCP](#hikaricp)
    - [MyBatis-Plus](#mybatis-plus)
        - [設定](#設定-2)
        - [分頁](#分頁)
            - [設定](#設定-3)
            - [使用](#使用)
        - [Enum](#enum)
            - [設定](#設定-4)
            - [使用](#使用-1)
- [Spring Web](#spring-web)
    - [Controller](#controller)
    - [Service](#service)
- [Spring Boot Validation](#spring-boot-validation)
    - [Group validation](#group-validation)

---

###### 參考資料
  * [Spring 官方文件 Spring Boot Features](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html)
  * [Spring 官方文件 "How-to" Guides](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html)
  * [iT邦幫忙 - 30天帶你潮玩Spring Boot Zone](https://ithelp.ithome.com.tw/users/20119569/ironman/2159)

## 簡介
Spring Boot是以Spring 4.0為核心設計的一個框架，繼承了Spring框架原有的優秀特性，並通過簡化設定使整個Spring應用程式的建立與開發更加快速便捷，旨在讓使用者輕鬆建立可獨立運作且耐用的Spring應用程式。  
簡化設定的方法是通過綁定使用的Spring平台和第三方libraries來避免版本衝突和依賴引用的不穩定性。

## 特性
1. 建立可獨立運作的Spring應用程式
1. 嵌入式Web container，不須佈署war檔案
1. 提供固定的dependencies _(spring-boot-starter-*)_ 以簡化設定與管理
1. 隨時用合適的預設設定自動設定Spring和第三方libraries
1. 提供隨時就緒的監控套件 _(spring-boot-starter-actuator)_
1. 不使用xml設定

## 產生專案
使用 IntelliJ IDEA 建立 Spring Boot 專案

步驟：

1. `New` -> `Projects` -> `Spring Initializr`
1. `Dependencies` 選取 `Web` -> `Spring Web`
1. 建立專案


## 設定檔
_設定檔用 YAML 或是 properties 皆可，以下以 YAML 為主_

* `application.yml` 為主要設定檔，存放變數的其他設定檔(Profiles)需命名為 `application-{PROFILE_NAME}.yml`，並於 `application.yml` 內加入 `spring.profiles.active` 設定，以切換運行版本
  >[**application.yml**](src/main/resources/application.yml)
  >```yml
  >  server:
  >    port: ${SERVER_PORT}
  >
  >  spring: 
  >    profiles:
  >      active: dev | production
  >```
  >[**application-dev.yml**](src/main/resources/application-dev.yml)
  >```yml
  >  SERVER_PORT: 8081
  >```
  >**application-production.yml**
  >```yml
  >  SERVER_PORT: 8088
  >```

* 或是在 `application.yml` 內以 `---` 分隔
  ```yml
    server:
      port: ${SERVER_PORT}
  
    spring:
      profiles:
        active: local
  
    ---
  
    spring:
      profiles: local
  
    SERVER_PORT: 8082
  ```

* 打包排除測試用設定檔
  >[**pom.xml**](pom.xml)
  >```xml
  ><build>
  >    <plugins>
  >        <plugin>
  >            <groupId>org.apache.maven.plugins</groupId>
  >            <artifactId>maven-jar-plugin</artifactId>
  >            <configuration>
  >                <excludes>
  >                    <exclude>**/application-dev.yml</exclude>
  >                </excludes>
  >            </configuration>
  >        </plugin>
  >    </plugins>
  ></build>
  >```

### 環境變數調整設定檔

* 環境變數的**key**與設定檔中的**property key**或是**placeholder**一致
* 優先使用順序  
  command參數(property key) > 系統環境變數(property key) > command參數(placeholder) > 系統環境變數(placeholder) > 設定檔

    >[**application.yml**](src/main/resources/application.yml)
    >```yml
    >  spring:
    >    profiles:
    >      active: dev
    >    datasource:
    >      url: ${MYSQL_URL}
    >```
    >
    >* 系統環境變數
    >  ```
    >    變數名稱: SPRING_PROFILES_ACTIVE
    >      變數值: dev
    >  ```
    >  ```
    >    變數名稱: MYSQL_URL
    >      變數值: jdbc:mysql://localhost:3306/sbs?serverTimezone=UTC
    >  ```
    >* command line
    >  ```bash
    >    java -jar -D"spring.profiles.active"=dev SpringBootStudy.jar
    >  ```
    >  ```bash
    >    java -jar -DMYSQL_URL="jdbc:mysql://localhost:3306/sbs?serverTimezone=UTC" SpringBootStudy.jar
    >  ```

### 應用程式內取得設定檔

* 使用 **`@Value`** _(org.springframework.beans.factory.annotation.Value)_
    * 設定 `${設定檔參數鍵值}` 或是 `${環境參數鍵值}`
    * 設定 getter
      >[**application.yml**](src/main/resources/application.yml)
      >```yml
      >  spring:
      >    application:
      >      name: SpringBootStudy
      >```
      >
      >[**SpringConfig.java**](src/main/java/com/example/SpringBootStudy/config/SpringConfig.java)
      >```java
      >  @Configuration
      >  @Getter
      >  public class SpringConfig {
      >      @Value("${spring.application.name}")
      >      private String applicationName;
      >      @Value("${JAVA_HOME}")
      >      private String javaHome;
      >  }
      >```

* 使用 **`@ConfigurationProperties`** _(org.springframework.boot.context.properties.ConfigurationProperties)_
    * 由 Spring 照階層將設定存放至物件
    * 加入參數 `prefix` 指定開始設定的階層
        * `prefix` 必須為小寫且以 `-` 分隔 (kebab case)
    * 設定 getter & setter
    * 下層物件也須設定 getter & setter
      >[**application.yml**](src/main/resources/application.yml)
      >```yml
      >  message:
      >    invitation:
      >      date: 2020/10/10 19:00:00
      >      place: Internationl Dark Sky Park
      >```
      >
      >[**Invitation.java**](src/main/java/com/example/SpringBootStudy/config/InvitationConfig.java)
      >```java
      >  @Configuration
      >  @ConfigurationProperties(prefix = "message.invitation")
      >  @Getter
      >  @Setter
      >  public class InvitationConfig {
      >      private Date date;
      >      private String place;
      >  }
      >```

* `@ConfigurationProperties` vs. `@Value`

    ||`@ConfigurationProperties`|`@Value`|
    |:--|:-:|:-:|
    |hierarchical binding|Yes|No|
    |Relaxed Binding|Yes|Limited|
    |Meta-data support (Configuration Processor)|Yes|No|
    |SpEL support|No|Yes|

#### 參數設置輔助 Configuration Processor

###### 參考資料
  * [Spring 官方文件 Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-configuration-metadata.html)

##### 設定

* 安裝
  >**pom.xml**
  >```
  >  <dependency>
  >    <groupId>org.springframework.boot</groupId>
  >    <artifactId>spring-boot-configuration-processor</artifactId>
  >    <optional>true</optional>
  >  </dependency>
  >```

## 監控 Actuator
查看程式運行狀況
1. SpringBoot運行的健康指標
1. 目前的properties屬性
1. ......(其他)

###### 參考資料
* [Spring 官方文件 Spring Boot Actuator: Production-ready Features](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html)

### 設定

  >[**pom.xml**](pom.xml)
  >```xml
  >  <dependency>
  >      <groupId>org.springframework.boot</groupId>
  >      <artifactId>spring-boot-starter-actuator</artifactId>
  >  </dependency>
  >```
設定完成後，SpringBoot運行時會自動開啟 `/actuator/health`和`/actuator/info`，可透過此endpoint查看運行狀況。
<br />若要開啟其他非預設的endpoint，需自行於application.yml中設定，且需加上Spring security做保護，避免重要資訊外洩。
<br />亦可改變`/actuator`的路徑：設定base-path。
  >[**application.yml**](src/main/resources/application.yml)
  >```yml
  >  management:
  >    health:
  >      db:
  >        enabled: true
  >    endpoints:
  >      web:
  >        exposure:
  >          include: "*" 
  >        base-path: /sbs/management
  >    endpoint:
  >      health:
  >        show-details: always
  >      shutdown:
  >        enabled: false
  >```




## Logback

***詳見 [Logback Study](https://github.com/melee42/Logback-Study)***

## 連接資料庫

### MySQL Connector/J

* 安裝
  >[**pom.xml**](pom.xml)
  >```xml
  >  <dependency>
  >      <groupId>mysql</groupId>
  >      <artifactId>mysql-connector-java</artifactId>
  >      <version>{MySQL Connector Version}</version>
  >      <scope>runtime</scope>
  >  </dependency>
  >```

### HikariCP

* 使用`spring-boot-starter-web`、`spring-boot-starter-jdbc` 或 `spring-boot-starter-data-jpa` 等等的**starters**都自動包含 `HikariCP` 的 dependency
* 與時間有關的設定皆使用 milliseconds
* 設定
  >[**application.yml**](src/main/resources/application.yml)
  >```yml
  >  spring:
  >    datasource:
  >      hikari:
  >        pool-name: SpringBootStudy-jdbc-pool
  >        connection-timeout: 
  >        idle-timeout: 
  >        max-lifetime: 
  >        minimum-idle: 
  >        maximum-pool-size: 
  >        validation-timeout: 
  >        connection-test-query: 
  >```

### MyBatis-Plus

###### 參考資料
* [MyBatis-Plus 官方簡介](https://baomidou.com/guide/)
* [MyBatis-Plus 官方程式範例](https://github.com/baomidou/mybatis-plus-samples)
* [MyBatis 官方文件](https://mybatis.org/mybatis-3/index.html)

#### 設定

* [**pom.xml**](pom.xml)
  >**增加 dependency**
  >```xml
  >  <dependency>
  >      <groupId>com.baomidou</groupId>
  >      <artifactId>mybatis-plus-boot-starter</artifactId>
  >      <version>${MyBatis Plus Version}</version>
  >  </dependency>
  >```
  >**增加 build resource**
  >```xml
  >  <build>
  >    <resources>
  >      <resource>
  >        <directory>src/main/java</directory>
  >        <includes>
  >          <include>**/*Mapper.xml</include>
  >        </includes>
  >      </resource>
  >    </resources>
  >  </build>
  >```

* 實作 Mapper
  >[**UserMapper.java**](src/main/java/com/example/SpringBootStudy/db/rmdb/mapper/UserMapper.java)
  >```java
  >public interface UserMapper extends BaseMapper<User> {
  >    Page<User> selectByName(String userName, Page<User> page);
  >}
  >```
  >[**UserMapper.xml**](src/main/java/com/example/SpringBootStudy/db/rmdb/mapper/UserMapper.xml)
  >```xml
  >  <?xml version="1.0" encoding="UTF-8" ?>
  >  <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
  >  <mapper namespace="com.example.SpringBootStudy.db.rmdb.mapper.UserMapper">
  >    <select id="selectByName" resultType="com.example.SpringBootStudy.db.rmdb.entity.User">
  >      SELECT * FROM `user`
  >      <where>
  >        <if test="userName != null and userName != ''">
  >          name LIKE CONCAT('%', #{userName}, '%')
  >        </if>
  >      </where>
  >  </select>
  >  </mapper
  >```

* 模糊查詢
    * `#{PARAM}` | `%${PARAM}%`  
      使用 `#{}` 會告知 MyBatis 開啟一個 PreparedStatement 參數，所以需要在參數送去mapper之前先在前後加上`%`。  
      使用 `${}` 會直接帶入參數，無法避免 SQL injection 問題，並不推薦。
    * `"%"#{PARAM}"%"`  
      MyBatis-Plus 的分頁套件在進行 SQL 的 COUNT 優化時會發生 parse 錯誤並改為用原生 SQL 執行 COUNT，原生 SQL 並不會產生什麼問題，執行正常。
    * `CONCAT('%', #{PARAM}, '%')`  
      使用 SQL CONCAT function 來加上 `%` ，執行正常。

#### 分頁

##### 設定
_版本 3.4.0_

  >[**MybatisConfig.java**](src/main/java/com/example/SpringBootStudy/config/MyBatisConfig.java)
  >```java
  >@Configuration
  >@MapperScan("com.example.SpringBootStudy.db.rmdb.mapper")
  >public class MyBatisConfig {
  >
  >    @Bean
  >    public MybatisPlusInterceptor mybatisPlusInterceptor() {
  >        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
  >        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
  >        return interceptor;
  >    }
  >
  >    public ConfigurationCustomizer configurationCustomizer() {
  >        return configuration -> configuration.setUseDeprecatedExecutor(false);
  >    }
  >
  >}
  >```

##### 使用
* **Page<T\>** _(com.baomidou.mybatisplus.extension.plugins.pagination.Page)_

  * 參數

    |參數|描述|預設|
    |--:|---|:-:|
    |current|當前分頁頁數|1|
    |size|每頁顯示筆數|10|
    |total|總頁數|0|
    |records|分頁資料 (List<T\>)|-|
    |isSearchCount|是否進行count查詢|true|


#### Enum

##### 設定

  >[**application.yml**](src/main/resources/application.yml)
  >```yml
  >mybatis-plus:
  >  type-enums-package: com.example.SpringBootStudy.enums
  >```

##### 使用

  >interface **IEnum<T\>** _(com.baomidou.mybatisplus.annotation.IEnum)_<br/><br/>
  >[**UserType.java**](src/main/java/com/example/SpringBootStudy/enums/UserType.java)
  >```java
  >public enum UserType implements IEnum<String> {
  >
  >    USER("1", "使用者"),
  >    VIP("2", "付費使用者"),
  >    ADMIN("3", "管理員");
  >
  >    private final String code;
  >    private final String description;
  >
  >    @Override
  >    @JsonValue
  >    public String getValue() {
  >        return this.code;
  >    }
  >}
  >```

  >**\@EnumValue** _(com.baomidou.mybatisplus.annotation.EnumValue)_<br/><br/>
  >[**Gender.java**](src/main/java/com/example/SpringBootStudy/enums/Gender.java)
  >```java
  >  public enum Gender {
  >
  >      FEMALE("F", "女"),
  >      MALE("M", "男"),
  >      UNKNOWN("U", "不願透漏");
  >
  >      @EnumValue
  >      @JsonValue
  >      private final String code;
  >      private final String description;
  >  }
  >```

## Spring Web

### Controller

* **\@RestController**
    * 自帶 **\@Controller** & **\@ResponseBody**
    * 適合不需要 View Resolver 的 Controller

* **\@PutMapping**<br />
  **\@GetMapping**<br />
  **\@PostMapping**<br />
  **\@PatchMapping**<br />
  **\@DeleteMapping**<br />
    * 自帶 **\@RequestMapping** 並各自對應 RequestMethod.**PUT** | **GET** | **POST** | **PATCH** | **DELETE**

        ||**\@RequestMapping**|**\@PutMapping**|
        |:-:|:-:|:-:|
        |ElementType|TYPE, METHOD|METHOD|

* **\@RequestParam**<br />
  **\@RequestBody**

    |**\@RequestParam**|**\@RequestBody**|
    |:-:|:-:|
    |query parameter<br />form data|request body|

### Service

* **\@Service**

## Spring Boot Validation

* **\@Valid**

* **\@NotNull**<br />
  **\@NotEmpty**<br />
  **\@NotBlank**<br />

* **\@Size**<br />
  **\@Pattern**

### Group validation
* [**UserController.java**](src/main/java/com/example/SpringBootStudy/controller/UserController.java)
* [**ParameterBindingExceptionHandler.java**](src/main/java/com/example/SpringBootStudy/controller/exception/ParameterBindingExceptionHandler.java)
* [**UserBO.java**](src/main/java/com/example/SpringBootStudy/model/bo/UserBO.java)
* [**validation**](src/main/java/com/example/SpringBootStudy/validation)
