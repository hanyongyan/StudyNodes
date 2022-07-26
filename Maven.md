# Maven

## 1 Maven简介

### 1.1 传统项目管理状态分析

- jar包不统一，不兼容

- 工程升级维护过程操作繁琐

- …

  ![img](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121135112719.png)

### 1.2 Maven是什么

- maven的本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型（POM）
- POM（Project Object Model）：项目对象模型


![img](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121135910882.png)

### 1.3 Maven的作用

- 项目构建：提供标准的、跨平台的自动化项目构建方式
- 依赖管理：方便快捷的管理项目依赖的资源（jar包），避免资源间的版本冲突问题
- 统一的开发结构：提供标准的、统一的项目结构

![img](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121140112715.png)

## 2 下载与安装（省略）

## 3 Maven基础概念

### 3.1 仓库

![img](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121141827509.png)

> 仓库分类

- 本地仓库：自己电脑上存储资源的仓库，连接远程仓库获取资源

- 远程仓库：包括私服和中央仓库，即非本机电脑上的仓库，为本地仓库提供资源

  - 中央仓库：Maven团队维护，存储所有资源的仓库

  - 私服：部门或公司范围内存储资源的仓库，它从中央仓库来获取资源

> 私服的作用

- 保存具有版本的资源，包含购买或自主研发的jar
- 中央仓库的jar都是开源的，不能存储具有版权的资源
- 一定范围内共享资源，仅对内部开放，不对外共享

### 3.2 坐标

- 坐标：Maven中的坐标用于描述仓库中资源的位置

- 主要组成：

  - groupId：定义当前Maven项目隶属组织名称（通常是域名反写，例如org.mybatis）

  - artifactId：定义当前Maven项目名称（通常是模块名称，例如CRM，SMS）

  - version：定义当前项目版本号

  - packaging：定义该项目的打包方式

示例：

```java
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.12.1</version>
</dependency>
```

- 作用：使用唯一标识，唯一性地定位资源位置，通过该标识可以将资源的识别与下载工作交由机器完成

### 3.3 仓库配置

#### 3.3.1 本地仓库配置

Maven启动后，会自动保存下载的资源到本地仓库，仓库位置可在conf/settings.xml下查看

- 默认位置

  ```xml
  <!-- localRepository
     | The path to the local repository maven will use to store artifacts.
     |
     | Default: ${user.home}/.m2/repository
    <localRepository>/path/to/local/repo</localRepository>
  -->
  ```

  其中的默认位置就是：

  ```xml
  <localRepository>${user.home}/.m2/repository</localRepository>
  ```

  可以自定义本地仓库的位置，将上述代码修改为：

  ```xml
  <localRepository>D:/DevEnvironment/localRepository</localRepository>
  ```

### 3.3.2 远程仓库配置

默认连接的远程仓库的位置：

```xml
<repositories> 
    <repository> 
        <id>central</id> 
        <name>Central Repository</name> 
        <url>https://repo.maven.apache.org/maven2</url> 
        <layout>default</layout> 
        <snapshots> 
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

可以将其替换为镜像仓库，修改conf/settings.xml，配置为阿里云的镜像仓库

```xml
<mirrors> 
    <!-- 配置具体的仓库的下载镜像 --> 
    <mirror> 
        <!-- 此镜像的唯一标识符，用来区分不同的镜像 --> 
        <id>nexus-aliyun</id> 
        <!-- 对哪种仓库进行镜像，即对哪个远程仓库进行替换，此处为中央仓库 --> 
        <mirrorOf>central</mirrorOf>
        <!-- 镜像名称 --> 
        <name>Nexus aliyun</name>
        <!-- 镜像的URL --> 
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror>
</mirrors>
```

## 4 手工制作Maven项目（省略

## 5 IDEA生成Maven项目（省略

## 6 依赖管理

1. 创建三个maven模块，project1，project2，project3，分别添加log4j的依赖，版本号依次为2.13.0，2.13.1，2.13.2，项目结构如下：

   ![image-20220121170201713](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121170201713.png)

2. 各个模块的maven依赖如下：

   ![image-20220121170234318](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121170234318.png)

### 6.2 依赖配置

- 依赖：指当前项目运行所需的jar，一个项目可以设置多个依赖

- 格式：

  ```xml
  <!-- 设置当前项目所依赖的所有jar --> 
  <dependencies> 
      <!-- 设置具体的依赖 --> 
      <dependency> 
          <!-- 依赖所属群组id --> 
          <groupId>junit</groupId> 
          <!-- 依赖所属项目id --> 
          <artifactId>junit</artifactId> 
          <!-- 版本号 --> 
          <version>4.12</version>
      </dependency>
  </dependencies>
  ```

### 6.3 依赖传递

#### 6.3.1 概念

- 直接依赖：在当前项目中通过依赖配置建立的依赖关系

- 间接依赖：被依赖的资源如果依赖其他资源，则当前项目间接依赖其他资源

  ![img](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121170443225.png)

#### 6.3.2 依赖传递的冲突问题

- 路径优先：当依赖中出现相同的资源，层级越深，优先级越低，反之越高

- 声明优先：当资源在相同层级被依赖时，配置顺序靠前的覆盖靠后的

- 特殊优先：当同级配置了相同资源的不同版本，后配置的覆盖先配置的

  ![image-20220121171540596](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121171540596.png)

> 示例

1. project2依赖project3：

   ```xml
   <!-- project2的pom -->
   <dependency>
       <groupId>com.hongyi</groupId>
       <artifactId>project3</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

