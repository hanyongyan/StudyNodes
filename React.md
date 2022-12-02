React 安装 `npm i react react-dom`

- react 包是核心，提供创建元素、组件等功能
- react-dom 包提供 DOM 相关功能等。

React 使用步骤

```html

<div id="root"></div>
// 1.引入两个文件，注意两个文件的顺序
<script src="./node_modules/react/umd/react.development.js"></script>
<script src="./node_modules/react-dom/umd/react-dom.development.js"></script>
// 2.创建react元素
<script>
    // 参数一：元素名称
    // 参数二：元素属性
    // 第三个及其以后的参数：元素的子节点
	const title = React.createElement('h1',null,"Hello React")
    // 这就是第三个及其以后
    //const title = React.createElement('h1',null,"Hello React",React.createElement())
    // 3.渲染 React 元素到页面中
    // 第一个参数：要渲染的 React 元素
    // 第二个参数：DOM对象，用于指定渲染到页面中的位置
    ReactDom.render(title,document.getElementById('root'))
</script>
```

---

React脚手架意义

也可以不使用下面的脚手架，使用 Vite 打包速度更快

- 脚手架是开发现代Web应用的必备。
- 充分利用 Webpack、Babel、ESLint 等工具辅助项目开发
- 零配置，无需手动配置繁琐的工具即可使用
- 关注业务，而不是工具配置

1. 初始化项目，

   命令：`npx create-react-app 项目名称`

   ``npm init react-app 项目名称`，

   `yarn create react-app 项目名称`推荐使用第一个

2. `npm start`运行

3. 还有几种模式 `npm run build` `npm test` `npm run eject`

yarn 介绍

- yarn 是 FaceBook 发布的包管理器，可以看作是 npm 的替代品，功能与 npm 相同
- 具有快速、可靠和安全的特点
- 初始化新项目：yarn init
- 安装包：yarn add
- 安装项目依赖项：yarn

---

在脚手架中使用 React

1. 导入 react 和 react-dom 两个包

   ```js
   import React from 'react'
   import ReactDOM from 'react-dom/client'
   ```

2. 调用 `const root = ReactDOM.createRoot(document.qs("#root"))`方法创建 react 元素

3. 调用 `root.render()`方法渲染 react 元素到页面中。

# JSX

createElement() 方法过于繁琐，由JSX来解决
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221127220947153.png" alt="image-20221127220947153" style="zoom:50%;" />

JSX是JavaScript XML 的简写，表示在 JavaScript 代码中写 XML(HTML) 格式的代码

优势：声明式预防而更加直观、与HTML结构相同，降低了学习成本、提高开发效率

使用JSX语法创建 react 元素 `const title = <h1>Hello JSX</h1>`

注意点：

- React元素的属性名使用驼峰命名法
- 特殊属性名：class -> className、for -> htmlFor 、tabindex -> tabIndex
- 没有子节点的 React 元素可以使用 封闭标签 <xx/>
- 推荐使用小括号包裹 JSX，从而避免 js 中的自动插入分号陷阱。

---

**JSX 中嵌入 JS 表达式** 语法：{ js 表达式}	<font color="red">注意区分与 vue 中的插值表达式</font>

```jsx
const name = "jack"
const div = (
    <div>你好，我叫{name}</div>
)
```

注意点：

- 单大括号中可以填写任意的合法的 js 表达式

- JSX 自身也是 JS 表达式

  ```jsx
  const h1 = <h1>我是JSX</h1>
  const dv = <div>嵌入表达式：{h1}</div>
  ```

- 注意：JS 中的对象是一个例外，一般只会出现在 style 属性中

- 不能在 {} 出现语句，比如：if

---

**列表渲染**

- 渲染一组数据，应该使用数组的 map() 方法
- 注意：渲染列表时应该添加 key 属性，<font color="red">key属性的值要保证唯一</font>
- 原则：map() 遍历谁，就给谁添加 key 属性
- 尽量比原使用索引号作为 key

```jsx
const songs = [
    {id:1, name:'西湖'},
    {id:2, name:'为你唱首歌'},
]

