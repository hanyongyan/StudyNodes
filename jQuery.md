# jQuery概述

jQuery就是为了快速方便的操作DOM，里面基本都是方法，内部是使用JavaScript实现的。

# jQuery的基本使用

等着页面DOM加载完毕后再去执行js

```js
// 第一种方式
$(document).ready(function(){

})
// 第二种方式
$(function(){
    
})
```

相当于原生js中的DOMContentLoaded，不同于原生js中的load事件是等页面文档、外部的js文件、CSS文件、图片加载完毕才执行内部代码。



$是jquery的别称，在代码中可以使用jquery代替 \$

$是jquery中的顶级对象，相当于原生js中的window，把元素利用\$包装成jQuery对象，就可以调用jquery方法

## jQuery对象

jQuery获取对象 $("div");

jQuery对象本质是：利用$对DOM对象包装后产生的对象（伪数组形式）

不能使用原生js的属性和方法

jQuery对象和DOM可以互相转换，因为原生js比jQuery更大，原生的一些属性和方法没有给我们封装，要想使用这些属性和方法需要把jQuery对象转换为DOM对象才能使用

1. DOM对象转换为jQuery对象，`$(DOM对象)`
2. jQuery对象转换为DOM对象，`$("div")[index]` ，`$("div").get(index)`index是索引号

# jQuery常用API

## jQuery选择器

`$("选择器") //里面选择器直接写CSS选择器即可，但是要加引号`

| 名称       | 用法              | 描述                                                     |
| ---------- | ----------------- | -------------------------------------------------------- |
| id选择器   | `$("#id")`        | 获取指定ID的元素                                         |
| 全选选择器 | `$("*")`          | 匹配所有元素                                             |
| 类选择器   | `$(".class")`     | 获取同一类class的元素                                    |
| 标签选择器 | `$("div")`        | 获取同一标签的所有元素                                   |
| 并集选择器 | `$("div,p,li")`   | 获取多个元素                                             |
| 交集选择器 | `$("li.current")` | 交集元素                                                 |
| 子代选择器 | `$("ul>li")`      | 使用>号，获取亲儿子层级的元素，不会获取孙子层级          |
| 后代选择器 | `$("ul li")`      | 使用空格，代表后代选择器，获取ul下的所有li元素，包括孙子 |

### 筛选选择器

| 语法       | 用法            | 描述                                           |
| ---------- | --------------- | ---------------------------------------------- |
| :first     | `$("li:first")` | 获取第一个li元素                               |
| :last      | `$("li:last")`  | 获取最后一个li元素                             |
| :eq(index) | `$("li:eq(2)")` | 获取到的li元素中，选择索引号为2的元素，从0开始 |
| :odd       | `$("li:odd")`   | 获取到的li元素中，选择索引号为奇数的元素       |
| :even      | `$("li:even")`  | 获取到的li元素中，选择索引号为偶数的元素       |

### 筛选方法

| 语法               | 用法                            | 说明                                             |
| ------------------ | ------------------------------- | ------------------------------------------------ |
| parent()           | `$("li").parent();`             | 查找父级                                         |
| children(selector) | `$("ul").children("li")`        | 相当于`$("ul>li")`，相当于子代选择器             |
| find(selector)     | `$("ul").find("li")`            | 相当于`$("ul li")`                               |
| siblings(selector) | `$(".first").siblings("li")`    | 查找兄弟节点，不包括自己                         |
| nextAll([expr])    | `$(".first").nextAll()`         | 查找当前元素之后所有同辈元素                     |
| prevtAll([expr])   | `$(".last").prevAll()`          | 查找当前元素之前所有同辈元素                     |
| hasClass(class)    | `("div").hasClass("protected")` | 检查当前的元素是否含有某个特定的类如果有返回true |
| eq(index)          | `("li").eq(2)`                  | 相当于`$("li:eq(2)")`                            |



## jQuery样式操作

隐式迭代：就是把匹配的所有元素内部进行遍历循环

修改CSS样式

`$("div").css("属性","值")` 赋值

 `$("div").css("属性")` 得到属性对应的值

` $("div").css({
	width: 400,
	height: 400,
	backgroundColor: "red"
// 如果是复合属性则必须采取驼峰命名法，如果值不是数字，则需要加引号
            })` 

设置类样式方法

1. `$("div").addClass("类名")`  		 添加类

2. `$("div").removeClass("类名")` 	删除类

3. `$("div").toggleClass("类名")`     切换类 如果没有添加上这个类，如果有删除这个类

   jQuery里面的类操作知识对指定类进行操作，不影响原来的类名

   原生JS中的className会覆盖元素原来的类名

## jQuery效果

**谁做动画一般谁前面加stop()**
eg：`$(*this*).children("ul").stop().slideToggle();`

### 显示隐藏：

1. 

1. `show([speed],[easing],[fn]);`

   speed：便是动画时长的毫秒数之，三种预定速度之一的字符串（”slow“，”normal“，or ”fast“）

   easing：用来指定切换效果，默认是swing（慢-快-慢），可用参数linear（匀速）

   fn：回调函数

