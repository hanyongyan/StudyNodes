# SSM

## Spring

Spring Framework系统架构
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220709181318065.png" alt="image-20220709181318065" style="zoom: 33%;" />

学习路线
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220709182629452.png" alt="image-20220709182629452" style="zoom: 33%;" />

### 核心容器

#### 核心概念（IoC/DI）

- IOC ( Inversion of Control )
  - 使用对象时，由主动 new 产生对象转换为由**`外部`**提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转
- Spring 技术对 IoC 思想进行了实现
  - Spring 提供了一个容器，成为 IoC 容器，用来充当 IoC 思想中的 `外部`
  - IoC 容器负责对象的创建、初始化等一系列工作，被创建或被管理的对象在 IoC 容器中统称为 `Bean`
- ## DI （Dependency Injection）依赖注入
  
  - 在容器中建立 Bean 与 Bean 之间的依赖关系的整个过程，成为依赖注入

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220709201149199.png" alt="image-20220709201149199" style="zoom:50%;" />

​			service 依赖 dao 层 形成了依赖关系

- 目标：**充分解耦**
  - 使用 IoC 容器管理 Bean （IoC）
  - 在 IoC 容器内将有依赖关系的 Bean 进行关系绑定 （DI）
- 最终效果
  - 使用对象时不仅可以直接从 IoC 容器中获取，并且获取到的 bean 已经绑定了所有的依赖关系

##### IoC入门案例

1. 导入Spring坐标

   ```xml
   <dependency>
   	<groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>自己选择</version>
   </dependency>
   ```

2. 定义Spring管理的类（接口）

3. 在`resources目录内`创建Spring配置文件，配置对应类作为Spring管理的bean

   IDEA中新建时会有spring xml的选项创建 一般命名为  `ApplicationContext`

   ```xml
   <!--创建好spring xml 文件以后  在里面添加下面-->	
   <bean id="自己定义名字" class="对应的实现类"></bean>
   <!--可添加多个对应多个实现类-->
   ```

4. 初始化IoC容器，通过容器获取Bean

   ```java
   public class a{
       public static void main(String[] args){
           //下面的ApplicationContext.xml 就是创建的spring xml 文件
           ApplicationContext ac=new ClassPathXmlApplicationContext("ApplicationContext.xml");
           //获取对象
             对象名 t = (对象名) ac.getBean("id名字");
       }
   }
   ```

##### DI入门案例

1. 提供依赖对象对应的 setter 方法

   ```java
   public class xxService {
       //此时的a是空指针
       private xxDao a;
       
       public void setXxDao(xxDao a){
           this.a=a;
       }
   }
   ```

2. 配置service与mapper/dao之间的关系 （依赖注入）也可以使用多个依赖
   设置依赖：service依赖于mapper/dao 

   ```xml
   	<bean id="xxxService" class="对应的service类">
           <!--name 对应是你现在的属性的名称 此处的a对应1中的a  ref对应的Dao的bean的名称-->
   		<propetry name="a" ref="xxxDao"></propetry>
   	</bean>
   
   	<bean id="xxxDao" class="对应的dao类"></bean>
   ```

##### 基础配置

Bean别名 

```xml
<bean id="xxx" name="别名" class="对应的实现类"></bean>
<!--可以定义多个别名 中间使用 逗号 分号 空格 来进行分割都可以-->
<!--获取bean可以通过id和name均可-->
```

Bean作用范围配置  （单例多例

```xml
<bean id="xxx" class="对应的实现类" scope="" ></bean>	
<!--scope有两个值 singleton(默认值)  prototype-->
<!--使用singleton每次创建的对象都是同一个 prototype创建一个是一个-->
<!--表现层对象，业务层对象，数据层对象，工具对象适合使用默认值-->
```

Bean实例化

