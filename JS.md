# JS预解析

JS引擎运行 Js 会分为两步：预解析和代码执行

1. 预解析：js引擎会把 js 里面所有的 var 还有 function 提升到当前作用域的最前面
2. 代码执行：按照代码书写的顺序从上往下执行

预解析分为 变量预解析（变量提升）和函数预解析（函数提升）

1. 变量提升：就是把所有的变量声明提升到**`当前的作用域`**最前面但不进行赋值操作

   ```js
   // 比如下面是能够执行成功的
   console.log(num);
   var num = 10;
   //相当于执行了以下代码
   var num;
   console.log(num);
   num = 10;
   ```

2. 函数提升：就是把所有的函数声明提升到`当前的作用域`的最前面但不调用函数

   ```js
   // 比如这样是可以执行的
   fun();
   function fun(){}
   ```

   

# H5自定义属性

H5规定自定义属性`data-`开头作为属性名并赋值，使得自定义属性更加清楚
H5新增的获取自定义属性的方法 `element.dataset.index`index代表 自定义属性 - 后面的值
element.dataset 代表所有的自定义属性
如果自定义属性中有多个 - 获取自定义属性要使用驼峰命名法
eg：`<div data-list-name = "haha">` 对应的获取就是 `element.dataset.listName`

# 节点概述

一般的，节点至少拥有nodeType（节点类型）、nodeName（节点名称）和nodeValue（节点值）这三个基本属性

- 元素节点：nodeType 为 1
- 属性节点：nodeType 为 2
- 文本节点：nodeType 为 3（文本节点包含文字、空格、换行等）

实际开发中，节点操作主要是操作元素节点

## 节点层级

利用DOM树可以把节点划分为不同的层级关系，常见的是父子兄弟层级关系

- 父节点：node.parentNode;		得到的是离元素最近的父节点，找不到就为null

- 子节点：parent.childNodes; 	  不推荐使用

  ​				parent.children		     更推荐使用，获取`所有`的子元素节点，`伪数组形式`

  ​				parent.firstChild			返回第一个子节点，一般是文本节点

  ​				parent.lastChild			返回最后一个子节点，一般是文本节点

  ​				parent.firstElementChild 		返回第一个族元素节点				ie9以上支持

  ​				parent.lastElementChild 		返回最后一个族元素节点			ie9以上支持

- 兄弟节点：node.nextSibling			返回当前元素的下一个兄弟节点，包含所有的节点

  ​				node.nextElementSibling 	返回下一个元素节点 `记这个`

  ​				node.previousSibling		返回当前元素的上一个兄弟节点，包含所有节点

  ​				nodepreviousElementSibling		返回上一个兄弟元素节点 `记这个`

## 节点

### 添加节点

`document.createElement('tagName');`方法创建由 tagName指定的 HTML 元素。

添加节点：node.appendChild(child) 将一个节点添加到指定父节点的子节点列表末尾，追加元素

​				node.insertBefore(child,指定元素)将一个节点添加到父节点的指定子节点的前面

```html
<ul>
	<li>123</li>
</ul>
<script>
    // 1. 创建节点元素
	var li = document.createElement("li");
    // 2. 添加节点
    var ul = document.querySelector("ul");
    ul.appendChild(li);
    // 3. 添加节点到指定位置
    var lili = document.createElement("li");
    ul.insertBefore(lili, ul.children[0]);
</script>
```

`element.insertAdjacentHTML(position, text);`插入一个与当前节点的周围

此方法是可以使用字符串来添加元素的，appendChild 是不支持追加字符串的子元素

position表示插入内容相对于元素的位置，并且必须是以下字符串之一：

- `'beforebegin'`：元素自身的前面。
- `'afterbegin'`：插入元素内部的第一个子节点之前。
- `'beforeend'`：插入元素内部的最后一个子节点之后。
- `'afterend'`：元素自身的后面。

```js
// 原为 <div id="one">one</div>
var d1 = document.getElementById('one');
d1.insertAdjacentHTML('afterend', '<div id="two">two</div>');

// 此时，新结构变成：
// <div id="one">one</div><div id="two">two</div>
```

### 删除节点

`node.removeChile(child)`

```html
<button>删除</button>
<ul>
    <li>熊大</li>
    <li>熊二</li>
    <li>光头强</li>
</ul>
<script>    
    var button = document.querySelector("button");
    button.onclick = function(){
        var ul =document.querySelector("ul");
        ul.removeChild(ul.children[0]);
    }
</script>
```

### 克隆节点

```html
<ul>
    <li>1</li>
	<li>2</li>
</ul>
<script>
	var ul = document.querySelector("ul");
	var lili = ul.chileren[0].cloneNode();
    ul.appendChild(lili);
    //这样的复制是浅拷贝
    //node.cloneNode() 括号为空或者里面是false 浅拷贝只复制标签不复制内容
    //node.cloneNode(true) 深拷贝 复制标签bing'q 
</script>
```



### 动态创建元素的区别

