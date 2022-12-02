# 分布式缓存

单点Redis的问题

1. 数据丢失问题：Redis是内存存储，服务器重启后可能会丢失数据。
2. 并发能力问题：单节点Redis并发能力虽然不错，但也无法满足如618这样的高并发问题
3. 故障恢复问题：如果Redis宕机，则服务不可用，需要一种自动的故障恢复手段。
3. 存储能力问题：Redis基于内存，单节点能存储的数据量难以满足海量数据的需求。

## Redis持久化

### RDB持久化

RDB全程 Redis Database Backup file（Redis数据备 份文件）。简单来说就是把内存中的所有数据都记录到磁盘中。当Redis实例故障重启后，从磁盘读取快照文件，恢复数据。

快照文件称为RDB文件，默认是保存在当前运行目录<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221123223338160.png" alt="image-20221123223338160" style="zoom:50%;" />

Redis停机时会执行一次RDB。Redis重启后会自动读取其中的内容

RDB内部有触发RDB的机制，可以在redis.conf文件中找到，格式如下：
```bash
# 900s内，如果至少有 1 个key被修改，则执行 bgsave，如果是 save "" 则表示禁用RDB
save 900 1
save 300 10
save 60 1000
# RDB 的其他配置也可以找 redis.conf 文件中设置：
# 是否压缩，建议不开启，压缩也会消耗CPU，磁盘不值钱
rdbcompression yes
# RDB文件名称
dbfilename dump.rdb
# 文件保存的路径目录
dir ./
```

`bgsave`开始时会fork主进程得到子进程，子进程共享主进程的内存数据。完成fork后读取内存数据并写入RDB文件。 

fork采用的是copy-on-write计数：

- 当主进程执行读操作时，访问共享内存；
- 当主进程执行写操作时，则会拷贝一份数据，执行写操作。

缺点：

- RDB执行间隔时间长，两次RDB之间写入数据有丢失的风险
- fork子进程、压缩、写出RDB文件都比较耗时

### AOF持久化

AOF全称为Append Only FIle（追加文件）。Redis处理的每一个写命令会记录在AOF文件，可以看作是命令日志文件。

AOF默认是关闭的，需要修改 redis.conf 配置文件来开启AOF：

```bash
# 是否开启AOF功能，默认是no
appendonly yes
# AOF文件的名称
appendfilename "appendonly.aof"
# AOF的命令记录的频率也可以通过 redis.conf 文件来配：
# 表示每执行一次写命令，立即记录到AOF文件
appendfsync always
# 写命令执行完先放入AOF缓冲区，然后表示每隔一秒将缓冲区数据写到AOF文件，是默认方案
appendfsync everysec
# 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
appendfsync no
# 三者的对比放到下面
```

| 配置项   | 刷盘时机     | 优点                     | 缺点                         |
| -------- | ------------ | :----------------------- | :--------------------------- |
| Always   | 同步刷盘     | 可靠性高，几乎不丢失数据 | 性能影响大                   |
| everysec | 每秒刷盘     | 性能始终                 | 最多丢失1秒数据              |
| no       | 操作系统控制 | 性能最好                 | 可靠性较差，可能丢失大量数据 |

AOF因为是记录命令，AOF文件会被RDB文件大的多。而且AOF会记录对同一个key的多次写操作，但只有最后一次写操作才有意义。通过执行 `bgrewriteaof` 命令，可以让AOF文件执行重写功能用最少的命令达到相同效果。

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124101029672.png" alt="image-20221124101029672" style="zoom: 50%;" />

如上图执行此命令以后，对同一个key的会保留最后一个，而且相同类型的命令会合并在一起

在redis客户端中执行此命令即可开启。

Redis也会在触发阈值时自动去重写AOF文件。阈值也可以在redis.conf文件中设置：、

```bash
# 以下均是默认开启的
# AOF文件比上次文件 增长超过多少百分比出发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多以上才触发重写
auto-aof-rewrite-min-size 64mb
```

### RDB和AOF的对比

RDB和AOF各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会结合两者来使用。

|                | RDB                                          | AOF                                                          |
| -------------- | -------------------------------------------- | ------------------------------------------------------------ |
| 持久化方式     | 定时对整个内存做快照                         | 记录每一次执行的命令                                         |
| 数据完整性     | 不完整，两次备份之间会丢失                   | 相对完整，取决于刷盘策略                                     |
| 文件大小       | 会有压缩，文件体积小                         | 记录命令，文件体积很大                                       |
| 当即恢复速度   | 很快                                         | 慢                                                           |
| 数据恢复优先级 | 低，因为数据完整性不如AOF                    | 高，因为数据完整性更高                                       |
| 系统占用资源   | 高，大量CPU和内存消耗                        | 低，主要是磁盘IO资源<br />但AOF重写时会占用大量CPU和内存资源 |
| 使用场景       | 可以容忍数分钟的数据丢失，追求更快的启动速度 | 对数据安全性要求较高                                         |

## Redis主从

### 搭建主从架构

单点Redis的并发能力是有上限的，要进一步提高redis的并发能力，就需要搭建主从集群实现读写分离。