2. `hide([speed],[easing],[fn]);`

3. `toggle([speed],[easing],[fn]);`

#### 滑动：

1. `slideDown([speed],[easing],[fn]);`
2. `slideUp([speed],[easing],[fn]);`
3. `slideToggle([speed],[easing],[fn]);`

#### 事件切换：

`hover([over,]out)`

1. over：鼠标移动到元素上要触发的函数（相当于mouseenter
2. out：鼠标移除元素要触发的函数（相当于mouseleave
3. 如果只写一个函数，那么鼠标经过离开都会触发

#### 淡入淡出效果

`fadeIn([speed],[easing],[fn])`淡入
`fadeOut([speed],[easing],[fn])`淡出
`fadeTo([speed],opacity,[easing],[fn])`  opacity 透明度必须写 取值范围 0~1 之间

### 自定义动画

`animate(params,[speed],[easing],[fn])`
params：想要改变的样式属性，以对象形式传递，必须写。属性名可以不带引号，如果是符合属性则需要采用驼峰命名法，其余参数都可以省略

```js
$("div").animate({
    left:...,
    top:...,
    ...
})
```

## jQuery属性操作

获取属性语法`element.prop("属性名")` 获取属性值
eg：`$("a").prop("herf")`

设置属性语法`element.prop("属性名","属性值")`

元素的自定义属性`element.attr("属性名")`，修改同上



数据缓存：data() 这个里面的数据是存放在元素的缓存里面

```js
$("span").data("uname","andy") 	//对于这个span元素存入了数据uname对应的值是andy
$("span").data("uname")			//获取span元素中数据uname的值
```

当我们使用H5中的自定义属性时，也可以使用data() 来获取

```html
<div data-index = "2">我是div</div>
<script>
	console.log($("div").data("index"));
</script>
```



## jQuery内容文本值

1. `html()` 获取元素的内容，如果里面填入值就是设置元素的内容 相当于原生的 innerHtml
2. `text()`相当于原生的 innerText
3. `val()`获取设置表单值

## jQuery元素操作

### 遍历元素

jQuery隐式迭代是对同一类元素做了同样的操作。如果想要给同一类元素做不同操作，就需要用到遍历。

```js
//语法一
$("div").each(function(index, domEle){
    // index 可以自己指定索引名称
    // 两个元素名字随便写 index 指定索引，domEle 指定所有每个DOM对象
    xxx;
})
// each() 方法遍历匹配的每一个元素。主要用DOM处理。each每一个
// 里面的回调函数有两个参数：index是每个元素的索引号；demEle是每个DOM元素对象，不是jQ对象
// 要想使用jQuery方法，需要给这个dom元素转换为jQuery对象 $(domEle)
```

```js
// 语法二
$.each(object,function(index,element){
    // 里面直接就是 jq 对象
    xxx;
})
// $.each()方法可以遍历任何对象。主要用于数据处理，比如数组，对象
// 里面的函数有两个参数：index是每个元素的索引号；element遍历内容
```



```html
<div>1</div>
<div>2</div>
<div>3</div>
<script>
    // 遍历迭代对css样式进行更改
	$(function(){
        var arr = ["red","blue","green"];
        $("div").each(function(index,domEle){
            //  需要先转换为JQ对象
            $(domEle).css("color",arr[index]);
        })
    })
</script>
```

### 创建元素

语法：`$("<li></li>")` 创建了一个 li 标签

### 添加元素

1. 内部添加：

   `element.append("内容")` ，把内容放入到匹配元素内部的最后面，相当于原生的 appendChild
   `element.append("内容")`，内部添加并且放到内容的最前面

2. 外部添加：

   `element.after("内容")`，把内容放入目标元素后面

   `element.before("内容")`，把内容放到目标元素前面

### 删除元素

1. `element.remove()` 删除匹配的元素即自身
2. `element.empty()`删除匹配的元素集合中所有的子节点
3. `element.html("")`清空匹配的元素内容

## jQuery尺寸、位置操作

### 尺寸

| 语法                               | 用法                                                     |
| ---------------------------------- | -------------------------------------------------------- |
| width()/height()                   | 取得匹配元素宽度和高度值，只算width/height，传入参数赋值 |
| innerWidth()/innerHeight()         | 获得匹配元素宽度和高度值，包含padding                    |
| outerWidth()/outerHeight()         | 取得匹配元素宽度和高度值，包含padding、border            |
| outerWidth(true)/outerHeight(true) | 取得匹配元素宽度和高度值，包含padding、border、margin    |

### 位置

位置主要有三个：`offset(),position(),scrollTop()/scrollLeft()`

1. `offset()`设置或获取元素偏移（包含top、left属性

   设置或返回被选元素相对于文档的偏移坐标，跟父级没有关系，传入参数可以修改属性

   ```js
   $(".son").offset({
       top: 200,
       left: 200
   });
   ```