构造方法（常用,不可传参的方式 ，传参的方式在 [依赖注入构造器注入内](# 依赖注入)

```xml
	<bean id="xxx" class="对应的类"></bean>
```

静态工厂实例化（了解

```java
// 由一个类中的方法来提供对象的实例化
public class xxxFactory{
    //注意是静态的
    public static xxx GetXxx(){
        return new xxxIml;
    }
}
```

```xml
<bean id="自定义" factory-method="类中提供实例化的方法" class="对应的类即上面这一个"></bean>
<!--不添加 factory-method 对应实现的就是 class中的类的实例化-->
```

实例工厂实例化（了解

```java
public class xxxFactory{
    //此处不是静态
    public xxx getXxx(){
        return new xxxImpl;
    }
}
```

```xml
<!--第一个为了配合第二个的使用-->
<bean id="xxxFactory" class="对应的类即上面这一个"></bean>
<bean id="xxx" factory-method="getXxx" factory-bean="xxxFactory 用来指向工厂"></bean>
```

FactoryBean实例化（实用）（实例工厂实例化的

```java
public class xxxFactoryBean implements FactoryBean<xxx>{
    public xxx getObject() throws Exception{
        return new xxximpl();
    }
    public Class<?> getObjectType(){
        return xxx.class;
    }
}
```

```xml
<bean id="xxx" class="xxxBean"></bean>
<!--造出的不是xxxBean的对象  而是xxxBean中的getObject() 方法返回的对象-->
```

 Bean生命周期

生命周期

- 初始化容器
  1. 创建对象（内存分配）
  2. 执行构造方法
  3. 执行属性注入（set操作 有依赖操作的时候）
  4. 执行bean初始化方法
- 使用bean
  1. 执行业务操作
- 关闭/销毁容器
  1. 执行bean销毁方法



- 普通方法

  1. 提供生命周期控制方法

     ```java
     public class xxxImpl implements xxx{
         //两个方法名字自定义
         public void init(){
             
         }
         public void destory(){
             
         }
     }
     ```

  2. 配置生命周期控制方法

     ```xml
     <bean id="xxx" class="xxxImpl" init-method="init" destory-method="destory"></bean>
     <!--init-method destory-method 中的内容对应类中的自定义方法的名字-->
     ```

- 接口控制

  实现InitializingBean，DisposableBean接口

  ```java
  public class xxxServiceImpl implements xxxService ,InitializingBean,DisposableBean{
      //初始化的方法
      public void afterPropertiesSet() throws Exception{
          
      }
      //销毁方法
      public void destory() throws Exception{
          
      }
  }
  ```

##### 依赖注入

依赖注入方式

- setter注入（自己开发更推荐）
  - 简单类型 （如String ，Integer）
  
    ```java
    //在bean中定义引用类型属性并提供可访问的set方法
    public class xxxDaoImpl implements xxxDao{
    	private int conn;
        public void setConn(int conn){
            this.conn=conn;
        }
    }
    ```
  
    ```xml
    <!--在配置中使用property标签value属性注入简单数据类型数据-->
    <bean id="xxxDap" class="对应的实现类">
    	<property name="conn即对应的变量的名字" value="你要设定的值" />
    </bean>
    ```
  
    
  
  - 引用类型  [DI入门案例](#DI入门案例)
  
- 构造器注入
  - 简单类型
  
    ```java
    //在bean中定义引用类型属性并提供可访问的构造方法
    public class xxxDaoImpl implements xxxDao{
        private int conn;
        private String name;
        public xxxDaoImpl(String name , int conn){
            this.name=name;
            this.conn=conn;
        }
    }
    ```
  
    ```xml
    <bean id="xxxDao" class="对应的实现类">
    	<!--根据构造方法参数名注入 name中的值对应的是构造器中的形参名字 耦合性高-->
        <constructor-arg name="name" value="赋值"/>
        <constructor-arg name="conn" value="赋值"/>
        <!--
        	根据构造方法参数类型注入 type对应形参的类型
        	<constructor-arg type="int" value="赋值"/>
        	<constructor-arg type="java.lang.String" value="赋值"/>
    	-->
        <!--
        	根据构造方法参数类型注入 index对应形参的位置 从0开始
        	<constructor-arg index="1" value="赋值"/>
        	<constructor-arg index="0" value="赋值"/>
    	-->
    </bean>
    ```
  
  - 引用类型
  
    ```java
    //在bean中定义引用类型属性并提供可访问的构造方法
    public class xxxServiceImpl implements xxxService{
        private xxxDao xxxDao;
        public xxxServiceImpl(xxxDao xxxDao){
            this.xxxDao=xxxDao;
        }
    }
    ```
  
    ```xml
    <!--配置中使用constructor-arg标签ref属性注入引用类型对象-->
    <bean id="xxxService" class="对应的实现类">
        <!--name属性对应的是上面的构造器中的形参名字 ref指向了这个bean-->
        <!--可以配置多个 更改对应的name和ref属性即可-->
    	<constructor-arg name="xxxDao" ref="xxxDao"/>
    </bean>
    <bean id="xxxDao" class="对应的实现类"/>
    ```

##### 依赖自动装配

- IoC容器根据bean所依赖的资源在容器中自动查找并注入到bean中的过程称为自动装配

- 自动装配用于引用类型依赖注入，不能对简单类型进行操作

- 自动装配方式

  - 按类型（byType，必须保证容器中相同类型的bean唯一，推荐使用

    ```xml
    <!--会自动寻找对应的获取实例化的bean-->	
    <bean id="xxxService" class="对应的service类" autowire="byType"/>
    <!--此处的id不设置也无妨-->
    <bean id="xxxDao" class="对应的dao类"/>
    ```

    

  - 按名称（byName，必须保证容器中具有指定名称的bean，因变量名与配置耦合，不推荐使用

    ```xml
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl"/>
        <!--autowire属性：开启自动装配，通常使用按类型装配-->
    	<!--此处的byName应该对应BookDaoImpl中的setBookDao方法 且setBookDao方法set后面的（bookDao首字母小写）要与依赖的dean的id名称相同-->
    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl" autowire="byName" />
    ```



##### 集合注入

```xml
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
	<property name="BookDaoImpl类中的集合参数名字">
        <!--如果是数组，list，set等参数名字 下面一句内的参数要与类的属性相同-->
        <array/list/set>
        	<value></value>    
            <value></value>  
        </array/list/set>
        <!--map集合注入-->
         <map>
        	<entry key="" value="">
            <entry key="" value="">
        </map>
		<!--properties注入时-->
        <props>
            <prop key=""></prop>
            <prop key=""></prop>

        </props>
    </property>
</bean>
```

##### 加载`properties`文件

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220711191623338.png" alt="image-20220711191623338" style="zoom:50%;" />

```xml
    <!--1.开启context命名空间 即上面的操作-->
    <!--2.使用context空间加载properties文件-->
    <context:property-placeholder location="jdbc.properties" system-properties-mode="NEVER"/>
    <context:property-placeholder location="*.properties" system-properties-mode="NEVER"/>
    <!--location中的值更推荐使用classpath:*.properties获取了全部的properties文件-->
    <!--classpath:*.properties  ：   设置加载当前工程类路径中的所有properties文件
    system-properties-mode属性：是否加载系统属性-->
	<!--classpath*:*.properties  ：   设置加载其他和当前工程类路径中的所有properties文件
    system-properties-mode属性：是否加载系统属性-->
```

使用`properties`中的值的时候需要使用占位符

```xml
<context:property-placeholder location="classpath:*.properties" system-properties-mode="NEVER"/>
 <bean id="DataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
```



##### 容器基本操作

创建容器
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220711192603037.png" alt="image-20220711192603037" style="zoom: 33%;" />

获取bean
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220711192754766.png" alt="image-20220711192754766" style="zoom:33%;" />



#### 注解开发（建议使用）

##### 注解开发定义bean

```java
//使用@component定义bean
@Component("bookDao")
public class BookDaoImpl implements BookDak{
    
}
//使用此办法获取bean时使用   ac.getBean(类的反射);
@Component
public class BookServiceImpl implements BookService{
    
}
```

```xml
<!--使用了上面的方法进行定义后须在配置文件中进行设置-->
<context:component-scan base-package="对应的包名，更大范围的包也行，直接对应到类也行"/>
```

Spring提供@Component注解的三个衍生注解，@Component("bookDao")下面的三个也适用

- @Controller：用于表现层bean定义
- @Service：用于业务层bean定义
- @Repository：用于数据层bean定义

与@Component没有什么区别，只是更好的让你了解属于哪一个层

##### 纯注解开发

使用Java类代替配置文件，开启了Spring快速开发赛道

默认生成的Spring的xml文件装换为类中的 `@Configuration`

对应的配置文件中的设置装换成类中的`@ComponentScan`   衍生注解上面的代码块

```java
//声明当前类为Spring配置类
@Configuration
//设置bean扫描路径，多个路径书写为字符串数组格式
@ComponentScan("对应的包名，更大范围的包也行，直接对应到类也行")
public class SpringConfig{
}
//设定扫面路径时此注解只能添加一次，多个数据使用数组格式
//@ComponentScan({"xx","xx"})
```

使用纯注解开发时Spring核心配置文初始化容器对象切换为读取Java配置类初始化容器对象

```java
ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml")
//转换为
ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class)
```

##### 作用范围和生命周期

```java
@Component
@Scpoe("singleton/prototype")//设置作用范围，单例还是多例
public class BookDaoImpl implements BookDao{
    @PostConstruct  //初始化
    public void init(){
    	//方法的名字进行自定义
    }
    @PreDestory    	//关闭时
    public void destory(){
        //方法的名字自定义
    }
}
```

##### 依赖注入

注入引用类型

```java
//使用@AutoWired注解开启自动装配模式（按类型
//@Qualifier注解开启指定名称装配bean 对应的表现层需要对bean设置名字
@Service
public class BookServiceImple implements BookService{
    @AutoWired//会自动进行搜索dui'y
    @Qualifier("bean的名字")//必须配合@AutoWired使用
    private BookDao BookDao;
    /**  此语句可以省略
   	public void setBookDao(BookDao BookDao){
   		this.BookDao = BookDao;
   	}
    */
	//自动装配基于反射设计创建对象并暴力反射对应属性为私有属性初始化数据，因此无需提供setter方法
}
```

简单类型注入

```java
@Service
public class BookServiceImple implements BookService{
	@Value("${对应的properties属性}")//引号中也可以直接赋值
    private BookDao BookDao;
}
```

加载properties文件

```java
//使用@PropertySource注解加载properties文件
//properties文件一般在resource目录内

@Configuration		//声明当前类为Spring配置类
@ComponentScan("")	//设置bean扫描路径，多个路径书写为字符串数组格式
@PropertySource("")	//路径仅支持单一文件配置，多文件请使用数组格式配置，不允许使用*
public class SpringConfig{
    
}
```



- 使用@Bean配置第三方bean

  ```java
  public class JdbcConfig{
      @Bean
      public DataSource dateSourcr(){
          DruidDataSource ds = new DataSource();
          //相关配置
          return ds;
      }
  }
  ```

- 建议使用独立的配置类管理第三方bean（不要写在SpringConfig中

  ```java
  public class JdbcConfig{
      @Bean
      public DataSource dateSourcr(){
          DruidDataSource ds = new DataSource();
          //相关配置
          return ds;
      }
  }
  //将独立的配置类加入核心配置
  //1.导入式（推荐使用）
  @Configuration
  @Import(JdbcConfig.class)//此注解只能添加一次，多个数据请使用数组格式
  public class SpringConfig{
  }
  //2.扫描式
  //在JdbcConfig上面添加@Configuration
  //SpringConfig上面import更改为@ComponentScan("对应的包来进行扫描")
  ```

- 如果第三方bean需要使用别的bean时

  ```java
  //简单类型
  public class JdbcConfig{
      @Value("值")//此处应该使用properties文件中的值
      private String username;//然后直接使用此变量即可
      @Bean
      public DataSource dateSourcr(){
          DruidDataSource ds = new DataSource();
          //相关配置
          return ds;
      }
  }
  //引用类型注入
  public class JdbcConfig{
      //方法进行传参，对应的参数需要有bean能够获取到，进行了自动装配
      @Bean
      public DataSource dateSourcr(BookDao bookDao){
          DruidDataSource ds = new DataSource();
          //相关配置
          return ds;
      }
  }
  ```



##### XML配置比对注解配置

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220714165104801.png" alt="image-20220714165104801" style="zoom:50%;" />



### 

#### 整合数据层技术MyBatis

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220714211703452.png" alt="image-20220714211703452" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220714211802077.png" alt="image-20220714211802077" style="zoom:50%;" />

```java
//一般会单独定义出这样一个类
//再在SpringConfig中使用Import进行导入，使其可以使用
public class MybatisConfig {
    //定义bean，SqlSessionFactoryBean，用于产生SqlSessionFactory对象
    //此处的DataSource是来自下面的JDBC的配置的bean自动装配来的
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setTypeAliasesPackage("com.itheima.domain");
        ssfb.setDataSource(dataSource);
        return ssfb;
    }
    //定义bean，返回MapperScannerConfigurer对象
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.itheima.dao");
        return msc;
    }
}
```



#### 整合

#### 整合JUnit

导入坐标

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.2.10.RELEASE</version>
</dependency>
```

一般测试类是在test包下进行创建

```java
//设置类运行器 固定格式
@RunWith(SpringJUnit4ClassRunner.class)
//设置Spring环境对应的配置类  相当于设置对应的配置文件
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    //支持自动装配注入bean  如果需要使用bean ke'zai
    @Autowired
    private AccountService accountService;

    @Test
    public void testFindById(){
    }

    @Test
    public void testFindAll(){
    }
}

```



#### 整合JDBC

```java
public class JdbcConfig{
    @Bean
    public DataSource dateSourcr(){
        DruidDataSource ds = new DataSource();
        //相关配置
        return ds;
    }
}
```

- 建议使用独立的配置类管理第三方bean（不要写在SpringConfig中

  ```java
  public class JdbcConfig{
      @Bean
      public DataSource dateSourcr(){
          DruidDataSource ds = new DataSource();
          
          return ds;
      }
  }
  //将独立的配置类加入核心配置
  //1.导入式（推荐使用）
  @Configuration
  @Import(JdbcConfig.class)//此注解只能添加一次，多个数据请使用数组格式
  public class SpringConfig{
  }
  //2.扫描式
  //在JdbcConfig上面添加@Configuration
  //SpringConfig上面import更改为@ComponentScan("对应的包来进行扫描")
  ```

- 如果第三方bean需要使用别的bean时

  ```java
  //简单类型
  public class JdbcConfig{
      
      
      @Value( "${jdbc.driver}")
      private String driver;
      @Value( "${jdbc.url}")
      private String url;
  	@Value( "${jdbc.username}")
      private String username ;
      @Value( "$ijdbc.password}")
      private String password;
  
         @Bean
      public DataSource dateSourcr(){
          DruidDataSource ds = new DataSource();
          ds.setDriverClassName(driver):
          ds.setUrl(url);
          ds.setUsername(username);
          ds.setPassword(password);
          return ds;
      }
  }
  //引用类型注入
  public class JdbcConfig{
      //方法进行传参，对应的参数需要有bean能够获取到，进行了自动装配
      @Bean
      public DataSource dateSourcr(BookDao bookDao){
          DruidDataSource ds = new DataSource();
          //相关配置
          return ds;
      }
  }
  ```

### AOP

#### 核心概念

- 面向切面编程，一种编程范式，知道开发者如何组织程序结构

- 作用：在`不惊动原始设计的基础`上为其进行功能`增强`
- Spring理念：无入侵式/无侵入式
- **编程中，对象与对象之间，方法与方法之间，模块与模块之间都是一个个切面。**

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220715113307702.png" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220715113345008.png" alt="image-20220715113345008" style="zoom:50%;" />

- Aspect（切面）： Aspect 声明类似于 Java 中的类声明，在 Aspect 中会包含着一些 Pointcut 以及相应的 Advice。
- Joint point（连接点）：表示在程序中明确定义的点，典型的包括方法调用，对类成员的访问以及异常处理程序块的执行等等，它自身还可以嵌套其它 joint point。
- Pointcut（切点）：表示一组 joint point，这些 joint point 或是通过逻辑关系组合起来，或是通过通配、正则表达式等方式集中起来，它定义了相应的 Advice 将要发生的地方。
- Advice（增强）：Advice 定义了在 Pointcut 里面定义的程序点具体要做的操作，它通过 before、after 和 around 来区别是在每个 joint point 之前、之后还是代替执行的代码。
- Target（目标对象）：织入 Advice 的目标对象.。
- Weaving（织入）：将 Aspect 和其他对象连接起来, 并创建 Adviced object 的过程

更简明的图

<img src="C:\Users\54656\AppData\Roaming\Typora\typora-user-images\image-20220715114232676.png" alt="image-20220715114232676" style="zoom:50%;" /><img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220715114328142.png" alt="image-20220715114328142" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220715114540720.png" alt="image-20220715114540720" style="zoom:33%;" />

#### AOP入门操作

开发模式：XML or 注解

1. 导入坐标
2. 制作连接点方法（原始操作，Dao接口与实现类）
3. 制作共性功能（通知类与通知）
4. 定义切入点
5. 绑定切入点与通知关系（切面）

```java
//定义通知类，制作通知  类名自定义
@Component	//定义为bean
@Aspect		//让里面的东西进行生效
public class MyAdvice{
    //定义切入点 方法名随便起
    //切入点定义依托一个不具有实际意义的方法进行，即无参数，无返回值，方法体无实际逻辑
    //execution括号里面的执行谁就写谁 void代表返回值
    @Pointcut("execution(void com.itheima.dao.BookDao.update())")
    private void mm(){}
    //制作通知
    //里面的mm()就绑定了上面的
    @Before("mm()")/@After("mm()")//表明是在你指定的方法执行前执行还是执行后执行，配置qie'ru
    public void method(){
        //实现功能
    }
}


@Configuration
@ComponentScan("对应的包")
@EnableAspetJAutoProxy	//开启注解式AOP功能
public class SpringConfig{}
```

#### AOP切入点表达式

- 切入点：要进行增强的方法

- 切入点表达式：要进行增强的方法的描述方式

  - 描述方法一：执行com.itheima.dao包下的BookDao接口中的无参数的update()方法

    `execution(void com.itheima.dao.BookDao.update())`

  - 描述方法二：执行com.itheima.dao.impl包下的BookDaoImpl类中的无参数update方法

    `execution(void com.itheima.dao.impl.BookDaoImpl.update())`



切入表达式标准格式：动作关键字（访问修饰符 返回值 包名.类/接口名.方法名（参数）异常名）

`execution(public User com.itheima.service.UserService.findById(int))`

- 动作关键字：描述切入点的行为动作，例如execution表示执行到指定切入点
- 访问修饰符：一般可以省略
- 返回值
- 包名，类/接口名，方法名，参数
- 异常名：方法定义中抛出指定异常，可以省略

通配符

可以使用通配符描述切入点，快速描述

- \* ：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现

  `execution (public * com.itheima.UserService.find*(*))`

  匹配com.itheima包下的任意包中的UserService类或接口（`一般为接口`）中所有find开头的带有一个参数的方法

- .. ：多个连续的任意符号，可以独立出现，常用于简化包名与参数的书写

  `execution (public User com..UserService.findById(..))`

  匹配com包下的任意包中的UserService类或接口中所有名称为finById的方法，有任意个参数

- \+ ：专用于匹配子类型

  `execution (* *..*Service+.*(..))`

  任意返回值，任意包下的以service结尾的接口/类的子类的任意方法

书写技巧![image-20220715193421976](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220715193421976.png)

#### AOP通知类型

- AOP通知描述了抽取的共性功能，根据共性功能抽取的位置不同，最终运行代码时要将其加入到合适的位置

- AOP通知分为五种类型

  - 前置通知 `@Before`

  - 后置通知 `@After`  两者上面已经介绍过

  - 环绕通知 `@Around`（重点

    ```java
    //参数的传递是必须的，设置了对应的方法在何处实现
    //由于无法预知原始方法运行后是否会抛出异常，因此环绕通知方法必须抛出ThrowAble对象
    @Around("")
    public Object around(ProceedingIoinPoint pjp) throws Throwable{
        //逻辑功能的实现
        Object res = pjp.proceed();//此功能在逻辑功能实现中穿插，对应调用了Around中的方法，如果对应的方法的返回值是void则res为null，返回值设置为void也可以
        return res;
    }
    ```

  - 返回后通知 `@AfterReturning`（了解

    方法完全结束时运行，与 `@After` 没差太多，区别是返回后通知只有方法没有抛出异常是会实现

  - 抛出异常后通知 `@AfterThrowing` 但是异常并不会解除（了解

#### AOP通知获取数据

- 获取切入点参数

  - JoinPoint：适用于前置、后置、返回后、抛出异常后通知

    ```java
    //定义通知类，制作通知  类名自定义
    @Component	//定义为bean
    @Aspect		//让里面的东西进行生效
    public class MyAdvice{
        @Pointcut("execution(void com.itheima.dao.BookDao.update())")
        private void mm(){}
        @Before("mm()")
        public void method(JionPoint jp){
            Object[] args = jp.getArgs();//获取绑定的方法传入的参数
        }
    }
    ```

  - ProceedJoinPoint（上面这个的子接口）：适用与环绕通知

    ```java
    //定义通知类，制作通知  类名自定义
    @Component	//定义为bean
    @Aspect		//让里面的东西进行生效
    public class MyAdvice{
        @Pointcut("execution(void com.itheima.dao.BookDao.update())")
        private void mm(){}
        @Around("mm()")
        public Object method(ProceedingJionPoint pjp){
            Object[] args = pjp.getArgs();//获取绑定的方法传入的参数
            Object res = pjp.proceed()//执行方法， 里面可以穿传入args，不对args进行修改的话，不传参数也一样，修改了args中的内容后绑定的对应方法传入的参数进行了改变
            return res;
        }
    }
    ```

- 获取切入点方法返回值

  - 返回后通知

    ```java
    //定义通知类，制作通知  类名自定义
    @Component	//定义为bean
    @Aspect		//让里面的东西进行生效
    public class MyAdvice{
        @Pointcut("execution(void com.itheima.dao.BookDao.update())")
        private void mm(){}
        @AfterRuturning(value = "mm()",returning = "ret")
        //ret用来获取绑定的方法的返回值
        //上面的ret必须与传入的形参的名称一致，如果需要使用JionPoint这个参数必须在前面
        public void method(JionPoint jp,Object ret){
            //ret就代表绑定的方法的返回值
        }
    }
    ```

  - 环绕通知

    直接使用`Object ret = pjp.peoceed();`获取原方法的返回值即可

- 获取原始异常（了解即可

  - 抛出异常后通知

    ```java
    @AfterThrowing(value = "pt()", throwing = "t")
    public void afterThrowing(Throwable t){
        //使用异常即可
    }
    ```

  - 环绕通知

    ```java
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        Object ret = null;
        try{
            ret=pjp.proceed();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return ret;
    }
    ```

    

### 事务

```java
//Spring提供的接口
public interface PlatformTransactionManager{
    //提交事务
    void commit(TransactionStatus status) throws TransactionException;
    //回滚事务
    void rollback(TransactionStatus status) throws TransactionException;
}
```

```java
//Spring提供的实现类，使用的是jdbc的事务
public class DataSourcrTransactionManager{
    ...
}
```

1. 在你要使用的地方添加注解，开启Spring的事务管理

   ```java
   public interface AccountService{
       @Transactional//一般写到接口上方
       //模拟银行的转账
       public void transfer(String out,String in,Double money);
   }
   
   //Spring注解式事务通常添加在业务层接口中而不会添加到业务层实现类中，降低耦合性
   //注解式事务可以添加到业务方法上表示当前方法开启事务，也可以添加到接口上表示当前接口所有方法开启事务
   ```

2. 设置事务管理器

   ```java
   //使用DataSource是必须的
   //DataSource会自动进行获取，进行了连接数据库
   @Bean
   public PlatformTransactionManager transactionManager(DataSource dataSource){
       DataSourceTransactionManager ptm = new DataSourceTransactionManager();
       ptm.setDataSource(dataSource);
       return ptm;
   }
   ```

3. 开启注解式事务驱动

   ```java
   @Configuration//开启注解
   @ComponentScan("com.itheima")//bean扫描
   @PropertySource("classpath:jdbc.properties")//扫描properties文件
   @Import({JdbcConfig.class,Mybatis.class})//导入独立的配置
   @EnableTransactionManangement//开启事务
   public class SpringConfig{}
   ```

事务相关配置
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220716214348838.png" alt="image-20220716214348838" style="zoom:50%;" />

```java
//设置事务回滚异常，只有遇到Error和运行时异常时事务会自动回滚，其他的事务回滚会不触发
public interface AccountService{
    @Transactional(rollbackFor = {IOException.class})
    //模拟银行的转账
    public void transfer(String out,String in,Double money);
}
```

```java
//事务传播行为
@Service
public class LogServiceImpl implements LogService{
    @Autowired
    private LogDao logDao;
    //当你需要开启一个新的事务时使用下面的这一句
    @Transactional(propagation = Propagation.RrQUIRES_NEW)
    public void log(String out,String in,Double money){
        logDao.log("转账操作由"+out+"到"+in+"，金额"+money);
    }
}
```

## SpringMvc

SpringMVC技术与Servlet技术功能相同，均属于web层开发技术，使用简单，开发便捷

### 入门案例

1. 先导入SpringMVC坐标和Servler坐标

   ```xml
   <dependency>
   	<groupId>javax.servlet< /groupId>
   	<artifactId>javax.servlet-api</ artifactId>
       <version>3.1.0</version>
   	<scope>provided< / scope>
   </dependency>
   <dependency>
   	<groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>5.2.10.RELEASE</version>
   </dependency>
   ```

2. 创建SpringMVC控制器类

   ```java
   @Controller//设定SpingMvc的核心控制器bean
   public class UserController{
       @RequestMapping("/save")//设置访问路径
       @ResponseBody//设置当前控制器方法响应内容为当前返回值，无需解析
       public String save(){
           return "";//需要返回的数据直接返回即可
       }
   }
   ```

3. 初始化SpringMVC环境，设定SpringMVC加载对应的bean

   ```java
   @Configuration
   @ComponentScan("包扫描")
   public class SpringMvcConfig{
   }
   ```

4. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220717172117889.png" alt="image-20220717172117889" style="zoom:50%;" />
   另一种写法更加简便

   ```java
   public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
       protected Class<?>[] getRootConfigClasses() {
           return new Class[0];
       }
   
       protected Class<?>[] getServletConfigClasses() {
           return new Class[]{SpringMvcConfig.class};
       }
   
       protected String[] getServletMappings() {
           return new String[]{"/"};
       }
   //上面是另一种写法 下面的是解决乱码问题的
       //乱码处理
       @Override
       protected Filter[] getServletFilters() {
           CharacterEncodingFilter filter = new CharacterEncodingFilter();
           filter.setEncoding("UTF-8");
           return new Filter[]{filter};
       }
   }
   //此写法以后基本固定
   ```

   

SpringMVC有其管理的bean，Spring也有其管理的bean，防止两者起冲突

```java
@Configuration
@ComponentScan("com.itheima.controller")//SpringMVC的管理bean
@EnableWebMvc//开启了好多东西，比如JSON转换为对象
public class SpringMvcConfig {
}

