首先需要安装 `npm install react-router-dom `

1. 引入 `createBrowerRouter` 方法和 `RouterProvider` 组件
2. 使用 `createBrowerRouter` 配置路由 path 和组件的对应关系生成 router 实例
3. 渲染 `RouterProvider` 组件并传入 router 实例

```jsx
// 实际开发中，应将路由抽离为一个单独的组件
import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Home/>,
    childred: [			// 嵌套路由，父路由中
       {
           index:true		// 父路由默认显示的页面
           element: <Son1/>
       }，
       {
          path:'',
          element: <Son2/>
       }
    ]
  },
  {
    path: '/login',
    element: <Login/>,		//此处使用的是组件，简单书写上面没有进行导入
  }
])

ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)
```

---

两种路由模式

> 概念: 前端路由由于实现方式的不同，常用的分成 history 路由和 hash 路由，分别由 `createBrowerRouter` 和 `createHashRouter` 创建

| 路由模式 | url 表现    | 底层原理                 | 是否需要后端支持 | 兼容性 |
| -------- | ----------- | ------------------------ | ---------------- | ------ |
| history  | url/login   | history 对象 + pushState | 需要             | ie10   |
| Hash     | url/#/login | 监听 hashchange 事件     | 不需要           | ie8    |

---

编程式导航实现

声明式导航使用 Link 标签即可

1. 导入一个 useNavigate 钩子函数
2. 执行 useNavigate 函数 得到 跳转函数
3. 在事件中执行跳转函数完成路由跳转

```jsx
import { useNavigate } from 'react-router-dom'
const Login = () => {
  const navigate = useNavigate()
  return (
    <div>
          // 点击就会跳转到 about 页面
      <button onClick={() => navigate('/about')}>go about</button>
    </div>
  )
}
export default Login
```

注: 如果在跳转时想要替换记录，可以添加额外参数 replace 为 true  `navigate('/', { replace: true })`

---

路由传参

> 场景：跳转路由的同时，有时候需要传递参数

1. searchParams 传参

   查询字符串传参的方式比较简单，参数的形式以问好拼接到地址后面

   路由传参

   ```jsx
   import { useNavigate } from 'react-router-dom'
   const Login = () => {
     const navigate = useNavigate()
     return (
       <div>
         <button onClick={() => navigate('/about?id=1001')}>go index</button>
       </div>
     )
   }
   export default Login
   ```

   路由取参

   ```jsx
   import { useSearchParams } from 'react-router-dom'
   
   const About = () => {
     const [params] = useSearchParams()
     let id = params.get('id')
     return <div>this is about {id}</div>
   }
   
   export default About
   ```

2. params传参

   params 方式传参，需要我们在路由表配置的位置添加一个 参数 占位

   **参数占位**

   ```jsx
   const router = createBrowserRouter([
     {
       path: '/about/:id',
       element: <About />,
     },
   ])
   
   export default router
   ```

   **路由传参**

   ```jsx
   import { useNavigate } from 'react-router-dom'
   const Login = () => {
     const navigate = useNavigate()
     return (
       <div>
         <button onClick={() => navigate('/about/1001')}>go index</button>
       </div>
     )
   }
   export default Login
   ```

   **路由取参**

   ```jsx
   import { useParams } from 'react-router-dom'
   const About = () => {
     const params = useParams()
     let id = params.id
     return <div>this is about {id}</div>
   }
   export default About
   ```

   

配置路由

```jsx
import ReactDOM from "react-dom/client";
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

const root = ReactDOM.createRoot(
  document.getElementById("root")
);
root.render(
  <BrowserRouter>	// 使用这个标签作为跟组件
    <Routes>		// 所有的 Route 都要包含在其中
      <Route path="/" element={<App />}>	// 指定路径和对应显示的组件
        <Route path="home" element={<Home />} />	// 子路由直接进行嵌套
        <Route path=":teams" element={<Teams />}>	// 动态匹配路径
      </Route>
    </Routes>
  </BrowserRouter>
);
```

