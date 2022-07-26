# 	Java Web

##  JDBC

Java程序中对应的类中的数据类型应该为包装类，防止存在默认值会造成影响

七个步骤

1. 获取Connection
2. 定义SQL：select * from tb_brand
3. 获取 PreparedStatement 对象
4. 设置参数
5. 执行SQL
6. 处理结果
7. 释放资源

简单的一个小运行

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbcTest {
    public static void main(String[] args) throws Exception {
        // 1.注册驱动
        // MySql 5 以后的此句以后都可以省略不写
        //Class.forName("com.mysql.jdbc.Driver");

        // 2.获取链接
        // url 中最后的 test 是你的数据库
        // url 是一个固定的写法 jdbc:mysql:表明是一个协议
        String url = "jdbc:mysql://127.0.01:3306/test";
        String username = "root";
        String password = "040416";
        Connection conn = DriverManager.getConnection(url, username, password);

        // 3.定义 sql 语句
        String sql="update score set math = 0 where id=2";
        
        // 4.获取执行sql语句的对象 statement
        Statement state = conn.createStatement();

        // 5.执行sql语句
        // 执行语句后返回一个所受影响的行数
        int count = state.executeUpdate(sql);

        // 6.处理结果
        System.out.println(count);

        // 7.释放资源
        state.close();
        conn.close();
    }
}

```

### API 详解

#### DriverManager

static Connection  getConnection(String url, String user , String password)；

- 参数

  1. url : 连接路径

     语法：jdbc:mysql:// ip 地址 : 端口号/数据库名称？参数键值对1&参数键值对2...

     实例：jdbc:mysql://127.0.0.1:3306 / test

     细节：

     - 如果连接的是本机 mysql 服务器，并且 mysql 服务默认端口是 3306 ，则 url 可以简写为：jdbc:mysql:// ip 地址 : 端口号/数据库名称?参数键值对...

     - 配置 useSSl=false，禁用安全连接方式，解决警告提示  eg：jdbc:mysql://127.0.0.1:3306/test?useSSL=false

  2. user : 用户名

  3. password ：密码

  


#### Connection

1. 获取执行SQL的对象

   - 普通执行SQL对象

     Statement creatStatement()		//有了statement对象以后才能够执行sql语句

   - 预编译SQL的执行SQL对象：防止SQL注入

     PrepareStatement  prepareStatement（sql）

   - 执行存储过程的对象			//此功能不常用

     CallableStatement prepareCall(sql)

2. 管理事务

   - MySql 事务管理

     ```mysql
     -- 开启事务：begin/start transaction
     -- 提交事务：commit
     -- 回滚事务：rollback
     ```

   - JDBC事务管理：Connection接口中定义了三个对应的方法

     ```java
     // 开启事务：setAutoCommit(boolean autoCommit):true 为自动提交事务，false为手动提交事务，即开启事务
     // 提交事务：commit()
     // 回滚事务：rollback()
     ```

     管理事务的 JAVA 代码
     是否做出提交或回滚的动作需要使用 try...catch 语句来执行  做出回滚动作时肯定时报出了错误

     ```java
     try{
         // 开启事务
         conn.setAutoCommit(false);
         //执行sql
         int count1=stmt.executeUpdate(sql1);
         //处理结果
         sout(count1);
         //执行sql
         int count2=stmt.executeUpdate(sql2);
         //处理结果
         sout(count2);
         //提交事务
         conn.commit();
     } catch (Exception e){
         // 回滚事务
         conn.rollback();
         e.printStackTrace();
     }
     ```

#### Statement

- Statement 作用

  执行SQL语句

  ```java
  int executeUpdate(sql):执行DML、DDL语句
  //返回值：(1)DML语句影响的行数(2)DDL语句执行后也可能返回0
  //DDL：用来定义数据库对象（数据库，表，字段）
  //DML：用来对数据库表中的数据进行增删改
  ResultSet executeQuery(sql):执行DQL语句
  //DQL：查询数据库
      
  ```


#### PrepareStatement  

- 作用

  预编译SQL语句并执行：预防SQL注入问题

  1. 获取preparedStatement对象

  ```java
  //SQL语句中的参数值，使用？占位符替代
  String sql="select * from user where username= ? and passwore=? ";
  //通过Connection对象获取，并传入对应的SQL语句
  PreparedStatement pstmt = conn.prepareStatement(sql);
  ```

    2.设置参数值

  ​	PreparedStatement 对象：setXxx(参数1，参数2)  :给 ? 赋值

  ​			Xxx：数据类型；如setString(参数1，参数2)

  ​			参数：参数1 表示 ? 的位置编号 从 1 开始

  ​					   参数2 表示 ? 的值

  3.执行SQL

  ​	executeUpdate()			//不在需要传递SQL

- SQL注入

  SQL注入使通过操作输入来修改事先定义好的SQL对象，用以达到执行代码对服务器进行攻击的方式	

## Maven基础知识

Maven的作用

- 标准化的项目结构
- 标准化的构建流程
- 方便的依赖管理

Maven 常用命令

- compile：编译
- clean：清理        删除编译生成的文件夹
- test：测试           运行test目录下的java文件
- package：打包   生成一个jar包
- install：安装       将你的项目生成jar包导入到你的仓库中去

### Maven坐标详解

- 什么是坐标？

  Maven中的坐标是资源的唯一标识

  使用坐标来定义项目或引入项目中需要的依赖

- Maven坐标主要自称

  groupid：定义当前Maven项目隶属组织名称（通常是域名反写）

  artifactid：定义当前Maven项目名称（通常是模块名称）

  version：定义当前项目版本号

  scope：控制jar包的作用范围，编译环境，测试环境，运行环境 

  ​		

### 依赖管理(导入jar包)

1. 在pom.xml中编写  <dependencies>  标签
2. 在  <dependencies>  标签中使用  <dependency>  引入坐标
3. 定义坐标的 groupId ，artifactId，version
4. 点击刷新按钮，使坐标生效

`pom.xml`文件通用格式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <!--下一句相当于项目的名称-->
    <artifactId>brand-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
	<!--设置为 war 包-->
    <packaging>war</packaging>

    <!--表明了 JDK 的版本-->
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>


    <dependencies>
        <!--导入坐标-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.10</version>
        </dependency>
    </dependencies>
    
    <!-- 不是必须的 忘记做什么用的了
  		<build>
            <plugins>
                <plugin>
                    <groupId>org.apache.tomcat.maven</groupId>
                    <artifactId>tomcat7-maven-plugin</artifactId>
                    <version>2.2</version>
                    <configuration>
                        <port>80</port>
                    </configuration>
                </plugin>
            </plugins>
        </build>
	-->
</project>
```



