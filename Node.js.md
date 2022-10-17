Node.js 是一个基于 Chrome V8 引擎的 Javascript 运行环境<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221007210751492.png" alt="image-20221007210751492" style="zoom:50%;" />

注意：

1. 浏览器是 JS 的前端运行环境
2. node.js 是 JS 的后端运行环境
3. node.js 无法调用 DOM 和 BOM等浏览器内置API

Node.js  作为一个 Javascript 的运行环境，仅仅提供了基础的功能和 API 。但是基于其提供的基础功能，很多工具和框架非常多

1. 基于 Express 框架（http://www.expressjs.com.cn/）可以快速构建 Web 应用
2. 基于 Electron 框架（http://www.electronjs.org/）可以构建跨平台的桌面应用
3. 基于 restify 框架（http://restify.com/）可以快速构建API接口项目



node.js 学习路径：
js 基础语法 + node.js 内置 API 模块 + 第三方模块

# fs文件模块系统

fs 模块是 node.js 官方提供的、用来操作文件的模块。他提供了一系列的方法和属性，用来满足用户对文件的操作需求。

例如：

1. fs.readFile() 用来读取指定文件中的内容

   `fs.readFile(path[, options], callback)`

   - 参数一：必选参数，字符串，表示文件的路径
   - 参数二：可选参数，表示以什么编码格式来读取文件
   - 参数三：必选参数，文件读取完成后，通过回调函数拿到读取的结果
   - `fs.readFile(path,"utf-8", function(err,dataStr){})`  
   - 读取失败是 err 为错误对象，datastr 为undefined ，成功 err 的值 为 null

2. fs.writeFile() 用来向指定的文件中写入内容

   `fs.writeFile(file, data[, options], callback)`

   <font color="#dd0000">只能创建文件，不能用来创建路径</font>
   
   <font color="#dd0000">新写入的文件会覆盖掉之前的旧内容</font>
   
   - 参数一：表示文件的存放路径
   - 参数二：表示要写入的内容
   - 参数三：表示以什么格式写入文件内容，默认是 utf-8
   - 参数四：文件写入完成后的回调函数
   - `fs.writeFile(file, data, function(err){})` 文件写入成功 err 对象为 null

## 路径问题

如果在 JS 中，使用 fs 模块来操作文件，需要使用下面的方式导入他：`const fs = require('fs')`  

在使用fs模块操作文件时，如果提供的操作路径是以 .\ 或者 ..\ 开头的相对路径是，很容易出现路径动态拼接错误的问题。 原因：在代码运行的时候，会议执行 node 命令时所处的目录，动态拼接出被操作文件的完整路径

解决办法：

1. 使用全路径（不过比较繁琐
2. 使用 `__dirname` 解决 __dirname 代表文件所在的路径

# path路径模块

导入方法：`const path = require("path")`

path 模块是 node.js 官方提供的、用来处理路径的模块。提供了一系列方法和属性，用来满足用户对路径的处理需求

例如：

- path.join() 方法，用来将多个路径片段拼接成一个完整的路径字符串

  `path.join([...paths])` 任意多个路径片段 

  以后涉及到路径拼接的操作，都要使用此方法进行处理。不要直接使用 + 来进行字符串的拼接
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/code.png" alt="code" style="zoom:50%;" />

- path.basename() 用来从路径字符串中，将文件名解析出来

  ```js
  const path = require("path");
  const str = '/a/b/c/index.html';
  console.log(path.basename(str)); // 结果为 index.html
  // 去除文件扩展名
  console.log(path.basename(str,".html")); //结果为 index
  ```

  可以获取路径中的最后一部分，经常通过这个方法获取路径中的文件名

- `path.extname(path)` 获取路径中的扩展名部分

# HTTP模块

http模块式 node.js 官方提供的、用来创建 web 服务器的模块。通过 http 模块提供的 http.createServer() 方法，就能方便的把一台普通的电脑，变成web服务器，从而对外提供 web 资源服务

导入方式： `const http = require("http")`

## 创建web服务器的基本步骤

1. 导入http模块

    `const http = require("http")`

2. 创建 web 服务器实例

   `const server = http.createServer();`

3. 为服务器绑定 request 请求，监听客户端的请求

   ```js
   // 使用服务器实例的 .on() 方法，为服务器绑定一个 request 事件
   server.on("request",(req,res)=>{
       // 只要有客户端来请求我们自己的服务器，就会触发 request 事件,
       // 从而调用这个事件处理函数
       ...
   })
   ```

4. 启动服务器

   ```js
   // 调用 server.listen(端口号，回调函数) 方法，即可启动 web 服务器
   server.listen(80, ()=>{
       ...
   })
   ```

只要服务器接收到了客户端的请求，就会通过 server.on() 为服务器绑定的 request 事件处理函数。

创建基本 web 服务器代码如下

