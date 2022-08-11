# Linux

执行Linux命令时，如果提示信息显示为乱码，使用命令 `echo 'LANG="en_US.UTF-8"' >> /etc/profile `
再执行命令 `source /etc/profile` 使编码重新加载一下

一次 Tab 键实现自动补全，两次 Tab 键给出操作提示



Linux命令格式 $\textcolor{red}{command [-options] [parameter]}$

- command：命令名
- [-options] ：选项，可用来对命令进行控制，也可以省略
- [parameter]：传给命令的参数，可以是零个，一个或多个
- 命令名、选项、参数之间有空格进行分割

## 常用命令

| 命令            | 对应英文             | 作用                     |
| --------------- | -------------------- | ------------------------ |
| ls              | list                 | 查看当前目录下的内容     |
| pwd             | print work directory | 查看当前所在的目录       |
| cd [目录名]     | change directory     | 切换目录                 |
| touch [文件名]  | touch                | 如果文件不存在，新建文件 |
| mkdir [目录名]  | make directory       | 创建目录                 |
| rm [文件名]     | remove               | 删除指定文件             |
| clear  / CTRL+L | clear                | 清屏                     |

## 文件目录操作命令

### ls：显式指定目录下的内容

语法：ls [-al] [dir]		（dir表示文件的路径

说明：

- -a 显示所有文件及目录，（.开头的隐藏文件也会列出
- -l 除文件名称外，同时将文件形态（d表述目录，-表示文件）、权限、拥有者、文件大小等信息列出

由于我们使用ls命令时经常加入 -l 选项，所以Linux为 ls -l 命令提供了一种简写方式，即 `ll`



### cd：用于切换当前工作目录，即进入指定目录

语法：cd [dirName]

特殊说明：

- ~ 表示用户的home目录
- . 表示目前所在的目录
- .. 表示目前目录的上级目录



### cat：用于显示文件内容

语法：cat [-n] fileName

说明：-n：由1开始对所有输出的行数编号

举例： cat /etc/profile				查看/etc目录下的profile文件内容



### more：以分页的形式显示文件内容

与cat非常类似，cat是一下子显示全部

语法：more fileName

操作说明：

- 回车键：向下滚动一行
- 空格键：向下滚动一屏
- b		 ：返回上一屏
- q / CTRL+C：退出more

举例：more /etc/profile 					以分页方式显示/etc目录下的profile文件内容



### tail：查看文件末尾的内容

一般用来监控日志文件

语法：tail [-f] fileName

说明：动态读取文件末尾内容并显示，通常用于日志文件的内容输出

举例：

tail /etc/profile 				显示/etc目录下的profile文件末尾10行的内容

tail -20 /etc/profile		   显示/etc目录下的profile文件末尾20行的内容

tail -f /itcast/my.log		  动态读取/itcast目录下的my.log文件末尾内容并显示



### mkdir：创建目录

语法：mkdir [-p] dirName

说明：-p 确保目录名称存在，不存在就创建一个。通过此选项，可以实现多层级创建



### rmdir：删除空目录

语法：rmdir [-p] dirName

说明：-p：当子目录被删除后使父目录为空目录的话，则一并删除

举例：

- rmdir itcast 						删除名为itcast的空目录
- rmdir -p itcast/test			 删除itcast目录中的test的子目录，如删除后itcast变为空目录，删除itcast目录
- rmdir itcast*						删除名称以itcast开始的空目录



### rm：删除文件或者目录

语法：rm [-rf] name

说明：

-r：将目录中所有文件（目录）删除

-f：无需确认，直接删除

-d：删除目录

举例：

- rm -f itcast/ 							删除名为itcast的目录和目录中的所有文件，删除前需要确认
- rm -rf itcast/							删除名为itcast的目录和目录中的所有文件，无需确认
- rm -f hello.txt 						 无需确认，直接删除hello.txt



## 拷贝移动命令

