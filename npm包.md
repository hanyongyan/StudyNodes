# i5ting_toc 是一个可以把 md 文档转换为 html 页面的小工具，使用步骤如下

```powershell
#安装为全局包
npm i -g i5ting_toc 
#调用 i5ting_toc ，实现 md 转 html
i5ting_toc -f 要转换的文件路径 -o
# 建议在文件所在目录进行转换
```

# 时间格式化包

```js
// npm i 
const moment = require('moment');
const dt = moment().format("YYYY-MM-DD HH:mm:ss");
console.log(dt);
```

## web 服务器

express 基于 http 模块封装出来的
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221010143457070.png" alt="image-20221010143457070" style="zoom:33%;" />





# 字符串加密包 bcryptjs

- 

- 加密之后的密码，无法被逆向破解
- 同一明文密码多次加密，得到的加密结果各不相同，保证了安全性
- `npm i bcryptjs`
- 调用 `bcrypt.hashSync(明文密码，随机盐的长度)`进行加密





# nodemon

 [官网](https://www.npmjs.com/package/nodemon/) 

在编写调试 node.js 项目的时候，如果修改了项目的代码，则需要频繁的手动 close 掉，然后从新启动。

使用 nodemon 工具，他能够监听项目文件的变动，当代码修改后，nodemon 会自动帮我们重启项目，极大方便了开发和调试

安装 `npm install -g nodemon`

平时启动 node 项目时，一般使用 node app.js 来启动项目，以后我们使用 `nodemon app.js` 来启动项目，这样当我们修改代码后，会自动实现重启项目的效果





# 优化表单验证

1. 安装 `@hapi/joi` 包，为表单中携带的每个数据项，定义验证规则 `npm install @hapi/joi@17.1.0`

2. 安装 `@excook/express-joi` 中间件，来实现自动对表单数据进行验证的功能 `npm i @escook/express-joi` [@escook/express-joi - npm (npmjs.com)](https://www.npmjs.com/package/@escook/express-joi)

3. 新建 `/schema/XXX.js` 用户信息验证规则模块，并初始化代码如下

   ```js
   const joi = require('@hapi/joi')
   
   /**
    * string() 值必须是字符串
    * alphanum() 值只能是包含 a-zA-Z0-9 的字符串
    * min(length) 最小长度
    * max(length) 最大长度
    * required() 值是必填项，不能为 undefined
    * pattern(正则表达式) 值必须符合正则表达式的规则
    */
   
   // 用户名的验证规则
   const username = joi.string().alphanum().min(1).max(10).required()
   // 密码的验证规则
   const password = joi
     .string()
     .pattern(/^[\S]{6,12}$/)
     .required()
   
   // 注册和登录表单的验证规则对象
   exports.reg_login_schema = {
     // 表示需要对 req.body 中的数据进行验证
     body: {
       username,
       password,
     },
   }
   ```

4. 修改 `/router/user.js` 中的代码如下：

   ```js
   const express = require('express')
   const router = express.Router()
   
   // 导入用户路由处理函数模块
   const userHandler = require('../router_handler/user')
   
   // 1. 导入验证表单数据的中间件
   const expressJoi = require('@escook/express-joi')
   // 2. 导入需要的验证规则对象
   const { reg_login_schema } = require('../schema/user')
   
   // 注册新用户
   // 3. 在注册新用户的路由中，声明局部中间件，对当前请求中携带的数据进行验证
   // 3.1 数据验证通过后，会把这次请求流转给后面的路由处理函数
   // 3.2 数据验证失败后，终止后续代码的执行，并抛出一个全局的 Error 错误，进入全局错误级别中间件中进行处理
   router.post('/reguser', expressJoi(reg_login_schema), userHandler.regUser)
   // 登录
   router.post('/login', userHandler.login)
   
   module.exports = router
   ```

5. 在 `app.js` 的全局错误级别中间件中，捕获验证失败的错误，并把验证失败的结果响应给客户端：

   ```js
   const joi = require('@hapi/joi')
   
   // 错误中间件
   app.use(function (err, req, res, next) {
     // 数据验证失败
     if (err instanceof joi.ValidationError) return res.cc(err)
     // 未知错误
     res.cc(err)
   })
   
   ```

   











# 