```js
const http = require('http')

// 创建 web 服务器实例
const server = http.createServer()

// 为服务器实例绑定 request 事件，监听客户端的请求
server.on('request', function (req, res) {
    // req 是请求对象，它包含了与客户端相关的数据和属性
    // res 是响应对象，它包含了与服务器相关的数据和属性
    const url = req.url
    const method = req.method
    const str = `Your request url is ${url}, and request method is ${method}`
    console.log(str)

    // 设置 Content-Type 响应头，解决中文乱码的问题
    res.setHeader('Content-Type', 'text/html; charset=utf-8')
    // 向客户端响应内容
    res.end(str)
})

server.listen(8080, function () {
    console.log('server running at http://127.0.0.1:8080')
})
```

实现简陋的路由效果，即根据不同的 URL 相应不同的数据回去

```js
const http = require('http')
const server = http.createServer()

server.on('request', (req, res) => {
    const url = req.url
    // 设置默认的响应内容为 404 Not found
    let content = '<h1>404 Not found!</h1>'
    // 判断用户请求的是否为 / 或 /index.html 首页
    // 判断用户请求的是否为 /about.html 关于页面
    if (url === '/' || url === '/index.html') {
        content = '<h1>首页</h1>'
    } else if (url === '/about.html') {
        content = '<h1>关于页面</h1>'
    }

    res.setHeader('Content-Type', 'text/html; charset=utf-8')
    res.end(content)
})

server.listen(80, () => {
    console.log('server running at http://127.0.0.1')
})
```

# 模块化

编程领域的模块化，就是遵守固定的规则，把一个大文件拆成独立并互相依赖的多个小模块

好处：

- 提高了代码的复用性
- 提高了代码的可维护性
- 可以实现按需加载

## 模块化规范

Node.js 中根据模块来源的不同，将模块分为了三大类，分别是：

- 内置模块（由 Node.js 官方提供，如 fs 、path 、http等

- 自定义模块（用户创建的每个 js 文件，都是自定义模块

- 第三方模块（由第三方开发出来的模块，并非官方提供的内置模块，也不是用户创建的自定义模块，需要提前下载

- 使用 require() 方法加载其他模块时，会执行被加载模块中的代码

- ```js
  // 1.加载内置的 fs 模块
  const fs = require("fs");
  // 2.加载用户的自定义模块  路径  后缀名可以不添加
  const custom = require("./custrom.js");
  // 3.加载第三方模块
  const moment = require("moment")
  ```

## 模块作用域

什么是模块作用域：和函数作用域类似，在自定义模块中定义的变量、方法等成员，只能在当前模块内被访问，这种模块级别的访问限制，叫做模块作用域

好处：放置了全局变量污染的问题

### 向外共享模块作用域中的成员

#### module对象

每个 .js 自定义模块中都有一个 module 对象，他里面存储了和当前模块有关的信息，打印如下
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221008210251126.png" alt="image-20221008210251126" style="zoom:50%;" />

module.exports 对象

在自定义模块时，可以使用 module.exports 对象，将模块内的成员共享出去，供外界使用。
使用 require() 方法导入自定义模块是，得到的就是 module.exports 所指向的对象

使用 require() 方法导入模块是，导入的结果，<font color="#dd0000">永远以</font> module.exports 指向的对象为准

### Node.js 中的模块化规范

Node.js 遵循了 CommonJS 模块化规范，CommonJS规定了模块的特性和各模块之间如何相互依赖

CommonJS 规定：

1. 每个模块内部，module 变量代表当前模块
2. module 变量是一个对象，他的 exports 属性（即 module.exports）是对外的接口。
3. 加载某个模块，其实是加载该模块的 module.exports 属性。require() 方法用于加载模块

## npm与包

- 什么是包：Node.js  中的第三方模块又叫做包

- 包的来源：包是由第三方个人或团队开发出来的，免费供所有人使用。

- 为什么需要包：Node.js 内置模块仅提供了一些底层的API，导致在基于内置模块进行项目开发时，效率很低。

  包是基于内置模块封装出来的，提供了更高级、更方便的 API ，极大的提高了开发效率

- 从哪里下载包：www.npmjs.com 它是全球最大的包共享平台

在项目中安装包的命令：`npm install 包的完整名称` 可以简写为 `npm i 完整的包名称` 安装多个包使用空格隔开
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221008224004531.png" alt="image-20221008224004531" style="zoom:50%;" />

如何安装指定版本的包 ：在使用命令安装包时，会自动安装最新的包，安装指定版本的包的语法 
`npm i 安装的包名@版本号`

### 包的语义化版本规范

报的版本号是以 “点分十进制” 形式进行定义的，总共有三位数组，例如 2.24.0

1. 第一位数字：大版本
2. 第二位数字：功能版本
3. 第三位数字：BUG 修复版本

版本号提升的规则：只要前面的版本号增长了，则后面的版本号归零

### 包管理配置文件

npm规定，在项目根目录中，必须提供一个叫做 packag.json 的包管理配置文件。用来记录与项目有关的一些配置信息。例如：

