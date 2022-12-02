# Vue3

自动引入组件的一个小插件 `unplugin-auto-import/vite`

[GitHub](https://github.com/antfu/unplugin-auto-import) 里面有详细的配置

1. 安装 `npm i -D unplugin-auto-import`
2. vite.config.ts
   
    ```jsx
    // 新增此组件
    import AutoImport from 'unplugin-auto-import/vite'
    plugins: [vue(),AutoImport({
        imports:['vue','vue-router'],
        dts:"src/auto-import.d.ts"
      })]
    // 这些是新增的
    import AutoImport from 'unplugin-auto-import/vite'
    AutoImport({
        // 这样进行设置，vue 和 vue-router 的就不用再进入了
        imports:['vue','vue-router'],
        // 会生成这个文件
        dts:"src/auto-import.d.ts"
      })
    // vscode 开发页面中如果显示报错
    eslint 中添加 "no-undef":"off"
    ```
    

配置完成之后使用ref reactive watch 等 无须import 导入 可以直接使用，

# **ref**

接受一个内部值，返回一个响应式的、可更改的 ref 对象，此对象只有一个指向其内部值的属性 `.value`

若要避免这种深层次的转换，请使用 **`[shallowRef()](https://cn.vuejs.org/api/reactivity-advanced.html#shallowref)`**来替代。

1. shallowRef  是浅层响应式的 如果仅仅修改 value 里面的元素是不会触发响应式的 `msg.value.property`
   
    除非对 value 整体进行修改 eg: `msg.value = {...}`
    

使用 ref 获取 DOM 元素 ：

```html
<p ref="dom"></p>
在 vue 文件中，使用 const dom = ref() 接受即可，此处的变量名要与标签里面的ref属性的名字一样，不过此时获取到以后 DOM 还没有渲染
```

如果需要将一个对象赋值给 ref ，那么这个对象**[reactive()](https://cn.vuejs.org/api/reactivity-core.html#reactive)**转为具有深层次响应式的对象。

reactive() 返回一个对象的响应式代理。响应式转换式深层的的：他会影响到所有嵌套的属性。

若要避免深层响应式转换，只想保留对这个对象顶层次访问的响应性，请使用 **[shallowReactive()](https://cn.vuejs.org/api/reactivity-advanced.html#shallowreactive)**
 作替代。

| ref | reactive |
| --- | --- |
| 支持所有的类型 | 支持引用类型 |
| 取值赋值都需要加value | 不需要value |

reactive 不能直接赋值，否则会破坏相应是对象，只能去修改

`readonly()` 接受一个对象 (不论是响应式还是普通的) 或是一个 **[ref](https://cn.vuejs.org/api/reactivity-core.html#ref)** ，返回一个原值的只读代理。

如果对原值进行了更改，那么此对象也会发生改变

---

1.  toRef() ：接受两个参数，第一个参数是一个对象，第二个参数式对象里面的一个属性
    
    作用：基于响应式对象上的一个属性，创建一个对应的 ref。这样创建的 ref 与其源属性保持同步：改变源属性的值将更新 ref 的值，反之亦然。
    
    `toRef()`这个函数在你想把一个 prop 的 ref 传递给一个组合式函数时会很有用：
    
2. toRefs
   
    作用：将一个响应式对象转换为一个普通对象，这个普通对象的每个属性都是指向源对象相应属性的 ref。每个单独的 ref 都是使用 **`[toRef()](https://cn.vuejs.org/api/reactivity-utilities.html#toref)`**
     创建的。
    
    使用场景：解构赋值
    
3. toRaw：根据一个 Vue 创建的代理返回其原始对象。
`toRaw()` 可以返回由 **`[reactive()](https://cn.vuejs.org/api/reactivity-core.html#reactive)`**、**`[readonly()](https://cn.vuejs.org/api/reactivity-core.html#readonly)`**、**`[shallowReactive()](https://cn.vuejs.org/api/reactivity-advanced.html#shallowreactive)`**或者 **`[shallowReadonly()](https://cn.vuejs.org/api/reactivity-advanced.html#shallowreadonly)`**创建的代理对应的原始对象。

---

# 计算属性 computed

[文档](https://cn.vuejs.org/api/reactivity-core.html#computed)

返回一个只读的响应式 **[ref](https://cn.vuejs.org/api/reactivity-core.html#ref)**对象。该 ref 通过 `.value`暴露 getter 函数的返回值。它也可以接受一个带有 `get`和 `set`函数的对象来创建一个可写的 ref 对象。

1.  示例一
    
    ```jsx
    const count = ref(1)
    const plusOne = computed(() => count.value + 1)
    
    console.log(plusOne.value) // 2
    
    plusOne.value++ // 错误
    ```
    
2. 示例二
   
    ```jsx
    const count = ref(1)
    const plusOne = computed({
      get: () => count.value + 1,
      set: (val) => {
        count.value = val - 1
      }
    })
    
    plusOne.value = 1
    console.log(count.value) // 0
    ```
    

我们可以向 `computed()` 传入第二个参数，是一个包含 `onTrack`和 `onTrigger` 两个回调函数的对象：

- `onTrack` 将在响应属性或引用作为依赖项被跟踪时被调用。
- `onTrigger` 将在侦听器回调被依赖项的变更触发时被调用。

```jsx
const plusOne = computed(() => count.value + 1, {
  onTrack(e) {
    // 当 count.value 被追踪为依赖时触发
    debugger
  },
  onTrigger(e) {
    // 当 count.value 被更改时触发
    debugger
  }
})

// 访问 plusOne，会触发 onTrack
console.log(plusOne.value)

// 更改 count.value，应该会触发 onTrigger
count.value++
```

# 侦听器 watch

[文档](https://cn.vuejs.org/api/reactivity-core.html#watch)

侦听一个或多个响应式数据源，并在数据源变化时调用所给的回调函数。

`watch()` 

1. 第一个参数
    - 一个函数，返回一个值
    
        比如说你想监听 reactive 中的一个属性，直接去监听该属性是不可以的，因为他不是 vue 所代理对象，就需要用到函数的形式 watch(()=> xxx.属性,(newVal,oldVal)=>{})
    
    - 一个 ref
    
        watch(xxx, (newVal, oldVal) =>{})
    
    - 一个响应式对象
        - 如果使用 ref 需要开启 deep 选项，reactive 则不需要开启 deep 选项
            - 这种情况应该开启深度监听 `watch(()=>{},{deep:true})` ,但是这种情况新值和旧值是相等的且是新值。 后面这个大括号内还有其他属性，文档进行查看即可，虽然reactive不需要开启 deep 选项但是新值和旧值依然是等价的
    
    - … 或是由以上类型的值组成的数组
    
2. 第二个参数是在发生变化时要调用的回调函数。这个回调函数接受三个参数：新值、旧值，以及一个用于注册副作用清理的回调函数。该回调函数会在副作用下一次重新执行前调用，可以用来清除无效的副作用，例如等待中的异步请求。

---

[watchEffect() ](https://cn.vuejs.org/api/reactivity-core.html#watcheffect)立即执行一个函数，同时响应式的追踪其依赖，并在依赖更改时重新执行。**

第一个参数就是要运行的副作用函数。这个副作用函数的参数也是一个函数。

第二个参数式一个可选的选项，可以用来调整副作用的刷新实际或调试副作用的依赖。

默认情况下：侦听器将在组件渲染只想执行。设置 `flush: "post"` 将会使侦听器延迟到组件渲染之后在执行，此种情况下有一个更方便的别名 `watchPostEffect()`。 在某些特殊情况下 (例如要使缓存失效)，可能有必要在响应式依赖发生改变时立即触发侦听器。这可以通过设置 `flush: 'sync'`
 来实现。然而，该设置应谨慎使用，因为如果有多个属性同时更新，这将导致一些性能和数据一致性的问题。

```jsx
// 监听谁就放进去
watchEffect(() => {
  console.log(msg.value);
  console.log(msg2.value);
});

// 第一个参数中放入一个函数
watchEffect((before) => {
  console.log(msg.value);
  console.log(msg2.value);
	// 当监听到数据变化时，下面这个函数中的会先运行，可以利用此机制先做一些事情
	before(()=>{})
});

// 停止侦听器的用法
const stop = watchEffect(() => {})
stop() // 当不再需要此侦听器时:

// 配置 flush 选项
const stop = watchEffect(() => {},{ flush: "post"})
// flush 有三个配置
// pre 代表组件更新前执行， sync 便是强制效果始终同步触发， post组件更新后执行

```

watch 和 watchEffect 和 [计算属性 computed](Vue3%201ca88b6dc6eb4b6b96afce718513fad9.md) 类似，侦听器也支持 `onTarck`和 `onTrigger`

```jsx
watch(source, callback, {
  onTrack(e) {
    debugger
  },
  onTrigger(e) {
    debugger
  }
})

watchEffect(callback, {
  onTrack(e) {
    debugger
  },
  onTrigger(e) {
    debugger
  }
})
```

---

组件的使用和 vue2 的语法不一样，使用 import 导入以后直接使用 组件即可

---

# 生命周期

beforeCreate  created setup语法糖模式没有这两个生命周期， setup 去代替了这两个生命周期

```vue
// 创建之前 读取不到 DOM
onBeforeMount(()=>{})
// 创建完成  可以读取DOM
onMounted(()=>{})
// 更新之前  获取的是更新之前的 DOM
onBeforeUpdate(()=>{})
// 更新完成  获取的是更新之后的 DOM
onUpdata(()=>{})
// 卸载之前
onBeforeUnmount(()=>{})
// 销毁完成
onUnmounted(()=>{})
```

---

向父组件中暴露属性：

```jsx
// 此组件为 menu
const a = 1;
const b = ref(2);
defineExpose({
	a,
	b
})
// 下面是父组件
<Menu ref = 'dom'></Menu>
<script setup>
// 获取了 dom 示例，里面就存有子组件中暴漏出来二
const dom = ref();
</script>
```



## 子向父暴露方法或属性

```vue
// 子组件 Son
defineExpose({
	 // 属性名是为了在父子间中进行调用，可以传函数也可以传对象
	// 如果暴漏的是一个ref或reactive对象，只会传递初始的值，无论此对象的值如何变化
	// 如果传递的函数中调用了自身组件的ref，是正常进行调用的
	name:"xxx"
	fn:()=>{} //
})
// 父组件
<Son ref="son"></Son>  // ref 的名字自定义

const son = ref() // 名称要与组件中 ref 的名字一样
son.value.name // 即可调用暴漏的属性
```



# 全局组件

在main.ts中

```typescript
import xxx from 'xxx'			// 先导入组件
app.component("xxx",xxx)		// 进行注册组件，并定义全局组件的名字
```

---

# 动态组件

在 template 标签内使用 component 标签 使用 :is 属性去绑定标签

[视频](https://www.bilibili.com/video/BV1dS4y1y7vd?p=18&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

# 异步组件

[看视频](https://www.bilibili.com/video/BV1dS4y1y7vd?p=20&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

可以使用异步组件去优化我们的项目，防止第一次加载时间过长

[官网文档：](https://cn.vuejs.org/guide/components/async.html#async-components)

顶层 await：
在 setup 语法糖里，使用方法 <scrpt setup> 中可以指使用顶层 await。结果代码会被编译成 `async setup()`  

父组件引用子组件，通过使用 defineAsyncComponent 加载异步配合 import 函数模式便可以分包

```jsx
<script setup lang="ts">
import { reactive, ref, markRaw, toRaw, defineAsyncComponent } from 'vue'
 
const Dialog = defineAsyncComponent(() => import('../../components/Dialog/index.vue'))
```

这样子组件中的数据才能够正常显示，必须使用 Suspense 组件

```html
<Suspense>
	<!--正常显示的数据-->
	<template #default>
		<Dialog></Dialog>
  </template>
 <!--当数据没有完成同步时，暂时显示的数据-->
	<template #fallback>
		<div>loading...</div>
	</template>
</Suspense>
```

---

# Teleport

[文档](https://cn.vuejs.org/guide/built-ins/teleport.html#teleport)

[视频](https://www.bilibili.com/video/BV1dS4y1y7vd?p=21&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

`<Teleport>` 是一个内置组件，它可以将一个组件内部的一部分模板“传送”到该组件的 DOM 结构外层的位置去。

有时候我们可能会遇到这样的场景：一个组件模板的一部分在逻辑上从属于该组件，但从整个应用试图的角度来看，他在 DOM 中应该被渲染在整个 Vue 应用外部的其他地方 

```html
<!--把 A 组件渲染到 parent 中-->
<div class="parent">
	<A></A>
</div>
------
<!--把 A 组件渲染的与 body 平级-->
<body>
	<div class="parent">
		<!--这个 to 可以接受多种选择器 -->
		<!-- disabled 接受 布尔值 如果使用 true，to 属性将不会生效-->
		<!-- disabled 这个属性不写时默认为 false -->
		<Teleport to="body" :disabled="false">
			<A></A>
		</Teleport>
	</div>
</body>
```

---

# Transition组件基本使用

[官网文档](https://cn.vuejs.org/guide/built-ins/transition.html#transition)

来设置组件消失或者出现时的展示动画，使其展示和消失不那么突兀，

可以结合 **[Animate.css](https://animate.style/) 来使用**   [视频](https://www.bilibili.com/video/BV1dS4y1y7vd/?p=24&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

如果要来使用类似 animate 的框架，应该使用文档里面的自定义过渡 class

你可以通过向 `<Transition>`组件传入 `duratio`  prop 来显式指定过渡的持续时间 (以毫秒为单位)。总持续时间应该匹配延迟加上内部元素的过渡持续时间：

```html
<!--如果有必要的话，你也可以用对象的形式传入，分开指定进入和离开所需的时间：-->
<Transition :duration="{ enter: 500, leave: 800 }">...</Transition>
```

你可以通过监听 `<Transition>`组件事件的方式在过渡过程中挂上钩子函数：看文档中的 JavaScript 钩子 使用

## TransitionGroup

[文档](https://cn.vuejs.org/guide/built-ins/transition-group.html)

`<TransitionGroup>`是一个内置组件，用于对 `v-for`列表中的元素或组件的插入、移除和顺序改变添加动画效果。

用法与 Transition 一致

---

# 依赖注入

[文档](https://cn.vuejs.org/api/composition-api-dependency-injection.html#composition-api-brdependency-injection)

能够完成父子组件之间的传值，也能够在子组件中同步修改传入的值，和 props 是不同的， props 中的值在子组件中修改时会警告的。如果不想被子组件更改的话 应该使用 readonly 包裹

## provide()

提供一个值，可以被后代组件注入，即被后代组件使用。

第一个参数应当是注入的 key，第二个参数则是提供的值。

```jsx
// 提供静态值
provide('foo', 'bar')

// 提供响应式的值
const count = ref(0)
provide('count', count)
```

## inject()

注入一个由祖先组件或整个应用 (通过 `app.provide()`) 提供的值。可以传入两个参数

```jsx
// 注入值的默认方式，此处的 foo 可以是祖先组件传来的响应式或非响应式
const foo = inject('foo')
// 注入一个值，若为空则使用提供的默认值
const bar = inject('foo', 'default value')
// 注入一个值，若为空则使用提供的工厂函数
const baz = inject('foo', () => new Map())
// 注入时为了表明提供的默认值是个函数，需要传入第三个参数
const fn = inject('function', () => {}, false)
```

---

## 父向子传值

关键函数 defineProps

```jsx
// 父组件
<Son :title="传入一个ref对象或者任意类型对象即可"></Son>
// 子组件
const props = defineProps<{ title: string }>(); 
// 通过 props.title 进行使用传来的内容，当然使用 title 也行，建议使用下面的
defineProps<{ title: string }>(); // 使用 title 进行调用传来的内容
// 里面进行设置是因为默认类型中没有这样的属性
// 如果需要对参数设置默认值
    withDefaults(defineProps<{ title: string }>(),{title: ()=> {} })
    后面进行返回是防止出现引用的qing'kuang
// 上面是 TS 的写法
// 下面是 JS 的写法
const props = defaultProps({
    title: {
    	type: 类型，
    	default: 设置默认值
    }
})
```

---

## 子向父传值

关键函数 defineEmits

```jsx
// JS 的写法
// 子组件 Son
const emit = defineEmits(['on-click']) // 多个就在数组中定义多个
const send = ()=>{
	emit('on-click','你好') 
	// 后面可以传入多个参数，此处传入几个参数，父组件的调用函数中就要传入几个参数
}
// 父组件 
<Son @on-click='getProps'></Son>	// 函数名自定义，点击时就自动触发该函数，并且传入参数
const getProps = (prop1...)=>{}  // name 是子组件传来的参数

// 下面是 TS 的写法
const emit = defineEmits<{
  (e: "on-click", name: string): void;
  // 此处相当于定义了 on-click 的函数，第二个参数及以后为对函数进行的传参， 返回值为 void
  (e: "on-change", name: number): void;
}>();
// 父组件

<Son @on-click='getProps'></Son>	// 函数名自定义，点击时就自动触发该函数，并且传入参数
const getProps = (prop1...)=>{}  // name 是子组件传来的参数
```

---

## 兄弟组件之间传值

兄弟组件之间传参是使父组件充当桥梁，先传递给父组件，再传递兄弟组件，具体看上面父子传参，就是比较麻烦。

下面的是封装好的简单使用的一个组件

1.  `npm i mitt -S`
2. main.ts 初始化
   
    ```jsx
    import { createApp } from 'vue'
    import App from './App.vue'
    import mitt from 'mitt'
    
    const Mit = mitt()
    
    //TypeScript注册
    // 由于必须要拓展ComponentCustomProperties类型才能获得类型提示
    declare module "vue" {
        export interface ComponentCustomProperties {
            $Bus: typeof Mit
        }
    }
    
    const app = createApp(App)
    
    //Vue3挂载全局API
    app.config.globalProperties.$Bus = Mit
    
    app.mount('#app') 
    //作者：小满zs https://www.bilibili.com/read/cv16107098?spm_id_from=333.999.0.0 出处：bilibili
    ```
    
3. 使用方法通过emit派发， on 方法添加事件，off 方法移除，clear 清空所有
   
    ```jsx
    // A 组件派发
    <template>
        <div>
            <h1>我是A</h1>
            <button @click="emit1">emit1</button>
            <button @click="emit2">emit2</button>
        </div>
    </template>
    
    <script setup lang='ts'>
    import { getCurrentInstance } from 'vue'
    const instance = getCurrentInstance();
    const emit1 = () => {
        instance?.proxy?.$Bus.emit('on-num', 100)
    }
    const emit2 = () => {
            // * 代表监听所有的事件触发！
        instance?.proxy?.$Bus.emit('*', 500)
    }
    </script>
    
    <style>
    </style> 
    //作者：小满zs https://www.bilibili.com/read/cv16107098?spm_id_from=333.999.0.0 出处：bilibili
    ```
    
    ```jsx
    // B组件监听（on）
    <template>
        <div>
            <h1>我是B</h1>
        </div>
    </template>
    
    <script setup lang='ts'>
    import { getCurrentInstance } from 'vue'
    const instance = getCurrentInstance()
    instance?.proxy?.$Bus.on('on-num', (num) => {
        console.log(num,'===========>B')
    })
    
    // * 监听所有事件， type 代表 是什么事件触发的
    instance?.proxy?.$Bus.on('*',(type,num)=>{
        console.log(type,num,'===========>B')
    })
        
    // 移除监听事件
    const Fn = (num: any) => {
        console.log(num, '===========>B')
    }
    instance?.proxy?.$Bus.on('on-num',Fn)//listen
    instance?.proxy?.$Bus.off('on-num',Fn)//unListen 
    // 清空所有监听
    instance?.proxy?.$Bus.all.clear() 
    </script>
    
    <style>
    </style> 
    ```
    

---

# TSX

安装插件：`npm install @vitejs/plugin-vue-jsx -D`

vite.config.ts 配置

```ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx';
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(),vueJsx()]
})
```

修改 tsconfig.json 配置文件

```json
   // 在 compilerOptions 节点中
	"jsx": "preserve",
    "jsxFactory": "h",
    "jsxFragmentFactory": "Fragment",
```

完成上面的配置就可以使用 TSX 了。

[vue 部分语法的使用](https://xiaoman.blog.csdn.net/article/details/123172735)

[视频介绍](https://www.bilibili.com/video/BV1dS4y1y7vd/?p=33&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

# 深入 v-model

[视频](https://www.bilibili.com/video/BV1dS4y1y7vd/?p=35&spm_id_from=pageDriver&vd_source=e676186ca320a2feb94cdce6f31b5c9a)

我们可以对组件使用 v-model

```jsx
// 父组件使用子组件  
<Son v-model="isShow"></Son>
// 子组件中进行接收 与 修改
defineProps<{
    // 固定写法
    modelValue: 数据类型 // 就可以使用了
}>
// 数组里面的内容是固定写法
const emit = defineEmits(['update:modelValue'])
const fn = ()=>{
    emit('update:modelValue',修改后的数据)
    // 组件中调用即可完成 修改父组件 v-model 传来的值，不需要在父组件中进行接受
}
```

多 v-model 用法

```jsx
// 父组件
<Son v-model="isShow" v-model:textVal="text"></Son>
// 子组件中进行接受与修改
defineProps<{
    modelValue: 数据类型,		// 对应 v-model 传来的
    textVal: 数据类型				// 对应 v-model: 冒号后面传来的
}>
const emit = defineEmits(['update:modelValue',"update:textVal"])
const fn = ()=>{
    emit('update:textVal',修改后的数据)
}
```

# 自定义指令

[别人的博客](https://xiaoman.blog.csdn.net/article/details/123228132)

有一个限制：必须以 `vNameOfDirective` 的形式来命名本地自定义指令，以使得他们可以直接在模板中进行使用。

```jsx
// 自定义指令
const vMove: Directive = {
	// 下面是指令的生命周期
  created: () => {
    console.log("初始化====>");
  },
  beforeMount(...args: Array<any>) {
    // 在元素上做些操作
    console.log("初始化一次=======>");
  },
  mounted(el: any, dir: DirectiveBinding<Value>) {
    el.style.background = dir.value.background;
    console.log("初始化========>");
  },
  beforeUpdate() {
    console.log("更新之前");
  },
  updated() {
    console.log("更新结束");
  },
  beforeUnmount(...args: Array<any>) {
    console.log(args);
    console.log("======>卸载之前");
  },
  unmounted(...args: Array<any>) {
    console.log(args);
    console.log("======>卸载完成");
  },
};
// 自定义指令的使用
<p v-mode:aaa.im="{background:'red'}"</p>
// v-mode 代表了自定义指令，aaa代表绑定了什么，im表示一个操作 引号内的内容表示传入的参数
```

![image-20221126231818663](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20221126231818663.png)

函数简写

你可能想在 `mounted` 和 `updated` 时触发相同行为，而不关心其他的钩子函数。那么你可以通过将这个函数模式实现

```tsx
// 定义了一个类型
type Dir = {
   background: string
}
// binding 的类型本来就是 DirectiveBinding 后面添加泛型<Dir> 是为了能够获取到value中的值
const vMove: Directive = (el, binding: DirectiveBinding<Dir>) => {
   el.style.background = binding.value.background }
```

---

# 定义全局函数和变量

vue3 在 `main.js` 或 `main.ts` 中使用 `app.config.globalProperties` 去定义变量和函数

定义函数和变量之前

```tsx
// 声明要扩充@vue/runtime-core包的声明.
// 这里扩充"ComponentCustomProperties"接口, 因为他是vue3中实例的属性的类型.
// 不添加这个其实并不影响使用，只不过会在编辑器页面内爆红
  declare module '@vue/runtime-core' {
    export interface ComponentCustomProperties {
	       自定义属性或函数的名字: 类型
					// 类型是 ts 中所需要写的
    }
}
// 比如下面这样
declare module '@vue/runtime-core' {
  export interface ComponentCustomProperties {
    name: string,
    $filters: Filter,
  }
}
// 定义了一个类型
type Filter = {
    format<T>(str:T):string
}

// 其实单一下面这一行代码就能够解决，添加上面的代码就是为了取消报错
app.config.globalProperties.name = "HanYongYan"
app.config.globalProperties.$filters = {
    // 这是里面进行定义了一个函数
    format<T> (str:T){
        return `xx${str}`
    }
}
// 定义好的函数或者变量在vue文件中如何使用呢？
// 在 template 标签中直接使用插值表达式即可使用
// 在script中 shi'yong'ru
import {getCurrentInstance} form 'vue'
const app = getCurrentInstance()
console.log(app?.proxy?.name)
```

---

# 全局配置 axios

```tsx
// main.ts 中使用如下配置
import axios from 'axios'
// 可在下面这一句之前对 axios 进行一系列的设置，
// 比如设置请求根路径 axios.defaults.baseURL = "http://1.15.45.111:8080/";
app.config.globalProperties.axios= axios

// 下面是组件的使用
// 此处得到的 proxy 相当于是 this
// 使用 ts 的话 proxy 可能会爆红 不过没有关系不影响
const { proxy } = getCurrentInstance()
proxy.$http.get('api/getNewsList')
.then((response)=>{
    console.log(response)
})
```

# Transition

[文档](https://cn.vuejs.org/guide/built-ins/transition.html)

vue 提供了两个内置组件，可以帮助你制作基于状态变化的过度和动画

- `<Transition>` 会在一个元素或组件进入和离开 DOM 时应用动画。
- `<TransitionGroup>`会在一个 `v-for`列表中的元素或组件被插入，移动，或移除时应用动画。

# Teleport

[文档](https://cn.vuejs.org/guide/built-ins/teleport.html)

`<Teleport>`是一个内置组件，它可以将一个组件内部的一部分模板“传送”到该组件的 DOM 结构外层的位置去。

有时我们可能会遇到这样的场景：一个组件模板的一部分在逻辑上从属于该组件，但从整个应用视图的角度来看，它在 DOM 中应该被渲染在整个 Vue 应用外部的其他地方。

# Suspense

[文档](https://cn.vuejs.org/guide/built-ins/suspense.html)

**是一项实验性功能。它不一定会最终成为稳定功能，并且在稳定之前相关 API 也可能会发生变化。**

`<Suspense>` 是一个内置组件，用来在组件树中协调对异步依赖的处理。它让我们可以在组件树上层等待下层的多个嵌套异步依赖项解析完成，并可以在等待时渲染一个加载状态。

# nextTick()

当你在 Vue 中更改响应式状态时，最终的 DOM 更新并不是同步生效的，

```java
// 示例
<script setup>
import { ref, nextTick } from 'vue'

const count = ref(0)

async function increment() {
  count.value++

  // DOM 还未更新
  console.log(document.getElementById('counter').textContent) // 0

  await nextTick()
  // DOM 此时已经更新
  console.log(document.getElementById('counter').textContent) // 1
}
</script>

<template>
  <button id="counter" @click="increment">{{ count }}</button>
</template>
```

# 环境变量	