2. 在project3中添加junit的依赖

   ```xml
   <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>4.12</version>
   </dependency>
   ```

3. 查看此时的依赖关系

   ![image-20220121171129186](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121171129186.png)

4. 对当前项目project2而言，project3和log4j:2.13.1为1度依赖，而project3的log4j:2.13.2为2度依赖，因此被忽略（omitted）


#### 6.3.3 可选依赖

可选依赖是指对外隐藏当前所依赖的资源，即不透明性。配置是在被依赖方。

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <!-- 添加这一选项，并设为true -->
    <optional>true</optional>
</dependency>
```

![image-20220121172029873](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121172029873.png)

发现该依赖被隐藏了。

#### 6.3.4 排除依赖

排除依赖是主动断开依赖的资源，被排除的资源不需要指定版本。配置是在依赖方。

例如project2要排除掉project3中的依赖log4j:2.13.2（虽然根据依赖冲突的解决也没有生效）。在project2的pom文件中添加：

```xml
<!-- 要排除掉project3中的log4j依赖 -->
<dependency>
    <groupId>com.hongyi</groupId>
    <artifactId>project3</artifactId>
    <version>1.0-SNAPSHOT</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

执行结果：![image-20220121180142958](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121180142958.png)

发现project2中没有了log4j:2.13.2的依赖。只剩下junit的依赖

### 6.4 依赖范围

依赖的jar默认情况可以在任何地方使用，可以通过scope标签设定其作用范围

- 作用范围

  - 主程序范围有效（main文件范围内）
  - 测试程序范围有效（test文件范围内）
  - 是否参与打包（package指令范围内）

| scope         | 主代码 | 测试代码 | 打包 | 示例        |
| ------------- | ------ | -------- | ---- | ----------- |
| compile（默认 | ✓      | ✓        | ✓    | log4j       |
| test          |        | ✓        |      | junit       |
| provided      | ✓      | ✓        |      | servlet-api |
| runtime       |        |          | ✓    | jdbc        |

> 示例

例如在project2中新增依赖：

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.9</version>
    <scope>test</scope>