- 项目的名称、版本号、描述等
- 项目中用到了哪些包
- 哪些包之在开发期间会用到
- 哪些包在开发和部署时都需要用到

#### 多人协作的问题

共享是剔除 node_modules ，那么如何记录项目中安装了哪些包呢 ，在项目根目录中，创建一个叫做 package.json（一般在安装包后会自动生成或者使用 `npm init -y `生成） 的配置文件，即可用来记录项目中安装了那些包。从而方便提出 node_modules 目录后，在团队成员之间共享项目的源代码。

<font color="#d00"> 注意：</font>在项目开发中，一定要把 node_modules 文件夹，添加到 .gitignore 忽略文件中。

1. 上面的命令只能在英文的目录下运行成功，所以项目文件夹的名称一定要使用英文命名，不使用中文，不出现空格
2. 运行 `npm install `命令安装包的时候，npm 包管理工具会<font color="#d00">自动</font>把报的名称和版本号记录到 package.json 中

### dependencies 节点

package.json 文件中，有一个 dependencies 节点，专门用来记录使用 npm install 命令安装了哪些包

### devDependencies节点

如果某些包只在项目开发阶段会用到，在项目上线之后不会用到，则建议把这些包记录到 devDependencies 节点中，与之对应的，如果某些包在开发和项目上线之后都需要用到，则建议把这些包记录到 dependencies 节点中

使用如下命令将包记录到 devDependencies 节点中：

- npm i 包名 -D
- npm install 包名 --save-dev  上面的是简写形式，两者相互等价

### 如何一次性安装所有的包

当我们拿到了一个剔除了 node_modules 的项目后，需要先把所有的包下载到项目中，才能将项目运行起来。

运行 `npm install` 或者 `npm i`一次性安装所有的依赖包

执行 npm install 命令时，npm 包管理工具会读取 package.json 中的 dependencies 节点
读取到记录的所有依赖包名称和版本号之后，npm 包管理工具会把这些包一次性下载到项目中

### 卸载包

运行 `npm uninstall 报名`命令，来指定卸载的包。

### 解决下包慢的问题

切换 npm 的下包镜像源
将 下包 的镜像源切换为淘宝镜像源 `npm config set registry=https://registry.npm.taobao.org/`

查看当前的下包镜像源 `npm config get registry`

### 包的分类

使用npm包管理工具下载的包，共分为两大类，分别是：

- 项目包

  安装到项目的 node-modules 目录中的包，都是项目包，又分为两类，分别是：

  - 开发依赖包（被记录到 devDependencies 节点中的包，之在开发期间会用到）
  - 核心依赖包（被记录到 dependencies 节点中的包，在开发期间和项目上线之后都会用到）

- 全局包

  在执行 npm install  命令时，吐过提供了 -g 参数，则会把包安装为全局包。

  全举报会被安装到 `C:\Users\用户目录\AppData\Roaming\npm\node_modules`目录下。

  ```powershell
  npm i 包名 -g					# 全局安装指定的包
  npm uninstall 包名 -g			# 卸载全局安装的包
  ```


### 规范的包结构

- 必须以单独的目录存在
- 包的顶级目录下必须包含 package.json 这个包管理配置文件
- package.json 中必须包含 name, version, main 这三个属性，分别代表包的名字、版本号、包的入口

### 开发属于自己的包

1. 新建一个 文件夹 ，作为包的根目录

2. 根目录中，新建三个文件：

   - package.json 		（包管理配置文件）
   - index.js				  （包的入口文件）
   - REAMDE.md 		 （包的说明文档）

3. 初始化 package.json

   ```json
   {
       "name":"名称", 					// 这个包的名字  唯一 如果上传平台不能重复
       "version":"1.0.0",	 	 		// 版本号
       "main":"index.js",		 		// 入口
       "description":"包功能的描述",
       "keywords": [各个属性],			// 搜索的关键字
       "license":"ISC",				// 开源协议 默认写这个即可
   }
   ```

4. 将不同的功能进行模块化拆分

   1. 将不同的功能拆分到不同的 js 文件中去
   2. 在入口文件中导入所有的模块，得到需要向外共享的方法
   3. 在入口文件中 使用 module.exports 把对应的方法共享出去

## 模块加载机制

模块在第一次加载后会被缓存，这也意味着多次调用 require() 不会导致模块的代码被执行多次。

不管是什么模块，他们都会优先从缓存中加载，从而提高模块的加载效率

内置模块的加载机制：内置模块的加载优先级最高

使用 require() 加载自定义模块时，必须指定 ./ 或 ../ 开头的路径标识符，在加载自定义模块时，如果没有指定路径标识符， node 会把他当作内置模块或者第三方模块进行加载

### 自定义模块的加载机制

在导入自定义模块时省略了文件的拓展名，node.js 会按顺序分别尝试加载以下的文件：

