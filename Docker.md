Docker 的一个架构

![image-20221204223202088](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221204223202088.png)

[docker命令大全](https://www.runoob.com/docker/docker-command-manual.html)

# 安装 Docker

<font color="red">注意 以下都是基于 centos7</font>

```shell
# yum 包更新到最新
yum update
# 安装需要的软件包，
yum install -y yum-utils device-mapper-persistent-date lvm2
# 设置yum源
yum-config-managet --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 安装docker，出现输入的界面都按y
yum install -y docker-ce
# 查看docker版本，验证是否安装成功
docker -v
```

配置镜像加速器，我们使用阿里云的，但是这个地址每个人的是不一样的。

登录阿里云，打开控制台，搜索容器镜像服务，点击镜像加速器，里面有命令粘贴过来直接运行即可

# Docker 命令

服务相关命令

- 启动docker服务				`systemctl start docker`
- 停止docker服务				`systemctl stop docker`
- 重启docker服务				`systemctl restart docker`
- 查看docker服务状态 		`systemctl status docker`
- 开机启动docker服务		`systemctl enable docker`

---

镜像相关命令

- 查看镜像 			`docker images `     `docker images -q`查看所用镜像的id
- 搜索镜像			`docker search redis` 搜索redis   一般去此网站进行搜索 [DockerHub](https://hub.docker.com/)
- 拉取镜像			`docker pull redis:3.2` 安装3.2的版本 如果没有冒号后面的就是最新版本
- 删除镜像			`docker rmi IMAGEID/名字:TAG`   全部删除 docker rmi \` docker images -q \` 

---

容器相关命令

- 创建容器		

  `docker run -it --name c1 centos:7 /bin/bash` 

  `docker run -id --name c2 centos:7`
  
  d 表示后台运行容器，即使用exit命令以后后天继续运行
  
  - -it 创建的容器一般称为交互式容器，-id 创建的容易一般称为守护式容器
  - -i 保持容器运行。通常与 -t 同时使用。加入it两个参数后，容器创建后会自动进入容器中，退出容器后，容器自动关闭。
  - -t 为容器重新分配一个伪输入终端，通常与 -i 同时使用
  - -d 以后台模式运行容器。需要使用 docker exec 进入容器，退出后容器不会关闭
  - --name 为创建的容器命名
  
- 查看容器

  查看正在运行的容器：`docker ps` 

  查看所有容器：`docker ps -a`

  查看所有容器id：`docker ps -aq`

- 进入容器内部

  `docker exex -it c2 /bin/bash` c2 表示容器名字，/bin/bash 表示分配终端

- 启动容器

  `docker start 容器名`

- 停止容器

  `docker stop 容器名`

- 删除容器

  `docker rmi 容器id/名称`

  删除所有的容器：docker rm \` docker ps -aq \` 两个` 之间是运行docker 命令

  不能删除正在运行的容器

- 查看容器信息

  `docker inspect 容器名`

# Docker 容器的数据卷

概念

- 数据卷是宿主机中的一个目录或文件
- 当容器目录和数据卷目录绑定后，对方的修改会立即同步。但是当容器被删除后数据卷中的数据仍然存在。
- 一个数据卷可以被多个是容器同时挂载

作用

- 容器数据持久化
- 外部机器和容器间接通信
- 容器之间数据交换

**配置数据卷**

创建启动容器时，使用 -v 参数 设置数据卷
`docker run ... -v 宿主机目录(文件):容器内目录(文件) ...`  挂载多个目录或文件 多次使用这个命令 
eg：`docker run -it --name c1 -v /root/data:/root/dota_container centos:7`

注意事项：

1. 目录必须是绝对路径，即使用 / 开头
2. 如果目录不存在，会自动创建
3. 可以挂载多个数据卷

**数据卷容器**

多容器进行数据交换有两种办法：1.多个容器改在同一个数据卷，2.数据卷容器

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221209213038506.png" alt="image-20221209213038506" style="zoom:50%;" />

即使 c3 挂了或者是删除了也不影响

1. 创建启动 c3 数据卷容器，使用 -v 参数 设置数据卷

   `docker run -it --name c3 -v /volume centos:7 /bin/bash` volume 是容器目录，这样写会在宿主机同步生成一个

2. 创建启动 c1 c2 容器，使用 `--valumes-from` 参数设置数据卷

   ```shell
   docker run -it --name c1 --volumes-from c3 centos:7 /bin/bash
   docker run -it --name c2 --volumes-from c3 centos:7 /bin/bash	
   ```

# Docker 应用部署

容器内的网络服务和外部机器不能直接进行通信，当容器中的网络服务需要被外部机器访问时，可以将容器中提供服务的端口映射到宿主机的端口上。外部机器访问宿主即的该端口，从而间接访问容器的服务。这种操作成为：端口映射

## MySQL部署

实现步骤：

1. 搜过mysql镜像

2. 拉去MySQL镜像

3. 创建容器，设置端口映射、目录映射

   ```shell
   # 在 /root 目录下创建mysql目录用于存储mysql数据信息
   mkdir ~/mysql
   cd ~/mysql
   ```

   ```shell
   # \ 代表换行
   docker run -id \
   -p 3306:3306 \
   --name c_mysql \
   -v $PWD/conf:/etc/mysql/conf.d \
   -v $PWD/logs:/logs \
   -v $PWD/data:/var/lib/mysql \
   -e MYSQL_ROOT_PASSWORD=123456 \
   mysql:5.6
   ```

   - 参数说明
     - -p 3307:3306 ：将容器的 3306 端口映射到宿主机的 3307 端口
     - -v $PWD/conf:/etc/mysql/conf.d ：将主机当前目录下的 conf/my.cnf 挂载到容器的 /etc/mysql/my.cnf 配置目录
     - -v $PWD/logs:/logs：将主机当前目录下的 logs 目录挂载到容器的 /logs。日志目录
     - -v $PWD/data:/var/lib/mysql：将主机当前目录下的 data 目录挂载到容器的 /var/lib/mysql 目录
     - -e MYSQL_ROOT_PASSWORD=123456：初始化root 用户的密码

4. 操作容器中的MySQL

## Tomcat部署

1. 搜索tomcat镜像

2. 拉取tomcat镜像

3. 创建容器，设置端口映射、目录映射

   ```shell
   # 在 /root 目录下创建 tomcat 目录用于存储 tomcat 数据信息
   mkdir ~/tomcat
   cd ~/tomcat
   ```

   ```shell
   docker run -id --name c_tomcat \
   -p 8080:8080 \
   -v $PWD:/usr/local/tomcat/webapps \
   tomcat
   ```

   - 参数说明：
     - -p 8080:8080 ：将容器的8080端口映射到主机的8080端口
     - -v $PWD:/usr/local/tomcat/webapps ：将主机当前目录挂载到容器的 webapps

4. 部署项目  

## Nginx部署

1. 搜索镜像

2. 拉取镜像

3. 创建容器

   ```shell
   # 在 /root 目录下创建 nginx 目录用于存储 nginx 数据信息
   mkdir ~/nginx
   cd ~/nginx
   mkdir conf
   cd conf
   # 在 ~/nginx/conf 下创建 nginx.conf 文件，粘贴下面内容
   ```

   ```nginx
   user nginx;
   worker_processes 1;
   
   error_log /var/log/nginx/error.log warn;
   pid /var/run/nginx.pid
   
   events {
       worker_connections 1024;
   }
   
   http {
       include /etc/nginx/mime/types;
       default_type application/octet-stream;
       
       log_format main '$remote_addr - $remote_user [$time_local] "$request"'
           			'$status $body_bytes_sent "$http_referer"'
           			'"$http_user_agent" "$http_x_forwarded_for"';
       access_log /var/log/nginx/accexx.log main;
       sendfile on;
       #tcp_nopush on;
       
       keepalive_timeout 65;
       
       #gzip on;
       include /etc/nginx/conf.d/*.conf;
   }
   ```

   ```shell
   docker run -id --name c_nginx \
   -p 80:80 \
   -v $PWD/conf/nginx.conf:/etc/nginx/nginx.conf \
   -v $PWD/logs:/var/log/nginx \
   -v $PWD/html:/usr/share/nginx/html \
   nginx
   ```

   

4. 测试访问



## Redis部署

1. 搜索镜像

2. 拉取镜像

3. 创建容器，设置端口映射

   ```shell
   docker run -id --name c_redis -p 6379:6379 redis
   # 内存操作不需要目录映射
   ```

4. 使用外部机器连接redis

   ```shell
   ./redis-cli.exe -h ip -p 端口 
   ```

# Dockerfile

## 镜像制作

容器转为镜像

```shell
# 通过挂载的目录不会被打包到容器中去
docker commit 容器id 镜像名称:版本号
docker save -o 压缩文件名称 镜像名称:版本号
docker load -i 压缩文件名称
```

## Dockerfile概念及作用

- 是一个文本文件
- 包含了一条条的指令
- 每一条指令构建一层，基于基础镜像，最终构建出一个新的镜像
- 对于开发人员：可以为开发团队提供一个完全一致的开发环境
- 对于测试人员：可以直接拿开发时所构建的镜像或者通过 Dockerfile 文件构建一个新的镜像开始工作了
- 对于运维人员：在部署时，可以实现应用的无缝移植

# 