@Configuration
//@ComponentScan({"com.itheima.service","com.itheima.dao"})  方法一。
//设置spring配置类加载bean时的过滤规则，当前要求排除掉表现层对应的bean
//excludeFilters属性：设置扫描加载bean时，排除的过滤规则
//type属性：设置排除规则，当前使用按照bean定义时的注解类型进行排除
//classes属性：设置排除的具体注解类，当前设置排除@Controller定义的bean
@ComponentScan(value="com.itheima",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = Controller.class
    )
)  //Spring管理的bean
public class SpringConfig {
}
//第三种：不区分Spring和SpringMVC的环境，加载到同一个环境中
```

### REST风格

REST，表现形式状态转换

- 传统风格资源描述形式

  http://localhost/user/getById?id=1

  http://localhost/user/saveUser

- REST风格描述

  http://localhost/user/1

  http://localhost/user

- 优点：

  - 隐藏资源的访问行为，无法通过地址得知对资源是何种操作
  - 书写简化

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220717234654942.png" alt="image-20220717234654942" style="zoom:50%;" />
GET可以简写为@GetMapping，其他同理	



1. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220718102636355.png" alt="image-20220718102636355" style="zoom:50%;" />
2. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220718102702218.png" alt="image-20220718102702218" style="zoom:50%;" />



####  @RequestParam  @RequestBody   @PathVariable

- 区别
  - @RequestParam 用于接收url地址传参或表单传参，
  
    只有一个数据或者数据时使用
  
    传来的是数组使用 List 接收
  
  - @RequestBody  用于接受json数据
  
  -  @PathVariable 用于接收路径参数，实用{参数名称}描述路径参数 
  
- 应用
  - 后期开发中，发送请求参数超过1个时，以json格式为主，@RequestBody 应用较广
  - 如果使用非JSON格式数据，选用@RequestParam接受请求参数
  - 采用RESTful进行开发，当参数数量较少时，例如一个，可以采用@PathVariable接受请求路径变量，通常用于传递id值

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220718174240916.png" alt="image-20220718174240916" style="zoom:50%;" />

### 请求参数

- 普通参数：url地址传参，地址参数与形参变量名相同，定义形参即可接受参数

  ```
  Get请求
  http://localhost/commonParam?name=itcast  			单个参数的形式
  http://localhost/commonParam?name=itcast&age=15		多个参数的形式  
  Post请求传入的数据与方法中的参数一直即可
  ```

  ```java
  @RestController
  @RequestMapping("/commonParam")		//设置了访问路径
  public class UserController {
      //与url传来的参数名字相同即可，url有多个参数，方法中就配多少个参数
      //如传来的参数名字为name，形参的名字为username用此办法解决参数不一致的情况
      public String commonParam(@RequestPaam("name") String userName){
      }
  }
  ```

- pojo数据类型

  简单数据类型一般处理的是参数个数比较少的请求，如果参数比较多，那么后台接收参数的时候就比较复杂，这个时候我们可以考虑使用POJO数据类型。

  POJO参数：请求参数名与形参对象属性名相同，定义POJO类型形参即可接收参数

  **对应名称必须一致**

  ```
  http://localhost/commonParam?name=itcast&age=15
  ```

  ```java
  public class user{
      private String name;
  }
  //后台接收参数
  public String pojoParam(User user){
  }
  ```

- 嵌套pojo类型参数

  ```java
  public class Address{
      private String province;
      private user user;
  }
  ```

  ```
  发送请求接受数据
  http://localhost/commonParam?province=henan&user.name=java
  ```

- 数组参数请求：请求参数名与形参对象属性名相同且请求参数为多个，定义数组类型即可接收参数

  ```
  http://localhost/commonParam?like=lanqiu&like=pingpong
  ```

  ```java
  public String arrayParam(String[] likes){
          return "{'module':'array param'}";
  }
  //此处的返回值无所谓
  ```

- 集合参数类型

  ```
  http://localhost/commonParam?like=lanqiu&like=pingpong
  ```

  ```java
  public String listParam(@RequestParam List<String> likes){
      return "{'module':'list param'}";
  }
  ```

  `@RequestParam`必须使用，否则SpringMvc会将list看成一个pojo对象处理





### web端页面解除过滤

SpringMVC对静态资源进行放行

```java
//一般放在config包下
@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {
    //设置静态资源访问过滤，当前类需要设置为配置类，并被扫描加载
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//当访问/pages/????时候，从/pages目录下查找内容
        registry.addResourceHandler("/pages/**").addResourceLocations("/pages/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("/plugins/");
    }
}
//在SpringConfig中对其进行扫描
```



### 表现层数据封装

为了避免前端获取数据麻烦，定义一个数据结果返回类

```java
//例：
public class result{
	public Object data;
    public Integer code;
    private String msg;
    //一般会有三个构造方法
    //public result(){}
    //public result(Integer i, Object o){}
    //public resule(Integer i, Object o, msg m){}   一个传信息，一个不传信息
}
//result类中的字段不固定，可以根据需要自行删减
public class result{
    private Boolean flag;//代表是否查询成功
    private Object date;//dai
}
```

### 异常处理器

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220718234213908.png" alt="image-20220718234213908" style="zoom:50%;" />

1. 各个层级均会出现异常，异常处理代码书写在那一层

   所有的异常均抛出到表现层进行处理

2. 表现层处理异常，每个方法单独书写，代码书写量巨大且意义不强，如何解决？

   AOP思想



异常处理器

- 集中的，统一的处理项目中出现的异常，一般统一处理到表现层

```java
//一边写到controller包内 如不写在这里记得再SpringMVC里扫描到其所在的包
@RestControllerAdvice
//因为我们上面使用了rest风格就要使用这种，如不使用rest风格去掉rest即可
public class ProjectExceptionAdvice{
    //定义处理哪一种异常，比如下面就是处理Exception异常
    //如不添加就是拦截所有的异常
    @ExceptionHandler(Exception.class)
    public result doReception(Exception e){
        //	处理异常
        return new result();//返回一个信息
    }
}
-----------------------
@ControllerAdvice(annotation = {RestController.class})
//处理对应的使用了RestController的注解的
//等同@RestControllerAdvice
```

那么业务层，数据层怎么集中到处理层处理呢？

- 项目异常分类
  - 业务异常（BusinessException）
    - 规范的用户行为产生的一场
    - 不规范的用户行为操作产生的异常
  - 系统异常（SystemException）
    - 项目运行过程中可预计且无法避免的异常
  - 其他异常（Exception）
    - 编程人员未预期到的异常

1. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719123247924.png" alt="image-20220719123247924" style="zoom:33%;" />
2. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719123303905.png" alt="image-20220719123303905" style="zoom:33%;" />
3. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719123310677.png" alt="image-20220719123310677" style="zoom:33%;" />
4. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719123354772.png" alt="image-20220719123354772" style="zoom:33%;" />
5. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719123409095.png" alt="image-20220719123409095" style="zoom:33%;" />
6. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719124020027.png" alt="image-20220719124020027" style="zoom:33%;" />



### 拦截器

- 拦截器（Interceptor）是一种动态拦截方法调用的机制
- 作用：
  - 在指定的方法调用前后执行预先设定好的代码
  - 组织原始方法的执行
- 拦截器与过滤器区别
  - 归属不同：Filter属于Servlet技术，Interceptor属于SpringMVC技术
  - 拦截内容不同：Filter对所有访问进行增强，Interceptor仅针对于SpringMVC的访问进行增强

#### 入门案例

1. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719215253878.png" alt="image-20220719215253878" style="zoom:50%;" />
2. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719215808307.png" alt="image-20220719215808307" style="zoom:50%;" />
3. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719215918153.png" alt="image-20220719215918153" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719220157598.png" alt="image-20220719220157598" style="zoom:50%;" />



拦截器链

- 当配置多个拦截器时，形成拦截器链

- 拦截器的运行顺序参照拦截器添加顺序为准

- 当拦截器中出现对原始处理器的拦截，后面的拦截器均终止运行

- 当拦截器运行中断，仅配置在前面的拦截器的afterCompletion操作

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220719222744043.png" alt="image-20220719222744043" style="zoom:50%;" />





## SpringBoot

![image-20220721102105670](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220721102105670.png)

SpringBoot项目快速启动

1. 对SpringBoot项目打包（执行Maven构建指令package）

2. 在对应生成 jar 包的目录下使用 cmd 命令  `java -jar 文件名.jar`

   jar 支持命令行启动需要依赖maven插件支持，请确认打包时是否具有SpringBoot对应的maven插件

   ```xml
   <build>
   	<plugins>
       	<plugin>
           	<groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>
       </plugins>
   </build>
   ```



### 起步依赖

- starter

  SpringBoot中常见项目定义，定义了当前项目使用的所有项目坐标，以达到减少以来配置的目的

- 使用任意坐标时，仅书写`groupId`和 `artifactId`，`version`则不用写（SpringBoot中写了超级多依赖）

  如发生坐标错误，再指定version（要小心版本冲突）

### 辅助功能

使用maven依赖管理变更起步依赖项

![](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220721112645360.png)

### 基础配置

配置文件中如果写了中文，打包时可能会报错，可在IDEA中修改编码格式来进行解决
设置中搜索编码，将里面的编码一类的改为`UTF-8`

- 配置文件格式

  - SpringBoot提供了多种属性配置方式

    - application.properties

      `server.port=80`

    - application.yml

      ```yaml
      server:
      	port: 80
      //此处使用的代码格式是yaml的其实yaml和yml差不多
      ```

    - application.yaml

      ```yaml
      server:
      	port: 80
      ```

      优先级顺序是从上到下的

      在使用时，通过关键字 + 提示修改属性

    一般使用yml格式的配置文件

    使用yml格式的文件配置时自动提示功能消失

  - <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220721133955464.png" alt="image-20220721133955464" style="zoom:50%;" />

  

#### Maven与SpringBoot关联操作（多环境配置

1. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220722221627509.png" alt="image-20220722221627509" style="zoom:50%;" />
2. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220722221730868.png" alt="image-20220722221730868" style="zoom:50%;" />
3. <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220722221821912.png" alt="image-20220722221821912" style="zoom:50%;" />



### SpringBoot整合JUnit

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220722230846447.png" alt="image-20220722230846447" style="zoom:50%;" />

如果测试类再SpringBoot启动类的包或子包中，可以省略启动类的设置，也就是省略classes的设定

否则要这样写![image-20220722231157335](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220722231157335.png)



### SprintBoot整合Mybatis

Mapper/Dao层，pojo自己提供，配置文件配置一下用户名密码等必须的东西

Mapper层需要添加注解 `@Mapper`

配置文件使用 `yaml`格式
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220723094355806.png" alt="image-20220723094355806" style="zoom:67%;" />

druid坐标需要自己添加

配置结束

### SpringBoot整合MybatisPlus

[MybatisPlus](#  MybatisPlus)

### SpringBoot整合Druid

导入坐标

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.11</version>
</dependency>
```