innerHtml创建多个元素效率更高（不拼接字符串的形式，采取数组形式拼接，如果使用字符串拼接效率会非常低）结构稍微复杂

createElement创建多个元素效率稍微低一点点，但是结构更清晰

不同浏览器下，innerHtml采用数组形式拼接生成元素效率要比createElement更高

```js
// 拼接字符串形式，此种形式效率是最最最低的
var inner = document.querySelector(".inner");
for(var i = 0;i<=100;i++){
    inner.innerHtml += '<a herf="#">百度</a>';
}

```

```js
// 数组的方式，此种形式效率最高
var inner = document.querySelector(".inner");
var arr = [];
for(var i = 0; i <= 100; i++){
	arr.push('<a herf="#">百度</a>');
}
inner.innerHtml = arr.join(""); 
//join 是讲数组转换成字符串的一个内置函数，传入的参数代表分割符
```

```js
// 使用createElement的形式 效率稍低与数组形式
var create = document.querySelector(".create");
for(var i = 0; i <= 100; i++){
    var a = document.createElement("a");
    create.appendChild(a);
}
```

# DOM重点核心

## 事件

### 注册事件

```js
var btn = document.querySelector("button");
btn.onclick = funcation(){
    alert("hi");
}
btn.onclick = funcation(){
    alert("hahaha");
}
// 执行以后只会弹出 最后一个
```

```js
var btn = document.querySelector("button");
btn.addEventListener("click",function(){
                    alert("22"); 
           			})
btn.addEventListener("click",function(){
                    alert("22"); 
           			})
//使用事件监听注册事件，同一个元素同一个时间可以添加多个监听器  ie9以上才使用
//第一个参数是事件类型不用加on
```

### 删除事件（解绑事件

```js
// 传统方式完成解绑事件
var div = document.querySelector("div");
div.onclick = funcation(){
	alert("22");
    div.onclick = null;
}
```

```js
// 监听器完成解绑事件
var div = document.querySelector("div");
div.addEventListener("click",fn); 	// 此处的函数调用不需要加括号
function fn(){
    alert(22);
    div.removeEventListener("click",fn);	//完成解绑
}
```

## DOM事件流

事件流描述的是从页面中接收事件的顺序。
事件发生时会在元素节点之间按照特定的顺序传播，这个传播过程即DOM事件流。

![image-20220916104144257](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220916104144257.png)

DOM事件流三个阶段

1. JS代码中只能执行捕获或者冒泡其中的一个阶段
2. onclick和attachEvent只能得到冒泡阶段
3. addEventLIstener(type, listener[ , useCapture])第三个参数如果是true，表示在时间捕获阶段调用事件处理程序；如果是false（不写默认就是false），表示在事件冒泡阶段调用事件处理程序
4. 实际开发中我们很少使用事件捕获，我们更关注事件冒泡。
5. 有些事件是没有冒泡的，比如 onblur、onmouseenter、onmouseleave
6. 事件冒泡有时候会带来麻烦，有时候优惠帮助很巧妙的做某些事件

```html
    <div class="father">
        <div class="son">son盒子</div>
    </div>
    <script>
        // dom 事件流 三个阶段
        // 1. JS 代码中只能执行捕获或者冒泡其中的一个阶段。
        // 2. onclick 和 attachEvent（ie） 只能得到冒泡阶段。
        // 3. 捕获阶段 如果addEventListener 第三个参数是 true 那么则处于捕获阶段  document -> html -> body -> father -> son
        
        // 下面的语句执行时会先弹出 father 再弹出 son
        
        // var son = document.querySelector('.son');
        // son.addEventListener('click', function() {
        //     alert('son');
        // }, true);
        // var father = document.querySelector('.father');
        // father.addEventListener('click', function() {
        //     alert('father');
        // }, true);
        // 4. 冒泡阶段 如果addEventListener 第三个参数是 false 或者 省略 那么则处于冒泡阶段  son -> father ->body -> html -> document
        
        // 下面的语句会先 弹出 son在弹出father 最后弹出 document
        var son = document.querySelector('.son');
        son.addEventListener('click', function() {
            alert('son');
        }, false);
        var father = document.querySelector('.father');
        father.addEventListener('click', function() {
            alert('father');
        }, false);
        document.addEventListener('click', function() {
            alert('document');
        })
    </script>
```

## 什么是事件对象

```html
<div>123</div>
<script>
    // 事件对象
    var div = document.querySelector('div');
    div.onclick = function(e) {
            // console.log(e);
            // console.log(window.event);
            // e = e || window.event;
            console.log(e);
        }
        // div.addEventListener('click', function(e) {
        //         console.log(e);

    //     })
    // 1. event 就是一个事件对象 写到我们侦听函数的 小括号里面 当形参来看
    // 2. 事件对象只有有了事件才会存在，它是系统给我们自动创建的，不需要我们传递参数
    // 3. 事件对象 是 我们事件的一系列相关数据的集合 跟事件相关的 比如鼠标点击里面就包含了鼠标的相关信息，鼠标坐标啊，如果是键盘事件里面就包含的键盘事件的信息 比如 判断用户按下了那个键
    // 4. 这个事件对象我们可以自己命名 比如 event 、 evt、 e（常用）
    // 5. 事件对象也有兼容性问题 ie678 通过 window.event 兼容性的写法  e = e || window.event;
</script>
```



