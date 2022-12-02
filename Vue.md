# Vue简介

特性：

1. 数据驱动视图
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019172122037.png" alt="image-20221019172122037" style="zoom:50%;" />
2. 双向数据绑定
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019172138157.png" alt="image-20221019172138157" style="zoom:50%;" />

## MVVM

MVVM 是 vue 实现<font color="#d00">数据驱动视图</font>和<font color="#d00">双向数据绑定</font>的核心原理。MVVM指的是 Model、View和 ViemModel，他把每个HTML页面都拆分成了这三个部分，如图所示：<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019173703791.png" alt="image-20221019173703791" style="zoom:50%;" />

工作原理
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019174014662.png" alt="image-20221019174014662" style="zoom:50%;" />

基本代码与MVVM的对应关系
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221019213344135.png" alt="image-20221019213344135" style="zoom: 67%;" />



# Vue 的指令与过滤器

## 指令

[官网指令内容](https://cn.vuejs.org/guide/essentials/template-syntax.html)
vue提供的模板渲染语法中，除了支持绑定简单的数据值之外，还支持 JS 表达式的运算，例如：

```vue
{{ number + 1 }}
{{ ok? 'yes':'no' }}
{{ message.split('').reverse().join('') }}
<div :id="'list-' + id "></div>
```

指定是 vue 为开发者提供的<font color="#d00">模板语法</font>，用于辅助开发者渲染页面的基本结构。

指令按照不同的用途分为如下6大类：

1. 内容渲染指令
2. 属性绑定指令
3. 事件绑定指令
4. 双向绑定指令
5. 条件渲染指令
6. 列表渲染指令

注意：指令是 Vue 开发中最基础、最常用、最简单的知识

> 内容渲染指令

内容渲染指令用来辅助开发者渲染 DOM 元素的文本内容。常见的内容渲染指令有如下三个：

- v-text，缺点会覆盖元素内部原有的内容！
- {{}}，插值表达式，解决了 v-text 的缺点
- v-html
  `v-html` 的内容直接作为普通 HTML 插入—— Vue 模板语法是不会被解析的。如果你发现自己正打算用 `v-html` 来编写模板，不如重新想想怎么使用组件来代替。

> 属性绑定指令

```html
<div v-bind:id="dynamicId"></div>
// v-bind 是语法，id是属性
// v-bind 可以省略为 :
```

> 事件绑定指令

与 el 和 data 平级的定义函数的为 methods

```js
methods:{
    add(){...}
	// add: function(){...}
    // 两种形式不同作用相同
    // 调用 data 中的数据时 使用 this 进行使用
}
```

v-on 绑定指令 eg：`        <button v-on:click="sub">1</button>` 

也可以在调用 sun函数是传参 格式为`        <button v-on:click="sub()">1</button>` ，当然 vue 的这个函数需要有形参 
如果 `add(e){...}` add 的定义是这样，但是调用此函数时没有传入参数，可以使用 `e.target` 来进行DOM操作，用法与js相同，

不过Vue提供了内置变量， 名字叫做 `$event`，他就是原生 DOM 的事件对象<font color="#d00">不常用</font>
`<button @click=add(1,$event)></button>`

`add(n,e)`

v-on 简写指令 <font color="#d00">@</font>

原生 DOM 对象有 onclick、oninput、inkeyup ... 等原生事件，替换为 vue 的事件绑定后，分别为： v-on:click、v-on:input、v-on:keyup

<font color="#d00">事件修饰符</font>

在事件处理函数中调用 event.preventDefault() 或 event.stopPropagation() 是非常常见的需求。因此 vue 提供了事件修饰符的概念，来辅助程序员更方便的对事件的触发进行控制。常见的5个事件修饰符如下：

| 事件修饰符 | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| .prevent   | 阻止默认行为（例如：阻止 a 连接的跳转、阻止表单的提交等      |
| .stop      | 阻止事件冒泡                                                 |
| .capture   | 以捕获模式触发当前的事件处理函数                             |
| .once      | 绑定的事件只触发一次                                         |
| .self      | 只有在 event.target 是当前自身元素时触发事件处理函数         |
| 官网文档   | [文档](https://cn.vuejs.org/guide/essentials/event-handling.html#event-modifiers) |

<font color = "#d00">按键修饰符</font>

在监听键盘事件时，我们经常需要判断详细的按键。因此，可以为键盘相关的事件添加按键修饰符，

```html
// 只有 key 是 enter 时调用 vm.sumbit()
<input @keyup.enter="sumbit">
```

> 双向绑定指令

vue 提供了 v-model 双向数据绑定指令，用来辅助开发者在不操作 DOM 的前体下，快速获取表单的数据

```html
<input v-model="username">
// 里面的值就与 username 双向绑定
// 仅限在 input select textarea 标签使用
```

注意一下 v-model 和 v-bind 的区别

修饰符：

1.  .lazy：监听 change 事件 而不是 input 即当你离开这个 input 表单时才会修改值，而不是立即修改
2.  .number：将输入的合法字符串转为数字
3.  .trim：移除输入内容两端空格

> 条件渲染指令

条件渲染指令用来辅助开发者按需控制 DOM 的显示与隐藏。条件渲染指令有如下两个，分别是：

- v-if：满足条件时会生成元素，不满足删除元素
- v-show：更改 dispaly 的属性

```html
<p v-if="condition===1">你好呀 </p>
<p v-show="condition===1">你好呀 </p>
```

如果需要频繁的转换 推荐使用 v-show

如果刚进入页面的时候，某些元素默认不需要被展示，而且收起这个元素可能不会被展示出来，推荐使用 v-if

v-if 可以单独使用，或者配置 v-else 指令一起使用，v-else 必须配合 v-if 一起使用

```html
    <div id="app">
        <div v-if="Math.random() > 0.5">随机数大于 0.5</div>
        <div v-else>随机数小于 0.5</div>
    </div>
```

还有 v-else-if  

> 列表渲染指令

v-for 列表渲染指令，用来辅助开发者基于一个数组来循环渲染一个列表结构。 v-for 指令需要使用 `item in items` 形式的特殊语法，其中：

- items 是待循环的数组
- item 是被循环的每一项

v-for 指令还支持一个可选的第二个参数，即当前项的索引。 语法格式为 `(item,index) in items`

使用 v-for 指令时 最好 `<div v-for="item in items" :key="item.id">`使用 这个key最好加上 ，而且尽量把 id 作为 key 的值，key的值类型，是字符串或者数字类型的，key值不允许重复，否则会终端报错
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221020125003330.png" alt="image-20221020125003330" style="zoom:50%;" />



## 过滤器

<font color="#d00">Vue3 已经将此功能进行了删除，Vue3 中建议使用计算属性或方法带起被剔除的过滤器功能</font>

过滤器（Filters）是 vue 为开发者提供的功能，常用于文本的格式化。过滤器可以用在两个地方：插值表达式和 v-bind 属性绑定

过滤器应该被添加在 JS 表达式的尾部，由 “管道符” 进行调用，示例代码如下

```html
<!-- 在双花括号中通过 管道符 调用 capitalize 过滤器，对 message 的值进行格式化-->
<P>{{message | capitalize}}</P>
<!-- 在 v-bind 中通过 管道符 调用 formatId 过滤器，对 rawId 的值进行格式化-->
<div v-bind:id="rawId | formatId"></div>
```

Filters 定义：

```js
// 与 data 平级
filters :{
    // 里面来进行定义过滤器
    // 过滤器函数形参中的 val，永远都是 管道符 前面的那个值
    capitalize(val){
        // 过滤器中一定要有返回值
    }
}
```

> 全局过滤器

在 filters 节点下定义的过滤器，称为 私有过滤器 ，因为他只能在当前 vm 实例所控制的 el 区域内使用。如果希望在多个 vue 势力之间共享过滤器，则可以按照如下的格式定义全局过滤器

```js
// 第一个参数是全局过滤器的名字
// 第二个参数是全局过滤器的处理函数
Vue.filter("capitalize",(str)=>{
    return ...;
})
```

不过全局过滤器要定义到所有的 vue 实例之前

如果全局过滤器和私有过滤器名字一致，此时按照就近原则，调用的是私有过滤器

过滤器可以串联的进行调用 `{{ message | filter1 | filter2 }}`

还可以传参：

```html
<p>{{ message | filter(arg1,arg2) }}</p>
<!--下面是 filter 的定义-->
<!--过滤器处理函数的形参队列中：第一个参数永远都是管道符前面待处理的值 从第二个参数开始才是调用过滤器是传来的 arg1 和 arg2 -->
Vue.filter("filter",(msg, arg1, arg2)=>{
	// 过滤器的执行代码
})
```

# 侦听器

watch 侦听器允许开发者监视数据的变化，从而针对数据的变化做特定的操作
方法语法格式如下： 

```js
new Vue({
    watch: {
        // 监听 username 值的变化
        // newVal 是变化后的新值， oldVal 是变化之前的旧值
        // 你要监听谁，就把谁当作 函数名
        username(newVal, oldVal){
            ...
        }
    }
})
```

缺点：

1. 无法在刚进入页面时自动触发！
2. 如果侦听的是一个对象，如果对象中的属性发生了变化，不会触发侦听器！

对象形式侦听器

```js
new Vue({
    watch:
        username:{
    		handler(newVal, oldVal){
            ...
        	},
            immediate: true,
            // 此选项来设置 页面打开时是否会自动执行，默认值时 false
		}
    }
})
```

好处：

1. 可以通过 immediate 选项，让侦听器自动触发！
2. 可以通过 deep 选项，让侦听器深度监听对象中每个属性的变化！

如果要侦听的是对象的子属性的变化，则必须包裹一层单引号

```js
'info.username'(newVal.oldVal){
    ...
}
```

即使用对象中的数据时需要使用单引号包裹

## 计算属性

计算属性指的时通过一系列运算之后，最终得到一个属性值
这个动态计算出来的属性值可以被模板结构或 methods 方法使用。示例代码如下

computed

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221020202007332.png" alt="image-20221020202007332" style="zoom: 67%;" />

虽然rgb 被定义成方法格式，但是它是一个属性

特点

1. 虽然计算属性在声明的时候被定义为方法，但是计算属性的本质是一个属性
2. 计算属性会缓存计算的结果，只有计算属性以来的数据变化时，才会重新进行运算

好处：

1. 实现了代码的复用
2. 只要计算属性中依赖的数据源变化了，则计算属性会自动重新求值

# Vue-cli

vue-cli 是 Vue.js 开发打标准工具。它简化了程序员基于 webpack 创建工程话的 Vue 项目的过程。

vue-cli 是 npm 上的一个全局包：`npm i -g @vue/cli`

基于 vue-cli 快速生成工程化的 vue 项目：`vue create 项目名称`

vue 项目中 src 目录的构成：

```bash
assets 文件夹：存放项目中用到的静态资源文件，例如：css 样式表、图片资源
components 文件夹：程序员封装的、可复用的组件，都要放到 components 目录下
main.js 是项目的入口文件。整个项目的运行，要先执行 main.js
App.vue 是项目的根组件。
```

在工程化的项目中， vue 要做的事情很单纯：通过 main.js 把 App.vue 渲染到 index.html 的指定区域中。

main.js 默认生成文件

```js
// 导入 Vue
import Vue from 'vue'
// 导入 App.vue 文件
import App from './App.vue'

Vue.config.productionTip = false

new Vue({
    // 把 render 函数指定的组件，渲染到 HTML 页面中
  	render: h => h(App),
}).$mount('#app') // 相当于 el

```

1. App.vue 用来编写待渲染的模板结构
2. index.html 中需要预留一个 el 区域
3. main.js 把 App.vue 渲染到了 index.html 所预留的区域中

# Vue 组件

Vue 项目中的组件化开发

vue 中规定：组件的后缀名是 .vue 。

组件的三个组成部分：

1. template -> 组件的模板结构
   里面只能有一个唯一的根节点

2. script -> 组件的 JS 行为
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221021153843404.png" alt="image-20221021153843404" style="zoom:50%;" />

3. style -> 组件的样式

   默认结构是 `<script></script>` ，如果想要使用 less 语法，书写格式为 `<script lang="less"></script>`
   
4. 可以在组件中使用属性来设置组件的名称

   `name:"xxx"` name 与 data 平级，如果不设置 name 属性，在其他组件中被注册时组件的名称就是 components 中 设置的名称，建议每一个组件都起一个 name 名称

## 组件之间的父子关系


<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221021155752490.png" alt="image-20221021155752490" style="zoom:50%;" />

使用组件的三个步骤：

在 App.vue 中导入其他两个组件

```vue
<template>
	<div>
        // 3.以标签形式使用刚才注册的组件
        <xxx></xxx>
    </div>
</template>
<script>
    // 1.使用 import 语法导入需要的组件 @默认指向 src 文件
	import xxx from '@/components/xxx.vue'
    export default{
        // 2.使用 components 节点注册组件
        components:{
            xxx
        }
        // 不过是用此方式注册的是 私有子组件
    }
</script>
```

  在 vue 项目的 main.js 入口文件中，通过 Vue.component() 方法，可以注册全局组件。示例代码如下：

```js
// 导入需要全局注册的组件
import xxx from '@/components/xxx.vue'
// 参数1：字符串格式，表示组件的“注册名称”
// 参数2：需要被全局注册的那个组件
Vue.component("xxx",xxx)
```



配置上面 @ 的路径提示，先安装插件 Path Autocomplete ，再在 settings.json 中添加大括号中的内容

```json
{
    // 导入文件是是否携带文件的拓展名
    "path-autocomplete.extensionOnImport": true,
    // 配置 @ 的路径提示
    "path-autocomplete.pathMappings": {
        "@":"${folder}/src"
    }
}
```

## 组件的 props

props 是组件的自定义属性，在封装通用组件的时候，合理地使用 props 可以极大的提高组件的复用性！
语法格式如下：

```vue
<script>
	export default{
        // 组件的自定义属性，名字合法即可
        props: ['自定义属性A','自定义属性B','其他自定义属性...']
        // 组件的私有属性
        data(){
            return
        }
    }
</script>
```

定义自定义属性以后，在别的组件中导入时对其进行赋值 

```vue
<template>
	<div>
        <xxx 自定义属性A=""></xxx>
    </div>
</template>
```

结合 v-bind 使用自定义属性 `<xxx :自定义属性A=""></xxx>` 使用 v-bind 以后里面传入的是 js 语法，此时里面的值是 数值，否则是 字符串

props 自定义的值是只读的，可以使用此办法解决此问题

```vue
props:["init"],
data(){
    return{
      count:this.init
    }
},
```

props 的另一种设置

```vue
<script>
	export default{
        // 取消使用了数组格式，数组格式无法配置默认值
		props: {
			init: {
            	default: 0, // 设置了默认值
                type: Number, // 指定了是什么类型的值，传入的如不符合会报错
                required: true // 说明此选项是必填项，如果不填写终端会报错，即使是有
			},
		}
	}
</script>
```

## 样式冲突

默认情况下，写在 .vue 组件中的样式会全局生效，因此很容易造成多个组件之间的样式冲突问题。

导致组件之间样式冲突的根本原因是：

1. 单页面应用程序中，所有组件的 DOM 结构，都是基于唯一的 index.html 页面呈现的
2. 每个组件中的样式，都会影响整个 index.html 页面中的 DOM 元素

在当前 .vue 的 style 标签上添加属性 scoped 即 `<style scoped></style>`
会自动为每一个标签添加同一个属性 然后使用了 属性选择器 来进行绑定

> 修改子组件样式

```vue
<style scoped>
    // 这样设置就会对子组件中的标签生效
    // 还有当使用第三方组件库的时候，如果有修改第三方组件默认样式的需求，需要用到 /deep/
    /deep/ h5{
        color: red;
    }
</style>
```

## 生命周期

生命周期是指一个组件从 创建 -> 运行 -> 销毁 的整个阶段，强调的是一个时间段。

生命周期函数：是由 Vue 框架提供的内置函数，会伴随着组件的生命周期，自动按次序执行。

<font color="#d00">注意</font>：生命周期强调的是时间段，生命周期函数强调的是时间点。

![image-20221022132752600](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221022132752600.png)



生命周期图示：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221022133915388.png" alt="image-20221022133915388" style="zoom: 33%;" />

每一个红圈圈起来的就是一个函数

create 已经初始化好了 props、data、methods ，一般在此处发起 请求，

mounted 此时已经渲染好了界面，一般在此处操作 DOM 元素	

updated DOM 元素完成变化，beforeUpdate 此时数据发生了变化 DOM 元素还未发生变化

## 组件之间的数据共享

 在项目开发中，组件之间最常见的关系为：

1. 父子关系
2. 兄弟关系

父向子共享属性需要用到自定义属性

子组件向父组件共享数据需要使用<font color="#d00">自定义事件</font>，示例代码如下：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221022155235731.png" alt="image-20221022155235731" style="zoom: 67%;" />

如果父组件中 getNewCount 函数传入了参数，那么 val 就不能够代表 由子组件传来的值。

函数的定义应该为

```js
getNewCount(item, e){
    ...
}
```

父组件中的使用为： ` getNewCount(item, $event)` 第一个为真正传入的参数，$event 是子组件中传来的值



兄弟组件之间的共享数据

在 Vue2.x 中，兄弟组件之间数据共享的方案是 EventBus ，也不仅是兄弟之间组件共享使用，基本都能使用此情况
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221022203448713.png" alt="image-20221022203448713" style="zoom:50%;" />

1. 创建 eventBus.js 模块，并向外共享一个 Vue 的实例对象
2. 在数据发送方，调用 bus.$emit("事件名称", 要发送的数据) 发送触发自定义事件
3. 在数据接收方，调用 bus.$on("事件名称", 事件处理函数) 方法注册一个自定义事件

# ref 引用

MVVM 在 vue 中，程序员不需要操作 DOM。程序员只需要把数据维护好即可！（数据驱动视图）

在 Vue 项目中，强烈建议不建议大家安装和使用 jQuery，那么当我们需要操作 DOM 时，需要拿到页面上某个 DOM 元素的引用，此时怎么办呢

ref 用来辅助开发者在不依赖 jQuery 的情况下，获取 DOM 元素或组件的引用。

每个 Vue 的组件实例上，都包含一个 `$refs` 对象，里面存储着对应的 DOM 元素或组件的引用。默认情况下， 组件的 $refs 指向一个空对象，（凡是以 \$ 开头的都是 Vue 的内置对象

对你所需要使用的标签添加属性 eg：`<h1 ref="自定义的名字"></h1>`，建议均以 Ref 进行结尾
然后通过 `this.$refs.自定义的名字` 来进行使用即可

<font color="#d00">ref 引用组件实例</font> 
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221022221303313.png" alt="image-20221022221303313" style="zoom:50%;" />

```html
<input type="text" v-if="inputVisible" @blur="showButton" ref="iptRef"/>
<button v-else @click="showInput"></button>
// showButton 是显示按钮， showInput 是展示输入框
// @blur 是失去焦点时做的操作
// 如果想要在显示输入框的同时进行聚焦于输入框上
// this.$refs.iptRef.focus() 使用此代码会报错，由生命周期可知，此时页面还没有渲染完毕，
// input 这个标签还不存在 故读取不到 ref 这个属性
// 解决方案
// this.$nextTick(()=>{
	this.$refs.iptRef.focus();
})
// 此函数的作用是等页面渲染完毕以后再执行
```

组件的 $nextTick(cb) 方法，会把 cb 回调推迟到下一个 DOM 更新周期之后执行。通俗的理解是：等组件的DOM更新完成之后，在执行 cb 回调函数。从而能保证 cb 回调函数可以操作到最新的 DOM 元素。

# 动态组件

动态组件指的是动态切换组件的显示与隐藏

如何实现动态组件的渲染呢？ vue提供了一个内置的 <component> 组件，专门用来实现动态组件的渲染。示例代码如下

```vue
<script>
	data(){
        // 1.当前要渲染的组件名称
		return { conName: "Left"}
	}
</script>
// 2.通过 is 属性，动态指定要渲染的组件
<component :is="comName"></component>

// 3.点击按钮，动态切换组件的名称
<button @click='comName = "Left"'> 展示 Left 组件 </button>
<button @click='comName = "Right"'> 展示 Right 组件 </button>
```

 <component> 就相当于一个占位符

在上面这个例子中切换时 组件会进行销毁和新建，这样不利于数据的保存，如果我们想要该组件内的组件进行缓存，应该使用 <keep-alive></keep-alive> 包裹 component 组件

keep-alive 对应的生命周期函数：

- 当组件被缓存时，会自动触发组件的 deactivated 生命周期函数
- 当组件被激活时，会自动触发组建的 activated 生命周期函数
- 这两个方法和 data 平级

keep-alive 的 include 属性

include 属性用来指定：只有名称匹配的组件会被缓存。多个组件名之间使用英文的逗号分割：

```html
<keep-alive inclue="MyLeft,MyRight">
	<component :is="comName"></component>
</keep-alive>
```

exclude 属性：是来指定谁不被缓存，和 include 两者二选一

# 插槽

插槽是 vue 为组件的封装者提供的能力。允许开发者在封装组件时，把不确定的、希望由用户置顶的部分定义为插槽

![image-20221024093851456](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221024093851456.png)

mycom组件中使用标签 `<slot></slot>` 在父组件中使用 mycom 组件中

```html
<my-com>
	<p>
        mycom 组件的内容区域，声明的 p 标签
    </p>
</my-com>
// p 标签内的内容会渲染在 mycom 组件中的 slot 标签内
```

vue 官方规定，每一个 slot 插槽，都要有一个 name 属性，如果省略了 name 属性，则会有一个默认的属性 default ，在规定 slot 插槽以后 在父组件中应该如下方式使用

```html
<my-com>
    // v-slot 必须使用在 template 标签上
    // template 标签只是一个虚拟的容器，不会被渲染成任何实质性的 html 元素
    // v-slot 属性可以简写为 #
	<template v-slot:slot的name属性>
        // v-slot: 后面的不用加引号
    	<p>
        mycom 组件的内容区域，声明的 p 标签
    	</p>
    </template>
</my-com>
```

如果父组件中没有 v-solt ，子组件的 slot 标签内可以放默认的内容

> 作用于插槽

在封装组件时，为预留的 <slot> 提供属性对应的值，这种用法叫做 作用于插槽

<font color="#d00"> \<slot name='xxx' msg='hello vue.js'> </font> 

```html
// 子组件
// 可以直接使用属性 也可以使用 v-bind 来绑定 data 中的数据
<slot name='xxx' msg='hello vue.js'>
// 父组件
// 这里的 obj 会接受 slot 除了name属性的值
// 这里的 obj 任意命名，标准命名为 scpoe
// 这里也可以进行解构赋值
<template #xxx='obj'>
	<p>
        {{ obj.msg }}
        // 这里的 obj 是一个对象 内容为 { msg: "hello vue.js"}
    </p>    
</template>
```

# 自定义指令

vue 官方提供了 v-text、v-for、v-model、v-if 等常用的指令。除此之外 vue 还允许开发者自定义指令

自定义指令分为两种：

- 私有自定义指令
- 全局自定义指令

在每个 vue 组件中，可以在 directives 节点下声明私有自定义指令。示例代码如下：

```vue
// 与 data 同层级
directives: {
    color:{
        // 为绑定到的 HTML 元素设置红色的文字
		// 当指令第一次被绑定到元素上的时候，会理解触发 bind 对象,当DOM更新时bind函数不会被触发，update 函数会在每次 DOM 更新时被调用
		// 形参中的 el 是绑定了此指令的、原生的 DOM 对象
		// binding 是传来的参数， binding.value 代表真正传来的值
   		 bind(el, binding){
        	el.style.color = binding.value
    	},
		// 在 DOM 更新时被调用
		update(el, binding){
			el.style.color = binding.value
		}
    }
}
------
<p v-color="'red'"></p> 	// 这样是传进去了一个值
<p v-color="color"></p>		// 这样传进去的是 data 中的 color
<p v-color></p>				// 这样是不进行传值的
```

如果 bind 和 update 函数中的逻辑完全相同，则对象格式的自定义指令可以简写成函数形式：

```js
directives: {
    // 在 bind 和 update 时，会触发相同的业务逻辑
    color(el, binding){
        el.style.color = binding.value
    }
}
```

全局自定义指令

全局共享的自定义指令需要通过 `Vue.directive()` 进行声明，示例代码如下：

```js
// 要在 main.js 中定义
// 参数一：字符串，表示全局自定义指令的名字
// 参数二：对象，用来接收指令的参数值
Vue.directive("color",{
    bind(el, binding){
        el.style.color = binding.value
    },
    update(el, binding){
        el.style.color = binding.value
    }
})
// 当 bind update 两个函数的逻辑相同时
Vue.directive('color',function(el, binding){
    el.style.color = binding.value
})
```

# vue整合axios

在 main.js 中 导入  <font color = '#d00'>了解即可 </font> 不能够实现模块的复用

```js
import axios from 'axios'
// 导入了 baseURL 再调用 axios 时就不用使用路径全拼了
axios.defaults.baseURL = 'http://api.example.com';
// 挂载自定义属性
Vue.prototype.axios = axios
// 以后再每个 .vue 组件中发起请求，直接调用 this.axios.xxx
```

不过不利于 API 接口的复用

# 路由

路由就是对应关系

## SPA 与前端路由

SPA：单页面应用程序

前端路由：Hash 地址与组件之间的对应关系，Hash地址即锚链接

前端路由的工作方式：

1. 用户点击了页面上的路由链接
2. 导致了URL地址栏中的 Hash 值发生了变化
3. 前端路由监听到了 hash 地址的变化
4. 前端路由把当前 hash 地址对应的组件渲染到浏览器中

![image-20221025170230881](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221025170230881.png)

## vue-router

vue-router 是 vue.js 官方给出的路由解决方案。他只能结合 vue 项目进行使用，能够轻松的管理 SPA 中组建的切换

[官方文档](https://router.vuejs.org/zh/)

安装和配置的步骤

1. 安装 vue-router 包

   vue2 中 `npm i vue-router@3.5.2 -S`

2. 创建路由模块

   在 src 目录下，新建 router/index.js 路由模块，并初始化如下的代码：

   ```js
   // 1.导入 vue 和 vueRouter 的包
   import Vue from 'vue';
   import VueRouter from 'vue-router';
   import Xxx from '@/components/要显示的组件名'
   // 2.调用 Vue.use() 函数，把 VueRouter 安装为 Vue 的插件
   Vue.use(VueRouter);
   // 3.创建路由的实例对象
   const router = new VueRouter({
       routes:[
           // 当访问 / 地址时，会自动跳转到你想访问的首页
           { path: '/', redirect: '/下面你想指定的首页' },
           {path:'app.vue中锚点链接的名字，去掉#号', component: Xxx}
           // 有多个就导入多个
       ]
   });
   // 向外共享路由的实例对象
   export default router
   ```

3. 导入并挂载路由模块

   ```js
   // 在 main.js 中
   // 导入模块
   import router from '@/router/index.js';
   
   new Vue({
     render: h => h(App),
     // 在 vue 项目中，要想把路由对象用起来，必须通过下面的方式挂载路由对象
     // 名称一致可简写为 router
     router: router
   }).$mount('#app')
   ```

4. 声明路由链接和占位符

   ```html
   // 在 App.vue 文件中 to 的值 对应 2 中的path地址
   <router-link to="/">首页</router-link>
   <router-link to="/movie">电影</router-link>
   <router-link to="/about">关于</router-link>
   // 再导入 此标签
   <router-view></router-view>
   // 即可完成对应的转换
   ```

### 嵌套路由

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221026090551920.png" alt="image-20221026090551920"  />

> 通过 children 属性声明子路由规则

在 src/router/index.js 路由模块中，导入需要的组件，并使用 children 属性声明子路由规则：

```js
// 导入对应的组件和子组件
import About from '@/components/About.vue'
import Tab from '@/components/tabs/Tab.vue'
const router = new VueRouter({
    routers:[
        {	// 父级路由规则
            path:'/about',
            component: About,
            // 默认子路由
            // 还有一种方法不适用默认下面这个属性，
            // 而是在 children 节点的 path 属性改为 空，即可设置默认子路由
            redirect: '/about/tab',
            children: [ //通过 children 属性，嵌套声明子路由规则
                // 访问 /about/tab 时 ，展示 Tab 组件
                // 注意不要以斜线开头
                {path: 'tab', component: Tab},
            ]
        }
    ]
})
```

### 动态路由匹配

![image-20221026103753746](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221026103753746.png)

动态路由指的是：把 Hash 地址中可变的部分定义为参数项，从而提高路由规则的复用性
在 vue-router 中使用英文的冒号（:）来定义路由的参数项。示例代码如下：

```js
// 路由中的动态参数以 : 进行声明，冒号后面是动态参数的名称
{ path: 'movid/:id', component: Movie }
// 将以下三个路由规则，合并成了一个，提高了路由规则的复用性
{ path: "/movie/1", component: Movie }
{ path: "/movie/2", component: Movie }
{ path: "/movie/3", component: Movie }
```

当你需要获得传入的参数来展示不同的内容的时候，我们通过 `this.$route.params.id` id 是你的动态参数名称。this.\$route是路由的参数对象，this.\$router 是路由的导航对象，this.$router 下面会写
内容详见[编程式导航](###编程式导航跳转)

区分一下 `this.$route` 和 `this.$router` 第一个是路由的参数对象，第二个是 路由的导航对象

为路由开启 props 传参
![image-20221026110909233](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221026110909233.png)

### 编程式导航跳转

在浏览器中，点击链接实现导航的方式，叫做声明式导航。例如

- 在普通网页中点击 <a> 连接， vue 项目中点击 <router-link> 都属于声明式导航

在浏览器中，调用 API 方法实现导航的方式，叫做编程时导航。例如：

- 普通网页中调用 lication.herf 跳转到新页面的方式，属于编程式导航。

vue-router 提供了许多编程式导航的 API ，其中最常用的导航 API 分别是：

- `this.$router.push("hash 地址")`

  跳转到指定的 hash 地址，并增加一条历史记录

- `this.$router.replace('hash地址')`

  跳转都指定的 hash 地址，并替换掉当前的历史记录

- `this.$router.go(数值n)`

  可以在浏览历史中前进和后退 数值几就是前进或后退几层，如果后退的层数超过上限原地不动

  在实际开发中，一般只会前进和后退一层页面。因此 vue-router 提供了两个如下便捷方式

  - `this.$router.back()`  在历史纪录中，后退到当一个页面
  - `this.$router.forward()` 在历史纪录中，前进到下一个页面

<font color="#d00">注意在行内使用编程式导航跳转的时候，this 必须省略</font>

###  导航守卫

[导航守卫官方文档](https://router.vuejs.org/zh/guide/advanced/navigation-guards.html)

导航守卫可以控制路由的访问权限。示意图如下：

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221026144553875.png" alt="image-20221026144553875" style="zoom:67%;" />

> 全局前置守卫

每次发生路由的导航跳转时，都会触发全局前置守卫。因此，在全局前置守卫中，程序员可以对每个路由进行访问权限的控制：

```js
// router/index.js 中写
// 创建路由实例对象
const router = new VueRouter({})
// 调用路由实例对象的 beforeEach 方法，即可声明 全局内置守卫
// 每次发生路由导航跳转的时候，都会自动触发 fn 这个 回调函数
router.beforeEach(fn)
// fn 是守卫方法 fn 接受三个形参 格式为：
(to, from, next)=>{
    // to 是将要访问的路由的信息对象，可以得到访问的基本内容
    // from 是将要离开的路由的信息对象
	// next 是一个函数，调用 next() 表示放行，允许这次路由导航
}
```

![image-20221026151216908](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221026151216908.png)























