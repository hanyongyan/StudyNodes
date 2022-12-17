**CSS属性书写顺序**

![image-20221205111211490](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221205111211490.png)

---

**清除浮动**

- 清除浮动的本质是清除浮动元素造成的影响
- 如果父盒子本身有高度，则不需要清除浮动
- 清楚浮动之后，父级就会根据浮动的子盒子自动检测高度。父级有了高度，就不会影响下面的标准流了

有四种方法

1. 额外标签法

   在浮动元素末尾添加一个空的标签 例如：`<div style="clear : both"></div>`,添加的元素必须是块级元素，里面的 style 才是最重要的，不常用

2. 父级添加overflow属性

   将属性值设置为 hidden、auto或scroll，无法显示溢出的部分

3. 父级添加after为元素

   ```css
   /* 正常是两个双冒号，此处为了兼容 ie 使用了单冒号 */
   .clearfix:after{
       content:"";			/* 伪元素必须写的属性 */
       dispaly:block;		/* 插入到元素必须是块级 */
       height:0;			/* 不要看见这个元素 */
       clear:both;			/* 核心代码清除浮动 */
   }
   .clearfix {		/* 兼容 ie6 7*/
       *zoom :1;
   }
   /* clearfix 添加到父类的class 属性 */
   ```

   clearfix 添加到父类的class 属性

4. 父级添加双伪元素

   ```css
   .clearfix:before, .clearfix:after{
       content:"";
       dispaly:table;
   }
   .clearfix:after{
       clear: both;
   }
   .clearfix {		/* 兼容 ie6 7*/
       *zoom :1;
   }
   ```

   clearfix 添加到父类的class 属性

推荐使用后面两个

---

**定位**

定位=定位模式+边偏移

定位模式有：static 静态定位、relative 相对定位、absolute 绝对定位、fixed 固定定位

边偏移有四个属性：top、bottom、left、right，

- 静态定位（了解

  元素默认的定位方式，即无定位的意思。 `选择器{position:static}`

- 相对定位 重要

  `选择器 {positioin:relative}`

  相对定位是元素在以移动位置的时候，是相对于<font color="red">它原来的位置</font>来说 

  原来在标准流的位置继续占有，后面的盒子依然以标准流的方式对待他。（不脱标）典型的应用给绝对定位当爹

- 绝对定位 重要

  `选择器 {position:absolute;}`

  绝对定位是元素在移动位置的时候，是相对于它祖先元素来说的

  1. 如果没有祖先元素或祖先元素没有定位，则以浏览器为准进行定位
  2. 祖先元素有定位（相对、绝对、固定），则以最近一级的有定位祖先元素为参考点移动位置
  3. 脱离标准流

- 固定定位

  `选择器{position:fixed}`

  固定到可视区的某一个位置，不随滚动条的滚动变化，跟父元素没有任何关系，不占有原先的位置（脱标）

- 粘性定位（了解

  `选择器 {position: sticky; top:10px}`

  可以被认为是相对定位和固定定位的混合

  1. 以浏览器的可视窗口为参照点移动元素（固定定位特点
  2. 粘性定位占有原先的位置（相对定位特点
  3. 必须添加 top、left、right、bottom 其中一个才会有效
  4. 当设置的值满足时会进行固定

定位总结

| 定位模式          | 是否脱标 | 移动位置           | 是否常用   |
| ----------------- | -------- | ------------------ | ---------- |
| static 静态定位   | 否       | 不能使用边偏移     | 很少       |
| relative 相对定位 | 否       | 相对于自身位置移动 | 常用       |
| absolute 绝对定位 | 是       | 带有定位的父级     | 常用       |
| fixed 固定定位    | 是       | 浏览器可视区       | 常用       |
| sticky 粘性定位   | 否       | 浏览器可视区       | 当前阶段少 |

使用定位布局时，可能会出现盒子重叠的情况。此时，可以使用z-index来控制盒子的前后次序（z轴）	

语法 `选择器 {z-index:1;}`

- 数值可以是正整数、负整数或0，默认是auto，数值越大盒子越靠上，没有单位
- 如果属性值相同，后来者居上
- 只有定位的盒子才有 z-index 属性

定位特殊属性

- 行内元素添加绝对或者固定定位，可以直接设置宽高
- 块级元素添加绝对或者固定定位，如果不给宽高，默认大小是内容的大小
- 都不会触发外边距合并

浮动只会压住下面标准流的盒子，但是不会压住下面标准流盒子里面的文字（图片）
绝对定位（固定定位）会压住下面标准流中的所有内容

---

**更改界面样式**

所谓的界面样式，就是更改一些用户操作央视，以便提高更好的用户体验

- 更改用户的鼠标样式

  `选择器 {cursor: 属性值;}`

  属性值：default 默认，pointer 小手，move 移动 ，text 文本，not-allowed 禁止

- 表单轮廓

  给表单添加 outline:0 或者 outline: none；这样之后就可以去掉默认的蓝色边框

