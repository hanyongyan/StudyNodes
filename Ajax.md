# 什么是Ajax

ajax能让我们轻松实现网页与服务器之间的数据交互 

jQuery中发起ajax的三个常用请求

1. $.get()获得数据

   ```js
   $.get(url,[data],[callback])
   ```

   url：要请求的资源地址

   data：请求资源要携带的参数

   callback：请求成功时的回调函数

   ```js
   // 无参请求
   $.get(url,function(res){
   	//res 代表请求的返回值
   })
   // 有参请求,此时传入的参数为 id 值为 1
   // 多个参数使用逗号分割
   $.get(url,{id:1},function(res){
   
   })
   ```

2. $.post() 上传数据

   功能单一，专门用来发起post请求，从而向服务器提交数据

   `$.post(url,[data],[callback])`

   url：提交数据的地址

   data：要提交的数据

   callback：数据提交成功时的回调函数

   ```js
   // 无参请求
   $.post(url,function(res){
   	//res 代表请求的返回值
   })
   // 有参请求,此时传入的参数为 id 值为 1
   // 多个参数使用逗号分割
   $.post(url,{id:1},function(res){
   
   })
   ```

3. $.ajax() 上传获得均可

   是一个功能比较综合的函数，它允许我们对Ajax请求进行更详细的配置

   ```js
   $.ajax({
       method:'', 	// 请求的方式，例如 GET 和 POST
       url:'',		// 请求的 URL 地址
       data:'',	// 这次请求要携带的数据 如果不需要数据可以省略
       success:function(res){}	// 请求成功之后的回调函数
   })
   ```

# 接口

使用 ajax 请求数据时，被请求的 URL 地址，就叫做数据接口（简称接口），同时，每个接口必须有请求方式

## 接口文档

顾名思义就是接口的说明文档，他是我们调用接口的依据。好的接口文档包含了对接口URL，参数以及输出内容的说明，卫门参照文档接口就能方便的知道接口的作用，以及接口如何进行调用

合格的的接口文档应该包含以下六项内容

1. 接口名称：用来表示各个接口的简单说明
2. 接口URL：接口的调用地址
3. 调用方式
4. 参数格式：接口需要传递的参数，每个参数必须包含参数名称、参数类型、是否必选、参说说明
5. 相应格式：返回值的详细描述，一般包含数据名称、数据类型、说明三项内容
6. 返回示例（可选）：通过对象的形式，例句服务器返回数据的结构

# form表单

## form表单的基本使用

表单在网页中主要负责数据采集功能，HTML中的 `<from>` 标签，就是用于采集用户输入的信息，并通过 `<from>` 标签的提交操作把采集到的信息提交到服务器端进行处理

表单有三个基本部分组成

- 表单标签：`<from>`
- 表单域：包含了文本框，密码框、隐藏框等
- 表单按钮：`<button>`

### form标签的属性

form标签用来采集数据，form标签的属性则是用来规定如何把采集到的数据发送到服务器

| 属性    | 值                                                           | 描述                                     |
| ------- | ------------------------------------------------------------ | ---------------------------------------- |
| action  | URL地址                                                      | 规定提交表单时，向何处发送表单数据       |
| method  | get或post                                                    | 规定以何种方式把表单数据提交到action url |
| enctype | application/x-www-from-urlencode<br />dmultipart/form-data<br />text/plain | 规定在发送表单数据之前如何对器进行编码   |
| target  | \_blank、\_self、\_parent、\_top、_framename                 | 规定在何处打开action url                 |

- form表单在未指定action属性值的情况下，action的默认值为当前页面的 url 地址，当提交表单后，页面会立即跳转到指定的 url 地址

- target的默认值是 \_self 表示在相同的框架中打开 action url

  | 值          | 描述                       |
  | ----------- | -------------------------- |
  | \_blank     | 在新窗口中打开             |
  | \_self      | 默认，在相同的框架中打开   |
  | \_parent    | 在父框架集中打开（很少用   |
  | \_top       | 在整个窗口中打开（很少用   |
  | \_framename | 在指定的框架中打开（很少用 |

- method的默认值为get，表示通过 URL 地址的形式，把表单数据提交到 action URL

  get方式是和提交少量的、简单的数据，post方式是和用来提交大量的、复杂的、或包含文件上传的数据
  实际开发中使用 post 提交方式用的最多，很少用get