### cp：用于复制文件或者目录

语法：cp [-r] source dest

说明： -r：如果复制的使目录需要使用此选项，此时将复制该目录下的所有文件

举例：

- cp hello.txt itcast/					将hello.txt复制到itcast目录中
- cp hello.txt ./hi.txt					将hello.txt赋值到当前目录，并改名为hi.txt
- cp -r itcast/  ./itheima/			 将itcast目录和目录内的所有文件复制到itheima中
- cp -r itcast/*  ./itheima/			将itcast目录内的所有文件复制到itheima中



### mv：为文件或目录改名或移动位置

语法：mv source dest

举例：

- mv hello.txt hi.txt							改名
- mv hi.txt itheima/ 			  			移动到itheima目录下
- mv hi.txt itheima/hello.txt 			  移动到itheima目录下，并改名为hello.txt
- mv itcast/ itheima/						 如果itheima目录不存在，将itcast改名为itheima
- mv itcast/ itheima/						 如果itheima目录存在，将itcast移动到itheima中



## 打包压缩命令

tar：对文件进行打包、解包、压缩、解压

语法：tar [-zcxvf] fileName [files]

包文件后缀为 .tar 表示只是完成了打包，并没有压缩

包文件后缀为 .tar.gz 表示打包的同时还进行了压缩

说明：

- -z：z 代表的使 gzip ，通过gzip命令处理文件，gzip 可以对文件压缩或者解压
- -c：c 代表的是create，及创建新的包文件，打包
- -x：x 代表的是 extract ，实现从包文件中还原文件，解包
- -v：v 代表的是 verbose ，显示命令的执行过程
- -f：f 代表的是 file ，用于指定包文件的名称

举例：

- 有一个目录test进行打包，打的包名为test.tar`tar -cvf test.tar test` 
- 有一个目录test进行打包并压缩，打的包名为test.tar.gz`tar -zcvf test.tar.gz test`
- 解包的操作`tar -xvf test.tar` 
- 解压的操作 ` tar -zxvf test.tar.gz `

下图的 -C表示解压到指定的路径中去

![image-20220807171534608](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220807171534608.png)



## 查找命令

### find：在指定目录下查找文件

语法：find dirName -option fileName

举例：

- find . -name "*.java"								在当前目录及其子目录下查找 .java 结尾的文件
- find /itcast -name "*.java"						在/itcast目录及其子目录下查找 .java 结尾的文件



### grep：从指定文件中查找指定的文本内容

语法：grep word fileName

举例：

- grep Hello HelloWord.java				查找HelloWord.java文件中穿线Hello字符串的位置
- grep hello *.java								查找当前目录中所有 .java 结尾的文件中包含hello字符串的位置



## 软件安装

### 软件安装方式

![image-20220807191837500](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220807191837500.png)



## 其余杂项

ps -ef ：查看所有运行的进程的信息

`|`在 Linux 中成为管道符，可以将前一个命令的结果输给后一个命令作为输入

在使用 `ps` 命令查看进程时，经常配合管道符和查找命令 grep 一起使用，来查看特定进程，比如查看 tomcat 进程 `ps -ef | grep tomcat`



### JDK安装

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808111910471.png" style="zoom:50%;" />



### Tomcat

安装操作步骤

1. 使用FinalShell自带的上传工具将Tomcat的二进制发布包上传到Linuxapache-tomcat-7.0.57.tar.gz
2. 解压安装包，命令为tar -zxvf apache-tomcat-7.0.57.tar.gz-C/usr/local
3. 进入Tomcat的bin目录启动服务，命令为sh startup.sh或者./startup.sh



打开tomcat所在的文件 /usr/local/apa.../bin  

执行语句 sh startup.sh 开启进程

执行语句 sh shutdown.sh 关闭进程

### 当前系统中安装的软件

rpm -qa														查询当前系统中安装的所有软件

rpm -qa | grep mysql				  			 	查询安装的软件中名称带有mysql的软件

rpm -e --nodeps 软件名称							卸载软件

### Mysql

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808112434091.png" alt="image-20220808112434091" style="zoom:50%;" />



<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220807224922543.png" alt="image-20220807224922543" style="zoom:50%;" />

`cat /var/log/mysqld.log | grep password`查看mysql密码

### 树形查看文件

`yum install tree`

安装完成以后使用 `tree` 命令即可树形图查看当前文件夹内的文件目录层次
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810174658987.png" alt="image-20220810174658987" style="zoom: 50%;" />

### Nginx安装

![image-20220810173812781](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220810173812781.png)

## 项目部署

### 手工部署项目

1. 先打包

2. 将 jar 包上传到Linux服务器

   `mkdir /usr/local/app` 		创建目录，将项目jar包放到此目录

3. 启动SpringBoot程序

   `java -jar 包名.jar`

4. 改为后台运行SpringBoot程序，并将日志输出到日志文件

   目前程序运行的问题

   - 线上程序不会采用控制台霸屏的形似和运行程序，而是将程序在后台运行

   - 线上程序不会将日志输入到控制台，而是输出到日志文件，方便运维查阅信息

     nohup命令:英文全称no hang up(不挂起)，用于不挂断地运行指定命令，退出终端不会影响程序的运行语法格式:nohup command \[ Arg ...][&]

     参数说明:

     Command:要执行的命令

     Arg:一些参数，可以指定输出文件

     &:让命令在后台运行

     举例:

     nohup java -jar boot工程.jar &> hello.log &			后台运行java -jar命令，并将日志输出到hello.log文件



### 通过Shell脚本自动部署项目

1. Linux中安装GIt

   `yum install git`
   使用 GIt 克隆代码 `git clone 链接` ，一般放在 /usr/local 中

2. Linux中安装Maven

   找到安装包上传到Linux

   ```sh
   tar -zxvf apache-maven-3.5.4-bin.tar.gz -C /usr/local
   vim /etc/profile 					# 修改配置文件，加入如下内容
   export MAVEN_HOME=/usr/local/apache-macen-3.5.4
   export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
   
   source /etc/profile					# 使配置文件重新生效
   mvn -version						# 查看maven版本
   vim /usr/local/apache-maven-3.5.4/conf/settings.xml			# 修改配置文件
   <localRepository>/usr/local/repo</localRepository>			# 指定本地仓库
   ```

3. 编写 Shell 脚本（拉取代码、编译、打包、启动）

   自己编写

4. 为用户授予执行 Shell 脚本的权限

   - chmod（英文全拼：change mode）命令是控制用户对文件的权限的命令

   - Linux中的权限分为：读(r) 、写(w)、执行(x)三种权限
     <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808143030172.png" alt="image-20220808143030172" style="zoom:67%;" />

   - Linux中的文件调用权限分为三级：文件所有者(Owner)、用户组(Group)、其他用户(Other User)

   - 只有文件的所有者和超级用户可以修改文件或目录的权限

   - 执行 Shell 脚本需要对此脚本文件的执行权限，如果没有则不能执行

   - 授权

     <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220808142110433.png" alt="image-20220808142110433" style="zoom:50%;" />

5. 执行 Shell 脚本









### 如何停止部署的项目呢？

- 来查看端口号所对应的进程： `netstat -nlp|grep :8080`  
  									比如结果是 `tcp6 12 0 :::8080  :::*  LISTEN   18794/java  `
  使用 `kill -9 18794`  18794 对应查询出的结果的最后面的数字

- 使用命令 `ps -ef | grep 'java -jar'`

  比如结果是 `root 14584  1728  2 13:33 pts/0 00:00:07 java -jar Hello-0.0.1-SNAPSHOT.jar`

  使用 `kill -9 14584 ` 14584对应查询结果的第一个数字