- 防止表单域拖拽

  `textarea {resize: none;}`

- vertcal-align 经常用于设置图片或者表单和文字垂直对齐

  官方解释：用于设置一个元素的垂直对齐方式，但是他只针对于<font color="red">行内元素或者行内块元素</font>有效。

  `vertical-align: baseline | top | middle | bottom`

  属性：baseline 默认，top 把元素的定远与行中最高元素的顶端对齐，middle 把此元素放在父元素的中部，bottom 把元素的顶端与行中最低的元素的顶端对齐。

  图片地测会有一个空白缝隙，原因是行内块元素回合文字的基线对齐。
  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221207150021680.png" alt="image-20221207150021680" style="zoom:67%;" />给图片添加边框会有空白缝隙

  解决方案：vertical-align 属性不要给baseline 即可 （提倡使用），把图片转换为块级元素（不推荐）

- 单行文本溢出显示省略号

  必须满足下面三个条件

  ```css
  /* 先强制一行内显示文本 */
  white-space: nowrap (默认normal自动换行)
  /* 超出的部分隐藏 */
  overflow: hidden;
  /* 文字用省略号代替超出的部分 */
  text-overflow: ellipsis;
  ```

- 多行文本溢出显示省略号

  有较大兼容性问题，适合用于 webkit 浏览器或移动端（移动端大部分是 webkit 内核）	

  ```css
  overflow: hidden
  text-overflow: ellipsis;
  /* 弹性伸缩盒子模型显示 */
  dispaly: -webkit-box;
  /* 限制在一个块元素显示的文本的行数 */
  -webkit-line-clamp: 2;
  /* 设置或检索伸缩盒对象的子元素的排列方式 */
  -webkit-box-orient: vertical;
  ```

  更推荐让后台人员来做这个效果，因为后台人员可以设置显示多少个字，操作更简单。

---

常见布局技巧

- margin 负值的运用

  当两个盒子均有边框且重叠时，重叠的部分边框会叠加，我们应当使用 margin: 边框宽度 来抵消边框叠加的问题，后面的盒子的边框会压住前面盒子的边框。

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221207153529504.png" alt="image-20221207153529504" style="zoom:33%;" />

  如果当我们悬浮到第一个盒子时边框显示为蓝色，注意右边框并不是蓝色，是因为右边的盒子边框压到了这里

  解决办法：提高盒子的层级即可（如果没有定位，则加相对定位（保留位置），如果有定位，则加 z-index

---

**CSS 初始化**

不同浏览器对有些标签的默认值是不同的，为了消除不同浏览器对HTML文本呈现的差异，照顾浏览器的兼容性，我们需要对CSS初始化。即重设浏览器的样式

---

**HTML5新增的语义化标签**

- \<header>：头部标签
- \<nav>：导航标签
- \<article>：内容标签
- \<section>：定义文档某个区域
- \<aside>：侧边栏标签
- \<footer>：尾部标签

语义化主要是针对搜索引擎的

---

**属性选择器**

属性选择器而可以根据元素特定属性来选择元素。这样就可以不用借助于类或 id 选择器

| 选择符                                     | 简介                                        |
| ------------------------------------------ | ------------------------------------------- |
| E[att]                                     | 选择具有 att 属性的 E 元素                  |
| E[att="val"] <font color="red">重点</font> | 选择具有 att 属性且属性值等于 val 的 E 元素 |
| E[att^="val"]                              | 匹配具有 att 属性且值以 val 开头的 E元素    |
| E[arr$="val"]                              | 匹配具有 att 属性且值以 val 结尾的 E 元素   |
| E[att*="val"]                              | 匹配具有 att 属性且值中含有 val 的 E 元素   |

---

**结构伪类选择器**

| 选择符           | 简介                          |
| ---------------- | ----------------------------- |
| E:first-child    | 匹配父元素中的第一个子元素 E  |
| E:last-child     | 匹配父元素中的最后一个 E 元素 |
| E:nth-child(n)   | 匹配父元素中的第 n 个子元素 E |
| E:first-of-type  | 指定类型 E 的第一个           |
| E:last-of-type   | 指定类型 E 的最后一个         |
| E:nth-of-type(n) | 指定类型 E 的第 n 个          |

nth-child，n可以填入数字，even表示偶数，odd表示奇数，填入n表示全部，也可以填 xn，-n+2 表示从第二个开始，-n+2 表示前两个

---

**<font color="red">伪元素选择器</font>**

伪元素选择器可以帮助我们利用 CSS 创建新标签元素，而不需要 HTML 标签，从而简化 HTML 结构

| 选择符   | 简介                     |
| -------- | ------------------------ |
| ::before | 在元素内部的前面插入内容 |
| ::after  | 在元素内部的后面插入内容 |

注意：