- enctype：默认值是 application/x-www-from-urlencode，不需要文件上传时不填写此属性即可

  | 值                               | 描述                                                         |
  | -------------------------------- | ------------------------------------------------------------ |
  | application/x-www-from-urlencode | 在发送前编码所有字符（默认                                   |
  | dmultipart/form-data             | 不对字符编码<br />在使用`包含文件上传`控件的表单时，必须使用该值 |
  | text/plain                       | 空格转换为 + 号，但不对特殊字符编码（很少用                  |

### 表单的同步提交

通过点击submit按钮，触发表单提交的操作，从而使页面跳转到 action url的行为，叫做表单的同步提交。

缺点：表单同步提交后，整个页面会发生跳转，跳转到 action url 所指向的地址，用户体验很差
			同步提交后，页面之前的状态和数据会丢失

解决方案：表单只负责采集数据，ajax负责将数据提交到服务器

## 通过ajax提交表单数据

### 监听表单提交时间

jQuery中，可以使用如下两种方式，监听到表单的提交时间

```js
$("#form1").submit(function(e){
    // ...
    e.preventDefault() // 阻止默认的提交事件，即防止跳转
})

$("#form1").on("submit",function(e){
    // ...
    e.preventDefault() // 阻止默认的提交事件，即防止跳转
})
```

为了简化表单中数据的获取操作，jQuery提供了 `serialize()` 函数，其语法格式如下 `$(selector).serialize()`，可以一次性获取到表单中的所有数据，使用此函数时必须为每个表单元素添加name属性！

```html
<form id = "form1">
    <input type="text" name = "user_name">
    <input type="password" name = "password">
    <button type="submit">提交</button>
</form>
<script>
	$("#form1").submit(function(e){
    e.preventDefault();
    var data = $(this).serialize();// 获取到了数据
    console.log(data);
})
</script>
```



## 模板引擎的基本概念

模板引擎可以解决字符串拼接的形式渲染UI结构

模板引擎，顾名思义，它可以根据程序员指定的模板结构和数据，自动生成一个完整的HTML页面
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924121758471.png" alt="image-20220924121758471" style="zoom:50%;" />

模板引擎的好处：

1. 减少了字符串的拼接操作
2. 使代码结构更清晰
3. 使代码更易于阅读与维护

## art-template模板引擎

中文官网：http://aui.github.io/art-template/zh-cn/index.html

语法可在官网官方文档里面查看，下面有简单的基本语法

先将art-template下载到本地，然后通过<script>标签加载到网页上使用

art-template使用步骤

1. 导入 art-template
2. 定义数据
3. 定义模板
4. 调用template函数（需要两个参数，数据和模板
5. 渲染HTML结构

```html
  <!-- 1. 导入模板引擎 -->
  <!-- 在 window 全局，多一个函数，叫做 template('模板的Id', 需要渲染的数据对象) -->
  <script src="./lib/template-web.js"></script>
  <!--导入jQuery-->
  <script src="./lib/jquery.js"></script>
</head>

<body>

  <div id="container"></div>

  <!-- 3. 定义模板 -->
  <!-- 3.1 模板的 HTML 结构，必须定义到 script 中 -->
  <script type="text/html" id="tpl-user">
 	<!--{{}} 代表占位符-->
    <h1>{{name}}    ------    {{age}}</h1>
    {{@ test}}

    <div>
      {{if flag === 0}}
      flag的值是0
      {{else if flag === 1}}
      flag的值是1
      {{/if}}
    </div>

    <ul>
      {{each hobby}}
      <li>索引是:{{$index}}，循环项是:{{$value}}</li>
      {{/each}}
    </ul>

    <h3>{{regTime | dateFormat}}</h3>
  </script>

  <script>
    // 定义处理时间的过滤器
    template.defaults.imports.dateFormat = function (date) {
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      var d = date.getDate()

      return y + '-' + m + '-' + d
    }


    // 2. 定义需要渲染的数据
    var data = { name: 'zs', age: 20, test: '<h3>测试原文输出</h3>', flag: 1, hobby: ['吃饭', '睡觉', '写代码'], regTime: new Date() }

    // 4. 调用 template 函数
    // 返回值是一个渲染好的 HTML 文档
    var htmlStr = template('tpl-user', data)
    console.log(htmlStr)
    // 5. 渲染HTML结构
    $('#container').html(htmlStr)
  </script>
</body>
```