| 事件对象属性方法    | 说明                                                         |
| ------------------- | ------------------------------------------------------------ |
| e.target            | 返回触发事件的对象                                     标准  |
| e.srcElement        | 返回触发事件的对象                                     非标准 ie678 |
| e.type              | 返回该事件的类型 比如click mouseover 不带on                  |
| e.cancelBubble      | 该属性阻止冒泡     非标准  ie678使用                         |
| e.returnValue       | 该属性组织默认事件（默认行为）非标准 ie678使用 比如不让链接跳转 |
| e.preventDefault()  | 该属性组织默认事件（默认行为）标准   比如不让链接跳转        |
| e.stopPropagation() | 阻止冒泡   标准                                              |

阻止默认事件：e.preventDefault()、e.returnValue、直接return

eg：

```js
//eg:
var a = document.querySelector("a");
a.onclick = function(e){
	e.preventDefault();
    //e.returnValue
    //return
    //三种方式均组织了默认行为
}
// a.addElventListener("click",function(e){
// e.preventDefault(); 也是可以的
//})
```

阻止事件冒泡：事件冒泡，开始时有最具体的元素接受，然后逐级向上传播到DOM最顶层节点
标准写法：e.stopPropagation();

```js
var son = document.querySelector('.son');
son.addEventListener('click', function() {
    alert('son');
    //在此处进行了阻止冒泡 但是点击father会继续进行冒泡
    e.stopPropagation();
    //如果是ie678
    e.cancelBubble = true;
    
    // 下面的完成了兼容性
    // if(e && e.stopPropagation){
	// 	e.stopPropagation();
    // } else {
    //     window.event.cancelBubble = true;
    // }
    
    
}, false);
var father = document.querySelector('.father');
father.addEventListener('click', function() {
    alert('father');
}, false);
document.addEventListener('click', function() {
    alert('document');
})
```

## 事件委托（代理、委派）

事件冒泡本身的特性，会带来坏处，也会带来好处，需要我们灵活掌握。

事件委托也成为事件代理，jQuery里面称为事件委派

事件委托的原理：`不是每个子节点单独设置事件监听器，二十事件监听器设置在父节点上，然后利用冒泡原理影响设置每个子节点`

事件委托的作用：提高了程序的性能

```html
<!--比如我们来弹出一个框-->
<ul>
    <li>你好1</li>
    <li>你好2</li>
    <li>你好3</li>
    <li>你好4</li>
</ul>
<script>
    var ul = document.querySelector("ul");
    // 原来的形式需要遍历一下才能进行
    // for(var i =0;i<ul.children.length;i++){
    //     ul.children[i].onclick = function(){
    //         alert( this.innerText);
    //     } 
    // }
    //现在的形式不需要遍历即可进行
    ul.addEventListener("click",function(e){
        // e.target返回触发事件的对象  
        alert(e.target.innerText);
    })
</script>
```

## 鼠标事件

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220916144600263.png" alt="image-20220916144600263" style="zoom:50%;" />

| 鼠标事件对象 | 说明                                  |
| ------------ | ------------------------------------- |
| e.clientX    | 返回鼠标相对于浏览器窗口可视区的X坐标 |
| e.clientY    | 返回鼠标相对于浏览器窗口可视区的Y坐标 |
| e.pageX      | 返回鼠标相对于文档页面的X坐标         |
| e.pageY      | 返回鼠标相对于文档页面的Y坐标         |
| e.screenX    | 返回鼠标相对于电脑屏幕的X坐标         |
| e.screenY    | 返回鼠标相对于电脑屏幕的Y坐标         |

## 键盘事件

| 键盘事件   | 触发条件                                                     |
| ---------- | ------------------------------------------------------------ |
| onkeyup    | 某个键盘按键被松开时触发                                     |
| onkeydown  | 某个键盘按键被按下时触发                                     |
| onkeypress | 某个键盘按键被按下时触发  但是他不识别功能键 比如 ctrl 箭头等 |

三个事件的执行顺序：keydown、keypress、keyup

keyup和keydown事件不区分大小写，keypress区分大小写字母

keyup和keypress在文本框里的特点：他们两个事件触发时，文字还没有落入文本框中

keyup事件触发的时候，文字已经落入文本框里面了

# BOM浏览器模型

## BOM和DOM的区别

| DOM                           | BOM                                             |
| ----------------------------- | ----------------------------------------------- |
| 文档对象模型                  | 浏览器对象模型                                  |
| DOM就是把文档当作一个对象看待 | 把浏览器当作一个对象来看待                      |
| DOM的顶级对象是document       | BOM的顶级对象是window                           |
| DOM主要学习的是操作页面元素   | BOM学习的是浏览器窗口交互的一些对象             |
| DOM是W3C标准规范              | BOM是浏览器厂商在各自浏览器上定义的，兼容性较差 |