</dependency>
```

执行结果![image-20220121181457657](https://kisugitakumi.oss-cn-chengdu.aliyuncs.com/img8/image-20220121181457657.png)

#### 6.4.2 依赖范围传递性

带有依赖范围的资源在进行传递时，作用范围将受到影响。

![image-20220121181641821](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220121181641821.png)

## 7 生命周期与插件

### 7.1 项目构建生命周期

Maven构建生命周期描述的是一次构建过程经历了多少个事件

![default的生命周期](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122132925074.png)

Maven对项目构建的生命周期划分为3套：

- `clean`：清理工作
- `default`：核心工作，例如编译，测试，打包，部署等
- `site`：产生报告，发布站点

> clean生命周期

- pre-clean：执行一些需要在clean之前完成的工作

- clean： 移除所有上一次构建生成的文件
- post-clean：执行一些需要在clean之后立刻完成的工作

> default构建生命周期

![image-20220122133255620](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122133255620.png)

说明：图中标红的是Maven典型（常用）的构建生命周期点。

例如当执行mvn compile命令时，会从头（mvn validate）开始执行命令至mvn compile初。

又如执行mvn package命令时，也会从头开始执行，直到mvn package命令处停止。

> site构建生命周期

![image-20220122133316989](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122133316989.png)

![image-20220122135358105](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122135358105.png)

### 7.2 插件

- 插件与生命周期内的阶段绑定，在执行到对应生命周期时执行对应的插件功能

- 默认maven在各个生命周期上绑定有预设的功能

- 通过插件可以自定义其他功能

  ![image-20220122133736137](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122133736137.png)

  理解：生命周期相当于人活到了几岁，而插件相当于在几岁时需要做哪些事情

> 演示示例——自定义插件

1. 项目搭建：在project3中的主类和测试类中分别创建Demo.java和DemoTest.java

   ```java
   public class Demo {
       public void run() {
           System.out.println("source code plugin...");
       }
   }
   ```

   ```java
   public class DemoTest {
       public void test() {
           System.out.println("test source code plugin...");
       }
   }
   ```

2. 自定义插件写在<bulid>标签中。在project3的pom文件中写入：

   ```xml
   <build>
       <plugins>
           <plugin>
               <!-- 自定义插件所需的坐标 -->
               <!-- 这个插件的功能是生成项目的java源码 -->
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-source-plugin</artifactId>
               <version>3.2.1</version>
               <!-- 定义在哪个生命周期点执行，可定义多个生命周期点execution -->
               <executions>
                   <execution>
                       <!-- 定义在什么范围类使得插件生效，可定义多个范围goal -->
                       <goals>
                           <!-- 定义在什么范围类使得插件生效 -->
                           <!-- jar表示在main中生效 -->
                           <!-- test-jar表示在test中生效 -->
                           <goal>jar</goal>
                       </goals>
                       <!-- 在哪个生命周期点执行，此处为generate-test-resources -->
                       <phase>generate-test-resources</phase>
                   </execution>
               </executions>
           </plugin>
       </plugins>
   </build>
   ```

   ![image-20220122140049804](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140049804.png)

   此处演示在generate-test-resources处自定义了一个插件，该插件生成项目的源码文件。意味着，例如执行mvn test时，会执行到该插件。执行mvn compile时，不会执行到该插件。而且该插件只会生成main中的源代码。

3. 刷新maven后，可看到该插件：

   ![image-20220122140449012](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140449012.png)

4. 执行mvn compile，发现target并没有生成源码文件

   ![image-20220122140607193](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140607193.png)

5. 执行mvn test，发现生成了源码文件

   ![image-20220122140707370](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140707370.png)

6. 打开文件，发现是main的源码：

   ![image-20220122140806790](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140806790.png)

7. 添加一个goal为test-jar。首先执行clean，再执行package：

   ![image-20220122140944862](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122140944862.png)

8. 发现有打包好的工程文件、main源码和test源码。

   ![image-20220122141025450](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122141025450.png)

------

以下为Maven高级部分

## 8 分模块开发与设计（省略

## 9 聚合和继承

### 9.1 聚合

聚合：多模块的构建维护。

![image-20220122165852909](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122165852909.png)

聚合作用：聚合用于快速构建maven工程，一次性构建多个项目或模块

> 示例

1. 创建一个空模块ssm，该项目中只有一个pom文件

   ![image-20220122170016494](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122170016494.png)

2. 打包类型定义为pom，并定义当前模块进行构建时关联的其他模块名称

   ```xml
   <!--定义该工程，用于进行构建管理-->
   <packaging>pom</packaging>
   
   <!--管理的工程列表-->
   <modules>
       <module>../ssm_dao</module>
       <module>../ssm_pojo</module>
       <module>../ssm_controller</module>
       <module>../ssm_service</module>
   </modules>
   ```

   注意：参与聚合操作的模块最终执行顺序与模块间的依赖关系有关，与配置顺序无关

3. 执行mvn install并观察：

   ![image-20220122170222472](https://kisugitakumi.oss-cn-chengdu.aliyuncs.com/img8/image-20220122170222472.png)

### 9.2 继承

#### 9.2.1 概述和示例

继承：模块依赖关系维护。

![image-20220122180752810](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122180752810.png)

- 继承作用：通过继承可以实现在子工程中沿用父工程的配置

- maven中的继承与java中的继承相似，在子工程中配置继承关系

> 示例

1. 在父工程ssm中声明依赖管理，将子工程所有的依赖都声明在此处。利用标签`<dependencyManagement>`

   ```xml
   <!--声明此处进行依赖管理-->
   <dependencyManagement>
       <!--具体的依赖-->
       <dependencies>
           <!--添加自己工程的依赖-->
           <dependency>
               <groupId>com.itheima</groupId>
               <artifactId>ssm_pojo</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
           <dependency>
               <groupId>com.itheima</groupId>
               <artifactId>ssm_dao</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
           <dependency>
               <groupId>com.itheima</groupId>
               <artifactId>ssm_service</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
           <!--springmvc环境-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-webmvc</artifactId>
               <version>5.1.9.RELEASE</version>
           </dependency>
           <!--jackson相关坐标3个-->
           <dependency>
               <groupId>com.fasterxml.jackson.core</groupId>
               <artifactId>jackson-databind</artifactId>
               <version>2.9.0</version>
           </dependency>
           <!--只展示了部分依赖-->
       </dependencies>
   </dependencyManagement>
   ```

2. 在子工程中定义父工程

   ```xml
   <!--定义该工程的父工程-->
   <parent>
       <!--父工程的名字-->
       <artifactId>ssm</artifactId>
       <groupId>com.itheima</groupId>
       <version>1.0-SNAPSHOT</version>
       <!--父工程的pom文件-->
       <relativePath>../ssm/pom.xml</relativePath>
   </parent>
   ```

3. 此时，子工程的依赖的版本号可以省略，例如：

   ```xml
   <!--导入资源文件pojo-->
   <dependency>
       <groupId>com.itheima</groupId>
       <artifactId>ssm_pojo</artifactId>
   </dependency>
   ```

4. 同理，插件的管理也可以在父工程中声明：

   ```xml
   <build>
       <pluginManagement>
           <!--设置插件-->
           <plugins>
               <!--具体的插件配置-->
               <plugin>
                   <groupId>org.apache.tomcat.maven</groupId>
                   <artifactId>tomcat7-maven-plugin</artifactId>
                   <version>2.1</version>
                   <configuration>
                       <port>80</port>
                       <path>/</path>
                   </configuration>
               </plugin>
           </plugins>
       </pluginManagement>
   </build>
   ```

5. 子工程中插件的版本和相关配置就可以省略

   ```xml
   <build>
     <!--设置插件-->
     <plugins>
       <!--具体的插件配置-->
       <plugin>
         <groupId>org.apache.tomcat.maven</groupId>
         <artifactId>tomcat7-maven-plugin</artifactId>
       </plugin>
     </plugins>
   </build>
   ```

#### 9.2.2 继承的资源

以下资源，子工程都可以从父工程中继承：

![image-20220122181446745](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122181446745.png)

#### 9.2.3 继承和聚合的区别

- 作用：

  - 聚合用于快速构建项目
  - 继承用于快速配置

- 相同点：

  - 聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中


  - 聚合与继承均属于设计型模块，并无实际的模块内容

- 不同点：

  - 聚合是在当前模块中配置关系，聚合可以感知到参与聚合的模块有哪些


  - 继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己 

### 9.3 属性

#### 9.3.1 自定义属性

- 作用：等同于定义变量，方便统一维护
- 配置位置：父工程的pom文件
- 标签：`<properties>`
- 调用格式：`${}`

> 示例

1. 在父工程中的pom文件内：

   ```xml
   <properties>
       <!--定义自定义的属性-->
       <spring.version>5.1.9.RELEASE</spring.version>
       <junit.version>4.12</junit.version>
   </properties>
   ```

2. 调用：

   ```xml
   <!--springmvc环境-->
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>${spring.version}</version>
   </dependency>
   ```

#### 9.3.2 内置属性

作用：使用maven内置属性，快速配置

例如，父工程和子工程的版本号一样，可以直接使用父工程的版本内置属性${version}：

```xml
<!--添加自己工程的依赖-->
<dependency>
    <groupId>com.itheima</groupId>
    <artifactId>ssm_pojo</artifactId>
    <version>${version}</version>
