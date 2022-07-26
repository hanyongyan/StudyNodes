# Git

## Git 常用命令

工作流程

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708114431611.png" alt="image-20220708114431611" style="zoom:50%;" />



Git的默认编辑器配置

```shell
# 使用vim编辑器
git config --global core.deitor vim
#使用vscode编辑器
git config --global core.deitor Code
#使用Notepad++编辑器
git config --global core.deitor notepad++
```



### 基本配置

设置用户信息

- git config --global user.name "xxx"
- git config --global user.email "xxx@xx.xx"

查看配置信息

- git config --global user.name
- git config --global user.email

 ### 别名

为常用指令配置别名
有些常用的指令参数非常多，每次都要输入好多参数，我们来使用别名

1. 打开用户目录，创建 `.bashrc` 文件  `eg：C:\Users\54656`

   如系统不支持直接创建，使用git Bash，执行 touch `~/.bashrc`

2. 在 `.bashrc` 文件中输入如下内容

   ```shell
   #用于输出git提交日志
   alias git-log='git log --pretty=oneline --all --graph --abbrev-commit'
   #用于输出当前目录所有文件及基本信息
   alias ll='ls -al'
   # alias 定义的关键字 
   # xxx = "zzz"  xxx 代表别名 zzz代表需要起别名的参数指令
   ```

3. 在用户目录下，打开 git Bash，执行 `source ~/.bashrc`

### 获取本地仓库

1. 在电脑的任意位置创建一个空目录（例如test）作为我们的本地Git仓库 
2. 进入这个目录中，点击右键打开Git bash窗口 
3. 执行命令git init 
4. 如果创建成功后可在文件夹下看到隐藏的.git目录。

### 基础操作指令

Git工作目录下对于文件的修改(增加、删除、更新)会存在几个状态，这些修改的状态会随着我们执行Git 的命令而发生变化。

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708100215521.png" alt="image-20220708100215521" style="zoom:67%;" />