配置文件配置

```yaml
# 推荐使用
spring: 
 datasource: 
  druid:
   driver-class-name: com.mysql.cj.jdbc.Driver
   url: jdbc:mysql://localhost:3306/数据库名
   username: root
   password: 密码
# 或者
spring: 
 datasource: 
   driver-class-name: com.mysql.cj.jdbc.Driver
   url: jdbc:mysql://localhost:3306/数据库名
   username: root
   password: 密码
   type: com.alibaba.druid.pool.DruidDataSource
```



### 整合任意第三方技术

- 导入对应的starter
- 配置对应的设置或采用默认配置



## SpringBoot运维实用篇

### 打包与运行

#### 程序打包与运行（Windows版

1. 点击package进行打包，打包时点击切换跳过测试模式，不会执行test包内的测试类

   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220726120212767.png" alt="image-20220726120212767" style="zoom:50%;" />

2. 运行项目

   `java -jar 工程名.jar`

jar支持命令行启动需要依赖maven插件支持，请确认打包时是否具有SpringBoot对应的maven插件

```xml
<build>
	<plugins>
    	<groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugins>
</build>
```

命令行启动常见问题及解决方案

```shell
# 查询端口
netstat -ano
# 查询指定端口 推荐使用 是模糊查询
netstat -ano | findstr "端口号"
# 根据进程PID查询进程名称  可以使用一下查看了解，可以查看谁在用
tasklist | findstr "进程PID号"
# 根据PID杀死任务
taskkill -f -pid "进程PID号"
# 根据进程名称杀死任务  进程名称可能对应着多个进程，会一下杀掉完
taskkill -f -t -im "进程名称"
```



#### 程序运行（Linux版











### 配置高级

#### 临时属性设置

使用 jar 命令启动 SpringBoot 工程时可以使用临时属性替换配置文件中的属性

比如写好的配置文件中的端口号为80，但是你的80端口有更重要的东西在运行，就可以使用临时属性来设置

使用cmd运行时添加后缀即可

`java -jar 项目名.jar --server.port=8080`

就使得使用的端口为8080

使用规则 `--对应的属性文件中的设置` 只不过是从yaml格式转换为了properties文件格式

要想配置更多的属性继续在后面使用 `--配置`

临时属性必须时当前boot工程支持的属性，否则设置无效

------