搭建主从架构看笔记 <font color="red">Redis集群.md</font>

### 数据同步原理

主从第一次同步是<font color="red">全量同步</font>
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124152053294.png" alt="image-20221124152053294" style="zoom:50%;" />

master如何判断slave是不是第一次来同步数据？这里会用到两个很重要的概念：

- Perlication Id：简称 replid，是数据集的标记，id一致则说明是统一数据集。每一个master都有唯一的replid，slave则会继承master节点的replid
- offset：偏移量，随着记录在 repl_baklog 中的数据增多而逐渐增大。slave完成同步时也会记录当前同步的offset。如果slave的offset小于master的offset，说明slave数据落后于master，需要更新。

简述全量同步的流程

1. slave 节点请求增量同步
2. master 节点判断 replid，发现不一致，拒绝增量同步
3. master 将完整内存数据生成RDB，发送RDB到slave
4. slave 清空本地数据，加载master的RDB
5. master 将RDB期间的命令记录在 repl_baklog，并持续将log中的命令发送给 slave
6. slave 执行接收到的命令，保持与 master 之间的同步

但如果slave重启后同步，则执行<font color="red">增量同步</font>
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124154121910.png" alt="image-20221124154121910" style="zoom:50%;" />

<font color="red">注意：</font>repl_baklog 大小有上限，写满后会覆盖最早的数据。如果 slave 断开时间过久，导致数据被覆盖，则无法实现增量同步，只能再次全量同步。这种情况不可避免！！

可以从下面几个方面来优化：

- 在 master 中配置 repl_diskless-sync yes 启用无磁盘复制，避免全量同步时的磁盘 IO
- Redis 单节点上的内存占用不要太大，减少RDB导致的过多磁盘IO
- 适当提高 repl_baklog 的大小，发现 slave 宕机时尽快实现故障恢复，尽可能避免全量同步
- 限制一个 master 上的 slave 节点数量，如说实在是太多 slave，则可以采用 主-从-从 链式结构，减少 master 压力
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124155049587.png" alt="image-20221124155049587" style="zoom: 50%;" />

## Redis哨兵

slave节点当即恢复后可以找master节点同步数据，那master节点宕机怎么办？

### 哨兵的作用和原理

Redis提供了哨兵（Sentinel）机制来实现主从集群的自动故障恢复。哨兵的结构和作用如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124161935777.png" alt="image-20221124161935777" style="zoom:67%;" />

### 服务状态监控

Sentinel 基于心跳机制检测服务状态，每隔 1 秒向集群的每个实例发送ping命令：

- 主观下线：如果某 Sentinel 节点发现某实例未在规定时间响应，则认为该实例**主观下线**
- 客观下线：若超过指定数量（quorum）的 sentinel 都认为该实例主观下线，则该实例**客观下线**。quorum值最好超过 Sentinel 实例数量的一半。

一旦发现 master 故障，sentinel 需要在 salve 中选择一个做为新的 master，选择依据是这样的：

- 首先会判断 slave 节点与 master 节点断开时间长短，如果超过指定值（down-after-milliseconds * 10）则会排除该 slave 节点
- 然后判断 slave 节点的 slave-priority 值，越小优先级越高，如果是0则永不参与选举
- 如果slave-priority一致，则判断slave节点的offset值，越大说明书数据越新，优先级很高
- 最后是判断 slave 节点的运行 id 大小，越小优先级越高

当选中了其中一个 slave 为新的 master 后（例如slave1），故障转移的步骤如下：
![image-20221124164229769](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124164229769.png)

Sentinel的三个作用是什么？

- 监控
- 故障转移
- 通知

### 搭建哨兵集群

搭建哨兵集群看笔记 <font color="red">Redis集群.md</font>