标准语法

1. 输出 ：`{{}}` 在{{}}语法中，可以进行变量的输出，对象属性的输出、三元表达式输出、逻辑或输出、加减乘除等表达式输出

2. 原文输出：`{{@ value}}` 如果要输出的value值中，包含了HTML标签结构，则需要使用原文输出语法，才能保证HTML标签能够被正常解析

3. 条件输出：在{{}}中使用 if... else if... /if的方式，进行按需输出

   ```
   {{if value}} 按需输出的内容 {{/if}}
   {{if v1}} 按需输出的内容 {{else if v2}} 按需输出的内容 {{/if}}
   //上面的代码中含有此例子
   ```

4. 循环输出：在{{}}内，通过each语法遍历循环数组，当前循环的索引使用 \$index 进行访问，当前的循环项使用 $value 进行访问，具体代码上面28行

5. 过滤器：过滤器的本质就是一个 function 处理函数
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924131556725.png" alt="image-20220924131556725" style="zoom: 67%;" />

   代码在上面代码 33 和 38 行

   定义过滤器的基本语法如下：filterName自定义

   `template.defaults.imports.filterName = function(value){ /* return 处理的结果*/}`

   使用过滤器 `{{value | filterName}}`



# Ajax加强

## XMLHttpRequest的基本使用

XMLHttpRequest（简称 xhr）是浏览器提供的 JavaScript 对象，通过它，可以请求<font color="#dd0000">服务器上的数据资源</font>。之前所学的jQuery中的Ajax函数，就是基于 xhr 对象封装出来的
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924210559947.png" alt="image-20220924210559947" style="zoom: 67%;" />

使用步骤

### 发起GET请求

1. 创建 xhr 对象
2. 调用 xhr.open() 函数
3. 调用 xhr.send() 函数
4. 监听 xhr.onreadystatechange 事件

```js
// 1.创建 XHR 对象
var xhr = new XMlHttpRequest();
// 2.调用 open 函数，使用参数时在URL中添加，被称为查询字符串
xhr.open(请求方式,URL);
// 3.调用 send 函数
xhr.send();
// 4.监听 onreadystatechange  事件
xhr.onreadystatechange = function(){
    // 固定写法，请求成功的情况
    if(xhr.readyState === 4 && xhr.status === 200){
        // 获取服务器响应的数据
        console.log(xhr.responseText);
    }
}
```

XMLHttpRequest对象的readyState属性，用来表示当前 Ajax 请求所处的状态。每个 Ajax 请求必然处于以下状态中的一个：

| 值   | 状态             | 描述                                              |
| ---- | ---------------- | ------------------------------------------------- |
| 0    | UNSENT           | XMLHttpRequest 对象已被创建，但尚未调用 open 方法 |
| 1    | OPENED           | open 方法已被调用                                 |
| 2    | HEADERS_RECEIVED | send 方法已经被调用，响应头也已经被接受           |
| 3    | LOADING          | 数据接收中，此时 response 属性中已经包含部分数据  |
| 4    | DONE             | Ajax 请求完成，这意味着数据传输已经彻底完成或失败 |

查询字符串（上面代码第三行）：查询字符串（URL参数）是指在URL末尾加上用于香服务器发送信息的字符串（变量）

格式：讲英文的?放在URL的末尾，然后再加上<font color="#dd0000">参数=值</font>，想要加上多个参数的花，使用 & 符号进行分割。以这个形式，可以将想要发送给服务器的数据添加到URL中



### 发起POST请求

步骤

1. 创建 xhr 对象
2. 调用 xhr.open() 函数
3. 设置 Content-Type 属性 （固定写法）
4. 调用 xhr.send() 函数
5. 监听 xhr.onreadystatechange 事件
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924220215247.png" alt="image-20220924220215247" style="zoom:50%;" />

### URL编码

URL地址中，只允许出现英文相关的字母、标点符号、数字，因此，在URL地址中不允许出现中文字符
如果URL中需要包含中文这样的字符，则必须对中文字符进行编码（转义）

<font color="#dd0000">URL编码的原则：</font>使用安全的字符（没有特殊用途或者特殊意义的可打印字符）去表示那些不安全的字符
编码原则的通俗理解：使用英文字符去表示非英文字符