我们可以通过编程形式带参数启动SpringBoot程序，为程序添加运行参数

```java
//设置以后通过cmd命令也无法使用临时参数
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        String[] arg = new String[1];
        //将端口号设置为8080
        arg[0] = "--server.port=8080";
        SpringApplication.run(SpringBootDemoApplication.class, arg);
    }
}
//不携带参数启动时，去掉方法里面的arg
```



#### 配置文件分类

SpringBoot中4级配置文件

级别从上到下依次降低   配置文件是互补的

1. 打包出来的 jar 包所在文件夹内创建 config/application.yml 进行配置  
2. jar 包所在的文件夹中的配置文件 application.yml
3. 工程中配置文件所在的文件夹内新建  config/application.yml
4. 配置文件

- 一级与二级留作系统打包后设置通用属性，一级常用于运维经理进行线上整体项目部署方案调控
- 三级与四级用于系统开发阶段设置通用属性，三级常用于项目经理进行整体项目属性调控

### 多环境开发

在一个项目中一般会有生产，开发，测试三个环境，如何切换使用的

```yaml
spring:
  profiles:
    active: test  #表明当前使用的环境
--- # 用于区分环境，如不添加会报错
spring:				
  profile: dev    # 环境的名字
server: 			
  port: 80
---
spring:					
  profile: test    		#设置生产环境
server: 				#生产环境具体参数设定
  port: 81
```

也可拆分来写

每一个独立的配置与原配置文件所在层级相同，命名为 `application-名字.yml`
然后再application.yml中进行配置具体使用哪一个配置文件

```yaml
spring:
  profiles:
    active: test  #表明当前使用的环境
```

application.yml是主环境配置文件其余的是环境分类配置文件

主环境文件中设置公共配置（全局

环境分类配置文件中常用于设置冲突属性（局部

properties配置文件基本相同

- 一般根据功能对配置文件中的信息进行拆分，并制作成独立的配置文件，命名规则如下

  - application-devMVC.yml

- 使用include属性再激活指定环境的情况下，同时对多个环境进行加载使其生效，多个环境使用逗号分隔

  当include中的配置冲突时会使用最后加载的比如下面的devMVC，如果主环境中有相同的配置，使用主环境中的配置

  ```yaml
  spring: 
   profiles: 
     active: dev
     inclue: devDB,devMVC
  ```

也可以使用group属性代替include属性，降低了配置书写量

```yaml
spring: 
 profiles: 
   active: dev
   group: 
     "dev": devDB,devMVC
     "pro": proDB,ProMVC
```

更推荐使用group，便于线上维护管理，但是有冲突的配置会使用最后加载的文件的配置

#### 多环境开发控制

当Maven与SpringBoot多环境产生冲突时，我们应该让maven使用boot中的环境，因为boot是依赖Maven运行的，Maven的配置会更优先

1. Maven中设置多环境属性

   ```xml
   <profiles>
   	<profile>
       	<id>dev_env</id><!--此处的id自定义设置-->
           <properties>
               <!--方便在spring配置文件中引用-->
           	<profile.active>dev</profile.active>
           </properties>
           <activation>
               <!--设置是否默认启动此环境-->
           	<activeByDefault>true</activeByDefault>
           </activation>
       </profile>
       
       <profile>
       	<id>pro_env</id>
           <properties>
           	<profile.active>pro</profile.active>
           </properties>
       </profile>
   </profiles>
   ```

2. Spring Boot中引用Maven属性

   ```yaml
   spring: 
    profiles:
      active: @profile.active@  #引用了maven中默认启动的属性的<profile.active>中的
   ```


如果在使用中切换配置，但是运行时配置不更新的情况，点击maven中的clean也没有的话，点击compile即可解决问题

### 日志

#### 日志基础

- 编程期调试代码
- 运营期记录信息
  - 记录日常运营重要信息（峰值流浪，平均响应时长
  - 记录应用报错信息（错误堆栈
  - 记录运维过程数据（扩容，宕机，报警。。。

日志级别设置

```yaml
logging:
  level:
    root: # 来填写级别  默认为info
```

代码中使用日志工具记录日志
使用Lombok提供的注解 @Slf4j 简化开发，煎炒日志对象的声明操作

```java
@Slf4j
//其他的注解该配配
public class BookController {
   @GetMapping
    public String getById(){
        //注解生成的对象就叫log
        log.info("infog...")//会在控制台打印出日志
    }
}
```

日志级别

- trace：运行堆栈信息，使用率低
- debug：程序员调试代码使用
- info：记录运维过程数据
- warn：记录运维过程报警信息
- error：记录错误堆栈信息

设置日志输出级别

```yaml
# 开启debug模式，输出调试信息，常用于检查系统运行状况
debug: true
# 设置日志级别，root表示根节点，即整体应用日志级别
logging:
	# 自定义组名，设置当前组中所包含的包,推荐使用日志组
    group:
      ebank: con.demo.controller
    level:
       root: info
       # 为对应组设置日志级别
       ebank: debug
       # 对包设置日志级别
       com.demo.service: info
```

#### 日志输出格式控制

![image-20220727124323343](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220727124323343.png)

- PID：进程ID，用于表明当前操作所处的进程，当多服务同时记录日志时，该值可用于协调程序员调试程序
- 所属类/接口名：当前显示信息为SpringBoot重写后的信息，名称过长时，简化包名书写为首字母，甚至直接删除

设置日志输出格式

```yaml
logging:
  pattern: 
    console: "%d %clr(%5p) --- [%t] %-40.40c %m %n"
# %d：日期
# %m：消息
# %n：换行
# %p：日志级别  %5p设置占位符  通用  默认右对其 设置左对齐使用-
# %clr()：设置颜色
# %t：所属进程
# %c：类名   %-40.40c左对齐一共四十位，.40代表强制截断为40位，超出的直接截掉
```



#### 日志文件

生成的日志文件跟项目所在同一目录

设置日志文件

```yaml
logging: 
   file:
     name: server.log  #设置了文件的名字
#单一的日志文件  不推荐使用
```

日志文件详细配置

```yaml
logging:
  file:
    name: server.log
  logback:
    rollingpolicy:
      max-file-size: 1MB #设置单个日志文件的最大值
      file-name-pattern: server.%d{yyyy-MM-dd}.%i.log #设置文件的命名格式
      										# %d是日期 括号里面的是日期格式
```



## SpringBoot开发实用篇

### 热部署

**只在开发环境中有效**

当项目中的文件发生改变以后不需要重启服务器

热部署仅加载当前开发者自定义开发的资源，不加载jar资源

导入坐标

```xml
<!--开启开发者工具-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```



手工启动热部署 Ctrl-F9

自动启动热部署<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220727162035556.png" alt="image-20220727162035556" style="zoom: 50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220727163807674.png" alt="image-20220727163807674" style="zoom: 50%;" />

会在idea失去焦点五秒以后启动热部署

#### 热部署配置范围

```yaml
spring:
  devtools:
    restart:
      exclude:  static/**  # resource/static文件内的资源不会启动热部署，默认从resource开始  /**代表全部文件 单个文件直接指定名字。
```

默认不触发重启的目录列表

- /META-INF/maven
- /META-INF/resources
- /resources
- /statis
- /public
- /templates

#### 关闭热部署

```yaml
spring:
  devtools:
    restart:
      enabled: false
```

还可以设置高优先级属性禁用热部署

```java
public static void main(String[] args){
    System.serProperty("spring.devtools.restart.enabled","false");
    SpringApplication.run(类名.class)
}
```



### 配置高级

#### 第三方bean属性绑定

使用@configurationProperties为第三方bean绑定属性

如果使用@configurationProperties出现注释警告
导入坐标

```xml
<dependency>
	<groupId>org,springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```



```java
@Bean
@configurationProperties(prefix = "datasource")
public DruidDataSource dateSource(){
    DruidDataSource ds = new DruidDataSource();
    return ds;
}
```

```yaml
datasource:
  driverClassName: com.mysql.jdbc.Driver
```

上面的ds对象有 driverClassName 此属性，故可以完成第三方bean绑定属性