1. git add (工作区 --> 暂存区) 
2. git commit (暂存区 --> 本地仓库



查看修改的状态

- 作用：查看修改的状态（暂存区，工作区）
- 命令形式：git status



添加工作去到暂存区

- 作用：添加工作区一个或多个文件的修改到暂存区
- 命令形式：git add 单个文件名 | 通配符 
  - 将所有修改加入暂存区 git add .



提交暂存区到本地仓库

- 作用：提交暂存区内容到本地仓库的当前分支
- 命令形式： git commit -m "注释内容"



查看提交日志

上面已经配置过了别名，后续使用 `git-log` 即可

- 作用：查看提交记录
- 命令形式：git log [option]
  - options
    - --all 显示所有分支
    - --pretty=oneline 将提交信息显示为一行
    - --abbrev-commit 使得输出的`commitld`更简短
    - --graph 以图的形式显示



版本回退

- 作用：版本切换
- 命令形式：git reset --hard commitID
  - commitID 可以使用 `git-log` 或 `git log` 指令查看
- 如何查看已经删除的记录？
  - git reflog
  - 这个指令可以看到已经删除的提交记录



删除文件

`git rm xxx` 
删除你不需要的文件 `xxx是你的文件名且带后缀`



### 添加文件至忽略列表

一般我们总会有些文件无需纳入Git 的管理，也不希望它们总出现在未跟踪文件列表。 通常都是些自动 生成的文件，比如日志文件，或者编译过程中创建的临时文件等。 在这种情况下，我们可以在工作目录 中创建一个名为 .gitignore 的文件（文件名称固定），列出要忽略的文件模式。下面是一个示例：

```shell
# 每一行的忽略规则语法
# 空格不匹配任意文件，可作为分隔符，可用反斜杠转义
# 开头的文件标识注释，可以使用反斜杠进行转义
#  ! 开头的模式标识否定，该文件将会再次被包含，如果排除了该文件的父级目录，则使用 ! 也不会再次被包含。可以使用反斜杠进行转义
# / 结束的模式只匹配文件夹以及在该文件夹路径下的内容，但是不匹配该文件
# / 开始的模式匹配项目跟目录
# 如果一个模式不包含斜杠，则它匹配相对于当前 .gitignore 文件路径的内容，如果该模式不在 .gitignore 文件中，则相对于项目根目录
# ** 匹配多级目录，可在开始，中间，结束
# ? 通用匹配单个字符
# * 通用匹配零个或多个字符
# [] 通用匹配单个字符列表
 
# 常用匹配示例
bin/: 忽略当前路径下的bin文件夹，该文件夹下的所有内容都会被忽略，不忽略 bin 文件
/bin: 忽略根目录下的bin文件
/*.c: 忽略 cat.c，不忽略 build/cat.c
debug/*.obj: 忽略 debug/io.obj，不忽略 debug/common/io.obj 和 tools/debug/io.obj
**/foo: 忽略/foo, a/foo, a/b/foo等
a/**/b: 忽略a/b, a/x/b, a/x/y/b等
!/bin/run.sh: 不忽略 bin 目录下的 run.sh 文件
*.log: 忽略所有 .log 文件
config.php: 忽略当前路径的 config.php 文件

# 优先级如下
# 从命令行中读取可用的忽略规则
# 当前目录定义的规则
# 父级目录定义的规则，依次递推
# $GIT_DIR/info/exclude 文件中定义的规则
# core.excludesfile中定义的全局规则

```

## 分支

HEAD的指向表明了当前的分支

创建分支时会在在当前分支的基础上进行操作，所有文件全部一致

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708105043156.png" alt="image-20220708105043156" style="zoom: 67%;" />

- 查看本地分支：git branch
- 创建本地分支：git branch 分支名
- 切换分支：git checkout / switch 分支名 （**切换分之前先提交本地的修改**）
- 创建并切换到新的分支中去：git checkout -b 分支名
- 合并分支：git merge 分支名
- 删除分支：
  - git branch -d 分支名   删除分支时，需要做各种检查
  - git branch -D 分支名  不做任何检查，强制删除



当两个分支修改了同一个文件的同一行，再进行合并时，会产生冲突，这时候就需要手动解决冲突

1. 处理文件中冲突的地方 （找到冲突的地方，修改成自己想要的）
2. 将解决完冲突的文件加入暂存区 (add)
3. 提交的仓库 (commit)

冲突部分的内容处理如下

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708110401890.png" alt="image-20220708110401890" style="zoom: 33%;" />

也可以不使用 vi 编辑器 ，直接对文件进行修改 在进行 add 和 commit



## Git 远程仓库

- 生成 SSH 公钥

  - ssh-keygen -t rsa
  - 不断回车
    - 如果公钥已经存在，则自动覆盖

- 获取公钥

  cat ~/.ssh/id_rsa.pub
  
- 在你使用的平台添加 SSH 公钥

### 操作远程仓库

#### 添加远程仓库

**此操作是先初始化本地库，然后与已经创建的远程库进行连接**

- 命令：git remote add <远端名称> <仓库路径>
  - 远端名称，默认是 orgin ，取决于远端服务器设置
  - 仓库路径，从远端服务器获取此 URL
  - 例如：git remote add origin git@github.com:renzhen5/gitTest.git<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708234714183.png" alt="image-20220708234714183" style="zoom: 67%;" />
  - 远程仓库的地址选择SSH

#### 查看远程仓库

`git remote`

#### 推送到远程仓库

命令：git push [-f] [--set-upstream] [远端名称 [本地分支名] : [远端分支名]]

- 如果远程分支名与本地分支名称相同，则可只写本地分支
  - `git push origin master`  ( 此处的 origin 在`添加仓库`里面已经说明是默认名称 )
  - `-f` 表示强制覆盖
  - `--set-upstream` 推送到远端的同时并且建立起和远端分支的关联关系（首次操作时使用即可）
    - `git push --set-upstream origin master`
  - 如果当前分支已经和远端分支关联，则可以省略分支名和远端名
    - `git push ` 将 master 分支推送到已关联的远端分支
  
- 更换 git 远程仓库地址

  - 查看当前 remote  `git remote -v`
  - 修改 remote  `git remote set-url origin + git地址`

- 重置 git 远程仓库地址

  - 删除当前地址 `git remote rm origin`

  - 新增地址 `git remote add origin + git地址`

    

#### 本地分支与远程分支的关联关系

查看关联关系我们可以使用 `git branch -vv` 命令<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708144558554.png" style="zoom:80%;" />
远程的 `origin 分支 对本地仓库的 master 分支`

#### 从远程仓库克隆

命令：`git clone` <仓库路径> [本地目录]

- 本地目录可以省略，会自动生成一个目录，与远程仓库的名字相同

#### 从远程仓中抓取和拉取

- 抓取命令：`git fetch [remota name] [branch name]` (一般使用 git fetch 即可 remota name 都是orgin ，branch name 都是 master)
  - 抓取指令就是将仓库里的更新都抓取到本地，不会进行合并
  - 如果不指定远端名称和分支名，则抓取所有分支
- 拉取命令：`git pull [remote name] [branch name]` (同上)
  - 拉去命令就是将远端仓库的修改拉去到本地并自动进行合并，等同于 `fetch + merge`

#### 解决合并冲突

A用户在本地修改代码后优先推送到远程仓库，此时B用户在本地修订代码，提交到本地仓库后，也需要 推送到远程仓库，此时B用户晚于A用户，**故需要先拉取远程仓库的提交，经过合并后才能推送到远端分 支,如下图所示。**

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220708233626839.png" alt="image-20220708233626839" style="zoom:50%;" />

**远程分支也是分支，所以合并时冲突的解决方式也和解决本地分支冲突相同相同**

## Java常用的.gitignore文件

```shell
# Compiled class file
*.class

# Eclipse
.project
.classpath
.settings/

# Intellij
*.ipr
*.iml
*.iws
.idea/

# Maven
target/

# Gradle
build
.gradle

# Log file
*.log
log/

# out
**/out/

# Mac
.DS_Store

# others
*.jar
*.war
*.zip
*.tar
*.tar.gz
*.pid
*.orig
temp/

```