BOM包含DOM

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220916172943424.png" alt="image-20220916172943424" style="zoom:50%;" />

## BOM概述

BOM即浏览器对象模型，他提供了独立于内容而与浏览器窗口进行交互的对象，其核心对象时window。

BOM由一系列相关的对象构成

window对象是浏览器的顶级对象，它具有双重角色。

1. 它是 JS 访问浏览器窗口的一个接口
2. 它是一个全局对象。定义在全局作用域中的变量、函数都会变成window对象的属性和方法

在调用的时候可以省略window

### 调整窗口大小事件

window.onresize = function(){}
window.addEventListener("resize",function(){})
只要窗口大小发生像素变化，就会触发这个事件
window.innerWidth 拿到屏幕尺寸

### 定时器

window对象给我们提供了两个定时器

`setTimeout(调用函数，[延迟的毫秒数])` 又被称为回调函数。该定时器在定时器到期后执行调用函数，延迟时间不选就是0ms，只会执行一次

停止定时器 clearTimeout(timeoutId)

```html
<button>点击停止定时器</button>
<script>
    var btn = document.querySelector('button');
    var timer = setTimeout(function() {
        console.log('爆炸了');

    }, 5000);
    btn.addEventListener('click', function() {
        clearTimeout(timer);// 传入的是这个定时器函数的名字
    })
</script>
```

 `setInterval(调用函数，[延迟的毫秒数])`每隔一段时间，就去调用一次回调函数，

**回调函数原理：**函数可以作为一个参数。将这个函数作为参数传到另一个函数里面，当那个函数执行完之后，在执行传进去的这个函数，这个过程就叫做回调。

停止定时器：clearInterval（intervalId）;使用此方法取消调用 setInterval() 建立的定时器

### this指向问题

1. 全局作用域或者普通函数中this指向全局对象window，定时器也是指向window
2. 方法调用中谁调用this指向谁
3. 构造函数中this指向构造函数实例

## JS执行机制

js是单线程

## 同步和异步

### 同步任务

同步任务都在主线程上执行，形成一个执行栈。

### 异步任务

JS的异步是通过回调函数实现的。

一般而言，异步任务有以下三种类型：

1. 普通事件，如click、resize等；   比如 document.onclick = fn() 只有当你点击时才会添加到任务队列中
2. 资源加载，如load、error等；
3. 定时器，包括setInterval、setTimeout；

异步任务相关回调函数添加到任务队列中（任务队列也成为消息队列）

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220917210243959.png" alt="image-20220917210243959" style="zoom: 67%;" />

### JS执行机制

1. 先执行执行栈中的同步任务
2. 异步任务（回调函数）放入任务队列中
3. 一旦执行栈中的所有同步任务执行完毕，系统就会按次序读取任务队列中的异步任务，于是被读取的异步任务结束等待状态，进入执行栈，开始执行。

![image-20220917211952599](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220917211952599.png)



## location对象

window对象给我们提供了一个location属性用于获取或设置窗体的URL，并且可以用于解析URL。因为这个属性返回的是一个对象，所以我们将这个属性也成为location对象。

| location对象属性  | 返回值                             |
| ----------------- | ---------------------------------- |
| `location.herf`   | 获取或者设置整个URL                |
| location.host     | 返回主机（域名） eg：www.baidu.com |
| location.port     | 返回端口号 如果未写返回空字符串    |
| location.pathname | 返回路径                           |
| `location.search` | 返回参数                           |
| location.hash     | 返回片段 #后面内容 常见于锚点链接  |

| location对象方法   | 返回值                                                       |
| ------------------ | ------------------------------------------------------------ |
| location.assign()  | 跟 herf 一样，可以跳转页面（也称为重定向页面                 |
| location.replace() | 替换当前页面，因为不记录历史，所以不能后退页面               |
| location.reload()  | 重新加载页面，相当于刷新或者f5 如果参数为 true 强制刷新 ctrl+f5 |

```js
location.assign("http://www.baidu.com");
location.replace("http://www.baidu.com");
location.reload();
```

## navigator对象

navigator对象包含有关浏览器的信息，他有很多属性，我们最常用的时userAgent，该属性可以返回由客户主机发送服务器的user-agent头部的值	

下面前端代码可以判断用户用哪个终端打开页面

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220918091116255.png" alt="image-20220918091116255" style="zoom: 67%;" />



## History对象

window对象给我们提供了一个history对象，与浏览器历史记录进行交互，该对象包含用户（在浏览器窗口中）访问过的URL

| history对象方法 | 作用                                                         |
| --------------- | ------------------------------------------------------------ |
| back()          | 可以后退功能                                                 |
| forward()       | 前进功能                                                     |
| go(参数)        | 前进后退功能，参数如果是1前进一一个也i面 如果是-1后退一个页面 |



# PC端网页特效

## 元素偏移量offset系列