[yaml文件读取](# yaml文件怎么读取)方法三 同为@configurationProperties配置的讲解

@EnableConfigurationProperties注解可以将使用@ConfigurationProperties注解对应的类加入Spring容器，相当于将其设置成了bean

@EnableConfigurationProperties与@Component不能同时使用，因为@Component也是将类设置为bena

#### 宽松绑定/松散绑定

@configurationProperties绑定属性支持属性名宽松绑定

也就是说配置文件中的配置的名称的命名格式与类中属性的命名格式可以不一样

```yaml
ipaddress: 11111
#ip-address  ip_address ip_a-dd-ress  都可以匹配成功
```

```java
private String ipAddress
```

@configurationProperties( prefix = "...")  prefix的命名规范：仅能使用纯小写字符，数字，下划线作为合法的字符

@Value不支持松散绑定



#### 常用时间与空间计量单位

```java
@ConfigurationProperties(prefix = "servers")
public class ServerConfig{
    @DurationUnit(按照提示填写选择你想要的)
    private Duration serverTimeOut
    @DataSizeUnit(按照提示填写选择你想要的)
    private DataSize dataSize;    
}
```

```yaml
servers: 
  serverTimeOut: 100
  dataSize: 100
```



#### Bean属性校验

开启数据校验有助于系统安全性，

先导入校验规范的坐标

```xml
<!--第一步-->
<!--导入规范-->
<dependency>
	<groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
</dependency>
<!--使用hibernate框架提供的校验器做实现类-->
<dependency>
	<groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

```java
//第二部：开启校验功能
@Validated
public class ServerConfig{
    //第三步：设置校验规则
    @Max(value = 400, message = "最大值不能超过四百")
    private int port;
    //点击max查看其对应的包，可以查看所有的校验规则
}
```



#### 进制数据转换规则

[点击这里查看，字面值表达式那里](# yml/yaml)

### 测试

#### 加载测试专用属性

在启动测试环境时可以通过properties参数设置测试环境专用的属性

```java
@SpringBootTest(properties = {"test.prop=testValue"})
public class pripertiesAndArgsTest{
    @Value("${test.prop}")
    private String msg;
    @Test
    void Test(){
        System.out.println(msg);
    }
}
//优势：比多环境开发中的测试环境影响范围更小，仅对当前测试类有效
```

在启动测试环境时可以通过args参数设置测试环境专用的传入参数

[详细介绍](# 临时属性设置)

```java
@SpringBootTest(args = {"--test.arg=testValue"})
public class propertiesAndArgsTest{
    @Value("${test.arg}")
    private String msg;
     void Test(){
        System.out.println(msg);
    }
}


```

#### 加载测试专用配置

使用@Import注解加载当前测试类专用的配置

当我们需要一个单独的bean只在测试用例中使用时，我们可以在test包下对这个bean进行构建

```java
@Configuration
public class testBean{
    @Bean
    private String msgTest(){
        return "msg";
    }
}

@SpringBootTest
@Import(testBean.class)
public class configurationTest{
    @Autowired
    privete String name;
    @Tese
    void testConfiguration(){
        sout(msg);
    }
} 
```

#### Web环境模拟测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebTest{
    @Test
    void testRandomPort(){}
}
```

NONE：不启动web服务

RANDOM_PORT：启动随机端口

DEFINED_PORT：启动配置文件中的端口

**虚拟请求测试**

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//开启虚拟MVC调用
@AutoConfigureMockMvc
public class WebTest{
    @Test
    //注入虚拟MVC调用对象
    void testWeb(@Autowired MockMvc mvc) throws Exception{
        //创建虚拟请求，当前使用get请求访问/books
     MockHttpServletRequesrBuilder builder = MockMvcRequestBuilders.get("/books");
     	//执行请求
     ResultActions action = mvc.perform(builder);
    }
}
```

**匹配响应执行状态**

```java
@Test
public void testStatus(@Autowired MockMvc mvc)throws Exceptiom{
	MockHttpServletRequesrBuilder builder = MockMvcRequestBuilders.get("/books");
    ResultActions action = mvc.perform(builder);
    //匹配执行状态（是否是预期值
    //定义执行状态匹配器
    StatusResultMatchers status = MockMvcResultMatchers.status();
    //定义预期执行状态,即设置你预期返回的状态码
    ResultMatcher ok = status.isOk();
    //使用本次真实执行结果与预期结果进行对比
    //action对象里面会有真正的状态码，与你预期的状态码两者进行对比
    action.andExpect(ok);
}
```

**匹配响应体**

```java
@Test
public void testBody(@Autowired MockMvc mvc)throws Exceptiom{
	MockHttpServletRequesrBuilder builder = MockMvcRequestBuilders.get("/books");
    ResultActions action = mvc.perform(builder);
    //匹配执行状态（是否是预期值
    //定义执行状态匹配器
    StatusResultMatchers content = MockMvcResultMatchers.content();
    //定义预期执行状态
    ResultMatcher result = content.string("");
    //使用本次真实执行结果与预期结果进行对比
    //action对象里面会有真正的对应的返回值，与你预期的返回值两者进行对比
    action.andExpect(result);
}
```

**匹配响应体JSON**（两者只是获取的时候对结果的解析方式不同

```java
@Test
public void testBodyJson(@Autowired MockMvc mvc)throws Exceptiom{
	MockHttpServletRequesrBuilder builder = MockMvcRequestBuilders.get("/books");
    ResultActions action = mvc.perform(builder);
    //匹配执行状态（是否是预期值
    //定义执行状态匹配器
    StatusResultMatchers content = MockMvcResultMatchers.content();
    //定义预期执行状态
    ResultMatcher result = content.json("对应的json数据");
    //使用本次真实执行结果与预期结果进行对比
    //action对象里面会有真正的对应的返回值，与你预期的返回值两者进行对比
    action.andExpect(result);
}
```

匹配其他的东西`MockMvcResultMatchers.content();`后面的content更改一下就行，使用提示可以查看里面都是有什么东西

#### 业务层测试回滚

当在进行测试业务时，不想把测试的数据添加到数据库中应开启事务，在对应的方法或者类上方添加注解 `@Transactional`SpringBoot会对测试用例对应的事务提交操作进行回滚

如果想在测试用例中提交事务，可以通过 `@Rollback(false)`注解设置，相当于两者抵消了



#### 测试用例设置随机数据

测试用例数据通常采用随机值进行测试，使用SpringBoot提供的随机数为其赋值

```yaml
testcase: 
  book: 
    id: ${random.int}  				# 随机整数
    id2: ${random.int(10)} 			# 10 以内随机数
    type: ${random.int(10,20)}		# 10 ~ 20随机数
    								# 不仅仅是只有int类型
    								# value对应的是string类型 long对应long
```

`@ConfigurationProperties(perfix = "testcase.book")`然后使用此注解进行读取即可	

### 数据层解决方案

#### SQL

- 现有数据层解决方案技术选型

  Druid + MybatisPlus + MySql

  













#### NoSQL

##### Redis

Redis是一款key-value存储结构的内存及NoSQL数据库

- 支持多种数据存储格式
- 支持持久化
- 支持集群

SpringBoot整合Redis
先导入坐标

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

进行配置

##### Mongodb

是一个开源，高性能，无模式的文档行数据库，最像关系型数据库的非关系型数据库

 **适用条件**有很高的修改需求









### 整合第三方技术

#### 缓存

缓存是一种介于数据永久存储介质与数据应用之间的临时存储介质

使用缓存可以有效地减少低速数据读取过程的次数（例如磁盘IO），提高系统性能

缓存不仅可以用于提高永久性存储介质的数据读取效率，还可以提供临时的数据存储空间

#### SpringBoot缓存使用方法

1. 启用缓存
2. 设置进入缓存的数据
3. 设置读取缓存的数据

导入坐标

```xml
<!--引用缓存-->
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

application.java文件中添加注解 `@EnableCaching`

```java
@SpringBootApplication
//开启缓存功能
@EnableCaching
public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
```

设置当前操作的结果数据进入缓存

```java
//value的值自定义，相当于命名空间，key的值代表在进行查询时会先根据id在缓存数据中查询
//一般key的值与查询方法传入的参数值对应
//这个注解会存入缓存，当你第一次使用后再传入同样的id不会更新数据，如果有需求传入相同的id返回的结果不能够固定，应使用 @CachePut注解，传入参数跟这一个保持一致，传入相同的id时会更新数据，只存入数据
//
@CachePut(value = "smsCode",key="#tele")
public String sendCodeToSMS(String tele) {
    //此处是调用了根据手机号生成验证码的方法
    String code = codeUtils.generator(tele);
    return code;
}
```

```java
//当你使用 @CachePut 这个注解时，获取对应的缓存数据时，使用 @Cacheable 注解，设置相同的命名空间，格式如下
//@CachePut 注解是只存入数据
//@Cacheable 是获取缓存中的数据，里面的value值和key值应该传入相同的数据
@Cacheable(value = "smsCode",key="#tele")
public String get(String tele){
    return null;
}
//虽说这里返回的是 null 值，但是当你的缓存中含有传入的tele时，会返回缓存中对应的值
```

$\textcolor{red}{}$$\textcolor{red}{@Cacheable}$ 此注解要想生效必须设置在 Spring 的 Bean 中

上面使用的是 Spring 的默认缓存



##### Ehcache缓存技术

SpringBoot提供的缓存技术除了提供默认的缓存方案，还可以对其他缓存技术进行整合，统一接口，方便缓存技术的开发与管理

更换其他的缓存解决方案差不多

1. 加入Ehcache坐标

   ```xml
   <dependency>
   	<groupId>net.sf.ehcache</groupId>
       <artifactId>ehcache</artifactId>
   </dependency>
   ```

2. 缓存设定为使用 Ehcache

   ```yaml
   spring:
    cache:
     type: ehcache
     ehcache:
      config: ehcache.xml  #配置文件
   ```

3. 配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
            updateCheck="false">
       <diskStore path="D:\ehcache" />
   
       <!--默认缓存策略 -->
       <!-- external：是否永久存在，设置为true则不会被清除，此时与timeout冲突，通常设置为false-->
       <!-- diskPersistent：是否启用磁盘持久化-->
       <!-- maxElementsInMemory：最大缓存数量-->
       <!-- overflowToDisk：超过最大缓存数量是否持久化到磁盘-->
       <!-- timeToIdleSeconds：最大不活动间隔，设置过长缓存容易溢出，设置过短无效果，可用于记录时效性数据，例如验证码-->
       <!-- timeToLiveSeconds：最大存活时间-->
       <!-- memoryStoreEvictionPolicy：缓存清除策略-->
       <defaultCache
           eternal="false"
           diskPersistent="false"
           maxElementsInMemory="1000"
           overflowToDisk="false"
           timeToIdleSeconds="60"
           timeToLiveSeconds="60"
           memoryStoreEvictionPolicy="LRU" />
       <!--LRU淘汰策略：挑选最后一次使用时间最早的淘汰-->
   	<!--LFU淘汰策略：挑选最近使用次数最少的淘汰-->
       
       <!--下面的name是缓存空间名称-->
       <cache
           name="smsCode" 			
           eternal="false"
           diskPersistent="false"
           maxElementsInMemory="1000"
           overflowToDisk="false"
           timeToIdleSeconds="10"
           timeToLiveSeconds="10"
           memoryStoreEvictionPolicy="LRU" />
   
   </ehcache>
   ```

****

##### jetCache远程缓存

对SpringCache进行了封装，在原有功能基础上实现了多级缓存，缓存统计，自动刷新，异步调用，数据报表等功能。

- jetCache设定了本地缓存与远程缓存的多级缓存解决方案
  - 本地缓存
    - LinkedHashMap（相对于HashMap进行了性能优化
    - Caddeine
  - 远程缓存
    - Redis
    - Tair

##### j2Cache

是一个缓存整合框架，可以提供缓存的整合方案，使各种缓存搭配使用，自身不提供缓存功能



#### 任务

定时任务是企业级应用中的常见操作

常见的一个定时任务技术：Quarzt，Spring Task

**SpringBoot整合Quarzt**

- 相关概念

  - 工作（Job）：用于定义具体执行的工作
  - 工作明细（JobDetail）：用于描述定时工作相关的信息
  - 触发器（Trigger）：用于描述触发工作的原则，通常使用 cron 表达式定义调度规则
  - 调度器（Scheduler）：描述了工作明细与触发器的对应关系

  触发器绑定工作明细，工作明细中指定工作

****

1. 导入坐标

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-quartz</artifactId>
   </dependency>
   ```

2. 定义具体要执行的任务，继承QuartzJobBean

   ```java
   public class QuartzTaskTest extends QuartzBean{
       @Override
       protected void executeInternal(JobExecutionContext context)
           throws JobExecutionException{
           //执行对应的语句，可以从context参数中获得一些数据
       }
   }
   ```

3. 定义工作明细与触发器，并绑定对应关系

   ```java
   @Configuration
   public class QuartzConfig{
       @Bean
       public JobDetail printJobDetail(){
         return JobBuilder.newJob(QuartzTaskTest.class).storeDurably().build();
       }
       @Bean
       public Trigger printJobTrigger(){
         CronScheduleBuilder cron = 
           CronScheduleBuilder.cronSchedule("传入执行的时间条件，具体资料自己搜吧");
         return TriggerBuilder.newTrigger().forJob(printJobDetail())
             .withSchedule(cron).build();
       }
   }
   ```

****

**Spring Task**

1. 开启定时任务功能

   ```java
   @SpringBootApplication
   @EnableScheduling
   public class Application{
       public static void main(String[] args){
           SpringApplication.run(Application.class,args);
       }
   }
   ```

2. 设定定时执行的任务，并设定执行周期

   ```java
   @Component
   public class ScheduleBean{
       @Scheduled(cron = "0/5 * * * * ?")
       //每五秒执行一次，
       public void print(){
           sout("nihaowa")
       }
   }
   ```

3. 定时任务相关配置

   ```yaml
   spring:
     task: 
       scheduling: 
         # 任务调度线程池大小，默认1
         pool: 
           size: 1
         # 调度线程名称前缀，默认scheduling-，方便调试使用
         # Thread.currentThread().getName() 由此方法调用可以看到
         thread-name-prefix: ssm_
         shuidown:
           # 线程池关闭时如果有任务未执行完毕，是否等待其执行完毕
           await-termination: false
           # 调度线程关闭前最大等待时间，确保最后一顶关闭
           await-termination-period: 10s
         
          
   ```
   
   

#### 邮件

**SpringBoot整合JavaMail**

- SMPTP：简单邮件传输协议，用于发送电子邮件的传输协议（最常用
- POP3：用于接收电子邮件的标准协议
- IMAP：互联网消息协议，是POP3的替代协议



1. 导入坐标

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-mail</artifactId>
   </dependency>
   ```

2. 配置文件进行设置

   ```yaml
   spring: 
     mail:
       host: smtp.163.com
       username: xx@163.com
       password: 邮箱所提供的授权码
   ```

3. 发送邮件

   ```java
   private String from = "****@163.com" 		//发送人
   private String to = "****@qq.com"			//就收人
   private String subject = "邮件标题";		 //邮件主题
   private String text = "邮件正文"			 //邮件内容 
       
   @Autowired
   private JavaMailSender mailSender;
   
   public void sendMail(){
       //简单邮件
       SimpleMailMessage mailMessage = new SimpleMailMessage();
       mailMessage.setFrom(from);
       mailMessage.setTo(to);
       mailMessage.setSubject(subject);
       mailMessage.setText(text);
       mailSender.send(mailMessage);
   }
   ```

****

**发送多部件邮件**

```java
@Autowired
private JavaMailSender mailSender;

public void sendMail(){
    try{
        MimeMessage message = JavaMailSender.createMimeMessage();
        //此处的true是为了开启发送附件
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        //此处设置为true，如果text中含有html标签，可以进行解析
        helper.setText(text,true);
        File f1 = new File("对应的路径");
        File f2 = new File("对应的路径");
        //f1.getName()获取了原文件的名字
        helper.addAttachment(f1.getName(),f1);
        helper.addAttachment("自定义名字记得加后缀",f2);
        mailSender.send(message);
    } catch (Exception e){
        e.printStackTrace();
    }
}
```



#### 消息

消息发送方：生产者

消息接收方：消费者

异步消息传递技术：JMS、AMQP、MQTT

JMS：一个规范，等同于JDBC规范，提供了与消息服务相关的API接口

- JMS消息模型：
  - peer-2-peer：点对点模型，消息发送到一个队列中，队列保存消息。队列的小时之恩呗一个消费者消费，或超时
  - publish-subscribe：发布订阅模型，消息可以被多个消费者消费，生产者和消费者完全独立，不需要感知对方的存在（企业级开发应使用这一个

- JMS消息种类
  - TextMessage
  - MapMessage
  - $\textcolor{red}{ByteMessage}$（常用
  - SstreamMessage
  - ObjectMessage
  - Message（只有消息头和属性

AMQP：一种协议（高级消息队列协议，也是消息代理规范），规范了网络交换的数据格式，兼容JMS
优点：具有跨平台性服务器供应商，生产者，消费者可以使用不同的语言来实现

- AMQP消息模型
  - ==direct exchange==
  - ==topic exchange==
  - fanout exchange
  - headers exchange
  - system exchange

AMQP消息种类：byte[]（解决了跨平台的问题

Kafka：一种高吞吐量的分布式发布订阅消息系统，提供实时消息功能











### 监控

**监控的意义**

- 监控服务器状态是否宕机
- 监控服务运行指标（内存、虚拟机、线程、请求等
- 监控日志
- 管理服务（服务下线

监控的实施方式：

​	显示监控信息的服务器：用于获取服务信息，并显示对应的的信息

​	运行的服务：启动时主动上报，告知监控服务器自己需要收到监控



**可视化监控平台**

SpringBootAdmin（不属于Spring官方，使用的版本应该和SpringBoot的版本对应，相同即可），开源社区项目，用于管理和监控SpringBoot应用程序。客户端注册到服务端后，通过HTTP请求方式，服务器定期从客户端获取对应的信息，并通过UI界面展示对应信息

- 服务端

  ```xml
  <dependency>
  	<groupId>de.codecertric</groupId>
      <artifactId>spring-boot-admin-starter-server</artifactId>
      <version>应该对应你使用的Spring的版本</version>
  </dependency>
  ```

- 客户端

  ```xml
  <dependency>
  	<groupId>de.codecertric</groupId>
      <artifactId>spring-boot-admin-starter-client</artifactId>
      <version>应该对应你使用的Spring的版本</version>
  </dependency>
  ```

服务端一定要做成web项目，并且启用Spring-Admin，启动类添加注解 `@EnableAdminServer`

客户端开放权限

```yaml
spring:
  boot:
    admin:
      client:
        url: http://localhost:8080   #对应的服务端的地址
management: 
  endpoint: 	  	# 这一层是设置是否对外开启功能
    health:			# health必须开放	
      show-details: always		#开启所有功能
    beans:  		# 端点名称
      enabled: true #开启断电
  endpoints:	# 这一层是设置开启的功能是否能在web层查看
    web: 
      exposure:
        include: "*"   #开放全部权限给对应服务端
```



**自定义监控指标**

![image-20220801101555976](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220801101555976.png)

info信息：在yaml配置文件中配置以后即可在web端查看到此信息

```yaml
info: 
 auther: hanyongyan
 version: @project.version@			# 引用了pom.xml文件中的属性
```

通过编程来添加自定义指标

```java
@Component
public class AppInfoContributor implements InfoContributor{
    @Override
    public void contribute(Info.Builder builder){
        Map<String,Object> infoMap = new HashMap<>();
        infoMap.put("buildTime",2006);
        builder.withDetails(infoMap);		//可以从map集合中添加多个信息
        builder.withDetail("runTime",System.currentTimeMillis());//添加单条信息
    }
}
```

health信息：需要适应变成来添加自定义指标

一般会添加重要的组件

```java
@Component 
public class AppHealthContributor extends HealthContributor{
    @Override
    protected void doHealthChech(Health.Builder builder) throws Excepiton{
        //传入信息的方式与info通过编程添加信息一样
        builder.status(Status.DOWN);//来设置这一项的状态
        //Status.UP Status.UNKNOWN 
    }
}
```





























## yml/yaml

[YAML教程 (yiibai.com)](https://www.yiibai.com/yaml/)

[YAML 入门教程 | 菜鸟教程 (runoob.com)](https://www.runoob.com/w3cnote/yaml-intro.html)

字面值表达方式

- boolean：true
- float：3.14								支持科学计数法 6.18e+5
- int：123 								   支持二进制0b，八进制01~7，十进制正常，十六进制0X  
- null：~
- string：helloword
- string2："HelloWord"
- date：2018-02-7					日期必须使用yyyy-MM-dd格式
- datetime：2018-02-17T15：02：31+08：00					时间和日期之间使用T连接，最后使用+代表时区

**当你在配置文件中输入以0开头的最好使用引号引起来，可能会误识别为其他进制**



- 大小写敏感

- 属性层级管事使用多行描述，每行结尾使用冒号结束

- 使用缩进表示层级关系，同层级左侧对其，只允许使用空格（不允许使用Tab键

- `属性值前面添加空格`（属性名与属性值之间使用冒号+空格作为分隔符

- 支持转义字符，需要使用双引号包裹

- \# 表示注释

- 数组数据

  ```yaml
  subject: 
        - java
        - 前端
        - 大数据
  ```

  

### yaml文件怎么读取

方法一

```java
@Value("${name}")				//读取到yaml配置文件中的name值
private String name;
@Value("${user.password}")		//多级读取
private String password;
@Value("${subject[1]}")		//读取数组中的内容
private String subject;
```

方法二

```java
@Autowired						//此变量会获取配置文件中全部的值
private Environment env;
//在方法中进行调用
env.getProperty("配置文件中对应的属性名")
```

方法三 （常用

```yaml
enterprise: 
  name: itcast
  age: 16
  tel: 100000
  subject: 
    - java
    - 前端
    - 大数据
```

```java
//此处的enterprise代表配置文件中的enterprise，想要的属性的上一层级,prifix不写也能用
@Component
@ConfigurationProperties(perfix = "enterprise")	
public class Enterprise{
    private String name;
    private Integer age;
    private String[] subject;
}
//会完成自动封装
```

自定义对象封装数据警告解决方案

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>ture</optional>
</dependency>
```



文件中变量的引用

```yaml
baseDir: c:\windows
#使用${属性名}引用数据
tempDir: ${baseDir}\temp
#tempDir的值就是  c:\windows\temp
```

### yaml文件读取xml文件内容

```xml
<time>
	<now>2020</now>
</time>
```

```yaml
Time: @time.now@
```



## MybatisPlus

[MyBatis-Plus 官网](https://baomidou.com/)

基于MyBatis框架基础上开发的增强型工具，提高效率，简化开发

MybatisPlus的id自增策略为雪花算法，如需使用默认自增

```yaml
mybatis-plus:
 global-config: 
  db-config:
   id-type: auto #更改自增策略
 configuration: 
  map-underscore-to-camel-case: ture #映射实体类中的属性时，使得字段名中含有下划线与驼峰命名相匹配
  log-impl: org.apache.ibatis.logging.stdout.StdOutImpl #开启日志
```

添加坐标

```xml
<dependency>
	<gooupId>com.baomidou</gooupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>...</version>
</dependency>
```

定义数据接口，继承BaseMapper<user>

```java
@Mapper
public interface userDao extends BaseMapper<user>{
    //里面简单的查询方法都有，如没有还可以按照原来的方法进行写出来
    //在业务层中调用时会使用父接口中的默认方法，见名知意
}
```

| 功能       | MP接口                                    |
| :--------- | :---------------------------------------- |
| 新增       | int insert(T t)                           |
| 删除       | int deleteById(Serializable id)           |
| 修改       | int updateById(T  t)                      |
| 根据id查询 | T seleceById(Serializable id)             |
| 查询全部   | List<T> selectList()                      |
| 分页查询   | IPage<T> selecePage(IPage<T> page)        |
| 按条件查询 | IPage selecePage(Wrapper<T> queryWrapper) |

分页查询的步骤

1. 设置分页拦截器作为Spring管理的bean（固定写法）
   相当于完成了动态拼写SQL

   ```java
   @Configuration
   public class MpConfig{
       @Bean
       public MybatisPlusInterceptor pageInterceptor(){
           MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
           interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
           //回头如果使用更多的直接再按照上一行的设置就行
           return interceptor;
       }
   }
   ```

2. 执行分页查询

   ```java
   IPage page = new Page(2,3)//2代表当前页码，3代表每页的数据
   UserDao.selectPage(page,null);
   sout("当前页码"+page.getCurrent());
   sout("每页数据总量"+page.getSize());
   sout("总页数"+page.getPages());
   sout("数据总量"+page.getTotal());//数据的总条数
   sout("当前页数据"+page.getRecords());
   ```

### DQL编程控制

条件查询
mp将书写复杂的SQL查询条件进行了封装，使用编程的形式完成查询条件的组合

Wrapper<T> 上面方法中的此东西 就是完成了封装

- 格式一：

  ```java
  QueryWrapper<User> qw = new QueryWrapper();
  //查询年龄大于等于18岁，小于65岁的用户
  qw.lt("age",65);
  qw.gt("age",18);
  List<User> userList = userDao.selectList(qw);
  System.out.println(userList);
  ```

- 格式二：链式编程格式

  ```java
  QueryWrapper<User> qw = new QueryWrapper();
  //查询年龄大于等于18岁，小于65岁的用户
  qw.lt("age",65).gt("age",18);
  List<User> userList = userDao.selectList(qw);
  System.out.println(userList);
  ```

- 格式三：Lambda格式（推荐

  ```java
  QueryWrapper<User> qw = new QueryWrapper();
  //查询年龄大于等于18岁，小于65岁的用户
  qw.lambda().lt(User::getAge,65).gt(User::getAge,18);
  List<User> userList = userDao.selectList(qw);
  System.out.println(userList);
  ```

- 格式四：Lambda格式（推荐

  ```java
  LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
  lqw.lt(User::getAge,65).gt(User::getAge,18);
  List<User> userList = userDao.selectList(lqw);
  System.out.println(userList);
  ```

- 直接连这写就是并且关系，
  或者使用 `lqw.lt(User::getAge,65).or().gt(User::getAge,18);`
  采用此种方式

#### 关于null值处理

不建议使用 if else 去做判断处理，一般使用条件参数控制

其他方法同理

```java
LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
lqw.lt(null != userQuery.getAge(),User::getAge,65);
lqw.gt(null != userQuery.getAge2(),User::getAge,18);
List<User> userList = userDao.selectList(lqw);
System.out.println(userList);
//上面有两个age参数是因为使用了新的实体类userQuery并继承了user新增了age2；
//当user中的某一个参数在进行查询时如果需要两个参数，而只填写了一个参数用此种办法
//比如 查询100元以下的商品，那么最低价值0没有填且值为null查询会失败，用此方法可以避免null的出现
//同样可以使用链式编程
```

#### 查询投影

其实就是设置查询出来的结果长什么样 

- 查询结果包含模型类中部分属性

  ```java
  LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
  lqw.select(User::getId,User::getName,User::getAge());//查询的结果只包含这三个字段
  List<User> userList = userDao.selectList(lqw);
  System.out.pringln(userList);
  ```

- 查询结果包含模型类中未定义的属性

  ```java
  QueryWrapper<User> qm = new QueryWrapper();
  qm.select("count(*) as nums, gender");	//注意此处的是字符串
  qm.groupBy("gender");					//将按照gender来进行分组
  List<Map<String,Object>> maps = userDao.selectMaps(qm);
  System.out.pringln(maps);
  ```

#### 查询条件

- 一般匹配查询

  `lqw.eq(User::getName,"jerry");  eq代表equals`查询 name 为jerry 的数据，可以使用链式格式

- 范围匹配（ > , = , between ）

  \> lt , >= le , < gt , <= ge , between

  between的用法 `lqw.between(User::getAge,10,20)` 前面是小值

- 模糊匹配（ like

- 空判定

- 包含性匹配（ in

- 分组（ group

- 排序（ order

- 。。。

更多详细点击这里查看[条件构造器 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/10c804/#abstractwrapper)

#### 字段映射与表名映射

- 当你实体类中的个别属性名称与数据库中字段的名称不匹配时，无法自动完成装配等配置
  `eg:`数据库中的字段名成为 `pwd`，而你实体类中的为 `private String password`
  此时要使用注解来完成两者之间的关系

  ```java
  @TableField(value = "pwd")
  private String password;
  //value 为默认值可不添加
  ```

- 实体类中添加了数据库中未定义的字段

  ```java
  @TableField(exist = false)
  private Integer online;
  //表明数据库中不存在这样一个字段
  ```

- 个别参数不参与查询
  使用 * 查询时，会转换成所有字段的查询。
  如果密码参与了查询，是有风险的
  我们就可以设置个别参数不参与查询

  ```java
  @TableField(value = "pwd", select = false)
  private String password;
  ```

- 表名和实体类名字不一样
  一般对于表我们采用见名知意的方法，与实体类设计时名字可能不太一样

  ```java
  @TableName("tbl_user")
  public class user{}
  //数据库中的表明为tbl_name 
  ```

### DML编程控制

DML完成增删改

id生成策略控制

```java
@TableId(type = IdType.AUTO)
private int id;
//value设置数据库主键名称
//type设置主键属性的生成策略
```

可以在配置文件中为当前项目中的使用的表的id的生成策略做全局配置，`@TableId`仍需使用

```yaml
mybatis-plus: 
 global-config:
  db-config:
   id-type: #会在此处又提醒，来配置就行了
   table-prefix: #来设置全局表名的前缀，省的手工指定表名
 configuration:
  log-impl: org.apache.ibatis.logginh.stdout.StdOutImpl #日志记录
```



- AUTO(0)：使用数据库ID自增策略控制id生成
- NONE(1)：不设置id生成策略
- INPUT(2)：用户手工输入ID
- ASSIGN_ID(3)：雪花算法生成id（可兼容数值型与字符串型）（生成一个64为的二进制数
- ASSIGN_UUID(4)：以UUID生成算法作为ID生成策略

#### 多记录操作

根据主键删除多条记录

```java
List<Long> ids = Arrays.asList(new Long[]{2,3});
userDao.deleteBatchIds(ids);
```

根据主键查询多条记录

```
List<Long> ids = Arrays.asList(new Long[]{2,3});
userDao.selectBatchIds(ids);
```

#### 逻辑删除

逻辑删除：为数据设置是否可种状态字段，删除时设置状态字段为不可用状态，数据保留在数据库中

在实体类中进行设置并进行约定

```java
@TableLogic(value = "1", delval = "1")
private Integer deleted;
//value的值代表未删除，delval代表已经删除
```

配置完成以后再进行删除操作就会更改`deleted`字段的值，而不是真正的删除数据
此时执行的sql语句从删除语句转换为了更新语句

使用 `selectList` 操作时查询语句后面会添加 `where deleted = 0`来过滤数据

使用配置文件配置，使用后`@TableLogic`仍需使用

```yaml
mybatis-plus: 
 global-config:
  db-config:
   logic-delete-field: deleted		#表示配置逻辑删除的字段
   logic-not-delete-value: 0		#表示未删除的默认字段
   logic-delete-value: 1			#表示删除的默认字段
```



#### 乐观锁

业务并发现象带来的问题：秒杀问题

就是根据获取version的值来进行判断某一操作执不执行

1. 数据库表中添加锁标记字段，一般字段名为version，值为int类型

2. 实体类中添加对应字段，并设定当前字段为逻辑删除标记字段

   ```java
   @Version
   private Integer version;
   ```

3. 配置乐观锁拦截器实现锁机制对应的动态sql语句拼装

   ```java
   @Configuration
   public class MpConfig{
       @Bean
       public MybatisPlusInterceptor mpInterceptor(){
           MybatisPlusInterceptor mp = new MybatisPlusInterceptor();
           mp.addInnerInterceptor(new OptimisticLockerInnerceptor());
           return mp;
       }
   }
   //上面分页查询也配置了一个
   ```

4. 使用乐观锁机制在修改前必须先获取到对应数据的version方可正常进行

   ```java
   @Test
   void testUpdate(){
       //先查询数据，获取到version数据
       User user = UserDao.selectById(1L);
       //执行数据修改操作
       user.serName("tom");
       userDao.updateById(user);
   }
   ```

   

### 快速开发数据层

```java
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
```

这样书写mapper即可，简单的增删改查在里面

提供的不满足使用时，自己去填写业务



### 快速开发业务层

```java
//定义接口
public interface BookService extends IService<Book>{}
```

```java
//实现类
@Service
public class BookServiceImpl extends
    ServiceImpl<BookMapper,Book> implements BookService{}
```

提供的不满足使用时，自己去写业务



### 快速开发（前期自己开发不建议使用

#### 代码生成器

- 模板：MybatisPlus提供
- 数据库相关配置：读取数据库获取信息
- 开发者自定义配置：手工配置

先引入坐标

```xml
<!--代码生成器-->
<dependency>
	<groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>...</version>
</dependency>

<!--velocity模板引擎-->
<dependency>
	<groupId>org.apache.velocity</groupId>
    <artifactId>ve</artifactId>
    <version>...</version>
</dependency>
```



配置代码

会自动生成对应的文件

```java
public class CodeGenerator {
    public static void main(String[] args) {
        //1.获取代码生成器的对象
        AutoGenerator autoGenerator = new AutoGenerator();

        //设置数据库相关配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatisplus_db?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        autoGenerator.setDataSource(dataSource);

        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/mybatisplus_04_generator/src/main/java");    //设置代码生成位置
        globalConfig.setOpen(false);    //设置生成完毕后是否打开生成代码所在的目录
        globalConfig.setAuthor("黑马程序员");    //设置作者
        globalConfig.setFileOverride(true);     //设置是否覆盖原始生成的文件
        globalConfig.setMapperName("%sDao");    //设置数据层接口名，%s为占位符，指代模块名称
        globalConfig.setIdType(IdType.ASSIGN_ID);   //设置Id生成策略
        autoGenerator.setGlobalConfig(globalConfig);

        //设置包名相关配置
        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setParent("com.aaa");   //设置生成的包名，与代码所在位置不冲突，二者叠加组成完整路径
        packageInfo.setEntity("domain");    //设置实体类包名
        packageInfo.setMapper("dao");   //设置数据层包名
        autoGenerator.setPackageInfo(packageInfo);

        //策略设置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("tbl_user");  //设置当前参与生成的表名，参数为可变参数
        strategyConfig.setTablePrefix("tbl_");  //设置数据库表的前缀名称，模块名 = 数据库表名 - 前缀名  例如： User = tbl_user - tbl_
        strategyConfig.setRestControllerStyle(true);    //设置是否启用Rest风格
        strategyConfig.setVersionFieldName("version");  //设置乐观锁字段名
        strategyConfig.setLogicDeleteFieldName("deleted");  //设置逻辑删除字段名
        strategyConfig.setEntityLombokModel(true);  //设置是否启用lombok
        autoGenerator.setStrategy(strategyConfig);
        //2.执行生成操作
        autoGenerator.execute();
    }
}

```



Lombok

简化实体类的开发

先导入jar坐标

```xml
<dependency>
	<groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
    <!--scope不配也没关系，版本由SpringBoot管理-->
</dependency>
```



```java
@Setter					//完成setter方法
@Getter					//完成getter方法
@ToString				//完成tostring方法
@NoargsConstructor		//无参构造方法
@AllArgsConstructor		//全部参数构造方法
@EqualsAndHashCode		//equals和hashCode方法
public class User{
    private Long in;
    private String name;
    
}
//上面的配置较多，建议使用@Data注解来进行开发，上面的除了构造方法全部包含
```







## 开发顺序

1. pom.xml						配置起步依赖
2. application.yml    		 设置数据源、端口、框架技术相关配置等
3. dao								继承BaseMapper、设置@Mapper
4. dao测试类
5. service							调用数据层接口或Mybatis-plus提供的接口快速开发
6. service测试类
7. controller						基于Restful开发，使用Postman测试能否跑通功能
8. 页面								放置在resource目录下的static目录中