const list = (
	<ul>
    	{songs.map(item => <li key={item.id}>{item.name}</li> )}
    </ul>
)
```

---

JSX 的样式处理

1. 行内样式 - style

   ```jsx
   <h1 style={{color:"red",backgroundColor:"skyblue"}}>JSX的样式处理</h1>
   ```

2. 类名 - className （推荐使用）

   ```jsx
   // 先引入 css 文件，在 css 文件中定义好对应的样式
   import './css/index/css'
   // title 是在 css 文件中定义好的
   const list = <h1 className="title">JSX的样式处理</h1>
   ```

---

总结：

1. JSX 是 React 的核心内容。
2. JSX 表示在 JS 代码中写 HTML 结构，是React声明式的体现
3. 使用 JSX 配合嵌入的 JS 表达式、条件渲染、列表渲染，可以描述任意 uI 结构
4. 推荐使用 className 的方式给 JSX 添加样式
5. <font color="red">React 完全利用 JS 语言自身的能力来编写 UI，而不是造轮子增强HTML功能</font>，也是与 VUE 不同的地方，只要是能用 JS 的语法，就绝对不会用其他的

---

# 组件

组建的特点：

- 组件是 React 的一等公民，使用 React 就是使用组件
- 租价表示页面中的部分功能
- 组合多个组件实现完整的页面功能
- 可复用、独立、可组合

---

## 使用函数创建组件

使用JS的函数或箭头函数创建的组件

```jsx
// 约定1：函数名称必须以大写字母开头
// 约定2：函数组件必须有返回值，表示该组件的结构
// 不想进行渲染，就直接返回 null
function Hello(){
    return (
    	<div>这是我的第一个函数组件！</div>
    )
}
```

渲染函数组件时：用函数名作为组件标签名，可以使用单标签和双标签

```jsx
ReactDOM.render(<Hello/>,root)
```

使用类创建组件

```jsx
// 约定1：类名称也必须以大写字母开头
// 约定2：类组件应该继承 React.Component 父类，从而可以使用父类中提供的方法火属性
// 约定3：类组件必须提供 render() 方法
// 约定4：render() 方法必须有返回值，表示该组件的结构
class Hello extends React.Component{
    reder(){
        return <div>Hello Class Component!</div>
    }
}
```

---

组件抽离为单独JS文件

1. 创建Hello.js文件
2. 在 Hello.js 中导入 React
3. 创建组件
4. 在Hello.js中导出组件
5. 在index.js中导入组件
6. 渲染组件

```jsx
// Hello.js
import React from 'react'
class Hello extends React.Component{
    render(){
        return <div>Hello React!</div>
    }
}
// const Hello = ()=> <div>Hello React!</div> 
// 使用函数也是可以的
export default Hello
// index.js
import Hello from './JS/Hello'
ReactDOM.render(<Hello/>,root)
```

---

###  事件处理

React时间绑定语法与DOM时间语法相似

语法：on + 事件名称 = { 事件处理程序 }，比如：onClick = { () =>{} }，注意React事件采用驼峰命名法

```jsx
class App extends React.Component {
    handelClick (){
        console.log('单击事件触发了！')
    }
    render(){
        return (
        	<button onClick={this.handleClick}></button>
        )
    }
}
// 下面是函数的形式
function App(){
    function handleClick(){
        console.log('单击事件触发了')
    }
    return (
    	<button onClick={handleClick}>click me!</button>
    )
}
```

## 事件对象

- 可以通过事件处理程序的参数获取到事件对象
- React 中的事件对象叫做：合成事件（对象）
- 合成事件：兼容所有浏览器，无需担心跨浏览器兼容性问题

```jsx
function handleClick(e){
    e.preventDefault()
    console.log('事件对象',e)
}
<a onClick={handleClick}>点我不会跳转页面</a>
```

---

## 有状态组件和无状态组件

- 函数组件又叫做无状态组件，类组件又叫做有状态组件
- 状态（state）即数据
- 函数组件没有自己的状态，只负责数据展示（静）
- 类组件有自己的状态，负责更新UI，让页面动起来

比如计数器案例中，点击按钮然如果数值加 1，0和1就是不同时刻的状态，而由0变为1就表示状态发生了变化。状态变化后，UI也要相应的更新。React 中想要实现该功能，就要使用有状态组件来完成。

---

**state 和 setState**

React中不是双向数据绑定，处理起来比 VUE 麻烦一点

- state是组件内部的私有数据，<font color="red">只能在组件内部使用</font>
- state 的值是对象，表示一个组件中可以有多个数据

```jsx
class Hello extends React.Component{
    constructor(){
        super()
        // 初始化state
        this.state = {
            // 里面的属性
            count:0
        }
    }
}
// 下面是 简化语法
class Hello extends React.Component{
	state={
        count: 0
    }
}
```

state 的基本使用

```jsx
class Hello extends React.Component{
	state={
        count: 0
    }
    render(){
        return (
        	<div>有状态组件：{this.state.count}</div>
        )
    }
}
```

setState() 修改状态

- 状态是可变的

- 语法：this.setState({要修改的数据})

- 注意：不要直接修改 state 的值，这样是错误的

- 作用：1.修改 state，2.更新UI

- 思想：数据驱动试图

  ```jsx
  // 正确
  this.setState({
      count: this.state.count + 1
  })
  // 错误
  this.state.count += 1
  ```

```jsx
// 计数器的一个小案例
class Hello extends React.Component {
  state = {
    count: 0
  }
  render() {
    return (
      <div>
        <h1>计数器：{this.state.count}</h1>
        <button onClick={() => {
          this.setState({
            count: this.state.count + 1
          })
        }}>click me</button>
      </div>
    )
  }
}
```

我们应该从JSX中抽离出逻辑代码，保证结构清晰和复用，但是当我们把逻辑抽离出来在类中定义为一个函数以后并不能通过 this.xxx() 调用 `<button onClick={this.xxx}></button>`，这样是不对的，解决方法如下

- 箭头函数：`<button onClick={() => this.xxx()}></button>`

- Function.prototype.bind()：利用ES5中的bind方法，将事件处理程序中的this与组件实例绑定到一起

  ```jsx
  class Hello extends React.Component {
      constructor(){
          super()
          this.xxx = this.xxx.bind(this)
          this.state = { } // 里面填写属性
      }
      // 省略xxx函数
      render(){
          return (
          	<button onClick = {this.xxx}>click me</button>
          )
      }
  }
  ```

- class的实例方法：利用箭头函数形式的 class 实例方法 <font color="red">推荐使用</font>

  ```jsx
  // 注意：改语法是实验性语法，但是由于 babel 的存在可以直接使用
  class Hello extends React.Component {
      xxx = ()=>{}
      render(){
          return (
          	<button onClick={this.xxx}>click me</button>
          )
      }
  }
  ```

---

## 表单处理

**受控组件**

- HTML中的表单元素是可输入的，也就是有自己的可变状态
- 而，React 中可变状态通常是保存到 state 中，并且只能通过 setState() 方法来修改
- React 将 state 与表单元素值value绑定到一起，<font color="red">由state的值来控制表单元素的值</font>
- 受控组件：其值收到React控制的表单元素

```jsx
// state 中添加一个状态，作为表单元素的 value 值（控制表单元素值的来源
// 给表单元素绑定 change 事件，将表单元素的值设置为state的值
class Hello extends React.Component {
  state = {
    txt: ''
  }
  change = (e) => {
    this.setState({
      txt: e.target.value
    })
    console.log(e.target.value, "  ", this.state.txt);
  }
  render() {
    return (
      <div>
        <input type="text" value={this.state.txt} onChange={this.change}></input>
      </div>
    )
  }
}
```

```jsx
// 选择框的改变
class Hello extends React.Component {
  state = {
    isChecked: false
  }
  changeChecked = e => {
    this.setState({
      isChecked: e.target.checked
    })
  }
  render() {
    return (
      <div>
        <input type="checkbox" checked={this.state.isChecked} onChange={this.changeChecked}></input>
      </div>
    )
  }
}
```

**多表单元素优化**

每一个表单元素都有一个单独的事件处理程序处理太繁琐，我们应该使用一个事件处理程序同时处理多个表单元素

1. 给表单元素添加name属性，名称与 state 相同

2. 根据表单元素类型获取对应值

3. 在 change 事件处理程序中通过 [name] 来修改对应的 state

   ![image-20221128134314776](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128134314776.png)

   统一处理函数

   ```jsx
     handleInputChange(event) {
       const target = event.target;
       const value = target.type === 'checkbox' ? target.checked : target.value;
       const name = target.name;
   
       this.setState({
         [name]: value
       });
     }
   ```

---

**非受控组件**

借助于 ref，使用原生DOM方式来获取表单元素值，

使用步骤：

1. 调用 React.createRef() 方法创建一个 ref 对象

   ```jsx
   constructor(){
       super()
       this.txtRef = React.createRef() // 前面的名字自己任意取
   }
   ```

2. 将创建好的 ref 对象添加到文本框中

   ```jsx
   <input type="text" ref={this.txtRef}/>
   ```

3. 通过 ref 对象获取到文本框的值

   ```jsx
   console.log(this.txtRef.current.value)
   ```

---

## 组件间的通讯

组件之间共享数据就是组件通讯

**组件的props**

- 组件时封闭的，要接受外部数据应该通过 props 来实现
- props的作用：接受传递给组件的数据
- 传递数据：给组件标签添加属性
- 接收数据：函数组件通过参数props接收数据，类组件通过 this.props 接收数据

```jsx
// 函数组件
const Hello = (props) => {
  console.log(props);
  return <div>
    接收到数据：{props.name}----{props.age}
  </div>
}

