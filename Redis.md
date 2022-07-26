# Redis

[Redis中文网](https://www.redis.net.cn/)

Redis是一个基于内存的 Key - Value 结构数据库

- 基于内存存储，读写性能高
- 适合存储热点数据（热点商品，咨询，新闻）

可以用作：数据库、缓存和消息中间件

被称为结构化的 NoSql（Not On SQL），不仅仅是sql，泛指非关系型数据库。NoSql数据库并不是要取代关系型数据库，而是关系型数据库的补充

Redis安装包分为windows版和Linux版:

- windows版下载地址: https://github.com/microsoftarchive/redis/releases
- Linux版下载地址: https://download.redis.io/releases/

在Linux系统安装Redis步骤:

1. 将Redis安装包上传到Linux
2. 解压安装包，命令: tar -zxvf redis-4.0.0.tar.gz -C /usr/local
3. 安装Redis的依赖环境gcc，命令: yum install gcc-c++
4. 进入/usr/local/redis-4.0.0，进行编译，命令: make
5. 进入redis的src目录，进行安装，命令: make install

启动Redis，在src目录下执行语句 `./redis-server`

操作Redis，在src目录下执行语句 `./redis-cli`

## Redis配置

配置完成以后均需要重新启动 redis

### Redis在Linux下隐式启动

在redis根目录下执行语句 `vim redis.conf` vim输入 /dea 进行查询 

将`daemonize no`改为 `daemonize yes`，启动时使用此语句 `./redis-server  ../redis.conf`(注意层级关系)

### Redis启动需要密码

在redis根目录下执行语句 `vim redis.conf` vim输入 /requirepass 进行查询 对 `#requirepass foobared`解除注释，`foobared`就是密码，自定义设置即可

在使用 `redis-cli`命令后，输入 `auto 密码`即可完成登录

### Redis开启远程连接

在redis根目录下执行语句 `vim redis.conf` vim输入 /bind进行查询 对 `bind 127.0.0.1 ::1`进行注释

使用windows远程连接时，在redis-cli.exe所在文件夹内使用cmd执行命令 
`.\redis-cli.exe -h 远程ip -p 端口 -a 密码`

如果连接成功后操作提示  Error: 在驱动器 %1 上插入软盘。

要把redis配置文件中的protected-mode设置成no

## 数据类型

Redis存储的是 key-value 结构的数据，其中key是字符串类型，value有五种常见的数据类型

- 字符串 string
- 哈希 hash
- 列表 list
- 集合 set
- 有序集合 sorted set

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808182703590.png" alt="image-20220808182703590" style="zoom:50%;" />

## Redis常用命令

### 字符串 string 操作命令

![image-20220808183452581](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808183452581.png)



### 哈希 hash 操作命令

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808201221168.png" alt="image-20220808201221168" style="zoom:67%;" />



### 列表 list 操作命令

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808204229751.png" alt="image-20220808204229751" style="zoom:67%;" />



### 集合 set 操作命令

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808204618298.png" alt="image-20220808204618298" style="zoom:50%;" />

### 有序集合 sorted set 操作命令

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808205434926.png" alt="image-20220808205434926" style="zoom:67%;" />

### 通用命令

![image-20220808205739497](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808205739497.png)

## Java中操作Redis

### Jedis

导入坐标

```xml
<dependency>
	<groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>...</version>
</dependency>
```

使用Jedis操作Redis的步骤：

1. 获取链接
2. 执行操作
3. 关闭连接

```java
@Test
public void JedisTest(){
    //传入的参数为 连接的是哪个的redis 6379代表端口号
    Jedis jedis = new Jedis("localhost",6379);
    //Jedis中的方法与Redis中的指令一样
    jedis.set("username","xiaoming");
    String usrename = jedis.get("username");
    jedis.close();
}
```

### SpringDataRedis

SpringBoot项目中，可以使用SpringDataRedis来简化Redis操作，maven坐标

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```



配置文件

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    #password: 123456		如果你开起了密码选项
    database: 0 			#redis默认有十六个数据库，在此使用第一个
    jedis:
      # Redis 连接池配置
      pool: 
        max-active: 8  		# 最大连接数
        max-wait: 1ms  		# 连接池最大阻塞等待时间
        max-idle: 4 		# 连接池中最大空闲连接
        min-idle: 0  		# 连接池中最小空闲连接

```



<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220809093725972.png" alt="image-20220809093725972" style="zoom:50%;" />

如果存储数据时有问题的话，比如 city 变成 xxxcity

我们来自定义一个类

```java
@Configuration
public class RedisConfig extends CachingConfigurerSupport{
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<Object,Object>  redisTemplate = new RedisTemplate<>();

        //默认的序列化器为：JadSerizalizationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }
}
//设置以后在redis客户端内获取时value也会乱，不过使用java程序运行时会jin
```

## SpringCache

SpringCache是一个框架，实现了基于注解的缓存功能，只需要一个注解，就能够实现缓存功能。

SpringCache提供了一层抽象，底层可以切换不同的cacha实现，具体就是通过CachaManager接口来同意不同的缓存技术

CachaManager是SPring提供的各种缓存技术抽象接口

针对不同的缓存技术需要实现不同的CachaManager

| CacheManager        | 描述                               |
| :------------------ | ---------------------------------- |
| EhCachaCacheManager | 使用EhCache作为缓存技术            |
| GuavaCacheManager   | 使用Google的GuavaCache作为缓存技术 |
| RedisCacheManager   | 使用Redis作为缓存技术              |

### SpringCache常用注解

| 注解           | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| @EnableCaching | 开启缓存注解功能，放在启动类上                               |
| @Cacheable     | 在方法执行前Spring先查看缓存中是否有数据，如果有数据，这直接返回缓存数据，若没有数据，调用方法并将方法返回值放到缓存中 |
| @CachePut      | 将方法的返回值放到缓存中,value代表缓存名称，key就是redis意义上的key |
| @CacheEvict    | 将一条或多条数据从缓存中删除                                 |

- @CachePut(value = "" , key = "" )：value代表缓存的名称，每个缓存名称下可以有多个key，key就是redis意义上的key，key不能够写死啊，所以可以在IDEA中 CTRL+点击 key，可以查看不写死的方法，
  - eg：@CachePut( value = "name", key = "#result.id")	使用了返回值中的id属性作为key
        ：@CachePut( value = "name", key = "#user.id")	 user是对应的方法传入的参数，此时的key就是user.id

- @CacheEvict(value = "" , key = "", allEntries = true )：value和key与上面的代表的一样，使用key将只删除key中的数据，使用 `allEntries = true `不使用key就删除value中的全部数据
- @Cacheable(value = "" , key = "", unless = "#result != null" )：value和key与上面的代表的一样 
  		unless就是表明在满足情况时不进行缓存数据（可以使用 result
  		condition与unless相反，表明在满足什么情况时，不进行缓存数据（不可以使用result



在SpringBoot项目中，使用缓存技术只需在项目中导入相关缓存技术的依赖包，并在启动类上使用 @EnableCaching 开启缓存支持即可。

### SpringCache使用Redis

1. 导入Maven坐标
   		spring-boot-starter-data-redis, spring-boot-starter-cache

2. 配置 application.yml

   ```yaml
   spring: 
     cache:
       redis:
         time-to-live: 100				# 设置缓存有效期，单位时毫秒
   ```

   [这个配置不要忘记](# SpringDataRedis)

3. 启动类添加 @EnableCaching 注解，开启缓存注解功能

4. 在 Controller 的方法上加入 @Cacheable 等注解，进行缓存操作

5. 如果是自定义了返回结果类要继承接口 `Serializable`