搭建视频看这个视频 [搭建哨兵集群](https://www.bilibili.com/video/BV1cr4y1671t/?p=106&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

## RedisTemplate 的哨兵模式

在 Sentinel 集群监管下的 Redis 主从集群，其节点会因为自动故障转移而发生变化，Redis的客户端必须感知这种变化，及时更新连接信息。Spring 的 RedisTemplate 底层利用 lettuce 实现了节点的感知和自动切换。

1. 在 pom 文件中引入 redis 的依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

2. 然后在配置文件 application.yml 中指定 sentinel 相关信息：

   ```yaml
   spring: 
     redis: 
       sentinel: 
         master: xxx  #指定master的名称
         nodes: # 指定redis-sentinel集群信息
           - ip:端口号
           - ip:端口号
   ```

3. 配置主从读写分离

   ```java
   // 直接定义在 启动类中也可以，其实任意一个配置类即可
   @Bean
   public LettuceClientConfigurationBuilderCustomizer configurationBuilderCustomizer(){
       return configBuilder -> configBuilder.readForm(ReadForm.REPLICA_PREFERRED);
   }
   ```

   这里的 ReadForm 是配置 Redis 的读取策略，是一个枚举，包括下面选择：

   - MASTER：从主节点读取
   - MASTER_PREFERRED：优先从 master 节点读取，master 不可用才读取 replica
   - REPLICA：从 slave（replica）节点读取
   - REPLICA_RPEFERRED：优先从slave（replica）节点读取，所有的 slave 都不可用才读取 master

## Redis分片集群

### 搭建分片集群

这种方式不需要哨兵机制

当我们是集群模式的时候通过命令行进入 redis 时 <font color="red">必须加 -c 命令 </font>，代表是集群模式 `redis-cli -c -p 6379`

主从和哨兵可以解决高可用、高并发读的问题。但是依然有两个问题没有解决：

- 海量数据存储问题
- 高并发写的问题

使用分片集群可以解决上述问题，分片集群特征：

- 集群中有多个 master，每个 master 保存不同数据
- 每个 master 都可以有多个 slave 节点
- master 之间通过 ping 检测彼此健康状态
- 客户端请求可以访问集群任意节点，最终都会被转发到正确节点

笔记在 <font color="red">Redis集群.md</font>

建议搭配视频食用 [分片集群](https://www.bilibili.com/video/BV1cr4y1671t/?p=108&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

### 散列插槽

Redis会把每一个 master 节点映射到 0~16383 共 16384 个插槽上，查看集群信息是就能看到：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124213705553.png" alt="image-20221124213705553" style="zoom: 67%;" />

数据 key 不是与节点绑定，而是与插槽绑定。Redis 会根据 key 的有效部分计算插槽值，分两种情况：

- key 中包含“{}”，且“{}”中至少包含一个字符，“{}”中的部分是有效部分
- key 中不包含“{}”，整个 key 都是有效部分。

例如：key 是 num，那么就根据 num 计算，如果是 {test}num，则根据 test 计算。计算方式是利用 CRC16 算法得到一个 hash 值，然后对16384取余，得到的结果就是 slot 值

### 集群伸缩

 redis-cli --cluster提供了很多操作集群的命令，可以通过此方式查看：`redis-cli --cluster help`

[看视频吧](https://www.bilibili.com/video/BV1cr4y1671t/?p=110&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

### 故障转移

当发生故障时会自动操作，不用担心。

### 数据迁移

利用 `cluster failover`命令可以手动让集群中的某个 master 宕机，切换到执行`cluster falover`命令的这个 slave 节点，实现无感知的数据迁移。其流程图如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124221254886.png" alt="image-20221124221254886" style="zoom:67%;" />

手动的 Failover 支持三种不同模式

- 缺省：默认的流程，如图1-6步
- force：省略的对 offset 的一致性校验
- takeover：直接执行第五步，忽略数据一致性、忽略 master 状态和其他 master 的意见 

### RedisTemplate 访问分片集群

RedisTemplate 底层同样基于 lettuce 实现了分片集群的支持，而是用的步骤与哨兵模式基本一致：

1. 引入 redis 的 starter 依赖
2. 配置分片集群地址
3. 配置读写分离

与哨兵模式相比，其中只有分片集群的配置方式略有差异，如下：

```yaml
spring: 
  redis:
    cluster:
      nodes: #指定分片集群的每一个节点信息
        - ip:port
        - ip:port
```

# 多级缓存

> 亿级流量的缓存方案

传统的缓存策略一般是请求到达 Tomcat 后，先查询 Redis，如果未命中则查询数据库，存在下面的问题：

- 请求要经过 Tomcat 处理，Tomcat 的性能称为整个系统的瓶颈
- Redis 缓存失效时，会对数据库产生冲击

多级缓存就是充分利用请求处理的每个环节，分别添加缓存，减轻 Tomcat 压力，提升服务性能：

![image-20221124225521445](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221124225521445.png)

用作缓存的额Nginx是业务Nginx，需要部署为集群，再有专门的Nginx用来做反向代理

我们主要学习除了浏览器客户端缓存的其他业务

## JVM进程缓存

### 初始 Caffeine

缓存在日常开发中起到至关重要的作用，由于是存储在内存中，数据的读取速度是非常快的，能大量减少对数据库的访问，减少数据库的压力。我们把缓存分为两类：

- 分布式缓存，例如Redis：
  - 优点：存储容量更大、可靠性更好、可以在集群间共享
  - 缺点：访问缓存有网络开销
  - 场景：缓存数据量较大、可靠性要求较高、需要在集群间共享
- 进程本地缓存，例如HashMap、GuavaCacha：
  - 优点：读取本地内存，没有网络开销，速度更快
  - 缺点：存储容量有限、可靠性较低、无法共享
  - 场景：性能要求较高，缓存数据量较小

两者一般是结合使用。

Caffeine 是一个基于 java8 开发的，提供了近乎最佳命中率的高性能的本地缓存库。目前 Spring 内部的缓存使用的就是 Caffeine。[GitHub](https://github.com/ben-manes/caffeine)  [文档](https://github.com/ben-manes/caffeine/wiki/Home-zh-CN)

```java
@Test
void test(){
    Cache<Key, Graph> cache = Caffeine.newBuilder()
        // 设置缓存的有效时间
    	.expireAfterWrite(10, TimeUnit.MINUTES)
        // 设置缓存的容量
    	.maximumSize(10_000)
    	.build();
    // 上面是两种缓存策略，还有一种引用缓存策略，利用GC来回收缓存数据。性能较差，不建议使用。

	// 查找一个缓存元素， 没有查找到的时候返回null
	Graph graph = cache.getIfPresent(key);
	// 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
    // 如果不存在 后面的 lambda 进行数据库查询操作
	graph = cache.get(key, k -> createExpensiveGraph(key));
	// 添加或者更新一个缓存元素
	cache.put(key, graph);
	// 移除一个缓存元素
	cache.invalidate(key);
}

```

在默认情况下，当一个缓存元素过期的时候，Caffeine 不会自动立即将其清理和驱逐。二是在一次读或写错操作后，或者在空闲时间对失效数据的驱逐。

## Lua语法入门

Nginx作为缓存中间件时使用 Lua 语言进行开发

在 Linux 系统中，运行 lua 文件 `lua 文件名.lua`

数组、table 都可以利用for循环来遍历：

- 遍历数组：

  ```lua
  -- 声明数组 key 为索引的 table
  local arr = {'java','python','lua'}
  -- 遍历数组
  for index, value in ipairs(arr) do
      print(index, value)
  end
  ```

- 遍历table：

  ```lua
  --声明 map，也就是 table
  local map = {name = 'Jack',age = 21}
  -- 遍历 table
  for key, value in pairs(map) do
      print(key,value)
  end
  ```

  

## 多级缓存

OpenResty是一个基于 Nginx 的高性能 Web 平台（和Tomcat 一样），用于方便的搭建能够处理超高并发、拓展性极高的动态 Web 应用、Web服务和动态网关。具备下列特点：

- 具备Nginx的完整功能
- 基于Lua语言进行扩展，集成了大量精良的Lua库、第三方模块
- 允许使用Lua自定义业务逻辑、自定义库

[官方网站](https://openresty.org/cn/)

### 安装 OpenResty

#### 1.安装

首先你的Linux虚拟机必须联网

##### **1）安装开发库**

首先要安装OpenResty的依赖开发库，执行命令：

```sh
yum install -y pcre-devel openssl-devel gcc --skip-broken
```



##### **2）安装OpenResty仓库**

你可以在你的 CentOS 系统中添加 `openresty` 仓库，这样就可以便于未来安装或更新我们的软件包（通过 `yum check-update` 命令）。运行下面的命令就可以添加我们的仓库：

```
yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
```



如果提示说命令不存在，则运行：

```
yum install -y yum-utils 
```

然后再重复上面的命令



##### **3）安装OpenResty**

然后就可以像下面这样安装软件包，比如 `openresty`：

```bash
yum install -y openresty
```



##### **4）安装opm工具**

opm是OpenResty的一个管理工具，可以帮助我们安装一个第三方的Lua模块。

如果你想安装命令行工具 `opm`，那么可以像下面这样安装 `openresty-opm` 包：

```bash
yum install -y openresty-opm
```



##### **5）目录结构**

默认情况下，OpenResty安装的目录是：/usr/local/openresty

![image-20200310225539214](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20200310225539214.png) 

看到里面的nginx目录了吗，OpenResty就是在Nginx基础上集成了一些Lua模块。



##### **6）配置nginx的环境变量**

打开配置文件：

```sh
vi /etc/profile
```

在最下面加入两行：

```sh
export NGINX_HOME=/usr/local/openresty/nginx
export PATH=${NGINX_HOME}/sbin:$PATH
```

NGINX_HOME：后面是OpenResty安装目录下的nginx的目录

然后让配置生效：

```
source /etc/profile
```



#### 2.启动和运行

OpenResty底层是基于Nginx的，查看OpenResty目录的nginx目录，结构与windows中安装的nginx基本一致：

![image-20210811100653291](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20210811100653291.png)

所以运行方式与nginx基本一致：

```sh
# 启动nginx
nginx
# 重新加载配置
nginx -s reload
# 停止
nginx -s stop
```





nginx的默认配置文件注释太多，影响后续我们的编辑，这里将nginx.conf中的注释部分删除，保留有效部分。

修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，内容如下：

```nginx
#user  nobody;
worker_processes  1;
error_log  logs/error.log;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       8081;
        server_name  localhost;
        location / {
            root   html;
            index  index.html index.htm;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```



在Linux的控制台输入命令以启动nginx：

```sh
nginx
```



然后访问页面：http://192.168.150.101:8081，注意ip地址替换为你自己的虚拟机IP：



#### 3.备注

加载OpenResty的lua模块：

```nginx
#lua 模块
lua_package_path "/usr/local/openresty/lualib/?.lua;;";
#c模块     
lua_package_cpath "/usr/local/openresty/lualib/?.so;;";  
```



common.lua

```lua
-- 封装函数，发送http请求，并解析响应
local function read_http(path, params)
    local resp = ngx.location.capture(path,{
        method = ngx.HTTP_GET,
        args = params,
    })
    if not resp then
        -- 记录错误信息，返回404
        ngx.log(ngx.ERR, "http not found, path: ", path , ", args: ", args)
        ngx.exit(404)
    end
    return resp.body
end
-- 将方法导出
local _M = {  
    read_http = read_http
}  
return _M
```



释放Redis连接API：

```lua
-- 关闭redis连接的工具方法，其实是放入连接池
local function close_redis(red)
    local pool_max_idle_time = 10000 -- 连接的空闲时间，单位是毫秒
    local pool_size = 100 --连接池大小
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then
        ngx.log(ngx.ERR, "放入redis连接池失败: ", err)
    end
end
```

读取Redis数据的API：

```lua
-- 查询redis的方法 ip和port是redis地址，key是查询的key
local function read_redis(ip, port, key)
    -- 获取一个连接
    local ok, err = red:connect(ip, port)
    if not ok then
        ngx.log(ngx.ERR, "连接redis失败 : ", err)
        return nil
    end
    -- 查询redis
    local resp, err = red:get(key)
    -- 查询失败处理
    if not resp then
        ngx.log(ngx.ERR, "查询Redis失败: ", err, ", key = " , key)
    end
    --得到的数据为空处理
    if resp == ngx.null then
        resp = nil
        ngx.log(ngx.ERR, "查询Redis数据为空, key = ", key)
    end
    close_redis(red)
    return resp
end
```



开启共享词典：

```nginx
# 共享字典，也就是本地缓存，名称叫做：item_cache，大小150m
lua_shared_dict item_cache 150m; 
```

### 请求参数处理

OpenResty 提供了各种 API 来获取不同类型的请求参数：

![image-20221125185135258](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221125185135258.png)

### 查询Tomcat

Nginx 内部发送 HTTP 请求
```lua
local resp = ngx.location.capture("/path",{
        method = ngx.HTTP_GET,  -- 请求方式
        args = {a=1,b=2},		-- get方式传参数
        body = "c=3&d=4"		-- post方式传参
    })
```

返回的响应内容包括：

- resp.status：响应状态码
- resp.header：响应头，是一个 table
- resp.body：响应体，就是响应数据

<font color="red">注意：</font>这里的 path 是路径，并不包含 ip 和端口。这个请求会被 nginx 内部的 server 监听并处理。

但是我们希望这个请求发送到 Tomcat 服务器，所以还需要编写一个 server 来对这个路径做反向代理：

```nginx
location /path {
    # 这里是服务器的 ip 地址和 java 服务端口，需要确保 服务器防火墙处于关闭状态
    proxy_pass http://xxx.xxx.xxx.xxx:port;
}
```

我们可以把 http 查询的请求封装为一个函数，放到 OpenResty 函数库中，方便后期使用。

1. 在 /usr/local/openresty/lualib/ 目录下创建 common.lua 文件：

   `vi /usr/local/openresty/lualib/common.lua`

2. 在 common.lua 中封装 http 查询的函数

   ```lua
   -- 封装函数，发送 http 请求，并解析响应
   local function read_http(path, params)
   	local resp = ngx.location.capture(path,{
       	-- 此处默认设置为了 get 请求
       	method = ngx.HTTP_GET,
       	args = params,
       })
       if not resp then
           -- 记录错误信息，返回404
           ngx.log(ngx.ERR,"http not found,path:", path, ", args:",args)
           ngx.exit(404)
       end
       return resp.body
   end
   -- 将方法导出
   local _M = {
       read_http = read_http
   }
   return _M    
   ```

> Tomcat集群的负载均衡

```nginx
# tomcat 集群配置
upstream tomcat-cluster{
    # 使用下面这句话的目的是使同一个路径多次发生请求时，去访问同一个服务器
    hash $request_uri; 
    server ip:port;
    server ip:port;
}

#反向代理配置，将 /xxx 路径的请求代理到 tomcat 集群
location /xxx {
    proxy_pass http://tomcat-cluster;
}
```

### Redis缓存预热

冷启动：服务刚刚启动时，Redis中并没有缓存，如果所有商品数据都在第一次查询时添加缓存，可能会给数据库带来较大压力。

**缓存预热**：在实际开发中，我们可以利用大数据统计用户访问的热点数据，在项目启动时将这些热点数据提前查询并保存到Redis中。数据量较少时，可以在启动时将所有数据都放入缓存中。

1. 利用 Docker 安装 Redis

   `docker run --name redis -p 6379:6379 -d redis redis-server --appendonly yes`

2. 在服务中引入Redis依赖

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

3. 配置 Redis 地址

   ```yaml
   spring:
     redis:
       host: xxx.xxx.xxx.xxx
   ```

4. 编写初始化类

   ```java
   @Component
   public class RedisHandler implements InitializingBean{
       @Autowired
       private StringRedisTemplate redisTemplate;
       @Override
       public void afterPropertiesSet() throws Exeeption{ // 初始化缓存...}
   }
   // 继承了InitializingBean这个接口当项目启动时当前类加载完毕以后自动执行继承的方法
   ```

### 查询Redis缓存

建立连接 发送请求 响应结果

OpenResty提供了操作Redis的模块，我们只要引入该模块就能直接使用：

- 引入Redis模块，并初始化Redis对象

  ```lua
  -- 引入redis模块
  local redis = require('resty.redis')
  -- 初始化 redis 对象
  local red = redis:new()
  -- 设置redis超时时间
  red:set_timeouts(1000,1000,1000)
  -- 第一个参数 建立连接的超时时间
  -- 第二个参数 发送请求的超时时间
  -- 第三个参数 响应结果的超时时间
  ```

- 封装函数，用来释放Redis连接，其实是放入连接池

  ```lua
  -- 关闭redis连接的工具方法，其实是放入连接池
  local function close_redis(red)
      local pool_max_idle_time = 10000 -- 连接的空闲时间，单位是毫秒,
      local pool_size = 100 -- 连接池大小
      -- 当空闲时间超过定义的空闲时间时 redis 才会真正的断开
      local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
      if not ok then
          ngx.log(ngx.ERR, "放入redis连接池失败：", err)
      end
  end
  ```

- 封装函数，从 Redis 读取数据并返回

  ```lua
  -- 查询 redis 的方法，ip和port时redis地址，key时查询的key
  local function read_redis(ip, port, key)
      -- 获取一个链接
      local ok, err = red:connect(ip, port)
      if not ok then
          ngx.log(ngx.ERR, '连接redis失败', err)
          return nil
      end
      -- 查询 redis
      local resp, err = red:get(key)
      -- 查询失败处理
      if not resp then
          ngx.log(ngx.ERR, '查询redis失败', err, ",key = ", key)
      end
      -- 得到的数据为空处理
      if resp == ngx.null then
          resp = nil
          ngx.log(ngx.ERR, '查询redis数据为空，key = ', key)
      end
      close_redis(red)
      return resp
  end
  ```

### Nginx本地缓存

OpenResty 为 Nginx 提供了 shard dict 的功能，可以在nginx的多个worker之间共享数据，实现缓存功能。

- 开启共享字典，在 nginx.conf 的 http下添加配置：

  ```nginx
  # 共享字典，也就是本地缓存，名称叫做：item_cache，大小 150m
  lua_shared_dict item_cache 150m;
  ```

- 操作共享字典：

  ```lua
  -- 获取本地缓存对象
  local item_cache = ngx.shared.item_cache
  -- 存储，指定key、value、过去时间，单位s，默认为0代表永不过期
  item_cache:set('key', 'value', 1000)
  -- 读取
  local val = item_cache:get('key')
  ```

## 缓存同步策略

缓存数据同步的常见方式有三种：

- 设置有效期：给缓存设置有效期，到期后自动删除。再次查询时更新
  - 优势：简单、方便
  - 缺点：时效性差，缓存过期之前可能不一致
  - 场景：更新频率较低，时效性要求低的业务
- 同步双写：在修改数据库的同时，直接修改缓存
  - 优势：时效性强，缓存与数据库一致
  - 缺点：有代码入侵
  - 场景：对一致性、时效性要求较高的缓存数据
- 异步通知：修改数据库时发送时间通知，相关服务监听到通知后修改缓存数据
  - 优势：低耦合，可以同时多个缓存业务
  - 缺点：时效性一般，可能存在中间不一致状态
  - 场景：时效性要求一般，有多个服务需要同步

基于MQ的异步通知
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126100422862.png" alt="image-20221126100422862" style="zoom:50%;" />

基于Canal的异步通知： 代码0侵入
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126100511995.png" alt="image-20221126100511995" style="zoom:50%;" />

Canal 是阿里巴巴旗下的一款开源项目，基于java开发。基于数据库增量日志解析，提供增量数据订阅&消费。[GitHub](https://github.com/alibaba/canal)

Canal 是基于 mysql 的主从同步来实现的，mysql 的主从同步的原理如下：

- mysql master 将数据变更写入二进制文件（binary log），其中记录的数据叫做 binary log events
- mysql slave 将 master 的 binary log events 拷贝到他的中继日志（relay log
- mysql slave 重放 relay log 中事件，将数据变更为反应他自己的数据。

Canal 就是把自己伪装成 mysql 的一个 slave 节点，从而监听 master 的 binary log 变化。再把得到的变化信息通知给 Canal 的客户端，进而完成对其他数据库的同步。

Canal 的一个安装步骤查看 <font color="red">安装Canal.md</font>

---

>  监听Canal

Canal 提供了各种语言的客户端，当 Canal 监听到 binlog 的变化时，会通知 Canal 的客户端
![image-20221126103147252](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126103147252.png)

不过官方的配置比较复杂，我们这里使用第三方开的 canal-starter 。[GitHub](https://github.com/normangyllenhaal/canal-client)

```xml
<!--引入依赖-->
<dependency>
	<groupId>top.javatool</groupId>
    <artifactId>canal-spring-boot-starter</artifactId>
    <version>1.2.1-RELEASE</version>
</dependency>
```

```yaml
# 编写配置
canal: 
  destination: heima 	#canal 实例名称，要跟 canal-server 运行时设置的 destination 一致
  server: ip:port		# canal地址
```

编写监听器，监听 canal 消息：

```java
@CanalTable("xxx") //指定要监听的表
@Component
public class ItemHandler implements EntryHandler<Item>{ //发模型指定表关联的实体类
    // 下面是监听到数据库的 增删改的消息
    @Override
    public void insert(Item item){
        // 新增数据到 redis
    }
    @Override
    public void update(Item before, Item after){
        // 更新 redis 数据
        // 更新本地缓存
    }
    @Override
    public void delete(Item item){
        // 删除 redis 数据
        // 清理本地缓存
    }
}
```

对实体类的 id 需要进行注解设置

```java
public class Item{
    @Id  // 此处要导入 spring 的包
    private Long id;
    
    @TableField(exist = false)
    @Transient  // 当实体类中的属性在数据库中对应的表中不存在时，需要添加这两个属性，如果不使用 canal 只加最上面的即可
    private Integer stock;
}
```

# Redis实践处理

## Redis 键值设计

> 优雅的 key 结构

redis 的 key 虽然可以自定义，但最好遵循下面的几个最佳时间约定：

- 遵循基本格式：[业务名称]:[数据名]:[id]
- 长度不超过 44 字节
- 不包含特殊字符

> 拒绝BigKey

BigKey 通常以 Key 的大小和 key 中成员的数量来综合判定，例如：

- key 本身的数据量过大：一个 String 类型的 key，它的值为 5MB
- key 中的成员数过多：一个ZSET类型的key，它的成员数量为10000个
- key 中成员的数据量过大：一个 Hash 类型的 key，它的成员数量虽然只有 1,000 个但这些成员的 Value 值的总大小为 100 MB

推荐值：

- 单个 key 的 value 小于 10kb
- 对于集合类型的 key ，建议元素数量小于 1000

BigKey的危害：

- 网络阻塞：对 BigKey 执行读请求时，少量的 QPS 就可能导致宽带使用率被占满，导致Redis实例，乃至所在物理机变慢
- 数据倾斜：BigKey 所在的 Redis 实力内存使用率远超其他实例，无法使数据分片的内存资源达到均衡
- Redis阻塞：对元素较多的 hash、list、zset 等做运算时会耗时较久，时主线程被阻塞
- CPU压力：对 BigKey 的数据序列化和反序列化会导致 CPU 的使用率飙升，影响 redis 实例和本机其他应用

如何发现 BigKey：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126115659279.png" style="zoom:50%;" />

如何删除 BigKey：

Bigkey内存占用较多，即便实时删除这样的 key 也需要消耗很长时间，导致 Redis 主线程阻塞，引发一系列问题。

- redis 3.0及以下版本：如果是集合类型，则遍历 Big Key 的元素，先逐个删除子元素，最后删除BigKey
- Redis 4.0以后：提供了异步删除的命令：unlink key 即可

> 恰当的数据类型

例如存储一个 User 对象，我们有三种存储方式：
![image-20221126142143519](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126142143519.png)

例如有 hash 类型的 key ，其中有100万对 field 和value，field是自增id，这个key存在什么问题？如何优化？
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126142307186.png" alt="image-20221126142307186" style="zoom: 67%;" />

存在的问题：

1. hash 的 entry 数量超过 500 时，会使用哈希表而不是 ZipList ，内存占用较多。
2. 可以通过 hash-max-ziplist-entries 配置 entry 上限。但是如果 entry 过多会导致 Big Key 问题

我们可以将大的hash拆分为小的 hash，将 id/100 作为 key，将 id % 100 作为 field，value不变
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126143045925.png" alt="image-20221126143045925" style="zoom:50%;" />

## 批处理优化

> Pipeline

Redis 提供了很多 Mxxx 这样的命令，可以实现批量插入数据，例如：

- mset
- hmset

不要在一次批处理中传输太多命令，否则单次命令占用带宽过多，会导致网络阻塞。

上面两个命令虽然可以进行批处理，但是却只能操作部分数据类型，因此如果有对复杂数据类型的批处理需要，建议使用Pipeline功能：

```java
@Test
void testPipeline(){
    // 创建管道，此处使用的时 jedis
    Pipeline pipeline = jedis.pipelined();
    // redisTemplate.executePipelined() 这个是 StringRedisTemplate的方法
    for(int i = 0; i <= 100000; i++){
        // 放入命令管道
        pipeline.set(key,value);
    }
}
```

> 集群下的批处理

如MSET或Pipeline这样的批处理需要在一次请求中携带多条命令，而此时如果 redis 是一个集群，那批处理命令的多个key<font color="red">必须</font>落在一个插槽中，否则就会导致执行失败。

![image-20221126151856055](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126151856055.png)

推荐使用<font color="red" >并行slot</font>

spring已经替我们完成了 并行slot

```java
@Test
void testMSetInCluster(){
    Map<String, String> map = new HashMap<>(2);
    map.put("name","rose");
    map.put("age","18");
    // 这个是批量进行添加数据
    redisTemplate.opsForValue().multiSet(map);
    
    // 下面是批量进行获取数据
    List<String> list = redisTemplate.opsForValue().multiGet(Arrays.asList("name","age"));
    list.forEach(System.out::println);
    
}
```

# 服务端优化

> 持久化配置

Redis的持久化虽然可以保证数据安全，但也会带来很多额外的开销，因此持久化应遵循下列建议：

1. 用来做缓存的Redis实例尽量不要开启持久化功能
2. 建议关闭RDB持久化功能，使用AOF持久化
3. 利用脚本定期在slave节点做RDB，实现数据备份
4. 设置合理的 rewrite 阈值，避免频繁的 bgrewrite
5. 配置 no-appendfsync-on-rewrite = yes，禁止在 rewrite 期间做 aof，避免因 AOF 引起的阻塞<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126154405163.png" alt="image-20221126154405163" style="zoom:50%;" />

部署有关建议：

1. Redis 实力的物理机要预留足够内存，应对 fork 和 rewrite
2. 每个 Redis 实力内存上线不要太大，例如 4G或 8G。可以加快fork的速度、减少主从同步、数据迁移压力
3. 不要与 CPU 密集型应用部署在一起
4. 不要与高硬盘负载应用一起部署。例如：数据库、消息队列

> 慢查询

在 Redis 执行耗时从超过某个阈值的命令，称为慢查询。慢查询会导致redis阻塞，甚至会出现业务故障。

慢查询的阈值可以通过配置指定：

- slowlog-log-slower-than：慢查询阈值，单位是微秒。默认是10_000，建议 1000

 慢查询回被放入慢查询日志中，日志的长度有上限，可以通过配置指定：

- slowlog-max-len：慢查询日志（本质是一个队列）的长度。默认是128，建议 1000

查看这两个配置可以使用：config get 命令，修改使用：config set xxx 修改后的值

eg：查看配置`config get slowlog-max-len`，修改配置 `config set slowlog-max-len 1000`

这种是动态配置，当服务重启以后就会失效

查看慢查询日志列表：

- slowlog len：查询慢查询日志长度
- slowlog get [n] ：读取 n 条慢查询日志
- slowlog reset：清空慢查询列表

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126184843242.png" alt="image-20221126184843242" style="zoom:50%;" />



> 命令及安全配置

Redis会绑定在 0.0.0.0:6379，这样将会将Redis服务暴漏到公网上，而Redis如果没有做身份认证，会出现严重的安全漏洞，

漏洞出现的核心的原因有以下几点：

- redis未设置密码
- 利用了redis的config set 命令动态修改redis配置
- 使用了root账号权限启动redis

避免漏洞，给出下面的建议：

- 设置密码
- 禁止线上使用下面命令：keys、flushall、flushdb、config set 等命令。可以利用rename-command禁用，
- bind：限制网卡，禁止外网网卡访问
- 开启防火墙
- 不要使用root账户启动redis
- 尽量不要使用默认端口

> 内存配置

 当Redis内存不足时，可能会导致Key频繁被删除、响应时间变长、QPS不稳定等问题。当内存使用率到达90%以上时就需要我们警惕，并快速定位到内存占用的原因。

  

| 内存占用   | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| 数据内存   | 是Redis最主要的部分，存储Redis的键值信息。主要问题是BigKey问题就、内存碎片问题 |
| 进程内存   | Redis主进程本身运行肯定需要占用内存，如代码、常量池等；这部分内存大约几兆，在大多数生产环境中与Redis数据占用的内存相比可以<font color="red">忽略</font> |
| 缓冲区内存 | 一般包括客户端缓冲区、AOF缓冲区、复制缓冲区等。客户端缓冲区又包括输入缓冲区和输出缓冲区两种。这部分内存占用波动较大，不当使用BigKey，可能导致内存移除 |

数据内存的问题：redis提供了一些命令，可以查看到Redis目前的内存分配状态：

- info memory：代表查看内存相关的信息
- memory key

内容缓存区常见的有三种：

- 复制缓冲区：主从复制的repl_backlog_buf，如果太小可能导致频繁的全量复制，影响性能。通过repl_backlog_size来设置，默认 1 mb
- AOF缓冲区：AOF刷盘之前的缓存区域，AOF执行rewrite的缓冲区。无法设置容量上限。
- 客户端缓冲区：分为输入缓冲区和输出缓冲区，输入缓冲区最大1G且不能设置。输出缓冲区可以设置。
  ![image-20221126200945028](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126200945028.png)

# 集群最佳实践

集群虽然具备高可用特性，能实现自动故障恢复，但是如果使用不当，也会存在一些问题：

1. 集群完整性问题

   在redis的默认配置中，如果发现任意一个插槽不可用，则整个集群都会停止对外服务，为了保证高可用性，这里建议将 cluster-require-full-coverage 配置为 no

2. 集群带宽问题

   集群节点之间会不断的ping来确定集群中其他节点的状态。每次ping携带的信息至少包括：

   - 插槽信息
   - 集群状态信息

   集群中节点越多，集群状态信息数据量也越大，10个节点的相关信息可能达到1kb，此时每次集群互通需要的带宽非常高。

   解决途径：

   - 避免大集群，集群节点数不要太多，最好少于1000，当然越少越好。如果业务庞大，则建立多个集群
   - 避免在单个物理机中运行太多redis实例
   - 配置合适的 cluster-node-timeout 值，即减少ping的频率

3. 数据倾斜问题

4. 客户端性能问题

5. 命令的集群兼容性问题

6. lua和事务问题

单体 Redis（主从Redis）已经能达到万级别的QPS，并且也具备很强的高可用特性，如果主从能满足业务需求的情况下，尽量不搭建Redis集群