const root = ReactDOM.createRoot(document.querySelector("#root"))
root.render(<Hello name='jack' age={18} />)
```

```jsx
// 类组件
class Hello extends React.Component {
  render() {
    return (
      <div>接收到的数据：{this.props.name}</div>
    )
  }
}

const root= ReactDOM.createRoot(document.querySelector("#root"))
root.render(<Hello name='韩永琰'/>)
```

特点：

1. 可以给组件传递任意类型的数据
   ![image-20221128220056804](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128220056804.png)

2. props 是 只读 的对象，只能读取属性的值，无法修改对象

3. 注意：使用类组件时，如果写了构造函数，应该将 props 传递给 supe() ，否则无法在构造函数中获取到 props ！但是不影响 render 中使用

   ![image-20221128220024887](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128220024887.png)

---

组件通讯的三种方式

1. 父到子
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128220727376.png" alt="image-20221128220727376" style="zoom:50%;" />

2. 子到父

   利用回调函数，父组件提供回调函数，子组件调用，将要传递的数据作为回调函数的参数

   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128223722821.png" alt="image-20221128223722821" style="zoom: 50%;" />
   
3. 兄弟组件

   将共享状态提升到最近的公共父组件中，由公共父组件管理这个状态，公共父组件职责：1.提供共享状态，2.提供操作共享状态的方法。[官方文档](https://zh-hans.reactjs.org/docs/lifting-state-up.html)
   <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128225832638.png" alt="image-20221128225832638" style="zoom:50%;" />

---

**Context**

[文档](https://zh-hans.reactjs.org/docs/context.html#when-to-use-context)

<font color="red">详细的案例看文档</font>

当一个组件去给他的孙组件，孙孙组件传递数据的时候呢，一层一层的传输吗？过于繁琐，我们使用 Context。作用：跨组件传递数据

使用步骤：

1. 调用 React.createContext() 创建 Provider（提供数据）和 Consumer（消费数据）两个组件。

   `const { Provider, Consumer } = React.createContext()

