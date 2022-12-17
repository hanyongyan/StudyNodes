# 使用 Less

```shell
npm i less -S
npm i less-loader -D
```

vite.config.js 中添加配置

```js
// 与 plugins 平级
css: {
    // css预处理器
    preprocessorOptions: {
        less: {
            charset: false,
            additionalData: "@import \"./src/css/base.less\";",
        },
    },
},
```

# 使用EsLint

[EsLint规则](https://eslint.bootcss.com/docs/rules/)

```shell
npm i eslint -D
npx eslint --init
```

然后选择你想要进行的配置即可

配置不需要进行检测的文件：直接新建 .eslintignore 文件，里面填入想要排除的文件即可，默认路径就是当前项目路径

# 配置路径别名

```js
// 与 plugins 平级
resolve:{
    // 配置路径别名
    alias:{
        // eslint-disable-next-line no-undef
        "@":path.resolve(__dirname,"src")
    }
},
```