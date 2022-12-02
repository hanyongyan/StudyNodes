初始 docker：

- Docker 是一个开源的应用容器引擎
- Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的Linux机器上
- 容器是完全使用沙箱机制，相互隔离
- 容器性能开销极低
- 从 17.03 后分为 CE 即社区版和 EE 企业版

docker 是一种容器技术，解决软件跨环境迁移的问题。

---

Centos7 安装 docker

```shell
# 1.yum 包更新到最新
yum update
# 2.安装需要的软件包，yum-utin 提供 yum-config-manager 功能，另外两个是 devicemapper 驱动依赖的
yum install -y yum-utils device-mapper-persistnet lvm2
# 3.设置 yum 源
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 4.安装docker，出现的输入界面都按y
yum install -y docker -ce
# 5.查看docker版本，验证是否安装成功
docker
```