## MyBatis

[中文官网](https://mybatis.net.cn/)

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220714170642092.png" alt="image-20220714170642092" style="zoom:33%;" />

- MyBatis是一款优秀的**持久层框架**，用于简化JDBC开发

持久层：

1. 负责将数据保存到数据库的那一层代码
2. JavaEE三层架构：表现层、业务层、持久层

框架：

- 框架就是一个半成品软件，是一套可重用的、通用的、软件基础代码模型
- 在框架的基础之上构建软件编穹更加高效、规范、通用、可扩展

MyBatis免除了几乎所有的JDBC代码以及设置参数和获取结果集的工作

Mapper代理开发

[具体介绍视频](https://www.bilibili.com/video/BV1Qf4y1T7Hx?p=50&spm_id_from=333.1007.top_right_bar_window_history.content.click&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

- 目的

  - 解决原生方式中的硬编码
  - 简化后期执行SQL

- 步骤

  - 定义与SQL映射文件同名的Mapper接口，并且将Mapper接口和SQL映射文件放置在同一目录下
    （同一目录下并不是真正的同一目录 具体看上面介绍视频）

    <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624230921452.png" alt="image-20220624230921452" style="zoom: 40%;" />

  - 设置SQL映射文件的namespace属性为Mapper接口全限定名   

  - 在Mapper 接口中定义方法，方法名就是SQL映射文件中sql语句的id，并保持参数类型和返回值类型一致

  - 编码

    1. 通过SqlSession的getMapper方法获取Mapper接口的代理对象
    2. 调用对应方法完成sql的执行

实体类的固定用法

```java
//不用记！！！ 在
//1.加载mybatis核心配置文件，获取SqlSessionFactory
String resource="mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
//2.获取SqlSession对象，用他来执行sql
SqlSession sqlSession = sqlSessionFactory.openSession();
//3.获取userMapper接口的代理对象
userMapper us = sqlSession.getMapper(userMapper.class);
//执行方法
ArrayList<user> users = us.selectAll();
```

细节：如果Mapper接口名称和SQL映射文件名称相同，并在同一目录下，则可以使用包扫描的方式简化SQL映射文件的加载
在文件`mybatis-config.xml`内设置  同时也是 `mybatis-config.xml`的完整配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--
        environments:配置数据库连接环境信息，可以配置多个environment，通过default书行切换不同的environment
    -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!--数据库连接信息-->
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <!--说明连接的数据库是哪一个-->
                <property name="url" value="jdbc:mysql:///mybatis_demo?useSSL=false"/>
                <!--数据库账号密码-->
                <property name="username" value="root"/>
                <property name="password" value="040416"/>
            </dataSource>
        </environment>
    </environments>
    <!--加载SQL映射文件-->
    <mappers>
        <!--加载SQL映射文件-->
        <!--<Demo.mapper resource="Demo/Demo.mapper/userMapper.xml"/>-->
        <!--Mapper代理方式-->
        <!--此种方式可以扫描这个包下的全部xml文件 不用单一添加-->
        <package name="Demo.mapper"/>
    </mappers>
</configuration>
```

### 配置文件完成增删改查（XxxMapper）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
    namespace：名称空间，填入你对应的路径
-->

<mapper namespace="Demo.mapper.BrandMapper">

<!--下面的所有方法均写在这里面-->

</mapper>

```



- 查询所有数据

  ```xml
  // 方法一  
  <select id="selectAll" resultType="Demo.pojo.Brand">
          select
             id, brand_name as brandName, company_name companyName, ordered, description, status
          from test.tb_brand;
          <!--使用了起别名是因为与实体类中的名称不对应无法完成封装-->
      </select>
  ```

  ```xml
  // 方法二  使用映射（最好用的方法）
  <!-- id 是唯一标识 随便取名字 其他地方需要引用
  	 type 是映射的类型，支持别名-->
  <resultMap id="brandResultMap" type="Demo.pojo.Brand">
      	<!--
  			result 是普通字段去使用 
   			主键字段使用 id 关键字
  			column是字段的名称
  			property是实体类的属性名
          -->
          <result column="brand_name" property="brandName"/>
          <result column="company_name" property="companyName"/>
      </resultMap>
      <select id="selectAll" resultMap="brandResultMap">
    		<!--此处的resultType改为了resultMap-->
          select
              *
          from test.tb_brand;
      </select>
  ```

- 根据某一个字段查询

  ```xml
      <!--根据ID查询-->
      <!--
          参数占位符：
              1. #{}：可以防止sql注入问题
              2. ${}：存在sql注入问题
              3. 使用时机：
                  * 参数传递的时候：#{}	
                  * 表明或者列名不固定的情况下：${}  会存在SQL注入风险
      -->
      <select id="selectById" resultMap="brandResultMap">
          select *
          from test.tb_brand where id = #{id};
          <!--{id}应和接口中传入的参数名字一致-->
      </select>
  ```

- 多条件查询（占位符不能够随便写了）

  ```java
  // Mapper 接口中的方法 
  /*
  	@Param("status")int XXX 
  	传入的参数XXX去找xml语句#{status}中的status
      传入多个参数是为了准确的传参			*/
  List<Brand> selectByCondition(@Param("status")int status, @Param("companyName")String companyName,@Param("brandName")String brandName);
  ```

  ```java
   List<Brand> selectByCondition(Brand brand);
  // 如果你传入的参数正好属于一个对象的话
  // xml语句中#{Xxx}会去找实体类中的getXxx方法
  // 意味着参数的占位符名称要和类中的属性名一样，要不会找不到对应的参数
  ```

  ```java
      List<Brand> selectByCondition(Map map);
  //传入一个map集合 map集合的键的名称要与XML的占位符名称一致
  ```

  

  ```xml
     <!-- 上面中的@Param中的值为了方便参数的传递 对应的值去找对应的值-->
  <select id="selectByCondition" resultType="Demo.pojo.Brand">
          select *
          from test.tb_brand
          where status=#{status} 
              and brand_name like #{brandName}
              and company_name like #{companyName}
      <!--使用like连接完成模糊查询-->
      </select>
  ```

### 动态查询

[动态查询官方网址](https://mybatis.net.cn/dynamic-sql.html)

```xml
   <!--
		* if：条件判断
			* test：逻辑表达式
	-->
	<select id="selectByCondition" resultMap="brandResultMap">
        select *
        from test.tb_brand
        where
        <if test="status != null">
            status = #{status}
        </if>
        <if test="brandName !=null and brandName != '' ">
            and brand_name like #{brandName}
        </if>
        <if test="companyName != null and companyName != '' ">
            and company_name like #{companyName}
        </if>
	<!--
		这样会存在一个问题，当为传入status值时 sql语法会出现问题
		解决办法在下面
	-->
    </select>
```

```xml
<!--解决办法一  提取上面的where片段 加一个恒等式-->
	 where 1=1
        <if test="status != null">
            status = #{status}
        </if>
```

```xml
<!--
	解决办法二
	使用了where标签 更加方便
-->
    <select id="selectByCondition" resultMap="brandResultMap">
        select *
        from test.tb_brand
        <where>
            <if test="status != null">
                 status = #{status}
            </if>
            <if test="brandName !=null and brandName != '' ">
                 brand_name like #{brandName}
            </if>
            <if test="companyName != null and companyName != '' ">
                 company_name like #{companyName}
            </if>
        </where>

    </select>
```

##### 单条件

- 从多个条件中选择一个
- choose（when,otherwise)：选择。类似于java中的switch语句（when相当于case，otherwise相当于default，test相当于判断条件）

```xml
    <select id="selectByConditionSingle" resultMap="brandResultMap">
        select *
        from test.tb_brand
        where
        <choose>
            <when test="status!=null">
                status=#{status}
            </when>
            <when test="companyName!=null and companyName!='' ">
                company_name like #{companyName}
            </when>
            <when test="brandName!=null and brandName!=''">
                brand_name like #{brandName}
            </when>
            <otherwise>
                1=1
            </otherwise>
        </choose>
    </select>
```

```xml
<!--使用where标签更方便  省略了otherwise的用法  动态的查询-->
    <select id="selectByConditionSingle" resultMap="brandResultMap">
        select *
        from test.tb_brand
        <where>
            <choose>
                <when test="status!=null">
                    status=#{status}
                </when>
                <when test="companyName!=null and companyName!='' ">
                    company_name like #{companyName}
                </when>
                <when test="brandName!=null and brandName!=''">
                    brand_name like #{brandName}
                </when>

            </choose>
        </where>
	</select>
```

##### 添加

**SqlSession对象时不传参（默认开启事务）或者传入false时会打开事务然后需要手动提交事务**

##### 无返回值

```xml
    <insert id="add">
        insert into test.tb_brand(brand_name, company_name, ordered, description, status)
        values (#{brandName},#{companyName},#{ordered},#{description},#{status});
    </insert>

```

```java
/*2.获取SqlSession对象
	SqlSession sqlSession = sqlSessionFactory.openSession();*/	
//4.执行方法
	brandMapper.add(brands);
//5.提交事务
	sqlSession.commit(); 
//如果在步骤2得到SqlSession对象时不传参（默认开启事务）或者传入false时会打开事务然后需要手动提交事务
//在传入true时不需要手动提交事务
```

##### 主键返回

```xml
 <insert id="add" useGeneratedKeys="true" keyProperty="id">
<!-- 
    就可是使用对应的getId() 方法得到id
    keyProperty 指向了ID的属性名称
-->
```

##### 修改

- 修改全部字段

  ```xml
      <insert id="add" useGeneratedKeys="true" keyProperty="id">
          insert into test.tb_brand(brand_name, company_name, ordered, description, status)
          values (#{brandName}, #{companyName}, #{ordered}, #{description}, #{status});
      </insert>
  <!--如果对应的接口中返回值是int 那么将返回ying'xiang-->
  ```

  

- 修改动态字段

```xml
	<!--引入了set标签 防止出现语法错误-->
	<update id="update">
        update test.tb_brand
        <set>
            <if test="brandName != null and brandName != '' ">
                brand_name = #{brandName},
            </if>
            <if test="companyName != null and companyName != '' ">
                company_name= #{companyName},
            </if>
            <if test="ordered != null  ">
                ordered = #{ordered},
            </if>
            <if test="description != null and description != '' ">
                description = #{description},
            </if>
            <if test="status != null ">
                status = #{status}
            </if>
            where id = #{id};
        </set>
    </update>

```

#####   删除

- 单一条件删除

  ```xml
      <delete id="deleteById">
          delete
          from test.tb_brand
          where id = #{id};
      </delete>
  ```


- 动态删除

  ```xml
      <!--根据id批量删除-->
      <delete id="deleteByIds">
          delete
          from test.tb_brand
          where id in
          <foreach collection="ids" item="id" separator="," open="(" close=")">
              #{id}
          </foreach>;
      </delete>
      <!--open="(" close=")" 相当于是括号  去除这两个 在foreach便签外添加（）一样 separator代表分隔符 -->
  ```

### 注解完成增删改查

使用注解开发回避配置文件开发更加方便

```java
@Select("select * from tb_user where id = #{id}")
public User selectByld(int id);
//在xml中对数据库中的字段起了别名以后需要使用 @ResultMap("填入你的 resultMap 标签的属性 id 的名称")

```

- 查询：@Select
- 添加：@Insert
- 修改：@Update
- 删除：@Delete
- 别名：@ResultMap

提示：

- 注解完成简单功能（对于复杂的语句，java的注解力不从心，而且让sql语句更加混乱）
- 配置文件完成复杂功能

```java
// 使用注解完成一个查询案例
@Select("select * from tb_brand where id = #{id}")
Brand selectById(int id);
//不需要再在配置xml
```

## HTTP

规定了浏览器与服务器之间传输的规则。

**HTTP协议特点**

- 基于TCP协议：面向连接，安全
- 基于请求-响应模型的：一次请求对应一次响应
- HTTP协议是无状态的协议：对于事物处理没有记忆能力。每次请求-响应都是独立的
  - 缺点：多次请求间不能共享数据（Java中使用会话技术（Cookie，Session）来解决这个问题）
  - 优点：速度快

### 请求数据格式

1. 请求行：请求数据的第一行。其中GET表示请求方式，/便是请求资源路径，HTTP/1.1表示协议版本
2. 请求头：第二行开始，格式为key：value形式。（请求头可以有多行）
3. 请求体：POST请求的最后一部分，存放请求参数

GET请求和POST请求的方式

- GET请求请求参数在请求行中，没有请求体。POST请求请求参数在请求体中。
- GET请求请求参数大小有限制，POST没有。

GET请求

```http
GET / HTTP/1.1
Host: www.itcast.cn
Connection: keep-alive
Cache-Control: max-age=o Upgrade-lnsecure-Requests: 1
User-Agent: Mozilla/5.0 Chrome/91.0.4472.106
...
```

POST请求（最后一行为请求体）

```http
POST / HTTP/1.1
Host: www.itcast.cn
Connection: keep-alive
Cache-Control: max-age=0 Upgrade-lnsecure-Requests: 1
User-Agent: Mozilla/5.0 Chrome/91.0.4472.106

username=superbaby&password=123456

```



### 响应数据格式

[状态码大全](https://cloud.tencent.com/developer/chapter/13553
)

响应数据分为三部分：

1. 响应行：响应数据的第一行。其中HTTP/1.1表示协议版本，200便是响应状态码，OK表示状态码描述
2. 响应偷：第二行开始，格式为key：value形式
3. 响应体：最后一部分。存放响应数据

响应数据的格式：

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220622121701265.png" alt="image-20220622121701265" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220622121825135.png" width="50%" style="zoom:50%;" />

## WEB服务器 - Tomcat

​	web服务器是一个应用程序，对HTTP协议的操作进行封装（简化开发），使得程序员不必直接对协议进行操作，让web开发更加便捷。主要功能是“提供网上信息浏览服务”。
​	Tomcat是轻量级的web服务器，支持 Servlet/JSP 少量 JavaEE规范。

## Servlet

快速入门

1. 创建web项目，导入Serclet依赖坐标

   ```xml
           <dependency>
               <groupId>javax.servlet</groupId>
               <artifactId>javax.servlet-api</artifactId>
               <version>4.0.1</version>
               <!--下面这一句必须加-->
               <scope>provided</scope>
           </dependency>
   ```

2. 创建：定义一个类，实现Servlet接口，并重写接口中所有方法

   ```java
   //urlPatterns表示访问目标
   //loadOnStartup 为负整数时：第一次访问是创建Servlet对象
   //			   0或正整数：服务器启动时创建Servlet对象，数字越小优先级越高
   //默认情况使用  @WebServlet("/demo")
   @WebServlet(urlPatterns = "/demo",loadOnStartup = 1)
   public class Demo implements Servlet {
       //调用时机：默认情况下，Servlet被第一次访问时调用并且只会被调用一次
       public void init(ServletConfig servletConfig) throws ServletException {
   
       }
    	//1.调用时机：每一次Servler被访问时调用
       //2.调用次数：每一次调用
       public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
   
       }
   	//调用时机：内存释放或者服务器关闭的时候，Servler对象会被销毁时调用
       //调用次数：1次
       public void destroy() {
   
       }
       
       @Override
       public ServletConfig getServletConfig() {
           return null;
       }
   	//返回一些关于Servlet对象的信息，比如作者、版本和版权  一般不怎么使用
       @Override
       public String getServletInfo() {
           return null;
       }
   }
   ```

3. 配置：在类上使用@WebServlet 注解，配置该Servlet的访问路径

   ```java
   @WebServlet 
   public class ServletDemo implements Servlet
   ```

4. 访问：启动Tomcat，浏览器输入url访问该Servlet

Servlet生命周期

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220622170518722.png" alt="image-20220622170518722" style="zoom:50%;" />

Servlet体系结构

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624092913357.png" alt="image-20220624092913357" style="zoom:50%;" />

我们自定义servlet时，一般继承HttpServlet并且复写他的 `doGet()` 和`doPost()` 方法

doGet()方法中写 get 请求方式的处理逻辑

doPost()方法中写 post 请求方式的处理逻辑

### Servlet要想被访问，必须配置其访问路径（urlPattern）

1. 一个 Servlet 可以配置多个 urlPattern

   @WebServlet( urlPattern = { "/demo1" , " /demo2" } )

2. urlPattern 配置规则

   优先级：精确匹配 > 目录匹配 > 扩展名匹配 > 任意匹配

   - 精确匹配    

     - 配置路径：@WebServlet( " /user/demo1 " )
     - 访问路径：localhost:8080/web-demo/user/demo1

   - 目录匹配

     - 配置路径：@WebServlet( " /user/* " )
     - 访问路径：localhost:8080/web-demo/user/aaa ，localhost:8080/web-demo/user/bbb

   - 扩展名匹配 

     - 配置路径：@WebServlet( " *.do " )   （注意没有 / ）
     - 访问路径：localhost:8080/web-demo/aaa.do ，localhost:8080/web-demo/bbb.do

   - 任意匹配（尽量不要使用，是危险的）

     - 配置路径：@WebServlet( " / " ) ，@WebServlet( " /* " )   

     - 访问路径：localhost:8080/web-demo/***

       后面的优先级高于前面的优先级，当我们项目中的Servlet配置了 `/` ，当其他的url-pattern都匹配不上时会走这个Servlet

       配置 `/*` ，意味着匹配任意访问路径

 ### XML配置方式编写Servlet（了解）

Servlet 从3.0版本后开始支持使用注解配置，3.0版本前只支持XML 配置文件的配置方式

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624101833874.png" alt="image-20220624101833874" style="zoom:50%;" />

```xml
<!--说明类的位置-->
<serclet-class>com.itheima.web.servlet.ServletDemo5</serclet-class>
<!--说明访问路径-->
<url-pattern>/demo5</url-pattern>
<!--两个name标签的内容需要一样-->
```

## Request(请求)

### Request 继承体系

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624105037077.png" alt="image-20220624105037077" style="zoom:50%;" />

因为使用Tomcat所以是Tomcat定义的实现类

### Request 获取请求数据

1. 请求行

   常用的一些方法

   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624113408330.png" alt="image-20220624113408330" style="zoom: 67%;" />

   getQueryString()：`localhost:8080/Tomcat/demo1?username=zhangsan&password=123` 返回的是后面的这一串

2. 请求头

   ```java
   String getHeader(String nama)
   //根据请求头名称，获取值
   ```

3. 请求体

   使用字节输入流或者字符输入流

### 通用方式获取请求参数

doGet() 和 doPost() 的逻辑概念是一样的，但是对于参数的获取的方式是不一样的，会出现代码重复的问题

- GET方式

  ```java
  String getQueryString()
  ```

- POST方式

  ```java
  BufferedReader getReader();
  ```

解决方案一:

```java
@WebServlet("/req1")
public class RequestDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方式
        String method = req.getMethod();
        //获取请求参数
        String params = "";
        if("GET".equals(method)){
            params = req.getQueryString();
        }else if("POST".equals(method)){
            BufferedReader reader = req.getReader();
            params = reader.readLine();
        }
        //将请求参数进行打印控制台
        System.out.println(params);
      
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
```

使用request的getMethod()来获取请求方式，根据请求方式的不同分别获取请求参数值，这样就可以解决上述问题，但是以后每个Servlet都需要这样写代码，实现起来比较麻烦，**这种方案我们不采用**

解决方案二:

request对象已经将上述获取请求参数的方法进行了封装，并且request提供的方法实现的功能更强大，以后只需要调用request提供的方法即可，在request的方法中都实现了哪些操作?

(1)根据不同的请求方式获取请求参数，获取的内容如下:

![image-20220624134258968](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624134258968.png)

(2)把获取到的内容进行分割，内容如下:

![image-20220624134310198](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624134310198.png)

(3)把分割后端数据，存入到一个Map集合中:

![image-20220624134323599](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624134323599.png)

**注意**:因为参数的值可能是一个，也可能有多个，所以Map的值的类型为String数组。

基于上述理论，request对象为我们提供了如下方法:

* 获取所有参数Map集合

```
Map<String,String[]> getParameterMap()
```

* 根据名称获取参数值（数组）

```
String[] getParameterValues(String name)
```

* 根据名称获取参数值(单个值)

```
String getParameter(String name)
```

接下来，我们通过案例来把上述的三个方法进行实例演示:

1.修改req.html页面，添加爱好选项，爱好可以同时选多个

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-demo/req2" method="get">
    <input type="text" name="username"><br>
    <input type="password" name="password"><br>
    <input type="checkbox" name="hobby" value="1"> 游泳
    <input type="checkbox" name="hobby" value="2"> 爬山 <br>
    <input type="submit">

</form>
</body>
</html>
```

![1628780937599](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/1628780937599.png)

2.在Servlet代码中获取页面传递GET请求的参数值

 2.1获取GET方式的所有请求参数

```java
/**
 * request 通用方式获取请求参数
 */
@WebServlet("/req2")
public class RequestDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //GET请求逻辑
        System.out.println("get....");
        //1. 获取所有参数的Map集合
        Map<String, String[]> map = req.getParameterMap();
        for (String key : map.keySet()) {
            // username:zhangsan lisi
            System.out.print(key+":");

            //获取值
            String[] values = map.get(key);
            for (String value : values) {
                System.out.print(value + " ");
            }

            System.out.println();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
```

获取的结果为:

![1628780965283](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/1628780965283.png)

 2.2获取GET请求参数中的爱好，结果是数组值

```java
/**
 * request 通用方式获取请求参数
 */
@WebServlet("/req2")
public class RequestDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //GET请求逻辑
        //...
        System.out.println("------------");
        String[] hobbies = req.getParameterValues("hobby");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
```

获取的结果为:

![1628781031437](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/1628781031437.png)

 2.3获取GET请求参数中的用户名和密码，结果是单个值

```java
/**
 * request 通用方式获取请求参数
 */
@WebServlet("/req2")
public class RequestDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //GET请求逻辑
        //...
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
```

获取的结果为:

![1628781176434](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/1628781176434.png)

3.在Servlet代码中获取页面传递POST请求的参数值

 3.1将req.html页面form表单的提交方式改成post

 3.2将doGet方法中的内容复制到doPost方法中即可

### 请求参数中文乱码问题（Tomcat8已解决  maven插件支持到 7 ）

由于浏览器传来的值的编码是 UTF-8 但是Tomcat对其解析时使用的编码为 ISO_8859_1 因为编码不兼容导致乱码

POST提交方式解决乱码

```java
request.setCharacterEncoding("UTF-8");
//对其编码方式进行改变，根据HTML页面的编码来进行设置
String username = request.getParameter("username");
System.out.println(username);
//不使用第一行代码打印出来就是乱码
```

GET提交方式解决乱码（也是通用解决办法）

```java
String username = request.getParameter("username");
//将字符串转换为字节数组，按照 ISO_8859_1 编码
byte[] bytes = username.getBytes(StandardCharsets.ISO_8859_1);
//通过字节数组按照 UTF_8 转换为字符串
String s = new String(bytes, StandardCharsets.UTF_8);
System.out.println(s);
```



### Request  请求转发

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624153421389.png" alt="image-20220624153421389" style="zoom:50%;" />

资源A和资源B共享数据

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624153721661.png" alt="image-20220624153721661" style="zoom:60%;" />

请求转发特点

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624154244195.png" alt="image-20220624154244195" style="zoom:80%;" />

重定向特点 两者进行对比一下

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624164951993.png" alt="image-20220624164951993" style="zoom:50%;" />

## Response(响应)

### Response继承体系

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624162255534.png" alt="image-20220624162255534" style="zoom: 67%;" />

### Response 设置响应数据功能介绍

1. 响应行

   ```java
   void setStatus(int sc)
   //设置响应状态码
   ```

2. 响应头

   ```java
   //设置响应头键值对
   void setHeader(String name,String value)
   ```

3. 响应体

   使用字符字节输出流

   ```java
   //获取字符输出流
   PrintWrite getWrite()
   //获取字节输出流
   ServletOutputStream getOutputStream()
   ```

### Response 完成重定向

重定向：一种资源跳转方式

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624164913223.png" alt="image-20220624164913223" style="zoom:50%;" />

加上虚拟目录是会写死这个路径我们应该使用动态获取的办法

```java
//获取虚拟目录
String contextPath = request.getContextPath();
response.sendRedirect(contextPath+"/ServletTest2");
// response.sendRedirect("/Tomcat/ServletTest2");
//上面这一句就是写死的

```

请求转发特点 对比一下

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624154244195.png" alt="image-20220624154244195" style="zoom:80%;" />

#### 路径问题

请求转发不需要虚拟目录，重定向需要虚拟目录

- 浏览器使用：需要加载虚拟目录（项目访问路径）
- 服务端使用：不需要加载虚拟目录

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624170637504.png" alt="image-20220624170637504" style="zoom: 60%;" />

### Response 响应字符数据

通过Response对象获取字符输出流

```java
PrintWriter writer=response.getWriter();
//可向网页界面中写入aaa，如果直接输出中文会乱码，编码不支持
writer.write("aaa")
//response.setContentType("text/html;charset=utf-8");
//可通过此方式设置输出的内容的风格，并设置编码格式 在一个引号内
```

### Response 响应字节数据

通过Response对象获取字节输出流

```java
ServletOutputStream outputStream = response.getOutputStream();
outputStream.write(字节数据);
```

IOUtils工具类可以更简单的使用IO流可在maven中配置

![image-20220624214634718](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220624214634718.png)

## JSP（已逐渐退出历史舞台  由 HTMl + AJAX替代 ）

![image-20220625161246918](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220625161246918.png)

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220625100634888.png" alt="image-20220625100634888" style="zoom:50%;" />

![image-20220626111343300](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626111343300.png)

### EL表达式

用于简化JSP页面中的java代码

主要功能：获取数据

语法：${expression}	（中间的表达式对应 Servlet 语句中的  req.setAttribute("expression", brands);）
在使用此语句 `req.getRequestDispatcher("/EL-demo.jsp").forward(request,response);`转移到对应页面中去

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626113033398.png" alt="image-20220626113033398" style="zoom:50%;" />

比如一个小使用

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220627171829709.png" alt="image-20220627171829709" style="zoom:50%;" />

### JSTL标签

使用标签取代 JSP 页面上的 Java 代码

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626113305367.png" alt="image-20220626113305367" style="zoom:50%;" />

`<c:if test=" EL表达式 ">  <?c:if>`  test 是逻辑表达式  同样是由 Servler 转发而来的

forEach 

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626114743111.png" style="zoom:50%;" />

还有一个属性  varStatus 其值任意填写
比如上图的获取 id 时获取的是数据库中的 id 但是其不一定连续 可以使用 `varStatus = "status"  ${status.index} 或者 ${status.count}  count 代表从 1 开始  index从 0 开始`  

普通 For 循环

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626115339659.png" alt="image-20220626115339659" style="zoom:50%;" />

### MVC模式

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626121752229.png" alt="image-20220626121752229" style="zoom:50%;" />

可以提高代码的复用性

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220626122235309.png" alt="image-20220626122235309" style="zoom:50%;" />

SSM就是对这三个层面代码的封装和简化

## 会话跟踪技术

会话：用户打开浏览器，访问web服务器的资源，会话建立，知道有一方断开连接，会话结束。在一次会话中可以包含多次请求和响应

会话跟踪：一种维护浏览器状态的方法，服务器需要识别多次请求是否来自同一浏览器，以便在同一次会话中的多次请求间共享数据

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220627120021257.png" alt="image-20220627120021257" style="zoom:50%;" />六次请求三次会话

实现方式

- 客户端会话跟踪技术（数据存储到客户端）：Cookie  （没有 Session 安全）
- 服务端会话跟踪技术（数据存储到服务端）：Session

### Cookie

#### 基本使用

1. 发送 Cookie

   1. 创建 Cookie 对象，设置数据

      `Cookie cookie =new Cookie("key","value");`

   2. 发送 Cookie 到客户端：Shi用 response 对象

      response.addCookie(cookie);
      
      ```java
      //Cookie 的创建
      Cookie c_username = new Cookie("username", username);
      Cookie c_password = new Cookie("password", password);
      //设置存活时间
      c_password.setMaxAge(60*60*24);//存活一天
      c_username.setMaxAge(60*60*24);//存活一天
      //Cookie 发送
      res.addCookie(c_username);
      res.addCookie(c_password);
      ```
      
      

2. 获取 Cookie

   1. 获取客户端携带的所有 Cookie ，使用 request 对象

      `Cookie[] cookies = request.getCookie();`

   2. 遍历数组，获取每一个 Cookie 对象：for 循环

   3. 使用 Cookie 对象方法获取数据

      `cookie.getName();`

      `cookie.getValue();`

#### 原理

COokie 的实现是基于 HTTP 协议的

- 响应头：set - cookie;
- 请求头：cookie

#### 使用细节

Cookie 存活时间

- 默认情况下，Cookie 存储在浏览器内存中，当浏览器关闭，内存释放，则 Cookie 被销毁
- setMaxAge( int seconds )：设置 Cookie 存活时间
  - 正数：将 Cookie 写入浏览器所在电脑的莹白，持久化存储，到时间自动删除
  - 负数：默认值，Cookie 在当前浏览器内存中，当浏览器关闭，则 Cookie 被销毁
  - 零：删除对应 Cookie

Cookie 存储中文（默认不支持中文）
		如需存储中文，则需要进行转码：URL编码
			转码
				`String value = "张三";  value = URLEnconder.encode(value,"UTF-8");`
			解码
				`String value = cookie.getValue(); value = URLDecoder.decode(value,"UTF-8");`

Cookie 设置好以后 如何自动填充用户名和密码呢	
		在页面获取 Cookie 数据后，设置到用户名和密码框中：
		使用 EL表达式 `${cookie.key.value}  // key指存储在 cookie 中的键的名称 设置在标签元素的 value 值`     

### Session

#### 基本使用

服务端会话跟踪技术：将数据保存到服务端

JavaEE 提供HttpSession 接口，来实现一次会话的多次请求见数据共享功能

使用：

1. 获取 Session 对象

   `HttpSession session=request.getSession();`

2. Session 对象功能

   - void setAttribute ( String name , Object O)：存储数据到 Session 域中
   - Object getAttribute ( String name )：根据 key ，获取值
   - void removeAttribute ( String name )：根据 key ，删除该键值对

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220629134127070.png" alt="image-20220629134127070" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220629134359529.png" alt="image-20220629134359529" style="zoom:50%;" />

#### 原理

Session 是基于 Cookie 实现的

#### 使用细节

不能长期的去保存一些数据，Cookie 可以

- Session 钝化、活化

  - 钝化：在服务器正常关闭后，Tomcat 会自动将 Session 数据写入硬盘的文件中
  - 活化：再次启动服务器后，从文件中加载数据到 Session 中 文件被删除

  eg：服务器正常重启后，Session 中的数据仍然存在

- Session 销毁

  - 默认情况下，无操作，30 分钟自动销毁

    如想更改默认时间 可在 web.xml 文件中添加

    ```xml
    <session-config>
        <session-timeout>xxx</session-timeout>    <!--xxx 代表分钟数-->
    </session-config>
    ```

  - 调用 Session 对象的 invalidate() 方法来进行销毁

### Cookie 和 Session 的区别

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220629142317671.png" alt="image-20220629142317671" style="zoom:50%;" />

## Filter

- 概念：Filter 表示过滤器，是 JavaWeb 三大组件 （ Servlet、Filter、Listener ）之一
- 顾虑其可以把对资源的请求拦截下来，从而实现一些特殊的请求
- 过滤器一般完成一些通用的操作，比如：权限控制、统一编码处理、敏感字符处理...

### 快速入门

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702101156657.png" alt="image-20220702101156657" style="zoom:50%;" />

### 执行流程

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702100711087.png" alt="image-20220702100711087" style="zoom:50%;" />

### 使用细节

- 拦截路径配置

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702102308072.png" alt="image-20220702102308072" style="zoom:50%;" />

- 过滤器链

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702103413227.png" alt="image-20220702103413227" style="zoom:50%;" />

## Listener（现在很少使用）

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702150234679.png" alt="image-20220702150234679" style="zoom:50%;" />

## AJAX （主流方式，完成了前后端分离）

概念：异步的 JavaScript 和 XML

作用：

1. 与服务器进行数据交换：通过 AJAX 可以给服务器发送请求，并获取服务器响应的数据
   使用了 AJAX 和服务器进行通信，就可以使用 HTML + AJAX 来替换 JSP 页面了  

   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702152018475.png" alt="image-20220702152018475" style="zoom:50%;" />

   与 JSP 的区别<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702152056041.png" alt="image-20220702152056041" style="zoom:33%;" />

2. 异步交互：可以在不重新加载整个页面的情况下，与服务器交换数据并更新部分网页的技术，如：搜索联想

3. 同步和异步的区别
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702153411248.png" style="zoom:50%;" />

### 快速入门

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702155113810.png" alt="image-20220702155113810" style="zoom:50%;" />

### Axios 异步框架

Axios 对原生的 AJAX 进行封装，简化书写

官网：[Axios 中文文档 ](https://www.axios-http.cn/)

快速入门
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702162225303.png" alt="image-20220702162225303" style="zoom:50%;" />

2中的then(function (resp){

}) 可以替换为 then(resp =>{

})   类似 Java 中的 Lambda 表达式

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702182217004.png" alt="image-20220702182217004" style="zoom:50%;" />

## Json

概念：JavaScript Object Notation 。 Javascript对象表示法

由于其语法简单，层次结构鲜明，现多用于作为数据载体，在网络中进行数据传输

### 基础语法

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702215230972.png" alt="image-20220702215230972" style="zoom:50%;" />

### JSON 数据和 Java 对象转换

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220702215938111.png" style="zoom: 50%;" />

```java
//Servlet 获取网页的json数据

//获取请求体数据
BufferedReader br = request.getReader();
String params = br.readLine();

//将 json 字符串转为 Java 对象、
Brand brand = JSON.parseObject(params,Brand.class);

```

## Vue (前端最为主流的框架)

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220703121927805.png" alt="image-20220703121927805" style="zoom:50%;" />

### 快速入门

1. 新建 HTML 页面，引入 Vue.js 文件

   `<scrpit src="js/vue.js"></scrpit>`

2. 在 JS 代码区域，创建Vue核心对象，进行数据绑定

   ```javascript
   new:Vue({
   	el:"#app",//对应相应的id 设置Vue的控制范围
   	data(){
   		return{
   			username:""//对应 id 标签的 v-model 的值要和 username 一样，不是引号中的值
   		}
   	}
   })
   ```

3. 编写视图

   ```html
   <div id="app">
       <input name="username" v-model="username">
       <!--插值表达式-->
       {{username}}
   </div>
   ```

   

### 常用指令

| 指令      | 作用                                                         |
| --------- | ------------------------------------------------------------ |
| v-bind    | 为HTML便签绑定属性值，如设置 href `<a v-bind:href="url"></a> v-bind可以省略` |
| v-model   | 在表单元素上创建双向数据绑定                                 |
| v-on      | 为HTML标签绑定事件 `v-on可替换为@`                           |
| v-if      | 条件性的渲染某元素，判定为true时渲染，否则不渲染             |
| v-else    | 同上                                                         |
| v-else-if | 同上                                                         |
| v-show    | 根据条件展示某元素，区别在于切换的是display属性的值          |
| v-for     | 列表渲染，遍历容器的元素或者对象的属性                       |

使用 Vue 定义方法

```js
new Vue({
    el:"#Xxx",
    methods:{
        show(){
            <!--方法体,show是方法的名字-->
        }
    }
});
```



### 生命周期

生命周期有八个阶段：每触发一个生命周期，会自动执行一个生命周期方法

|     状态      | 阶段周期 |
| :-----------: | :------- |
| beforeCreate  | 创建前   |
|    created    | 创建后   |
|  beforeMount  | 加载前   |
|    mounted    | 加载完成 |
| beforeUpdate  | 更新前   |
|    updated    | 更新后   |
| beforeDestory | 销毁前   |
|   destoryed   | 销毁后   |

mounted：Vue初始化成功，HTML页面渲染成功，发送异步请求，加载数据


## Element （用于美化页面）

Element：基于Vue的网站组件库，用于快速构建网页

组件：组成网页的部件，例如 超链接，按钮，图片，表格等...

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220704131642128.png" alt="image-20220704131642128" style="zoom:50%;" />

[Element - 网站快速成型工具](https://element.eleme.cn/#/zh-CN)