offset翻译过来就是偏移量，我们使用offset系列相关属性可以`动态的`得到该元素的位置（偏移）、大小等。

- 获取元素距离带有定位父元素的位置
- 获取元素自身的大小（高度宽度，不过返回的数值不带单位）

| offset系列属性       | 作用                                                         |
| -------------------- | ------------------------------------------------------------ |
| element.offsetParent | 返回作为该元素`带有定位`的父级元素，如果父级都没有定位则返回body |
| element.offsetTop    | 返回元素相对带有定位父元素上方的偏移量，如果没有父亲或者父亲没有定位则以body为主 |
| element.offsetLeft   | 返回元素相对带有定位父元素左边框的偏移量，如果没有父亲或者父亲没有定位则以body为主 |
| element.offsetWidth  | 返回自身报考padding、边框、内容区的宽度，返回数值不带单位    |
| element.offsetHeight | 返回自身报考padding、边框、内容区的高度，返回数值不带单位    |

### offset和style的区别

| offset                                        | style                                                        |
| --------------------------------------------- | ------------------------------------------------------------ |
| 可以得到任意样式表中的样式值                  | 只能得到行内样式表的样式值<br />行内样式表：写在标签里面的style |
| 获得的数值是没有单位的                        | 获得的是带有单位的字符串                                     |
| offsetWidth包含padding+border+width           | style.width获得不包含padding和border的值                     |
| offsetWidth等属性是只读属性，只能获取不能赋值 | style.width是可读写属性                                      |
| 我们要想获取元素大小位置，用offset更合适      | 我们要想更改元素值使用style                                  |

## 元素可视区client系列

我们使用client系列的相关属性来获取元素可视区的相关信息。通过client系列的相关属性可以动态的得到该元素的边框大小、元素大小等。

| client系列属性       | 作用                                                         |
| -------------------- | ------------------------------------------------------------ |
| element.clientTop    | 返回元素上边框的大小                                         |
| element.clientLeft   | 返回元素左边框的大小                                         |
| element..cilentWidth | 返回自身包括padding、内容区的宽度、不含边框，返回数值不带单位 |
| element.clientHeight | 返回自身包括padding、内容区的高度、不含边框，返回数值不带单位 |

与offset的区别时不包含边框

## 立即执行函数

立即执行函数：不需要调用，里面能够自己执行的函数

```js
(function(){})(); //第二个小括号可以看作是调用函数 ，可以传入参数
或者
(function(){}());
// 多个立即执行函数之间必须有分号隔开
//上面的虽然是匿名函数，不过也是可以设置方法名的
//立即执行函数最大的作用就是 du'li'chuang
```

pageshow事件，这个时间在页面显示时触发，无论页面是否来自缓存。在重新加载页面是，pageshow会在load事件触发后触发；根据事件对象中的persisted来判断是否是缓存中的页面触发的pageshow事件，注意这个事件给window添加，一般为了`适配火狐浏览器`使用

```js
window.addEventListener("pageshow",function(e){
    if(e.persisted){
		...
    }
})
```

## 元素滚动scroll系列

使用scroll系列的相关属性可以动态的得到该`元素`的大小、滚动距离等

| scroll系列属性       | 作用                                           |
| -------------------- | ---------------------------------------------- |
| element.scrollTop    | 返回被卷去的上侧距离，返回数值不带单位         |
| element.scrollLeft   | 返回被卷去的左侧距离                           |
| element.scrollWidth  | 返回自身实际的宽度，不含边框，返回数值不带单位 |
| element.scrollHeight | 返回自身实际的高估，不含边框                   |

scroll与client相比是真正的内容大小

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220919095234104.png" alt="image-20220919095234104" style="zoom:50%;" />

scrollTop相当于是超出盒子的上方部分

页面被卷去的头部：可以通过`window.pageYOffset`获得，如果是被卷去的左侧 `window.pageXOffset`
注意元素被卷去的头部是 element.scrollTop，页面被卷去的透视则是 window.pageYOffset

## 三者对比

1. offset系列经常用于获取元素位置
2. client经常用于获取元素大小
3. scroll经常用于获取滚动距离
4. 注意页面滚动的距离通过window.pageXOffset获得

## mouseenter和mouseover的区别

mouseover鼠标经过自身盒子会触发，经过子盒子还会出发。mouseenter只会经过自身盒子触发

之所以这样，就是因为mouseenter不会冒泡	



## 动画函数封装

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220919104809523.png" alt="image-20220919104809523" style="zoom:50%;" />







# JS面向对象

子类在构造函数中使用 super ，必须放到 this 前面（必须先调用父类的构造方法，在使用子类构造方法）

ES6中类没有变量提升，所以必须先定义类，才能通过类实例化对象

类里面的共有的属性和方法一定要加 this 使用

## 类里面的this指向问题

1. 构造函数里面的 this 指向的是 创建的实例对象
2. 方法里面的 this 指向的是这个方法的调用者

