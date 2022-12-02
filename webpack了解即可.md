# Webpack

实际的前端开发：

- 模块化（JS 的模块化、css 的模块化、资源的模块化
- 组件化（复用现有的 UI 结构、样式、行为
- 规范化（目录结构的划分、编码，接口，文档规范化、git分支管理
- 自动化（自动化构建、自动部署、自动化测试

目前主流的前端工程化解决方案：[webpack](https://www.webpackjs.com/)

主要功能：提供了有好的前端模块化开发支持，以及代码压缩混淆、压缩处理器端 JS 的兼容性、性能优化等强大的功能

## webpack基本使用

在项目中安装 webpack `npm i webpack@5.42.1 webpack-cli@4.7.2 -D`

配置 webpack

1. 在项目根目录中，创建名为 `webpack.config.js` 的 webpack 配置文件，并初始化如下的基本配置：

   ```js
   module.exports = {
       mode:"development"	
       // mode 用来指定构建模式，可选值又 development 和 production
       // 开发阶段选用development
       // 上线使用 production
       
       // development 打包时间短，不压缩  
       // production 打包时间长，压缩
   }
   ```

2. 在 package.json 的 script 节点下，新增 dev 脚本如下：

   ```js
   "script":{
   	"dev":"webpack"
       // dev名字任意 后面的必须为 webpack
       // 下面运行的 dev 要互相对应
       //可以通过 npm run 执行，例如npm run dev
   }
   ```

3. 在终端中运行 `npm run dev` 命令，启动 webpack 进行项目的打包构建

webpack.config.js 是 webpack 的配置文件，webpack 在真正开始打包构建之前，会先读取这个配置文件，从而基于给定的配置，对项目进行打包。

<font color="#d00">注意：</font>由于 webpack 是基于 node.js 开发出来的打包工具，因此在他的配置文件中，支持使用 node.js 相关的语法和模块进行 webpack 的个性化配置

### 默认约定

webpack 有如下的默认规定

- 默认的打包入口文件为 `src -> index.js`
- 默认的输出文件路径为 `dist -> main.js`

可在 webpack.config.js 中修改打包的默认约定

> 自定义打包的入口和出口：

在 webpack.config.js 配置文件中，通过 entry 节点指定打包的入口。通过 output 节点指定打包的出口。

```js
const path = require("path");

module.exports = {
    entry: path.join(__dirname,'./src/index.js'); //打包入口文件的路径
    output: {
    	path: path.join(__dirname,'dist');		  //输出文件的存放路径
		filename: 'boundle.js'				  	  //输出文件的名称
	}
}
```



## webpack插件

通过安装和配置第三方的插件，可以拓展 webpack 的能力，从而让 webpack 用起来更方便，最常用的 webpack 插件有如下两个：

1. webpack-dev-server
   - 类似于 node.js 阶段用到的 nodemon 工具
   - 每当修改了源代码，webpack 会自动进行项目的打包和构建
2. html-webpack-plugin
   - webpack 中的 html 插件 （类似于一个模板引擎插件）
   - 可以通过此插件自定制 index.html 页面的内容

### webpack-dev-server

webpack-dev-server 可以让 webpack 监听项目源代码的变化，从而进行自动打包构建。

- 安装命令 `npm i webpack-dev-server@3.11.2 -D`

  - 修改 `package.json -> script` 中的 dev 命令如下：

    ```js
    "script":{
        "dev":"webpack serve"
    }
    ```

  - 再次运行 `npm run dev` 命令，重新进行项目的打包

  - 在浏览器中访问 http://localhost:8080 地址，查看自动打包效果

- <font color="#d00">注意</font>：webpack-dev-server 会启动一个实时打包的 http 服务器

- 如果运行后报错 <font color="#d00">Unable to load '@webpack-cli/serve' command</font>再执行一个命令
  `npm install webpack-cli -D`

配置：

1. 修改 package.json -> script 中的 dev 命令如下：

   ```js
   "script": {
   	"dev": "webpack serve", // script 节点下的脚本，可以通过 npm run 执行
   }
   ```

2. 再次运行 `npm run dev` 命令，重新进行项目的打包

3. 在浏览器中访问 http://localhost:8080 地址，查看自动打包效果

<font color="#d00">注意</font> webpack-dev-server 会启动一个实时打包的 http 服务器

> 打包生成的文件去哪了？、

1. 不配置 webpack-dev-server 的情况下，webpack 打包生成的文件，会存放到实际的物理磁盘上
   - 严格遵守开发者在 webpack.config.js 中指定配置
   - 根据 output 节点指定路径进行存放
2. 配置了 webpack-dev-server 之后，打包生成的文件存放到了内存中
   - 不在根据 output 节点指定的路径，存放到实际的物理磁盘上
   - 提高了实时打包输出的性能，因为内存比物理磁盘速度快很多

> 生成到内存中的文件该如何访问

webpack-dev-server 生成到内存中的文件，默认放到了项目的根目录中，而且是虚拟的、不可见的。

- 可以直接用 / 表示项目根目录，后面跟上要访问的文件名称，即可访问内存中的文件
- 例如 /bundle.js 就表示要访问 webpack-dev-server 生成到内存中的 bundle.js 文件

### html-webpack-plugin

1. 安装插件：`npm i html-webpack-plugin -D`

2. 配置 html-webpack-plugin

   ```js
   // 导入 HTML 插件，得到一个构造函数
   const Htmlplugin = require("html-webpack-plugin");
   // 创建 HTML 插件的实例对象
   const htmlPlugin = new HtmlPlugin({
       // 指定源文件的存放路径
       templage:"./scr/index.html",
       // 指定生成的文件的存放路径
       filename:"./index/html"
   })
   ```

3. 通过 html 插件复制到项目根目录中的 index.html 页面，也被放到了内存中；html 插件在自动生成的 index.html 页面，自动打包注入了 webpack 生成的 js 文件

4. 在 webpack.config.js 配置文件中，可以通过 devServer 节点对 webpack-dev-server 插件进行更多的配置

   ```js
   devServe:{
       open: true,			// 初次打包完成后自动打开浏览器
       host: "127.0.0.1",	// 实时打包所使用的主机地址
       port: 80			// 实时打包所使用的端口号
   }
   ```

## webpack中的loader

概述：实际开发过程中，webpack 默认只能打包处理以 .js 后缀名结尾的模块。其他非 .js 后缀名结尾的模块，webpack 默认处理不了，需要调用 loader 加载器才可以正常打包，否则会报错！

loader 加载器的作用：协助webpack 打包处理特定的文件模块。比如：

- css-loader 可以打包处理 .css 相关的文件
- less-loader 可以打包处理 .less 相关的文件
- babel-loader 可以打包处理 webpack 无法处理的高级 JS 语法

loader 的调用过程
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019090403854.png" alt="image-20221019090403854" style="zoom: 50%;" />

使用css文件不需要在 html 文件中导入

### 安装处理 css 文件的 loader

1. `npm i style-loader css-loader -D`

2. 在 webpack.config.js 的 module->rules 数组中，添加 loader 规则如下：

   ```js
   module: {
       rules: [
           {test: /\.css$/,use:['style-loader','css-loader']}
       ]
   }
   // test 表示匹配的文件类型，use 表示对应要调用的 loader
   // use 数组中指定的 loader 顺序是固定的 
   // 多个 loader 的调用顺序是：从后往前调用
   ```

   ![image-20221019093043813](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019093043813.png)

### 安装处理 less 文件的 loader

1. `npm i less-loader less -D`

2. 在 webpack.config.js 的 module -> rules 数组中，添加 loader 规则如下：

   ```js
   module: {
       rules: [
           {test:/\.less$/,use: ['style-loader','css-loader','less-loader']}
       ]
   }
   ```

   ### 安装处理 url 路径相关的文件

   1.  `npm i url-loader file-loader -D`

   2. 在 webpack.config.js 的 module->rules 数组中，添加loader规则如下：

      ```js
      module: {
          rules: [
              {test:/\.jpg|png|gif$/, use: "url-loader?limit=22229"}
          ]
      }
      // 其中 ? 之后的是 loader的参数想
      // limit 用来指定图片的大小，单位是字节
      // 只有 <= limit 大小的图片，才会被转为base64 格式的图片
      ```

   使用此 loader 时，不必在 html 文件中对 img 标签的 src 进行复制，应该在 index.js 中使用DOM为其操作赋值

### 打包处理 js 文件中的高级语法

webpack 只能打包处理一部分高级的 JS 语法。对于那些 webpack 无法处理的高级 js 语法，需要借助于 babel-loader 进行打包处理。例如下面的代码：

```js
// 1.定义了名为 info 的装饰器
function info(target){
    // 2.为目标添加静态属性 info
    target.info = 'Person info'
}
// 3.为 Person 类应用 info 装饰器
@info
class Person{}
// 4. 打印Person 的静态属性 info
console.log(Person.info)
```

1. `npm i babel-loader@8.2.2 @babel/core@7.14.6 @babel/plugin-proposal-decorators@7.14.5 -D`

2. 在 webpack.config.js 的 module -> rules 数组中添加 loader 规则如下

   ```js
   {test: /\.js$/, use:"babel-loader", exclude:/node_modules/}
   ```

3. 配置 babel-loader

   在项目根目录下，创建名为 bable.config.js 的配置文件，定义 babel 的配置项如下：

   ```js
   module.exports = {
       // 声明 babel 可用的插件
       plugins: [['@babel/plugin-proposal-decorators',{legacy: true}]]
   }
   ```


详情参考 Bable 官网 [官网](https://babeljs.io/docs/en/babel-plugin-proposal-decorators)

### 配置build

1. 在 package.json 文件的 script 节点下，新增 build 命令如下：

   ```js
   "script": {
   	"dev": "webpack serve",// 开发环境中运行 dev 命令
      	"build": "webpack --mode production"// 项目运行时，运行 build 命令
   }
   ```

   --model 是一个参数项，用来指定 webpack 的运行模式。production 代表生产环境，会对打包生成的文件进行代码压缩和性能优化。

   <font color="#d00">注意：</font>通过 --model 指定的参数项，会覆盖 webpack.config.js 中的 model 选项

2. 把 JS 文件统一生成到 js 目录中

   在 webpack.config.js 配置文件的 output 节点中，进行如下的配置：

   ```js
   ouput: {
       path: path.join(__dirname,"dish"),
       // 明确告诉 webpack 把生成的 main.js 文件存放到 dist 目录下的 js子目录中
       filename: 'js/bundle.js'
   }
   ```

3. 把图片文件同意生成到 image 目录中
   修改 webpack.config.js 中的 url-loader 配置项，新增 outputPath选项即可指定图片文件的输出路径

   ```js
   {
       test: /\.jpg|png|gif/,
       use: {
           loader: 'url-loader?limit=xxx&outputPath=images'
       }
       // xxx 代表自己去填写
   }
   ```

4. 自动清理 dist 目录下的旧文件
   为了在每次打包发布时自动清理掉 dist 目录中的旧文件，可以安装并配置 <font color="#d00">clean-webpack-plugin</font>插件：

   - `npm i clean-webpack-plugin -D`

   - ```js
     // 按需导入插件、得到构造函数之后，创建插件的实例对象
     const {CleanWebpackPlugin} = require("clean-webpack-plugin")
     const cleanPlugin = new CleanWebpackPlugin()
     ```

   - ```js
     // 把创建的 cleanPlugin 插件实例对象，挂载到 plugins 节点中
     plugins: [htmlPlugin, cleanPlugin] //挂载插件
     ```

     

# Source Map

source map 就是一个信息文件，里面储存着位置信息，也就是说，source map 文件中压缩混淆后的代码，所对应的转换前的位置。

有了它，出错的时候，除错工具将直接显示原始代码，而不是转换后的代码，能够极大的方便后期的调试

开发环境下，推荐在 webpack.config.js 中添加如下的配置，即可保证运行时报错的行数与源代码的行数保持一致：

```js
module.exports = {
    // eval-source-map 仅限在开发模式下使用，不建议在生产模式下使用
    // 此选项生成的 source map 能够保证 运行时报错的行数 与 源代码的行数 保持一致
    devtool: 'eval-source-map'
}
```

- 开发环境下：
  - 建议把 devtool 的值设置为 eval-source-map
  - 好处：可以精准定位到具体的错误行
- 生产环境下：
  - 建议关闭 source map 或将 devtool 的值设置为 nonsources-source-map
  - 好处：防止源码泄露，提高网站的安全性

<font color="#d00">实际开发中不需要自己配置 webpack</font>

- 实际开发中会使用命令行工具（俗称 CLI）一键生成 带有 webpack 的项目
- 开箱即用，所有webpack配置项都是现成的！
- 我们只需要知道 webpack 中的基本概念即可！

麻了！！！