浏览器提供了URL编码与解码的API，分别时：

- `encodeURI()`编码的函数
- `decodeURI()`解码的函数

## 数据交换格式

数据交换格式，就是服务器端与客户端之间进行数据传输与交换的格式

前端领域，经常提及的两种数据交换格式分别是 XML 和 JSON 。其中 XML 用的非常少，重点学习数据交换格式 JSON
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924220913725.png" alt="image-20220924220913725" style="zoom:50%;" />



JSON中包含对象和数组两种结构，通过这两种结构的相互嵌套，可以表示各种复杂的数据结构
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924221032077.png" alt="image-20220924221032077" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924221219735.png" alt="image-20220924221219735" style="zoom:50%;" />

JSON语法注意事项

- 属性名必须使用双引号包裹
- 字符串类型的值必须使用双引号包裹
- JSON 中不允许使用单引号表示字符串
- JSON 中不能写注释
- JSON 的最外层必须时对象或数组格式
- 不能使用 undefined 或函数作为 JSON 的值

JSON的作用：在计算机与网络之间存储和传输数据
JSON的本质：用字符串来表示 Javascript 对象数据或数组数据

JSON和JS对象的关系：JSON 是 JS 对象的字符串表示法，它使用文本表示一个 JS 对象的信息，本质是一个字符串

### JSON和JS对象互转

实现从 JSON 字符串转换为 JS 对象 使用 `JSON.parse()`方法

```js
var obj = JSON.parse('{"a":"Hello!","b":"World!"}');
// 将里面的 JSON 风格的字符串转换为 js对象
```

JS 对象转换为 JSON 字符串，使用 `JSON.stringfy()` 方法

```js
var obj = { a: 'Hello!', b: 'World!' };
var str1 = JSON.stringify(obj);
console.log(str1);
```

> 序列化和反序列化

把数据对象转换为字符串的过程，叫做序列化。例如调用JSON.stringify()函数的操作

把字符串转换为数据对象的过程，叫做反序列化

## 封装自己的Ajax函数

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924223733454.png" alt="image-20220924223733454" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924223813791.png" alt="image-20220924223813791" style="zoom:67%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924223949802.png" alt="image-20220924223949802" style="zoom:50%;" /><img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924231505226.png" alt="image-20220924231505226" style="zoom:50%;" />



判断请求的类型

不同的请求类型，对应 xhr 对象的不同操作，因此需要对请求类型进行 if... eles ... 的判断
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220924232037104.png" alt="image-20220924232037104" style="zoom:50%;" />



## XMLHttpRequest Level2的新特性

旧版XMLHttpRequest 的缺点

1. 只支持文本数据的传输，无法用来读取和上传文件
2. 传送和接收数据时，没有进度信息，只能提示有没有完成

XMLHttpRequest Level2的新特性

1. 可以设置 HTTP 请求的时限
2. 可以使用 FormData 对象管理表单数据
3. 可以上传文件
4. 可以获得数据传输的进度信息

有时候，ajax操作很耗时，而且无法预知要花多少时间，如果网速很慢，用户可能要等很久，新版本的 XMLHttpRequest 对象，增加了 timeout 属性，可以设置 HTTP 请求的时限

`xhr.timeout = 3000`单位是 ms，过了这个时限，就自动停止 HTTP 请求，与之配套的还有一个 timeout 事件，用来指定回调函数

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925105805112.png" alt="image-20220925105805112" style="zoom:50%;" />

### FormData对象管理表单数据

Ajax操作往往用来提交表单数据，为了方便表单处理，HTML5 新增了一个 FOrmData 对象，可以模拟表单操作

```js
// 1.创建 FormData 对象
var fd = new FormData();
// 2.为 FormData 添加表单项
fd.append("uanem","zs");
fd.append("upwd","123456");
// 3.创建 XHR 对象
var xhr = new XMLHttpRequest();
// 4.指定请求类型与URL地址
xhr.open("POST",URL);
// 5.直接提交 FormData 对象，这与提交网页表单的效果，完全一样
xhr.send(fd);
```

FormData对象也可以用来获取网页表单的值，示例代码如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925111101443.png" alt="image-20220925111101443" style="zoom:50%;" />



### 上传文件

