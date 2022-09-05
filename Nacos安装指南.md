# Nacos安装指南



# 1.Windows安装

开发阶段采用单机安装即可。

## 1.1.下载安装包

在Nacos的GitHub页面，提供有下载链接，可以下载编译好的Nacos服务端或者源代码：

GitHub主页：https://github.com/alibaba/nacos

GitHub的Release下载页：https://github.com/alibaba/nacos/releases

如图：

![image-20220905210035481](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210035481.png)





## 1.2.解压

将这个包解压到任意非中文目录下，如图：

![image-20210402161843337](assets/image-20210402161843337.png)

目录说明：

- bin：启动脚本
- conf：配置文件



## 1.3.端口配置

Nacos的默认端口是8848，如果你电脑上的其它进程占用了8848端口，请先尝试关闭该进程。

**如果无法关闭占用8848端口的进程**，也可以进入nacos的conf目录，修改配置文件中的端口：

![image-20220905210056801](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210056801.png)

修改其中的内容：

![image-20220905210107326](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210107326.png)



## 1.4.启动

启动非常简单，进入bin目录，结构如下：

![image-20220905210117924](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210117924.png)

然后执行命令即可：

- windows命令：

  ```
  startup.cmd -m standalone
  ```


执行后的效果如图：

![image-20220905210126305](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210126305.png)



## 1.5.访问

在浏览器输入地址：http://127.0.0.1:8848/nacos即可：

![image-20220905210136714](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210136714.png)

默认的账号和密码都是nacos，进入后：

![image-20220905210142126](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210142126.png)





# 2.Linux安装

Linux或者Mac安装方式与Windows类似。

## 2.1.安装JDK

Nacos依赖于JDK运行，索引Linux上也需要安装JDK才行。

上传jdk安装包：

![image-20220905210149744](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210149744.png)

上传到某个目录，例如：`/usr/local/`



然后解压缩：

```sh
tar -xvf jdk-8u144-linux-x64.tar.gz
```

然后重命名为java



配置环境变量：

```sh
export JAVA_HOME=/usr/local/java
export PATH=$PATH:$JAVA_HOME/bin
```

设置环境变量：

```sh
source /etc/profile
```





## 2.2.上传安装包

如图：

![image-20220905210156893](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210156893.png)



上传到Linux服务器的某个目录，例如`/usr/local/src`目录下：

![image-20220905210213617](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210213617.png)



## 2.3.解压

命令解压缩安装包：

```sh
tar -xvf nacos-server-1.4.1.tar.gz
```

然后删除安装包：

```sh
rm -rf nacos-server-1.4.1.tar.gz
```

目录中最终样式：

![image-20220905210222024](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210222024.png)

目录内部：

![image-20220905210228222](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220905210228222.png)



## 2.4.端口配置

与windows中类似



## 2.5.启动

在nacos/bin目录中，输入命令启动Nacos：

```sh
sh startup.sh -m standalone
```







# 3.Nacos的依赖

父工程：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.5.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```



客户端：

```xml
<!-- nacos客户端依赖包 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

```



# 4.yml文件配置

配置到子工程中

```yaml
spring:
 cloud: 
  nacos:
   server-addr: localhost:8848  # nacos fu'wu'duan'di
```