- before 和 after 创建一个元素，但是属于行内元素
- 新创建的这个元素在文档树中是找不到的，所以我们称为为元素
- 语法：`element::before {}`
- before 和 after 必须有 content 属性，content 表示文字内容
- 伪元素选择器和标签选择器一样，权重为1

使用场景：

- 遮罩层

  ```css
  .mask{
      position:relative;
  }
  .mask::before {
      content:"";
      dispaly: none;
      position: absolute;
      top:0;
      left:0;
      width:100%;
      height:100%;
      background: rgba(0,0,0,.5) url(图片地址) no-repeat center;
  }
  .mask:hover::bofore {
      dispaly: block;
  }
  
  ```

- 伪元素清除浮动

  在上面的清除浮动那里

---

**盒子模型**

CSS3 中可以通过 box-sizing来指定盒模型，有两个值：即可指定为 content-box、border-box ，这样我们计算盒子大小的方式就发生了改变。

1. box-sizing:content-box 盒子大小为 width + padding + border （默认
2. box-sizing:border-box 盒子大小为 width

如果盒子模型我们改为了 box-sizing:border-box ，那么 padding 和 border 就不会撑大盒子了（前提是 padding 和 border 不会超过 width 宽度

---

以下是新特性

**滤镜filter** 了解即可

filter CSS 属性将模糊或颜色偏移等图形效果应用于元素。

`filter: 函数(); `例如`filter:blur(5px);` blur 模糊处理 数值越大越模糊

**calc 函数**

此CSS函数让你在声明CSS属性值时执行一些计算

`width: calc(100% -80px)` 括号里面可以使用加减乘除来进行计算

**过渡** <font color="red" > 重点</font>

过渡（transition）是CSS3中具有颠覆性的特征之一，我们可以在不使用 Flash 动画或 JS 的情况下，当元素从一种样式变换为另一样式时为元素添加效果

过渡动画：是从一个状态，渐渐的过渡到另一个状态

可以让我们的页面更好看，更动感十足（ie9以下不支持）但是不会影响页面布局。

我们现在经常和 :hover 一起搭配使用

`transition: 要过渡的属性 花费时间 运动曲线 何时开始；`

- 属性：想要变化的 CSS 属性，如果想要所有的属性都变化写一个 all 即可
- 花费时间：单位是 秒（必须写单位）比如 0.5s
- 运动曲线：默认是 ease （可以省略）
  - liner 匀速、ease 逐渐慢下来、ease-in 加速、east-out 减速、ease-in-out 先加速后减速
- 何时开始：单位是 秒（必须写单位）可以设置延迟出发时间，默认是 0s （可以省略）

```css
div{
    width: 200px;
    height: 200px;
    backgroung-color: pink;
    transition: width .5s;
    /* 如果需要更改多个属性，每个之间使用 逗号进行隔开 */
    /* transition: width .5s, height .3s; */
}
div:hover {
    width: 400px;
    height: 400px
}
```

---

**LOGO SEO 优化**

1. logo 里面首先放一个 h1 标签，目的是为了提权，告诉搜索引擎，这个地方很重要
2. h1 里面再放一个链接，可以返回首页的，把 logo 的背景图片给连接即可。
3. 为了搜索引擎收录我们，我们链接里面要放文字（网站名称），但是文字不要显示出来
   - 方法一： text-indent 移到盒子外面（text-indent：-9999px），然后 overflow:hedden ，这个是淘宝的做法
   - 方法二：直接给 font-size：0 ，就看不到文字了，京东的做法
4. 最后给链接一个 title 属性，这样鼠标就放到 logo 上就可以看到提示文字了。

```html
<div class="logo">
    <h1>
        <a herf="" title="">xxx</a>
    </h1>
</div>
```

```css
.logo a{
    dispaly: block;
    width: ;
    height: ;
    background: url() no-repeat;
    /* font-size: 0; 京东的做法 */
    /* 下面是淘宝的做法 */
    text-indent: -9999px;
    overflow: hidden
}
```

---

# 移动端

视口：视口就是浏览器显示页面内容的屏幕区域，视口可以分为布局视口、视觉视口和<font color="red">理想视口</font>

理想视口需要手动填写 meta 视口标签通知浏览器操作，主要目的布局时口的宽度应该与理想视口的宽度一直，简单理解就是设备有短款，我们布局的视口就有多宽

```html
<!-- meta 视口标签  -->
<meta name="viewport" content="width=device-width,user-scalable=no, initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0">
<!-- vscode 初始化自带的  webstorm 初始化不自带-->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
```

| 属性          | 解释说明                                               |
| ------------- | ------------------------------------------------------ |
| width         | 宽度设置的是vierport的宽度，可以设置device-width特殊值 |
| initial-scale | 初始缩放比，大于0的数字                                |
| maximum-scale | 最大缩放比，大于0的数字                                |
| minimum-scale | 最小缩放比，大于0的数字                                |
| uer-scalable  | 用户是否可以缩放，yes或no（1或0）                      |