```js
class Star {
    constructor(uname, age) {
        // constructor 里面的this 指向的是 创建的实例对象
        that = this;
        console.log(this);

        this.uname = uname;
        this.age = age;
        // this.sing();
        this.btn = document.querySelector('button');
        this.btn.onclick = this.sing;
    }
    sing() {
        // 这个sing方法里面的this 指向的是 btn 这个按钮,因为这个按钮调用了这个函数
        console.log(this);
		
        console.log(that.uname); // that里面存储的是constructor里面的this
    }
    dance() {
        // 这个dance里面的this 指向的是实例对象 ldh 因为ldh 调用了这个函数
        _that = this;
        console.log(this);
    }
}
```

## 实例成员和静态成员

```js
class Person {
    constructor(uname, age) {
        this.uname = uname;
        this.age = age;
    }
}
```

- 实例成员就是构造函数内部通过 this 添加的成员 ，uname, age 就是实例成员

  实例成员只能通过实例化的对象来进行访问

  比如：`var ldh = new Person("刘德华",18);  console.log(ldh.age)`

- 静态成员在构造函数本身上添加的成员

  `Person.sex = '男'`只能通过构造函数来进行访问 `console.log(Person.sex)`

## 构造函数原型 prototype

构造函数通过原型分配的函数时所有对象所共享的。

每一个构造函数都有一个 prototype 属性，指向另一个对象。注意这个 prototype 是一个对象，这个对象的所有属性和方法，都会被构造函数所拥有

我们可以把<font color="#dd0000">那些不变的方法</font>，直接定义在 prototype 对象上，这样所有对象的示例就可以共享这些方法。

一般情况下我们的公共属性定义到构造函数里面，公共的方法我们就定义到原型对象prototype身上

### \_\_proto\_\_

对象都会有一个属性 \__proto__指向构造函数的 prototype 对象，之所以我们对象可以使用构造函数prototype 原型对象的属性和方法，就是因为对象 \__\_proto__ 原型的存在
![image-20221003172224493](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221003172224493.png)

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221004183101377.png" alt="image-20221004183101377" style="zoom:50%;" />
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221004183342500.png" alt="image-20221004183342500" style="zoom:50%;" />



### 利用原型对象扩展内置对象方法

比如

```js
Array.prototype.sum = function () {
    var sum = 0;
    for (var i = 0; i < this.length; i++) {
        sum+=this[i];
    }
    return sum;
}

var arr = [1,2,3];
console.log(arr.sum());

```

## 继承

ES6之前并没有给我们提供 extends 继承，我们可以通过构造函数+原型对象模拟实现继承，被称为组合继承

### call（）

调用这个函数，并且修改函数运行时的 this 指向

`fun.call(thisArg,arg1,arg2,...)`

- thisArg：当前调用函数 this 的指向对象
- arg1，arg2：传递的其他参数

```js
function fn(args) {
    console.log(args[0] + args[1]);
    console.log(this);
}
var arr = [1, 2, 3];
fn.call(arr, arr);
// 单独调用fn函数时 this指向的是window
// 使用fn.call(arr,arr) this指向的是 arr 
```

### 借用构造函数继承父类型属性

```js
// 1.父构造函数
function Father(uname, age) {
    this.uname = uname;
    this.age = age;
    this.money = function () {
        console.log("金钱");
    }
}
// 2.子构造函数
function Son(uname, age) {
    Father.call(this, uname, age);
}
// Son.prototype = Father.prototype; 这样直接赋值会有问题，如果修改子原型对象，父原型对象也会跟着一起变化
Son.prototype = new Father();
// 如果使用了上面的形式修改了原型对象，记得利用 constructor 指回原来的原型对象
Son.prototype.constructor = Son;


// 这个是子构造函数的专属方法
Son.prototype.exam = function () {
    console.log("考试");
}
var son = new Son("刘德华", 19);
son.exam();
// 此时就完成了继承 而且 Father 中也不会有 exan() 方法
son.money();
```

### 类的本质

类的本质其实还是一个函数，我们也可以简单地认为 类就是 构造函数的另外一种写法

## ES5新增方法概述

ES5中给我们增加了一些方法，可以很方便的操作数组或者字符串，这些方法主要包括：

- 数组方法
- 字符串方法
- 对象方法

### 数组方法	

迭代（遍历）方法：`forEach(), map(), filter(), some(), every()`

- `array.forEach(function(currentValue, index, arr))`

  - currentValue：数组当前项的值
  - index：数组当前项的索引
  - arr：数组对象本身

  ```js
  var arr = [1, 2, 3];
  arr.forEach(function (value, index, array) {
      // 获取值
      console.log(value);
      // 获取索引号
      console.log(index);
      // 数组本身
      console.log(array);   
  })
  ```

- `array.filter(function(currentValue, index, arr))`

  - filter() 方法创建一个新的数组，新数组中的元素是通过检查指定数组中符合条件的所有元素，主要用于筛选数组

  - 注意他直接返回一个新数组

  - currentValue：数组当前项的值

  - index：数组当前项的索引

  - arr：数组对象本身

  - ```js
    var arr = [12, 22, 33, 1];
    var newArr = arr.filter(function (value, index) {
        return value >= 20;
    });
    console.log(newArr);
    // 过滤出了数组中大于等于20的元素
    
    // 也可以使用类似 lambda 表达式
    var newArr = arr.filter(value => value>20);
    ```

