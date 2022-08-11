# Nginx

Nginx是一款轻量级的 Web 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。其特点是占有内存少，并发能力强。

![image-20220810173812781](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810173812781.png)

重点文件/目录

- conf/nginx.conf					Nginx配置文件
- html										存放静态文件（html,css,js等）
- logs										日志目录，存放日志文件
- sbin/nginx							二进制文件，用于启动、停止Nginx服务

## Nginx命令

- 在sbin目录下
  -  使用 `./nginx -v`即可完成查看版本号
  - `./nginx -t` 检查conf/nginx/conf 文件配置的是否有错误
  - 启动和停止
    - 启动命令 `./nginx`		默认端口为 80 
    - 停止命令 `./nginx -s stop`
  - 修改配置文件后重新加载配置文件 `./nginx -s reload`

这样执行语句过于繁琐，要么使用绝对路径，要么进入到对应的文件

在 /etc/profile 文件中 修改 PATH 追加 /usr/local/nginx/sbin  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810180719463.png" alt="image-20220810180719463" style="zoom:50%;" />
重载配置文件 `source /etc/profile`
就能够直接使用 `nginx -v` 类似的命令

## 配置文件结构

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810211219710.png" style="zoom:50%;" />

- 全局块：
  全局块是和Nginx运行相关的全局配置

- Events块：
  配置网络连接相关的配置

- Http块
  代理、缓存、日志记录、虚拟主机配置

  - http全局块

  - Server块
    - Server全局块
    - location块

注意：http块中可以配置多个Server块，每个Server块中可以配置多个location块

## Nginx具体应用

### 部署静态资源

Nginx可以作为静态web服务器来部署静态资源。静态资源指在服务端真实存在并且能够直接展示的一些文件，比如常见的html页面、css文件、js文件、图片、视频等资源。

相对于Tomcat，Nginx处理静态资源的能力更加高效，所以在生产环境下，一般都会将静态资源部署到Nginx中。将静态资源部署到Nginx非常简单，只需要将文件复制到Nginx安装目录下的html目录中即可。

```nginx
server {
    listen 80;					# 监听端口
    server_name localhost;		# 服务器名称 用来绑定域名
    location / {				# 匹配客户端请求url
        root html;				# 指定静态资源根目录
        index index.html		# 指定默认首页
    }
}
```



### 反向代理

- 正向代理：

  是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端。

  正向代理的典型用途是为在防火墙内的局域网客户端提供访问Internet的途径。比如上外网

  正向代理一般是在**客户端设置代理服务器**，通过代理服务器转发请求，最终访问到目标服务器。

- 反向代理：

  反向代理服务器位于用户与目标服务器之间，但是对于用户而言，反向代理服务器就相当于目标服务器，即用户直接访问反向代理服务器就可以获得目标服务器的资源，反向代理服务器负责将请求转发给目标服务器。

  用户**不需要知道目标服务器的地址**，也无须在用户端作任何设定。
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810213302008.png" alt="image-20220810213302008" style="zoom:50%;" />

  ```nginx
  # 配置反向代理
  server {
      listen 82;
      server_name localhost ;
      location / {
          proxy_pass http://192.168.138.101:8080 ;# 反向代理配置，将请求转发到指定服务
      }
  }
  ```

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810214226508.png" alt="image-20220810214226508" style="zoom:50%;" />

### 负载均衡

早期的网站流量和业务功能都比较简单，单台服务器就可以满足基本需求，但是随着互联网的发展，业务流量越来越大并且业务逻辑也越来越复杂，单台服务器的性能及单点故障问题就凸显出来了，因此需要多台服务器组成应用集群,进行性能的水平扩展以及避免单点故障出现。

- 应用集群：将同意应用部署到多台机器上，组成应用集群，接受负载均衡器分发的请求，进行业务处理并返回响应数据
- 负载均衡：将用户请求根据对应的负载均衡算法分发到应用集群中的一台服务器进行处理（基于反向代理
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810225929096.png" alt="image-20220810225929096" style="zoom:50%;" />

```nginx
# 配置负载均衡
upstream targetserver{				# upstream指令可以定义一组服务器
    server 192.168.1.1:8080;		# 有默认的使用策略，默认轮询使用
    server 192.168.1.1:8081;
}
server {
    listen 8080;
    server_name localhost ;
    location / {
        proxy_pass http://targetserver ;	# http://targetserver 指代上面的targetserver
    }
}
```

| 名称       | 说明             |
| ---------- | ---------------- |
| 轮询       | 默认方式         |
| weight     | 权重方式         |
| ip_pash    | 依据ip分配方式   |
| least_conn | 依据最少链接方式 |
| url_hash   | 依据url分配方式  |
| fair       | 依据响应时间方式 |

eg：weight的使用 `server 192.168.1.1:8080 weight=2;`