</dependency>
<dependency>
    <groupId>com.itheima</groupId>
    <artifactId>ssm_dao</artifactId>
    <version>${version}</version>
</dependency>
<dependency>
    <groupId>com.itheima</groupId>
    <artifactId>ssm_service</artifactId>
    <version>${version}</version>
</dependency>
```

### 9.4 版本管理

- `SNAPSHOT`快照版本
  - 项目开发过程中，为方便团队成员合作，解决模块间相互依赖和时时更新的问题，开发者对每个模块进行构建的时候，输出的临时性版本叫快照版本（测试阶段版本）
  - 快照版本会随着开发的进展不断更新

- RELEASE发布版本

  - 项目开发到进入阶段里程碑后，向团队外部发布较为稳定的版本，这种版本所对应的构件文件是稳定的，即便进行功能的后续开发，也不会改变当前发布版本内容，这种版本称为发布版本 

### 9.5 资源配置

![image-20220122184324524](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122184324524.png)

配置文件引用pom属性：

- 作用：在任意配置文件中加载pom文件中定义的属性
- 调用格式：${ }

> 示例

1. 例如jdbc.properties要读取父工程pom文件中的属性。首先在父工程pom文件中的<build>标签中：

   ```xml
   <bulid>
       <!--配置资源文件对应的信息-->
       <resources>
           <resource>
               <!--各个项目下所有的资源路径的目录-->
               <directory>${project.basedir}/src/main/resources</directory>
               <!--开启对配置文件的资源加载过滤-->
               <filtering>true</filtering>
           </resource>
       </resources>
   
       <!--针对test目录下的配置资源文件对应的信息-->
       <testResources>
           <testResource>
               <!--各个项目下所有的资源路径的目录-->
               <directory>${project.basedir}/src/test/resources</directory>
               <!--${project.basedir}代表了当前项目所在的文件-->
               <!--开启对配置文件的资源加载过滤-->
               <filtering>true</filtering>
           </testResource>
       </testResources>
   </bulid>
   ```

   设置属性：

   ```xml
   <!--定义自定义的属性-->
   <properties>
       <maven.compiler.source>11</maven.compiler.source>
       <maven.compiler.target>11</maven.compiler.target>
       <spring.version>5.1.9.RELEASE</spring.version>
       <junit.version>4.12</junit.version>
       <jdbc.url>jdbc:mysql://127.0.0.1:3306/ssm_db?useSSL=false</jdbc.url>
   </properties>
   ```

2. jdbc.properties内容：

   ```properties
   jdbc.driver=com.mysql.jdbc.Driver
   # 引用pom文件中的属性
   jdbc.url=${jdbc.url}
   jdbc.username=root
   jdbc.password=12345678
   ```

### 9.6 多环境开发配置

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122185714834.png" alt="image-20220122185714834" style="zoom:50%;" />

> 配置方法

1. 在父工程pom文件中：

   ```xml
   <!--创建多环境-->
   <profiles>
       <!--定义具体的环境：生产环境-->
       <profile>
           <!--定义环境对应的唯一名称-->
           <id>pro_env</id>
           <!--定义环境中使用的属性值-->
           <properties>
               <jdbc.url>jdbc:mysql://127.0.0.1:3306/ssm_db?useSSL=false</jdbc.url>
           </properties>
       </profile>
       <!--定义具体的环境：开发环境-->
       <profile>
           <!--定义环境对应的唯一名称-->
           <id>dev_env</id>
           <!--定义环境中使用的属性值-->
           <properties>
               <jdbc.url>jdbc:mysql://127.0.0.2:3306/ssm_db?useSSL=false</jdbc.url>
           </properties>
           <!--设置默认启动-->
           <activation>
               <activeByDefault>true</activeByDefault>
           </activation>
       </profile>
   </profiles>
   ```

2. 加载指定的环境

   （如不使用此步骤就会使用pom.xml中设置好的默认启动）

   调用格式：mvn 指令 -p 环境id

   eg：mvn install -P dev_env

   使用idea![image-20220122202158001](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122202158001.png)
   

### 9.7 跳过测试

> 应用场景

1. 整体模块功能未开发

2. 模块中某个功能未开发完毕
3. 单个功能更新调试导致其他功能失败
4. 快速打包（因为测试需要耗费时间）
5. …

> 使用操作界面跳过测试

![image-20220122202910416](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220122202910416.png)