- `array.some(function(currentValue, index, arr))`

  - some() 方法用于检测数组中的元素是否满足指定条件，通俗点 查找数组中是否有满足条件的元素
  
  - 返回的布尔值，查询到就返回 true 查找不到就返回 false
  
  - 如果查找到第一个满足条件的元素，则终止循环，不再继续查找
  
  - ```js
    var arr = [1,2,3];
    var flag = arr.some(x=> x==1);
    console.log(flag);
    ```

### 对象方法

`Object.defineProperty(obj, prop, descriptor)`

- obj：必须，目标对象
- prop：必须，需定义或修改的属性的名字
- descriptor：必须，目标属性所拥有的特性
  - 以对象形式书写
  - value：设置属性的值，默认为 undefined
  - writable：值是否可以重写。true | false 默认为 false
  - enumerable：目标属性是否可以被枚举，true | false 默认为 false  及是否 使用 obj.xxx 查看
  - configurable：目标属性是否可以被删除或是否可以再次修改特性 true | false 默认为 false

## 函数进阶

### 函数内 this 的指向

this 的指向，是当我们调用函数的时候确定的。调用方式的不同决定了 this 的指向不同
一般指向我们的调用者

| 调用方式     | this指向                                   |
| ------------ | ------------------------------------------ |
| 普通函数调用 | window                                     |
| 构造函数调用 | 实例对象、原型对象里面的方法也指向实例对象 |
| 对象方法调用 | 该方法岁数对象                             |
| 事件绑定方法 | 绑定事件对象                               |
| 定时器函数   | window                                     |
| 立即执行函数 | window                                     |

### 改变函数内部 this 指向

JS 为我们专门提供了一些函数方法来帮我们更优雅的处理函数内部 this 的指向问题，常用的有 bind(),call(),apply()

1. call 方法 具体使用上面

2. apply()

   调用一个函数，简单理解为调用函数的方式，但是他可以改变函数的 this 指向

   fun.apply(thisArg, [argsArray])

   - thisArg：在 fun 函数运行时指定的 this 值
   - argsArray：传递的值，必须包含在数组里面
   - 返回值就是函数的返回值，因为他就是调用函数

   应用举例：比如 `Math.max()` 传入参数时，就无法传入一个数组，如果使用 apply 方法就可以传入数组并求出最大值 `Math.max.apply(Math, arr)`，此处传入的 this 不建议为 null

   与 call 的区别就是传递参数的形式不一样

3. bind()

   不会调用函数，但是能够改变函数内部 this 指向

   fun.bind(thisArg, arg1, arg2, ...)

   - thisArg：在 fun 函数运行时 指定的 this 值
   - arg1,arg2：传递的参数
   - 返回值有指定的 this 值和初始化参数改造的原函数拷贝

   ```js
   function fn() {
       console.log(this);
   }
   var o = {
       name:"andy"
   }
   var f = fn.bind(o);
   f()
   ```

   一种使用情况

   ```html
   <button>点击</button>
   
   <script>
       var btn = document.querySelector("button");
       btn.onclick = function () {
           this.disabled = true;
           setTimeout(function () {
               this.disabled = false;
           }.bind(this), 3000);
       }
   </script>
   ```

   三者的主要应用场景

   1. call 经常做继承
   2. apply 经常跟数组有关系，比如借助于数学对象实现数组的最大值最小值
   3. bind 不调用函数但是更改this指向，比如改变定时器内部的this指向

## 严格模式

[严格模式 - JavaScript | MDN (mozilla.org)](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Strict_mode)

JS 除了提供正常模式外，还提供了严格模式，ES5 的严格模式时采用具有限制性 JS 变体的一种方式，即在严格的条件下运行 JS 代码

严格模式对正常的 JS 语义做了一些更改：

1. 消除了 JS 语法的一些不合理、不严谨之处，减少了一些怪异的行为
2. 消除代码运行的一些不安全之处，保证代码运行的安全
3. 提高编译器效率，增加运行速度

严格模式可以应用到整个脚本或者个别函数中。因此在使用时，我们可以将严格模式分为为脚本开启严格模式和为函数开启严格模式两种情况

1. 为脚本开启严格模式

   需要在所有语句之前放一个特定语句 `"use strict";` 或者 `'use strict' ;`

   有的 script 基本是严格模式，有的 script 脚本是正常模式，这样不利于文件合并，所以可以将整个脚本文件放在一个立即执行的匿名函数之中。这样独立创建一个作用域而不影响其他 script 脚本文件

   ```js
   (function(){
       "use strict";
       ...
   })()
   ```

2. 为函数开启严格模式

   需要把`"use strict";`声明放在函数体所有语句之前。

   ```js
   function fn(){
       "use strict";
       ...
   }
   ```