2. `position()`只能获取不能设置偏移，获取距离带有定位父级位置偏移，如果没有定位则以文档为主

3. `scrollTop()/scrollLeft()`设置或获取元素被卷去的头部或左侧

   

# jQuery事件

## jQuery事件注册

`element.事件(function(){})`与原生js差不多

## jQuery事件处理

### on()

`on()`方法在匹配元素上绑定一个或多个事件处理函数
`element.on(events,[selector],fn)`

1. events：一个或多个用空格分割的事件类型，如“click”或“keydown”
2. selector：元素的子元素选择器
3. fn：回调函数，及绑定在元素身上的侦听函数

```js
// 绑定多个事件，多个处理事件处理程序
$("div").on({
    mouseenter: function() {
        $(this).css("background", "skyblue");
    },
    click: function() {
        $(this).css("background", "purple");
    },
    mouseleave: function() {
        $(this).css("background", "blue");
    }
});
// 如果事件处理程序相同
$("div").on("mouseenter mouseleave", function() {
    $(this).toggleClass("current");
});
```

`on()`可以事件委派操作，时间委派的定义就是，把原来加给子元素身上的事件绑定在父元素身上，就是把事件委派给父元素

可以为动态添加的元素绑定事件

```js
// click是绑定在ul身上的，但是触发的对象是 li
$("ul").on("click","li",function(){
    alert("hello world!")
})
```

`on()`可以给未来动态创建的元素绑定事件

```html

<ol>
    
</ol>

<script>
    <!--此种形式是不能完成click操作的-->
	// $("ol li").click(function(){
	//     alert(11)
	// })
    // var li = $("<li>我是li</li>");
    // $("ol").append(li);
    
   	<!--这种形式是能够完成click操作的-->
    $("ol").on("clicl","li",function(){
	    alert(11)
	})
    var li = $("<li>我是li</li>");
    $("ol").append(li);
</script>
```

### off()

`off()`方法可以移除通过 `on()` 方法添加的事件处理程序

1. `$("p").off()`解绑p元素所有事件处理程序
2. `$("p").off("click")`解绑p元素上面的点击事件，后面的foo是
3. `$("p").off("click","li")`解绑事件委托

### one()

跟 `on()` 方法是一样的，但是他只能触发一次

### 自动触发事件 `trigger()`

有些事件希望自动触发，比如轮播图自动播放功能跟点击右侧按钮一致。可以利用定时器自动触发右侧按钮点击事件，不必鼠标点击触发

```js
// 自动触发事件
// 1. 元素.事件()
// $("div").click();会触发元素的默认行为
// 2. 元素.trigger("事件")
// $("div").trigger("click");会触发元素的默认行为
// $("input").trigger("focus");
// 3. 元素.triggerHandler("事件") 就是不会触发元素的默认行为
// $("div").triggerHandler("click");
```



## jQuery事件对象

事件被触发，就会有事件对象的产生

`element.on(events,[selector],function(event){})`event即为事件对象

阻止默认行为：`event.preventDefault() 或者return fasle;`
阻止冒泡：`event.stopPropagation()`

# 其他方法

## jQuery对象的拷贝方法

如果想要包某个对象拷贝（合并）给另外一个对象使用，此时可以使用 `$.extend()`方法

语法：`$.extend([deep],target,object1,[objectN])`

1. deep：如果设为true为深拷贝，默认为fasle，浅拷贝
2. target：要拷贝的目标对象
3. object1：待拷贝到第一个对象的对象，可以有多个
3. 浅拷贝：是把被拷贝的对象复杂数据类型中的地址拷贝给目标对象，修改目标对象会影响被拷贝对象

```js
var targetObj = {
    
};
var obj = {
    id: 1,
    name: "andy",
    msg:{
        age:19
    }
}
$.extend(targetObj,obj);//从obj复制到targetObj
//如果对msg中的内容更改，两个对象的msg内容都会更改，如果修改id或者name就不会出现这种情况
```

## jQuery多库共存的两种方法

jQuery使用 $ 作为标识符，随着 jQuery 的流行，其他js库也会使用 $ 作为标识符，这样一起使用会引起冲突，需要一个解决方案，让jQuery和其他的js库不存在冲突，可以同时存在，这就叫做多库共存

jQuery解决方案

1. 把里面的 $ 符号统一改为 jQuery。比如 `jQuery("div")`
2. jQuery变量规定新的名称：`$.noConflict()`  `var xx =$.noConflict() ` 此时 xx 就相当于$

## jQuery插件

jQuery功能比较有限，想要更复杂的特效效果，可以借助于jQuery插件完成

注意：这些插件也是依赖于jQuery完成的，所以必须要先引入jQuery文件，因此也被称为jQuery

常用插件网站

1. jQuery插件库：http://www.jq22.com/
2. jQuery之家：http://www.htmleaf.com/



![image-20220923112144561](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220923112144561.png)

## bootstrap JS插件

bootstrap框架也是依赖于jQuery开发的，因此里面的js插件使用，也必须引入jQuery文件 