当嵌套子路由的时候，子组件想渲染在哪里，就在父组件的哪里添加`<Outlet />` 作为子路由的出口

---

无匹配路由：

当页面跳转到我们没有定义的链接是，我们应该添加路由配置`<Route path="*" element={一个组件}></Route>` * 在这里有着特殊的意义，只有在没有其他路由匹配时

---

动态路径 URL参数：当我们想要获得 URL 中对应的动态参数时 
```jsx
import { useParams } from "react-router-dom";

export default function Invoice() {
  let params = useParams();	// 使用这个钩子函数来进行获取 动态路径
  return <h2>Invoice: {params.invoiceId}</h2>; // 这里使用的参数的key就是动态参数
}
```

---

索引路由：在子路由中添加属性 index 就成为了索引路由，下面是对于索引路由的答疑，需要去掉path属性，其实不去掉也可以，去掉 path 属性接的吧 link 组件中的修改掉

- 在父路由路径的出口出呈现索引路由
- 当父路由匹配但其他子路由都不匹配时，索引路由匹配。
- 索引路由是父路由的默认子路由。
- 当用户还没有单击导航列表中的项目之一时，索引路由会呈现。
- 索引路由不能够含有子路由

---

动态链接：将 Link 替换为 NavLink

```jsx
{invoices.map(invoice => (
    <NavLink
        style={({ isActive }) => {
            return {
                display: "block",
                margin: "1rem 0",
                color: isActive ? "red" : ""
            };
        }}
        to={`/invoices/${invoice.number}`}
        key={invoice.number}
    >
        {invoice.name}
    </NavLink>
))}
// 这是一个渲染多个链接的表达式
// 当我们点击到这个标签时，isActive 这个参数在 { isACtive } 是固定写法
// 也可以对 className 做同样的事情
```

----

--------

另一种形式

```jsx
const route = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "contact",
        element: <Contact />,
      },
      {
        path: "dashboard",
        element: <Dashboard />,
        loader: ({ request }) =>	
          // 路由加载器在路由渲染之前被调用，并通过useLoaderData为元素提供数据
          fetch("/api/dashboard.json", {
            signal: request.signal,
          }),
      },
      {
        element: <AuthLayout />,
        children: [
          {
            path: "login",
            element: <Login />,
            loader: redirectIfUser,
          },
          {
            path: "logout",
            action: logoutUser,
          },
        ],
      },
    ],
  },
]);
```

---

# createBrowserRouter

[文档](https://reactrouter.com/en/main/routers/create-browser-router)

建议所有 web 项目都是用 createBrowserRouter，其次是 createHashRouter

使用 [DOM History API](https://developer.mozilla.org/zh-CN/docs/Web/API/History) 来更新 URL 和 管理历史栈

```jsx
import * as React from "react";
import * as ReactDOM from "react-dom";
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

import Root, { rootLoader } from "./routes/root";
import Team, { teamLoader } from "./routes/team";

// 创建了路由对象
const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    loader: rootLoader, // 在渲染之前向路由元素提供数据，下面会进行
    children: [
      {
        // index:true; 这里是父路由中默认显示的子路由，如果使用此属性，建议不要再使用 path 属性
        path: "team",
        element: <Team />,
        loader: teamLoader,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <RouterProvider router={router} />
);
```

loader 属性：路由在渲染之前加载的加载器。当路由跳转时，loader 函数会异步的执行，并且在目标路由组件中可以获取到。

使用方式：
```jsx
// 路由的定义
{
    element:<Team/>,
    path:":teamId",
    loader: async ( {params} ) => { // params 是路由参数
        return axios.get("路径");
    }
}
// 组件的定义
const Team = ()=>{
    const res = useLoaderData(); // 使用这个钩子函数就能够得到异步请求到的数据
    ...
}
```