新版本XMLHttpResquest对象，不仅可以发送文本信息，还可以上传文件

实现步骤：

1. 定义 UI 结构 （即页面结构）
2. 验证是否选择了文件
3. 向 FormData 中追加文件
4. 使用 xhr 发起上传文件的请求
5. 监听 onreadystatechange 事件

示例：

```html
<body>
  <!-- 1. 文件选择框 -->
  <input type="file" id="file1" />
  <!-- 2. 上传文件的按钮 -->
  <button id="btnUpload">上传文件</button>
  <br />
  <!-- 3. img 标签，来显示上传成功以后的图片 -->
  <img src="" alt="" id="img" width="800" />

  <script>
    // 1. 获取到文件上传按钮
    var btnUpload = document.querySelector('#btnUpload')
    // 2. 为按钮绑定单击事件处理函数
    btnUpload.addEventListener('click', function () {
      // 3. 获取到用户选择的文件列表
      // 获取到的是一个数组
      var files = document.querySelector('#file1').files
      if (files.length <= 0) {
        return alert('请选择要上传的文件！')
      }
      var fd = new FormData()
      // 将用户选择的文件，添加到 FormData 中
      fd.append('avatar', files[0])

      var xhr = new XMLHttpRequest()
      xhr.open('POST', 'http://www.liulongbin.top:3006/api/upload/avatar')
      xhr.send(fd)

      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
          var data = JSON.parse(xhr.responseText)
          // 查看 返回了什么数据
          console.log(data);
          // 此处是查看从服务器返回来的信息
          if (data.status === 200) {
            // 上传成功，url地址是由服务器返回来的
            document.querySelector('#img').src = 'http://www.liulongbin.top:3006' + data.url
          } else {
            // 上传失败
            console.log('图片上传失败！' + data.message)
          }
        }
      }
    })
  </script>
</body>
```

### 显示上传进度

可以通过监听他 xhr.upload.onprogress 事件，来获取到文件的上传进度。
语法格式如下：
![image-20220925113746590](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925113746590.png)





## jQuery高级用法

### jQuery上传文件

```html

 <input type="file" id="file1" />
  <button id="btnUpload">上传文件</button>
<script>
$('#btnUpload').on('click', function () {
        var files = $('#file1')[0].files
        if (files.length <= 0) {
          return alert('请选择文件后再上传！')
        }

        var fd = new FormData()
        fd.append('avatar', files[0])

        // 发起 jQuery 的 Ajax 请求，上传文件
        $.ajax({
          method: 'POST',
          url: 'http://www.liulongbin.top:3006/api/upload/avatar',
          data: fd,
          processData: false,
          contentType: false,
          success: function (res) {
            console.log(res)
          }
        })
      })
    
</script>
```



### jQuery实现loading效果

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925125807947.png" alt="image-20220925125807947" style="zoom:50%;" />

附加到文档即只能添加到document身上，注意 `$(document).ajaxStart()`函数会监听文档内的所有 AJax 请求

```html
<body>

  <input type="file" id="file1" />
  <button id="btnUpload">上传文件</button>

  <br />
  <img src="./images/loading.gif" alt="" style="display: none;" id="loading" />

  <script>
    $(function () {
      // 监听到Ajax请求被发起了
      $(document).ajaxStart(function () {
        $('#loading').show()
      })

      // 监听到 Ajax 完成的事件
      $(document).ajaxStop(function () {
        $('#loading').hide()
      })

      $('#btnUpload').on('click', function () {
        var files = $('#file1')[0].files
        if (files.length <= 0) {
          return alert('请选择文件后再上传！')
        }

        var fd = new FormData()
        fd.append('avatar', files[0])

        // 发起 jQuery 的 Ajax 请求，上传文件
        $.ajax({
          method: 'POST',
          url: 'http://www.liulongbin.top:3006/api/upload/avatar',
          data: fd,
          processData: false,
          contentType: false,
          success: function (res) {
            console.log(res)
          }
        })
      })
    })
  </script>

</body>
```

# axios