1. 按照确切的文件名进行加载
2. 补全 .js 扩展名进行加载
3. 补全 .json 扩展名进行加载
4. 补全 .node 扩展名进行加载
5. 加载失败，终端报错

### 第三方模块的加载机制

如果传递给 require() 的模块标识符不是一个内置模块，同时也没有路径标识符，则 node.js 会从当前模块的父目录开始，尝试从 /node_modules 文件中加载第三方模块。如果没有找到对应的第三方模块，则移动到再上一层父目录中，进行加载，知道文件系统的根目录。
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010142459350.png" alt="image-20221010142459350" style="zoom:50%;" />

### 目录作为模块

当把目录作为模块标识符，传递给 require()  进行加载的时候，有三种加载方式：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010142611886.png" alt="image-20221010142611886" style="zoom:50%;" />

# Express

[官网	](http://www.expressjs.com.cn/)

Express：是基于 Node.js 平台，快速、开放、极简的web开发框架

通俗的理解：Express 的作用和 node.js 内置的 http 模块类似，是专门用来创建 web 服务器的。

Express的本质就是 npm 上的第三方包，提供了快速创建 web 服务器的便捷方法
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010143457070.png" style="zoom:33%;" />

express 可以做什么，对于前端程序员来说，最常见的两种服务器是：

- web 网站服务器：专门对外提供 web 网页资源的服务器
- API 接口服务器：专门对外提供 API 接口的服务器

使用 express ，我们可以更方便、快速的创建 web 网站的服务器或 API 接口的服务器

安装：在项目所处的目录中，运行终端命令即可， `npm i express`

```js
// 创建基本的服务器
// 1.导入 express
const express = require('express');
// 2.创建 web 服务器
const app = express();

// 3.调用 app.listen(端口号，启动成功后的回调函数) ,启动服务器
app.listen(80, () => {
    console.log("express server running at http://127.0.0.1");
})

// 参数1：客户端请求的 URL 地址
// 参数2：请求对应的处理函数
//        req：请求对象（包含了与请求相关的属性与方法
//        res：响应对象（包含了与响应相关的属性与方法
app.get("请求URL", (req, res) => { /*处理函数*/})
// 参数1：客户端请求的 URL 地址
// 参数2：请求对应的处理函数
//        req：请求对象（包含了与请求相关的属性与方法
//        res：响应对象（包含了与响应相关的属性与方法
app.post("请求URL", (req, res) => { /*处理函数*/})

//还有 put delete 请求 同上
```

## 把内容相应给客户端

<font color="#d00">res.send()</font> 在一个函数中只能调用一次

通过 res.send() 方法，可以把处理好的内容，发送给客户端：

```js
app.get("/user",(req,res)=>{
    //向客户端发送 JSON 对象
    res.send({name:"zs",age:20})
})
app.post("/user",(req,res)=>{
    // 向客户端发送文本请求
    res.send("请求成功")
})
```

## 获取URL中携带的查询参数

通过 req.query() 对象，可以访问到客户端通过查询字符串的形式，发送到服务器的参数。

```js
app.get("/", (req, res) => {
    // req.query 默认是一个空对象
    // 客户端使用 ?name=zs&age=20 这种查询字符串形式，发送到服务器的参数
    // 可以通过 req.query 对象访问到，例如：
    // req.query.name  req.query.age
    // 当客户端传来的参数后 res.query 是一个 json 对象
    console.log(req.query);
})
```

## 获取 URL 中的动态参数

通过 req.params 对象，可以访问到 URL 中，通过 : 匹配到的动态参数

```js
//获取携带的动态参数 :id 就代表一个不确定的值 
// 冒号后面的名称是自己任意去取的，可以有多个动态参数
app.get("/user/:id",(req,res)=>{
    // req.params 默认是一个空对象
    // 里面存放着通过 ： 动态匹配到的参数值
    // 有值以后同样是一个 JSON 对象
    console.log(req.params);
})
```

## 托管静态资源

### express.static()

express 提供了一个非常好用的函数，叫做 express.static() ，通过它，我们可以非常方便的创建一个静态资源服务器。例如，通过如下代码就可以将 public 目录下的图片，CSS 文件、JS 文件对外开放访问了：

```js
// 托管静态资源
app.use(express.static("目录"))
// 就可以直接访问 目录 中的所有文件了
// localhost:端口/直接访问该路径中的文件
// express 在指定的静态目录中查找文件，并对外提供资源的访问路径，因此存放静态文件的目录名不会出现
//当需要使用固定前缀时 app.use("前缀",express.static("目录"))
```

托管多个静态资源目录，多次调用 express.static() 方法即可，访问静态资源文件时，`express.static` 中间件函数会根据目录的添加顺序查找所需的文件。

## express路由

在 express 中，路由是指 客户端的请求与服务器处理函数之间的映射关系

express 中的路由分三部分组成，分别是 请求的类型、请求的URL地址、处理函数，格式如下：
`app.METHOD(PATH,HANDLER)`

路由的例子如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010194405351.png" alt="image-20221010194405351" style="zoom:50%;" />

过程： 当每一个请求到达服务器之后，需要先经过路由的匹配，只有匹配成功之后，才会调用对应的处理函数。
在匹配时，会按照路由的顺序进行匹配，如果 <font color="#d00" > 请求类型</font>和<font color="#d00" > 请求的URL</font>同时匹配成功，则Express会将这次请求交给对应的function函数进行处理
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010194916620.png" alt="image-20221010194916620" style="zoom:50%;" />

### 模块化路由

为了方便对路由进行模块化的管理，Express 不建议将路由直接挂载到 app 上，而是推荐将路由抽离为单独的模块，抽离为单独模块的步骤如下：

1. 创建路由模块对应的 .js 文件
2. 调用 express.Router() 函数创建路由对象
3. 想路由对象上挂载具体的路由
4. 使用 module.exports 向外共享路由对象
5. 使用 app.use() 函数注册路由模块


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010202958490.png" alt="image-20221010202958490" style="zoom:50%;" />

注册路由模块
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010203643419.png" alt="image-20221010203643419" style="zoom:50%;" />

app.use() 就是用来注册中间件的

### 为路由模块添加前缀

类似于托管静态资源时，为静态资源统一挂载访问前缀一样，路由模块添加前缀的方式也非常简单：

`app.use("/api",userRouter)`

## express 中间件

中间件：特指业务流程的中间处理环节

express中间件的调用流程：当一个请求到达 express 的服务器后，可以连续调用多个中间件，从而对这次请求进行预处理

express 的中间件，本质上就是一个 function 处理函数，express 中间件的格式如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010205632939.png" alt="image-20221010205632939" style="zoom:50%;" />

next() 函数的作用：是实现多个中间件连续调用的关键，他表示把流传关系转交给下一个中间件或路由
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010205820713.png" alt="image-20221010205820713" style="zoom:50%;" />

### 定义中间件函数

可以通过如下的方式，定义一个最简单的中间件函数
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010210221529.png" alt="image-20221010210221529" style="zoom: 50%;" />

**全局生效的中间件：**

全局生效的中间件：客户端发送的<font color="#d00"> 任何请求</font>，到达服务器之后，<font color="#d00"> 都会触发的中间件</font>，叫做全局生效的中间件。
通过调用 `app.use(mw)` ，即可定义一个全局生效的中间件，示例代码如下

```js
// 常量 mw 所指向的就是一个中间件函数
const mw = function(req,res,next){
    console.log("这是一个最简单的中间件函数");
    next();
}
// 全局生效的中间件 会按照加载的顺序执行
app.use(mw);
```

```js
// 全局生效的中间件的 简化形式
app.use(function(req,res,next){
    console.log("这是一个最简单的中间件函数");
    next();
})
```

**局部生效的中间件：**

不使用 app.use()  定义的中间件，叫做局部生效的中间件，示例代码如下
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011103412548.png" alt="image-20221011103412548" style="zoom: 50%;" />
**定义多个局部中间件**
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011104046275.png" alt="image-20221011104046275" style="zoom: 50%;" />

两者完全等价

### 中间件的作用

多个中间件之间，<font color="#d00">共享同一份req和res</font>，基于这样的特性，我们可以在上游的中间件中，统一为req或res对象添加自定义的属性或方法，供下游的中间件或路由进行使用

### 中间件的注意事项

1. 一定要在路由之前注册中间件
2.  客户端发送过来的请求，可以连续调用多个中间件进行处理
3. 执行完中间件的业务代码之后，不要忘记调用 next() 函数
4. 为了防止代码逻辑混乱，调用 next() 函数之后不要再写额外的代码
5. 连续调用多个中间件时，多个中间件之间，共享 req 和 res 对象

### 中间件的分类

为了大家理解和记忆中间件的使用， express 官方把常见的中间件用法，分成了五大类

1. 应用级别的中间件

   通过 app.use() 或 app.get() 或 app.post() ,绑定到 app 实例上的中间件，叫做应用级别的中间件

2. 路由级别的中间件

   绑定到 express.Router() 实例上的中间件，叫做路由级别的中间件，它的用法和应用级别中间件没有任何区别。只不过，应用级别中间件是绑定到 app 实例上，路由级别中间件绑定到 router 实例上

3. 错误级别的中间件

   作用：专门用来捕获整个项目中发生的异常错误，从而防止项目异常崩溃的问题。

   <font color="#d00">格式</font>：错误级别中间件的 function 处理函数中，必须有4个形参，分别是 （err, req, res ,next）
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011111327878.png" alt="image-20221011111327878" style="zoom:50%;" />
   应该放到所有路由之后否则会无法执行

4. express 内置的中间件
   内置了 3 个常用的内置中间件，极大的提高了express项目的开发效率和体验：

   - express.statis 快速托管静态资源的内置中间件：例如HTML文件，图片、CSS 样式等（没有兼容性

   - express.json 解析 JSON  格式的请求体数据（仅在 4.16.0+ 版本中可用

   - express.urlencoded 解析 URL-encoded 格式的请求体数据 使用post提交数据时使用（仅在 4.16.0+ 版本中可用
     <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011115045253.png" alt="image-20221011115045253" style="zoom:50%;" />

   - 如果没有配置后两个中间件，res.body 为 undefined

     

5. 第三方的中间件
   在项目中，大家可以按需下载并配置第三方中间件

   1. 运行安装目录安装中间件
   2. 使用 require 导入中间件
   3. 调用 app.use() 注册并使用中间件

## 使用 express 写接口



```js
// 此处写接口 此文件叫做 apiRouter
const express = require('express');
const router = express.Router();

// 挂载对应的路由
router.get("/get", (req, res) => {
    // 通过 req.query 获取客户端通过查询字符串，发送到服务器的数据
    const query = req.query
    // 调用 res.send() 方法，向客户端响应处理的结果
    res.send({
        status: 0, // 0 表示处理成功，1 表示处理失败
        msg: 'GET 请求成功！', // 状态的描述
        data: query, // 需要响应给客户端的数据
    })
})

module.exports=router;
```

```js
// 此处调用
const express = require('express');
const app = express();

// // 配置解析表单数据的中间件
// app.use(express.urlencoded({ extended: false }))

// 导入路由模块
const router = require('./apiRouter')
// 把路由模块，注册到 app 上
app.use('/api', router)

// 启动服务器
app.listen(80, () => {
  console.log('express server running at http://127.0.0.1')
})
```

## 接口的跨域问题

上述代码不支持跨域请求，解决接口跨域问题的方案有两种：

1. CORS（主流的解决方案，推荐使用
2. JSONP （有缺陷的解决方案：只支持 GET 请求

使用 CORS 跨域资源共享

cros 是 express 的一个第三方中间件，通过安装和配置 cors 中间件，可以很方便的解决跨域问题。

使用步骤：

1. 运行 `npm install cors` 安装中间件
2. 使用 `const cors = require("cors")` 导入中间件
3. 在路由之前调用 `app.use(cors())` 配置中间件


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011182616623.png" alt="image-20221011182616623" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011182731497.png" alt="image-20221011182731497" style="zoom:50%;" />

CORS响应头部 - Access-Control-Allow-Origin
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011183401053.png" alt="image-20221011183401053" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011183415853.png" style="zoom:50%;" />

CORS响应头部 - Access-Control-Allow-Headers 
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011183609249.png" alt="image-20221011183609249" style="zoom:50%;" />

CORS响应头部 - Access-Control-Allow-Headers 
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011183728564.png" alt="image-20221011183728564" style="zoom:50%;" />

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011184433443.png" alt="image-20221011184433443" style="zoom:50%;" />
简单请求和预检请求的区别：

简单请求的特点：客户端与服务器之间之会发生一次请求（简单请求 GET POST

预检请求的特点：客户端与服务器之间会发生两次请求，OPTION 预检请求成功之后，才会发起真正的请求

# 项目中操作 MySQL

1. 安装操作 MySQL 数据库的第三方模块 （mysql）
2. 通过 mysql 模块连接到 MySQL 数据库
3. 通过 mysql 模块执行 SQL 语句
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011205205172.png" alt="image-20221011205205172" style="zoom: 50%;" />

安装 mysql 模块 `npm install mysql`

```js
// 1.导入模块
const e = require('express');
const mysql = require('mysql');
// 2.建立于 MySQL 数据库的连接
const db = mysql.createPool({
    host: "127.0.0.1",       // 数据库的 ip 地址
    user: "root",            // 登录数据库的张豪
    password: "*****",      // 登录数据库的密码
    database: "*****"   // 指定要操作哪个数据库
})
```

查询数据
`db.query("查询语句", (err, res) => {})`

插入数据
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011214142830.png" alt="image-20221011214142830" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011215026055.png" alt="image-20221011215026055" style="zoom:50%;" />

更新数据
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221011215951409.png" alt="image-20221011215951409" style="zoom: 67%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221012202258874.png" alt="image-20221012202258874" style="zoom: 67%;" />
删除数据
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221012204825415.png" alt="image-20221012204825415" style="zoom:50%;" />
使用 delete 语句，会真正的把真正的数据从表中删除掉，为了保险起见，设置status的状态字段，来执行 update语句

# Web 开发模式

目前主流的 web 开发模式有两种，分别是：

1. 基于服务端渲染的传统 Web 开发模式
2. 基于前后端分离的新型 Web 开发模式

## 服务端渲染的传统 Web 开发模式

概念：服务器发送给客户端的 HTML 页面，是在服务器通过字符串的拼接，动态生成的，因此客户端不需要使用 Ajax 这样的计数额外请求页面的数据，示例：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221012214655461.png" alt="image-20221012214655461" style="zoom: 67%;" />

优点：

- 前端耗时少。因为服务器端负责动态生成 HTML内容，浏览器只需要直接渲染页面即可，尤其是移动端，更省电
- 有利于SEO。因为服务器端相应的是完整的 HTML 页面内容，所以爬虫更容易获取信息，更有利于SEO

缺点：

- 占用服务器端资源。即服务器端完成 HTML 页面内容的拼接，如果请求较多，会对服务器造成一定的压力
- 不利于前后端分离，开发效率低。使用服务器渲染，则无法进行分工合作，与其实对于前端复杂度高的项目，不利于项目高效开发

## 前后端分离的开发模式

前后端分离的开发模式，依赖于 Ajax 技术的广泛应用。简而言之，前后端分离的 Web 开发模式，就是后端只负责提供 API 接口，前端使用 Ajax 调用接口的开发模式

优点：

- 开发体验好，前端专注于 UI 页面的开发，后端专注于 api 的开发，且前端有更多的选择性。
- 用户体验好，Ajax 技术的广泛应用，极大的提高了用户的体验，可以轻松实现页面的局部刷新。
- 减轻了服务器端的渲染压力。因为页面最终是在每个用户的浏览器中生成的

缺点：

- 不利于SEO，因为完整的HTML页面需要在客户端动态拼接完成，所以爬虫无法爬取页面的有效信息。（解决方案：利用 Vue、React 等前端框架的 SSR (server side render) 技术能够很好的解决 SEO 问题

## 如何选择 web 开发模式

<font color="#d00">不谈业务场景而盲目选择使用何种开发模式都是耍流氓</font>

- 比如企业级网站，主要功能是展示而没有复杂的交互，并且需要良好的 SEO，则这时我们就需要使用服务器端渲染
- 而类似后台管理项目，交互性比较强，不需要考虑 SEO，那么就可以使用前后端分离的开发模式

另外，具体使用何种开发模式并不是绝对的，为了同时兼顾首页的渲染速度和前后端分离的开发效率，一些网站采用了收评服务器端渲染 + 其他页面前后端分离的开发模式

# 身份认证

身份认证又称为”身份验证“，是指通过一定的手段，完成对用户身份的确认

- 日常生活中的身份认证随处可见，例如：高铁的验票乘车，手机的密码或指纹解锁，支付宝或微信的支付密码等

- 在 Web 开发中，也涉及到用户身份的认证，例如：各大网站的手机验证码登录、邮箱密码登录、二维码登录等。

## 为什么需要身份认证

身份认证的目的，是为了确认当前所生成为某种身份的用户，确实是所声称的用户.

### 不同开发模式向的身份认证

对于服务端渲染的前后端分离的这两种开发模式来说，分别有着不同的身份认证方案：

1. 服务端渲染推荐使用 Session 认证机制
2. 前后端分离推荐使用 JWT 认证机制

## Session 认证机制

### HTTP 协议的无状态性

了解 HTTP 协议的无状态性是进一步学习 Session 认证机制的必要前提

HTTP 协议的无状态性，指的是客户端的每次 HTTP 请求都是独立的，连续多个请求之间没有直接的关系，服务器不会主动保留每次 HTTP 请求的状态
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221012223024763.png" alt="image-20221012223024763" style="zoom:67%;" />

### 如何突破 HTTP 无状态的限制

对于超市来说，为了方便收银员在进行结算时给 VIP 用户打折，超市可以为每个 VIP 用户发放会员卡。
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221012223308328.png" alt="image-20221012223308328" style="zoom:50%;" />

现实生活中的会员卡身份认证方式，在 Web 开发中的专业术语叫做 Cookie

### 什么是 Cookie

Cookie 是存储在用户浏览器中的一段不超过 4kb 的字符串，它由一个名称（Name）、一个值（Value）和其他几个用于控制 Cookie 有效期、安全性、适用范围的可选属性组成

不同域名下的 Cookie <font color="#d00">各自独立</font>，每当客户端发起请求是，会自动把当前域名下的所有未过期的 Cookie 一起发送到服务器	

Cookie 的几大特性：

1. 自动发送
2. 域名独立
3. 过期时限
4. 4 kb 限制

### Cookie 在身份认证中的作用

客户端第一次请求服务器的时候，服务器通过响应头的形式，像客户端发送一个身份认证的 Cookie，客户端会自动将 Cookie 保存在浏览器中。

随后，当客户端浏览器每次请求服务器的时候，浏览器会自动将身份认证相关的 Cookie，通过请求头的形式发送给服务器，服务器即可验明客户端的身份

Cookie不具有安全性，由于Cookie是存储在浏览器中的，而且浏览器也提供了读写 Cookie 的API，因此 Cookie很容易被伪造，不具有安全性，因此不建议将重要的隐私数据，通过 Cookie 的形式发送到浏览器。

### 提高身份认证的安全性

客户端加服务器的双重认证，就是 Session 认证机制的精髓

### Session 的工作原理


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013100715739.png" alt="image-20221013100715739" style="zoom:50%;" />

## express中使用session认证

安装 express-session 中间件 `npm i express-session`
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013104541008.png" alt="image-20221013104541008" style="zoom:67%;" />

## JWT 认证机制

### 了解 session 认证的局限性

session 认证机制需要配合 Cookie 才能实现。由于 Cookie 默认不支持跨域访问，所以当涉及到前端跨域请求后端接口的时候，需要做很多额外的配置，才能实现跨域 session 认证

注意：

- 当前端请求后端接口不存在跨域问题的时候，推荐使用 session 身份认证机制
- 当前端需要跨域请求后端接口的时候，不推荐使用 session 身份认证机制，推荐使用 jwt 认证机制

### 什么是 JWT

JWT（英文全称：JSON Web Token）是目前最流行的跨域认证解决方案

### JWT 工作原理


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013105114091.png" alt="image-20221013105114091" style="zoom:50%;" />

总结：用户的信息通过 Token 字符串的形式，保存到客户端浏览器中。服务器通过还原 Token 字符串的形式来认证用户的身份。

### JWT 的组成部分

JWT 通常由三部分组成，分别是 Header（头部），payload（有效荷载）、signature（签名），三者之间使用英文的点号分割，格式：`Header.Payload.Signature`

- payload部分才是真正的用户信息，他是用户信息经过加密之后生成的字符串
- header 和 signature 是安全性相关的，只是为了保证 Token 的安全性
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013110000502.png" alt="image-20221013110000502" style="zoom:50%;" />

### JWT 使用方式

客户端收到服务器返回的 JWT 之后，通常会将他存储在 localStorage 或 sessionStorage 中。

此后，客户端每次与服务器通信，都要带上 JWT 的字符串，从而进行身份认证。推荐的做法是把 JWT 放在 HTTP 请求头的 Authorization 字段中，格式：`Authorization: Bearer <token>`	

## express中使用JWT

安装两个包 `npm i jsonwebtoken express-jwt`

其中：

1. jsonwebtoken 用于生成 JWT 字符串
2. express-jwt 用于将 jwt 字符串解析还原成 json 对象

使用 require 函数，分别导入两个相关的包

```js
const jwt = require("jsonwebtoken");
const expressJWT = require("express-jwt")
```

### 定义 secret 密钥

为了保证 jwt 字符串的安全性，防止 jwt 字符串在网络传输过程中被人破解，我们需要专门定义一个用于贾母和解密的 secret 密钥：

1. 当生成 jwt 字符串的时候，需要使用 secret 密钥对用户的信息进行加密，最终得到加密好的 劲舞团 字符串

2. 当把 jwt 字符串解析还原成 json 对象的时候，需要使用 secret 密钥进行解密

   `const secretKey = "自定义的字符串 越复杂越好"`

### 在登陆成功后生成 jwt 字符串

调用 jsonwebtoken 包提供的 sign() 方法，将用户的信息加密成 JWT 字符串，相应给客户端：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013115807945.png" alt="image-20221013115807945" style="zoom:67%;" />

千万不要把密码加密到 token 字符串中

### 将 jwt 字符串还原为 json 对象

客户端每次在访问那些有权限接口的时候，都需要主动通过请求头中的 authorization 字段，将 token 字符串发送到服务器进行身份认证

此时服务器可以通过 express-jwt 这个中间件，自动将客户端发送过来的 token 解析还原成 json 对象，如果解析成功说明此次请求合法
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013120515913.png" alt="image-20221013120515913" style="zoom:67%;" />
只要配置成功了 express-jwt 这个中间件，就可以把解析出来的用户信息，挂载到 req.user 属性

### 使用 req.user 获取用户信息

当 express-jwt 这个中间件配置成功之后，即可在那些有权限的接口中，使用 req.user  对象，来访问从jwt字符串中解析出来的用户信息了，示例代码如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013122746475.png" alt="image-20221013122746475" style="zoom:67%;" />

### 捕获解析 jwt 失败后产生的错误

当使用 express-jwt 解析 token 字符串时，如果客户端发送过来的 token 字符串不合法或过期，会产生一个解析失败的错误，影响项目的运行，我们可以通过express的错误中间件，捕获这个错误并进行相关的处理，示例代码如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221013123943389.png" alt="image-20221013123943389" style="zoom:67%;" />









# nodemon

 [官网](https://www.npmjs.com/package/nodemon/) 

在编写调试 node.js 项目的时候，如果修改了项目的代码，则需要频繁的手动 close 掉，然后从新启动。

使用 nodemon 工具，他能够监听项目文件的变动，当代码修改后，nodemon 会自动帮我们重启项目，极大方便了开发和调试

安装 `npm install -g nodemon`

平时启动 node 项目时，一般使用 node app.js 来启动项目，以后我们使用 `nodemon app.js` 来启动项目，这样当我们修改代码后，会自动实现重启项目的效果





