### 严格模式下 this 指向问题

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221006221453222.png" alt="image-20221006221453222" style="zoom:50%;" />

## 闭包

[闭包 - JavaScript | MDN (mozilla.org)](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Closures)

闭包指有权访问另一个函数作用域中变量的函数

简单理解就是：一个作用域可以访问另外一个函数内部的局部变量

### 闭包的作用

延伸变量的作用范围

函数外部使用函数内部变量 相当于把函数内部变量进行了返回

```js
function fun() {
    var num = 10;
    return function () {
        console.log(num);
    }
}
var fn = fun();
fn()
```



## 深拷贝和浅拷贝

1. 浅拷贝知识拷贝一层，更深层次对象级别的只拷贝引用
2. 深拷贝拷贝多层，每一级别的数据都会拷贝

浅拷贝函数 `Object.assign(拷贝到的对象，被拷贝的对象)`

深拷贝

```js
function deepCopy(newObj, oldObj) {
    for (const k in oldObj) {
        var item = oldObj[k];
        // 判断这个值是否为数组
        // 数组在上是因为 数组也属于 object
        if (item instanceof Array) {
            newObj[k] = [];
            deepCopy(newObj[k], item);
        } else if (item instanceof Object) {
            // 判断这个值是否是对象
            newObj[k] = {};
            deepCopy(newObj[k], item);
        } else {
            // 简单类型
            newObj[k] = item;
        }
    }
}
```

## 正则表达式

[正则表达式 - JavaScript | MDN (mozilla.org)](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Guide/Regular_Expressions)

JS 中 正则表达式是对象

创建正则表达式：

1. 通过调用 RegExp 对象的构造函数创建

   `var 变量名 = new RegExp(/表达式/);`

2. 通过字面量创建

   `var 变量名 = /表达式/`

### 测试正则表达式 test

test() 正则对象方法，用于见此字符串是否符合该规则，该对象会返回布尔值，其参数是测试字符串

`regexobj.test(str)`







# ES6

let 声明变量的关键字

- 只在所处于的块级有效

- 不存在变量提升

  ```js
  console.log(a);
  let a = 10;
  //这样是报错的
  console.log(a);
  var a = 10;
  //这样的输出结果是 undefined
  ```

const：声明常量，常量就是值（<font color="#dd0000">内存地址</font>）不能变化的量，对于复杂类型只要内存地址没有发生改变，其内部的值是可以更改的

- 具有块级作用域
- 必须赋初始值

### var、let、const的区别

1. var：其作用域为该语句所在的函数内，且存在变量提升现象
2. let：作用域为该语句所在的代码块内，不存在变量提升
3. const：后面出现的代码中不能再修改该常量的值

|     var      |      let       |     const      |
| :----------: | :------------: | :------------: |
| 函数及作用域 |   块级作用域   |   块级作用域   |
|   变量提升   | 不存在变量提升 | 不存在变量提升 |
|   值可更改   |    值可更改    |   值不可更改   |

## 解构赋值

ES6中允许从数组中提取值，按照对应位置，对变量赋值。对象也可以实现解构

### 数组解构

```js
var [a,b,c]=[1,2,3];
console.log(a);
console.log(b);
console.log(c);
```

### 对象解构

```js
// 对象的解构对应的名字应该相同 ，而且不在乎顺序
var { name, age } = {
    name: "zhangsan",
    age: 20
}
console.log(name);
console.log(age);
//
// 设置别名的一种形式
var { age: myAge, name: myName } = {
    name: "zhangsan",
    age: 20
}
console.log(myName);
console.log(myAge);
```

## 箭头函数

箭头函数不绑定 this 关键字，箭头函数中的 this ，指向的是定义函数位置的上下文 this

箭头函数没有类似 js 中的 arguments 传入的参数不固定时语法结构如下

```js
let sum = (...args)=>{
    let total = 0;
    args.forEach(item=>total+=item);
    return total;
}
console.log(sum(1,2,3,4,5))
```

## 扩展运算符

展开语法，扩展运算符可以将数组或者对象转为用逗号分割的参数序列

```js
let arr = [1,2,3];
console.log(...arr);
// 输出结果是：  1  2  3
```

合并数组

```js
let arr1 = [1,2,3];
let arr2 = [4,5,6];
let arr3 = [...arr1,...arr2];
// 输出结果为： [ 1, 2, 3, 4, 5, 6 ]
// 方法2
arr1.push(...arr2)
```

将类数组或者可遍历对象转换为真正的数组

```js
var divs = document.querySelectorAll("div");
divs = [...divs];
```

## 模板字符串

```js
var name = ``;
```

模板字符串可以解析变量

```js
let name = "张三";
console.log(`hello,my name is ${name}`);
// 输出结果为 hello,my name is 张三
```

## 数据结构 Set

类似于数组，但是成员的值都是唯一的，没有重复的值

```js
const s = new Set();
// ke'yi'jie's
const set = new Set([...]);
```