中文文档：[起步 | Axios 中文文档 | Axios 中文网 (axios-http.cn)](https://www.axios-http.cn/docs/intro)

Axios 是专注于网络数据请求的库。

相比于原生的 XMLHttpRequest对象，Axios简单易用，相比于 jQuery，Axios更加轻量化，只专注与网络数据请求



**axios发起GET请求**

语法：`axios.get("url",{params:{ /* 参数 */}}).then(cacllback)`

具体的请求示例如下：

```js
// 请求的 URL 地址
var url = 'http://www.liulongbin.top:3006/api/get';
// 请求的参数对象
var paramsObj = { name: 'zs', age: 20 }
// 调用 axios.get() 发起 GET 请求
axios.get(url, { params: paramsObj }).then(function (res) {
    // res.data 是服务器返回的数据
  	console.log(res.data)
})
```

**axios发起POST请求**

语法：`axios.post("url",{ /* 参数 */ }).then(callback)`

具体请求示例如下：

```js
var url = 'http://www.liulongbin.top:3006/api/post'
var dataObj = { address: '北京', location: '顺义区' }
axios.post(url, dataObj).then(function (res) {
  console.log(res.data)
})
```



**直接使用axios发起请求**

语法：

```js
axios({
    method:"请求类型",
    url:"URL地址",
    data:{ /* POST数据 */},
    params:{ /* GET参数 */}
}).then(callback)
```



两个示例

```js
// POST请求
axios({
  method: 'POST',
  url: 'http://www.liulongbin.top:3006/api/post',
  data: {
    name: '娃哈哈',
    age: 18,
    gender: '女'
  }
}).then(function (res) {
  console.log(res.data)
})

// GET请求
var url = 'http://www.liulongbin.top:3006/api/get'
var paramsData = { name: '钢铁侠', age: 35 }
axios({
  method: 'GET',
  url: url,
  params: paramsData
}).then(function (res) {
  console.log(res.data)
})
```



# 跨域与JSONP

## 同源策略

如果两个页面的协议，域名和端口<font color="#dd0000">都</font>相同，则两个页面具有相同的源
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925134656136.png" alt="image-20220925134656136" style="zoom:67%;" />

同源策略：是浏览器提供的一个安全功能。
同源策略限制了从一个元加载的文档或脚本如何与来自另一个源的资源进行交互。这是一个用于隔离潜在而已文件的重要安全机制。<font color="#dd0000">即</font>浏览器规定，A网站的JavaScript，不允许和非同源的网站C之间，进行资源的交互

1. 无法读取非同源网页的 Cookie、LocalStorage 和 IndexedDB
2. 无法接触非同源网页的 DOM
3. 无法向非同源地址发送 ajax 请求

## 跨域

### 什么是跨域

同源指的是两个 URL 的协议、域名、端口一致，反之，则是跨域。

 出现跨域的根本原因：浏览器的同源策略不允许非同源的 URL 之间进行资源的交互。

### 浏览器对跨域请求的拦截


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220925153829813.png" alt="image-20220925153829813" style="zoom: 50%;" />

注意：浏览器允许发起跨域请求，但是，跨域请求回来的数据，会被浏览器拦截，无法被页面获取到！

### 如何实现跨域数据请求

现如今，实现跨域数据请求，最主要的两种解决方案，分别是 JSONP 和 CORS

JSONP：出现的早，兼容性好，是前端程序员为了解决跨域问题，被迫想出来的一种临时解决方案。确实是只支持 GET 请求，不支持 POST 请求

CORS：出现的较晚，它是 W3C 标准，属于跨域 Ajax 请求的根本解决方案。支持 GET 和 POST 请求，缺点是不兼容某些低版本的浏览器。

## JSONP

JSONP是 JSON 的一种使用模式，可用于解决主流浏览器的跨域数据访问的问题

JSONP的实现原理：
		由于浏览器同源策略的限制，网页中无法通过 Ajax 请求非同源的接口数据，但是 \<script> 标签不受浏览器同源策略的影响，可以通过 src 属性， 请求非同源的 js 脚本。
		因此，JSONP的实现原理，就是通过 \<script> 标签的 src 属性，请求跨域的数据接口，并通过函数调用的形式，接受跨域接口响应回来的数据



# 节流

节流策略：顾名思义，可以减少一段时间内事件的触发频率

节流的应用场景：

1. 鼠标连续不断的触发某事件（如点击），只在单位时间内触发一次
2. 懒加载是要监听计算滚动条的位置，但不必每次华东都出发，可以降低计算的频率，而不必去浪费CPU



