2. 使用 Provider 组件作为父节点

   ```jsx
   <Provider>
   	<div> <Child/> </div>
   </Provider>
   ```

3. 设置 value 属性，表示要传递的数据。`<Provider value="pink">` 写在上面的标签内

4. 在需要获得数据的组件中调用 Consumer 组件接收数据。

   ```jsx
   <Consumer>
   	{data => <span>data表示接收到的数据--{data}</span>}
   </Consumer>
   ```

---

**children属性**

- 表示组件标签的子节点。当组件标签由子节点时，props 就会有该属性
- children 属性与普通的 props 一样，值可以是任意值（文本、React元素、组件，甚至是函数

```jsx
const Hello = (props) => {
  console.log(props);
  return <div>组件的子节点：{props.children}</div>
}
root.render(<Hello>我是子节点</Hello>)
```

对应的页面结果是
![image-20221128233520510](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221128233520510.png)

---

**props校验**

[文档](https://zh-hans.reactjs.org/docs/typechecking-with-proptypes.html)

对于组件来说，props是外来的，无法保证组件使用者传入什么格式的数据
允许创建组件的时候，就指定props的类型、格式等。能捕获使用组件时因为 props 导致的错误，给出明确的错误提示，增加组件的健壮性

使用步骤

1.  `npm i prop-types`

2. 导入 prop-types 包 `import PropTypes form 'prop-types'`

3. 使用组件名.propTypes = {} 来给组件的 props 添加校验规则

   ```jsx
   App.propTypes = {
       colors: PropTypes.array
   }
   // 对App中的 colors 进行了格式设置
   ```

约束规则：

- 常见类型：array、bool、func、number、object、string
- React元素类型：element
- 必填项：isRequired
- 特定结构的对象：shapr({})

```jsx
// 必选
requiredFunc: Proptypes.func.isRequired,
// 特定结构的对象
optionalObjectWithShape: PropTyps.shape({
    color:PropTypes.string,
    fontSize:Proptypes.number
})
```

配置默认值：

```jsx
// 指定 props 的默认值：
App.defaultProps = {
  name: 'Stranger'
};
```

---

## 生命周期

<font color="red">只有 类组件 才有生命周期</font>

[文档](https://projects.wojtekmaj.pl/react-lifecycle-methods-diagram/)

下面是常用的生命周期，详细的生命周期查看文档

![image-20221129094539813](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129094539813.png)

1. 挂载时

   - 执行时机：组件创建时即页面加载时

     ![image-20221129095339836](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129095339836.png)

     调用 setState 会导致递归更新

2. 更新时

   ![image-20221129101208763](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129101208763.png)

   图中的注意，如果直接调用 setState 更新状态，也会导致递归更新

   使用 `componentDidUpdat()`时应该传入参数 

   ```jsx
   // 第一个参数代表属性，第二个参数代表 state 
   componentDidUpdate(prevProps,prevState){
       // 按照具体情况对其两个使用if
   }
   ```

3. 卸载时

   执行时机：组件从页面中消失
   ![image-20221129110940539](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129110940539.png)

---

## render-props 和高阶组件

React 实现组件复用，复用的是什么 1.state 2.操作 state 的方法

两种方式：1.render props 模式，2.高阶组件（HOC‘

利用React自身特点的编码技巧，演化而成的固定模式

### render props 模式

- 如何拿到该组件中复用的state：在使用组件时添加一个值为函数的 prop，通过函数参数来获取（需要组件内部实现）

- 如何渲染任意的UI：使用该函数的返回值作为要渲染的UI内容（需要组件内部实现）

  <img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129113535549.png" alt="image-20221129113535549" style="zoom: 66%;" />

相当于子向父传值，上面是一种卸载标签内的形式，我们推荐使用 children 形式，上面有children的介绍

一个小案例：同时也是 children 的形式
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129134043650.png" alt="image-20221129134043650" style="zoom:50%;" />

推荐给 render props 模式添加 props 校验
```jsx
Mouse.PorpTypes = {
    children: PropTypes.func.isRequired
}
```

<font color="red">应该在组件卸载时解除这个组件的事件绑定一类的东西</font>

使用 Hook 模式

```tsx
const MouseMove = () => {
    const [x, setX] = useState<number>(0);
    const [y, setY] = useState<number>(0);
    const changeMouse = (e: MouseEvent) => {
        setY(e.clientY)
        setX(e.clientX)
    }
    useEffect(() => {
        window.addEventListener("mousemove", changeMouse)
        return () => {
            document.removeEventListener('mousemove', changeMouse)
        };
    })

    return {
        positionX: x,
        positionY: y
    }
}

const App = ()=>{
    return (
        <h1>鼠标的位置为：x:{MouseMove().positionX}  y:{MouseMove().positionY}</h1>
    )
}
```

---

### 高阶组件

[文档](https://zh-hans.reactjs.org/docs/higher-order-components.html#gatsby-focus-wrapper)

<font color="red">高阶组件是一个函数</font>，接受要包装的组件，返回增强后的组件

高阶组件内部<font color="red">创建一个类组件</font>，在这个类组件中<font color="red">提供复用的状态逻辑代码</font>，通过 prop 将复用的状态传递给被包装组件

使用步骤：

1. 创建一个函数，名称约定<font color="red">以 with 开头</font>

2. 指定函数参数，参数应该以大写字母开头（因为其是作为一个组件使用的

3. 在函数内部创建一个类组件，提供复用的状态逻辑代码，并返回

4. 在该组件中，渲染参数组件，同时将状态通过 prop 传递给参数组件

5. 调用该高阶组件，传入要增强的组件，通过返回值拿到增强后的组件，并将其渲染到页面中

   ![image-20221129173647659](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129173647659.png)

一个小案例：
<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221129192152512.png" alt="image-20221129192152512" style="zoom:50%;" />

使用高阶组件存在的问题：得到的多个组件名称相同，比如上如多次调用了高阶组件，组件的名字全都是 Mouse。

原因：默认情况下，React 使用组件名称作为 dispalyName 。

解决方式：为 高阶组件 设置 dispalyName 标语调试时区分不同的组件

还存在一种问题，具体看上边的图把

# 部分说明

## setState() 的说明

setState() 是异步更新的，使用该语法时后面的 setState 不要依赖前面的 setState；可以多次调用 setState，只会触发一次重新渲染。

推荐使用语法：`setState((state, props)=>{})`语法，state 表示<font color="red">最新</font>的 state，props 表示最新的 props

还有第二个参数（可选参数）：

当需要在状态更新（页面完成重新渲染后立即执行某个操作）

```jsx
// 一个参数的形式
this.setState((state, props)=>{
    return {
        count: state.count + 1
    }
})
// 两个参数的形式
this.setState((state,props)=>{},()=>{})
// 第二个参数我们 通常建议使用 componentDidUpdate() 来代替此方式。
```

setState 有两个作用：1.修改 state、2.更新组件（UI）

当父组件重新渲染时，也会重新渲染子组件。但只会渲染当前组件子树	

## 性能优化

**减轻 state**：<font color="red">只存储跟组件渲染相关的数据</font>，对于需要在多个方法中用到的数据，应该放在 this 中

**避免不必要的重新渲染**：父组件更新会引起子组件也被更新，如果子组件没有任何变化也会重新渲染，怎么避免呢？

使用钩子函数 `shouldComponentUpdate(nextProps, nextState)`，通过返回值决定该组件是否重新渲染，返回 true 表示重新渲染，false 表示不重新渲染

[钩子函数文档](https://zh-hans.reactjs.org/docs/optimizing-performance.html) 里面进行搜索这个函数名即可

```jsx
class Hello extends React.Component {
    // 两个参数都表示最新的状态
    shouldComponentUpdate(nextProps, nextState){
        // 根据条件，决定是否重新渲染组件
        return false;
    }
}
```

**纯组件**：PureComponent 与 Component 功能类似，但是其内部自动实现了 `shouldComponentUpdate` 钩子，不需要我们进行手动比较，原理是内部组件通过分别对比前后两次 props 和 state 的值，来决定是否重新渲染组件。但是它只进行浅比较即地址的比较，如果数据结构很复杂时，不能使用

---

# 虚拟 DOM 和 Diff 算法

- React 更新视图的思想是：只要 state 变化就重新渲染视图
- 特点：思路非常清晰
- 问题：组件中只有一个 DOM 元素需要更新是，也得把整个组件的内容重新渲染到页面中？不是的
- 理想状态：部分更新，只更新变化的地方
- 问题：React 是如何做到部分更新的？ <font color="red">虚拟DOM配合Diff算法</font>

---

虚拟DOM：本质上就是一个 JS 对象，用来描述你希望在屏幕上看到的内容（UI）

执行过程：

1. 初次渲染时，React 会根据初始 state (Model)，创建一个虚拟DOM对象（树）
2. 根据虚拟DOM生成真正的DOM，渲染到页面中
3. 当数据变化后（setState），重新根据新的数据，创建新的虚拟DOM对象（树）
4. 与上一次得到的虚拟DOM对象，使用<font color="red">Diff算法</font>对比（找不同），得到需要更新的内容。
5. 最终，React之江变化的内容更新（patch）到DOM中，重新渲染到页面

---

# 路由

## 基本使用

1. 安装：`npm i react-router-dom`

2. 导入路由的三个核心组件

   `import { BrowserRouter as Router, Route ,Link } form 'react-router-dom'`

3. 使用 Router 组件包裹整个应用（<font color="red">重要</font>)

4. 使用 Link 组件 作为导航菜单（路由入口） `<Link to="/first">页面一</Link>`

5. 使用 Route 组件配置路由规则和要展示的组件（路由出口）

   ![image-20221130101019817](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221130101019817.png)

---

**常用组件说明**

- Router 组件：包裹整个应用，一个 React 应用只需要使用一次

- LInk组件：用于指定导航连接（a标签

- Route 组件：指定路由展示组件相关信息

   path属性：路由规则
   component：展示的组件
   Route 组件写在哪，渲染出来的组件就展示在哪

---

**编程式导航**

通过JS代码实现页面跳转

history 是 React 路由提供的，用户获取浏览器历史记录的相关信息

push(path)：跳转到某个页面，参数 path 表示要跳转的路径

go(n)：表示向前或向后跳转到某个页面，n表示页面数量

```jsx
class Login extends Component {
    handleLogin = ()=>{
        // ...
        this.props.history.push("/home")
    }
    render(){...省略}
}
```

---

**默认路由**：表示进入页面时就会匹配的路由

默认路由path为：/  ，当进入的时候就会看到对应的组件

---

**模糊匹配模式**：React路由是模糊匹配模式。规则：只要以 pathname 以 path 开头就会匹配成功

```jsx
<Link to='/login'>登陆页面</Link>
<Route path='/' component={Home}/> 匹配成功
//login也会显示 默认路由的组件
```

| path   | 能够匹配的pathname           |
| ------ | ---------------------------- |
| /      | 所有的pathname               |
| /first | /first、/first/a、/first/a/b |

---

**精确匹配**：给 Route 组件添加 exact 属性，让其变为精确匹配模式。只有当 path 和 pathname 完全匹配时才会展示该路由

# Hooks



