🤤基础篇

- 认识微服务
- 服务拆分
- 远程调用
- Eureka
- Ribbon
- Nacos
- Feign
- Gateway
- RabbitMQ
- Elasticsearch

💻高级篇

- JMeter
- Sentinel
- Seata
- Redis
- 。。。

# 认识微服务

## 单体架构

**单体架构**：将业务的所有功能集中在一个项目中开发，打成一个包部署。

![](https://cdn.xn2001.com/img/2021/20210901083809.png)

**优点**：架构简单，部署成本低

**缺点**：耦合度高（维护困难、升级困难）

## 分布式架构

**分布式架构**：根据业务功能对系统做拆分，每个业务功能模块作为独立项目开发，称为一个服务。

![](https://cdn.xn2001.com/img/2021/20210901092921.png)

**优点**：降低服务耦合，有利于服务升级和拓展

**缺点**：服务调用关系错综复杂

分布式架构虽然降低了服务耦合，但是服务拆分时也有很多问题需要思考：

- 服务拆分的粒度如何界定？
- 服务之间如何调用？
- 服务的调用关系如何管理？

**人们需要制定一套行之有效的标准来约束分布式架构。**

## 微服务

微服务的架构特征：

- 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务能力，做到单一职责
- 自治：团队独立、技术独立、数据独立，独立部署和交付
- 面向服务：服务提供统一标准的接口，与语言和技术无关
- 隔离性强：服务调用做好隔离、容错、降级，避免出现级联问题

![](https://cdn.xn2001.com/img/2022/202205162352847.png)

微服务的上述特性**其实是在给分布式架构制定一个标准**，进一步降低服务之间的耦合度，提供服务的独立性和灵活性。做到高内聚，低耦合。

**因此，可以认为微服务是一种经过良好架构设计的分布式架构方案 。**

其中在 Java 领域最引人注目的就是 SpringCloud 提供的方案了。

## SpringCloud

SpringCloud 是目前国内使用最广泛的微服务框架。官网地址：https://spring.io/projects/spring-cloud。

SpringCloud 集成了各种微服务功能组件，并基于 SpringBoot 实现了这些组件的自动装配，从而提供了良好的开箱即用体验。

其中常见的组件包括：

![](https://cdn.xn2001.com/img/2021/20210901083717.png)

另外，SpringCloud 底层是依赖于 SpringBoot 的，并且有版本的兼容关系，如下：

![](https://cdn.xn2001.com/img/2021/20210901084050.png)

## 内容知识

![需要学习的微服务知识内容](https://cdn.xn2001.com/img/2021/20210901092925.png)

![技术栈](https://cdn.xn2001.com/img/2021/20210901084131.png)

![自动化部署](https://cdn.xn2001.com/img/2021/20210901090737.png)

## 技术栈对比

![](https://cdn.xn2001.com/img/2021/20210901090726.png)

# 服务拆分

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/01-cloud-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/01-cloud-demo

**服务拆分注意事项**

单一职责：不同微服务，不要重复开发相同业务

数据独立：不要访问其它微服务的数据库

面向服务：将自己的业务暴露为接口，供其它微服务调用

![](https://cdn.xn2001.com/img/2021/20210901090745.png)

cloud-demo：父工程，管理依赖

- order-service：订单微服务，负责订单相关业务
- user-service：用户微服务，负责用户相关业务

要求：

- 订单微服务和用户微服务都必须有**各自的数据库**，相互独立
- 订单服务和用户服务**都对外暴露 Restful 的接口**
- 订单服务如果需要查询用户信息，**只能调用用户服务的 Restful 接口**，不能查询用户数据库

微服务项目下，打开 idea 中的 Service，可以很方便的启动。

![](https://cdn.xn2001.com/img/2021/20210901090750.png)

启动完成后，访问 [http://localhost:8080/order/101](http://localhost:8080/order/101)

![](https://cdn.xn2001.com/img/2021/20210901090757.png)

# 远程调用

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/02-cloud-restTemplate
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/02-cloud-restTemplate

正如上面的服务拆分要求中所提到，

> 订单服务如果需要查询用户信息，**只能调用用户服务的 Restful 接口**，不能查询用户数据库

因此我们需要知道 Java 如何去发送 http 请求，Spring 提供了一个 RestTemplate 工具，只需要把它创建出来即可。（即注入 Bean）

![](https://cdn.xn2001.com/img/2021/20210901090814.png)

发送请求，自动序列化为 Java 对象。

![](https://cdn.xn2001.com/img/2021/20210901090846.png)

启动完成后，访问：http://localhost:8080/order/101

![](https://cdn.xn2001.com/img/2021/20210901090909.png)

在上面代码的 url 中，我们可以发现调用服务的地址采用硬编码，这在后续的开发中肯定是不理想的，这就需要**服务注册中心**（Eureka）来帮我们解决这个事情。

# Eureka注册中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/03-cloud-eureka
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/03-cloud-eureka

最广为人知的注册中心就是 Eureka，其结构如下：

![](https://cdn.xn2001.com/img/2021/20210901090919.png)

**order-service 如何得知 user-service 实例地址？**

- user-service 服务实例启动后，将自己的信息注册到 eureka-server(Eureka服务端)，叫做**服务注册**
- eureka-server 保存服务名称到服务实例地址列表的映射关系
- order-service 根据服务名称，拉取实例地址列表，这个叫**服务发现**或服务拉取

**order-service 如何从多个 user-service 实例中选择具体的实例？**

order-service从实例列表中利用**负载均衡算法**选中一个实例地址，向该实例地址发起远程调用

**order-service 如何得知某个 user-service 实例是否依然健康，是不是已经宕机？**

- user-service 会**每隔一段时间(默认30秒)向 eureka-server 发起请求**，报告自己状态，称为**心跳**
- 当超过一定时间没有发送心跳时，eureka-server 会认为微服务实例故障，将该实例从服务列表中剔除
- order-service 拉取服务时，就能将故障实例排除了

---

接下来我们动手实践的步骤包括

![](https://cdn.xn2001.com/img/2021/20210901090932.png)

## 搭建注册中心

**搭建 eureka-server**

引入 SpringCloud 为 eureka 提供的 starter 依赖，注意这里是用 **server**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**编写启动类**

注意要添加一个 `@EnableEurekaServer` **注解**，开启 eureka 的**注册中心**功能

```java
package com.xn2001.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

**编写配置文件**

编写一个 application.yml 文件，内容如下：

```yml
server:
  port: 10086
spring:
  application:
    name: eureka-server
eureka:
  client:
    service-url: 
      defaultZone: http://127.0.0.1:10086/eureka
```

其中 `default-zone` 是因为前面配置类开启了注册中心所需要配置的 eureka 的**地址信息**，因为 eureka 本身也是一个微服务，这里也要将自己注册进来，当后面 eureka **集群**时，这里就可以填写多个，使用 “,” 隔开。

启动完成后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901090945.png)

## 服务注册

> 将 user-service、order-service 都注册到 eureka

引入 SpringCloud 为 eureka 提供的 starter 依赖，注意这里是用 **client**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

在启动类上添加注解：`@EnableEurekaClient`

在 application.yml 文件，添加下面的配置：

```yml
spring:
  application:
  	#name：orderservice
    name: userservice
eureka:
  client:
    service-url: 
      defaultZone: http:127.0.0.1:10086/eureka
```

3个项目启动后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901090958.png)

这里另外再补充个小技巧，我们可以通过 idea 的多实例启动，来查看 Eureka 的集群效果。

![](https://cdn.xn2001.com/img/2021/20210901091005.png)

4个项目启动后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901091015.png)

## 服务拉取

> 在 order-service 中完成服务拉取，然后通过负载均衡挑选一个服务，实现远程调用

下面我们让 order-service 向 eureka-server 拉取 user-service 的信息，实现服务发现。

首先给 `RestTemplate` 这个 Bean 添加一个 `@LoadBalanced` **注解**，用于开启**负载均衡**。（后面会讲）

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate(){
    return new RestTemplate();
}
```

修改 OrderService 访问的url路径，用**服务名**代替ip、端口：

![](https://cdn.xn2001.com/img/2021/20210901091216.png)

spring 会自动帮助我们从 eureka-server 中，根据 userservice 这个服务名称，获取实例列表后去完成负载均衡。

# Ribbon负载均衡

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/04-cloud-ribbon
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/04-cloud-ribbon

我们添加了 `@LoadBalanced` 注解，即可实现负载均衡功能，这是什么原理呢？

**SpringCloud 底层提供了一个名为 Ribbon 的组件，来实现负载均衡功能。**

![](https://cdn.xn2001.com/img/2021/20210901091242.png)

## 源码跟踪

为什么我们只输入了 service 名称就可以访问了呢？为什么不需要获取ip和端口，这显然有人帮我们根据 service 名称，获取到了服务实例的ip和端口。它就是`LoadBalancerInterceptor`，这个类会在对 RestTemplate 的请求进行拦截，然后从 Eureka 根据服务 id 获取服务列表，随后利用负载均衡算法得到真实的服务地址信息，替换服务 id。

我们进行源码跟踪：

![](https://cdn.xn2001.com/img/2021/20210901091323.png)

这里的 `intercept()` 方法，拦截了用户的 HttpRequest 请求，然后做了几件事：

- `request.getURI()`：获取请求uri，即 http://user-service/user/8
- `originalUri.getHost()`：获取uri路径的主机名，其实就是服务id `user-service`
- `this.loadBalancer.execute()`：处理服务id，和用户请求

这里的 `this.loadBalancer` 是 `LoadBalancerClient` 类型

继续跟入 `execute()` 方法：

![](https://cdn.xn2001.com/img/2021/20210901091330.png)

- `getLoadBalancer(serviceId)`：根据服务id获取 `ILoadBalancer`，而 `ILoadBalancer` 会拿着服务 id 去 eureka 中获取服务列表。
- `getServer(loadBalancer)`：利用内置的负载均衡算法，从服务列表中选择一个。在图中**可以看到获取了8082端口的服务**

可以看到获取服务时，通过一个 `getServer()` 方法来做负载均衡:

 ![](https://cdn.xn2001.com/img/2021/20210901091345.png)

我们继续跟入：

![](https://cdn.xn2001.com/img/2021/20210901091355.png)

继续跟踪源码 `chooseServer()` 方法，发现这么一段代码：

 ![](https://cdn.xn2001.com/img/2021/20210901091414.png)

我们看看这个 `rule` 是谁：

 ![](https://cdn.xn2001.com/img/2021/20210901091432.png)

这里的 rule 默认值是一个 `RoundRobinRule` ，看类的介绍：

![](https://cdn.xn2001.com/img/2021/20210901091442.png)

负载均衡默认使用了轮训算法，当然我们也可以自定义。

## 流程总结

SpringCloud Ribbon 底层采用了一个拦截器，拦截了 RestTemplate 发出的请求，对地址做了修改。

基本流程如下：

- 拦截我们的 `RestTemplate` 请求 http://userservice/user/1
- `RibbonLoadBalancerClient` 会从请求url中获取服务名称，也就是 user-service
- `DynamicServerListLoadBalancer` 根据 user-service 到 eureka 拉取服务列表
- eureka 返回列表，localhost:8081、localhost:8082
- `IRule` 利用内置负载均衡规则，从列表中选择一个，例如 localhost:8081
- `RibbonLoadBalancerClient` 修改请求地址，用 localhost:8081 替代 userservice，得到 http://localhost:8081/user/1，发起真实请求

![](https://cdn.xn2001.com/img/2021/20210901091755.png)

## 负载均衡策略

负载均衡的规则都定义在 IRule 接口中，而 IRule 有很多不同的实现类：

![](https://cdn.xn2001.com/img/2021/20210901091811.png)

不同规则的含义如下：

| **内置负载均衡规则类**    | **规则描述**                                                 |
| ------------------------- | ------------------------------------------------------------ |
| RoundRobinRule            | 简单轮询服务列表来选择服务器。它是Ribbon默认的负载均衡规则。 |
| AvailabilityFilteringRule | 对以下两种服务器进行忽略：（1）在默认情况下，这台服务器如果3次连接失败，这台服务器就会被设置为“短路”状态。短路状态将持续30秒，如果再次连接失败，短路的持续时间就会几何级地增加。 （2）并发数过高的服务器。如果一个服务器的并发连接数过高，配置了AvailabilityFilteringRule 规则的客户端也会将其忽略。并发连接数的上限，可以由客户端设置。 |
| WeightedResponseTimeRule  | 为每一个服务器赋予一个权重值。服务器响应时间越长，这个服务器的权重就越小。这个规则会随机选择服务器，这个权重值会影响服务器的选择。 |
| **ZoneAvoidanceRule**     | 以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。 |
| BestAvailableRule         | 忽略那些短路的服务器，并选择并发数较低的服务器。             |
| RandomRule                | 随机选择一个可用的服务器。                                   |
| RetryRule                 | 重试机制的选择逻辑                                           |

默认的实现就是 `ZoneAvoidanceRule`，**是一种轮询方案**。

## 自定义策略

通过定义 IRule 实现可以修改负载均衡规则，有两种方式：

1 代码方式在 order-service 中的 OrderApplication 类中，定义一个新的 IRule：

![](https://cdn.xn2001.com/img/2021/20210901091832.png)

2 配置文件方式：在 order-service 的 application.yml 文件中，添加新的配置也可以修改规则：

```yml
userservice: # 给需要调用的微服务配置负载均衡规则，orderservice服务去调用userservice服务
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则 
```

**注意**：一般用默认的负载均衡规则，不做修改。

## 饥饿加载

当我们启动 orderservice，第一次访问时，时间消耗会大很多，这是因为 Ribbon 懒加载的机制。

![](https://cdn.xn2001.com/img/2021/20210901091850.png)

Ribbon 默认是采用懒加载，即第一次访问时才会去创建 LoadBalanceClient，拉取集群地址，所以请求时间会很长。

而饥饿加载则会在项目启动时创建 LoadBalanceClient，降低第一次访问的耗时，通过下面配置开启饥饿加载：

```yml
ribbon:
  eager-load:
    enabled: true
    clients: userservice # 项目启动时直接去拉取userservice的集群，多个用","隔开
```

# Nacos注册中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/05-cloud-nacos
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/05-cloud-nacos

SpringCloudAlibaba 推出了一个名为 Nacos 的注册中心，在国外也有大量的使用。

![](https://cdn.xn2001.com/img/2021/20210901091857.png)

解压启动 Nacos，详细请看 [Nacos安装指南](https://www.xn2001.com/archives/661.html)

```
startup.cmd -m standalone
```

访问：http://localhost:8848/nacos/

![](https://cdn.xn2001.com/img/2021/20210901091904.png)

## 服务注册

这里上来就直接服务注册，很多东西可能有疑惑，其实 Nacos 本身就是一个 SprintBoot 项目，这点你从启动的控制台打印就可以看出来，所以就不再需要去额外搭建一个像 Eureka 的注册中心。

**引入依赖**

在 cloud-demo 父工程中引入 SpringCloudAlibaba 的依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.6.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

然后在 user-service 和 order-service 中的pom文件中引入 nacos-discovery 依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

**配置nacos地址**

在 user-service 和 order-service 的 application.yml 中添加 nacos 地址：

```yaml
spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
```

项目重新启动后，可以看到三个服务都被注册进了 Nacos

![](https://cdn.xn2001.com/img/2021/20210901091918.png)

浏览器访问：http://localhost:8080/order/101，正常访问，同时负载均衡也正常。

## 分级存储模型

一个**服务**可以有多个**实例**，例如我们的 user-service，可以有:

- 127.0.0.1:8081
- 127.0.0.1:8082
- 127.0.0.1:8083

假如这些实例分布于全国各地的不同机房，例如：

- 127.0.0.1:8081，在上海机房
- 127.0.0.1:8082，在上海机房
- 127.0.0.1:8083，在杭州机房

Nacos就将同一机房内的实例，划分为一个**集群**。

![](https://cdn.xn2001.com/img/2021/20210901091928.png)

微服务互相访问时，应该尽可能访问同集群实例，因为本地访问速度更快。**当本集群内不可用时，才访问其它集群。**例如：杭州机房内的 order-service 应该优先访问同机房的 user-service。

![](https://cdn.xn2001.com/img/2021/20210901091937.png)

## 配置集群

接下来我们给 user-service **配置集群**

修改 user-service 的 application.yml 文件，添加集群配置：

```yml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ # 集群名称 HZ杭州
```

重启两个 user-service 实例后，我们再去启动一个上海集群的实例。

```
-Dserver.port=8083 -Dspring.cloud.nacos.discovery.cluster-name=SH
```

![](https://cdn.xn2001.com/img/2021/20210901091947.png)

查看 nacos 控制台

![](https://cdn.xn2001.com/img/2021/20210901091957.png)

## NacosRule

Ribbon的默认实现 `ZoneAvoidanceRule` 并不能实现根据同集群优先来实现负载均衡，我们把规则改成 **NacosRule** 即可。我们是用 orderservice 调用 userservice，所以在 orderservice 配置规则。

```java
@Bean
public IRule iRule(){
    //默认为轮询规则，这里自定义为随机规则
    return new NacosRule();
}
```

另外，你同样可以使用配置的形式来完成，具体参考上面的 Ribbon 栏目。

```yml
userservice:
  ribbon:
    NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule #负载均衡规则 
```

然后，再对 orderservice 配置集群。

```yml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ # 集群名称
```

现在我启动了四个服务，分别是：

- orderservice - HZ
- userservice - HZ
- userservice1 - HZ
- userservice2 - SH

访问地址：http://localhost:8080/order/101

在访问中我们发现，只有同在一个 HZ 集群下的 userservice、userservice1 会被调用，并且是随机的。

我们试着把 userservice、userservice2 停掉。依旧可以访问。

在 userservice3 控制台可以看到发出了一串的警告，因为 orderservice 本身是在 HZ 集群的，这波 HZ 集群没有了 userservice，就会去别的集群找。

![](https://cdn.xn2001.com/img/2021/20210901092012.png)

## 权重配置

实际部署中会出现这样的场景：

服务器设备性能有差异，部分实例所在机器性能较好，另一些较差，我们希望性能好的机器承担更多的用户请求。但默认情况下 NacosRule 是同集群内随机挑选，不会考虑机器的性能问题。

因此，Nacos 提供了**权重配置来控制访问频率**，0~1 之间，权重越大则访问频率越高，权重修改为 0，则该实例永远不会被访问。

在 Nacos 控制台，找到 user-service 的实例列表，点击编辑，即可修改权重。

![](https://cdn.xn2001.com/img/2021/20210901092020.png)

在弹出的编辑窗口，修改权重

![](https://cdn.xn2001.com/img/2021/20210901092026.png)

另外，在服务升级的时候，有一种较好的方案：我们也可以通过调整权重来进行平滑升级，例如：先把 userservice 权重调节为 0，让用户先流向 userservice2、userservice3，升级 userservice后，再把权重从 0 调到 0.1，让一部分用户先体验，用户体验稳定后就可以往上调权重啦。

## 环境隔离

Nacos 提供了 namespace 来实现环境隔离功能。

比如开发环境和上线环境，两者数据不相通

- Nacos 中可以有多个 namespace
- namespace 下可以有 group、service 等
- 不同 namespace 之间**相互隔离**，例如不同 namespace 的服务互相不可见

![](https://cdn.xn2001.com/img/2021/20210901092032.png)

### 创建namespace

默认情况下，所有 service、data、group 都在同一个 namespace，名为 public(保留空间)：

![](https://cdn.xn2001.com/img/2021/20210901092038.png)

我们可以点击页面新增按钮，添加一个 namespace：

![](https://cdn.xn2001.com/img/2021/20210901092050.png)



然后，填写表单：

![](https://cdn.xn2001.com/img/2021/20210901092059.png)

就能在页面看到一个新的 namespace：

![](https://cdn.xn2001.com/img/2021/20210901092114.png)

### 配置namespace

给微服务配置 namespace 只能通过修改配置来实现。

例如，修改 order-service 的 application.yml 文件：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ
        namespace: 492a7d5d-237b-46a1-a99a-fa8e98e4b0f9 # 命名空间ID
```

重启 order-service 后，访问控制台。

**public**

![](https://cdn.xn2001.com/img/2021/20210901092143.png)

**dev**

![](https://cdn.xn2001.com/img/2021/20210901092130.png)

此时访问 order-service，因为 namespace 不同，会导致找不到 userservice，控制台会报错：

![](https://cdn.xn2001.com/img/2021/20210901092138.png)

## 临时实例

Nacos 的服务实例分为两种类型：

- **临时实例**：如果实例宕机超过一定时间，会从服务列表剔除，**默认的类型**。
- 非临时实例：如果实例宕机，不会从服务列表剔除，也可以叫永久实例。

配置一个服务实例为永久实例：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        ephemeral: false # 设置为非临时实例
```

另外，Nacos 集群**默认采用AP方式(可用性)**，当集群中存在非临时实例时，**采用CP模式(一致性)**；而 Eureka 采用AP方式，不可切换。（这里说的是 CAP 原理，后面会写到）

推荐使用临时实例

# Nacos配置中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/05-cloud-nacos
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/05-cloud-nacos

Nacos除了可以做注册中心，同样可以做配置管理来使用。

当微服务部署的实例越来越多，达到数十、数百时，逐个修改微服务配置就会让人抓狂，而且很容易出错。**我们需要一种统一配置管理方案，可以集中管理所有实例的配置。**

![](https://cdn.xn2001.com/img/2021/20210901092150.png)



Nacos 一方面可以将配置集中管理，另一方可以在配置变更时，及时通知微服务，**实现配置的热更新。**

## 创建配置

在 Nacos 控制面板中添加配置文件

![](https://cdn.xn2001.com/img/2021/20210901092159.png)

然后在弹出的表单中，填写配置信息：

![](https://cdn.xn2001.com/img/2021/20210901092206.png)

**注意**：项目的核心配置，需要热更新的配置才有放到 nacos 管理的必要。基本不会变更的一些配置(例如数据库连接)还是保存在微服务本地比较好。

## 拉取配置

首先我们需要了解 Nacos 读取配置文件的环节是在哪一步，在没加入 Nacos 配置之前，获取配置是这样：

![](https://cdn.xn2001.com/img/2021/20210901092215.png)

加入 Nacos 配置，它的读取是在 application.yml 之前的：

![](https://cdn.xn2001.com/img/2021/20210901092223.png)

这时候如果把 nacos 地址放在 application.yml 中，显然是不合适的，**Nacos 就无法根据地址去获取配置了。**

因此，nacos 地址必须放在优先级最高的 bootstrap.yml 文件。

![](https://cdn.xn2001.com/img/2021/20210901092228.png)

**引入 nacos-config 依赖**

首先，在 user-service 服务中，引入 nacos-config 的客户端依赖：

```xml
<!--nacos配置管理依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

**添加 bootstrap.yml**

然后，在 user-service 中添加一个 bootstrap.yml 文件，内容如下：

```yaml
spring:
  application:
    name: userservice # 服务名称
  profiles:
    active: dev #开发环境，这里是dev 
  cloud:
    nacos:
      server-addr: localhost:8848 # Nacos地址
      config:
        file-extension: yaml # 文件后缀名
```

application.yml中重复的配置记得删除掉，重复的代码不删除也可以，远端的配置文件优先级更高

根据 spring.cloud.nacos.server-addr 获取 nacos地址，再根据`${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}`作为文件id，来读取配置。

在这个例子例中，就是去读取 `userservice-dev.yaml`

![](https://cdn.xn2001.com/img/2021/20210901092237.png)

使用代码来验证是否拉取成功

在 user-service 中的 UserController 中添加业务逻辑，读取 pattern.dateformat 配置并使用：

```java
@Value("${pattern.dateformat}")
private String dateformat;

@GetMapping("now")
public String now(){
    //格式化时间
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
}
```

![](https://cdn.xn2001.com/img/2021/20210901092243.png)

启动服务后，访问：http://localhost:8081/user/now

![](https://cdn.xn2001.com/img/2021/20210901092251.png)

## 配置热更新

我们最终的目的，是修改 nacos 中的配置后，微服务中无需重启即可让配置生效，也就是**配置热更新**。

有两种方式：1. 用 `@value` 读取配置时，搭配 `@RefreshScope`；2. 直接用 `@ConfigurationProperties` 读取配置

### @RefreshScope

方式一：在 `@Value` 注入的变量所在类上添加注解 `@RefreshScope`

![](https://cdn.xn2001.com/img/2021/20210901092258.png)

### @ConfigurationProperties

`更加推荐`

方式二：使用 `@ConfigurationProperties` 注解读取配置文件，就不需要加 `@RefreshScope` 注解。

在 user-service 服务中，添加一个 PatternProperties 类，读取 `patterrn.dateformat` 属性

```java
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    public String dateformat;
}
```

```java
@Autowired
private PatternProperties patternProperties;

@GetMapping("now2")
public String now2(){
    //格式化时间
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.dateformat));
}
```

## 配置共享

其实在服务启动时，nacos 会读取多个配置文件，例如：

- `[spring.application.name]-[spring.profiles.active].yaml`，例如：userservice-dev.yaml

- `[spring.application.name].yaml`，例如：userservice.yaml

这里的 `[spring.application.name].yaml` 不包含环境，**因此可以被多个环境共享**。

**添加一个环境共享配置**

我们在 nacos 中添加一个 userservice.yaml 文件：

![](https://cdn.xn2001.com/img/2021/20210901092323.png)



**在 user-service 中读取共享配置**

在 user-service 服务中，修改 PatternProperties 类，读取新添加的属性：

![](https://cdn.xn2001.com/img/2021/20210901092314.png)

在 user-service 服务中，修改 UserController，添加一个方法：

![](https://cdn.xn2001.com/img/2021/20210901092331.png)



**运行两个 UserApplication，使用不同的profile**

修改 UserApplication2 这个启动项，改变其profile值：

![](https://cdn.xn2001.com/img/2021/20210901092345.png)

![](https://cdn.xn2001.com/img/2021/20210901092338.png)

这样，UserApplication(8081) 使用的 profile 是 dev，UserApplication2(8082) 使用的 profile 是test

启动 UserApplication 和 UserApplication2

访问地址：http://localhost:8081/user/prop，结果：

![](https://cdn.xn2001.com/img/2021/20210901092400.png)

访问地址：http://localhost:8082/user/prop，结果：

![](https://cdn.xn2001.com/img/2021/20210901092419.png)

可以看出来，不管是 dev，还是 test 环境，都读取到了 envSharedValue 这个属性的值。

上面的都是同一个微服务下，**那么不同微服务之间可以环境共享吗？**

通过下面的两种方式来指定：

- extension-configs
- shared-configs 

```yml
spring: 
  cloud:
    nacos:
      config:
        file-extension: yaml # 文件后缀名
        extends-configs: # 多微服务间共享的配置列表
          - dataId: common.yaml # 要共享的配置文件id
```

```yml
spring: 
  cloud:
    nacos:
      config:
        file-extension: yaml # 文件后缀名
        shared-configs: # 多微服务间共享的配置列表
          - dataId: common.yaml # 要共享的配置文件id
```

## 配置优先级

当 nacos、服务本地同时**出现相同属性时**，优先级有高低之分。

![](https://cdn.xn2001.com/img/2021/20210901092501.png)

更细致的配置

![](https://cdn.xn2001.com/img/2021/20210901092520.png)

# Nacos集群

## 架构介绍

![](https://cdn.xn2001.com/img/2021/202108181959897.png)

其中包含 3 个Nacos 节点，然后一个负载均衡器 Nginx 代理 3 个 Nacos，我们计划的 Nacos 集群如下图，MySQL 的主从复制后续再添加。

![](https://cdn.xn2001.com/img/2021/202108182000220.png)

三个 Nacos 节点的地址

| 节点   | ip            | port |
| ------ | ------------- | ---- |
| nacos1 | 192.168.150.1 | 8845 |
| nacos2 | 192.168.150.1 | 8846 |
| nacos3 | 192.168.150.1 | 8847 |

## 初始化数据库

Nacos 默认数据存储在内嵌数据库 Derby 中，不属于生产可用的数据库。官方推荐的最佳实践是使用带有主从的高可用数据库集群，主从模式的高可用数据库。这里我们以单点的数据库为例。

首先新建一个数据库，命名为 nacos，而后导入下面的 SQL

```sql
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text,
  `src_ip` varchar(50) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
```

## 配置Nacos

进入 nacos 的 conf 目录，修改配置文件 cluster.conf.example，重命名为 cluster.conf

![](https://cdn.xn2001.com/img/2021/202108182004564.png)

添加内容

```
127.0.0.1:8845
127.0.0.1.8846
127.0.0.1.8847
```

然后修改 application.properties 文件，添加数据库配置

```properties
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&amp;connectTimeout=1000&amp;socketTimeout=3000&amp;autoReconnect=true&amp;useUnicode=true&amp;useSSL=false&amp;serverTimezone=UTC
db.user.0=root
db.password.0=123456
```

将 nacos 文件夹复制三份，分别命名为：nacos1、nacos2、nacos3

![](https://cdn.xn2001.com/img/2021/202108182004103.png) 

然后分别修改三个文件夹中的 application.properties，

nacos1

```properties
server.port=8845
```

nacos2

```properties
server.port=8846
```

nacos3

```properties
server.port=8847
```

然后分别启动三个 nacos

```
startup.cmd
```

## Nginx反向代理

修改 nginx 文件夹下的 conf/nginx.conf 文件，配置如下

```nginx
upstream nacos-cluster {
    server 127.0.0.1:8845;
	server 127.0.0.1:8846;
	server 127.0.0.1:8847;
}

server {
    listen       80;
    server_name  localhost;

    location /nacos {
        proxy_pass http://nacos-cluster;
    }
}
```

启动 nginx，在浏览器访问：http://localhost/nacos

在代码中的 application.yml 文件配置改为如下：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:80 # Nacos地址
```

实际部署时，需要给做反向代理的 Nginx 服务器设置一个域名，这样后续如果有服务器迁移 Nacos 的客户端也无需更改配置。Nacos 的各个节点应该部署到多个不同服务器，做好容灾和隔离工作。

# Feign远程调用

> 代码参考
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/06-cloud-feign
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/06-cloud-feign

我们以前利用 RestTemplate 发起远程调用的代码：

![](https://cdn.xn2001.com/img/2021/20210901092616.png)

- 代码可读性差，编程体验不统一
- 参数复杂URL难以维护

Feign 是一个声明式的 http 客户端，官方地址：https://github.com/OpenFeign/feign

其作用就是帮助我们**优雅的实现 http 请求的发送**，解决上面提到的问题。

![](https://cdn.xn2001.com/img/2021/20210901092639.png)

## Feign使用

**引入依赖**

我们在 order-service 引入 feign 依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

**添加注解**

在 order-service 启动类添加注解开启 Feign

![](https://cdn.xn2001.com/img/2021/20210901092704.png)

**请求接口**

在 order-service 中新建一个接口，内容如下

```java
package com.xn2001.order.client;

import com.xn2001.order.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
```

`@FeignClient("userservice")`：其中参数填写的是微服务名

`@GetMapping("/user/{id}")`：其中参数填写的是请求路径

这个客户端主要是基于 SpringMVC 的注解 `@GetMapping` 来声明远程调用的信息

Feign 可以帮助我们发送 http 请求，无需自己使用 RestTemplate 来发送了。

**测试**

```java
@Autowired
private UserClient userClient;

public Order queryOrderAndUserById(Long orderId) {
    // 1.查询订单
    Order order = orderMapper.findById(orderId);
    // TODO: 2021/8/20 使用feign远程调用
    User user = userClient.findById(order.getUserId());
    // 3. 将用户信息封装进订单
    order.setUser(user);
    // 4.返回
    return order;
}
```

## 自定义配置

Feign 可以支持很多的自定义配置，如下表所示：

| 类型                   | 作用             | 说明                                                   |
| ---------------------- | ---------------- | ------------------------------------------------------ |
| **feign.Logger.Level** | 修改日志级别     | 包含四种不同的级别：NONE、BASIC、HEADERS、FULL         |
| feign.codec.Decoder    | 响应结果的解析器 | http远程调用的结果做解析，例如解析json字符串为java对象 |
| feign.codec.Encoder    | 请求参数编码     | 将请求参数编码，便于通过http请求发送                   |
| feign.Contract         | 支持的注解格式   | 默认是SpringMVC的注解                                  |
| feign.Retryer          | 失败重试机制     | 请求失败的重试机制，默认是没有，不过会使用Ribbon的重试 |

一般情况下，默认值就能满足我们使用，如果要自定义时，只需要创建自定义的 @Bean 覆盖默认 Bean 即可。下面以日志为例来演示如何自定义配置。

基于配置文件修改 feign 的日志级别可以针对单个服务：

```yaml
feign:  
  client:
    config: 
      userservice: # 针对某个微服务的配置
        loggerLevel: FULL #  日志级别 
```

**也可以针对所有服务**：

```yaml
feign:  
  client:
    config: 
      default: # 这里用default就是全局配置，如果是写服务名称，则是针对某个微服务的配置
        loggerLevel: FULL #  日志级别 
```

而日志的级别分为四种：

- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据

也可以基于 **Java 代码**来修改日志级别，先声明一个类，然后声明一个 Logger.Level 的对象

```java
public class DefaultFeignConfiguration  {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.BASIC; // 日志级别为BASIC
    }
}
```

如果要**全局生效**，将其放到启动类的 `@EnableFeignClients` 这个注解中：

```java
@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration .class) 
```

如果是**局部生效**，则把它放到对应的 `@FeignClient` 这个注解中：
局部生效谁使用放到谁上面

```java
@FeignClient(value = "userservice", configuration = DefaultFeignConfiguration .class) 
@Service
public class OrderService{
    @Auwowired
    private userClient userClient;
}
```

## 性能优化

Feign 底层发起 http 请求，依赖于其它的框架。其底层客户端实现有：

- **URLConnection**：默认实现，不支持连接池
- **Apache HttpClient** ：支持连接池
- **OKHttp**：支持连接池

因此提高 Feign 性能的主要手段就是使用**连接池**代替默认的 URLConnection

另外，日志级别应该尽量用 basic/none，可以有效提高性能。（日志消耗性能）

**这里我们用 Apache 的HttpClient来演示连接池。**Spring底层默认的一种实现

在 order-service 的 pom 文件中引入 HttpClient 依赖

```xml
<!--httpClient的依赖 -->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
</dependency>
```

**配置连接池**

在 order-service 的 application.yml 中添加配置

```yaml
feign:
  client:
    config:
      default: # default全局的配置
        loggerLevel: BASIC # 日志级别，BASIC就是基本的请求和响应信息
  httpclient:
    enabled: true # 开启feign对HttpClient的支持
    max-connections: 200 # 最大的连接数
    max-connections-per-route: 50 # 每个路径的最大连接数
```

在 FeignClientFactoryBean 中的 loadBalance 方法中打断点

![](https://cdn.xn2001.com/img/2021/20210901092729.png)

Debug 方式启动 order-service  服务，可以看到这里的 client，底层就是 HttpClient

![](https://cdn.xn2001.com/img/2021/20210901092737.png)

## 最佳实践

### 继承方式

一样的代码可以通过继承来共享：

1）定义一个 API 接口，利用定义方法，并基于 SpringMVC 注解做声明

2）Feign 客户端、Controller 都集成该接口

![](https://cdn.xn2001.com/img/2021/20210901092803.png)

优点

- 简单
- 实现了代码共享

缺点

- 服务提供方、服务消费方紧耦合
- 参数列表中的注解映射并不会继承，因此 Controller 中必须再次声明方法、参数列表、注解

### 抽取方式

将 FeignClient 抽取为独立模块，并且把接口有关的 pojo、默认的 Feign 配置都放到这个模块中，提供给所有消费者使用。

例如：将 UserClient、User、Feign 的默认配置都抽取到一个 feign-api 包中，所有微服务引用该依赖包，即可直接使用。

![](https://cdn.xn2001.com/img/2021/20210901092811.png)

接下来我们就用该方法在代码中实现

**首先创建一个 module，命名为 feign-api**

![](https://cdn.xn2001.com/img/2021/20210901092835.png)

在 feign-api 中然后引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

order-service中 的 UserClient、User 都复制到 feign-api 项目中

![](https://cdn.xn2001.com/img/2021/20210901092848.png)

接下来在 order-service 中使用 feign-api

由于我们已经将 UserClient、User 放在 fegin-api 中共享了 ，所以可以删除 order-service 中的 UserClient、User，然后在 order-service  中引入 feign-api 

```xml
<dependency>
    <groupId>com.xn2001.feign</groupId>
    <artifactId>feign-api</artifactId>
    <version>1.0</version>
</dependency>
```

**修改注解**

当定义的 FeignClient 不在 SpringBootApplication 的扫描包范围下时，这些 FeignClient 就不能使用。

**方法一**：指定FeginClient所在的包
这种情况会加载所有的Client

修改 order-service 启动类上的 `@EnableFeignClients` 注解

```java
@EnableFeignClients(basePackages = "com.xn2001.feign.clients")
```

**方法二**：指定FeginClient字节码（此处的是数组形式，也可以加载多个）

```java
@EnableFeignClients(clients = {UserClient.class})
```

此处的功能就是使得 Spring 扫描到对应的文件

# Gateway网关

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/07-cloud-gateway
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/07-cloud-gateway

Spring Cloud Gateway 是 Spring Cloud 的一个全新项目，该项目是基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等响应式编程和事件流技术开发的网关，它旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式。

Gateway 网关是我们服务的守门神，**所有微服务的统一入口。**

网关的**核心功能特性**：

- 请求路由
- 权限控制
- 限流

![](https://cdn.xn2001.com/img/2021/20210901092857.png)



**权限控制**：网关作为微服务入口，需要校验用户是是否有请求资格，如果没有则进行拦截。

**路由和负载均衡**：一切请求都必须先经过 gateway，但网关不处理业务，而是根据某种规则，把请求转发到某个微服务，这个过程叫做路由。当然路由的目标服务有多个时，还需要做负载均衡。

**限流**：当请求流量过高时，在网关中按照下流的微服务能够接受的速度来放行请求，避免服务压力过大。

在 SpringCloud 中网关的实现包括两种：

- gateway
- zuul

Zuul 是基于 Servlet 实现，属于阻塞式编程。而 Spring Cloud Gateway 则是基于 Spring5 中提供的WebFlux，属于响应式编程的实现，具备更好的性能。

## 入门使用

1. 创建 SpringBoot 工程 gateway，引入网关依赖
2. 编写启动类
3. 编写基础配置和路由规则
4. 启动网关服务进行测试

```xml
<!--网关-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<!--nacos服务发现依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

创建 application.yml 文件，内容如下：

```yaml
server:
  port: 10010 # 网关端口
spring:
  application:
    name: gateway # 服务名称
  cloud:
    nacos:
      server-addr: localhost:8848 # nacos地址
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://userservice # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
```

我们将符合`Path` 规则的一切请求，都代理到 `uri`参数指定的地址。

上面的例子中，我们将 `/user/**` 开头的请求，代理到 `lb://userservice`，其中 lb 是负载均衡(LoadBalance)，根据服务名拉取服务列表，实现负载均衡。

重启网关，访问 [http://localhost:10010/user/1](http://localhost:10010/user/1 ) 时，符合 `/user/**` 规则，请求转发到 uri：[http://userservice/user/1](http://userservice/user/1)

![](https://cdn.xn2001.com/img/2021/202108220125749.png)

多个 predicates 的话，要同时满足规则，下文有例子。

## 流程图

![](https://cdn.xn2001.com/img/2021/202108220127419.png)

路由配置包括：

1. 路由id：路由的唯一标示
2. 路由目标（uri）：路由的目标地址，http代表固定地址，lb代表根据服务名负载均衡
3. 路由断言（predicates）：判断路由的规则
4. 路由过滤器（filters）：对请求或响应做处理

## 断言工厂

我们在配置文件中写的断言规则只是字符串，这些字符串会被 Predicate Factory 读取并处理，转变为路由判断的条件。

例如 `Path=/user/**` 是按照路径匹配，这个规则是由

`org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory` 类来处理的，像这样的断言工厂在 Spring Cloud Gateway 还有十几个

| 名称       | 说明                           | 示例                                                         |
| ---------- | ------------------------------ | ------------------------------------------------------------ |
| After      | 是某个时间点后的请求           | -  After=2037-01-20T17:42:47.789-07:00[America/Denver]       |
| Before     | 是某个时间点之前的请求         | -  Before=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]       |
| Between    | 是某两个时间点之前的请求       | -  Between=2037-01-20T17:42:47.789-07:00[America/Denver],  2037-01-21T17:42:47.789-07:00[America/Denver] |
| Cookie     | 请求必须包含某些cookie         | - Cookie=chocolate, ch.p                                     |
| Header     | 请求必须包含某些header         | - Header=X-Request-Id, \d+                                   |
| Host       | 请求必须是访问某个host（域名） | -  Host=`**.somehost.org`, `**.anotherhost.org`              |
| Method     | 请求方式必须是指定方式         | - Method=GET,POST                                            |
| Path       | 请求路径必须符合指定规则       | - Path=/red/{segment},/blue/**                               |
| Query      | 请求参数必须包含指定参数       | - Query=name, Jack或者-  Query=name                          |
| RemoteAddr | 请求者的ip必须是指定范围       | - RemoteAddr=192.168.1.1/24                                  |
| Weight     | 权重处理                       |                                                              |

> 官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories

一般的，我们只需要掌握 Path，加上官方文档的例子，就可以应对各种工作场景了。

```yaml
predicates:
  - Path=/order/**
  - After=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]
```

像这样的规则，现在是 2021年8月22日01:32:42，很明显 After 条件不满足，可以不会转发，路由不起作用。

## 过滤器工厂

GatewayFilter 是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理。

![](https://cdn.xn2001.com/img/2021/202108220133487.png)

Spring提供了31种不同的路由过滤器工厂。

> 官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories

| **名称**             | **说明**                     |
| -------------------- | ---------------------------- |
| AddRequestHeader     | 给当前请求添加一个请求头     |
| RemoveRequestHeader  | 移除请求中的一个请求头       |
| AddResponseHeader    | 给响应结果中添加一个响应头   |
| RemoveResponseHeader | 从响应结果中移除有一个响应头 |
| RequestRateLimiter   | 限制请求的流量               |

下面我们以 AddRequestHeader 为例：

![](https://cdn.xn2001.com/img/2021/202108220139913.png)

**需求**：给所有进入 userservice 的请求添加一个请求头：`sign=xn2001.com is eternal`

只需要修改 gateway 服务的 application.yml文件，添加路由过滤即可。

```yaml
spring:
  cloud:
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://userservice # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
          filters:
            - AddRequestHeader=sign, xn2001.com is eternal # 添加请求头
```

如何验证，我们修改 userservice 中的一个接口

```java
@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) {
    log.warn(sign);
    return userService.queryById(id);
}
```

重启两个服务，访问：[http://localhost:10010/user/1](http://localhost:10010/user/1)

可以看到控制台打印出了这个请求头

![](https://cdn.xn2001.com/img/2021/202108220145565.png)

当然，Gateway 也是有**全局过滤器**的，如果要**对所有的路由都生效**，则可以将过滤器工厂写到 default-filters 下：

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=sign, xn2001.com is eternal # 添加请求头
```

## 全局过滤器

上面介绍的过滤器工厂，网关提供了 31 种，但每一种过滤器的作用都是固定的。**如果我们希望拦截请求，做自己的业务逻辑则没办法实现**。

全局过滤器的作用也是处理一切进入网关的请求和微服务响应，**与 GatewayFilter 的作用一样**。区别在于 GlobalFilter 的逻辑可以**写代码来自定义规则**；而 GatewayFilter 通过配置定义，处理逻辑是固定的。

**需求**：定义全局过滤器，拦截请求，判断请求的参数是否满足下面条件

- 参数中是否有 authorization

- authorization 参数值是否为 admin

如果同时满足则放行，否则拦截。

```java
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 测试：http://localhost:10010/order/101?authorization=admin
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取第一个 authorization 参数
        String authorization = exchange.getRequest().getQueryParams().getFirst("authorization");
        if ("admin".equals(authorization)){
            // 放行
            return chain.filter(exchange);
        }
        // 设置拦截状态码信息
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置拦截
        return exchange.getResponse().setComplete();
    }

    // 设置过滤器优先级，值越低优先级越高
    // 也可以使用 @Order 注解
    @Override
    public int getOrder() {
        return 0;
    }
}
```

## 过滤器顺序

请求进入网关会碰到三类过滤器：DefaultFilter、当前路由的过滤器、GlobalFilter；

请求路由后，会将三者合并到一个过滤器链（集合）中，排序后依次执行每个过滤器.

![](https://cdn.xn2001.com/img/2021/202108230002747.png)



排序的规则是什么呢？

- 每一个过滤器都必须指定一个 int 类型的 order 值，**order 值越小，优先级越高，执行顺序越靠前**。
- GlobalFilter 通过实现 Ordered 接口，或者使用 @Order 注解来指定 order 值，由我们自己指定。
- 路由过滤器和 defaultFilter 的 order 由 Spring 指定，默认是按照声明顺序从1递增。
- 当过滤器的 order 值一样时，**会按照 defaultFilter > 路由过滤器 > GlobalFilter 的顺序执行。**

## 跨域问题

不了解跨域问题的同学可以百度了解一下；在 Gateway 网关中解决跨域问题还是比较方便的。

```yaml
spring:
  cloud:
    gateway:
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 解决options请求被拦截问题
        corsConfigurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求 allowedOrigins: “*” 允许所有网站
              - "http://localhost:8090"
            allowedMethods: # 允许的跨域ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowCredentials: true # 是否允许携带cookie
            maxAge: 360000 # 这次跨域检测的有效期
```



# Docker

## 初识Docker

**项目部署的问题：**

大型项目组件较多，运行环境也较为复杂，部署时会碰到一些问题：

- 依赖关系复杂，容易出现兼容性问题
- 开发、测试、生产环境有差异



**Docker如何解决大型项目依赖关系复杂，不同组件依赖的兼容性问题？**

- Docker允许在开发中将应用、依赖、函数库、配置一起打包，形成可移植镜像
- Dockers应用运行在容器中，使用沙箱机制，相互隔离

Docker如何解决开发、测试、生产环境有差异的问题

- Docker镜像中包含完整运行环境，包括系统函数库，仅依赖系统的Linux内核，因此可以在任意Linux操作系统上运行。

Docker是一个快速交付应、运行应用的技术

1. 可以将程序及其依赖、运行环境一起打包为一个镜像，可以迁移到任意Linux系统
2. 运行时利用沙箱机制形成隔离容器，各个应用互不干扰
3. 启动、移除都可以通过一行命令完成，方便快捷

### Docker与虚拟机

- Docker是一个系统进程；虚拟机是在操作系统中的操作系统
- Docker体积小、启动速度快、性能好；虚拟机体积大、启动速度慢、性能一般

### 镜像和容器

**镜像（Image）：**Docker将应用程序所及其所需的依赖、函数库、环境、配置等文件打包到一起，称为镜像。（只读）

**容器（Container）：**镜像中的应用程序运行后形成的进程就是容器，只是Cocker会给容器做隔离，对外不可见。

<img src="https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/image-20220909085607559.png" alt="image-20220909085607559" style="zoom:50%;" />

DockerHub：DockerHub是一个Docker镜像的托管平台。这样的平台称为Docker Registry

## Docker的基本操作

## Dockerfile自定义镜像

## Docker-Compose

## Doocker服务镜像

# RabbitMQ消息中间件

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/08-rabbitmq-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/08-rabbitmq-demo

## 同步异步通讯

**微服务间通讯有同步和异步两种方式**

同步通讯：就像打电话，需要实时响应。

异步通讯：就像发邮件，不需要马上回复。

![](https://cdn.xn2001.com/img/2021/20210904133345.png)

两种方式各有优劣，打电话可以立即得到响应，但是你却不能跟多个人同时通话。发送邮件可以同时与多个人收发邮件，但是往往响应会有延迟。

我们之前学习的 **Feign 调用**就属于**同步方式**，虽然调用可以实时得到结果，但存在下面的问题：

![](https://cdn.xn2001.com/img/2021/20210904133517.png)

**同步调用的优点**：

- 时效性较强，可以立即得到结果

**同步调用的缺点**：

- 耦合度高
- 性能和吞吐能力下降
- 有额外的资源消耗
- 有级联失败问题

异步调用则可以避免上述问题，我们以购买商品为例，用户支付后需要调用订单服务完成订单状态修改，调用物流服务，从仓库分配响应的库存并准备发货。在事件模式中，支付服务是事件发布者（publisher），在支付完成后只需要发布一个支付成功的事件（event），事件中带上订单id。订单服务和物流服务是事件订阅者（Consumer），订阅支付成功的事件，监听到事件后完成自己业务即可。

为了解除事件发布者与订阅者之间的耦合，两者并不是直接通信，而是有一个中间人（Broker）。发布者发布事件到Broker，不关心谁来订阅事件。订阅者从Broker订阅事件，不关心谁发来的消息。

![](https://cdn.xn2001.com/img/2021/20210904144714.png)

Broker 是一个像数据总线一样的东西，所有的服务要接收数据和发送数据都发到这个总线上，这个总线就像协议一样，让服务间的通讯变得标准和可控。

![](https://cdn.xn2001.com/img/2021/20210904145001.png)

**异步调用好处**：

- 吞吐量提升：无需等待订阅者处理完成，响应更快速

- 故障隔离：服务没有直接调用，不存在级联失败问题
- 调用间没有阻塞，不会造成无效的资源占用
- 耦合度极低，每个服务都可以灵活插拔，可替换
- 流量削峰：不管发布事件的流量波动多大，都由 Broker 接收，订阅者可以按照自己的速度去处理事件

**异步调用缺点**：

- 架构复杂了，业务没有明显的流程线，不好管理
- 需要依赖于 Broker 的可靠、安全、性能

## MQ消息队列

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列，也就是事件驱动架构中的 Broker

比较常见的 MQ 实现：

- ActiveMQ
- RabbitMQ
- RocketMQ
- Kafka

几种常见MQ的对比：

|            | **RabbitMQ**            | **ActiveMQ**                      | **RocketMQ** | **Kafka**      |
| ---------- | ----------------------- | --------------------------------- | ------------ | -------------- |
| 公司/社区  | Rabbit                  | Apache                            | 阿里         | Apache         |
| 开发语言   | Erlang                  | Java                              | Java         | Scala&amp;Java |
| 协议支持   | AMQP、XMPP、SMTP、STOMP | OpenWire、STOMP、REST、XMPP、AMQP | 自定义协议   | 自定义协议     |
| 可用性     | 高                      | 一般                              | 高           | 高             |
| 单机吞吐量 | 一般                    | 差                                | 高           | 非常高         |
| 消息延迟   | 微秒级                  | 毫秒级                            | 毫秒级       | 毫秒以内       |
| 消息可靠性 | 高                      | 一般                              | 高           | 一般           |

以 RabbitMQ 为例，我们在 Centos7 虚拟机中使用 Docker 来安装

在线拉取镜像

``` sh
docker pull rabbitmq:3-management
```

执行下面的命令来运行MQ容器

```sh
docker run \
 -e RABBITMQ_DEFAULT_USER=admin \
 -e RABBITMQ_DEFAULT_PASS=123456 \
 --name mq \
 --hostname mq1 \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 rabbitmq:3-management
```

启动成功后访问地址：http://192.168.211.128:15672

**RabbitMQ 中的一些角色**

- publisher：生产者
- consumer：消费者
- exchange：交换机，负责消息路由
- queue：队列，存储消息
- virtualHost：虚拟主机，**隔离不同租户**的 exchange、queue、消息的隔离

**MQ 的基本结构**

![](https://cdn.xn2001.com/img/2021/20210904172912.png)

## 入门案例

RabbitMQ 官方提供了 5 个不同的 Demo 示例，对应了不同的消息模型。

![](https://cdn.xn2001.com/img/2021/20210904173739.png)

Hello World 模型

 ![](https://cdn.xn2001.com/img/2021/20210904200637.png)

官方的 HelloWorld 是基于最基础的消息队列模型来实现的，只包括三个角色：

- publisher：消息发布者，将消息发送到队列queue
- queue：消息队列，负责接受并缓存消息
- consumer：订阅队列，处理队列中的消息

### publisher实现

- 建立连接
- 创建 channel
- 声明队列
- 发送消息
- 关闭连接和 channel

```java
public class PublisherTest {
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.211.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        // 1.2.建立连接
        Connection connection = factory.newConnection();
        // 2.创建通道Channel
        Channel channel = connection.createChannel();
        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);
        // 4.发送消息
        String message = "Hello RabbitMQ！";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：[" + message + "]");
        // 5.关闭通道和连接
        channel.close();
        connection.close();
    }
}
```

### consumer实现

- 建立连接
- 创建 channel
- 声明队列
- 订阅消息

```java
public class ConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.211.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        // 1.2.建立连接
        Connection connection = factory.newConnection();
        // 2.创建通道Channel
        Channel channel = connection.createChannel();
        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);
        // 4.订阅消息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：[" + message + "]");
            }
        });
        System.out.println("等待接收消息中");
    }
}
```

## SpringAMQP

SpringAMQP 是基于 RabbitMQ 封装的一套模板，并且还利用 SpringBoot 对其实现了自动装配，使用起来非常方便。

SpringAMQP 的官方地址：https://spring.io/projects/spring-amqp

![](https://cdn.xn2001.com/img/2021/20210904202046.png)

![](https://cdn.xn2001.com/img/2021/20210904202056.png)

SpringAMQP 提供了三个功能：

- 自动声明队列、交换机及其绑定关系
- 基于注解的监听器模式，异步接收消息
- 封装了 RabbitTemplate 工具，用于发送消息 

```xml
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

###  BasicQueue

首先配置 MQ地址，在 publisher、consumer 服务中的 application.yml 中添加配置

```yml
spring:
  rabbitmq:
    host: 192.168.150.101 # 主机名
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: admin # 用户名
    password: 123456 # 密码
```

在 consumer 服务中添加监听队列

```java
@Component
public class RabbitMQListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("消费者接收到消息：【" + msg + "】");
    }
}
```

在 publisher 服务中添加发送消息的测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "你好啊，乐心湖！";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

### WorkQueue

Work queues，也被称为（Task queues），任务模型。简单来说就是**让多个消费者绑定到一个队列，共同消费队列中的消息**。

![](https://cdn.xn2001.com/img/2021/20210904211238.png)

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。

此时就可以使用 work 模型，多个消费者共同处理消息处理，速度就能大大提高了。

我们循环发送，模拟大量消息堆积现象，在 publisher 服务中的 SpringAmqpTest 类中添加一个测试方法：

```java
/**
 * workQueue
 * 向队列中不停发送消息，模拟消息堆积。
 */
@Test
public void testWorkQueue() throws InterruptedException {
    // 队列名称
    String queueName = "simple.queue";
    // 消息
    String message = "hello, message_";
    for (int i = 0; i < 50; i++) {
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message + i);
        Thread.sleep(20);
    }
}
```

**消息接收**

要模拟多个消费者绑定同一个队列，我们在 consumer 服务的 RabbitMQListener 中添加2个新的方法：

```java
@RabbitListener(queues = "simple.queue")
public void listenWorkQueue1(String msg) throws InterruptedException {
    System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(20);
}

@RabbitListener(queues = "simple.queue")
public void listenWorkQueue2(String msg) throws InterruptedException {
    System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(200);
}
```

启动 ConsumerApplication 后，在执行 publisher 服务中刚刚编写的发送测试方法 testWorkQueue

可以看到消费者1很快完成了自己的25条消息。消费者2却在缓慢的处理自己的25条消息。

也就是说消息是**平均分配给每个消费者**，并没有考虑到消费者的处理能力。这是因为 RabbitMQ 默认有一个消息预取机制，显然这不是我们想要的结果，我们需要的是能者多劳嘛，所以去限制每次只能取一条消息，可以解决这个问题。

在 spring 中有一个简单的配置，设置 prefetch 属性，我们修改 consumer 服务的 application.yml 文件，添加配置

```yml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1 # 每次只能获取一条消息，处理完成才能获取下一个消息
```

Work 模型的使用：

- 多个消费者绑定到一个队列，同一条消息只会被一个消费者处理
- 通过设置 prefetch 来控制消费者预取的消息数量

### 发布/订阅

![](https://cdn.xn2001.com/img/2021/20210904213455.png)



图中可以看到，在订阅模型中，多了一个 exchange 角色，而且过程略有变化

- Publisher：生产者，也就是要发送消息的程序，但是不再发送到队列中，**而是发给 exchange（交换机）**
- Consumer：消费者，与以前一样，订阅队列，没有变化
- Queue：消息队列也与以前一样，接收消息、缓存消息
- Exchange：交换机，一方面，接收生产者发送的消息；另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于 Exchange 的类型。Exchange 有以下3种类型：
  - Fanout：广播，将消息交给所有绑定到交换机的队列
  - Direct：定向，把消息交给符合指定 routing key 的队列
  - Topic：通配符，把消息交给符合 routing pattern（路由模式） 的队列

**Exchange（交换机）只负责转发消息，不具备存储消息的能力**，因此如果没有任何队列与 Exchange 绑定，或者没有符合路由规则的队列，那么消息会丢失！

### Fanout

Fanout，英文翻译是扇出，在 MQ 中我们也可以称为广播。

![](https://cdn.xn2001.com/img/2021/20210912160350.png)

在广播模式下，消息发送流程是这样的：

- 可以有多个队列
- 每个队列都要绑定到 Exchange（交换机）
- 生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定
- 交换机把消息发送给绑定过的所有队列
- 订阅队列的消费者都能拿到消息

接下里我们用 SpringAMQP 来简单实现 FanoutExchange

1. 在 consumer 服务中，利用代码声明队列、交换机，并将两者绑定
2. 在 consumer 服务中，编写两个消费者方法，分别监听 fanout.queue1 和 fanout.queue2

3. 在 publisher 中编写测试方法，向 fanout发送消息

**声明队列和交换机**

Spring 提供了一个接口 Exchange，来表示所有不同类型的交换机。

![](https://cdn.xn2001.com/img/2021/20210904213809.png)

在 consumer 中创建一个类，声明队列、交换机、**绑定对象 Binding**

```java
@Configuration
public class FanoutConfig {

    /**
     * 声明交换机
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("xn2001.fanout");
    }

    /**
     * 声明队列
     * @return Queue
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue1");
    }
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1(FanoutExchange fanoutExchange,Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    @Bean
    public Binding bindingQueue2(FanoutExchange fanoutExchange,Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

}
```

![](https://cdn.xn2001.com/img/2021/20210912181932.png)

通过这样 `@Bean` 的方式来申明确实比较麻烦，其实我们也是可以直接通过 `@RabbitListener` 注解来完成的，代码如下：

在 consumer 服务的 SpringRabbitListener 中添加三个方法，作为消费者

```java
@RabbitListener(queues = "fanout.queue1")
public void listenFanoutQueue1(String msg) throws InterruptedException {
    System.out.println("接收到fanout.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(queues = "fanout.queue2")
public void listenFanoutQueue2(String msg) throws InterruptedException {
    System.err.println("接收到fanout.queue2的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "fanout.queue3"),
    exchange = @Exchange(value = "xn2001.fanout",type = "fanout")
))
public void listenFanoutQueue3(String msg) {
    System.out.println("接收到fanout.queue3的消息：【" + msg + "】" + LocalTime.now());
}
```

在 publisher 服务的 SpringAmqpTest 类中添加测试方法

```java
/**
 * fanout
 * 向交换机发送消息
 */
@Test
public void testFanoutExchange() {
    // 交换机名称
    String exchangeName = "xn2001.fanout";
    // 消息
    String message = "hello, everybody!";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
}
```

运行该方法，可以发现 fanout.queue1、fanout.queue2 都收到了交换机的消息。

![](https://cdn.xn2001.com/img/2021/20210912182458.png)

总结一下：

交换机的作用是什么？

- 接收 publisher 发送的消息
- 将消息按照规则路由到与之绑定的队列
- 不能缓存消息，路由失败，消息丢失
- FanoutExchange 会将所有消息路由到每个绑定的队列

### Direct

在 Fanout 模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到 DirectExchange

![](https://cdn.xn2001.com/img/2021/20210912182822.png)

 在 Direct 模型下：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
- 消息的发送方向 Exchange发送消息时，也必须指定消息的 `RoutingKey`。
- Exchange 不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey` 与消息的 `Routing key`完全一致，才会接收到消息

在 consumer 的 SpringRabbitListener 中添加两个消费者，同时基于注解来声明队列和交换机

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "direct.queue1"),
    exchange = @Exchange(value = "xn2001.direct"),
    key = {"a","b"}
))
public void listenDirectQueue1(String msg){
    System.out.println("接收到direct.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "direct.queue2"),
    exchange = @Exchange(value = "xn2001.direct"),
    key = {"a","c"}
))
public void listenDirectQueue2(String msg){
    System.out.println("接收到direct.queue2的消息：【" + msg + "】" + LocalTime.now());
}
```

在 publisher 服务的 SpringAmqpTest 类中添加测试方法

```java
/**
 * direct
 * 向交换机发送消息
 */
@Test
public void testDirectExchangeToA() {
    // 交换机名称
    String exchangeName = "xn2001.direct";
    // 消息
    String message = "hello, i am direct to a!";
    rabbitTemplate.convertAndSend(exchangeName, "a", message);
}

/**
 * direct
 * 向交换机发送消息
 */
@Test
public void testDirectExchangeToB() {
    // 交换机名称
    String exchangeName = "xn2001.direct";
    // 消息
    String message = "hello, i am direct to b!";
    rabbitTemplate.convertAndSend(exchangeName, "b", message);
}
```

### Topic

`Topic ` 与 `Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic `类型可以让队列在绑定`Routing key` 的时候使用通配符！

通配符规则：

`#`：匹配一个或多个词 

`*`：只能匹配一个词

例如：

`item.#`：能够匹配`item.spu.insert` 或者 `item.spu`

`item.*`：只能匹配`item.spu`

 ![](https://cdn.xn2001.com/img/2021/20210912194016.png)

- Queue1：绑定的是 `china.#` ，因此凡是以 `china.` 开头的 `routing key` 都会被匹配到。包括 china.news 和 china.weather
- Queue2：绑定的是 `#.news` ，因此凡是以 `.news ` 结尾的  `routing key` 都会被匹配。包括 china.news 和 japan.news 

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "topic.queue1"),
    exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
    key = {"china.#"}
))
public void listenTopicQueue1(String msg){
    System.out.println("接收到topic.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "topic.queue2"),
    exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
    key = {"china.*"}
))
public void listenTopicQueue2(String msg){
    System.out.println("接收到topic.queue2的消息：【" + msg + "】" + LocalTime.now());
}
```

```java
/**
 * topic
 * 向交换机发送消息
 */
@Test
public void testTopicExchange() {
    // 交换机名称
    String exchangeName = "xn2001.topic";
    // 消息
    String message1 = "hello, i am topic form china.news";
    String message2 = "hello, i am topic form china.news.2";
    rabbitTemplate.convertAndSend(exchangeName, "china.news", message1);
    rabbitTemplate.convertAndSend(exchangeName, "china.news.2", message2);
}
```



![](https://cdn.xn2001.com/img/2021/20210912200012.png)

### 消息转换器

Spring 会把你发送的消息序列化为字节发送给 MQ，接收消息的时候，还会把字节反序列化为 Java 对象。

**默认情况下 Spring 采用的序列化方式是 JDK 序列化。**

我们可以去试一下效果

```java
@RabbitListener(queuesToDeclare = @Queue(value = "object.queue"))
public void listenObjectQueue(Map<String,Object> msg) throws InterruptedException {
    System.err.println("object接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(200);
}
```

```java
@Test
public void testSendMap()  {
    // 准备消息
    Map<String,Object> msg = new HashMap<>();
    msg.put("name", "Jack");
    msg.put("age", 21);
    // 发送消息
    rabbitTemplate.convertAndSend("object.queue", msg);
}
```

![](https://cdn.xn2001.com/img/2021/20210912204117.png)

众所周知，JDK序列化存在下列问题：

- 数据体积过大
- 有安全漏洞
- 可读性差

我们推荐可以使用 JSON 来序列化

在 publisher 和 consumer 两个服务中都引入依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.10</version>
</dependency>
```

配置消息转换器。

在各自的启动类中添加一个 Bean 即可

```java
@Bean
public MessageConverter jsonMessageConverter(){
    return new Jackson2JsonMessageConverter();
}
```

![](https://cdn.xn2001.com/img/2021/20210912204512.png)



# ELasticsearch搜索引擎

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/09-elasticsearch-hotel-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/09-elasticsearch-hotel-demo

ELasticsearch 是一款非常强大的开源搜索引擎，具备非常多强大功能，可以帮助我们从海量数据中快速找到需要的内容，可以用来实现搜索、日志统计、分析、系统监控等功能。

![](https://cdn.xn2001.com/img/2021/20210918202359.png)

## 倒排索引

**首先，倒排索引的概念是基于 MySQL 这样的正向索引而言的。**

那么我们先讲何为正向索引。例如给下表（tb_goods）中的 id 创建索引

![](https://cdn.xn2001.com/img/2021/20210918202527.png)

如果是根据 id 查询，那么直接走索引，查询速度非常快。

但如果是基于 title 做模糊查询，只能是逐行扫描数据，流程如下：

1. 用户搜索数据，条件是 title 符合 `"%手机%"`
2. 逐行获取数据，比如 id 为 1 的数据
3. 判断数据中的 title 是否符合用户搜索条件
4. 如果符合则放入结果集，不符合则丢弃。然后回到步骤1

逐行扫描，也就是全表扫描，随着数据量增加，其查询效率也会越来越低。当数据量达到数百万时，就是。。。

而倒排索引中有两个非常重要的概念：

- 文档（`Document`）：用来搜索的数据，其中的每一条数据就是一个文档。例如一个网页、一个商品信息
- 词条（`Term`）：对文档数据或用户搜索数据，利用某种算法分词，得到的具备含义的词语就是词条。例如：我是中国人，就可以分为：我、是、中国人、中国、国人这样的几个词条

**创建倒排索引**是对正向索引的一种特殊处理，流程如下：

- 将每一个文档的数据利用算法分词，得到一个个词条
- 创建表，每行数据包括词条、词条所在文档 id、位置等信息
- 因为词条唯一性，可以给词条创建索引，例如 hash 表结构索引

如图：

![](https://cdn.xn2001.com/img/2021/20210918203514.png)



**倒排索引的搜索流程**如下（以搜索"华为手机"为例）

1. 用户输入条件`"华为手机"`进行搜索
2. 对用户输入内容**分词**，得到词条：`华为`、`手机`
3. 拿着词条在倒排索引中查找，可以得到包含词条的文档 id 有 1、2、3
4. 拿着文档 id 到正向索引中查找具体文档

![](https://cdn.xn2001.com/img/2021/20210918203815.png)

**虽然要先查询倒排索引，再查询正向索引，但是词条和文档id 都建立了索引，查询速度非常快！无需全表扫描。**

为什么一个叫做正向索引，一个叫做倒排索引呢？

**正向索引**是最传统的，根据 id 索引的方式。但根据词条查询时，必须先逐条获取每个文档，然后判断文档中是否包含所需要的词条，是**根据文档找词条的过程**

**倒排索引**则相反，是先找到用户要搜索的词条，根据得到的文档 id 获取该文档。是**根据词条找文档的过程**

## 文档和字段

elasticsearch 是面向**文档（Document）**存储的，可以是数据库中的一条商品数据，一个订单信息。文档数据会被序列化为 json 格式后存储在 elasticsearch

![](https://cdn.xn2001.com/img/2021/20210918212707.png)

而 JSON 文档中往往包含很多的**字段（Field）**，类似于数据库中的列。

## 索引和映射

**索引（Index）**，就是相同类型的文档的集合。

例如：

- 所有用户文档，就可以组织在一起，称为用户的索引；
- 所有商品的文档，可以组织在一起，称为商品的索引；
- 所有订单的文档，可以组织在一起，称为订单的索引；

![](https://cdn.xn2001.com/img/2021/20210918213357.png)



因此，我们可以把索引当做是数据库中的表。

数据库的表会有约束信息，用来定义表的结构、字段的名称、类型等信息。因此，索引库中就有**映射（mapping）**，是索引中文档的字段约束信息，类似表的结构约束。

**mysql 与 elasticsearch**

| **MySQL** | **Elasticsearch** | **说明**                                                     |
| --------- | ----------------- | ------------------------------------------------------------ |
| Table     | Index             | 索引(index)，就是文档的集合，类似数据库的表(table)           |
| Row       | Document          | 文档（Document），就是一条条的数据，类似数据库中的行（Row），文档都是JSON格式 |
| Column    | Field             | 字段（Field），就是JSON文档中的字段，类似数据库中的列（Column） |
| Schema    | Mapping           | Mapping（映射）是索引中文档的约束，例如字段类型约束。类似数据库的表结构（Schema） |
| SQL       | DSL               | DSL是elasticsearch提供的JSON风格的请求语句，用来操作elasticsearch，实现CRUD |

- Mysql：擅长事务类型操作，可以确保数据的安全和一致性

- Elasticsearch：擅长海量数据的搜索、分析、计算

因此在企业中，往往是两者结合使用：

- 对安全性要求较高的写操作，使用 MySQL 实现
- 对查询性能要求较高的搜索需求，使用 ELasticsearch 实现
- 两者再基于某种方式，实现数据的同步，保证一致性

![](https://cdn.xn2001.com/img/2021/20210918213631.png)



## 安装Elasticsearch

因为我们还需要部署 kibana 容器，需要让 es 和 kibana 容器互联。这里先创建一个网络：

```sh
docker network create es-net 
```

安装

```sh
docker run -d \
--name es \
-e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
-e "discovery.type=single-node" \
-v es-data:/usr/share/elasticsearch/data \
-v es-plugins:/usr/share/elasticsearch/plugins \
--privileged \
--network es-net \
-p 9200:9200 \
-p 9300:9300 \
elasticsearch:7.12.1
```

命令解释：

- `-e "cluster.name=es-docker-cluster"`：设置集群名称
- `-e "http.host=0.0.0.0"`：监听的地址，可以外网访问
- `-e "ES_JAVA_OPTS=-Xms512m -Xmx512m"`：内存大小
- `-e "discovery.type=single-node"`：非集群模式
- `-v es-data:/usr/share/elasticsearch/data`：挂载逻辑卷，绑定es的数据目录
- `-v es-logs:/usr/share/elasticsearch/logs`：挂载逻辑卷，绑定es的日志目录
- `-v es-plugins:/usr/share/elasticsearch/plugins`：挂载逻辑卷，绑定es的插件目录
- `--privileged`：授予逻辑卷访问权
- `--network es-net` ：加入一个名为 es-net 的网络中
- `-p 9200:9200`：端口映射配置

访问地址：http://192.168.211.128:9200 即可看到 elasticsearch 的响应结果

![](https://cdn.xn2001.com/img/2021/20210918214620.png)

## 安装kibana

kibana 可以给我们提供一个 elasticsearch 的可视化界面，便于我们学习命令。

```sh
docker run -d \
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601  \
kibana:7.12.1
```

- `--network es-net` ：加入一个名为 es-net 的网络中，与 elasticsearch 在同一个网络中
- `-e ELASTICSEARCH_HOSTS=http://es:9200"`：设置 elasticsearch 的地址，因为 kibana 已经与 elasticsearch 在一个网络，因此可以用容器名直接访问 elasticsearch
- `-p 5601:5601`：端口映射配置

访问地址：http://192.168.211.128:5601，即可看到结果

![](https://cdn.xn2001.com/img/2021/20210918214722.png)

控制面板：http://192.168.211.128:5601/app/dev_tools#/console

## 安装IK分词器

由于国内访问 GitHub 较慢，我们选择离线模式安装。

安装插件需要知道 elasticsearch 的 plugins 目录位置，而我们用了数据卷挂载，因此需要查看 elasticsearch 的数据卷目录，通过下面命令查看

```sh
docker volume inspect es-plugins
```

显示结果：

```json
[
    {
        "CreatedAt": "2022-05-06T10:06:34+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/es-plugins/_data",
        "Name": "es-plugins",
        "Options": null,
        "Scope": "local"
    }
]
```

说明 plugins 目录被挂载到了 `/var/lib/docker/volumes/es-plugins/_data ` 这个目录中

![](https://cdn.xn2001.com/img/2021/20210918215615.png)

重启容器

```shell
# 4、重启容器
docker restart es

# 查看es日志
docker logs -f es
```

IK分词器包含两种模式：

* `ik_smart`：智能切分，粗粒度
* `ik_max_word`：最细切分，细粒度

我们在上面的 Kibana 控制台测试

```json
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "钟老师你好菜啊"
}
```

## 扩展词词典

在上面的IK分词器我们可以随着热点词来扩展，可以自己添加，比如 ”钟老师应该是一个热点词“，另外你也可以配置一些停用掉的敏感词，让其不进行分词。

打开IK分词器 config 目录是 `IKAnalyzer.cfg.xml`，添加一个文件名，我们以 `ext.dic` 文件名为例。

![](https://cdn.xn2001.com/img/2021/20210918221159.png)

我们去创建 `ext.dic` ，在其中添加热点词就好了，一个词一行。

![](https://cdn.xn2001.com/img/2021/20210918220910.png)

重启 elasticsearch

```sh
docker restart es
```

重新测试

```json
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "钟老师你好菜啊"
}
```

![](https://cdn.xn2001.com/img/2021/20210918221424.png)

## 索引库操作

### Mapping属性映射

索引库就类似数据库表，**mapping 映射就类似表的结构**

我们要向 es 中存储数据，必须先创建“库”和“表”

mapping 是对索引库中文档的约束，常见的 mapping 属性包括：

- type：字段数据类型，常见的简单类型有：
  - 字符串：text（可分词的文本）、keyword（精确值，例如：品牌、国家、ip地址）
  - 数值：long、integer、short、byte、double、float、
  - 布尔：boolean
  - 日期：date
  - 对象：object
- **index：是否创建索引，默认为 true**
- analyzer：使用哪种分词器
- properties：该字段的子字段

我们以需要存储下面的 JSON 为例来讲解

```json
{
    "age": 21,
    "weight": 52.1,
    "isMarried": false,
    "info": "钟老师真菜",
    "email": "jialna@qq.com",
    "score": [99.1, 99.5, 98.9],
    "name": {
        "firstName": "湖",
        "lastName": "心"
    }
}
```

首先对应的每个字段映射（mapping）情况如下：

- age：类型为 integer；参与搜索，index 为 true；无需分词器
- weight：类型为 float；参与搜索，index 为 true；无需分词器
- isMarried：类型为boolean；参与搜索，index 为 true；无需分词器
- info：类型为字符串，需要分词，因此是 text；参与搜索，index为true；分词器可以用 ik_smart
- email：类型为字符串，但是不需要分词，因此是 keyword；不参与搜索，index 为 false；无需分词器
- score：虽然是数组，**但是我们只看元素的类型**，类型为 float；参与搜索，index 为 true；无需分词器
- name：类型为 object，需要定义多个子属性
  - name.firstName：类型为字符串，不需要分词，keyword；参与搜索，index 为 true；无需分词器
  - name.lastName：类型为字符串，不需要分词，keyword；参与搜索，index 为 true；无需分词器

### 创建索引库和映射

上面我们了解了 Mapping 属性映射，接下来我们就去看看如何创建索引库及映射。

```json
PUT /索引库名称
{
  "mappings": {
    "properties": {
      "字段名":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "字段名2":{
        "type": "keyword",
        "index": "false"
      },
      "字段名3":{
        "properties": {
          "子字段": {
            "type": "keyword"
          }
        }
      }
      // ...略
    }
  }
}
```

```json
PUT /xn2001
{
  "mappings": {
    "properties": {
      "info":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email":{
        "type": "keyword",
        "index": "false"
      },
      "name":{
        "properties": {
          "firstName": {
            "type": "keyword"
          },
          "lastName": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
```

![](https://cdn.xn2001.com/img/2021/20210918224647.png)

我们用真实的数据库表来创建一个索引库

![](https://cdn.xn2001.com/img/2021/20210919164626.png)

- 字段名、字段数据类型，可以参考数据表结构的名称和类型
- 是否参与搜索要分析业务来判断，例如图片地址，就无需参与搜索
- 是否分词呢要看内容，内容如果是一个整体就无需分词
- 分词器，我们可以统一使用 `ik_max_word`

```json
PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}
```

特殊字段说明：

- location：地理坐标，里面包含精度、纬度
- all：一个组合字段，其目的是将多字段的值利用 `copy_to` 合并，提供给用户搜索，这样一来就只需要搜索一个字段就可以得到结果，性能更好。

> ES中支持两种地理坐标数据类型：
>
> - geo_point：由纬度（latitude）和经度（longitude）确定的一个点。例如："32.8752345, 120.2981576"
> - geo_shape：有多个 geo_point 组成的复杂几何图形。例如一条直线，"LINESTRING (-77.03653 38.897676, -77.009051 38.889939)"

### 修改索引库

倒排索引结构虽然不复杂，但是一旦数据结构改变（比如改变了分词器），就需要重新创建倒排索引，这简直是灾难。因此索引库**一旦创建，无法修改 mapping**

虽然无法修改 mapping 中已有的字段，但是却允许添加新的字段到 mapping 中，不会对倒排索引产生影响。

```json
PUT /索引库名/_mapping
{
  "properties": {
    "新字段名":{
      "type": "integer"
    }
  }
}
```

### 删除索引库

```json
DELETE /索引库名
```

### 查询索引库

```json
GET /数据库名
```

## DSL文档操作

### 新增文档

```json
POST /索引库名/_doc/文档id
{
    "字段1": "值1",
    "字段2": "值2",
    "字段3": {
        "子属性1": "值3",
        "子属性2": "值4"
    }
    // ...
}
```

```json
POST /xn2001/_doc/1
{
    "info": "我不会Java",
    "email": "jialna@qq.com",
    "name": {
        "firstName": "钟",
        "lastName": "弟弟"
    }
}
```

### 修改文档

修改文档有两种方式：

- 全量修改：直接覆盖原来的文档
- 增量修改：修改文档中的部分字段

**全量修改**是覆盖原来的文档，其本质是：

- 根据指定的 id 删除文档
- 新增一个相同 id 的文档

**注意**：如果根据 id 删除时，id 不存在，第二步的新增也会执行，也就是变成了新增操作

```json
PUT /{索引库名}/_doc/id
{
    "字段1": "值1",
    "字段2": "值2",
    // ... 略
}
```

```json
PUT /xn2001/_doc/1
{
    "info": "我也不会敲代码",
    "email": "3300123589@qq.com",
    "name": {
        "firstName": "弟弟",
        "lastName": "钟"
    }
}
```

**增量修改**是只修改指定 id 匹配的文档中的部分字段

```json
POST /{索引库名}/_update/文档id
{
    "doc": {
         "字段名": "新的值",
    }
}
```

```json
POST /heima/_update/1
{
  "doc": {
    "email": "update@qq.com"
  }
}
```

### 查询文档

```json
GET /{索引库名称}/_doc/{id}
```

### 删除文档

```json
DELETE /{索引库名}/_doc/{id}
```

## RestClient文档操作

ES 官方提供了各种不同语言的客户端，用来操作 ES。这些客户端的本质就是组装 DSL 语句，通过 http 请求发送给 ES。官方文档地址：https://www.elastic.co/guide/en/elasticsearch/client/index.html

其中的Java Rest Client又包括两种：

- Java Low Level Rest Client
- Java High Level Rest Client

![](https://cdn.xn2001.com/img/2021/20210919234405.png)

我们下面学习的是 **Java HighLevel Rest Client 客户端 API**

### 初始化RestClient

在 elasticsearch 提供的 API 中，elasticsearch 一切交互都封装在一个名为 RestHighLevelClient 的类中，必须先完成这个对象的初始化，建立与 elasticsearch 的连接。

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
</dependency>
```

SpringBoot 默认的 ES 版本是 7.6.2，我们需要覆盖默认的ES版本

```xml
<properties>
    <java.version>1.8</java.version>
    <elasticsearch.version>7.12.1</elasticsearch.version>
</properties>
```

初始化 RestHighLevelClient，初始化的代码如下：

```java
RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
        HttpHost.create("http://192.168.211.128:9200")
));
```

我们创建一个测试类 HotelIndexTest，然后将初始化的代码编写在 `@BeforeEach` 方法

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
public class HotelIndexTest {

    private RestHighLevelClient restHighLevelClient;

    @Test
    void testInit(){
        System.out.println(this.restHighLevelClient);
    }

    @BeforeEach
    void init(){
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### 创建索引库

```java
@Test
void createHotelIndex() throws IOException {
    //指定索引库名
    CreateIndexRequest hotel = new CreateIndexRequest("hotel");
    //写入JSON数据，这里是Mapping映射
    hotel.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
    //创建索引库
    restHighLevelClient.indices().create(hotel, RequestOptions.DEFAULT);
}
```

```java
public class HotelConstants {
    public static String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
```

### 删除索引库

```java
@Test
void deleteHotelIndex() throws IOException {
    DeleteIndexRequest hotel = new DeleteIndexRequest("hotel");
    restHighLevelClient.indices().delete(hotel,RequestOptions.DEFAULT);
}
```

### 判断索引库

```java
@Test
void existHotelIndex() throws IOException {
    GetIndexRequest hotel = new GetIndexRequest("hotel");
    boolean exists = restHighLevelClient.indices().exists(hotel, RequestOptions.DEFAULT);
    System.out.println(exists);
}
```

### 新增文档

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
@SpringBootTest
public class HotelDocumentTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    void testInit(){
        System.out.println(this.restHighLevelClient);
    }

    @Test
    void createHotelIndex() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 1.准备Request对象
        IndexRequest hotelIndex = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        // 2.准备Json文档
        hotelIndex.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 3.发送请求
        restHighLevelClient.index(hotelIndex, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void init(){
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### 查询文档

```java
@Test
void testGetDocumentById() throws IOException {
    // 1.准备Request
    GetRequest hotel = new GetRequest("hotel", "61083");
    // 2.发送请求，得到响应
    GetResponse hotelResponse = restHighLevelClient.get(hotel, RequestOptions.DEFAULT);
    // 3.解析响应结果
    String hotelDocSourceAsString = hotelResponse.getSourceAsString();
    // 4.json转实体类
    HotelDoc hotelDoc = JSON.parseObject(hotelDocSourceAsString, HotelDoc.class);
    System.out.println(hotelDoc);
}
```

### 删除文档

```java
@Test
void testDeleteDocumentById() throws IOException {
    DeleteRequest hotel = new DeleteRequest("hotel", "61083");
    restHighLevelClient.delete(hotel,RequestOptions.DEFAULT);
}
```

### 修改文档

前面我们说过，修改文档有两种方式：

- 全量修改：直接覆盖原来的文档
- 增量修改：修改文档中的部分字段

在 RestClient 的 API 中，全量修改与新增的 API 完全一致，判断依据是 ID

- 如果新增时，ID已经存在，则修改
- 如果新增时，ID不存在，则新增

所以全量修改写法与新增文档一样，下面我们主要是介绍增量修改。

```java
@Test
void testUpdateDocument() throws IOException {
    // 1.准备Request
    UpdateRequest request = new UpdateRequest("hotel", "61083");
    // 2.准备请求参数
    request.doc(
        "price", "952",
        "starName", "四钻"
    );
    // 3.发送请求
    restHighLevelClient.update(request, RequestOptions.DEFAULT);
}
```

### 批量导入文档

案例需求：利用 `BulkRequest` 批量将数据库数据导入到索引库中。

- 利用 mybatis-plus 查询酒店数据

- 将查询到的酒店数据（Hotel）转换为文档类型数据（HotelDoc）

- 利用 JavaRestClient 中的 BulkRequest 批处理，实现批量新增文档

批量处理 BulkRequest，其本质就是将多个普通的 CRUD 请求组合在一起发送。

因此Bulk中添加了多个IndexRequest，就是批量新增功能了。示例：

![](https://cdn.xn2001.com/img/2021/20210919234350.png)

利用这一点，我们可以写出自己需要的代码，如下

```java
@Test
void testBulk() throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    List<Hotel> hotelList = hotelService.list();
    hotelList.forEach(item -> {
        HotelDoc hotelDoc = new HotelDoc(item);
        bulkRequest.add(new IndexRequest("hotel")
                .id(hotelDoc.getId().toString())
                .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
    });
    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
}
```

---

总之，在 Java 代码中，client 针对操作索引库还是文档，基本都是一样的代码

restHighLevelClient.indices().xxx，代表操作索引库

restHighLevelClient.xxx，代表操作文档

而其中所需要的参数，我们直接通过 **ctrl+p** 这样的快捷键去查看就可以，不需要单独记住。

## DSL文档查询

Elasticsearch 提供了基于 JSON 的 DSL([Domain Specific Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html))来定义查询。常见的查询类型包括：

**查询所有**：查询出所有数据，一般测试用。例如：match_all

**全文检索（full text）查询**：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如：

- match_query
- multi_match_query

**精确查询**：根据精确词条值查找数据，一般是查找 keyword、数值、日期、boolean 等类型字段。例如：

- ids
- range
- term

**地理（geo）查询**：根据经纬度查询。例如：

- geo_distance
- geo_bounding_box

**复合（compound）查询**：复合查询可以将上述各种查询条件组合起来，合并查询条件。例如：

- bool
- function_score

---

```json
// 查询所有
GET /indexName/_search
{
  "query": {
    "match_all": {
    }
  }
}
```

### 全文检索

使用场景：全文检索查询的基本流程如下：

- 对用户搜索的内容做分词，得到词条
- 根据词条去倒排索引库中匹配，得到文档id
- 根据文档id找到文档，返回给用户

比较常用的场景包括：

- 商城的输入框搜索
- 百度输入框搜索

例如京东：

![](C:\Users\54656\Desktop\SpringCloud\实用篇\day01-SpringCloud01\资料\微服务技术栈.assets\image-20210721165326938.png)

因为是拿着词条去匹配，因此参与搜索的字段也必须是可分词的text类型的字段。

常见的全文检索查询包括：

- match 查询：单字段查询
- multi_match 查询：多字段查询，任意一个字段符合条件就算符合查询条件

match 查询语法如下：

```json
GET /indexName/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT"
    }
  }
}
```

mulit_match 查询语法如下：

```json
GET /indexName/_search
{
  "query": {
    "multi_match": {
      "query": "TEXT",
      "fields": ["FIELD1", " FIELD12"]
    }
  }
}
```

因为我们将 brand、name、business 值都利用 **copy_to** 复制到了 **all** 字段中，你根据三个字段搜索，和根据 all字段搜索效果是一样的。

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "7天酒店"
    }
  }
}
```

```json
GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "7天酒店",
      "fields": ["brand","name"]
    }
  }
}
```

**搜索字段越多，对查询性能影响越大，因此建议采用 copy_to 将多个字段合并为一个，然后使用单字段查询的方式。**

### 精准查询

精确查询一般是查找 keyword、数值、日期、boolean 等类型字段。所以**不会**对搜索条件分词。

- term：根据词条精确值查询
- range：根据值的范围查询

#### term查询

因为精确查询的字段搜是不分词的字段，因此查询的条件也必须是**不分词**的词条。查询时，用户输入的内容跟自动值完全匹配时才认为符合条件。如果用户输入的内容过多，反而搜索不到数据。

语法说明：

```json
// term查询
GET /indexName/_search
{
  "query": {
    "term": {
      "FIELD": {
        "value": "VALUE"
      }
    }
  }
}
```

示例：

```json
GET /hotel/_search
{
  "query": {
    "term": {
      "brand": {
        "value": "7天酒店"
      }
    }
  }
}
```

#### range查询

范围查询，一般应用在对数值类型做范围过滤的时候。比如做价格范围过滤。

基本语法：

```json
// range查询
GET /indexName/_search
{
  "query": {
    "range": {
      "FIELD": {
        "gte": 10, // 这里的gte代表大于等于，gt则代表大于
        "lte": 20 // lte代表小于等于，lt则代表小于
      }
    }
  }
}
```

示例：

![](https://cdn.xn2001.com/img/2021/20210921182858.png)

精确查询常见的有哪些？

- term 查询：根据词条精确匹配，一般搜索 keyword 类型、数值类型、布尔类型、日期类型字段
- range 查询：根据数值范围查询，可以是数值、日期的范围

### 地理坐标查询

地理坐标查询，其实就是根据经纬度查询，官方文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-queries.html

常见的使用场景包括：

- 携程：搜索我附近的酒店
- 滴滴：搜索我附近的出租车
- 微信：搜索我附近的人

附近的酒店：

![](https://cdn.xn2001.com/img/2021/20210921183030.png) 

附近的车：

![](https://cdn.xn2001.com/img/2021/20210921183033.png) 

> 矩形范围查询

矩形范围查询，也就是 `geo_bounding_box` 查询，查询坐标落在某个矩形范围的所有文档

![](https://cdn.xn2001.com/img/2021/20210921183124.gif)

查询时，需要指定矩形的**左上**、**右下**两个点的坐标，然后画出一个矩形，落在该矩形内的都是符合条件的点。

```json
// geo_bounding_box查询
GET /indexName/_search
{
  "query": {
    "geo_bounding_box": {
      "FIELD": {
        "top_left": { // 左上点
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": { // 右下点
          "lat": 30.9,
          "lon": 121.7
        }
      }
    }
  }
}
```

> 附近查询

附近查询，也叫做距离查询（geo_distance）：查询到指定中心点小于某个距离值的所有文档

在地图上找一个点作为圆心，以指定距离为半径，画一个圆，落在圆内的坐标都算符合条件：

![](https://cdn.xn2001.com/img/2021/20210921183215.gif)

```json
// geo_distance 查询
GET /indexName/_search
{
  "query": {
    "geo_distance": {
      "distance": "15km", // 半径
      "FIELD": "31.21,121.5" // 圆心
    }
  }
}
```

我们先搜索陆家嘴附近15km的酒店

![](https://cdn.xn2001.com/img/2021/20210921183228.png)

发现共有47家酒店，然后把半径缩短到3公里

![](https://cdn.xn2001.com/img/2021/20210921183240.png)

可以发现，搜索到的酒店数量减少到了5家。

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "location": "31.034661,121.612282", //圆心
          "order" : "asc", //排序
          "unit" : "km" //单位
      }
    }
  ]
}
```

结果为：

```json
"hits" : [
    {
        "_index" : "hotel",
        "_type" : "_doc",
        "_id" : "2056298828",
        "_score" : null,
        "_source" : {
            ...
        },
        "sort" : [
            4.8541199685347785 //这里的结果为离圆心的距离
        ]
    },
```

注意：输出结果中的 **sort** 为距离，比较常用。

排序完成后，页面还要获取我附近每个酒店的具体**距离**值，这个值在响应结果中是独立的：

![](https://cdn.xn2001.com/img/2021/20211019011334.png)

### 复合查询

复合（compound）查询：复合查询可以将其它简单查询组合起来，实现更复杂的搜索逻辑。

- fuction score：算分函数查询，可以控制文档相关性算分，控制文档排名
- bool query：布尔查询，利用逻辑关系组合多个其它的查询，实现复杂搜索

### 相关性算分

> 这部分内容作为了解即可。

当我们利用 match 查询时，文档结果会根据与搜索词条的关联度打分（_score），返回结果时按照分值降序排列。例如，我们搜索 "虹桥如家"，结果如下：

```json
[
  {
    "_score" : 17.850193,
    "_source" : {
      "name" : "虹桥如家酒店真不错",
    }
  },
  {
    "_score" : 12.259849,
    "_source" : {
      "name" : "外滩如家酒店真不错",
    }
  },
  {
    "_score" : 11.91091,
    "_source" : {
      "name" : "迪士尼如家酒店真不错",
    }
  }
]
```

elasticsearch 早期使用的打分算法是 **TF-IDF 算法**，公式如下：

![](https://cdn.xn2001.com/img/2021/20210921205752.png)

在后来的5.1版本升级中，elasticsearch 将算法改进为 **BM25 算法**，公式如下：

![](https://cdn.xn2001.com/img/2021/20210921205757.png)

TF-IDF 算法有一各缺陷，就是词条频率越高，文档得分也会越高，单个词条对文档影响较大。而 BM25 则会让单个词条的算分有一个上限，曲线更加平滑：

![](https://cdn.xn2001.com/img/2021/20210921205831.png)

### 算分函数查询

根据相关度打分是比较合理的需求，但有时候也不能够满足我们的需求。

以百度为例，你搜索的结果中，并不是相关度越高排名越靠前，而是谁给的钱多排名就越靠前。

**要想认为控制相关性算分，就需要利用 elasticsearch 中的 function score 查询了。**

![](https://cdn.xn2001.com/img/2021/20210921210256.png)

function score 查询中包含四部分内容：

- **原始查询**条件：query 部分，基于这个条件搜索文档，并且基于BM25算法给文档打分，**原始算分**（query score)
- **过滤条件**：filter 部分，符合该条件的文档才会**重新算分**
- **算分函数**：符合 filter 条件的文档要根据这个函数做运算，得到的**函数算分**（function score），有四种函数
  - weight：函数结果是常量
  - field_value_factor：以文档中的某个字段值作为函数结果
  - random_score：以随机数作为函数结果
  - script_score：自定义算分函数算法
- **运算模式**：算分函数的结果、原始查询的相关性算分，两者之间的运算方式，包括：
  - multiply：相乘
  - replace：用 function score 替换 query score
  - sum、avg、max、min

function score 的运行流程如下：

1. 根据**原始条件**查询搜索文档，并且计算相关性算分，称为**原始算分**（query score）
2. 根据**过滤条件**，过滤文档
3. 符合**过滤条件**的文档，基于**算分函数**运算，得到**函数算分**（function score）
4. 将**原始算分**（query score）和**函数算分**（function score）基于**运算模式**做运算，得到最终结果，作为相关性算分。

因此，其中的关键点是

- 过滤条件：决定哪些文档的算分被修改
- 算分函数：决定函数算分的算法
- 运算模式：决定最终算分结果

例如：我们给“如家”这个品牌的酒店排名靠前一些

```json
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {  .... }, // 原始查询，可以是任意条件
      "functions": [ // 算分函数
        {
          "filter": { // 满足的条件，品牌必须是如家
            "term": {
              "brand": "如家"
            }
          },
          "weight": 10 // 算分权重为10
        }
      ],
      "boost_mode": "sum" // 加权模式，求和
    }
  }
}
```

测试，在未添加算分函数时，如家得分如下

![](https://cdn.xn2001.com/img/2021/20210921231508.png)

添加了算分函数后，如家得分就提升了

![](https://cdn.xn2001.com/img/2021/20210921231513.png)

### 布尔查询

布尔查询是一个或多个查询子句的组合，每一个子句就是一个**子查询**。子查询的组合方式有

- must：必须匹配每个子查询，类似“与”
- should：选择性匹配子查询，类似“或”
- must_not：必须不匹配，**不参与算分**，类似“非”
- filter：必须匹配，**不参与算分**

比如在搜索酒店时，除了关键字搜索外，我们还可能根据品牌、价格、城市等字段做过滤

**每一个不同的字段，其查询的条件、方式都不一样，必须是多个不同的查询，而要组合这些查询，就必须用 bool查询了。**

需要注意的是，搜索时，参与**打分的字段越多，查询的性能也越差**。因此这种多条件查询时，建议这样做：

- 搜索框的关键字搜索，是全文检索查询，使用 must 查询，参与算分
- 其它过滤条件，采用 filter 查询，不参与算分

```json
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {"city": "上海" }}
      ],
      "should": [
        {"term": {"brand": "皇冠假日" }},
        {"term": {"brand": "华美达" }}
      ],
      "must_not": [
        { "range": { "price": { "lte": 500 } }}
      ],
      "filter": [
        { "range": {"score": { "gte": 45 } }}
      ]
    }
  }
}
```

需求：搜索名字包含“如家”，价格不高于 400，在坐标 31.21,121.5 周围 10km 范围内的酒店。

- 名称搜索，属于全文检索查询，应该参与算分，放到 must 中
- 价格不高于 400，用 range 查询，属于过滤条件，不参与算分，放到 must_not 中
- 周围 10km 范围内，用 geo_distance 查询，属于过滤条件，不参与算分，放到 filter 中

![](https://cdn.xn2001.com/img/2021/20210921233252.png)

bool 查询的几种逻辑关系

- must：必须匹配的条件，可以理解为“与”
- should：选择性匹配的条件，可以理解为“或”
- must_not：必须不匹配的条件，不参与打分
- filter：必须匹配的条件，不参与打分

## 搜索结果处理

### 排序

elasticsearch 默认是根据相关度算分（_score）来排序，但是也支持自定义方式对搜索[结果排序](https://www.elastic.co/guide/en/elasticsearch/reference/current/sort-search-results.html)。可以排序字段类型有：keyword 类型、数值类型、地理坐标类型、日期类型等

keyword、数值、日期类型排序的语法基本一致。

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "FIELD": "desc"  // 排序字段、排序方式ASC、DESC
    }
  ]
}
```

排序条件是一个数组，也就是可以写多个排序条件。按照声明的顺序，当第一个条件相等时，再按照第二个条件排序。

需求描述：酒店数据按照用户评价（score)降序排序，评价相同的按照价格(price)升序排序

![](https://cdn.xn2001.com/img/2021/20210921233829.png)

地理坐标排序略有不同

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "FIELD" : "纬度，经度", // 文档中geo_point类型的字段名、目标坐标点
          "order" : "asc", // 排序方式
          "unit" : "km" // 排序的距离单位
      }
    }
  ]
}
```

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "location": "31.034661,121.612282", 
          "order" : "asc", 
          "unit" : "km" 
      }
    }
  ]
}
```

> 获取你的位置的经纬度的方式：https://lbs.amap.com/demo/jsapi-v2/example/map/click-to-get-lnglat

假设我的位置是：31.034661，121.612282，寻找我周围距离最近的酒店。

![](https://cdn.xn2001.com/img/2021/20210921233931.png)

### 分页

elasticsearch 默认情况下只返回 top10 的数据。而如果要查询更多数据就需要修改分页参数了。

elasticsearch 通过修改 from、size 参数来控制要返回的分页结果：

- from：从第几个文档开始
- size：总共查询几个文档

类似于mysql中的`limit ?, ?`

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

> 深度分页问题

现在，我要查询990~1000的数据，查询逻辑要这么写

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 990, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

这里是查询990开始的数据，也就是 第990~第1000条 数据。

注意：elasticsearch 内部分页时，必须先查询 0~1000条，然后截取其中的 990 ~ 1000 的这10条

![](https://cdn.xn2001.com/img/2021/20210921234503.png)

查询TOP1000，如果 es 是单点模式，这并无太大影响。

但是 elasticsearch 将来一定是集群，例如我集群有5个节点，我要查询 TOP1000 的数据，并不是每个节点查询200条就可以了。节点A的 TOP200，在另一个节点可能排到10000名以外了。

**因此要想获取整个集群的 TOP1000，必须先查询出每个节点的 TOP1000，汇总结果后，重新排名，重新截取  TOP1000。**

![](https://cdn.xn2001.com/img/2021/20210921234555.png)

**当查询分页深度较大时，汇总数据过多，对内存和CPU会产生非常大的压力，因此 elasticsearch 会禁止from+ size 超过10000的请求。**

针对深度分页，ES提供了两种解决方案，[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html)：

- search after：分页时需要排序，原理是从上一次的排序值开始，查询下一页数据。官方推荐使用的方式。
- scroll：原理将排序后的文档id形成快照，保存在内存。官方已经不推荐使用。

---

分页查询的常见实现方案以及优缺点

- `from + size`
  - 优点：支持随机翻页
  - 缺点：深度分页问题，默认查询上限（from + size）是10000
  - 场景：百度、京东、谷歌、淘宝这样的随机翻页搜索
- `after search`
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：只能向后逐页查询，不支持随机翻页
  - 场景：没有随机翻页需求的搜索，例如手机向下滚动翻页

- `scroll`
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：会有额外内存消耗，并且搜索结果是非实时的
  - 场景：海量数据的获取和迁移。从ES7.1开始不推荐，建议用 after search方案。

### 高亮

我们在百度，京东搜索时，关键字会变成红色，比较醒目，这叫高亮显示：

![](https://cdn.xn2001.com/img/2021/20210921234711.png)

高亮显示的实现分为两步：

- 1）给文档中的所有关键字都添加一个标签，例如`<em>`标签
- 2）页面给`<em>`标签编写CSS样式

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT" // 查询条件，高亮一定要使用全文检索查询
    }
  },
  "highlight": {
    "fields": { // 指定要高亮的字段
      "FIELD": {
        "pre_tags": "<em>",  // 用来标记高亮字段的前置标签
        "post_tags": "</em>" // 用来标记高亮字段的后置标签
      }
    }
  }
}
```

**注意**：

- 高亮是对关键字高亮，因此**搜索条件必须带有关键字**，而不能是范围这样的查询。
- 默认情况下，**高亮的字段，必须与搜索指定的字段一致**，否则无法高亮
- 如果要对非搜索字段高亮，则需要添加一个属性：`required_field_match=false`

![](https://cdn.xn2001.com/img/2021/20210921234739.png)

> DSL 总体结构如下：

![](https://cdn.xn2001.com/img/2021/20210921235330.png)

## RestClient文档查询

### 发起查询请求

![](https://cdn.xn2001.com/img/2021/20211016170057.png)

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/16 17:05
 */
@SpringBootTest
public class HotelSearchTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    public void match_All() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

- 第一步，创建`SearchRequest`对象，指定索引库名
- 第二步，利用`request.source()`构建 DSL，DSL 中可以包含查询、分页、排序、高亮等
  - `query()`：代表查询条件，利用 `QueryBuilders.matchAllQuery()` 构建一个 match_all 查询的 DSL
- 第三步，利用 `client.search()` 发送请求，得到响应

关键的 API 有两个，一个是 `request.source()`，其中包含了查询、排序、分页、高亮等所有功能

![](https://cdn.xn2001.com/img/2021/20211016170203.png)

另一个是 `QueryBuilders`，其中包含 match、term、function_score、bool 等各种查询

![](https://cdn.xn2001.com/img/2021/20211016170234.png)

### 解析查询响应

![](https://cdn.xn2001.com/img/2021/20211016174631.png)

Elasticsearch 返回的结果是一个 JSON 字符串，结构包含

- `hits`：命中的结果
  - `total`：总条数，其中的value是具体的总条数值
  - `max_score`：所有结果中得分最高的文档的相关性算分
  - `hits`：搜索结果的文档数组，其中的每个文档都是一个 json 对象
    - `_source`：文档中的原始数据，也是 json 对象

因此，我们解析响应结果，就是逐层解析 JSON 字符串，流程如下

- `SearchHits`：通过 `response.getHits()` 获取，就是 json 中的最外层的 hits，代表命中的结果
  - `SearchHits.getTotalHits().value`：获取总条数信息
  - `SearchHits.getHits()`：获取 SearchHit 数组，也就是文档数组
    - `SearchHit.getSourceAsString()`：获取文档结果中的 `_source`，也就是原始的 json 文档数据

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/16 17:05
 */
@SpringBootTest
public class HotelSearchTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    public void match_All() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @BeforeEach
    void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### match查询

![](https://cdn.xn2001.com/img/2021/20211016182859.png)

```java
@Test
public void matchQuery() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source()
            .query(QueryBuilders.matchQuery("all","如家"));
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}

@Test
public void multiMatchQuery() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source()
            .query(QueryBuilders.multiMatchQuery("如家","name","brand"));
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 精确查询

 精确查询主要是两者

- term：词条精确匹配
- range：范围查询

![](https://cdn.xn2001.com/img/2021/20211016192658.png) 

### 布尔查询

布尔查询是用 must、must_not、filter等方式组合其它查询，代码示例如下

![](https://cdn.xn2001.com/img/2021/20211016192745.png)

```java
@Test
void testBool() throws IOException {
    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    request.source()
            .query(
                    QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery("city", "上海"))
                            .filter(QueryBuilders.rangeQuery("price").lte(300))
            );
    // 3.发送请求
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 排序、分页

搜索结果的排序和分页是与 query 同级的参数，因此同样是使用 `request.source()` 来设置。

对应的API如下

![](https://cdn.xn2001.com/img/2021/20211016202447.png)

```java
@Test
void testPageAndSort() throws IOException {
    // 页码，每页大小
    int page = 1, size = 5;

    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    // 2.1.query
    request.source().query(QueryBuilders.matchAllQuery());
    // 2.2.排序 sort
    request.source().sort("price", SortOrder.ASC);
    // 2.3.分页 from、size
    request.source().from((page - 1) * size).size(5);
    // 3.发送请求
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 高亮

- 查询的 DSL：其中除了查询条件，还需要添加高亮条件，同样是与 query 同级。
- 结果解析：结果除了要解析 `_source` 文档数据，还要解析高亮结果

**高亮请求的构建 API**

![](https://cdn.xn2001.com/img/2021/20211016202707.png)

上述代码省略了查询条件部分，但是高亮查询必须使用全文检索查询，并且要有搜索关键字，将来才可以对关键字高亮.

```java
@Test
void testHighlight() throws IOException {
    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    // 2.1.query
    request.source().query(QueryBuilders.matchQuery("all", "如家"));
    // 2.2.高亮
    request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
    // 3.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    handleResponse(response); //代码在下文
}
```

**高亮结果解析**

高亮的结果与查询的文档结果默认是分离的，并不在一起。

因此解析高亮的代码需要额外处理：

![](https://cdn.xn2001.com/img/2021/20211016202853.png)

- 第一步：从结果中获取 source。`hit.getSourceAsString()`，这部分是非高亮结果，json 字符串，需要反序列为 HotelDoc 对象
- 第二步：获取高亮结果。`hit.getHighlightFields()`，返回值是一个 Map，key 是高亮字段名称，值是HighlightField 对象，代表高亮值
- 第三步：从 map 中根据高亮字段名称，获取高亮字段值对象 HighlightField
- 第四步：从 HighlightField 中获取 Fragments，并且转为字符串。**这部分是真正的高亮字符串**
- 第五步：用高亮的结果替换 HotelDoc 中的非高亮结果

完整代码如下：

```java
private void handleResponse(SearchResponse response) {
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    // 4.1.获取总条数
    long total = searchHits.getTotalHits().value;
    System.out.println("共搜索到" + total + "条数据");
    // 4.2.文档数组
    SearchHit[] hits = searchHits.getHits();
    // 4.3.遍历
    for (SearchHit hit : hits) {
        // 获取文档source
        String json = hit.getSourceAsString();
        // 反序列化
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        // 获取高亮结果
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        if (!CollectionUtils.isEmpty(highlightFields)) {
            // 根据字段名获取高亮结果
            HighlightField highlightField = highlightFields.get("name");
            if (highlightField != null) {
                // 获取高亮值
                String name = highlightField.getFragments()[0].string();
                // 覆盖非高亮结果
                hotelDoc.setName(name);
            }
        }
        System.out.println("hotelDoc = " + hotelDoc);
    }
}
```

### 地理坐标查询

![](https://cdn.xn2001.com/img/2021/20211019000540.png)

### 相关性得分

function_score 查询结构如下

![](https://cdn.xn2001.com/img/2021/20211019011433.png)



对应的 JavaAPI 如下

![](https://cdn.xn2001.com/img/2021/20211019011439.png)

## DSL数据聚合

**[聚合（](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[）](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)**可以让我们极其方便的实现对数据的统计、分析、运算。

- 什么品牌的手机最受欢迎？
- 这些手机的平均价格、最高价格、最低价格？
- 这些手机每月的销售情况如何？

在 Elasticsearch 实现这些统计功能比数据库的 sql 要方便的多，而且查询速度非常快，可以实现近实时搜索效果。

聚合常见的有三类

- **桶（Bucket）**聚合：用来对文档做分组
  - TermAggregation：按照文档字段值分组，例如按照品牌值分组、按照国家分组
  - Date Histogram：按照日期阶梯分组，例如一周为一组，或者一月为一组

- **度量（Metric）**聚合：用以计算一些值，比如：最大值、最小值、平均值等
  - Avg：求平均值
  - Max：求最大值
  - Min：求最小值
  - Stats：同时求 max、min、avg、sum 等
- **管道（pipeline）**聚合：其它聚合的结果为基础做聚合

> **注意**：参加聚合的字段必须是keyword、日期、数值、布尔类型

### Bucket聚合语法

例如：我们要统计所有数据中的酒店品牌有几种，其实就是按照品牌对数据分组。此时可以根据酒店品牌的名称做聚合，也就是 Bucket 聚合。

```json
GET /hotel/_search
{
  "size": 0,  // 设置size为0，结果中不包含文档，只包含聚合结果
  "aggs": { // 定义聚合
    "brandAgg": { //给聚合起个名字
      "terms": { // 聚合的类型，按照品牌值聚合，所以选择term
        "field": "brand", // 参与聚合的字段
        "size": 20 // 希望获取的聚合结果数量
      }
    }
  }
}
```

![](https://cdn.xn2001.com/img/2021/20211022184007.png)

默认情况下，Bucket 聚合会统计 Bucket 内的文档数量，记为 `_count`，并且按照 `_count` 降序排序。

我们可以指定 order 属性，自定义聚合的排序方式

```json
GET /hotel/_search
{
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "order": {
          "_count": "asc" // 按照_count升序排列
        },
        "size": 20
      }
    }
  }
}
```

默认情况下，Bucket 聚合是对索引库的所有文档做聚合，但真实场景下，用户会输入搜索条件，因此聚合必须是对搜索结果聚合。那么聚合必须添加限定条件。

我们可以限定要聚合的文档范围，只要添加 query 条件即可；

```json
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "lte": 200 // 只对200元以下的文档聚合
      }
    }
  }, 
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20
      }
    }
  }
}
```

这次，聚合得到的品牌明显变少了

![](https://cdn.xn2001.com/img/2021/20211022184549.png)

### Metric聚合语法

上面，我们对酒店按照品牌分组，形成了一个个桶。现在我们需要对桶内的酒店做运算，获取每个品牌的用户评分的 min、max、avg 等值。

这就要用到 Metric 聚合了，例如 stats 聚合：就可以获取 min、max、avg 等结果

```json
GET /hotel/_search
{
  "size": 0, 
  "aggs": {
    "brandAgg": { 
      "terms": { 
        "field": "brand", 
        "size": 20
      },
      "aggs": { // 是brands聚合的子聚合，也就是分组后对每组分别计算
        "score_stats": { // 聚合名称
          "stats": { // 聚合类型，这里stats可以计算min、max、avg等
            "field": "score" // 聚合字段，这里是score
          }
        }
      }
    }
  }
}
```

这次的 score_stats 聚合是在 brandAgg 的聚合内部嵌套的子聚合。因为我们需要在每个桶分别计算。

另外，我们还可以给聚合结果做个排序，例如按照每个桶的酒店平均分做排序

![](https://cdn.xn2001.com/img/2021/20211022184847.png)

## RestAPI数据聚合

聚合条件与 query 条件同级别，因此需要使用 `request.source()` 来指定聚合条件

![](https://cdn.xn2001.com/img/2021/20211022190429.png)

聚合的结果也与查询结果不同，API 也比较特殊。不过同样是 JSON 逐层解析

![](https://cdn.xn2001.com/img/2021/20211022190457.png)

```java
@Test
public void testAggregation() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source().aggregation(AggregationBuilders.terms("brandAgg").field("brand").size(20));
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    Terms brandAgg = response.getAggregations().get("brandAgg");
    List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
    for (Terms.Bucket bucket : buckets) {
        String key = bucket.getKeyAsString();
        System.out.println("key = " + key);
    }
}
```

## 自动补全

当用户在搜索框输入字符时，我们应该提示出与该字符有关的搜索项，提示完整词条的功能，就是自动补全了。

### 拼音分词器

如果我们需要根据拼音字母来推断，因此要用到拼音分词功能。

要实现根据字母做补全，就必须对文档按照拼音分词。插件地址：https://github.com/medcl/elasticsearch-analysis-pinyin

![](https://cdn.xn2001.com/img/2021/20211023015636.png)

使用 `docker volume inspect es-plugins` 查看插件目录，将下载的文件解压上传，重启 Elasticsearch

测试用法如下：

```json
POST /_analyze
{
  "text": "如家酒店还不错",
  "analyzer": "pinyin"
}
```

结果：

![](https://cdn.xn2001.com/img/2021/20211023021713.png) 



### 自定义分词器

默认的拼音分词器会将每个汉字单独分为拼音，而我们希望的是每个词条形成一组拼音，需要对拼音分词器做个性化定制，形成自定义分词器。

elasticsearch 中分词器（analyzer）的组成包含三部分：

- character filters：在 tokenizer 之前对文本进行处理。例如删除字符、替换字符
- tokenizer：将文本按照一定的规则切割成词条（term）。例如 keyword，就是不分词；还有 ik_smart
- tokenizer filter：将 tokenizer 输出的词条做进一步处理。例如大小写转换、同义词处理、拼音处理等

文档分词时会依次由这三部分来处理文档：

   ![](https://cdn.xn2001.com/img/2021/20211023021451.png)

声明自定义分词器的语法如下：

```json
PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { // 自定义分词器
        "my_analyzer": {  // 分词器名称
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },
      "filter": { // 自定义tokenizer filter
        "py": { // 过滤器名称
          "type": "pinyin", // 过滤器类型，这里是pinyin
		  "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}
```

测试

![](https://cdn.xn2001.com/img/2021/20211023021532.png)

注意**：为了避免搜索到同音字，搜索时不要使用拼音分词器**

![](https://cdn.xn2001.com/img/2021/20211023022355.png)

### 自动补全查询

elasticsearch 提供了 [Completion Suggester](https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-suggesters.html) 查询来实现自动补全功能。这个查询会匹配以用户输入内容开头的词条并返回；为了提高补全查询的效率，对于文档中字段的类型有一些约束

- 参与补全查询的字段必须是 completion 类型。

- 字段的内容一般是用来补全的多个词条形成的数组。

```json
// 创建索引库
PUT test
{
  "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}
```

然后插入下面的数据

```json
// 示例数据
POST test/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test/_doc
{
  "title": ["Nintendo", "switch"]
}
```

查询的 DSL 语句如下

```json
// 自动补全查询
GET /test/_search
{
  "suggest": {
    "title_suggest": {
      "text": "s", // 关键字
      "completion": {
        "field": "title", // 补全查询的字段
        "skip_duplicates": true, // 跳过重复的
        "size": 10 // 获取前10条结果
      }
    }
  }
}
```

例如一个酒店的索引库完整案例

```json
// 酒店数据索引库
PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}
```

### JavaAPI

![](https://cdn.xn2001.com/img/2021/20211025113007.png)



解析响应的代码如下

![](https://cdn.xn2001.com/img/2021/20211025113011.png)

## 数据同步

elasticsearch 中的数据来自于 mysq l数据库，因此 mysql 数据发生改变时，elasticsearch 也必须跟着改变，这个就是 elasticsearch 与 mysql 之间的**数据同步**



![](https://cdn.xn2001.com/img/2021/20211025163219.png)

常见的数据同步方案有三种

- 同步调用
- 异步通知
- 监听 binlog

### 同步调用

方案一：同步调用

![](https://cdn.xn2001.com/img/2021/20211025163313.png)

- hotel-demo对外提供接口，用来修改 elasticsearch 中的数据
- 酒店管理服务在完成数据库操作后，直接调用 hotel-demo 提供的接口

### 异步通知

方案二：异步通知

![](https://cdn.xn2001.com/img/2021/20211025163346.png)

- hotel-admin 对 mysql 数据库数据完成增、删、改后，发送 MQ 消息
- hotel-demo监听 MQ，接收到消息后完成 elasticsearch 数据修改

### 监听binlog

方案三：监听binlog

![](https://cdn.xn2001.com/img/2021/20211025163428.png)

- mysql 开启 binlog 功能
- mysql 完成增、删、改操作都会记录在 binlog 中
- hotel-demo 基于canal 监听 binlog 变化，实时更新 elasticsearch 中的内容

### 优缺点

方式一：同步调用

- 优点：实现简单，粗暴
- 缺点：业务耦合度高

方式二：异步通知

- 优点：低耦合，实现难度一般
- 缺点：依赖 mq 的可靠性

方式三：监听binlog

- 优点：完全解除服务间耦合
- 缺点：开启 binlog 增加数据库负担、实现复杂度高

### 实现方式

我们以**异步通知**为例，使用 MQ 消息中间件

MQ结构如图：

![](https://cdn.xn2001.com/img/2021/20211025163734.png)

**引入依赖**，在 hotel-admin、hotel-demo 中引入 rabbitmq 的依赖：

```xml
<!--amqp-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**声明队列交换机**

```java
public class MQConstants {
    /**
     * 交换机
     */
    public final static String HOTEL_EXCHANGE = "hotel.topic";
    /**
     * 监听新增和修改的队列
     */
    public final static String HOTEL_INSERT_QUEUE = "hotel.insert.queue";
    /**
     * 监听删除的队列
     */
    public final static String HOTEL_DELETE_QUEUE = "hotel.delete.queue";
    /**
     * 新增或修改的RoutingKey
     */
    public final static String HOTEL_INSERT_KEY = "hotel.insert";
    /**
     * 删除的RoutingKey
     */
    public final static String HOTEL_DELETE_KEY = "hotel.delete";
}
```

消息接收方


```java
@Configuration
public class MqConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MqConstants.HOTEL_EXCHANGE, true, false);
    }

    @Bean
    public Queue insertQueue() {
        return new Queue(MqConstants.HOTEL_INSERT_QUEUE, true);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(MqConstants.HOTEL_DELETE_QUEUE, true);
    }

    @Bean
    public Binding insertQueueBinding() {
        return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MqConstants.HOTEL_INSERT_KEY);
    }

    @Bean
    public Binding deleteQueueBinding() {
        return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MqConstants.HOTEL_DELETE_KEY);
    }
}
```

消息发送方

```java
rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_INSERT_KEY, hotel.getId());

rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_DELETE_KEY, id);
```

 消息接收方

```java
@Override
public void insertById(Long id) {
    try {
        // 根据id查询酒店数据
        Hotel hotel = getById(id);
        // 转换为文档类型
        HotelDoc hotelDoc = new HotelDoc(hotel);

        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

@Override
public void deleteById(Long id) {
    try {
        DeleteRequest request = new DeleteRequest("hotel", id.toString());
        client.delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

```java
@Component
public class HotelListener {

    @Autowired
    private HotelService hotelService;

    /**
     * 监听酒店新增或修改的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id) {
        hotelService.insertById(id);
    }

    /**
     * 监听酒店删除的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_DELETE_QUEUE)
    public void listenHotelDelete(Long id) {
        hotelService.deleteById(id);
    }
}
```

# Elasticsearch集群

单机的 Elasticsearch 做数据存储，必然面临两个问题：海量数据存储问题、单点故障问题。

解决方案：

- 海量数据存储问题：将索引库从逻辑上拆分为N个分片（shard），存储到多个节点
- 单点故障问题：将分片数据在不同节点备份（replica ）

**ES集群相关概念**：

* 集群（cluster）：一组拥有共同的 cluster name 的 节点。

* 节点（node)  ：集群中的一个 Elasticearch 实例

* 分片（shard）：索引可以被拆分为不同的部分进行存储，称为分片。**在集群环境下，一个索引的不同分片可以拆分到不同的节点中，解决数据量太大，单点存储量有限的问题。**

![](https://cdn.xn2001.com/img/2022/202205071006783.png)

> 此处，我们把数据分成3片：shard0、shard1、shard2
>
> 主分片（Primary shard）：相对于副本分片的定义。
>
> 副本分片（Replica shard）每个主分片可以有一个或者多个副本，数据和主分片一样。

数据备份可以保证高可用，但是每个分片备份一份在节点上，所需要的节点数量就会翻倍，成本太高。为了在高可用和成本间寻求平衡

- 首先对数据分片，存储到不同节点
- 然后对每个分片进行备份，放到对方节点，**完成互相备份**

这样可以大大减少所需要的服务节点数量，如图，我们以3分片，每个分片备份一份为例：

![](https://cdn.xn2001.com/img/2022/202205071006790.png)

现在，每个分片都有1个备份，存储在3个节点：

- node0：保存了分片0和1
- node1：保存了分片0和2
- node2：保存了分片1和2

## 部署集群

### 搭建Elasticsearch 

我们会在单机上利用 Docker 容器运行多个 Elasticsearch 实例来模拟集群。

可以直接使用 docker-compose 来完成，这要求你的Linux虚拟机至少有**4G**以上的内存空间。

docker-compose.yml

```yml
version: '2.2'
services:
  es01:
    image: elasticsearch:7.12.1
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic
  es02:
    image: elasticsearch:7.12.1
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data02:/usr/share/elasticsearch/data
    ports:
      - 9201:9200
    networks:
      - elastic
  es03:
    image: elasticsearch:7.12.1
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic
    ports:
      - 9202:9200
volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local
networks:
  elastic:
    driver: bridge
```

修改 Linux 系统权限，修改 `/etc/sysctl.conf` 文件

```sh
vi /etc/sysctl.conf
```

添加下面的内容

```sh
vm.max_map_count=262144
```

让配置生效：

```sh
sysctl -p
```

通过docker-compose启动集群

```sh
docker-compose up -d
```

### 集群状态监控

kibana 可以监控 Elasticsearch 集群，但是更推荐使用 [cerebro](https://github.com/lmenezes/cerebro)

下载解压打开 /bin/cerebro.bat

访问 http://localhost:9000 即可进入管理界面

![](https://cdn.xn2001.com/img/2022/202205071018030.png)

输入任意节点的地址和端口，点击 connect 

![](https://cdn.xn2001.com/img/2022/202205071018761.png)

绿色的线条代表集群处于绿色（健康状态）

### 创建索引库

我们还可以通过 cerebro 创建索引库，当然你需要使用 kibana 也可以。

![](https://cdn.xn2001.com/img/2022/202205071020771.png)

填写索引库信息

![](https://cdn.xn2001.com/img/2022/202205071023380.png)

回到首页，即可查看索引库分片效果

![](https://cdn.xn2001.com/img/2022/202205071024397.png)

## 集群职责划分

Elasticsearch 中集群节点有不同的职责划分

![](https://cdn.xn2001.com/img/2022/202205071029258.png)

**默认情况下，集群中的任何一个节点都同时兼职上述四种角色。**

真实的集群一定要将集群职责分离

- master 节点：对 CPU 要求高，但是内存要求低
- data 节点：对 CPU 和内存要求都高
- coordinating 节点：对网络带宽、CPU 要求高

职责分离可以让我们根据不同节点的需求分配不同的硬件去部署。而且避免业务之间的互相干扰。

![](https://cdn.xn2001.com/img/2022/202205071029263.png)



## 集群脑裂问题

脑裂是因为集群中的节点失联导致的。

例如一个集群中，主节点 node1 与其它节点失联。

![](https://cdn.xn2001.com/img/2022/202205071029267.png)

此时，node2 和 node3 认为 node1 宕机，就会重新选主。

![](https://cdn.xn2001.com/img/2022/202205071344676.png)

当 node3 当选后，集群继续对外提供服务，node2 和 node3 自成集群，node1 自成集群，两个集群数据不同步，出现数据差异。

当网络恢复后，因为集群中有两个 master 节点，集群状态的不一致，出现脑裂的情况。

![](https://cdn.xn2001.com/img/2022/202205071344829.png)



解决脑裂的方案是，要求选票超过 **(eligible节点数量+1)/2** 才能当选为 master，因此 eligible 节点数量最好是奇数。对应配置项是`discovery.zen.minimum_master_nodes`，在版本 7.0 以后，已经成为默认配置，因此一般不会发生脑裂问题。

例如：3个节点形成的集群，选票必须超过 (3+1)/2 ，也就是 2 票。node3 得到 node2 和 node3 的选票，当选为 master。node1 只有自己 1 票，没有当选。集群中依然只有1个主节点，没有出现脑裂。

## 集群分布式存储

当新增文档时，应该保存到不同分片，保证数据均衡，那么 coordinating node 如何确定数据该存储到哪个分片呢？

Elasticsearch 会通过 hash 算法来计算文档应该存储到哪个分片

![](https://cdn.xn2001.com/img/2022/202205071412275.png)

- _routing 默认是文档的 id
- 算法与分片数量有关，因此索引库一旦创建，分片数量不能修改！

新增文档的流程如下图，

1. 新增一个 id=1 的文档
2. 对 id 做 hash 运算，假如得到的是 2，则应该存储到 shard-2
3. shard-2 的主分片在 node3 节点，将数据路由到 node3，node3 保存文档
4. 同步给 shard-2 的副本分片2(R-2)，在 node2 节点
5. 返回结果给 coordinating-node 节点(node1)

![](https://cdn.xn2001.com/img/2022/202205071412283.png)

## 集群分布式查询

Elasticsearch 查询分成两个阶段

- scatter phase：分散阶段，coordinating node 会把请求分发到每一个分片。

- gather phase：聚集阶段，coordinating node 汇总 data node 的搜索结果，并处理为最终结果集返回给用户。

![](https://cdn.xn2001.com/img/2022/202205071422105.png)



## 集群故障转移

集群的 master 节点会监控集群中的节点状态，如果发现有节点宕机，会立即将宕机节点的分片数据迁移到其它节点，确保数据安全，这个叫做故障转移。

例如一个集群结构如图，三个都是健康的。

![](https://cdn.xn2001.com/img/2022/202205071516134.png)

现在，node1 是主节点，其它两个节点是从节点。突然，node1 发生了故障

![](https://cdn.xn2001.com/img/2022/202205071516139.png)



宕机后的第一件事，需要重新选主，例如选中了 node2

![](https://cdn.xn2001.com/img/2022/202205071516685.png)



node2 成为主节点后，会检测集群监控状态，将 node1 上的数据迁移到 node2、node3，确保数据依旧正常访问。

![](https://cdn.xn2001.com/img/2022/202205071516805.png)

# JMeter压力测试

## 安装启动

JMeter 依赖于JDK，所以必须确保当前计算机上已经安装了 JDK，并且配置了环境变量。

Apache Jmeter官网下载，地址：http://jmeter.apache.org/download_jmeter.cgi

![](https://cdn.xn2001.com/img/2022/202205081544073.png)

解压缩即可使用，目录结构如下

![](https://cdn.xn2001.com/img/2022/202205081545678.png)

其中的 bin 目录就是执行的脚本，其中包含启动脚本

![](https://cdn.xn2001.com/img/2022/202205081545976.png)

双击即可运行，但是有两点注意

- 启动时速度比较慢，要耐心等待。
- 启动后终端（黑窗口）不能关闭，否则 JMeter 也跟着关闭。

![](https://cdn.xn2001.com/img/2022/202205081545691.png)

## 修改中文

默认 JMeter 的语言是英文，需要设置

![](https://cdn.xn2001.com/img/2022/202205081547848.png)

![](https://cdn.xn2001.com/img/2022/202205081547110.png)

上面的配置只能保证本次运行是中文，如果要永久中文，需要修改 JMeter 的配置文件。

打开 JMeter 文件夹，在 bin 目录中找到 **jmeter.properties**，添加下面配置：

```properties
language=zh_CN
```

![](https://cdn.xn2001.com/img/2022/202205081546219.png)

## 基本使用

在测试计划上点鼠标右键，选择「添加 > 线程（用户） > 线程组」

![](https://cdn.xn2001.com/img/2022/202205081548692.png)

在新增的线程组中，填写线程信息

![](https://cdn.xn2001.com/img/2022/202205081549330.png)

在线程组这里点鼠标右键，添加 http 请求

![](https://cdn.xn2001.com/img/2022/202205081551101.png)

编写取样器内容

![](https://cdn.xn2001.com/img/2022/202205081551053.png)

添加监听报告

![](https://cdn.xn2001.com/img/2022/202205081551627.png)

汇总报告结果

![](https://cdn.xn2001.com/img/2022/202205081548975.png)

添加监听结果树

![](https://cdn.xn2001.com/img/2022/202205231440680.png)

察看结果树

![](https://cdn.xn2001.com/img/2022/202205081548982.png)

# Sentine流量组件

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/10-sentinel-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/10-sentinel-demo

## 雪崩问题

> 微服务之间相互调用，因为调用链中的一个服务故障，引起整个链路都无法访问的情况。

微服务中，服务间调用关系错综复杂，一个微服务往往依赖于多个其它微服务。

 ![](https://cdn.xn2001.com/img/2022/202205071911137.png)

如图，如果服务提供者I 发生了故障，当前的应用的部分业务因为依赖于服务I，因此也会被阻塞。此时，其它不依赖于服务I 的业务似乎不受影响。

 ![](https://cdn.xn2001.com/img/2022/202205071911461.png)

但是，依赖服务I 的业务请求被阻塞，则 tomcat 的这个线程不会释放，于是越来越多的用户请求到来，越来越多的线程会阻塞。

 ![](https://cdn.xn2001.com/img/2022/202205071911464.png)

服务器支持的线程和并发数有限，请求一直阻塞，会导致服务器资源耗尽，**从而导致所有其它服务都不可用**。

综上，依赖于当前服务的其它服务随着时间的推移，最终也都会变的不可用，形成级联失败，这就是雪崩问题。

![](https://cdn.xn2001.com/img/2022/202205071911480.png)

解决雪崩问题的常见方式有四种

1. 超时处理
2. 线程隔离
3. 降级熔断
4. 限流

> **限流**是对服务的保护，避免因瞬间高并发流量而导致服务故障，进而避免雪崩。是一种**预防**措施。
>
> **超时处理、线程隔离、降级熔断**是在部分服务故障时，将故障控制在一定范围，避免雪崩。是一种**补救**措施。

1.超时处理：设定超时时间，请求超过一定时间没有响应就返回错误信息，不会无休止等待。

![](https://cdn.xn2001.com/img/2022/202205071936072.png)

2.线程隔离

![](https://cdn.xn2001.com/img/2022/202205071953573.png)

是一种舱壁模式，如上图，船舱都会被隔板分离为多个独立空间，当船体破损时，只会导致部分空间进入，将故障控制在一定范围内，避免整个船体都被淹没。于此类似，我们可以限定每个业务能使用的线程数，避免耗尽整个 tomcat 的资源，因此也叫线程隔离。

![](https://cdn.xn2001.com/img/2022/202205071935351.png)

3.降级熔断

是一种断路器模式：由**断路器**统计业务执行的异常比例，如果超出阈值则会**熔断**该业务，拦截访问该业务的一切请求。

断路器会统计访问某个服务的请求数量，异常比例。

![](https://cdn.xn2001.com/img/2022/202205071956512.png)

当发现访问服务 D 的请求异常比例过高时，认为服务 D 有导致雪崩的风险，会拦截访问服务 D 的一切请求，形成熔断。

![](https://cdn.xn2001.com/img/2022/202205071956665.png)

4.限流

**流量控制**：限制业务访问的 QPS，避免服务因流量的突增而故障。

![](https://cdn.xn2001.com/img/2022/202205071935370.png)

在 SpringCloud 当中支持多种服务保护技术

- [Netfix Hystrix](https://github.com/Netflix/Hystrix)
- [Sentinel](https://github.com/alibaba/Sentinel)
- [Resilience4J](https://github.com/resilience4j/resilience4j)

早期比较流行的是 Hystrix 框架，但目前国内实用最广泛的还是阿里巴巴的 Sentinel 框架，这里我们做下对比：

|                | **Sentinel**                                   | **Hystrix**                   |
| -------------- | ---------------------------------------------- | ----------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离         |
| 熔断降级策略   | 基于慢调用比例或异常比例                       | 基于失败比率                  |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于 RxJava）       |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                |
| 扩展性         | 多个扩展点                                     | 插件的形式                    |
| 基于注解的支持 | 支持                                           | 支持                          |
| 限流           | 基于 QPS，支持基于调用关系的限流               | 有限的支持                    |
| 流量整形       | 支持慢启动、匀速排队模式                       | 不支持                        |
| 系统自适应保护 | 支持                                           | 不支持                        |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                        |
| 常见框架的适配 | Servlet、Spring Cloud、Dubbo、gRPC  等         | Servlet、Spring Cloud Netflix |

## 初识Sentinel

Sentinel是阿里巴巴开源的一款微服务流量控制组件。官网地址：https://sentinelguard.io/zh-cn/index.html

Sentinel 具有以下特征

- **丰富的应用场景**：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。
- **完备的实时监控**：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
- **广泛的开源生态**：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
- **完善的** **SPI** **扩展点**：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。

## 整合Sentinel

下载后 jar 包后，运行代码：`java -jar sentinel-dashboard-1.8.1.jar`

如果要修改 Sentinel 的默认端口、账户、密码，可以通过下列配置：

| **配置项**                       | **默认值** | **说明**   |
| -------------------------------- | ---------- | ---------- |
| server.port                      | 8080       | 服务端口   |
| sentinel.dashboard.auth.username | sentinel   | 默认用户名 |
| sentinel.dashboard.auth.password | sentinel   | 默认密码   |

例如，修改端口：

```sh
java -Dserver.port=8090 -jar sentinel-dashboard-1.8.1.jar
```

访问 http://localhost:8080 页面，就可以看到 Sentinel 的控制台了。

![](https://cdn.xn2001.com/img/2022/202205072010468.png)

账号和密码默认都是：sentinel

![](https://cdn.xn2001.com/img/2022/202205072018383.png)

此时空白一片，还需要我们来整合进 SpringCloud

准备好我们的项目，在资料中，结构如下：

> 这里老师讲的疏忽了，没有走 gateway 网关，但不影响。
>
> 提供的资料中的 gateway 网关带了权限配置导致这里我们访问不了网关（之前上课留下的），需要删除掉其中的 AuthorizeFilter.jar

![](https://cdn.xn2001.com/img/2022/202205072033318.png)

我们在 order-service 中整合 Sentinel，并连接 Sentinel 的控制台，步骤如下

1）引入 Sentinel 依赖

```xml
<!--sentinel-->
<dependency>
    <groupId>com.alibaba.cloud</groupId> 
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

2）配置控制台

修改 application.yml 文件，添加下面内容：

```yaml
server:
  port: 8088
spring:
  cloud: 
    sentinel:
      transport:
        dashboard: localhost:8080
```

3）访问 order-service 的任意端点

打开浏览器，访问 http://localhost:10010/order/101，多访问几次，多点几次刷新，这样才能触发 Sentinel 的监控。

然后再访问 Sentinel 的控制台，查看效果。

![](https://cdn.xn2001.com/img/2022/202205081430741.png)

## 流量控制

雪崩问题虽有四种方案，但是**限流是避免服务因突发的流量而发生故障，是对微服务雪崩问题的预防。**学过毛中特的都知道，《预判风险所在是防范风险的前提》，我们先学习流量控制。

### 簇点链路

当请求进入微服务时，首先会访问 DispatcherServlet，然后进入 Controller、Service、Mapper，这样的一个调用链就叫做 **簇点链路**。

**簇点链路中被监控的每一个接口就是一个资源**。默认情况下 Sentinel 会监控 SpringMVC 的每一个端点（Endpoint，也就是 Controller 中的方法），因此 SpringMVC 的每一个端点（Endpoint）就是调用链路中的一个资源。

例如，我们刚才访问的 order-service 中的 OrderController 中的端点：/order/{orderId}

![](https://cdn.xn2001.com/img/2022/202205081445779.png)



流控、熔断等都是针对簇点链路中的资源来设置的，因此我们可以点击对应资源后面的按钮来设置规则：

- 流控：流量控制
- 降级：降级熔断
- 热点：热点参数限流，是限流的一种
- 授权：请求的权限控制

点击资源 /order/{orderId} 后面的流控按钮，就可以弹出表单。

![](https://cdn.xn2001.com/img/2022/202205081509045.png)

表单中可以填写限流规则，如下

![](https://cdn.xn2001.com/img/2022/202205081509055.png)

其含义是限制 /order/{orderId} 这个资源的单机 QPS 为 1，即每秒只允许 1 次请求，超出的请求会被拦截并报错。

> 需求：给 /order/{orderId} 这个资源设置流控规则，QPS 不能超过 5，然后测试。

1）首先在 sentinel 控制台添加限流规则

![](https://cdn.xn2001.com/img/2022/202205081513259.png)

2）利用 jmeter 测试，下面有讲到 JMeter 基础使用。

![](https://cdn.xn2001.com/img/2022/202205081514678.png)

20 个用户，2 秒内运行完，这样的话 QPS 就是 10，超过了我们在 sentinel 设置的 5

右键运行：

![](https://cdn.xn2001.com/img/2022/202205081512845.png)



> 注意，不要点击菜单中的执行按钮来运行。

结果：

![](https://cdn.xn2001.com/img/2022/202205081512850.png)

可以看到，成功的请求每次只有 5 个。

![](https://cdn.xn2001.com/img/2022/202205081525292.png)

### 流控模式

在添加限流规则时，点击高级选项，可以选择三种**流控模式**

#### 直接模式

- 直接：统计当前资源的请求，触发阈值时对当前资源直接限流，也是默认的模式
  - 直接对当前资源限流

- 关联：统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流
  - 相当于高优先级资源触发阈值，对低优先级资源限流。

- 链路：统计从指定链路访问到本资源的请求，触发阈值时，对指定链路限流
  - 是针对请求来源的限流


![](https://cdn.xn2001.com/img/2022/202205081553073.png)



上面我们测试的就是直接模式，默认就是直接模式。

#### 关联模式

统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流。

**使用场景**：比如用户支付时需要修改订单状态，同时用户要查询订单。查询和修改操作会争抢数据库锁，产生竞争。业务需求是优先支付和更新订单的业务，因此当修改订单业务触发阈值时，需要对查询订单业务限流。

![](https://cdn.xn2001.com/img/2022/202205081602290.png)

例如：配置规则，**当 /write 资源访问量触发阈值时，就会对 /read 资源限流，避免影响 /write 资源。**

![](https://cdn.xn2001.com/img/2022/202205081602252.png)

我们去程序中模拟：

- 在 OrderController 新建两个端点：/order/query 和 /order/update，无需实现业务

- 配置流控规则，当 /order/ update 资源被访问的 QPS 超过 5 时，对 /order/query 请求限流

1）定义 /order/query 端点，模拟订单查询

```java
@GetMapping("/query")
public String queryOrder() {
    return "查询订单成功";
}
```

2）定义 /order/update 端点，模拟订单更新

```java
@GetMapping("/update")
public String updateOrder() {
    return "更新订单成功";
}
```

重启服务，查看 Sentinel 控制台的簇点链路。

![](https://cdn.xn2001.com/img/2022/202205081618267.png)



3）配置流控规则

想要对哪个端点限流，就点击哪个端点后面的按钮。我们是对订单查询 /order/query 限流

![](https://cdn.xn2001.com/img/2022/202205081619163.png)在表单中填写流控规则

![](https://cdn.xn2001.com/img/2022/202205081602274.png)

4）在 JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205081619453.png)

可以看到 1000 个用户，100 秒，因此 QPS 为10，超过了我们设定的阈值：5

查看 http 请求

![](https://cdn.xn2001.com/img/2022/202205081602280.png)

请求的目标是 /order/update，但限流的目标是 /order/query，这就是关联模式；我们在浏览器访问 /order/query，可以发现确实被限流了。

![](https://cdn.xn2001.com/img/2022/202205081620364.png)

#### 链路模式

只针对从指定链路访问到本资源的请求做统计，判断是否超过阈值。

例如有两条请求链路

- /test1 --> /common

- /test2 --> /common

如果只希望统计从 /test2 进入到 /common 的请求，则可以这样配置

![](https://cdn.xn2001.com/img/2022/202205081632892.png)

**实战案例**

有查询订单和创建订单业务，两者都需要查询商品。针对从查询订单进入到查询商品的请求统计，并设置限流。

1. 在 OrderService 中添加一个 queryGoods 方法，不用实现业务

2. 在 OrderController 中，改造 /order/query 端点，调用 OrderService 中的 queryGoods 方法

3. 在 OrderController 中添加一个 /order/save 端点，调用 OrderService 的 queryGoods 方法

4. 给 queryGoods 设置限流规则，从 /order/query 进入 queryGoods 的方法限制 QPS 必须小于 2

1）添加查询商品方法

在order-service服务中，给 OrderService 类添加一个 queryGoods 方法

```java
public void queryGoods(){
    System.err.println("查询商品");
}
```

2）查询订单时，查询商品

在 order-service 的 OrderController 中，修改 /order/query 端点的业务逻辑

```java
@GetMapping("/query")
public String queryOrder() {
    // 查询商品
    orderService.queryGoods();
    // 查询订单
    System.out.println("查询订单");
    return "查询订单成功";
}  
```

3）新增订单，查询商品

在 order-service 的 OrderController 中，修改 /order/save 端点，模拟新增订单：

```java
@GetMapping("/save")
public String saveOrder() {
    // 查询商品
    orderService.queryGoods();
    // 查询订单
    System.out.println("新增订单");
    return "新增订单成功";
}
```

4）给查询商品添加资源标记

默认情况下，OrderService 中的方法是不被 Sentinel 监控的，需要我们自己通过注解来标记要监控的方法。

给 OrderService 的 queryGoods 方法添加 `@SentinelResource` 注解。

```java
@SentinelResource("goods")
public void queryGoods(){
    System.err.println("查询商品");
}
```

链路模式中，是对不同来源的两个链路做监控。但是 Sentinel 默认会给进入 SpringMVC 的所有请求设置同一个 root 资源，会导致链路模式失效。我们需要关闭这种对 SpringMVC 的资源聚合，修改 order-service 服务的 application.yml 文件

```yaml
spring:
  cloud:
    sentinel:
      web-context-unify: false # 关闭context整合
```

重启服务，访问 /order/query 和 /order/save，可以查看到 Sentinel 的簇点链路规则中，出现了新的资源

![](https://cdn.xn2001.com/img/2022/202205081633195.png)



5）添加流控规则

点击 goods 资源后面的流控按钮，在弹出的表单中填写下面信息

![](https://cdn.xn2001.com/img/2022/202205081633212.png)



只统计从 /order/query 进入 /goods 的资源，QPS 阈值为 2，超出则被限流。

6）Jmeter测试

![](https://cdn.xn2001.com/img/2022/202205081633203.png)

可以看到这里200个用户，50秒内发完，QPS为4，超过了我们设定的阈值2

一个 http 请求是访问 /order/save

![](https://cdn.xn2001.com/img/2022/202205081858739.png)

运行的结果是 /order/save 完全不受影响。

![](https://cdn.xn2001.com/img/2022/202205081858880.png)

访问 /order/query

![](https://cdn.xn2001.com/img/2022/202205081633221.png)

运行结果是 /order/query，每次只有2个通过。

![](https://cdn.xn2001.com/img/2022/202205081633245.png)

## 流控效果

### 快速失败

细心的小伙伴会发现在流控的高级选项中，还有一个流控效果选项，前面我们的测试都是基本快速失败的。

![](https://cdn.xn2001.com/img/2022/202205081907653.png)

流控效果是指请求达到流控阈值时应该采取的措施，包括三种

- 快速失败：达到阈值后，新的请求会被立即拒绝并抛出 FlowException 异常，是默认的处理方式。

- Warm Up：预热模式，对超出阈值的请求同样是拒绝并抛出异常。但这种模式阈值会动态变化，从一个较小值逐渐增加到最大阈值。

- 排队等待：让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长。

### Warm up

阈值一般是一个微服务能承担的最大 QPS，但是一个服务刚刚启动时，一切资源尚未初始化（**冷启动**），如果直接将 QPS 跑到最大值，可能导致服务瞬间宕机。

Warm Up 也叫**预热模式**，是应对服务冷启动的一种方案。请求阈值初始值是 `maxThreshold / coldFactor`，持续指定时长后，逐渐提高到 maxThreshold 值。而 coldFactor 的默认值是 3.

例如，我设置 QPS 的 maxThreshold 为 10，预热时间为 5 秒，那么初始阈值就是 10 / 3 = 3，然后在 5 秒后逐渐增长到 10

![](https://cdn.xn2001.com/img/2022/202205081907114.png)



**案例**：给 /order/{orderId} 这个资源设置限流，最大 QPS 为 10，利用 Warm Up 效果，预热时长为 5 秒。

1）配置流控规则

![](https://cdn.xn2001.com/img/2022/202205090008173.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205090008283.png)

刚刚启动时，大部分请求失败，成功的只有 3 个，说明 QPS 被限定在 3 

![](https://cdn.xn2001.com/img/2022/202205090009927.png)

随着时间推移，成功比例越来越高

![](https://cdn.xn2001.com/img/2022/202205090009448.png)



到 Sentinel 控制台查看实时监控

![](https://cdn.xn2001.com/img/2022/202205081907395.png)

一段时间后

![](https://cdn.xn2001.com/img/2022/202205081907398.png)

用官方的话讲，该方式主要用于系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮的情况。

### 排队等待

当请求超过 QPS 阈值时，「快速失败」和 「Warm Up」会拒绝新的请求并抛出异常。

而排队等待则是让所有请求进入一个队列中，然后按照阈值允许的时间间隔依次执行。后来的请求必须等待前面执行完成，如果请求预期的等待时间超出最大时长，则会被拒绝。这种方式严格控制了请求通过的间隔时间，也即是让请求以均匀的速度通过，对应的是漏桶算法。

例如：QPS = 5，意味着每 200ms 处理一个队列中的请求；timeout = 2000，意味着**预期等待时长**超过 2000ms 的请求会被拒绝并抛出异常。

比如现在一下子来了 12  个请求，因为每 200ms 执行一个请求，那么预期等待时长就是：

- 第6个请求的**预期等待时长** =  200 * (6 - 1) = 1000ms
- 第12个请求的预期等待时长 = 200 * (12-1) = 2200ms

又比如下图：

![](https://cdn.xn2001.com/img/2022/202205090037622.png)

现在，第 1 秒同时接收到 10 个请求，但第 2 秒只有 1 个请求，此时 QPS 的曲线这样的

![](https://cdn.xn2001.com/img/2022/202205081907396.png)

如果使用排队等待的流控效果，所有进入的请求都要排队，以固定的 200ms 的间隔执行，QPS 会变的很平滑

![](https://cdn.xn2001.com/img/2022/202205090016702.png)

这种方式主要用于处理间隔性突发的流量，例如消息队列。想象一下这样的场景，在某一秒有大量的请求到来，而接下来的几秒则处于空闲状态，我们希望系统能够在接下来的空闲期间逐渐处理这些请求，而不是在第一秒直接拒绝多余的请求。

**案例**：给 /order/{orderId} 这个资源设置限流，最大 QPS 为 10，利用排队等待的流控效果，超时时长设置为 5s

1）添加流控规则

![](https://cdn.xn2001.com/img/2022/202205090011577.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205081907171.png)

QPS 为 15，已经超过了我们设定的 10，如果是之前的「快速失败」 、「Warm Up」模式，超出的请求应该会直接报错。

但是我们看看「排队等待」的运行结果

![](https://cdn.xn2001.com/img/2022/202205081907254.png)

再去 Sentinel 查看实时监控的 QPS 曲线

![](https://cdn.xn2001.com/img/2022/202205081907426.png)

QPS 非常平滑，一致保持在 10，但是超出的请求没有被拒绝，而是放入队列。因此**响应时间**（等待时间）会越来越长。

当队列满了以后，才会有部分请求失败

![](https://cdn.xn2001.com/img/2022/202205081907443.png)

## 热点参数限流

之前的限流是统计访问某个资源的所有请求，判断是否超过 QPS 阈值。而「热点参数限流」是**分别统计参数值相同的请求**，判断是否超过 QPS 阈值。

### 全局参数限流

例如，一个根据 id 查询商品的接口

![](https://cdn.xn2001.com/img/2022/202205090045292.png)

访问 /goods/{id} 的请求中，id 参数值会有变化，「热点参数限流」会根据参数值分别统计 QPS，统计结果：

![](https://cdn.xn2001.com/img/2022/202205090046762.png)

当 id=1 的请求触发阈值被限流时，id值不为1的请求则不受影响。

配置示例：对 hot 这个资源的 0 号参数（也就是第一个参数）做统计，每 1s **相同参数值**的请求数不能超过 5

![](https://cdn.xn2001.com/img/2022/202205090042929.png)

### 热点参数限流

假设上面的例子是一个商品查询接口，那么刚才的配置中，对这个接口的所有商品一视同仁，QPS 都限定为 5

而在实际开发中，可能部分商品是热点商品，例如秒杀商品，我们希望这部分商品的 QPS 限制与其它商品不一样，高一些。那就需要配置「热点参数限流」的高级选项了。

![](https://cdn.xn2001.com/img/2022/202205090050071.png)

结合上一个配置，这里的含义是对 0 号的 long 类型参数限流，每 1 个相同参数的 QPS 不能超过 5，有如下两个例外

- 如果参数值是 100，则每 1s 允许的 QPS 为 10
- 如果参数值是 101，则每 1s 允许的 QPS 为 15

**案例需求**：给 /order/{orderId} 这个资源添加「热点参数限流」，规则如下

- 默认的热点参数规则是每 1s 请求量不超过 2
- 给 102 这个参数设置例外：每 1s 请求量不超过 4
- 给 103 这个参数设置例外：每 1s 请求量不超过 10

**注意事项**：热点参数限流对默认的 SpringMVC 资源无效，需要利用 `@SentinelResource` 注解标记资源。

1）标记资源

给 order-service 中的 OrderController 中的 /order/{orderId} 资源添加注解

![](https://cdn.xn2001.com/img/2022/202205090042942.png)

2）热点参数限流规则

访问该接口，可以看到我们标记的 hot 资源出现了

![](https://cdn.xn2001.com/img/2022/202205090136368.png)

点击左侧菜单中**热点规则**菜单

![](https://cdn.xn2001.com/img/2022/202205090137049.png)

点击新增，填写表单

![](https://cdn.xn2001.com/img/2022/202205090137574.png)

3）JMeter 测试

这里发起请求的 QPS 为 5，包含 3 个 http 请求

![](https://cdn.xn2001.com/img/2022/202205090138955.png)

普通参数，QPS 阈值为 2

![](https://cdn.xn2001.com/img/2022/202205090042964.png)

![](https://cdn.xn2001.com/img/2022/202205090042203.png)



例外项，QPS 阈值为 4

![](https://cdn.xn2001.com/img/2022/202205090140941.png)

![](https://cdn.xn2001.com/img/2022/202205090042238.png)



**例外项，QPS 阈值为 10**

![](https://cdn.xn2001.com/img/2022/202205090042244.png)

![](https://cdn.xn2001.com/img/2022/202205090140933.png)

## 隔离和降级

限流只是一种预防措施，虽然限流可以尽量避免因高并发而引起的服务故障，但服务还会因为其它原因而故障。

而要将这些故障控制在一定范围，避免雪崩，就要靠**线程隔离**（舱壁模式）和**熔断降级**手段了。

**线程隔离**：调用者在调用服务提供者时，给每个调用的请求分配独立线程池，出现故障时，最多消耗这个线程池内资源，避免把调用者的所有资源耗尽。

![](https://cdn.xn2001.com/img/2022/202205091001949.png)

**熔断降级**：是在调用方这边加入断路器，统计对服务提供者的调用，如果调用的失败比例过高，则熔断该业务，不允许访问该服务的提供者了。

![](https://cdn.xn2001.com/img/2022/202205091203825.png)

可以看到，不管是线程隔离还是熔断降级，都是对**客户端**（调用方）的保护。需要在**调用方**发起远程调用时做线程隔离、或者服务熔断。

而我们的微服务远程调用都是基于 Feign 来完成的，因此我们需要将 Feign 与 Sentinel 整合，在 Feign 里面实现线程隔离和服务熔断。

## Feign整合Sentinel

SpringCloud中，微服务调用都是通过Feign来实现的，因此做客户端保护必须整合 Feign 和 Sentinel

修改配置，开启 Sentinel 功能，修改 OrderService 的 application.yml 文件，开启 Feign 的 Sentinel 功能

```yaml
feign:
  sentinel:
    enabled: true # 开启feign对sentinel的支持
```

服务降级：访问失败后，服务

**编写失败降级逻辑代码**，业务失败后，不能直接报错，而应该返回用户一个友好提示或者默认结果，这个就是失败降级逻辑。

给 FeignClient 编写失败后的降级逻辑

①方式一：FallbackClass，但无法对远程调用的异常做处理。

②方式二：FallbackFactory，可以对远程调用的异常做处理，我们选择这种

这里我们演示方式二的失败降级处理。

**步骤一**：在 feing-api 项目中定义类，实现 FallbackFactory

![](https://cdn.xn2001.com/img/2022/202205091209104.png)

```java
package com.xn2001.feign.clients.fallback;

import com.xn2001.feign.clients.UserClient;
import com.xn2001.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2022/5/9 14:24
 */

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
       return userClient -> {
           log.error("查询用户失败",throwable);
           return new User();
       };
    }
}
```

**步骤二**：在 feing-api 项目中的 DefaultFeignConfiguration 类中将 UserClientFallbackFactory 注册为一个Bean

```java
@Bean
public UserClientFallbackFactory userClientFallbackFactory(){
    return new UserClientFallbackFactory();
}
```

**步骤三**：在 feing-api 项目中的 UserClient 接口中使用 UserClientFallbackFactory

```java
@FeignClient(value = "userservice", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
```

重启后，访问一次订单查询业务，然后查看 Sentinel 控制台，可以看到新的簇点链路

![](https://cdn.xn2001.com/img/2022/202205091210616.png)

## 线程隔离

线程隔离（舱壁模式）有两种方式实现

- 线程池隔离：给每个服务调用业务分配一个线程池，利用线程池本身实现隔离效果。

- 信号量隔离（Sentinel默认采用）：不创建线程池，而是计数器模式，记录业务使用的线程数量，达到信号量上限时，禁止新的请求。

两者的优缺点

- 线程池隔离：基于线程池模式，有额外开销，但隔离控制更强
- 信号量隔离：基于计数器模式，简单，开销小

![](https://cdn.xn2001.com/img/2022/202205091501180.png)

![](https://cdn.xn2001.com/img/2022/202205091502961.png)

**Sentinel 使用的是信号量隔离**，而 Hystrix 则两种线程隔离都可以，18 年Hystrix已经停止更新。

如何使用呢，在添加限流规则时，可以选择两种阈值类型

![](https://cdn.xn2001.com/img/2022/202205091500754.png)

- QPS：就是每秒的请求数，之前已经演示过。

- 线程数：是该资源能使用的 Tomcat 线程数的最大值。也就是通过限制线程数量，实现**线程隔离**（舱壁模式）。

**案例需求**：给 order-service 服务中的 UserClient 的查询用户接口设置流控规则，线程数不能超过 2，然后利用 JMeter 测试。

1）配置隔离规则，选择 feign 接口后面的流控按钮

![](https://cdn.xn2001.com/img/2022/202205091507487.png)

![](https://cdn.xn2001.com/img/2022/202205091508076.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205091500777.png)

一次发生 10 个请求，有较大概率并发线程数超过 2，而超出的请求会走之前定义的失败降级逻辑。

查看运行结果

![](https://cdn.xn2001.com/img/2022/202205091500739.png)

发现虽然结果都是通过了，不过部分请求得到的响应是「降级」返回的 null 信息。也就是我们写的降级方法。

## 熔断降级

熔断降级是解决雪崩问题的重要手段。其思路是由**断路器**统计服务调用的异常比例、慢请求比例，如果超出阈值则会**熔断**该服务。即拦截访问该服务的一切请求；而当服务恢复时，断路器会放行访问该服务的请求。

断路器控制熔断和放行是通过状态机来完成的，如下图就是一个断路器的状态机

![](https://cdn.xn2001.com/img/2022/202205091510676.png)

状态机包括三个状态

- closed：关闭状态，断路器放行所有请求，并开始统计异常比例、慢请求比例。会去判断是否达到熔断条件，这一步我们叫做「熔断策略」，达到该条件则切换到 open 状态，打开断路器。
- open：打开状态，服务调用被**熔断**，访问被熔断服务的请求会被拒绝，快速失败，直接走降级逻辑。Open 状态 5 秒后会进入 half-open 状态。
- half-open：半开状态，会一段时间放行一次请求，根据执行结果来判断接下来的操作。请求成功：则切换到 closed 状态；请求失败：则切换到 open 状态。

断路器熔断策略有三种：慢调用、异常比例、异常数

#### 慢调用

业务的响应时长（RT）大于指定时长的请求认定为慢调用请求。在指定时间内，如果请求数量超过设定的最小数量，慢调用比例大于设定的阈值，则触发熔断。

例如下图，设置 RT 超过 500ms 的调用是慢调用，统计最近 10000ms 内的请求，如果请求量超过 10 次，并且慢调用比例不低于 0.5，则触发熔断，熔断时长为 5s，然后进入 half-open 状态，放行一次请求做测试。

![](https://cdn.xn2001.com/img/2022/202205091520916.png)

**案例需求**：给 UserClient 的查询用户接口设置降级规则，慢调用的 RT 阈值为 50ms，统计时间为 1s ，最小请求数量为 5，失败阈值比例为 0.4，熔断时长为 5s；

1）设置慢调用

修改 user-service 中的 /user/{id} 这个接口的业务。通过休眠模拟一个延迟时间。

![](https://cdn.xn2001.com/img/2022/202205091538605.png)

此时，orderId = 101 的订单，关联的是 id 为 1 的用户，调用时长为 60ms

![](https://cdn.xn2001.com/img/2022/202205091520928.png)

orderId = 102 的订单，关联的是 id 为 2 的用户，调用时长为非常短

![](https://cdn.xn2001.com/img/2022/202205091520932.png)

2）设置熔断规则

下面，给 feign 接口设置降级规则，超过 50ms 的请求都会被认为是慢请求。

![](https://cdn.xn2001.com/img/2022/202205091624147.png)

![](https://cdn.xn2001.com/img/2022/202205091624535.png)

3）测试

在浏览器访问：http://localhost:8088/order/101，快速刷新5次，可以发现，触发了熔断，请求时长缩短至 5ms，快速失败了，并且走降级逻辑，返回的 null

![](https://cdn.xn2001.com/img/2022/202205091624280.png)

此时在浏览器访问：http://localhost:8088/order/102，也被熔断了

![](https://cdn.xn2001.com/img/2022/202205091625297.png)

#### 异常

**异常比例或异常数**：统计指定时间内的调用，如果调用次数超过指定请求数，并且出现异常的比例达到设定的比例阈值（或超过指定异常数），则触发熔断。

例如，异常比例设置如下，统计最近 1000ms 内的请求，如果请求量超过 10 次，并且异常比例不低于 0.4，则触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091520106.png)

异常数设置如下，统计最近 1000ms 内的请求，如果请求量超过 10 次，并且异常比例不低于 2 次，则触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091520416.png)

**案例需求**：给 UserClient 的查询用户接口设置降级规则，统计时间为 1s，最小请求数量为 5，失败阈值比例为 0.4，熔断时长为 5s

1）设置异常请求

首先，修改 user-service 中的 /user/{id} 这个接口的业务。手动抛出异常，以触发异常比例的熔断，也就是说，id 为 2时，就会触发异常。

![](https://cdn.xn2001.com/img/2022/202205091520469.png)

2）设置熔断规则

在 5 次请求中，只要异常比例超过 0.4，也就是有 2 次以上的异常，就会触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091818818.png)

![](https://cdn.xn2001.com/img/2022/202205091520694.png)

3）测试

在浏览器快速访问：http://localhost:8088/order/102，快速刷新 5 次，触发熔断降级。

![](https://cdn.xn2001.com/img/2022/202205091821790.png)

此时，我们去访问本应该正常的 103，发现也被降级了。

![](https://cdn.xn2001.com/img/2022/202205091520623.png)

## 授权规则

授权规则可以对请求方来源做判断和控制。授权规则可以对调用方的来源做控制，有白名单和黑名单两种方式。

比如我们希望控制对资源 `test` 的访问设置白名单，只有来源为 `appA` 和 `appB` 的请求才可通过。

- 白名单：来源（origin）在白名单内的调用者允许访问

- 黑名单：来源（origin）在黑名单内的调用者不允许访问

点击左侧菜单的授权，可以看到授权规则

![](https://cdn.xn2001.com/img/2022/202205100008770.png)

- 资源名：就是受保护的资源，例如 /order/{orderId}

- 流控应用：是来源者的名单
  - 如果是勾选白名单，则名单中的来源被许可访问。
  - 如果是勾选黑名单，则名单中的来源被禁止访问。

比如我们允许请求从 gateway 到 order-service，不允许浏览器访问 order-service，那么白名单中就要填写**网关的来源名称（origin）**。

![](https://cdn.xn2001.com/img/2022/202205100008679.png)

Sentinel 是通过 RequestOriginParser 这个接口的 parseOrigin() 方法来获取请求的来源的。

```java
public interface RequestOriginParser {
    /**
     * 从请求request对象中获取origin，获取方式自定义
     */
    String parseOrigin(HttpServletRequest request);
}
```

这个方法的作用就是从 request 对象中，获取请求者的 origin 值并返回。默认情况下，Sentinel 不管请求者从哪里来，返回值永远是 default，也就是说一切请求的来源都被认为是一样的值 default

因此，我们需要自定义这个接口的实现，让**不同的请求，返回不同的 origin**

例如 order-service 服务中，我们定义一个 RequestOriginParser 的实现类

```java
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 1.获取请求头
        String origin = request.getHeader("origin");
        // 2.非空判断
        if (StringUtils.isEmpty(origin)) {
            origin = "blank";
        }
        return origin;
    }
}
```

我们必须让**所有从 Gateway 路由到微服务的请求都带上 origin 头**。这个需要利用之前学习的一个 GatewayFilter 来实现，使用 AddRequestHeaderGatewayFilter，修改 Gateway 服务中的 application.yml

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=origin,gateway #origin前面不能带空格
```

这样，从 Gateway 路由的所有请求都会带上 origin 头，值为 gateway。而从其它地方到达微服务的请求一般没有这个头。

接下来，我们添加一个授权规则，放行 origin 值为 gateway 的请求。

![](https://cdn.xn2001.com/img/2022/202205100104446.png)

![](https://cdn.xn2001.com/img/2022/202205100104442.png)

现在，我们直接跳过网关，访问 order-service 服务

![](https://cdn.xn2001.com/img/2022/202205100104100.png)

通过网关访问

![](https://cdn.xn2001.com/img/2022/202205100104271.png)

## 自定义异常结果

默认情况下，发生限流、降级、授权拦截时，都会抛出异常到调用方。异常结果都是 flow limmiting（限流）。这样不够友好，无法得知是限流还是降级还是授权拦截。

而如果要自定义异常时的返回结果，需要实现 BlockExceptionHandler 接口

```java
public interface BlockExceptionHandler {
    /**
     * 处理请求被限流、降级、授权拦截时抛出的异常：BlockException
     */
    void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception;
}
```

这个方法有三个参数：

- HttpServletRequest request：request 对象
- HttpServletResponse response：response 对象
- BlockException e：被 Sentinel 拦截时抛出的异常

这里的 BlockException 包含多个不同的子类

| **异常**             | **说明**           |
| -------------------- | ------------------ |
| FlowException        | 限流异常           |
| ParamFlowException   | 热点参数限流的异常 |
| DegradeException     | 降级异常           |
| AuthorityException   | 授权规则异常       |
| SystemBlockException | 系统规则异常       |

自定义异常处理，下面我们就在 order-service 定义一个自定义异常处理类

```java
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        String msg = "未知异常";
        int status = 429;

        if (e instanceof FlowException) {
            msg = "请求被限流了";
        } else if (e instanceof ParamFlowException) {
            msg = "请求被热点参数限流";
        } else if (e instanceof DegradeException) {
            msg = "请求被降级了";
        } else if (e instanceof AuthorityException) {
            msg = "没有权限访问";
            status = 401;
        }

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        response.getWriter().println("{\"msg\": " + msg + ", \"status\": " + status + "}");
    }
}
```

重启测试，在不同场景下，会返回不同的异常消息。

限流：

![](https://cdn.xn2001.com/img/2022/202205100112438.png)

授权拦截时：

![](https://cdn.xn2001.com/img/2022/202205100122438.png)

## 规则持久化

现在，Sentinel 的所有规则都是内存存储，重启后所有规则都会丢失。在生产环境下，我们必须确保这些规则的持久化，避免丢失。

规则是否能持久化，取决于规则管理模式，Sentinel 支持三种规则管理模式

- 原始模式：Sentinel 的默认模式，将规则保存在内存，重启服务会丢失。
- pull 模式
- push 模式

### pull模式

pull 模式：控制台将配置的规则推送到 Sentinel 客户端，而客户端会将配置规则保存在本地文件或数据库中。以后会定时去本地文件或数据库中查询，更新本地规则。

![](https://cdn.xn2001.com/img/2022/202205100126168.png)

### push模式

push 模式：控制台将配置规则推送到远程配置中心，例如 Nacos，Sentinel 客户端监听 Nacos，获取配置变更的推送消息，完成本地配置更新。这种模式是比较好的一种。

![](https://cdn.xn2001.com/img/2022/202205100128564.png)

我们简单了解一下这种方式，首先修改 OrderService，让其监听 Nacos 中的 Sentinel 规则配置。

在 order-service 中引入Sentinel 监听 Nacos 的依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

配置 Nacos 地址，在 order-service 中的 application.yml 文件配置 Nacos 地址及监听的配置信息

```yaml
spring:
  cloud:
    sentinel:
      datasource:
        flow:
          nacos:
            server-addr: localhost:8848 # nacos地址
            dataId: orderservice-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow # 还可以是：degrade、authority、param-flow
```

然后需要修改 sentinel-dashboard 源码，SentinelDashboard 默认不支持 Nacos 的持久化，需要修改源码。

> 相当于开源开一半，主要给阿里云用户商业使用。
>
> 以下以供了解，未实测。

解压 Sentinel 源码包

![](https://cdn.xn2001.com/img/2022/202205101404663.png)

然后并用 IDEA 打开这个项目，结构如下

![](https://cdn.xn2001.com/img/2022/202205101404666.png)

在 sentinel-dashboard 源码的pom文件中，nacos 的依赖默认的 scope 是 test，只能在测试时使用，这里要去除

![](https://cdn.xn2001.com/img/2022/202205101405160.png)

将 sentinel-datasource-nacos 依赖的 scope 去掉，改成下面这样。

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

在 sentinel-dashboard 的 test 包下，已经编写了对 nacos 的支持，我们需要将其拷贝到 main 中。

![](https://cdn.xn2001.com/img/2022/202205101405035.png)

然后，还需要修改测试代码中的 NacosConfig 类 

![](https://cdn.xn2001.com/img/2022/202205101406914.png)

修改其中的 Nacos 地址，让其读取 application.properties 中的配置

![](https://cdn.xn2001.com/img/2022/202205101404688.png)

在 sentinel-dashboard 的 application.properties 中添加 Nacos 地址配置

```properties
nacos.addr=localhost:8848
```

另外，还需要修改 `com.alibaba.csp.sentinel.dashboard.controller.v2` 包下的 FlowControllerV2

![](https://cdn.xn2001.com/img/2022/202205101404531.png)

让我们添加的 Nacos 数据源生效

![](https://cdn.xn2001.com/img/2022/202205101407560.png)

接下来，还要修改前端页面，添加一个支持 nacos 的菜单。

修改 `src/main/webapp/resources/app/scripts/directives/sidebar/` 目录下的 sidebar.html 文件

![](https://cdn.xn2001.com/img/2022/202205101404891.png)

将其中的这部分注释打开

![](https://cdn.xn2001.com/img/2022/202205101408124.png)

修改其中的文本

![](https://cdn.xn2001.com/img/2022/202205101408073.png)

运行 IDEA 中的 maven 插件，编译和打包修改好的 sentinel-dashboard

![](https://cdn.xn2001.com/img/2022/202205101408008.png)

启动方式跟官方一样

```sh
java -jar sentinel-dashboard.jar
```

如果要修改 Nacos 地址，可以添加参数

```sh
java -jar -Dnacos.addr=localhost:8848 sentinel-dashboard.jar
```

# Seata分布式事务

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/11-seata-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/11-seata-demo

## 分布式事务问题

> 此处可结合 MySQL 事务一起学习，可直接查看该笔记
>
> <a class="post_link" href="https://www.xn2001.com/archives/677.html"><i data-feather="file-text"></i>MySQL入门到精通？</a>

在传统数据库事务中，必须要满足四个原则，我们把他称为 ACID

![](https://cdn.xn2001.com/img/2022/202205231445816.png)

**分布式事务**，就是指不是在单个服务或单个数据库架构下，产生的事务，例如

- 跨数据源的分布式事务
- 跨服务的分布式事务
- 综合情况

在数据库水平拆分、服务垂直拆分之后，一个业务操作通常要跨多个数据库、服务才能完成。例如电商行业中比较常见的下单付款案例，包括下面几个行为：

1. 创建新订单
2. 扣减商品库存
3. 从用户账户余额扣除金额

完成上面的操作需要访问三个不同的微服务和三个不同的数据库。

![](https://cdn.xn2001.com/img/2022/202205231447738.png)

订单的创建、库存的扣减、账户扣款在每一个服务和数据库内是一个本地事务，可以保证 ACID 原则。

但是当我们把三件事情看做一个"业务"，要满足保证“业务”的原子性，要么所有操作全部成功，要么全部失败，不允许出现部分成功部分失败的现象，这就是**分布式系统下的事务**。此时 ACID 难以满足，这是分布式事务要解决的问题。

以下是一个项目演示，如下图有一个微服务项目

![](https://cdn.xn2001.com/img/2022/202205231502038.png)

其中：seata-demo 是父工程，负责管理项目依赖

- account-service：账户服务，负责管理用户的资金账户。提供扣减余额的接口
- storage-service：库存服务，负责管理商品库存。提供扣减库存的接口
- order-service：订单服务，负责管理订单。创建订单时，需要调用 account-service 和 storage-service

我们去创建订单，发送 POST 请求

```
curl --location --request POST 'http://localhost:8082/order?userId=user202103032042012&amp;commodityCode=100202003032041&amp;count=20&amp;money=200'
```

测试发现，当库存不足时，此时账户余额已经扣减，并不会回滚，出现了分布式事务问题。

![](https://cdn.xn2001.com/img/2022/202205231505539.png)

## 解决分布式事务

### CAP定理

1998年，加州大学的计算机科学家 Eric Brewer 提出，分布式系统有三个指标。

> - Consistency（一致性）
> - Availability（可用性）
> - Partition tolerance （分区容错性）

它们的第一个字母分别是 C、A、P。

Eric Brewer 说，这三个指标不可能同时做到。这个结论就叫做 CAP 定理。

![](https://cdn.xn2001.com/img/2022/202205231506789.png)

#### Consistency一致性

Consistency（一致性）：用户访问分布式系统中的任意节点，得到的数据必须一致。

比如现在包含两个节点，其中的初始数据是一致的

![](https://cdn.xn2001.com/img/2022/202205231506787.png)

当我们修改其中一个节点的数据时，两者的数据产生了差异

![](https://cdn.xn2001.com/img/2022/202205231506790.png)

要想保住一致性，就必须实现 node01 到 node02 的数据同步

![](https://cdn.xn2001.com/img/2022/202205231506801.png)



#### Availability可用性

Availability （可用性）：用户访问集群中的任意健康节点，必须能得到响应，而不是超时或拒绝。

如图，有三个节点的集群，访问任何一个都可以及时得到响应

![](https://cdn.xn2001.com/img/2022/202205231506803.png)

当有部分节点因为网络故障或其它原因无法访问时，代表节点不可用

![](https://cdn.xn2001.com/img/2022/202205231506810.png)

#### Partition Tolerance分区容错

Partition（分区）：因为网络故障或其它原因导致分布式系统中的部分节点与其它节点失去连接，形成独立分区。

Tolerance（容错）：在集群出现分区时，整个系统也要持续对外提供服务。

![](https://cdn.xn2001.com/img/2022/202205231506703.png)

在分布式系统中，系统间的网络不能 100% 保证健康，一定会有故障的时候，而服务有必须对外保证服务。**因此 Partition Tolerance 不可避免。**当节点接收到新的数据变更时，就会出现问题了。

![](https://cdn.xn2001.com/img/2022/202205231506722.png)

如果此时要保证**一致性**，就必须等待网络恢复，完成数据同步后，整个集群才对外提供服务，服务处于阻塞状态，不可用。

如果此时要保证**可用性**，就不能等待网络恢复，那 node01、node02 与 node03 之间就会出现数据不一致。

也就是说，在 P 一定会出现的情况下，A 和 C 之间只能实现一个。

### BASE理论

BASE 理论是对 CAP 的一种解决思路，包含三个思想：

- **Basically Available** **（基本可用）**：分布式系统在出现故障时，允许损失部分可用性，即保证核心可用。
- **Soft State（软状态）**：在一定时间内，允许出现中间状态，比如临时的不一致状态。
- **Eventually Consistent（最终一致性）**：虽然无法保证强一致性，但是在软状态结束后，最终达到数据一致。

分布式事务最大的问题是各个子事务的一致性问题，因此可以借鉴 CAP定理 和 BASE理论，有两种解决思路

- AP 模式：各子事务分别执行和提交，允许出现结果不一致，然后采用弥补措施恢复数据即可，实现最终一致。

- CP 模式：各个子事务执行后互相等待，同时提交，同时回滚，达成强一致。但事务等待过程中，处于弱可用状态。

> 前面我们所学的 Elasticsearch 集群就是 CP 模式，保证了数据的一致性。

解决分布式事务，各个子系统之间必须能感知彼此的事务状态，才能保证状态一致，因此需要一个事务协调者来协调每一个事务的参与者，也就是需要一个事务协调者。（TC）

另外，这里的子系统事务，称为**分支事务**；有关联的各个分支事务在一起称为**全局事务**。

![](https://cdn.xn2001.com/img/2022/202205231506031.png)

## 部署Seata

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。

官网地址：http://seata.io/，其中的文档、播客中提供了大量的使用说明、源码分析。

![](https://cdn.xn2001.com/img/2022/202205231707983.png)

### Seata的架构

Seata 事务管理中有三个重要的角色

- **TC (Transaction Coordinator) -** **事务协调者**：维护全局和分支事务的状态，协调全局事务提交或回滚。

- **TM (Transaction Manager) -** **事务管理器**：定义全局事务的范围、开始全局事务、提交或回滚全局事务。

- **RM (Resource Manager) -** **资源管理器**：管理分支事务处理的资源，与 TC 交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

![](https://cdn.xn2001.com/img/2022/202205231704030.png)

Seata 基于上述架构提供了四种不同的分布式事务解决方案

- XA 模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
- TCC 模式：最终一致的分阶段事务模式，有业务侵入
- AT 模式：最终一致的分阶段事务模式，无业务侵入，也是 Seata 的默认模式
- SAGA 模式：长事务模式，有业务侵入

无论哪种方案，都离不开 TC，也就是事务的协调者。

![](https://cdn.xn2001.com/img/2022/202205232202909.png)

### 部署TC服务

下载 seata-server 包，https://seata.io/zh-cn/blog/download.html

在非中文目录解压缩，其目录结构如下

![](https://cdn.xn2001.com/img/2022/202205241709346.png)

修改 conf 目录下的 registry.conf 文件

![](https://cdn.xn2001.com/img/2022/202205241709343.png)

```properties
registry {
  # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
  type = "nacos"

  nacos {
    # seata tc 服务注册到 nacos的服务名称，可以自定义
    application = "seata-tc-server"
    serverAddr = "127.0.0.1:8848"
    group = "DEFAULT_GROUP"
    namespace = ""
    cluster = "SH"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
  type = "nacos"
  # 配置nacos地址等信息
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "DEFAULT_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
```

为了让 TC 服务的集群可以共享配置，我们选择了 Nacos 作为统一配置中心。因此服务端配置文件 `seataServer.properties` 文件需要在Nacos 中配好。在 Nacos 后台新建一个配置文件：http://localhost:8848/nacos/

![](https://cdn.xn2001.com/img/2022/202205241709375.png)



配置内容如下

> 注意：一定要把注释删掉！要修改你的数据库信息。

```properties
# 数据存储方式，db代表数据库
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
# 这是MySQL8的驱动，MySQL5使用的是com.mysql.jdbc.Driver
store.db.driverClassName=com.mysql.cj.jdbc.Driver
# 数据库地址、用户名、密码都需要修改成你自己的数据库信息。
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;serverTimezone=GMT%2B8
store.db.user=root
store.db.password=123456
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
# 事务、日志等配置
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
# 客户端与服务端传输方式
transport.serialization=seata
transport.compressor=none
# 关闭metrics功能，提高性能
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```

TC 服务在管理分布式事务时，需要记录事务相关数据到数据库中，你需要提前创建好这些表。新建一个名为 seata 的数据库，运行 SQL

这些表主要记录全局事务、分支事务、全局锁信息。

```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `resource_group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `branch_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE,
  INDEX `idx_xid`(`xid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of branch_table
-- ----------------------------

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
  `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_service_group` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `timeout` int(11) NULL DEFAULT NULL,
  `begin_time` bigint(20) NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`xid`) USING BTREE,
  INDEX `idx_gmt_modified_status`(`gmt_modified`, `status`) USING BTREE,
  INDEX `idx_transaction_id`(`transaction_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of global_table
-- ----------------------------


-- ----------------------------
-- Records of lock_table
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

```

进入 bin 目录，运行其中的 seata-server.bat 即可。

![](https://cdn.xn2001.com/img/2022/202205241709380.png)

启动成功后，打开浏览器，访问 Nacos 地址：http://localhost:8848，然后进入服务列表页面，可以看到 seata-tc-server 的信息。

![](https://cdn.xn2001.com/img/2022/202205241709137.png)

### Seata微服务集成

> 很多人说这里很难，启动老是失败，注意检查好上一步配置是否正确，比如集群是否是 SH，分区是否是 DEFAULT_GROUP，实例名是否是 seata-tc-server

首先，我们需要在微服务中引入 Seata 依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <exclusions>
        <!--版本较低，1.3.0，因此排除-->
        <exclusion>
            <artifactId>seata-spring-boot-starter</artifactId>
            <groupId>io.seata</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!--seata starter 采用1.4.2版本-->
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>${seata.version}</version>
</dependency>
```

需要修改 application.yml 文件，添加一些配置，例如在 order-service 服务中的 application.yml，配置 TC 服务信息，通过注册中心 Nacos，结合服务名称获取 TC 地址

```yaml
seata:
  registry: # TC服务注册中心的配置，微服务根据这些信息去注册中心获取tc服务地址
    type: nacos # 注册中心类型 nacos
    nacos:
      server-addr: 127.0.0.1:8848 # nacos地址
      namespace: "" # namespace，默认为空
      group: DEFAULT_GROUP # 分组，默认是DEFAULT_GROUP
      application: seata-tc-server # seata服务名称
      username: nacos
      password: nacos
  tx-service-group: seata-demo # 事务组名称
  service:
    vgroup-mapping: # 事务组与cluster的映射关系
      seata-demo: SH
```

微服务如何根据这些配置寻找 TC 地址的，我们知道注册到 Nacos 中的微服务，确定一个具体实例需要四个信息，`namespace+group+application+cluster`

- namespace：命名空间，为空就是默认的 public
- group：分组
- application：服务名
- cluster：集群名

以上四个信息，在刚才的 yml 文件中都能找到。

![](https://cdn.xn2001.com/img/2022/202205241726339.png)

结合起来，TC 服务的信息就是：`public@DEFAULT_GROUP@seata-tc-server@SH`，这样就能确定 TC 服务集群了。然后就可以去 Nacos拉取对应的实例信息。

启动微服务后，Seata 控制台会显示连接上的服务。

![](https://cdn.xn2001.com/img/2022/202205242305852.png)

### TC服务异地容灾

1.模拟异地容灾的 TC 集群

计划启动两台 Seata 的 TC 服务节点

| 节点名称 | ip地址    | 端口号 | 集群名称 |
| -------- | --------- | ------ | -------- |
| seata    | 127.0.0.1 | 8091   | SH       |
| seata2   | 127.0.0.1 | 8092   | HZ       |

之前我们已经启动了一台 seata 服务，端口是 8091，集群名为 SH。

现在，将 seata 目录复制一份，起名为 seata2

修改 seata2/conf/registry.conf 内容如下

```nginx
registry {
  # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
  type = "nacos"

  nacos {
    # seata tc 服务注册到 nacos的服务名称，可以自定义
    application = "seata-tc-server"
    serverAddr = "127.0.0.1:8848"
    group = "DEFAULT_GROUP"
    namespace = ""
    cluster = "HZ"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
  type = "nacos"
  # 配置nacos地址等信息
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
```

进入 seata2/bin 目录，然后运行命令

```powershell
seata-server.bat -p 8092
```

打开 nacos 控制台，查看服务列表

![](https://cdn.xn2001.com/img/2022/202205241709152.png)

![](https://cdn.xn2001.com/img/2022/202205241709154.png)

2.将事务组映射配置到 nacos

接下来，我们需要将 tx-service-group 与 cluster 的映射关系都配置到 Nacos 配置中心。

新建一个配置

![](https://cdn.xn2001.com/img/2022/202205241709167.png)

配置的内容如下

```properties
# 事务组映射关系
service.vgroupMapping.seata-demo=SH
service.enableDegrade=false
service.disableGlobalTransaction=false
# 与TC服务的通信配置
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=false
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
# RM配置
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=false
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
# TM配置
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000

# undo日志配置
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
client.log.exceptionRate=100
```

3.微服务读取nacos配置

接下来，需要修改每一个微服务的 application.yml 文件，让微服务读取 Nacos 中的 client.properties 文件

```yaml
seata:
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SEATA_GROUP
      data-id: client.properties
```

重启微服务，现在微服务到底是连接 TC 的 SH 集群，还是 TC 的 HZ 集群，都统一由 Nacos 的 client.properties 来决定了。

大体先了解这么多，具体参考官方文档学习：https://seata.io/zh-cn/docs/overview/what-is-seata.html

## XA模式

XA 规范 是 X/Open 组织定义的分布式事务处理（DTP，Distributed Transaction Processing）标准，XA 规范描述了全局的 TM 与局部的 RM 之间的接口，几乎所有主流的数据库都对 XA 规范提供了支持。

XA 是规范，目前主流数据库都实现了这种规范，实现的原理都是**基于两阶段提交**。

正常情况：

![](https://cdn.xn2001.com/img/2022/202205242231750.png)

异常情况：

![](https://cdn.xn2001.com/img/2022/202205242231755.png)

一阶段：

1.事务协调者通知每个事物参与者执行本地事务

2.本地事务执行完成后报告事务执行状态给事务协调者，此时事务不提交，继续持有数据库锁

二阶段：

- 事务协调者基于一阶段的报告来判断下一步操作
  - 如果一阶段都成功，则通知所有事务参与者，提交事务
  - 如果一阶段任意一个参与者失败，则通知所有事务参与者回滚事务

Seata 对原始的 XA 模式做了简单的封装和改造，以适应自己的事务模型，基本架构如下图

![](https://cdn.xn2001.com/img/2022/202205242231760.png)

RM 一阶段的工作：

① 注册分支事务到TC

② 执行分支业务 SQL 但不提交

③ 报告执行状态到 TC

TC 二阶段的工作：

- TC 检测各分支事务执行状态

  a.如果都成功，通知所有 RM 提交事务

  b.如果有失败，通知所有 RM 回滚事务

RM 二阶段的工作：

- 接收 TC 指令，提交或回滚事务

### 优缺点

XA 模式的优点是什么？

- 事务的强一致性，满足 ACID 原则。
- 常用数据库都支持，实现简单，并且没有代码侵入。

XA 模式的缺点是什么？

- 因为一阶段需要锁定数据库资源，等待二阶段结束才释放，性能较差。
- 依赖关系型数据库实现事务。

### 实现XA模式

Seata 的 starter 已经完成了 XA 模式的自动装配，实现非常简单，步骤如下

1）修改每一个微服务的 application.yml 文件（每个参与事务的微服务），开启XA模式：

```yaml
seata:
  data-source-proxy-mode: XA
```

2）给发起全局事务的入口方法添加 `@GlobalTransactional` 注解

本例中是 OrderServiceImpl 中的 create 方法.

![](https://cdn.xn2001.com/img/2022/202205242231783.png)

3）重启服务并测试

重启 order-service，再次测试，发现无论怎样异常情况，三个微服务都能成功回滚。

## AT模式

AT 模式同样是分阶段提交的事务模型，不过缺弥补了XA模型中资源锁定周期过长的缺陷。

基本流程图：

![](https://cdn.xn2001.com/img/2022/202205242258121.png)

阶段一 RM 的工作：

- 注册分支事务
- **记录 undo-log（数据快照）**
- **执行业务 SQL 并提交事务**
- 报告事务状态

阶段二提交时 RM 的工作：

- 删除 undo-log 即可

阶段二回滚时 RM 的工作：

- 根据 undo-log 恢复数据到更新前

### 流程梳理

我们用一个真实的业务来梳理下 AT 模式的原理。

比如，现在有一个数据库表，记录用户余额

| **id** | **money** |
| ------ | --------- |
| 1      | 100       |

其中一个分支业务要执行的 SQL 为

```sql
update tb_account set money = money - 10 where id = 1
```

AT模式下，当前分支事务执行流程如下：

一阶段：

1）TM 发起并注册全局事务到 TC

2）TM 调用分支事务

3）分支事务准备执行业务 SQL

4）RM 拦截业务 SQL，根据 where 条件查询原始数据，形成快照。

```json
{
    "id": 1, "money": 100
}
```

5）RM 执行业务 SQL，提交本地事务，释放数据库锁。此时 `money = 90`

6）RM 报告本地事务状态给 TC

二阶段：

1）TM 通知 TC 事务结束

2）TC 检查分支事务状态。如果都成功，则立即删除快照；如果有分支事务失败，需要回滚。读取快照数据`{"id": 1, "money": 100}`，将快照恢复到数据库。此时数据库再次恢复为 100。

![](https://cdn.xn2001.com/img/2022/202205242258118.png)

### AT与XA的区别

简述 AT 模式与 XA 模式最大的区别是什么？

- XA 模式一阶段不提交事务，锁定资源；AT 模式一阶段直接提交，不锁定资源。
- XA 模式依赖数据库机制实现回滚；AT 模式利用数据快照实现数据回滚。
- XA 模式强一致；AT 模式最终一致。

### 脏写问题

> 注意：此处脏写是在多线程环境下的问题

在**多线程并发**访问 AT 模式的分布式事务时，有可能出现脏写问题。如图，当事务 1 因为某些原因要恢复快照时，另一个线程的事务 2 白更新了一次数据，出现了脏写问题。

![](https://cdn.xn2001.com/img/2022/202205242258122.png)

解决思路就是引入了全局锁的概念。在提交事务之前，先去拿全局锁，避免同一时刻有另外一个事务在操作当前数据，拿不到全局锁超过一定时间则回滚。如图，这样一来事务 2 就更新失败了，此时事务 1 恢复数据不会给另一个线程事务 2 造成改了又没改的离谱现象。

![](https://cdn.xn2001.com/img/2022/202205242258136.png)

### 优缺点

AT 模式的优点：

- 一阶段完成直接提交事务，释放数据库资源，性能比较好
- 利用全局锁实现读写隔离
- 没有代码侵入，框架自动完成回滚和提交

AT 模式的缺点：

- 两阶段之间属于软状态，属于最终一致
- 框架的快照功能会影响性能，但比XA模式要好很多

### 实现AT模式

AT 模式中的快照生成、回滚等动作都是由框架自动完成，没有任何代码侵入，因此实现非常简单。

只不过，AT模式需要一个表来记录全局锁、另一张表来记录数据快照 undo_log

1）导入数据库表，记录全局锁

lock_table 表导入到 TC 服务关联的数据库，我这里的 TC 服务数据库是 seata

```sql
-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
  `row_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `xid` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pk` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`row_key`) USING BTREE,
  INDEX `idx_branch_id`(`branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
```

undo_log 表导入到微服务关联的数据库，我这里的微服务数据库是 seata_demo

```sql
-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;
```

2）修改 application.yml 文件，将事务模式修改为 AT 模式即可。

```yaml
seata:
  data-source-proxy-mode: AT # 默认就是AT
```

3）重启服务并测试。

## TCC模式

TCC 模式与 AT 模式非常相似，每阶段都是独立事务，不同的是 TCC 模式通过人工编码来实现数据恢复。需要实现三个方法：

- Try：资源的检测和预留； 

- Confirm：完成资源操作业务；要求 Try 成功 Confirm 一定要能成功。

- Cancel：预留资源释放，可以理解为 try 的反向操作。

例如，一个扣减用户余额的业务。假设账户 A 原来余额是 100，需要余额扣减 30 元。

**阶段一（ Try ）**：检查余额是否充足，如果充足则冻结金额增加 30 元，可用余额扣除 30

初始余额

![](https://cdn.xn2001.com/img/2022/202205261907452.png)

余额充足，可以冻结

![](https://cdn.xn2001.com/img/2022/202205261907455.png)

此时，总金额 = 冻结金额 + 可用金额，数量依然是 100 不变。事务直接提交无需等待其它事务。

**阶段二（Confirm)**：假如要提交（Confirm），则冻结金额扣减 30

确认可以提交，不过之前可用金额已经扣减过了，这里只要清除冻结金额就好了

![](https://cdn.xn2001.com/img/2022/202205261907456.png)

此时，总金额 = 冻结金额 + 可用金额 = 0 + 70  = 70元

**阶段二(Canncel)**：如果要回滚（Cancel），则冻结金额扣减 30，可用余额增加 30

需要回滚，那么就要释放冻结金额，恢复可用金额

![](https://cdn.xn2001.com/img/2022/202205261907454.png)

Seata 中的 TCC 模型依然延续之前的事务架构，如图：

![](https://cdn.xn2001.com/img/2022/202205261907469.png)

### 优缺点

TCC 模式的每个阶段是做什么的？

- Try：资源检查和预留
- Confirm：业务执行和提交
- Cancel：预留资源的释放

TCC 的优点是什么？

- 一阶段完成直接提交事务，释放数据库资源，性能好
- 相比 AT 模型，无需生成快照，无需使用全局锁，性能最强
- 不依赖数据库事务，而是依赖补偿操作，可以用于非事务型数据库

TCC 的缺点是什么？

- 有代码侵入，需要人为编写 try、Confirm 和 Cancel 接口，太麻烦
- 软状态，事务是最终一致
- 需要考虑 Confirm 和 Cancel 的失败情况，做好幂等处理

### 空回滚

当某分支事务的 try 阶段**阻塞**时，可能导致全局事务超时而触发二阶段的 cancel 操作。在未执行 try 操作时先执行了 cancel 操作，这时cancel 不能做回滚，就是**空回滚**。执行 cancel 操作时，应当判断 try 是否已经执行，如果尚未执行，则应该空回滚。

![](https://cdn.xn2001.com/img/2022/202205261921656.png)

### 业务悬挂

空回滚后出现的一个新问题：对于已经空回滚的业务，之前被阻塞的 try 操作恢复，继续执行 try，可此时整个业务都已经结束了，难道我们可以让他再去走 confirm 或 cancel 吗，显然不行。因此事务一直处于中间状态，这就是**业务悬挂**，我们应当去避免这种情况。所以执行 try 操作时，应当判断 cancel 是否已经执行过了，如果已经执行，**应当阻止空回滚后的 try 操作，避免悬挂。**

### 实现TCC模式

#### 设计数据表

实现 TCC 模式我们都知道要去记录冻结状态，所以这就需要一个数据表。

xid：是全局事务 id

freeze_money：用来记录用户冻结金额

state：用来记录事务状态

```sql
CREATE TABLE `account_freeze_tbl` (
  `xid` varchar(128) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `freeze_money` int(11) unsigned DEFAULT '0' COMMENT '冻结金额',
  `state` int(1) DEFAULT NULL COMMENT '事务状态，0:try，1:confirm，2:cancel',
  PRIMARY KEY (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
```

- Try 业务
  - 记录冻结金额和事务状态到 account_freeze 表
  - 扣减 account 表可用金额
- Confirm 业务
  - 根据 xid 删除 account_freeze 表的冻结记录
- Cancel 业务
  - 修改 account_freeze 表，冻结金额为 0，state 为 2
  - 修改 account 表，恢复可用金额
- 如何判断是否空回滚
  - cancel 业务中，根据 xid 查询 account_freeze，如果为 null 则说明 try 还没做，需要空回滚
- 如何避免业务悬挂
  - try 业务中，根据 xid 查询 account_freeze，如果已经存在则证明 Cancel 已经执行，拒绝执行 try 业务

接下来，我们根据实际业务修改 account-service，利用 TCC 实现余额扣减功能。

#### 声明TCC接口

> Seata 全局事务的 id 可以通过 `RootContext.getXID();` 获取，
>
> 也可以通过 BusinessActionContext 参数的 getXid() 方法获取。

TCC 的 Try、Confirm、Cancel 方法都需要在接口中基于注解来声明，首先是接口上要用 `@LocalTCC`，try 逻辑方法用注解`@TwoPhaseBusinessAction(name="try 方法名", commitMethod="confirm 方法名", rollbackMethod="cancel 方法名") `注明 ，在该方法参数上加入 `@BusinessActionContextParameter(paramName="try 方法的参数")`，可以使得该参数传入 `BusinessActionContext` 类，便于 confirm 和 cancel 读取。

![](https://cdn.xn2001.com/img/2022/202205281606688.png)

我们在 account-service 项目中的 service 包中新建一个接口，声明 TCC 三个接口。

```java
@LocalTCC
public interface AccountTCCService {

    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money")int money);

    boolean confirm(BusinessActionContext ctx);

    boolean cancel(BusinessActionContext ctx);
}
```

#### 编写实现类

在 account-service 服务中的 service.impl 包下新建一个类，实现 TCC 业务

```java
@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper freezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        // 0.获取事务id
        String xid = RootContext.getXID();
        // 处理业务悬挂
        if (freezeMapper.selectById(xid) != null) {
            return;
        }
        // 1.扣减可用余额
        accountMapper.deduct(userId, money);
        // 2.记录冻结金额，事务状态
        AccountFreeze freeze = new AccountFreeze();
        freeze.setUserId(userId);
        freeze.setFreezeMoney(money);
        freeze.setState(AccountFreeze.State.TRY);
        freeze.setXid(xid);
        freezeMapper.insert(freeze);
    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        // 1.获取事务id
        String xid = ctx.getXid();
        // 2.根据id删除冻结记录
        int count = freezeMapper.deleteById(xid);
        return count == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        // 0.查询冻结记录
        String xid = ctx.getXid();
        AccountFreeze freeze = freezeMapper.selectById(xid);
        // 处理空回滚
        if (freeze == null) {
            //空回滚
            freeze = new AccountFreeze();
            String userId = ctx.getActionContext("userId").toString();
            freeze.setUserId(userId);
            freeze.setFreezeMoney(0);
            freeze.setState(AccountFreeze.State.CANCEL);
            freeze.setXid(xid);
            freezeMapper.insert(freeze);
            return true;
        }
        // 幂等判断
        if(freeze.getState() == AccountFreeze.State.CANCEL){
            // 已经处理过了cancel，无需重复
            return true;
        }
        // 1.恢复可用余额
        accountMapper.refund(freeze.getUserId(), freeze.getFreezeMoney());
        // 2.将冻结金额清零，状态改为CANCEL
        freeze.setFreezeMoney(0);
        freeze.setState(AccountFreeze.State.CANCEL);
        int count = freezeMapper.updateById(freeze);
        return count == 1;
    }
}
```

## SAGA模式

Saga 模式是 Seata 即将开源的长事务解决方案，将由蚂蚁金服主要贡献。Seata 官网对于 Saga 的指南：https://seata.io/zh-cn/docs/user/saga.html

分布式事务执行过程中，依次执行各参与者的正向操作，如果所有正向操作均执行成功，那么分布式事务提交。如果任意一个正向操作执行失败，那么分布式事务会去退回去执行前面各参与者的逆向回滚操作，回滚已提交的参与者，使分布式事务回到初始状态。

![](https://cdn.xn2001.com/img/2022/202205281708899.png)

Saga 模式也分为两个阶段

- 一阶段：直接提交本地事务
- 二阶段：成功则什么都不做；失败则通过编写补偿业务来回滚

### 优缺点

优点：

- 事务参与者可以基于事件驱动实现异步调用，吞吐高
- 一阶段直接提交事务，无锁，性能好
- 不用编写 TCC 中的三个阶段，实现简单

缺点：

- 软状态持续时间不确定，时效性差
- 没有锁，没有事务隔离，会有脏写

# Redis分布式缓存

> 陈年旧事 Redis：https://www.xn2001.com/archives/598.html

单机的 Redis 存在以下四大问题，我们将学着去解决。

![](https://cdn.xn2001.com/img/2022/202206142215082.png)

## Redis持久化

- RDB 持久化
- AOF 持久化

### RDB持久化

RDB 全称 Redis Database Backup file（Redis数据备份文件），也被叫做 Redis 数据快照。简单来说就是把内存中的所有数据都记录到磁盘中。当 Redis 实例故障重启后，从磁盘读取快照文件，恢复数据。快照文件称为 RDB 文件，**默认是保存在当前运行目录。**

RDB 持久化在四种情况下会执行

- 执行 save 命令
- 执行 bgsave 命令
- Redis 停机时
- 触发 RDB 条件时

**save 命令**

执行下面的命令，可以立即执行一次 RDB

![](https://cdn.xn2001.com/img/2022/202206142216902.png)

save 命令会导致主进程执行 RDB，这个过程中其它所有命令都会被阻塞。只有在数据迁移时可能用到。

**bgsave 命令**

下面的命令可以异步执行 RDB

![](https://cdn.xn2001.com/img/2022/202206142216906.png)

这个命令执行后会开启独立进程完成 RDB，主进程可以持续处理用户请求，不受影响。

**停机时**

Redis 停机时会执行一次 save 命令，实现 RDB 持久化。

**自动触发 RDB 条件**

Redis 内部有触发 RDB 的机制，可以在 redis.conf 文件中找到，格式如下：

```properties
# 900秒内，如果至少有1个key被修改，则执行bgsave
# save "" 则表示禁用RDB
save 900 1  
save 300 10  
save 60 10000 
```

RDB 的其它配置也可以在 redis.conf 文件中设置

```properties
# 是否压缩 ,建议不开启，压缩也会消耗cpu，磁盘的话不值钱
rdbcompression yes
# RDB文件名称
dbfilename dump.rdb  
# 文件保存的路径目录
dir ./ 
```

bgsave 开始时会 fork 主进程得到子进程，子进程共享主进程的内存数据。完成 fork 后读取内存数据并写入 RDB 文件。

fork 采用的是 copy-on-write 技术：当主进程执行读操作时，访问共享内存；当主进程执行写操作时，则会拷贝一份数据，执行写操作。

![](https://cdn.xn2001.com/img/2022/202206142216920.png)

RDB 方式 bgsave 的基本流程？

- fork主进程得到一个子进程，共享内存空间
- 子进程读取内存数据并写入新 的RDB 文件
- 用新 RDB 文件替换旧的 RDB 文件

RDB 会在什么时候自动执行？save 60 1000代表什么含义？

- 默认是服务停止时
- 代表 60s 内至少执行 1000 次修改则触发 RDB

RDB 的缺点？

- RDB 执行间隔时间长，两次 RDB 之间写入数据有丢失的风险
- fork 子进程、压缩、写出 RDB 文件都比较耗时

### AOF持久化

AOF 全称为 Append Only File（追加文件），Redis 处理的每一个写命令都会记录在 AOF 文件，可以看做是命令日志文件。

![](https://cdn.xn2001.com/img/2022/202206142216922.png)

AOF 默认是关闭的，需要修改 redis.conf 配置文件来开启 AOF

```properties
# 是否开启AOF功能，默认是no
appendonly yes
# AOF文件的名称
appendfilename "appendonly.aof"
```

AOF 的命令记录的频率也可以通过 redis.conf 文件来配

```properties
# 表示每执行一次写命令，立即记录到AOF文件
appendfsync always 
# 写命令执行完先放入AOF缓冲区，然后表示每隔1秒将缓冲区数据写到AOF文件，是默认方案
appendfsync everysec 
# 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
appendfsync no
```

三种策略对比

![](https://cdn.xn2001.com/img/2022/202206142216924.png)

**AOF文件重写**

因为是记录命令，AOF 文件会比 RDB 文件大的多。而且 AOF 会记录对同一个 key 的多次写操作，但只有最后一次写操作才有意义。通过执行 bgrewriteaof 命令，可以让 AOF 文件执行重写功能，用最少的命令达到相同效果。

![](https://cdn.xn2001.com/img/2022/202206142216934.png)

如图，AOF 原本有三个命令，但是这三个都是对 num 的操作，第二次会覆盖第一次的值，因此第一个命令记录下来没有意义。

所以重写命令后，AOF文件内容就是：`mset name jack num 666`

Redis 也会在触发阈值时自动去重写 AOF 文件。阈值也可以在 redis.conf 中配置

```properties
# AOF文件比上次文件 增长超过多少百分比则触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写 
auto-aof-rewrite-min-size 64mb 
```

RDB 和 AOF 各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会结合两者来使用。

![](https://cdn.xn2001.com/img/2022/202206142216414.png)

Redis 支持同时开启 RDB 和 AOF，在这种情况下当 Redis 重启的时候会优先载入 AOF 文件来恢复原始的数据，因为在通常情况下 AOF 文件保存的数据集要比 RDB 文件保存的数据集完整。

## Redis主从复制

单节点 Redis 的并发能力是有上限的，要进一步提高 Redis 的并发能力，就需要搭建主从集群，实现读写分离。

![](https://cdn.xn2001.com/img/2022/202206142243954.png)

### 搭建主从

> 跳过部署三个 Redis，很简单。

共包含三个节点，一个主节点，两个从节点。

这里我们在同一台虚拟机中开启 3 个 Redis 实例，模拟主从集群，信息如下：

|       IP        | PORT |  角色  |
| :-------------: | :--: | :----: |
| 192.168.150.101 | 7001 | master |
| 192.168.150.101 | 7002 | slave  |
| 192.168.150.101 | 7003 | slave  |

为了方便查看日志，我们打开 3 个 ssh 窗口，分别启动 Redis 实例，启动命令：

```sh
# 第1个
redis-server 7001/redis.conf
# 第2个
redis-server 7002/redis.conf
# 第3个
redis-server 7003/redis.conf
```

![](https://cdn.xn2001.com/img/2022/202206150213482.png)

如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

**开启主从关系**

现在三个实例还没有任何关系，要配置主从可以使用 `replicaof` 或者 `slaveof`（5.0以前）命令。

有临时和永久两种模式：

1.修改配置文件（永久生效）

在 redis.conf 中添加一行配置：```slaveof <masterip> <masterport>```

2.使用 redis-cli 客户端连接到redis服务，执行slaveof命令（重启后失效）

`slaveof <masterip> <masterport>`

>  在 5.0 以后新增命令 replicaof，与 salveof 效果一致。

这里我们为了演示方便，使用方式二。通过 redis-cli 命令连接 7002，执行下面命令

```sh
# 连接 7002
redis-cli -p 7002
# 执行slaveof
slaveof 192.168.150.101 7001
```

通过 redis-cli 命令连接 7003，执行下面命令

```sh
# 连接 7003
redis-cli -p 7003
# 执行slaveof
slaveof 192.168.150.101 7001
```

然后连接 7001 节点，查看集群状态：

```sh
# 连接 7001
redis-cli -p 7001
# 查看状态
info replication
```

![](https://cdn.xn2001.com/img/2022/202206150213485.png)

执行下列操作以测试

- 利用 redis-cli 连接7001，执行```set num 123```

- 利用 redis-cli 连接7002，执行```get num```，再执行```set num 666```

- 利用 redis-cli 连接7003，执行```get num```，再执行```set num 888```

可以发现，只有在 7001 这个 master 节点上可以执行写操作，7002 和 7003 这两个 slave 节点只能执行读操作。

### 同步原理

#### 全量同步

主从第一次建立连接时，会执行**全量同步**，将 master 节点的所有数据都拷贝给 slave 节点，流程如下

![](https://cdn.xn2001.com/img/2022/202206150222378.png)

有几个概念需要知道：

- **Replication Id**：简称 replid，是数据集的标记，id 一致则说明是同一数据集。每一个 master 都有唯一的replid，slave 则会继承 master 节点的 replid；
- **offset**：偏移量，随着记录在 repl_baklog 中的数据增多而逐渐增大。slave 完成同步时也会记录当前同步的offset，即 slave 的 offset 永远小于等于 master 的 offset；当 slave 的 offset 小于 master 的 offset，说明 slave 数据落后于 master，需要更新。

因此 slave 做数据同步，必须向 master 声明自己的 replid 和 offset，master 才可以判断到底需要同步哪些数据。而 slave 原本也是一个 master，有自己的 replid 和 offset，当第一次变成 slave，与 master 建立连接时，发送的 replid 和 offset 是自己的 replid 和 offset。master 判断发现 slave 发送来的 replid 与自己的不一致，说明这是一个全新的 slave，就知道要做全量同步了。master 会将自己的 replid 和 offset 都发送给这个 slave，slave 保存这些信息。以后 slave 的replid 就与 master 一致了。因此，**master判断一个节点是否是第一次同步的依据，就是看 replid 是否一致**。

![](https://cdn.xn2001.com/img/2022/202206150222382.png)

完整流程描述：

- slave 节点请求增量同步
- master 节点判断 replid，发现不一致，拒绝增量同步，选择全量同步
- master 将完整内存数据生成 RDB，发送 RDB 到 slave
- slave 清空本地数据，加载 master 的 RDB
- master 将 RDB 期间的命令记录在 repl_baklog，并持续将 log 中的命令发送给 slave
- slave 执行接收到的命令，保持与 master 之间的同步

#### 增量同步

全量同步需要先做 RDB，然后将 RDB 文件通过网络传输给 slave，成本太高。因此除了第一次做全量同步，其它大多数时候 slave 与 master 都是做**增量同步**。

什么是增量同步？就是只更新 slave 与 master 存在差异的部分数据。

![](https://cdn.xn2001.com/img/2022/202206150222386.png)

**repl_backlog 原理**

master 怎么知道 slave 与自己的数据差异在哪里？

这就要说到全量同步时的 repl_baklog 文件了。

这个文件是一个固定大小的数组，只不过数组是环形，也就是说**角标到达数组末尾后，会再次从 0 开始读写**，这样数组头部的数据就会被覆盖。repl_baklog 中会记录 Redis 处理过的命令日志及 offset，包括 master 当前的 offset 和 slave 已经拷贝到的 offset

![](https://cdn.xn2001.com/img/2022/202206150222384.png) 

slave 与 master 的 offset 之间的差异，就是 salve 需要增量拷贝的数据了。

随着不断有数据写入，master 的 offset 逐渐变大，slave 也不断的拷贝，追赶 master 的 offset，直到数组被填满：

![](https://cdn.xn2001.com/img/2022/202206150222403.png) 

![](https://cdn.xn2001.com/img/2022/202206150222407.png) 

此时，如果有新的数据写入，就会覆盖数组中的旧数据。不过，旧的数据只要是绿色的，说明是已经被同步到slave 的数据，即便被覆盖了也没什么影响。因为未同步的仅仅是红色部分。但是，如果 slave 出现网络阻塞，导致 master 的 offset 远远超过了 slave 的 offset，如下图

![](https://cdn.xn2001.com/img/2022/202206150222063.png) 

如果 master 继续写入新数据，其 offset 就会覆盖旧的数据，直到将 slave 现在的 offset 也覆盖了

![](https://cdn.xn2001.com/img/2022/202206150222082.png) 

棕色框中的红色部分，就是尚未同步，但是却已经被覆盖的数据。此时如果 slave 恢复，需要同步，却发现自己的 offset 都没有了，**无法完成增量同步了，只能做全量同步。**

![](https://cdn.xn2001.com/img/2022/202206150222104.png)

### 主从同步优化

主从同步可以保证主从数据的一致性，非常重要。可以从以下几个方面来优化 Redis 主从集群

- 在 master 中配置 `repl-diskless-sync yes` 启用无磁盘复制，避免全量同步时的磁盘 IO
- Redis 单节点上的内存占用不要太大，减少 RDB 导致的过多磁盘IO
- 适当提高 repl_baklog 的大小，发现 slave 宕机时尽快实现故障恢复，尽可能避免全量同步
- 限制一个 master 上的 slave 节点数量，如果实在是太多 slave，则可以采用**主-从-从**链式结构，减少 master 压力

![](https://cdn.xn2001.com/img/2022/202206150222112.png)

简述全量同步和增量同步区别？

- 全量同步：master 将完整内存数据生成 RDB，发送 RDB 到 slave。后续命令则记录在 repl_baklog，逐个发送给slave
- 增量同步：slave 提交自己的 offset 到 master，master 获取 repl_baklog 中从 offset 之后的命令给slave

什么时候执行全量同步？

- slave 节点第一次连接 master 节点时
- slave 节点断开时间太久，repl_baklog 中的 offset 已经被覆盖时

什么时候执行增量同步？

- slave 节点断开又恢复，并且在 repl_baklog 中能找到 offset 时

## Redis哨兵



## Redis分片集群</textarea>

                          <!--文章的页脚部件：打赏和其他信息的输出-->
             <div class="show-foot"><div class="notebook" data-toggle="tooltip" data-original-title="2022 年 07 月 03 日 01 : 05  AM">
                     <i class="fontello fontello-clock-o"></i>
                     <span>最后修改：2022 年 07 月 03 日</span>
                 </div><div class="copyright" data-toggle="tooltip" data-html="true" data-original-title="转载请保留本文转载地址，著作权归作者所有"><span>© 允许规范转载</span>
                 </div>
             </div>
                         <!--打赏模块-->
             <div class="support-author">
                 <button id="support_author"  data-toggle="modal" data-target="#myModal" class="box-shadow-wrap-lg btn_post_footer btn btn-pay btn-yellow btn-rounded"><svg fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" class="icon" aria-hidden="true"><path d="M10.084 7.606c3.375-1.65 7.65-1.154 10.493 1.487 3.497 3.25 3.497 8.519 0 11.77-3.498 3.25-9.167 3.25-12.665 0-.897-.834-2.488-2.96-2.488-5.085" stroke="currentColor" stroke-width="1.4" stroke-miterlimit="10" stroke-linecap="round" stroke-linejoin="round"></path><path d="M17.392 14.78s1.532-1.318-.053-2.79c-1.585-1.473-3.17-.404-3.719.403-.549.807.495 2.082.93 2.69.434.61 1.364 2.182-.054 3.202-1.417 1.012-3.002.658-4.153-.708-1.15-1.367-.602-3.365 0-3.924M17.338 11.982l1.159-1.076M9.87 18.922l.937-.871" stroke="currentColor" stroke-width="1.4" stroke-miterlimit="10" stroke-linecap="round" stroke-linejoin="round"></path><path d="M.8 1.205s7.15 4.673 8.773 6.182c1.623 1.508 3.231 4.008 1.616 5.509-2.195 2.04-4.054.595-6.737-.75-.884-.447-3.15-1.777-3.15-1.777M10.136.9l1.047 3.188" stroke="currentColor" stroke-width="1.4" stroke-miterlimit="10" stroke-linecap="round" stroke-linejoin="round"></path></svg><span>打赏</span></button> <div id="myModal" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
                 <div class="modal-dialog modal-sm" role="document">
                     <div class="modal-content box-shadow-wrap-lg">
                         <div class="modal-header box-shadow-bottom-normal">
                             <button type="button" class="close" data-dismiss="modal"><i style="vertical-align: bottom;" data-feather="x-circle"></i></button>
                             <h4 class="modal-title">赞赏作者</h4>
                         </div>
                         <div class="modal-body">
                             <div class="solid-tab tab-container post_tab">
                                <ul class="nav no-padder b-b scroll-hide" role="tablist"> <li class="nav-item active" role="presentation"><a class="nav-link active" style="" data-toggle="tab"  role="tab" data-target="#alipay_author"><i class="iconfont icon-alipay" aria-hidden="true"></i>支付宝</a></li><li class="nav-item " role="presentation"><a class="nav-link " style="" data-toggle="tab"  role="tab" data-target="#wechatpay_author"><i class="iconfont icon-wechatpay" aria-hidden="true"></i>微信</a></li>        </ul>
                                <div class="tab-content no-border"><div role="tabpanel" id="alipay_author" class="tab-pane fade active in">
                            <img noGallery class="pay-img tab-pane" id="alipay_author" role="tabpanel" src="https://www.xn2001.com/usr/themes/handsome/assets/img/loading.svg" data-original="https://cdn.xn2001.com/blog/zfb.jpg" />
                            </div><div role="tabpanel" id="wechatpay_author" class="tab-pane fade  ">
                            <img noGallery  class="pay-img tab-pane" id="wechatpay_author" role="tabpanel" src="https://www.xn2001.com/usr/themes/handsome/assets/img/loading.svg" data-original="https://cdn.xn2001.com/blog/vx.jpg" />
                            </div>    </div><!--tab-content-->
                             </div> <!--tab-container--></div> <!--modal-body-->
                         </div><!--modal-content-->
                     </div><!--modal-dialog-->
                 </div><!--modal-->
        <button id="star_post" data-cid="663" class="box-shadow-wrap-lg btn_post_footer like_button btn btn-pay btn-rounded">
                 <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="shake-little unlike_svg feather feather-thumbs-up"><path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"></path></svg>
                 <div class="circle-rounded"></div>
                 <svg class="liked_svg" style="transform: scale(2.2);" xmlns="http://www.w3.org/2000/svg" viewBox="30 30 60 60" width="60" height="60" preserveAspectRatio="xMidYMid meet">
                    <g clip-path="url(#__lottie_element_1061)">
                        <g style="display: block;" transform="matrix(1,-0.0000012433954452717444,0.0000012433954452717444,1,47.87498474121094,47.057003021240234)" opacity="1">
                            <g class="like_rotate">
                                <g opacity="1" transform="matrix(1,0,0,1,14.376999855041504,11.416000366210938)">
                                    <path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="10" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="2" d=" M-7.936999797821045,9.531000137329102 C-7.936999797821045,9.531000137329102 3.378000020980835,9.531000137329102 3.378000020980835,9.531000137329102 C3.815999984741211,9.531000137329102 4.209000110626221,9.258999824523926 4.360000133514404,8.847000122070312 C4.360000133514404,8.847000122070312 7.501999855041504,0.36000001430511475 7.501999855041504,0.36000001430511475 C8.020000457763672,-1.0360000133514404 6.986000061035156,-2.5199999809265137 5.497000217437744,-2.5199999809265137 C5.497000217437744,-2.5199999809265137 -0.5669999718666077,-2.5199999809265137 -0.5669999718666077,-2.5199999809265137 C-0.6399999856948853,-2.5199999809265137 -0.6859999895095825,-2.5969998836517334 -0.6499999761581421,-2.6600000858306885 C-0.36800000071525574,-3.1679999828338623 0.6269999742507935,-4.922999858856201 0.8870000243186951,-5.764999866485596 C1.309000015258789,-7.13100004196167 0.847000002861023,-8.715999603271484 -1.4539999961853027,-9.519000053405762 C-1.4759999513626099,-9.526000022888184 -1.4989999532699585,-9.527000427246094 -1.5180000066757202,-9.519000053405762 C-1.5299999713897705,-9.513999938964844 -1.5410000085830688,-9.505999565124512 -1.5490000247955322,-9.494000434875488 C-1.7309999465942383,-9.234999656677246 -2.6489999294281006,-7.934000015258789 -3.6419999599456787,-6.52400016784668 C-4.795000076293945,-4.888000011444092 -6.050000190734863,-3.1059999465942383 -6.380000114440918,-2.638000011444092 C-6.434000015258789,-2.562000036239624 -6.519000053405762,-2.5199999809265137 -6.611000061035156,-2.5199999809265137 C-6.611000061035156,-2.5199999809265137 -7.938000202178955,-2.5199999809265137 -7.938000202178955,-2.5199999809265137 C-7.982999801635742,-2.5199999809265137 -8.020000457763672,-2.4839999675750732 -8.020000457763672,-2.437999963760376 C-8.020000457763672,-2.437999963760376 -8.020000457763672,9.447999954223633 -8.020000457763672,9.447999954223633 C-8.020000457763672,9.494000434875488 -7.982999801635742,9.531000137329102 -7.936999797821045,9.531000137329102z"></path>
                                    <path fill="rgb(255,255,255)" fill-opacity="1" d=" M-7.936999797821045,9.531000137329102 C-7.936999797821045,9.531000137329102 3.378000020980835,9.531000137329102 3.378000020980835,9.531000137329102 C3.815999984741211,9.531000137329102 4.209000110626221,9.258999824523926 4.360000133514404,8.847000122070312 C4.360000133514404,8.847000122070312 7.501999855041504,0.36000001430511475 7.501999855041504,0.36000001430511475 C8.020000457763672,-1.0360000133514404 6.986000061035156,-2.5199999809265137 5.497000217437744,-2.5199999809265137 C5.497000217437744,-2.5199999809265137 -0.5669999718666077,-2.5199999809265137 -0.5669999718666077,-2.5199999809265137 C-0.6399999856948853,-2.5199999809265137 -0.6859999895095825,-2.5969998836517334 -0.6499999761581421,-2.6600000858306885 C-0.36800000071525574,-3.1679999828338623 0.6269999742507935,-4.922999858856201 0.8870000243186951,-5.764999866485596 C1.309000015258789,-7.13100004196167 0.847000002861023,-8.715999603271484 -1.4539999961853027,-9.519000053405762 C-1.4759999513626099,-9.526000022888184 -1.4989999532699585,-9.527000427246094 -1.5180000066757202,-9.519000053405762 C-1.5299999713897705,-9.513999938964844 -1.5410000085830688,-9.505999565124512 -1.5490000247955322,-9.494000434875488 C-1.7309999465942383,-9.234999656677246 -2.6489999294281006,-7.934000015258789 -3.6419999599456787,-6.52400016784668 C-4.795000076293945,-4.888000011444092 -6.050000190734863,-3.1059999465942383 -6.380000114440918,-2.638000011444092 C-6.434000015258789,-2.562000036239624 -6.519000053405762,-2.5199999809265137 -6.611000061035156,-2.5199999809265137 C-6.611000061035156,-2.5199999809265137 -7.938000202178955,-2.5199999809265137 -7.938000202178955,-2.5199999809265137 C-7.982999801635742,-2.5199999809265137 -8.020000457763672,-2.4839999675750732 -8.020000457763672,-2.437999963760376 C-8.020000457763672,-2.437999963760376 -8.020000457763672,9.447999954223633 -8.020000457763672,9.447999954223633 C-8.020000457763672,9.494000434875488 -7.982999801635742,9.531000137329102 -7.936999797821045,9.531000137329102z"></path>
                                </g>
                                <g opacity="1" transform="matrix(1,0,0,1,2.694000005722046,14.967000007629395)">
                                    <path fill="rgb(255,255,255)" fill-opacity="1" d=" M0.5019999742507935,7.0269999504089355 C0.5019999742507935,7.0269999504089355 -0.5019999742507935,7.0269999504089355 -0.5019999742507935,7.0269999504089355 C-0.7789999842643738,7.0269999504089355 -1.003999948501587,6.802000045776367 -1.003999948501587,6.525000095367432 C-1.003999948501587,6.525000095367432 -1.003999948501587,-6.525000095367432 -1.003999948501587,-6.525000095367432 C-1.003999948501587,-6.802000045776367 -0.7789999842643738,-7.0269999504089355 -0.5019999742507935,-7.0269999504089355 C-0.5019999742507935,-7.0269999504089355 0.5019999742507935,-7.0269999504089355 0.5019999742507935,-7.0269999504089355 C0.7789999842643738,-7.0269999504089355 1.003999948501587,-6.802000045776367 1.003999948501587,-6.525000095367432 C1.003999948501587,-6.525000095367432 1.003999948501587,6.525000095367432 1.003999948501587,6.525000095367432 C1.003999948501587,6.802000045776367 0.7789999842643738,7.0269999504089355 0.5019999742507935,7.0269999504089355z"></path>
                                </g>
                            </g>
                        </g>
                    </g>
                 </svg>
                 <span>赞&nbsp;<span id="like_label" class="like_label">124</span></span>
                 </button><div class="mt20 text-center article__reward-info">
                        <span class="mr10">如果觉得我的文章对你有用，请随意赞赏</span>
                       </div>
                     </div><!--support-author-->             <!--/文章的页脚部件：打赏和其他信息的输出-->
         </div>
        </article>
       </div>
       <!--上一篇&下一篇-->
       <nav class="m-t-lg m-b-lg">
        <ul class="pager">
        <li class="next"> <a class="box-shadow-wrap-normal" href="https://www.xn2001.com/archives/659.html" title="ElementUI表格项合并" data-toggle="tooltip"> 
下一篇 </a></li>   <li class="previous"> <a class="box-shadow-wrap-normal" href="https://www.xn2001.com/archives/664.html" title="Java-Socket实现简单聊天室" data-toggle="tooltip"> 上一篇 </a></li>
        </ul>
       </nav>
       <!--评论-->
        
    
    
    <div id="comments">
    
                    <!--评论列表-->
            <div id="post-comment-list" class="skt-loading"><h4 class="comments-title m-t-lg m-b">52 条评论</h4><nav class="loading-nav text-center m-t-lg m-b-lg hide">
<p class="infinite-scroll-request"><i class="animate-spin fontello fontello-refresh"></i>Loading...</p>
</nav><ol class="comment-list">
        <!--自定义评论代码结构-->

        <li id="comment-656" class="comment-body comment-parent comment-odd">
            <div id="div-comment-656" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">韩式炸鸡</b>
                                <a data-coid="656" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-09-05T21:11:19+08:00">September 5th, 2022 at 09:11 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>可以分享下载吗OωO</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=656#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-656', 656);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-655" class="comment-body comment-parent comment-even">
            <div id="div-comment-655" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://q2.qlogo.cn/g?b=qq&nk=2367254022&s=100" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">asdads</b>
                                <a data-coid="655" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-09-01T17:26:01+08:00">September 1st, 2022 at 05:26 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>docker篇呢</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=655#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-655', 655);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-654" class="comment-body comment-parent comment-odd">
            <div id="div-comment-654" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">坏小子</b>
                                <a data-coid="654" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-29T22:19:55+08:00">August 29th, 2022 at 10:19 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>在你这听了一晚上轻音乐</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=654#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-654', 654);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-653" class="comment-body comment-parent comment-even">
            <div id="div-comment-653" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">坏小子</b>
                                <a data-coid="653" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-29T22:17:09+08:00">August 29th, 2022 at 10:17 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>我居然在你这听开了轻音乐 <img src="https://www.xn2001.com/usr/themes/handsome/assets/img/emotion/aru/shy2.png" class="emotion-aru"></p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=653#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-653', 653);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-652" class="comment-body comment-parent comment-odd">
            <div id="div-comment-652" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">坏小子</b>
                                <a data-coid="652" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-29T22:15:32+08:00">August 29th, 2022 at 10:15 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>22年的今天看到了你的笔记，谢谢</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=652#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-652', 652);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-651" class="comment-body comment-parent comment-even">
            <div id="div-comment-651" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">知名的打工人</b>
                                <a data-coid="651" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-29T15:02:58+08:00">August 29th, 2022 at 03:02 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>112</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=651#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-651', 651);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-650" class="comment-body comment-parent comment-odd">
            <div id="div-comment-650" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://q2.qlogo.cn/g?b=qq&nk=1158080997&s=100" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">厉害</b>
                                <a data-coid="650" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-26T20:31:32+08:00">August 26th, 2022 at 08:31 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>博主才21岁。。。好努力</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=650#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-650', 650);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-645" class="comment-body comment-parent comment-even">
            <div id="div-comment-645" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">刚下飞机的人士</b>
                                <a data-coid="645" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-19T09:55:20+08:00">August 19th, 2022 at 09:55 am</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>很有用，感谢分享</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=645#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-645', 645);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-640" class="comment-body comment-parent comment-odd">
            <div id="div-comment-640" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">二胖</b>
                                <a data-coid="640" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-03T20:36:54+08:00">August 3rd, 2022 at 08:36 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>看完视频在看这个加深理解，博主太PLUS了</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=640#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-640', 640);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    
        <!--自定义评论代码结构-->
    
        <li id="comment-637" class="comment-body comment-parent comment-even">
            <div id="div-comment-637" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//115189224acacdcea58b7633e4807ee6?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">风潇潇</b>
                                <a data-coid="637" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count"></span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-07-31T10:44:20+08:00">July 31st, 2022 at 10:44 am</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b></b></span><div class="comment-content-true">
                            <p>后续还有吗？mq高级的内容</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=637#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-637', 637);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断开始 -->
                <div class="comment-children list-unstyled m-l-xxl">
                    <ol class="comment-list">
        <!--自定义评论代码结构-->
    
        <li id="comment-642" class="comment-body comment-child comment-level-odd comment-odd">
            <div id="div-comment-642" class="comment-body">
    
                <a class="pull-left thumb-sm comment-avatar" rel="nofollow">
                    <img nogallery src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" class="img-40px photo img-square normal-shadow">                    
                </a>
                <div class="m-b m-l-xxl">
                    <div class="comment-meta">
            <span class="comment-author vcard">
              <b class="fn">joao</b>
                                <a data-coid="642" class="post-comment-star text-muted star_talk"><i class="glyphicon glyphicon-heart-empty"></i>&nbsp;<span class="star_count">1</span></a>
                              </span>
                        <div class="comment-metadata">
                            <time class="format_time text-muted text-xs block m-t-xs" pubdate="pubdate" datetime="2022-08-06T15:56:13+08:00">August 6th, 2022 at 03:56 pm</time>                        </div>
                    </div>
                    <!--回复内容-->
                    <div class="comment-content m-t-sm">
                        <span class="comment-author-at"><b><a href="#comment-637">@风潇潇</a></b></span><div class="comment-content-true">
                            <p>这也太棒了吧</p>                        </div>
                    </div>
                    <!--回复按钮-->
                    <div class="comment-reply m-t-sm">
                        <a href="https://www.xn2001.com/archives/663.html/comment-page-1?replyTo=642#respond-post-663" rel="nofollow" onclick="return TypechoComment.reply('comment-642', 642);">回复</a>                    </div>
                </div>
    
            </div>
            <!-- 单条评论者信息及内容 -->
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    </ol> <!-- 嵌套评论所有内容-->
                </div>
             <!-- 是否嵌套评论判断结束 -->
        </li><!--匹配`自定义评论的代码结构`下面的li标签-->
    </ol><nav id="comment-navigation" class="text-center m-t-lg m-b-lg" role="navigation"><ol class="page-navigator"><li class="current"><a href="https://www.xn2001.com/archives/663.html/comment-page-1#comments">1</a></li><li><a href="https://www.xn2001.com/archives/663.html/comment-page-2#comments">2</a></li><li><a href="https://www.xn2001.com/archives/663.html/comment-page-3#comments">3</a></li><li><a href="https://www.xn2001.com/archives/663.html/comment-page-4#comments">4</a></li><li class="next"><a href="https://www.xn2001.com/archives/663.html/comment-page-2#comments"><i class="fontello fontello-chevron-right"></i></a></li></ol></nav><script type="text/javascript" id='outputCommentJS'>
(function () {
    window.TypechoComment = {
        dom : function (id) {
            return document.getElementById(id);
        },
    
        create : function (tag, attr) {
            var el = document.createElement(tag);
        
            for (var key in attr) {
                el.setAttribute(key, attr[key]);
            }
        
            return el;
        },
    
        reply : function (cid, coid) {
            var comment = this.dom(cid), parent = comment.parentNode,
                response = this.dom('respond-post-663'), input = this.dom('comment-parent'),
                form = 'form' == response.tagName ? response : response.getElementsByTagName('form')[0],
                textarea = response.getElementsByTagName('textarea')[0];
    
            if (null == input) {
                input = this.create('input', {
                    'type' : 'hidden',
                    'name' : 'parent',
                    'id'   : 'comment-parent'
                });
    
                form.appendChild(input);
            }
    
            input.setAttribute('value', coid);
    
            if (null == this.dom('comment-form-place-holder')) {
                var holder = this.create('div', {
                    'id' : 'comment-form-place-holder'
                });
    
                response.parentNode.insertBefore(holder, response);
            }
    
            comment.appendChild(response);
            this.dom('cancel-comment-reply-link').style.display = '';
    
            if (null != textarea && 'text' == textarea.name) {
                textarea.focus();
            }
    
            return false;
        },
    
        cancelReply : function () {
            var response = this.dom('respond-post-663'),
            holder = this.dom('comment-form-place-holder'), input = this.dom('comment-parent');
    
            if (null != input) {
                input.parentNode.removeChild(input);
            }
    
            if (null == holder) {
                return true;
            }
    
            this.dom('cancel-comment-reply-link').style.display = 'none';
            holder.parentNode.insertBefore(response, holder);
            return false;
        }
    };
})();
</script>
<script type="text/javascript">
var registCommentEvent = function() {
    var event = document.addEventListener ? {
        add: 'addEventListener',
        focus: 'focus',
        load: 'DOMContentLoaded'
    } : {
        add: 'attachEvent',
        focus: 'onfocus',
        load: 'onload'
    };
    var r = document.getElementById('respond-post-663');

    if (null != r) {
        var forms = r.getElementsByTagName('form');
        if (forms.length > 0) {
            var f = forms[0], textarea = f.getElementsByTagName('textarea')[0], added = false;
            var submitButton = f.querySelector('button[type="submit"]');
            if (null != textarea && 'text' == textarea.name) {
                var referSet =  function () {
                    if (!added) {
//                        console.log('commentjs');
                        const child = f.querySelector('input[name="_"]');
                        const child2 = f.querySelector('input[name="checkReferer"]');
                        if (child!=null){
                            f.removeChild(child);                        
                        } 
                        if (child2!=null){
                            f.removeChild(child2);                        
                        } 
                        var input = document.createElement('input');
                        input.type = 'hidden';
                        input.name = '_';
                            input.value = (function () {
    var _1gFK = ''///*'pC'*/'pC'
+//'Y9p'
'94'+//'bJH'
'f'+'d'//'d'
+''///*'bV'*/'bV'
+'0e'//'Z'
+'4'//'h'
+'1'//'aWh'
+//'9Fq'
'a'+/* 'K'//'K' */''+//'V'
'88b'+//'wm'
'400'+'27'//'dv4'
+//'5vq'
'1c'+//'L'
'2e0'+//'F'
'013'+'53'//'LK'
+//'y0'
'9cd'+//'Hk'
'ea1', _v8XUoSn = [[3,4]];
    
    for (var i = 0; i < _v8XUoSn.length; i ++) {
        _1gFK = _1gFK.substring(0, _v8XUoSn[i][0]) + _1gFK.substring(_v8XUoSn[i][1]);
    }
    
    return _1gFK;
})();
                    
                        f.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = 'hidden';
                        input.name = 'checkReferer';
                        input.value = 'false';
                        
                        f.appendChild(input);


                        added = true;
                    }
                };//end of reset
                referSet();
            }
        }
    }
};

$(function(){
    registCommentEvent();
});
</script></div>        
        <!--如果允许评论，会出现评论框和个人信息的填写-->
                                    <div id="respond-post-663" class="respond comment-respond no-borders">

                    <h4 id="reply-title" class="comment-reply-title m-t-lg m-b">发表评论                        <small data-toggle="tooltip" data-placement="right" title="使用cookie技术保留您的个人信息以便您下次快速评论，继续评论表示您已同意该条款">
                            <i style="vertical-align: -1px" data-feather="alert-circle"></i>
                        </small>
                        <small class="cancel-comment-reply">
                            <a id="cancel-comment-reply-link" href="https://www.xn2001.com/archives/663.html#respond-post-663" rel="nofollow" style="display:none" onclick="return TypechoComment.cancelReply();">取消回复</a>                        </small>
                    </h4>
                    <form id="comment_form" method="post" action="https://www.xn2001.com/archives/663.html/comment"  class="comment-form" role="form">
                        <input type="hidden" name="receiveMail" id="receiveMail" value="yes" />
                        <div class="comment-form-comment form-group">
                            <label class="padder-v-sm" for="comment">评论                                <span class="required text-danger">*</span></label>
                            <textarea id="comment" class="textarea form-control OwO-textarea" name="text" rows="5" placeholder="说点什么吧……" onkeydown="if(event.ctrlKey&&event.keyCode==13){document.getElementById('submit').click();return false};"></textarea>
                            <div class="OwO padder-v-sm"></div>
                                                        <div class="secret_comment" id="secret_comment" data-toggle="tooltip"
                            data-original-title="开启该功能，您的评论仅作者和评论双方可见">
                                <label class="secret_comment_label control-label">私密评论</label>
                                <div class="secret_comment_check">
                                    <label class="i-switch i-switch-sm bg-info m-b-ss m-r">
                                        <input type="checkbox" id="secret_comment_checkbox">
                                        <i></i>
                                    </label>
                                </div>
                            </div>
                                                    </div>
                        <!--判断是否登录-->
                                                                            <div id="author_info" class="row row-sm">
                                                                <div class="comment-form-author form-group col-sm-6 col-md-4">
                                    <label for="author">名称                                        <span class="required text-danger">*</span></label>
                                    <div>
                                                                                <img class="author-avatar" src="https://cravatar.cn/avatar//d41d8cd98f00b204e9800998ecf8427e?s=65&r=G&d=" nogallery/>
                                        <input id="author" class="form-control" name="author" type="text" value="韩式炸鸡" maxlength="245" placeholder="姓名或昵称">
                                                                                <div class="random_user_name shake-constant">🎲</div>
                                                                            </div>
                                </div>
    
                                <div class="comment-form-email form-group col-sm-6 col-md-4">
                                    <label for="email">邮箱                                                                            </label>
                                    <input type="text" name="mail" id="mail" class="form-control" placeholder="邮箱（选填,将保密）" value="" />
                                </div>
    
                                <div class="comment-form-url form-group col-sm-12 col-md-4">
                                    <label for="url">地址                                                                            </label>
                                    <input id="url" class="form-control" name="url" type="url" value="" maxlength="200" placeholder="网站或博客"></div>
                            </div>
                                                        <!--提交按钮-->
                            <div class="form-group">
                                <button type="submit" name="submit" id="submit" class="submit btn-rounded box-shadow-wrap-lg btn-gd-primary padder-lg">
                                    <span>发表评论</span>
                                    <span class="text-active">提交中...</span>
                                </button>
                                <i class="animate-spin fontello fontello-spinner hide" id="spin"></i>
                                <input type="hidden" name="comment_post_ID" id="comment_post_ID">
                                <input type="hidden" name="comment_parent" id="comment_parent">
                            </div>
                    </form>
                </div>
                    
            </div>


      </div>
         <div class="resize-pane">
        <div id="trigger_right_content" class="trigger_content"><div class="trigger_drag_content"></div></div>
        <div id="trigger_right_button" data-placement="left" data-toggle="tooltip" data-original-title="点击展开右侧边栏" class="normal-widget resize-pane-trigger box-shadow-wrap-lg"><i data-feather="sidebar"></i></div>
    </div>     </div>
     <!--文章右侧边栏开始-->
             <!--文章右侧边栏结束-->
    </div>
   </main>


​    
<div id="morphing-content" class="hidden read_mode_article">
        <div class="page">
            <h1 class="title">微服务技术栈</h1>
            <div class="metadata singleline"><a href="#" rel="author" class="byline">钟小湖</a>&nbsp;•&nbsp;<span class="delimiter"></span><time class="date">2021 年 09 月 01 日</time></div>     
            <div class="loading-post text-center m-t-lg m-b-lg">
                 <p class="infinite-scroll-request"><i class="animate-spin fontello fontello-refresh"></i>Loading...</p>
             </div><div id="morphing-content-real"></div>
            <textarea class="hide" id="morphing-content-real_text">&lt;div class=&quot;tip share&quot;&gt;请注意，本文编写于 369 天前，最后修改于 65 天前，其中某些信息可能已经过时。&lt;/div&gt;

<div class="tip inlineBlock success">

😀稍等，准备更新后续。
</div>

<div class="tip inlineBlock warning">

📇右侧可以打开目录噢。
</div>

🤤基础篇

- 认识微服务
- 服务拆分
- 远程调用
- Eureka
- Ribbon
- Nacos
- Feign
- Gateway
- RabbitMQ
- Elasticsearch

💻高级篇

- JMeter
- Sentinel
- Seata
- Redis
- 。。。

# 认识微服务

## 单体架构

**单体架构**：将业务的所有功能集中在一个项目中开发，打成一个包部署。

![](https://cdn.xn2001.com/img/2021/20210901083809.png)

**优点**：架构简单，部署成本低

**缺点**：耦合度高（维护困难、升级困难）

## 分布式架构

**分布式架构**：根据业务功能对系统做拆分，每个业务功能模块作为独立项目开发，称为一个服务。

![](https://cdn.xn2001.com/img/2021/20210901092921.png)

**优点**：降低服务耦合，有利于服务升级和拓展

**缺点**：服务调用关系错综复杂

分布式架构虽然降低了服务耦合，但是服务拆分时也有很多问题需要思考：

- 服务拆分的粒度如何界定？
- 服务之间如何调用？
- 服务的调用关系如何管理？

**人们需要制定一套行之有效的标准来约束分布式架构。**

## 微服务

微服务的架构特征：

- 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务能力，做到单一职责
- 自治：团队独立、技术独立、数据独立，独立部署和交付
- 面向服务：服务提供统一标准的接口，与语言和技术无关
- 隔离性强：服务调用做好隔离、容错、降级，避免出现级联问题

![](https://cdn.xn2001.com/img/2022/202205162352847.png)

微服务的上述特性**其实是在给分布式架构制定一个标准**，进一步降低服务之间的耦合度，提供服务的独立性和灵活性。做到高内聚，低耦合。

**因此，可以认为微服务是一种经过良好架构设计的分布式架构方案 。**

其中在 Java 领域最引人注目的就是 SpringCloud 提供的方案了。

## SpringCloud

SpringCloud 是目前国内使用最广泛的微服务框架。官网地址：https://spring.io/projects/spring-cloud。

SpringCloud 集成了各种微服务功能组件，并基于 SpringBoot 实现了这些组件的自动装配，从而提供了良好的开箱即用体验。

其中常见的组件包括：

![](https://cdn.xn2001.com/img/2021/20210901083717.png)

另外，SpringCloud 底层是依赖于 SpringBoot 的，并且有版本的兼容关系，如下：

![](https://cdn.xn2001.com/img/2021/20210901084050.png)

## 内容知识

![需要学习的微服务知识内容](https://cdn.xn2001.com/img/2021/20210901092925.png)

![技术栈](https://cdn.xn2001.com/img/2021/20210901084131.png)

![自动化部署](https://cdn.xn2001.com/img/2021/20210901090737.png)

## 技术栈对比

![](https://cdn.xn2001.com/img/2021/20210901090726.png)

# 服务拆分

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/01-cloud-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/01-cloud-demo

**服务拆分注意事项**

单一职责：不同微服务，不要重复开发相同业务

数据独立：不要访问其它微服务的数据库

面向服务：将自己的业务暴露为接口，供其它微服务调用

![](https://cdn.xn2001.com/img/2021/20210901090745.png)

cloud-demo：父工程，管理依赖

- order-service：订单微服务，负责订单相关业务
- user-service：用户微服务，负责用户相关业务

要求：

- 订单微服务和用户微服务都必须有**各自的数据库**，相互独立
- 订单服务和用户服务**都对外暴露 Restful 的接口**
- 订单服务如果需要查询用户信息，**只能调用用户服务的 Restful 接口**，不能查询用户数据库

微服务项目下，打开 idea 中的 Service，可以很方便的启动。

![](https://cdn.xn2001.com/img/2021/20210901090750.png)

启动完成后，访问 [http://localhost:8080/order/101](http://localhost:8080/order/101)

![](https://cdn.xn2001.com/img/2021/20210901090757.png)

# 远程调用

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/02-cloud-restTemplate
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/02-cloud-restTemplate

正如上面的服务拆分要求中所提到，

> 订单服务如果需要查询用户信息，**只能调用用户服务的 Restful 接口**，不能查询用户数据库

因此我们需要知道 Java 如何去发送 http 请求，Spring 提供了一个 RestTemplate 工具，只需要把它创建出来即可。（即注入 Bean）

![](https://cdn.xn2001.com/img/2021/20210901090814.png)

发送请求，自动序列化为 Java 对象。

![](https://cdn.xn2001.com/img/2021/20210901090846.png)

启动完成后，访问：http://localhost:8080/order/101

![](https://cdn.xn2001.com/img/2021/20210901090909.png)

在上面代码的 url 中，我们可以发现调用服务的地址采用硬编码，这在后续的开发中肯定是不理想的，这就需要**服务注册中心**（Eureka）来帮我们解决这个事情。

# Eureka注册中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/03-cloud-eureka
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/03-cloud-eureka

最广为人知的注册中心就是 Eureka，其结构如下：

![](https://cdn.xn2001.com/img/2021/20210901090919.png)

**order-service 如何得知 user-service 实例地址？**

- user-service 服务实例启动后，将自己的信息注册到 eureka-server(Eureka服务端)，叫做**服务注册**
- eureka-server 保存服务名称到服务实例地址列表的映射关系
- order-service 根据服务名称，拉取实例地址列表，这个叫**服务发现**或服务拉取

**order-service 如何从多个 user-service 实例中选择具体的实例？**

order-service从实例列表中利用**负载均衡算法**选中一个实例地址，向该实例地址发起远程调用

**order-service 如何得知某个 user-service 实例是否依然健康，是不是已经宕机？**

- user-service 会**每隔一段时间(默认30秒)向 eureka-server 发起请求**，报告自己状态，称为**心跳**
- 当超过一定时间没有发送心跳时，eureka-server 会认为微服务实例故障，将该实例从服务列表中剔除
- order-service 拉取服务时，就能将故障实例排除了

---

接下来我们动手实践的步骤包括

![](https://cdn.xn2001.com/img/2021/20210901090932.png)

## 搭建注册中心

**搭建 eureka-server**

引入 SpringCloud 为 eureka 提供的 starter 依赖，注意这里是用 **server**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**编写启动类**

注意要添加一个 `@EnableEurekaServer` **注解**，开启 eureka 的**注册中心**功能

```java
package com.xn2001.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

**编写配置文件**

编写一个 application.yml 文件，内容如下：

```yml
server:
  port: 10086
spring:
  application:
    name: eureka-server
eureka:
  client:
    service-url: 
      defaultZone: http://127.0.0.1:10086/eureka
```

其中 `default-zone` 是因为前面配置类开启了注册中心所需要配置的 eureka 的**地址信息**，因为 eureka 本身也是一个微服务，这里也要将自己注册进来，当后面 eureka **集群**时，这里就可以填写多个，使用 “,” 隔开。

启动完成后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901090945.png)

## 服务注册

> 将 user-service、order-service 都注册到 eureka

引入 SpringCloud 为 eureka 提供的 starter 依赖，注意这里是用 **client**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

在启动类上添加注解：`@EnableEurekaClient`

在 application.yml 文件，添加下面的配置：

```yml
spring:
  application:
  	#name：orderservice
    name: userservice
eureka:
  client:
    service-url: 
      defaultZone: http:127.0.0.1:10086/eureka
```

3个项目启动后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901090958.png)

这里另外再补充个小技巧，我们可以通过 idea 的多实例启动，来查看 Eureka 的集群效果。

![](https://cdn.xn2001.com/img/2021/20210901091005.png)

4个项目启动后，访问 http://localhost:10086/

![](https://cdn.xn2001.com/img/2021/20210901091015.png)

## 服务拉取

> 在 order-service 中完成服务拉取，然后通过负载均衡挑选一个服务，实现远程调用

下面我们让 order-service 向 eureka-server 拉取 user-service 的信息，实现服务发现。

首先给 `RestTemplate` 这个 Bean 添加一个 `@LoadBalanced` **注解**，用于开启**负载均衡**。（后面会讲）

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate(){
    return new RestTemplate();
}
```

修改 OrderService 访问的url路径，用**服务名**代替ip、端口：

![](https://cdn.xn2001.com/img/2021/20210901091216.png)

spring 会自动帮助我们从 eureka-server 中，根据 userservice 这个服务名称，获取实例列表后去完成负载均衡。

# Ribbon负载均衡

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/04-cloud-ribbon
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/04-cloud-ribbon

我们添加了 `@LoadBalanced` 注解，即可实现负载均衡功能，这是什么原理呢？

**SpringCloud 底层提供了一个名为 Ribbon 的组件，来实现负载均衡功能。**

![](https://cdn.xn2001.com/img/2021/20210901091242.png)

## 源码跟踪

为什么我们只输入了 service 名称就可以访问了呢？为什么不需要获取ip和端口，这显然有人帮我们根据 service 名称，获取到了服务实例的ip和端口。它就是`LoadBalancerInterceptor`，这个类会在对 RestTemplate 的请求进行拦截，然后从 Eureka 根据服务 id 获取服务列表，随后利用负载均衡算法得到真实的服务地址信息，替换服务 id。

我们进行源码跟踪：

![](https://cdn.xn2001.com/img/2021/20210901091323.png)

这里的 `intercept()` 方法，拦截了用户的 HttpRequest 请求，然后做了几件事：

- `request.getURI()`：获取请求uri，即 http://user-service/user/8
- `originalUri.getHost()`：获取uri路径的主机名，其实就是服务id `user-service`
- `this.loadBalancer.execute()`：处理服务id，和用户请求

这里的 `this.loadBalancer` 是 `LoadBalancerClient` 类型

继续跟入 `execute()` 方法：

![](https://cdn.xn2001.com/img/2021/20210901091330.png)

- `getLoadBalancer(serviceId)`：根据服务id获取 `ILoadBalancer`，而 `ILoadBalancer` 会拿着服务 id 去 eureka 中获取服务列表。
- `getServer(loadBalancer)`：利用内置的负载均衡算法，从服务列表中选择一个。在图中**可以看到获取了8082端口的服务**

可以看到获取服务时，通过一个 `getServer()` 方法来做负载均衡:

 ![](https://cdn.xn2001.com/img/2021/20210901091345.png)

我们继续跟入：

![](https://cdn.xn2001.com/img/2021/20210901091355.png)

继续跟踪源码 `chooseServer()` 方法，发现这么一段代码：

 ![](https://cdn.xn2001.com/img/2021/20210901091414.png)

我们看看这个 `rule` 是谁：

 ![](https://cdn.xn2001.com/img/2021/20210901091432.png)

这里的 rule 默认值是一个 `RoundRobinRule` ，看类的介绍：

![](https://cdn.xn2001.com/img/2021/20210901091442.png)

负载均衡默认使用了轮训算法，当然我们也可以自定义。

## 流程总结

SpringCloud Ribbon 底层采用了一个拦截器，拦截了 RestTemplate 发出的请求，对地址做了修改。

基本流程如下：

- 拦截我们的 `RestTemplate` 请求 http://userservice/user/1
- `RibbonLoadBalancerClient` 会从请求url中获取服务名称，也就是 user-service
- `DynamicServerListLoadBalancer` 根据 user-service 到 eureka 拉取服务列表
- eureka 返回列表，localhost:8081、localhost:8082
- `IRule` 利用内置负载均衡规则，从列表中选择一个，例如 localhost:8081
- `RibbonLoadBalancerClient` 修改请求地址，用 localhost:8081 替代 userservice，得到 http://localhost:8081/user/1，发起真实请求

![](https://cdn.xn2001.com/img/2021/20210901091755.png)

## 负载均衡策略

负载均衡的规则都定义在 IRule 接口中，而 IRule 有很多不同的实现类：

![](https://cdn.xn2001.com/img/2021/20210901091811.png)

不同规则的含义如下：

| **内置负载均衡规则类**    | **规则描述**                                                 |
| ------------------------- | ------------------------------------------------------------ |
| RoundRobinRule            | 简单轮询服务列表来选择服务器。它是Ribbon默认的负载均衡规则。 |
| AvailabilityFilteringRule | 对以下两种服务器进行忽略：（1）在默认情况下，这台服务器如果3次连接失败，这台服务器就会被设置为“短路”状态。短路状态将持续30秒，如果再次连接失败，短路的持续时间就会几何级地增加。 （2）并发数过高的服务器。如果一个服务器的并发连接数过高，配置了AvailabilityFilteringRule 规则的客户端也会将其忽略。并发连接数的上限，可以由客户端设置。 |
| WeightedResponseTimeRule  | 为每一个服务器赋予一个权重值。服务器响应时间越长，这个服务器的权重就越小。这个规则会随机选择服务器，这个权重值会影响服务器的选择。 |
| **ZoneAvoidanceRule**     | 以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。 |
| BestAvailableRule         | 忽略那些短路的服务器，并选择并发数较低的服务器。             |
| RandomRule                | 随机选择一个可用的服务器。                                   |
| RetryRule                 | 重试机制的选择逻辑                                           |

默认的实现就是 `ZoneAvoidanceRule`，**是一种轮询方案**。

## 自定义策略

通过定义 IRule 实现可以修改负载均衡规则，有两种方式：

1 代码方式在 order-service 中的 OrderApplication 类中，定义一个新的 IRule：

![](https://cdn.xn2001.com/img/2021/20210901091832.png)

2 配置文件方式：在 order-service 的 application.yml 文件中，添加新的配置也可以修改规则：

```yml
userservice: # 给需要调用的微服务配置负载均衡规则，orderservice服务去调用userservice服务
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则 
```

**注意**：一般用默认的负载均衡规则，不做修改。

## 饥饿加载

当我们启动 orderservice，第一次访问时，时间消耗会大很多，这是因为 Ribbon 懒加载的机制。

![](https://cdn.xn2001.com/img/2021/20210901091850.png)

Ribbon 默认是采用懒加载，即第一次访问时才会去创建 LoadBalanceClient，拉取集群地址，所以请求时间会很长。

而饥饿加载则会在项目启动时创建 LoadBalanceClient，降低第一次访问的耗时，通过下面配置开启饥饿加载：

```yml
ribbon:
  eager-load:
    enabled: true
    clients: userservice # 项目启动时直接去拉取userservice的集群，多个用","隔开
    # - userservice
    # - orderservice 多个的情况，yaml文件的数组模式	
```

# Nacos注册中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/05-cloud-nacos
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/05-cloud-nacos

SpringCloudAlibaba 推出了一个名为 Nacos 的注册中心，在国外也有大量的使用。

![](https://cdn.xn2001.com/img/2021/20210901091857.png)

解压启动 Nacos，详细请看 [Nacos安装指南](https://www.xn2001.com/archives/661.html)

```
startup.cmd -m standalone
```

访问：http://localhost:8848/nacos/

![](https://cdn.xn2001.com/img/2021/20210901091904.png)

## 服务注册

这里上来就直接服务注册，很多东西可能有疑惑，其实 Nacos 本身就是一个 SprintBoot 项目，这点你从启动的控制台打印就可以看出来，所以就不再需要去额外搭建一个像 Eureka 的注册中心。

**引入依赖**

在 cloud-demo 父工程中引入 SpringCloudAlibaba 的依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.6.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

然后在 user-service 和 order-service 中的pom文件中引入 nacos-discovery 依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

**配置nacos地址**

在 user-service 和 order-service 的 application.yml 中添加 nacos 地址：

```yaml
spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
```

项目重新启动后，可以看到三个服务都被注册进了 Nacos

![](https://cdn.xn2001.com/img/2021/20210901091918.png)

浏览器访问：http://localhost:8080/order/101，正常访问，同时负载均衡也正常。

## 分级存储模型

一个**服务**可以有多个**实例**，例如我们的 user-service，可以有:

- 127.0.0.1:8081
- 127.0.0.1:8082
- 127.0.0.1:8083

假如这些实例分布于全国各地的不同机房，例如：

- 127.0.0.1:8081，在上海机房
- 127.0.0.1:8082，在上海机房
- 127.0.0.1:8083，在杭州机房

Nacos就将同一机房内的实例，划分为一个**集群**。

![](https://cdn.xn2001.com/img/2021/20210901091928.png)

微服务互相访问时，应该尽可能访问同集群实例，因为本地访问速度更快。**当本集群内不可用时，才访问其它集群。**例如：杭州机房内的 order-service 应该优先访问同机房的 user-service。

![](https://cdn.xn2001.com/img/2021/20210901091937.png)

## 配置集群

接下来我们给 user-service **配置集群**

修改 user-service 的 application.yml 文件，添加集群配置：

```yml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ # 集群名称 HZ杭州
```

重启两个 user-service 实例后，我们再去启动一个上海集群的实例。

```
-Dserver.port=8083 -Dspring.cloud.nacos.discovery.cluster-name=SH
```

![](https://cdn.xn2001.com/img/2021/20210901091947.png)

查看 nacos 控制台

![](https://cdn.xn2001.com/img/2021/20210901091957.png)

## NacosRule

Ribbon的默认实现 `ZoneAvoidanceRule` 并不能实现根据同集群优先来实现负载均衡，我们把规则改成 **NacosRule** 即可。我们是用 orderservice 调用 userservice，所以在 orderservice 配置规则。

```java
@Bean
public IRule iRule(){
    //默认为轮询规则，这里自定义为随机规则
    return new NacosRule();
}
```

另外，你同样可以使用配置的形式来完成，具体参考上面的 Ribbon 栏目。

```yml
userservice:
  ribbon:
    NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule #负载均衡规则 
```

然后，再对 orderservice 配置集群。

```yml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ # 集群名称
```

现在我启动了四个服务，分别是：

- orderservice - HZ
- userservice - HZ
- userservice1 - HZ
- userservice2 - SH

访问地址：http://localhost:8080/order/101

在访问中我们发现，只有同在一个 HZ 集群下的 userservice、userservice1 会被调用，并且是随机的。

我们试着把 userservice、userservice2 停掉。依旧可以访问。

在 userservice3 控制台可以看到发出了一串的警告，因为 orderservice 本身是在 HZ 集群的，这波 HZ 集群没有了 userservice，就会去别的集群找。

![](https://cdn.xn2001.com/img/2021/20210901092012.png)

## 权重配置

实际部署中会出现这样的场景：

服务器设备性能有差异，部分实例所在机器性能较好，另一些较差，我们希望性能好的机器承担更多的用户请求。但默认情况下 NacosRule 是同集群内随机挑选，不会考虑机器的性能问题。

因此，Nacos 提供了**权重配置来控制访问频率**，0~1 之间，权重越大则访问频率越高，权重修改为 0，则该实例永远不会被访问。

在 Nacos 控制台，找到 user-service 的实例列表，点击编辑，即可修改权重。

![](https://cdn.xn2001.com/img/2021/20210901092020.png)

在弹出的编辑窗口，修改权重

![](https://cdn.xn2001.com/img/2021/20210901092026.png)

另外，在服务升级的时候，有一种较好的方案：我们也可以通过调整权重来进行平滑升级，例如：先把 userservice 权重调节为 0，让用户先流向 userservice2、userservice3，升级 userservice后，再把权重从 0 调到 0.1，让一部分用户先体验，用户体验稳定后就可以往上调权重啦。

## 环境隔离

Nacos 提供了 namespace 来实现环境隔离功能。

- Nacos 中可以有多个 namespace
- namespace 下可以有 group、service 等
- 不同 namespace 之间**相互隔离**，例如不同 namespace 的服务互相不可见

![](https://cdn.xn2001.com/img/2021/20210901092032.png)

### 创建namespace

默认情况下，所有 service、data、group 都在同一个 namespace，名为 public(保留空间)：

![](https://cdn.xn2001.com/img/2021/20210901092038.png)

我们可以点击页面新增按钮，添加一个 namespace：

![](https://cdn.xn2001.com/img/2021/20210901092050.png)



然后，填写表单：

![](https://cdn.xn2001.com/img/2021/20210901092059.png)

就能在页面看到一个新的 namespace：

![](https://cdn.xn2001.com/img/2021/20210901092114.png)

### 配置namespace

给微服务配置 namespace 只能通过修改配置来实现。

例如，修改 order-service 的 application.yml 文件：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
      discovery:
        cluster-name: HZ
        namespace: 492a7d5d-237b-46a1-a99a-fa8e98e4b0f9 # 命名空间ID
```

重启 order-service 后，访问控制台。

**public**

![](https://cdn.xn2001.com/img/2021/20210901092143.png)

**dev**

![](https://cdn.xn2001.com/img/2021/20210901092130.png)

此时访问 order-service，因为 namespace 不同，会导致找不到 userservice，控制台会报错：

![](https://cdn.xn2001.com/img/2021/20210901092138.png)

## 临时实例

Nacos 的服务实例分为两种类型：

- **临时实例**：如果实例宕机超过一定时间，会从服务列表剔除，**默认的类型**。
- 非临时实例：如果实例宕机，不会从服务列表剔除，也可以叫永久实例。

配置一个服务实例为永久实例：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        ephemeral: false # 设置为非临时实例
```

另外，Nacos 集群**默认采用AP方式(可用性)**，当集群中存在非临时实例时，**采用CP模式(一致性)**；而 Eureka 采用AP方式，不可切换。（这里说的是 CAP 原理，后面会写到）

# Nacos配置中心

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/05-cloud-nacos
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/05-cloud-nacos

Nacos除了可以做注册中心，同样可以做配置管理来使用。

当微服务部署的实例越来越多，达到数十、数百时，逐个修改微服务配置就会让人抓狂，而且很容易出错。**我们需要一种统一配置管理方案，可以集中管理所有实例的配置。**

![](https://cdn.xn2001.com/img/2021/20210901092150.png)



Nacos 一方面可以将配置集中管理，另一方可以在配置变更时，及时通知微服务，**实现配置的热更新。**

## 创建配置

在 Nacos 控制面板中添加配置文件

![](https://cdn.xn2001.com/img/2021/20210901092159.png)

然后在弹出的表单中，填写配置信息：

![](https://cdn.xn2001.com/img/2021/20210901092206.png)

**注意**：项目的核心配置，需要热更新的配置才有放到 nacos 管理的必要。基本不会变更的一些配置(例如数据库连接)还是保存在微服务本地比较好。

## 拉取配置

首先我们需要了解 Nacos 读取配置文件的环节是在哪一步，在没加入 Nacos 配置之前，获取配置是这样：

![](https://cdn.xn2001.com/img/2021/20210901092215.png)

加入 Nacos 配置，它的读取是在 application.yml 之前的：

![](https://cdn.xn2001.com/img/2021/20210901092223.png)

这时候如果把 nacos 地址放在 application.yml 中，显然是不合适的，**Nacos 就无法根据地址去获取配置了。**

因此，nacos 地址必须放在优先级最高的 bootstrap.yml 文件。

![](https://cdn.xn2001.com/img/2021/20210901092228.png)

**引入 nacos-config 依赖**

首先，在 user-service 服务中，引入 nacos-config 的客户端依赖：

```xml
<!--nacos配置管理依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

**添加 bootstrap.yml**

然后，在 user-service 中添加一个 bootstrap.yml 文件，内容如下：

```yaml
spring:
  application:
    name: userservice # 服务名称
  profiles:
    active: dev #开发环境，这里是dev 
  cloud:
    nacos:
      server-addr: localhost:8848 # Nacos地址
      config:
        file-extension: yaml # 文件后缀名
```

根据 spring.cloud.nacos.server-addr 获取 nacos地址，再根据`${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}`作为文件id，来读取配置。

在这个例子例中，就是去读取 `userservice-dev.yaml`

![](https://cdn.xn2001.com/img/2021/20210901092237.png)

使用代码来验证是否拉取成功

在 user-service 中的 UserController 中添加业务逻辑，读取 pattern.dateformat 配置并使用：

```java
@Value("${pattern.dateformat}")
private String dateformat;

@GetMapping("now")
public String now(){
    //格式化时间
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
}
```

![](https://cdn.xn2001.com/img/2021/20210901092243.png)

启动服务后，访问：http://localhost:8081/user/now

![](https://cdn.xn2001.com/img/2021/20210901092251.png)

## 配置热更新

我们最终的目的，是修改 nacos 中的配置后，微服务中无需重启即可让配置生效，也就是**配置热更新**。

有两种方式：1. 用 `@value` 读取配置时，搭配 `@RefreshScope`；2. 直接用 `@ConfigurationProperties` 读取配置

### @RefreshScope

方式一：在 `@Value` 注入的变量所在类上添加注解 `@RefreshScope`

![](https://cdn.xn2001.com/img/2021/20210901092258.png)

### @ConfigurationProperties

方式二：使用 `@ConfigurationProperties` 注解读取配置文件，就不需要加 `@RefreshScope` 注解。

在 user-service 服务中，添加一个 PatternProperties 类，读取 `patterrn.dateformat` 属性

```java
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    public String dateformat;
}
```

```java
@Autowired
private PatternProperties patternProperties;

@GetMapping("now2")
public String now2(){
    //格式化时间
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.dateformat));
}
```

## 配置共享

其实在服务启动时，nacos 会读取多个配置文件，例如：

- `[spring.application.name]-[spring.profiles.active].yaml`，例如：userservice-dev.yaml

- `[spring.application.name].yaml`，例如：userservice.yaml

这里的 `[spring.application.name].yaml` 不包含环境，**因此可以被多个环境共享**。

**添加一个环境共享配置**

我们在 nacos 中添加一个 userservice.yaml 文件：

![](https://cdn.xn2001.com/img/2021/20210901092323.png)



**在 user-service 中读取共享配置**

在 user-service 服务中，修改 PatternProperties 类，读取新添加的属性：

![](https://cdn.xn2001.com/img/2021/20210901092314.png)

在 user-service 服务中，修改 UserController，添加一个方法：

![](https://cdn.xn2001.com/img/2021/20210901092331.png)



**运行两个 UserApplication，使用不同的profile**

修改 UserApplication2 这个启动项，改变其profile值：

![](https://cdn.xn2001.com/img/2021/20210901092345.png)

![](https://cdn.xn2001.com/img/2021/20210901092338.png)

这样，UserApplication(8081) 使用的 profile 是 dev，UserApplication2(8082) 使用的 profile 是test

启动 UserApplication 和 UserApplication2

访问地址：http://localhost:8081/user/prop，结果：

![](https://cdn.xn2001.com/img/2021/20210901092400.png)

访问地址：http://localhost:8082/user/prop，结果：

![](https://cdn.xn2001.com/img/2021/20210901092419.png)

可以看出来，不管是 dev，还是 test 环境，都读取到了 envSharedValue 这个属性的值。

上面的都是同一个微服务下，**那么不同微服务之间可以环境共享吗？**

通过下面的两种方式来指定：

- extension-configs
- shared-configs 

```yml
spring: 
  cloud:
    nacos:
      config:
        file-extension: yaml # 文件后缀名
        extends-configs: # 多微服务间共享的配置列表
          - dataId: common.yaml # 要共享的配置文件id
```

```yml
spring: 
  cloud:
    nacos:
      config:
        file-extension: yaml # 文件后缀名
        shared-configs: # 多微服务间共享的配置列表
          - dataId: common.yaml # 要共享的配置文件id
```

## 配置优先级

当 nacos、服务本地同时**出现相同属性时**，优先级有高低之分。

![](https://cdn.xn2001.com/img/2021/20210901092501.png)

更细致的配置

![](https://cdn.xn2001.com/img/2021/20210901092520.png)

# Nacos集群

## 架构介绍

![](https://cdn.xn2001.com/img/2021/202108181959897.png)

其中包含 3 个Nacos 节点，然后一个负载均衡器 Nginx 代理 3 个 Nacos，我们计划的 Nacos 集群如下图，MySQL 的主从复制后续再添加。

![](https://cdn.xn2001.com/img/2021/202108182000220.png)

三个 Nacos 节点的地址

| 节点   | ip            | port |
| ------ | ------------- | ---- |
| nacos1 | 192.168.150.1 | 8845 |
| nacos2 | 192.168.150.1 | 8846 |
| nacos3 | 192.168.150.1 | 8847 |

## 初始化数据库

Nacos 默认数据存储在内嵌数据库 Derby 中，不属于生产可用的数据库。官方推荐的最佳实践是使用带有主从的高可用数据库集群，主从模式的高可用数据库。这里我们以单点的数据库为例。

首先新建一个数据库，命名为 nacos，而后导入下面的 SQL

```sql
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text,
  `src_ip` varchar(50) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
```

## 配置Nacos

进入 nacos 的 conf 目录，修改配置文件 cluster.conf.example，重命名为 cluster.conf

![](https://cdn.xn2001.com/img/2021/202108182004564.png)

添加内容

```
127.0.0.1:8845
127.0.0.1.8846
127.0.0.1.8847
```

然后修改 application.properties 文件，添加数据库配置

```properties
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&amp;connectTimeout=1000&amp;socketTimeout=3000&amp;autoReconnect=true&amp;useUnicode=true&amp;useSSL=false&amp;serverTimezone=UTC
db.user.0=root
db.password.0=123456
```

将 nacos 文件夹复制三份，分别命名为：nacos1、nacos2、nacos3

![](https://cdn.xn2001.com/img/2021/202108182004103.png) 

然后分别修改三个文件夹中的 application.properties，

nacos1

```properties
server.port=8845
```

nacos2

```properties
server.port=8846
```

nacos3

```properties
server.port=8847
```

然后分别启动三个 nacos

```
startup.cmd
```

## Nginx反向代理

修改 nginx 文件夹下的 conf/nginx.conf 文件，配置如下

```nginx
upstream nacos-cluster {
    server 127.0.0.1:8845;
	server 127.0.0.1:8846;
	server 127.0.0.1:8847;
}

server {
    listen       80;
    server_name  localhost;

    location /nacos {
        proxy_pass http://nacos-cluster;
    }
}
```

启动 nginx，在浏览器访问：http://localhost/nacos

在代码中的 application.yml 文件配置改为如下：

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:80 # Nacos地址
```

实际部署时，需要给做反向代理的 Nginx 服务器设置一个域名，这样后续如果有服务器迁移 Nacos 的客户端也无需更改配置。Nacos 的各个节点应该部署到多个不同服务器，做好容灾和隔离工作。

# Feign远程调用

> 代码参考
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/06-cloud-feign
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/06-cloud-feign

我们以前利用 RestTemplate 发起远程调用的代码：

![](https://cdn.xn2001.com/img/2021/20210901092616.png)

- 代码可读性差，编程体验不统一
- 参数复杂URL难以维护

Feign 是一个声明式的 http 客户端，官方地址：https://github.com/OpenFeign/feign

其作用就是帮助我们**优雅的实现 http 请求的发送**，解决上面提到的问题。

![](https://cdn.xn2001.com/img/2021/20210901092639.png)

## Feign使用

**引入依赖**

我们在 order-service 引入 feign 依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

**添加注解**

在 order-service 启动类添加注解开启 Feign

![](https://cdn.xn2001.com/img/2021/20210901092704.png)

**请求接口**

在 order-service 中新建一个接口，内容如下

```java
package com.xn2001.order.client;

import com.xn2001.order.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
```

`@FeignClient("userservice")`：其中参数填写的是微服务名

`@GetMapping("/user/{id}")`：其中参数填写的是请求路径

这个客户端主要是基于 SpringMVC 的注解 `@GetMapping` 来声明远程调用的信息

Feign 可以帮助我们发送 http 请求，无需自己使用 RestTemplate 来发送了。

**测试**

```java
@Autowired
private UserClient userClient;

public Order queryOrderAndUserById(Long orderId) {
    // 1.查询订单
    Order order = orderMapper.findById(orderId);
    // TODO: 2021/8/20 使用feign远程调用
    User user = userClient.findById(order.getUserId());
    // 3. 将用户信息封装进订单
    order.setUser(user);
    // 4.返回
    return order;
}
```

## 自定义配置

Feign 可以支持很多的自定义配置，如下表所示：

| 类型                   | 作用             | 说明                                                   |
| ---------------------- | ---------------- | ------------------------------------------------------ |
| **feign.Logger.Level** | 修改日志级别     | 包含四种不同的级别：NONE、BASIC、HEADERS、FULL         |
| feign.codec.Decoder    | 响应结果的解析器 | http远程调用的结果做解析，例如解析json字符串为java对象 |
| feign.codec.Encoder    | 请求参数编码     | 将请求参数编码，便于通过http请求发送                   |
| feign.Contract         | 支持的注解格式   | 默认是SpringMVC的注解                                  |
| feign.Retryer          | 失败重试机制     | 请求失败的重试机制，默认是没有，不过会使用Ribbon的重试 |

一般情况下，默认值就能满足我们使用，如果要自定义时，只需要创建自定义的 @Bean 覆盖默认 Bean 即可。下面以日志为例来演示如何自定义配置。

基于配置文件修改 feign 的日志级别可以针对单个服务：

```yaml
feign:  
  client:
    config: 
      userservice: # 针对某个微服务的配置
        loggerLevel: FULL #  日志级别 
```

**也可以针对所有服务**：

```yaml
feign:  
  client:
    config: 
      default: # 这里用default就是全局配置，如果是写服务名称，则是针对某个微服务的配置
        loggerLevel: FULL #  日志级别 
```

而日志的级别分为四种：

- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据

也可以基于 **Java 代码**来修改日志级别，先声明一个类，然后声明一个 Logger.Level 的对象

```java
public class DefaultFeignConfiguration  {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.BASIC; // 日志级别为BASIC
    }
}
```

如果要**全局生效**，将其放到启动类的 `@EnableFeignClients` 这个注解中：

```java
@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration .class) 
```

如果是**局部生效**，则把它放到对应的 `@FeignClient` 这个注解中：

```java
@FeignClient(value = "userservice", configuration = DefaultFeignConfiguration .class) 
```

## 性能优化

Feign 底层发起 http 请求，依赖于其它的框架。其底层客户端实现有：

- **URLConnection**：默认实现，不支持连接池
- **Apache HttpClient** ：支持连接池
- **OKHttp**：支持连接池

因此提高 Feign 性能的主要手段就是使用**连接池**代替默认的 URLConnection

另外，日志级别应该尽量用 basic/none，可以有效提高性能。

**这里我们用 Apache 的HttpClient来演示连接池。**

在 order-service 的 pom 文件中引入 HttpClient 依赖

```xml
<!--httpClient的依赖 -->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
</dependency>
```

**配置连接池**

在 order-service 的 application.yml 中添加配置

```yaml
feign:
  client:
    config:
      default: # default全局的配置
        loggerLevel: BASIC # 日志级别，BASIC就是基本的请求和响应信息
  httpclient:
    enabled: true # 开启feign对HttpClient的支持
    max-connections: 200 # 最大的连接数
    max-connections-per-route: 50 # 每个路径的最大连接数
```

在 FeignClientFactoryBean 中的 loadBalance 方法中打断点

![](https://cdn.xn2001.com/img/2021/20210901092729.png)

Debug 方式启动 order-service  服务，可以看到这里的 client，底层就是 HttpClient

![](https://cdn.xn2001.com/img/2021/20210901092737.png)

## 最佳实践

### 继承方式

一样的代码可以通过继承来共享：

1）定义一个 API 接口，利用定义方法，并基于 SpringMVC 注解做声明

2）Feign 客户端、Controller 都集成该接口

![](https://cdn.xn2001.com/img/2021/20210901092803.png)

优点

- 简单
- 实现了代码共享

缺点

- 服务提供方、服务消费方紧耦合
- 参数列表中的注解映射并不会继承，因此 Controller 中必须再次声明方法、参数列表、注解

### 抽取方式

将 FeignClient 抽取为独立模块，并且把接口有关的 pojo、默认的 Feign 配置都放到这个模块中，提供给所有消费者使用。

例如：将 UserClient、User、Feign 的默认配置都抽取到一个 feign-api 包中，所有微服务引用该依赖包，即可直接使用。

![](https://cdn.xn2001.com/img/2021/20210901092811.png)

接下来我们就用该方法在代码中实现

**首先创建一个 module，命名为 feign-api**

![](https://cdn.xn2001.com/img/2021/20210901092835.png)

在 feign-api 中然后引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

order-service中 的 UserClient、User 都复制到 feign-api 项目中

![](https://cdn.xn2001.com/img/2021/20210901092848.png)

接下来在 order-service 中使用 feign-api

由于我们已经将 UserClient、User 放在 fegin-api 中共享了 ，所以可以删除 order-service 中的 UserClient、User，然后在 order-service  中引入 feign-api 

```xml
<dependency>
    <groupId>com.xn2001.feign</groupId>
    <artifactId>feign-api</artifactId>
    <version>1.0</version>
</dependency>
```

**修改注解**

当定义的 FeignClient 不在 SpringBootApplication 的扫描包范围下时，这些 FeignClient 就不能使用。

修改 order-service 启动类上的 `@EnableFeignClients` 注解

```java
@EnableFeignClients(basePackages = "com.xn2001.feign.clients")
```

# Gateway网关

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/07-cloud-gateway
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/07-cloud-gateway

Spring Cloud Gateway 是 Spring Cloud 的一个全新项目，该项目是基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等响应式编程和事件流技术开发的网关，它旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式。

Gateway 网关是我们服务的守门神，**所有微服务的统一入口。**

网关的**核心功能特性**：

- 请求路由
- 权限控制
- 限流

![](https://cdn.xn2001.com/img/2021/20210901092857.png)



**权限控制**：网关作为微服务入口，需要校验用户是是否有请求资格，如果没有则进行拦截。

**路由和负载均衡**：一切请求都必须先经过 gateway，但网关不处理业务，而是根据某种规则，把请求转发到某个微服务，这个过程叫做路由。当然路由的目标服务有多个时，还需要做负载均衡。

**限流**：当请求流量过高时，在网关中按照下流的微服务能够接受的速度来放行请求，避免服务压力过大。

在 SpringCloud 中网关的实现包括两种：

- gateway
- zuul

Zuul 是基于 Servlet 实现，属于阻塞式编程。而 Spring Cloud Gateway 则是基于 Spring5 中提供的WebFlux，属于响应式编程的实现，具备更好的性能。

## 入门使用

1. 创建 SpringBoot 工程 gateway，引入网关依赖
2. 编写启动类
3. 编写基础配置和路由规则
4. 启动网关服务进行测试

```xml
<!--网关-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<!--nacos服务发现依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

创建 application.yml 文件，内容如下：

```yaml
server:
  port: 10010 # 网关端口
spring:
  application:
    name: gateway # 服务名称
  cloud:
    nacos:
      server-addr: localhost:8848 # nacos地址
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://userservice # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
```

我们将符合`Path` 规则的一切请求，都代理到 `uri`参数指定的地址。

上面的例子中，我们将 `/user/**` 开头的请求，代理到 `lb://userservice`，其中 lb 是负载均衡(LoadBalance)，根据服务名拉取服务列表，实现负载均衡。

重启网关，访问 [http://localhost:10010/user/1](http://localhost:10010/user/1 ) 时，符合 `/user/**` 规则，请求转发到 uri：[http://userservice/user/1](http://userservice/user/1)

![](https://cdn.xn2001.com/img/2021/202108220125749.png)

多个 predicates 的话，要同时满足规则，下文有例子。

## 流程图

![](https://cdn.xn2001.com/img/2021/202108220127419.png)

路由配置包括：

1. 路由id：路由的唯一标示
2. 路由目标（uri）：路由的目标地址，http代表固定地址，lb代表根据服务名负载均衡
3. 路由断言（predicates）：判断路由的规则
4. 路由过滤器（filters）：对请求或响应做处理

## 断言工厂

我们在配置文件中写的断言规则只是字符串，这些字符串会被 Predicate Factory 读取并处理，转变为路由判断的条件。

例如 `Path=/user/**` 是按照路径匹配，这个规则是由

`org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory` 类来处理的，像这样的断言工厂在 Spring Cloud Gateway 还有十几个

| 名称       | 说明                           | 示例                                                         |
| ---------- | ------------------------------ | ------------------------------------------------------------ |
| After      | 是某个时间点后的请求           | -  After=2037-01-20T17:42:47.789-07:00[America/Denver]       |
| Before     | 是某个时间点之前的请求         | -  Before=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]       |
| Between    | 是某两个时间点之前的请求       | -  Between=2037-01-20T17:42:47.789-07:00[America/Denver],  2037-01-21T17:42:47.789-07:00[America/Denver] |
| Cookie     | 请求必须包含某些cookie         | - Cookie=chocolate, ch.p                                     |
| Header     | 请求必须包含某些header         | - Header=X-Request-Id, \d+                                   |
| Host       | 请求必须是访问某个host（域名） | -  Host=`**.somehost.org`, `**.anotherhost.org`              |
| Method     | 请求方式必须是指定方式         | - Method=GET,POST                                            |
| Path       | 请求路径必须符合指定规则       | - Path=/red/{segment},/blue/**                               |
| Query      | 请求参数必须包含指定参数       | - Query=name, Jack或者-  Query=name                          |
| RemoteAddr | 请求者的ip必须是指定范围       | - RemoteAddr=192.168.1.1/24                                  |
| Weight     | 权重处理                       |                                                              |

> 官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories

一般的，我们只需要掌握 Path，加上官方文档的例子，就可以应对各种工作场景了。

```yaml
predicates:
  - Path=/order/**
  - After=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]
```

像这样的规则，现在是 2021年8月22日01:32:42，很明显 After 条件不满足，可以不会转发，路由不起作用。

## 过滤器工厂

GatewayFilter 是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理。

![](https://cdn.xn2001.com/img/2021/202108220133487.png)

Spring提供了31种不同的路由过滤器工厂。

> 官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories

| **名称**             | **说明**                     |
| -------------------- | ---------------------------- |
| AddRequestHeader     | 给当前请求添加一个请求头     |
| RemoveRequestHeader  | 移除请求中的一个请求头       |
| AddResponseHeader    | 给响应结果中添加一个响应头   |
| RemoveResponseHeader | 从响应结果中移除有一个响应头 |
| RequestRateLimiter   | 限制请求的流量               |

下面我们以 AddRequestHeader 为例：

![](https://cdn.xn2001.com/img/2021/202108220139913.png)

**需求**：给所有进入 userservice 的请求添加一个请求头：`sign=xn2001.com is eternal`

只需要修改 gateway 服务的 application.yml文件，添加路由过滤即可。

```yaml
spring:
  cloud:
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://userservice # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
          filters:
            - AddRequestHeader=sign, xn2001.com is eternal # 添加请求头
```

如何验证，我们修改 userservice 中的一个接口

```java
@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) {
    log.warn(sign);
    return userService.queryById(id);
}
```

重启两个服务，访问：[http://localhost:10010/user/1](http://localhost:10010/user/1)

可以看到控制台打印出了这个请求头

![](https://cdn.xn2001.com/img/2021/202108220145565.png)

当然，Gateway 也是有**全局过滤器**的，如果要**对所有的路由都生效**，则可以将过滤器工厂写到 default-filters 下：

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=sign, xn2001.com is eternal # 添加请求头
```

## 全局过滤器

上面介绍的过滤器工厂，网关提供了 31 种，但每一种过滤器的作用都是固定的。**如果我们希望拦截请求，做自己的业务逻辑则没办法实现**。

全局过滤器的作用也是处理一切进入网关的请求和微服务响应，**与 GatewayFilter 的作用一样**。区别在于 GlobalFilter 的逻辑可以**写代码来自定义规则**；而 GatewayFilter 通过配置定义，处理逻辑是固定的。

**需求**：定义全局过滤器，拦截请求，判断请求的参数是否满足下面条件

- 参数中是否有 authorization

- authorization 参数值是否为 admin

如果同时满足则放行，否则拦截。

```java
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 测试：http://localhost:10010/order/101?authorization=admin
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取第一个 authorization 参数
        String authorization = exchange.getRequest().getQueryParams().getFirst("authorization");
        if ("admin".equals(authorization)){
            // 放行
            return chain.filter(exchange);
        }
        // 设置拦截状态码信息
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置拦截
        return exchange.getResponse().setComplete();
    }

    // 设置过滤器优先级，值越低优先级越高
    // 也可以使用 @Order 注解
    @Override
    public int getOrder() {
        return 0;
    }
}
```

## 过滤器顺序

请求进入网关会碰到三类过滤器：DefaultFilter、当前路由的过滤器、GlobalFilter；

请求路由后，会将三者合并到一个过滤器链（集合）中，排序后依次执行每个过滤器.

![](https://cdn.xn2001.com/img/2021/202108230002747.png)



排序的规则是什么呢？

- 每一个过滤器都必须指定一个 int 类型的 order 值，**order 值越小，优先级越高，执行顺序越靠前**。
- GlobalFilter 通过实现 Ordered 接口，或者使用 @Order 注解来指定 order 值，由我们自己指定。
- 路由过滤器和 defaultFilter 的 order 由 Spring 指定，默认是按照声明顺序从1递增。
- 当过滤器的 order 值一样时，**会按照 defaultFilter > 路由过滤器 > GlobalFilter 的顺序执行。**

## 跨域问题

不了解跨域问题的同学可以百度了解一下；在 Gateway 网关中解决跨域问题还是比较方便的。

```yaml
spring:
  cloud:
    gateway:
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 解决options请求被拦截问题
        corsConfigurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求 allowedOrigins: “*” 允许所有网站
              - "http://localhost:8090"
            allowedMethods: # 允许的跨域ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowCredentials: true # 是否允许携带cookie
            maxAge: 360000 # 这次跨域检测的有效期
```

# RabbitMQ消息中间件

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/08-rabbitmq-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/08-rabbitmq-demo

## 同步异步通讯

**微服务间通讯有同步和异步两种方式**

同步通讯：就像打电话，需要实时响应。

异步通讯：就像发邮件，不需要马上回复。

![](https://cdn.xn2001.com/img/2021/20210904133345.png)

两种方式各有优劣，打电话可以立即得到响应，但是你却不能跟多个人同时通话。发送邮件可以同时与多个人收发邮件，但是往往响应会有延迟。

我们之前学习的 **Feign 调用**就属于**同步方式**，虽然调用可以实时得到结果，但存在下面的问题：

![](https://cdn.xn2001.com/img/2021/20210904133517.png)

**同步调用的优点**：

- 时效性较强，可以立即得到结果

**同步调用的缺点**：

- 耦合度高
- 性能和吞吐能力下降
- 有额外的资源消耗
- 有级联失败问题

异步调用则可以避免上述问题，我们以购买商品为例，用户支付后需要调用订单服务完成订单状态修改，调用物流服务，从仓库分配响应的库存并准备发货。在事件模式中，支付服务是事件发布者（publisher），在支付完成后只需要发布一个支付成功的事件（event），事件中带上订单id。订单服务和物流服务是事件订阅者（Consumer），订阅支付成功的事件，监听到事件后完成自己业务即可。

为了解除事件发布者与订阅者之间的耦合，两者并不是直接通信，而是有一个中间人（Broker）。发布者发布事件到Broker，不关心谁来订阅事件。订阅者从Broker订阅事件，不关心谁发来的消息。

![](https://cdn.xn2001.com/img/2021/20210904144714.png)

Broker 是一个像数据总线一样的东西，所有的服务要接收数据和发送数据都发到这个总线上，这个总线就像协议一样，让服务间的通讯变得标准和可控。

![](https://cdn.xn2001.com/img/2021/20210904145001.png)

**异步调用好处**：

- 吞吐量提升：无需等待订阅者处理完成，响应更快速

- 故障隔离：服务没有直接调用，不存在级联失败问题
- 调用间没有阻塞，不会造成无效的资源占用
- 耦合度极低，每个服务都可以灵活插拔，可替换
- 流量削峰：不管发布事件的流量波动多大，都由 Broker 接收，订阅者可以按照自己的速度去处理事件

**异步调用缺点**：

- 架构复杂了，业务没有明显的流程线，不好管理
- 需要依赖于 Broker 的可靠、安全、性能

## MQ消息队列

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列，也就是事件驱动架构中的 Broker

比较常见的 MQ 实现：

- ActiveMQ
- RabbitMQ
- RocketMQ
- Kafka

几种常见MQ的对比：

|            | **RabbitMQ**            | **ActiveMQ**                      | **RocketMQ** | **Kafka**      |
| ---------- | ----------------------- | --------------------------------- | ------------ | -------------- |
| 公司/社区  | Rabbit                  | Apache                            | 阿里         | Apache         |
| 开发语言   | Erlang                  | Java                              | Java         | Scala&amp;Java |
| 协议支持   | AMQP、XMPP、SMTP、STOMP | OpenWire、STOMP、REST、XMPP、AMQP | 自定义协议   | 自定义协议     |
| 可用性     | 高                      | 一般                              | 高           | 高             |
| 单机吞吐量 | 一般                    | 差                                | 高           | 非常高         |
| 消息延迟   | 微秒级                  | 毫秒级                            | 毫秒级       | 毫秒以内       |
| 消息可靠性 | 高                      | 一般                              | 高           | 一般           |

以 RabbitMQ 为例，我们在 Centos7 虚拟机中使用 Docker 来安装

在线拉取镜像

``` sh
docker pull rabbitmq:3-management
```

执行下面的命令来运行MQ容器

```sh
docker run \
 -e RABBITMQ_DEFAULT_USER=admin \
 -e RABBITMQ_DEFAULT_PASS=123456 \
 --name mq \
 --hostname mq1 \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 rabbitmq:3-management
```

启动成功后访问地址：http://192.168.211.128:15672

**RabbitMQ 中的一些角色**

- publisher：生产者
- consumer：消费者
- exchange：交换机，负责消息路由
- queue：队列，存储消息
- virtualHost：虚拟主机，**隔离不同租户**的 exchange、queue、消息的隔离

**MQ 的基本结构**

![](https://cdn.xn2001.com/img/2021/20210904172912.png)

## 入门案例

RabbitMQ 官方提供了 5 个不同的 Demo 示例，对应了不同的消息模型。

![](https://cdn.xn2001.com/img/2021/20210904173739.png)

Hello World 模型

 ![](https://cdn.xn2001.com/img/2021/20210904200637.png)

官方的 HelloWorld 是基于最基础的消息队列模型来实现的，只包括三个角色：

- publisher：消息发布者，将消息发送到队列queue
- queue：消息队列，负责接受并缓存消息
- consumer：订阅队列，处理队列中的消息

### publisher实现

- 建立连接
- 创建 channel
- 声明队列
- 发送消息
- 关闭连接和 channel

```java
public class PublisherTest {
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.211.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        // 1.2.建立连接
        Connection connection = factory.newConnection();
        // 2.创建通道Channel
        Channel channel = connection.createChannel();
        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);
        // 4.发送消息
        String message = "Hello RabbitMQ！";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：[" + message + "]");
        // 5.关闭通道和连接
        channel.close();
        connection.close();
    }
}
```

### consumer实现

- 建立连接
- 创建 channel
- 声明队列
- 订阅消息

```java
public class ConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.211.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        // 1.2.建立连接
        Connection connection = factory.newConnection();
        // 2.创建通道Channel
        Channel channel = connection.createChannel();
        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);
        // 4.订阅消息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：[" + message + "]");
            }
        });
        System.out.println("等待接收消息中");
    }
}
```

## SpringAMQP

SpringAMQP 是基于 RabbitMQ 封装的一套模板，并且还利用 SpringBoot 对其实现了自动装配，使用起来非常方便。

SpringAMQP 的官方地址：https://spring.io/projects/spring-amqp

![](https://cdn.xn2001.com/img/2021/20210904202046.png)

![](https://cdn.xn2001.com/img/2021/20210904202056.png)

SpringAMQP 提供了三个功能：

- 自动声明队列、交换机及其绑定关系
- 基于注解的监听器模式，异步接收消息
- 封装了 RabbitTemplate 工具，用于发送消息 

```xml
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

###  BasicQueue

首先配置 MQ地址，在 publisher、consumer 服务中的 application.yml 中添加配置

```yml
spring:
  rabbitmq:
    host: 192.168.150.101 # 主机名
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: admin # 用户名
    password: 123456 # 密码
```

在 consumer 服务中添加监听队列

```java
@Component
public class RabbitMQListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("消费者接收到消息：【" + msg + "】");
    }
}
```

在 publisher 服务中添加发送消息的测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "你好啊，乐心湖！";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

### WorkQueue

Work queues，也被称为（Task queues），任务模型。简单来说就是**让多个消费者绑定到一个队列，共同消费队列中的消息**。

![](https://cdn.xn2001.com/img/2021/20210904211238.png)

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。

此时就可以使用 work 模型，多个消费者共同处理消息处理，速度就能大大提高了。

我们循环发送，模拟大量消息堆积现象，在 publisher 服务中的 SpringAmqpTest 类中添加一个测试方法：

```java
/**
 * workQueue
 * 向队列中不停发送消息，模拟消息堆积。
 */
@Test
public void testWorkQueue() throws InterruptedException {
    // 队列名称
    String queueName = "simple.queue";
    // 消息
    String message = "hello, message_";
    for (int i = 0; i < 50; i++) {
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message + i);
        Thread.sleep(20);
    }
}
```

**消息接收**

要模拟多个消费者绑定同一个队列，我们在 consumer 服务的 RabbitMQListener 中添加2个新的方法：

```java
@RabbitListener(queues = "simple.queue")
public void listenWorkQueue1(String msg) throws InterruptedException {
    System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(20);
}

@RabbitListener(queues = "simple.queue")
public void listenWorkQueue2(String msg) throws InterruptedException {
    System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(200);
}
```

启动 ConsumerApplication 后，在执行 publisher 服务中刚刚编写的发送测试方法 testWorkQueue

可以看到消费者1很快完成了自己的25条消息。消费者2却在缓慢的处理自己的25条消息。

也就是说消息是**平均分配给每个消费者**，并没有考虑到消费者的处理能力。这是因为 RabbitMQ 默认有一个消息预取机制，显然这不是我们想要的结果，我们需要的是能者多劳嘛，所以去限制每次只能取一条消息，可以解决这个问题。

在 spring 中有一个简单的配置，设置 prefetch 属性，我们修改 consumer 服务的 application.yml 文件，添加配置

```yml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1 # 每次只能获取一条消息，处理完成才能获取下一个消息
```

Work 模型的使用：

- 多个消费者绑定到一个队列，同一条消息只会被一个消费者处理
- 通过设置 prefetch 来控制消费者预取的消息数量

### 发布/订阅

![](https://cdn.xn2001.com/img/2021/20210904213455.png)



图中可以看到，在订阅模型中，多了一个 exchange 角色，而且过程略有变化

- Publisher：生产者，也就是要发送消息的程序，但是不再发送到队列中，**而是发给 exchange（交换机）**
- Consumer：消费者，与以前一样，订阅队列，没有变化
- Queue：消息队列也与以前一样，接收消息、缓存消息
- Exchange：交换机，一方面，接收生产者发送的消息；另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于 Exchange 的类型。Exchange 有以下3种类型：
  - Fanout：广播，将消息交给所有绑定到交换机的队列
  - Direct：定向，把消息交给符合指定 routing key 的队列
  - Topic：通配符，把消息交给符合 routing pattern（路由模式） 的队列

**Exchange（交换机）只负责转发消息，不具备存储消息的能力**，因此如果没有任何队列与 Exchange 绑定，或者没有符合路由规则的队列，那么消息会丢失！

### Fanout

Fanout，英文翻译是扇出，在 MQ 中我们也可以称为广播。

![](https://cdn.xn2001.com/img/2021/20210912160350.png)

在广播模式下，消息发送流程是这样的：

- 可以有多个队列
- 每个队列都要绑定到 Exchange（交换机）
- 生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定
- 交换机把消息发送给绑定过的所有队列
- 订阅队列的消费者都能拿到消息

接下里我们用 SpringAMQP 来简单实现 FanoutExchange

1. 在 consumer 服务中，利用代码声明队列、交换机，并将两者绑定
2. 在 consumer 服务中，编写两个消费者方法，分别监听 fanout.queue1 和 fanout.queue2

3. 在 publisher 中编写测试方法，向 fanout发送消息

**声明队列和交换机**

Spring 提供了一个接口 Exchange，来表示所有不同类型的交换机。

![](https://cdn.xn2001.com/img/2021/20210904213809.png)

在 consumer 中创建一个类，声明队列、交换机、**绑定对象 Binding**

```java
@Configuration
public class FanoutConfig {

    /**
     * 声明交换机
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("xn2001.fanout");
    }

    /**
     * 声明队列
     * @return Queue
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue1");
    }
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1(FanoutExchange fanoutExchange,Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    @Bean
    public Binding bindingQueue2(FanoutExchange fanoutExchange,Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

}
```

![](https://cdn.xn2001.com/img/2021/20210912181932.png)

通过这样 `@Bean` 的方式来申明确实比较麻烦，其实我们也是可以直接通过 `@RabbitListener` 注解来完成的，代码如下：

在 consumer 服务的 SpringRabbitListener 中添加三个方法，作为消费者

```java
@RabbitListener(queues = "fanout.queue1")
public void listenFanoutQueue1(String msg) throws InterruptedException {
    System.out.println("接收到fanout.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(queues = "fanout.queue2")
public void listenFanoutQueue2(String msg) throws InterruptedException {
    System.err.println("接收到fanout.queue2的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "fanout.queue3"),
    exchange = @Exchange(value = "xn2001.fanout",type = "fanout")
))
public void listenFanoutQueue3(String msg) {
    System.out.println("接收到fanout.queue3的消息：【" + msg + "】" + LocalTime.now());
}
```

在 publisher 服务的 SpringAmqpTest 类中添加测试方法

```java
/**
 * fanout
 * 向交换机发送消息
 */
@Test
public void testFanoutExchange() {
    // 交换机名称
    String exchangeName = "xn2001.fanout";
    // 消息
    String message = "hello, everybody!";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
}
```

运行该方法，可以发现 fanout.queue1、fanout.queue2 都收到了交换机的消息。

![](https://cdn.xn2001.com/img/2021/20210912182458.png)

总结一下：

交换机的作用是什么？

- 接收 publisher 发送的消息
- 将消息按照规则路由到与之绑定的队列
- 不能缓存消息，路由失败，消息丢失
- FanoutExchange 会将所有消息路由到每个绑定的队列

### Direct

在 Fanout 模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到 DirectExchange

![](https://cdn.xn2001.com/img/2021/20210912182822.png)

 在 Direct 模型下：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
- 消息的发送方向 Exchange发送消息时，也必须指定消息的 `RoutingKey`。
- Exchange 不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey` 与消息的 `Routing key`完全一致，才会接收到消息

在 consumer 的 SpringRabbitListener 中添加两个消费者，同时基于注解来声明队列和交换机

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "direct.queue1"),
    exchange = @Exchange(value = "xn2001.direct"),
    key = {"a","b"}
))
public void listenDirectQueue1(String msg){
    System.out.println("接收到direct.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "direct.queue2"),
    exchange = @Exchange(value = "xn2001.direct"),
    key = {"a","c"}
))
public void listenDirectQueue2(String msg){
    System.out.println("接收到direct.queue2的消息：【" + msg + "】" + LocalTime.now());
}
```

在 publisher 服务的 SpringAmqpTest 类中添加测试方法

```java
/**
 * direct
 * 向交换机发送消息
 */
@Test
public void testDirectExchangeToA() {
    // 交换机名称
    String exchangeName = "xn2001.direct";
    // 消息
    String message = "hello, i am direct to a!";
    rabbitTemplate.convertAndSend(exchangeName, "a", message);
}

/**
 * direct
 * 向交换机发送消息
 */
@Test
public void testDirectExchangeToB() {
    // 交换机名称
    String exchangeName = "xn2001.direct";
    // 消息
    String message = "hello, i am direct to b!";
    rabbitTemplate.convertAndSend(exchangeName, "b", message);
}
```

### Topic

`Topic ` 与 `Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic `类型可以让队列在绑定`Routing key` 的时候使用通配符！

通配符规则：

`#`：匹配一个或多个词 

`*`：只能匹配一个词

例如：

`item.#`：能够匹配`item.spu.insert` 或者 `item.spu`

`item.*`：只能匹配`item.spu`

 ![](https://cdn.xn2001.com/img/2021/20210912194016.png)

- Queue1：绑定的是 `china.#` ，因此凡是以 `china.` 开头的 `routing key` 都会被匹配到。包括 china.news 和 china.weather
- Queue2：绑定的是 `#.news` ，因此凡是以 `.news ` 结尾的  `routing key` 都会被匹配。包括 china.news 和 japan.news 

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "topic.queue1"),
    exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
    key = {"china.#"}
))
public void listenTopicQueue1(String msg){
    System.out.println("接收到topic.queue1的消息：【" + msg + "】" + LocalTime.now());
}

@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "topic.queue2"),
    exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
    key = {"china.*"}
))
public void listenTopicQueue2(String msg){
    System.out.println("接收到topic.queue2的消息：【" + msg + "】" + LocalTime.now());
}
```

```java
/**
 * topic
 * 向交换机发送消息
 */
@Test
public void testTopicExchange() {
    // 交换机名称
    String exchangeName = "xn2001.topic";
    // 消息
    String message1 = "hello, i am topic form china.news";
    String message2 = "hello, i am topic form china.news.2";
    rabbitTemplate.convertAndSend(exchangeName, "china.news", message1);
    rabbitTemplate.convertAndSend(exchangeName, "china.news.2", message2);
}
```



![](https://cdn.xn2001.com/img/2021/20210912200012.png)

### 消息转换器

Spring 会把你发送的消息序列化为字节发送给 MQ，接收消息的时候，还会把字节反序列化为 Java 对象。

**默认情况下 Spring 采用的序列化方式是 JDK 序列化。**

我们可以去试一下效果

```java
@RabbitListener(queuesToDeclare = @Queue(value = "object.queue"))
public void listenObjectQueue(Map<String,Object> msg) throws InterruptedException {
    System.err.println("object接收到消息：【" + msg + "】" + LocalTime.now());
    Thread.sleep(200);
}
```

```java
@Test
public void testSendMap()  {
    // 准备消息
    Map<String,Object> msg = new HashMap<>();
    msg.put("name", "Jack");
    msg.put("age", 21);
    // 发送消息
    rabbitTemplate.convertAndSend("object.queue", msg);
}
```

![](https://cdn.xn2001.com/img/2021/20210912204117.png)

众所周知，JDK序列化存在下列问题：

- 数据体积过大
- 有安全漏洞
- 可读性差

我们推荐可以使用 JSON 来序列化

在 publisher 和 consumer 两个服务中都引入依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.10</version>
</dependency>
```

配置消息转换器。

在各自的启动类中添加一个 Bean 即可

```java
@Bean
public MessageConverter jsonMessageConverter(){
    return new Jackson2JsonMessageConverter();
}
```

![](https://cdn.xn2001.com/img/2021/20210912204512.png)



# ELasticsearch搜索引擎

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/09-elasticsearch-hotel-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/09-elasticsearch-hotel-demo

ELasticsearch 是一款非常强大的开源搜索引擎，具备非常多强大功能，可以帮助我们从海量数据中快速找到需要的内容，可以用来实现搜索、日志统计、分析、系统监控等功能。

![](https://cdn.xn2001.com/img/2021/20210918202359.png)

## 倒排索引

**首先，倒排索引的概念是基于 MySQL 这样的正向索引而言的。**

那么我们先讲何为正向索引。例如给下表（tb_goods）中的 id 创建索引

![](https://cdn.xn2001.com/img/2021/20210918202527.png)

如果是根据 id 查询，那么直接走索引，查询速度非常快。

但如果是基于 title 做模糊查询，只能是逐行扫描数据，流程如下：

1. 用户搜索数据，条件是 title 符合 `"%手机%"`
2. 逐行获取数据，比如 id 为 1 的数据
3. 判断数据中的 title 是否符合用户搜索条件
4. 如果符合则放入结果集，不符合则丢弃。然后回到步骤1

逐行扫描，也就是全表扫描，随着数据量增加，其查询效率也会越来越低。当数据量达到数百万时，就是。。。

而倒排索引中有两个非常重要的概念：

- 文档（`Document`）：用来搜索的数据，其中的每一条数据就是一个文档。例如一个网页、一个商品信息
- 词条（`Term`）：对文档数据或用户搜索数据，利用某种算法分词，得到的具备含义的词语就是词条。例如：我是中国人，就可以分为：我、是、中国人、中国、国人这样的几个词条

**创建倒排索引**是对正向索引的一种特殊处理，流程如下：

- 将每一个文档的数据利用算法分词，得到一个个词条
- 创建表，每行数据包括词条、词条所在文档 id、位置等信息
- 因为词条唯一性，可以给词条创建索引，例如 hash 表结构索引

如图：

![](https://cdn.xn2001.com/img/2021/20210918203514.png)



**倒排索引的搜索流程**如下（以搜索"华为手机"为例）

1. 用户输入条件`"华为手机"`进行搜索
2. 对用户输入内容**分词**，得到词条：`华为`、`手机`
3. 拿着词条在倒排索引中查找，可以得到包含词条的文档 id 有 1、2、3
4. 拿着文档 id 到正向索引中查找具体文档

![](https://cdn.xn2001.com/img/2021/20210918203815.png)

**虽然要先查询倒排索引，再查询正向索引，但是词条和文档id 都建立了索引，查询速度非常快！无需全表扫描。**

为什么一个叫做正向索引，一个叫做倒排索引呢？

**正向索引**是最传统的，根据 id 索引的方式。但根据词条查询时，必须先逐条获取每个文档，然后判断文档中是否包含所需要的词条，是**根据文档找词条的过程**

**倒排索引**则相反，是先找到用户要搜索的词条，根据得到的文档 id 获取该文档。是**根据词条找文档的过程**

## 文档和字段

elasticsearch 是面向**文档（Document）**存储的，可以是数据库中的一条商品数据，一个订单信息。文档数据会被序列化为 json 格式后存储在 elasticsearch

![](https://cdn.xn2001.com/img/2021/20210918212707.png)

而 JSON 文档中往往包含很多的**字段（Field）**，类似于数据库中的列。

## 索引和映射

**索引（Index）**，就是相同类型的文档的集合。

例如：

- 所有用户文档，就可以组织在一起，称为用户的索引；
- 所有商品的文档，可以组织在一起，称为商品的索引；
- 所有订单的文档，可以组织在一起，称为订单的索引；

![](https://cdn.xn2001.com/img/2021/20210918213357.png)



因此，我们可以把索引当做是数据库中的表。

数据库的表会有约束信息，用来定义表的结构、字段的名称、类型等信息。因此，索引库中就有**映射（mapping）**，是索引中文档的字段约束信息，类似表的结构约束。

**mysql 与 elasticsearch**

| **MySQL** | **Elasticsearch** | **说明**                                                     |
| --------- | ----------------- | ------------------------------------------------------------ |
| Table     | Index             | 索引(index)，就是文档的集合，类似数据库的表(table)           |
| Row       | Document          | 文档（Document），就是一条条的数据，类似数据库中的行（Row），文档都是JSON格式 |
| Column    | Field             | 字段（Field），就是JSON文档中的字段，类似数据库中的列（Column） |
| Schema    | Mapping           | Mapping（映射）是索引中文档的约束，例如字段类型约束。类似数据库的表结构（Schema） |
| SQL       | DSL               | DSL是elasticsearch提供的JSON风格的请求语句，用来操作elasticsearch，实现CRUD |

- Mysql：擅长事务类型操作，可以确保数据的安全和一致性

- Elasticsearch：擅长海量数据的搜索、分析、计算

因此在企业中，往往是两者结合使用：

- 对安全性要求较高的写操作，使用 MySQL 实现
- 对查询性能要求较高的搜索需求，使用 ELasticsearch 实现
- 两者再基于某种方式，实现数据的同步，保证一致性

![](https://cdn.xn2001.com/img/2021/20210918213631.png)



## 安装Elasticsearch

因为我们还需要部署 kibana 容器，需要让 es 和 kibana 容器互联。这里先创建一个网络：

```sh
docker network create es-net 
```

安装

```sh
docker run -d \
--name es \
-e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
-e "discovery.type=single-node" \
-v es-data:/usr/share/elasticsearch/data \
-v es-plugins:/usr/share/elasticsearch/plugins \
--privileged \
--network es-net \
-p 9200:9200 \
-p 9300:9300 \
elasticsearch:7.12.1
```

命令解释：

- `-e "cluster.name=es-docker-cluster"`：设置集群名称
- `-e "http.host=0.0.0.0"`：监听的地址，可以外网访问
- `-e "ES_JAVA_OPTS=-Xms512m -Xmx512m"`：内存大小
- `-e "discovery.type=single-node"`：非集群模式
- `-v es-data:/usr/share/elasticsearch/data`：挂载逻辑卷，绑定es的数据目录
- `-v es-logs:/usr/share/elasticsearch/logs`：挂载逻辑卷，绑定es的日志目录
- `-v es-plugins:/usr/share/elasticsearch/plugins`：挂载逻辑卷，绑定es的插件目录
- `--privileged`：授予逻辑卷访问权
- `--network es-net` ：加入一个名为 es-net 的网络中
- `-p 9200:9200`：端口映射配置

访问地址：http://192.168.211.128:9200 即可看到 elasticsearch 的响应结果

![](https://cdn.xn2001.com/img/2021/20210918214620.png)

## 安装kibana

kibana 可以给我们提供一个 elasticsearch 的可视化界面，便于我们学习命令。

```sh
docker run -d \
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601  \
kibana:7.12.1
```

- `--network es-net` ：加入一个名为 es-net 的网络中，与 elasticsearch 在同一个网络中
- `-e ELASTICSEARCH_HOSTS=http://es:9200"`：设置 elasticsearch 的地址，因为 kibana 已经与 elasticsearch 在一个网络，因此可以用容器名直接访问 elasticsearch
- `-p 5601:5601`：端口映射配置

访问地址：http://192.168.211.128:5601，即可看到结果

![](https://cdn.xn2001.com/img/2021/20210918214722.png)

控制面板：http://192.168.211.128:5601/app/dev_tools#/console

## 安装IK分词器

由于国内访问 GitHub 较慢，我们选择离线模式安装。

安装插件需要知道 elasticsearch 的 plugins 目录位置，而我们用了数据卷挂载，因此需要查看 elasticsearch 的数据卷目录，通过下面命令查看

```sh
docker volume inspect es-plugins
```

显示结果：

```json
[
    {
        "CreatedAt": "2022-05-06T10:06:34+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/es-plugins/_data",
        "Name": "es-plugins",
        "Options": null,
        "Scope": "local"
    }
]
```

说明 plugins 目录被挂载到了 `/var/lib/docker/volumes/es-plugins/_data ` 这个目录中

![](https://cdn.xn2001.com/img/2021/20210918215615.png)

重启容器

```shell
# 4、重启容器
docker restart es

# 查看es日志
docker logs -f es
```

IK分词器包含两种模式：

* `ik_smart`：智能切分，粗粒度
* `ik_max_word`：最细切分，细粒度

我们在上面的 Kibana 控制台测试

```json
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "钟老师你好菜啊"
}
```

## 扩展词词典

在上面的IK分词器我们可以随着热点词来扩展，可以自己添加，比如 ”钟老师应该是一个热点词“，另外你也可以配置一些停用掉的敏感词，让其不进行分词。

打开IK分词器 config 目录是 `IKAnalyzer.cfg.xml`，添加一个文件名，我们以 `ext.dic` 文件名为例。

![](https://cdn.xn2001.com/img/2021/20210918221159.png)

我们去创建 `ext.dic` ，在其中添加热点词就好了，一个词一行。

![](https://cdn.xn2001.com/img/2021/20210918220910.png)

重启 elasticsearch

```sh
docker restart es
```

重新测试

```json
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "钟老师你好菜啊"
}
```

![](https://cdn.xn2001.com/img/2021/20210918221424.png)

## 索引库操作

### Mapping属性映射

索引库就类似数据库表，**mapping 映射就类似表的结构**

我们要向 es 中存储数据，必须先创建“库”和“表”

mapping 是对索引库中文档的约束，常见的 mapping 属性包括：

- type：字段数据类型，常见的简单类型有：
  - 字符串：text（可分词的文本）、keyword（精确值，例如：品牌、国家、ip地址）
  - 数值：long、integer、short、byte、double、float、
  - 布尔：boolean
  - 日期：date
  - 对象：object
- **index：是否创建索引，默认为 true**
- analyzer：使用哪种分词器
- properties：该字段的子字段

我们以需要存储下面的 JSON 为例来讲解

```json
{
    "age": 21,
    "weight": 52.1,
    "isMarried": false,
    "info": "钟老师真菜",
    "email": "jialna@qq.com",
    "score": [99.1, 99.5, 98.9],
    "name": {
        "firstName": "湖",
        "lastName": "心"
    }
}
```

首先对应的每个字段映射（mapping）情况如下：

- age：类型为 integer；参与搜索，index 为 true；无需分词器
- weight：类型为 float；参与搜索，index 为 true；无需分词器
- isMarried：类型为boolean；参与搜索，index 为 true；无需分词器
- info：类型为字符串，需要分词，因此是 text；参与搜索，index为true；分词器可以用 ik_smart
- email：类型为字符串，但是不需要分词，因此是 keyword；不参与搜索，index 为 false；无需分词器
- score：虽然是数组，**但是我们只看元素的类型**，类型为 float；参与搜索，index 为 true；无需分词器
- name：类型为 object，需要定义多个子属性
  - name.firstName：类型为字符串，不需要分词，keyword；参与搜索，index 为 true；无需分词器
  - name.lastName：类型为字符串，不需要分词，keyword；参与搜索，index 为 true；无需分词器

### 创建索引库和映射

上面我们了解了 Mapping 属性映射，接下来我们就去看看如何创建索引库及映射。

```json
PUT /索引库名称
{
  "mappings": {
    "properties": {
      "字段名":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "字段名2":{
        "type": "keyword",
        "index": "false"
      },
      "字段名3":{
        "properties": {
          "子字段": {
            "type": "keyword"
          }
        }
      }
      // ...略
    }
  }
}
```

```json
PUT /xn2001
{
  "mappings": {
    "properties": {
      "info":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email":{
        "type": "keyword",
        "index": "false"
      },
      "name":{
        "properties": {
          "firstName": {
            "type": "keyword"
          },
          "lastName": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
```

![](https://cdn.xn2001.com/img/2021/20210918224647.png)

我们用真实的数据库表来创建一个索引库

![](https://cdn.xn2001.com/img/2021/20210919164626.png)

- 字段名、字段数据类型，可以参考数据表结构的名称和类型
- 是否参与搜索要分析业务来判断，例如图片地址，就无需参与搜索
- 是否分词呢要看内容，内容如果是一个整体就无需分词
- 分词器，我们可以统一使用 `ik_max_word`

```json
PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}
```

特殊字段说明：

- location：地理坐标，里面包含精度、纬度
- all：一个组合字段，其目的是将多字段的值利用 `copy_to` 合并，提供给用户搜索，这样一来就只需要搜索一个字段就可以得到结果，性能更好。

> ES中支持两种地理坐标数据类型：
>
> - geo_point：由纬度（latitude）和经度（longitude）确定的一个点。例如："32.8752345, 120.2981576"
> - geo_shape：有多个 geo_point 组成的复杂几何图形。例如一条直线，"LINESTRING (-77.03653 38.897676, -77.009051 38.889939)"

### 修改索引库

倒排索引结构虽然不复杂，但是一旦数据结构改变（比如改变了分词器），就需要重新创建倒排索引，这简直是灾难。因此索引库**一旦创建，无法修改 mapping**

虽然无法修改 mapping 中已有的字段，但是却允许添加新的字段到 mapping 中，不会对倒排索引产生影响。

```json
PUT /索引库名/_mapping
{
  "properties": {
    "新字段名":{
      "type": "integer"
    }
  }
}
```

### 删除索引库

```json
DELETE /索引库名
```

### 查询索引库

```json
GET /数据库名
```

## DSL文档操作

### 新增文档

```json
POST /索引库名/_doc/文档id
{
    "字段1": "值1",
    "字段2": "值2",
    "字段3": {
        "子属性1": "值3",
        "子属性2": "值4"
    }
    // ...
}
```

```json
POST /xn2001/_doc/1
{
    "info": "我不会Java",
    "email": "jialna@qq.com",
    "name": {
        "firstName": "钟",
        "lastName": "弟弟"
    }
}
```

### 修改文档

修改文档有两种方式：

- 全量修改：直接覆盖原来的文档
- 增量修改：修改文档中的部分字段

**全量修改**是覆盖原来的文档，其本质是：

- 根据指定的 id 删除文档
- 新增一个相同 id 的文档

**注意**：如果根据 id 删除时，id 不存在，第二步的新增也会执行，也就是变成了新增操作

```json
PUT /{索引库名}/_doc/id
{
    "字段1": "值1",
    "字段2": "值2",
    // ... 略
}
```

```json
PUT /xn2001/_doc/1
{
    "info": "我也不会敲代码",
    "email": "3300123589@qq.com",
    "name": {
        "firstName": "弟弟",
        "lastName": "钟"
    }
}
```

**增量修改**是只修改指定 id 匹配的文档中的部分字段

```json
POST /{索引库名}/_update/文档id
{
    "doc": {
         "字段名": "新的值",
    }
}
```

```json
POST /heima/_update/1
{
  "doc": {
    "email": "update@qq.com"
  }
}
```

### 查询文档

```json
GET /{索引库名称}/_doc/{id}
```

### 删除文档

```json
DELETE /{索引库名}/_doc/{id}
```

## RestClient文档操作

ES 官方提供了各种不同语言的客户端，用来操作 ES。这些客户端的本质就是组装 DSL 语句，通过 http 请求发送给 ES。官方文档地址：https://www.elastic.co/guide/en/elasticsearch/client/index.html

其中的Java Rest Client又包括两种：

- Java Low Level Rest Client
- Java High Level Rest Client

![](https://cdn.xn2001.com/img/2021/20210919234405.png)

我们下面学习的是 **Java HighLevel Rest Client 客户端 API**

### 初始化RestClient

在 elasticsearch 提供的 API 中，elasticsearch 一切交互都封装在一个名为 RestHighLevelClient 的类中，必须先完成这个对象的初始化，建立与 elasticsearch 的连接。

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
</dependency>
```

SpringBoot 默认的 ES 版本是 7.6.2，我们需要覆盖默认的ES版本

```xml
<properties>
    <java.version>1.8</java.version>
    <elasticsearch.version>7.12.1</elasticsearch.version>
</properties>
```

初始化 RestHighLevelClient，初始化的代码如下：

```java
RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
        HttpHost.create("http://192.168.211.128:9200")
));
```

我们创建一个测试类 HotelIndexTest，然后将初始化的代码编写在 `@BeforeEach` 方法

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
public class HotelIndexTest {

    private RestHighLevelClient restHighLevelClient;

    @Test
    void testInit(){
        System.out.println(this.restHighLevelClient);
    }

    @BeforeEach
    void init(){
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### 创建索引库

```java
@Test
void createHotelIndex() throws IOException {
    //指定索引库名
    CreateIndexRequest hotel = new CreateIndexRequest("hotel");
    //写入JSON数据，这里是Mapping映射
    hotel.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
    //创建索引库
    restHighLevelClient.indices().create(hotel, RequestOptions.DEFAULT);
}
```

```java
public class HotelConstants {
    public static String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
```

### 删除索引库

```java
@Test
void deleteHotelIndex() throws IOException {
    DeleteIndexRequest hotel = new DeleteIndexRequest("hotel");
    restHighLevelClient.indices().delete(hotel,RequestOptions.DEFAULT);
}
```

### 判断索引库

```java
@Test
void existHotelIndex() throws IOException {
    GetIndexRequest hotel = new GetIndexRequest("hotel");
    boolean exists = restHighLevelClient.indices().exists(hotel, RequestOptions.DEFAULT);
    System.out.println(exists);
}
```

### 新增文档

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
@SpringBootTest
public class HotelDocumentTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    void testInit(){
        System.out.println(this.restHighLevelClient);
    }

    @Test
    void createHotelIndex() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 1.准备Request对象
        IndexRequest hotelIndex = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        // 2.准备Json文档
        hotelIndex.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 3.发送请求
        restHighLevelClient.index(hotelIndex, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void init(){
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### 查询文档

```java
@Test
void testGetDocumentById() throws IOException {
    // 1.准备Request
    GetRequest hotel = new GetRequest("hotel", "61083");
    // 2.发送请求，得到响应
    GetResponse hotelResponse = restHighLevelClient.get(hotel, RequestOptions.DEFAULT);
    // 3.解析响应结果
    String hotelDocSourceAsString = hotelResponse.getSourceAsString();
    // 4.json转实体类
    HotelDoc hotelDoc = JSON.parseObject(hotelDocSourceAsString, HotelDoc.class);
    System.out.println(hotelDoc);
}
```

### 删除文档

```java
@Test
void testDeleteDocumentById() throws IOException {
    DeleteRequest hotel = new DeleteRequest("hotel", "61083");
    restHighLevelClient.delete(hotel,RequestOptions.DEFAULT);
}
```

### 修改文档

前面我们说过，修改文档有两种方式：

- 全量修改：直接覆盖原来的文档
- 增量修改：修改文档中的部分字段

在 RestClient 的 API 中，全量修改与新增的 API 完全一致，判断依据是 ID

- 如果新增时，ID已经存在，则修改
- 如果新增时，ID不存在，则新增

所以全量修改写法与新增文档一样，下面我们主要是介绍增量修改。

```java
@Test
void testUpdateDocument() throws IOException {
    // 1.准备Request
    UpdateRequest request = new UpdateRequest("hotel", "61083");
    // 2.准备请求参数
    request.doc(
        "price", "952",
        "starName", "四钻"
    );
    // 3.发送请求
    restHighLevelClient.update(request, RequestOptions.DEFAULT);
}
```

### 批量导入文档

案例需求：利用 `BulkRequest` 批量将数据库数据导入到索引库中。

- 利用 mybatis-plus 查询酒店数据

- 将查询到的酒店数据（Hotel）转换为文档类型数据（HotelDoc）

- 利用 JavaRestClient 中的 BulkRequest 批处理，实现批量新增文档

批量处理 BulkRequest，其本质就是将多个普通的 CRUD 请求组合在一起发送。

因此Bulk中添加了多个IndexRequest，就是批量新增功能了。示例：

![](https://cdn.xn2001.com/img/2021/20210919234350.png)

利用这一点，我们可以写出自己需要的代码，如下

```java
@Test
void testBulk() throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    List<Hotel> hotelList = hotelService.list();
    hotelList.forEach(item -> {
        HotelDoc hotelDoc = new HotelDoc(item);
        bulkRequest.add(new IndexRequest("hotel")
                .id(hotelDoc.getId().toString())
                .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
    });
    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
}
```

---

总之，在 Java 代码中，client 针对操作索引库还是文档，基本都是一样的代码

restHighLevelClient.indices().xxx，代表操作索引库

restHighLevelClient.xxx，代表操作文档

而其中所需要的参数，我们直接通过 **ctrl+p** 这样的快捷键去查看就可以，不需要单独记住。

## DSL文档查询

Elasticsearch 提供了基于 JSON 的 DSL([Domain Specific Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html))来定义查询。常见的查询类型包括：

**查询所有**：查询出所有数据，一般测试用。例如：match_all

**全文检索（full text）查询**：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如：

- match_query
- multi_match_query

**精确查询**：根据精确词条值查找数据，一般是查找 keyword、数值、日期、boolean 等类型字段。例如：

- ids
- range
- term

**地理（geo）查询**：根据经纬度查询。例如：

- geo_distance
- geo_bounding_box

**复合（compound）查询**：复合查询可以将上述各种查询条件组合起来，合并查询条件。例如：

- bool
- function_score

---

```json
// 查询所有
GET /indexName/_search
{
  "query": {
    "match_all": {
    }
  }
}
```

### 全文检索

使用场景：全文检索查询的基本流程如下：

- 对用户搜索的内容做分词，得到词条
- 根据词条去倒排索引库中匹配，得到文档id
- 根据文档id找到文档，返回给用户

比较常用的场景包括：

- 商城的输入框搜索
- 百度输入框搜索

例如京东：

![](C:\Users\54656\Desktop\SpringCloud\实用篇\day01-SpringCloud01\资料\微服务技术栈.assets\image-20210721165326938.png)

因为是拿着词条去匹配，因此参与搜索的字段也必须是可分词的text类型的字段。

常见的全文检索查询包括：

- match 查询：单字段查询
- multi_match 查询：多字段查询，任意一个字段符合条件就算符合查询条件

match 查询语法如下：

```json
GET /indexName/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT"
    }
  }
}
```

mulit_match 查询语法如下：

```json
GET /indexName/_search
{
  "query": {
    "multi_match": {
      "query": "TEXT",
      "fields": ["FIELD1", " FIELD12"]
    }
  }
}
```

因为我们将 brand、name、business 值都利用 **copy_to** 复制到了 **all** 字段中，你根据三个字段搜索，和根据 all字段搜索效果是一样的。

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "7天酒店"
    }
  }
}
```

```json
GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "7天酒店",
      "fields": ["brand","name"]
    }
  }
}
```

**搜索字段越多，对查询性能影响越大，因此建议采用 copy_to 将多个字段合并为一个，然后使用单字段查询的方式。**

### 精准查询

精确查询一般是查找 keyword、数值、日期、boolean 等类型字段。所以**不会**对搜索条件分词。

- term：根据词条精确值查询
- range：根据值的范围查询

#### term查询

因为精确查询的字段搜是不分词的字段，因此查询的条件也必须是**不分词**的词条。查询时，用户输入的内容跟自动值完全匹配时才认为符合条件。如果用户输入的内容过多，反而搜索不到数据。

语法说明：

```json
// term查询
GET /indexName/_search
{
  "query": {
    "term": {
      "FIELD": {
        "value": "VALUE"
      }
    }
  }
}
```

示例：

```json
GET /hotel/_search
{
  "query": {
    "term": {
      "brand": {
        "value": "7天酒店"
      }
    }
  }
}
```

#### range查询

范围查询，一般应用在对数值类型做范围过滤的时候。比如做价格范围过滤。

基本语法：

```json
// range查询
GET /indexName/_search
{
  "query": {
    "range": {
      "FIELD": {
        "gte": 10, // 这里的gte代表大于等于，gt则代表大于
        "lte": 20 // lte代表小于等于，lt则代表小于
      }
    }
  }
}
```

示例：

![](https://cdn.xn2001.com/img/2021/20210921182858.png)

精确查询常见的有哪些？

- term 查询：根据词条精确匹配，一般搜索 keyword 类型、数值类型、布尔类型、日期类型字段
- range 查询：根据数值范围查询，可以是数值、日期的范围

### 地理坐标查询

地理坐标查询，其实就是根据经纬度查询，官方文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-queries.html

常见的使用场景包括：

- 携程：搜索我附近的酒店
- 滴滴：搜索我附近的出租车
- 微信：搜索我附近的人

附近的酒店：

![](https://cdn.xn2001.com/img/2021/20210921183030.png) 

附近的车：

![](https://cdn.xn2001.com/img/2021/20210921183033.png) 

> 矩形范围查询

矩形范围查询，也就是 `geo_bounding_box` 查询，查询坐标落在某个矩形范围的所有文档

![](https://cdn.xn2001.com/img/2021/20210921183124.gif)

查询时，需要指定矩形的**左上**、**右下**两个点的坐标，然后画出一个矩形，落在该矩形内的都是符合条件的点。

```json
// geo_bounding_box查询
GET /indexName/_search
{
  "query": {
    "geo_bounding_box": {
      "FIELD": {
        "top_left": { // 左上点
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": { // 右下点
          "lat": 30.9,
          "lon": 121.7
        }
      }
    }
  }
}
```

> 附近查询

附近查询，也叫做距离查询（geo_distance）：查询到指定中心点小于某个距离值的所有文档

在地图上找一个点作为圆心，以指定距离为半径，画一个圆，落在圆内的坐标都算符合条件：

![](https://cdn.xn2001.com/img/2021/20210921183215.gif)

```json
// geo_distance 查询
GET /indexName/_search
{
  "query": {
    "geo_distance": {
      "distance": "15km", // 半径
      "FIELD": "31.21,121.5" // 圆心
    }
  }
}
```

我们先搜索陆家嘴附近15km的酒店

![](https://cdn.xn2001.com/img/2021/20210921183228.png)

发现共有47家酒店，然后把半径缩短到3公里

![](https://cdn.xn2001.com/img/2021/20210921183240.png)

可以发现，搜索到的酒店数量减少到了5家。

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "location": "31.034661,121.612282", //圆心
          "order" : "asc", //排序
          "unit" : "km" //单位
      }
    }
  ]
}
```

结果为：

```json
"hits" : [
    {
        "_index" : "hotel",
        "_type" : "_doc",
        "_id" : "2056298828",
        "_score" : null,
        "_source" : {
            ...
        },
        "sort" : [
            4.8541199685347785 //这里的结果为离圆心的距离
        ]
    },
```

注意：输出结果中的 **sort** 为距离，比较常用。

排序完成后，页面还要获取我附近每个酒店的具体**距离**值，这个值在响应结果中是独立的：

![](https://cdn.xn2001.com/img/2021/20211019011334.png)

### 复合查询

复合（compound）查询：复合查询可以将其它简单查询组合起来，实现更复杂的搜索逻辑。

- fuction score：算分函数查询，可以控制文档相关性算分，控制文档排名
- bool query：布尔查询，利用逻辑关系组合多个其它的查询，实现复杂搜索

### 相关性算分

> 这部分内容作为了解即可。

当我们利用 match 查询时，文档结果会根据与搜索词条的关联度打分（_score），返回结果时按照分值降序排列。例如，我们搜索 "虹桥如家"，结果如下：

```json
[
  {
    "_score" : 17.850193,
    "_source" : {
      "name" : "虹桥如家酒店真不错",
    }
  },
  {
    "_score" : 12.259849,
    "_source" : {
      "name" : "外滩如家酒店真不错",
    }
  },
  {
    "_score" : 11.91091,
    "_source" : {
      "name" : "迪士尼如家酒店真不错",
    }
  }
]
```

elasticsearch 早期使用的打分算法是 **TF-IDF 算法**，公式如下：

![](https://cdn.xn2001.com/img/2021/20210921205752.png)

在后来的5.1版本升级中，elasticsearch 将算法改进为 **BM25 算法**，公式如下：

![](https://cdn.xn2001.com/img/2021/20210921205757.png)

TF-IDF 算法有一各缺陷，就是词条频率越高，文档得分也会越高，单个词条对文档影响较大。而 BM25 则会让单个词条的算分有一个上限，曲线更加平滑：

![](https://cdn.xn2001.com/img/2021/20210921205831.png)

### 算分函数查询

根据相关度打分是比较合理的需求，但有时候也不能够满足我们的需求。

以百度为例，你搜索的结果中，并不是相关度越高排名越靠前，而是谁给的钱多排名就越靠前。

**要想认为控制相关性算分，就需要利用 elasticsearch 中的 function score 查询了。**

![](https://cdn.xn2001.com/img/2021/20210921210256.png)

function score 查询中包含四部分内容：

- **原始查询**条件：query 部分，基于这个条件搜索文档，并且基于BM25算法给文档打分，**原始算分**（query score)
- **过滤条件**：filter 部分，符合该条件的文档才会**重新算分**
- **算分函数**：符合 filter 条件的文档要根据这个函数做运算，得到的**函数算分**（function score），有四种函数
  - weight：函数结果是常量
  - field_value_factor：以文档中的某个字段值作为函数结果
  - random_score：以随机数作为函数结果
  - script_score：自定义算分函数算法
- **运算模式**：算分函数的结果、原始查询的相关性算分，两者之间的运算方式，包括：
  - multiply：相乘
  - replace：用 function score 替换 query score
  - sum、avg、max、min

function score 的运行流程如下：

1. 根据**原始条件**查询搜索文档，并且计算相关性算分，称为**原始算分**（query score）
2. 根据**过滤条件**，过滤文档
3. 符合**过滤条件**的文档，基于**算分函数**运算，得到**函数算分**（function score）
4. 将**原始算分**（query score）和**函数算分**（function score）基于**运算模式**做运算，得到最终结果，作为相关性算分。

因此，其中的关键点是

- 过滤条件：决定哪些文档的算分被修改
- 算分函数：决定函数算分的算法
- 运算模式：决定最终算分结果

例如：我们给“如家”这个品牌的酒店排名靠前一些

```json
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {  .... }, // 原始查询，可以是任意条件
      "functions": [ // 算分函数
        {
          "filter": { // 满足的条件，品牌必须是如家
            "term": {
              "brand": "如家"
            }
          },
          "weight": 10 // 算分权重为10
        }
      ],
      "boost_mode": "sum" // 加权模式，求和
    }
  }
}
```

测试，在未添加算分函数时，如家得分如下

![](https://cdn.xn2001.com/img/2021/20210921231508.png)

添加了算分函数后，如家得分就提升了

![](https://cdn.xn2001.com/img/2021/20210921231513.png)

### 布尔查询

布尔查询是一个或多个查询子句的组合，每一个子句就是一个**子查询**。子查询的组合方式有

- must：必须匹配每个子查询，类似“与”
- should：选择性匹配子查询，类似“或”
- must_not：必须不匹配，**不参与算分**，类似“非”
- filter：必须匹配，**不参与算分**

比如在搜索酒店时，除了关键字搜索外，我们还可能根据品牌、价格、城市等字段做过滤

**每一个不同的字段，其查询的条件、方式都不一样，必须是多个不同的查询，而要组合这些查询，就必须用 bool查询了。**

需要注意的是，搜索时，参与**打分的字段越多，查询的性能也越差**。因此这种多条件查询时，建议这样做：

- 搜索框的关键字搜索，是全文检索查询，使用 must 查询，参与算分
- 其它过滤条件，采用 filter 查询，不参与算分

```json
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {"city": "上海" }}
      ],
      "should": [
        {"term": {"brand": "皇冠假日" }},
        {"term": {"brand": "华美达" }}
      ],
      "must_not": [
        { "range": { "price": { "lte": 500 } }}
      ],
      "filter": [
        { "range": {"score": { "gte": 45 } }}
      ]
    }
  }
}
```

需求：搜索名字包含“如家”，价格不高于 400，在坐标 31.21,121.5 周围 10km 范围内的酒店。

- 名称搜索，属于全文检索查询，应该参与算分，放到 must 中
- 价格不高于 400，用 range 查询，属于过滤条件，不参与算分，放到 must_not 中
- 周围 10km 范围内，用 geo_distance 查询，属于过滤条件，不参与算分，放到 filter 中

![](https://cdn.xn2001.com/img/2021/20210921233252.png)

bool 查询的几种逻辑关系

- must：必须匹配的条件，可以理解为“与”
- should：选择性匹配的条件，可以理解为“或”
- must_not：必须不匹配的条件，不参与打分
- filter：必须匹配的条件，不参与打分

## 搜索结果处理

### 排序

elasticsearch 默认是根据相关度算分（_score）来排序，但是也支持自定义方式对搜索[结果排序](https://www.elastic.co/guide/en/elasticsearch/reference/current/sort-search-results.html)。可以排序字段类型有：keyword 类型、数值类型、地理坐标类型、日期类型等

keyword、数值、日期类型排序的语法基本一致。

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "FIELD": "desc"  // 排序字段、排序方式ASC、DESC
    }
  ]
}
```

排序条件是一个数组，也就是可以写多个排序条件。按照声明的顺序，当第一个条件相等时，再按照第二个条件排序。

需求描述：酒店数据按照用户评价（score)降序排序，评价相同的按照价格(price)升序排序

![](https://cdn.xn2001.com/img/2021/20210921233829.png)

地理坐标排序略有不同

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "FIELD" : "纬度，经度", // 文档中geo_point类型的字段名、目标坐标点
          "order" : "asc", // 排序方式
          "unit" : "km" // 排序的距离单位
      }
    }
  ]
}
```

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "location": "31.034661,121.612282", 
          "order" : "asc", 
          "unit" : "km" 
      }
    }
  ]
}
```

> 获取你的位置的经纬度的方式：https://lbs.amap.com/demo/jsapi-v2/example/map/click-to-get-lnglat

假设我的位置是：31.034661，121.612282，寻找我周围距离最近的酒店。

![](https://cdn.xn2001.com/img/2021/20210921233931.png)

### 分页

elasticsearch 默认情况下只返回 top10 的数据。而如果要查询更多数据就需要修改分页参数了。

elasticsearch 通过修改 from、size 参数来控制要返回的分页结果：

- from：从第几个文档开始
- size：总共查询几个文档

类似于mysql中的`limit ?, ?`

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

> 深度分页问题

现在，我要查询990~1000的数据，查询逻辑要这么写

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 990, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

这里是查询990开始的数据，也就是 第990~第1000条 数据。

注意：elasticsearch 内部分页时，必须先查询 0~1000条，然后截取其中的 990 ~ 1000 的这10条

![](https://cdn.xn2001.com/img/2021/20210921234503.png)

查询TOP1000，如果 es 是单点模式，这并无太大影响。

但是 elasticsearch 将来一定是集群，例如我集群有5个节点，我要查询 TOP1000 的数据，并不是每个节点查询200条就可以了。节点A的 TOP200，在另一个节点可能排到10000名以外了。

**因此要想获取整个集群的 TOP1000，必须先查询出每个节点的 TOP1000，汇总结果后，重新排名，重新截取  TOP1000。**

![](https://cdn.xn2001.com/img/2021/20210921234555.png)

**当查询分页深度较大时，汇总数据过多，对内存和CPU会产生非常大的压力，因此 elasticsearch 会禁止from+ size 超过10000的请求。**

针对深度分页，ES提供了两种解决方案，[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html)：

- search after：分页时需要排序，原理是从上一次的排序值开始，查询下一页数据。官方推荐使用的方式。
- scroll：原理将排序后的文档id形成快照，保存在内存。官方已经不推荐使用。

---

分页查询的常见实现方案以及优缺点

- `from + size`
  - 优点：支持随机翻页
  - 缺点：深度分页问题，默认查询上限（from + size）是10000
  - 场景：百度、京东、谷歌、淘宝这样的随机翻页搜索
- `after search`
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：只能向后逐页查询，不支持随机翻页
  - 场景：没有随机翻页需求的搜索，例如手机向下滚动翻页

- `scroll`
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：会有额外内存消耗，并且搜索结果是非实时的
  - 场景：海量数据的获取和迁移。从ES7.1开始不推荐，建议用 after search方案。

### 高亮

我们在百度，京东搜索时，关键字会变成红色，比较醒目，这叫高亮显示：

![](https://cdn.xn2001.com/img/2021/20210921234711.png)

高亮显示的实现分为两步：

- 1）给文档中的所有关键字都添加一个标签，例如`<em>`标签
- 2）页面给`<em>`标签编写CSS样式

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT" // 查询条件，高亮一定要使用全文检索查询
    }
  },
  "highlight": {
    "fields": { // 指定要高亮的字段
      "FIELD": {
        "pre_tags": "<em>",  // 用来标记高亮字段的前置标签
        "post_tags": "</em>" // 用来标记高亮字段的后置标签
      }
    }
  }
}
```

**注意**：

- 高亮是对关键字高亮，因此**搜索条件必须带有关键字**，而不能是范围这样的查询。
- 默认情况下，**高亮的字段，必须与搜索指定的字段一致**，否则无法高亮
- 如果要对非搜索字段高亮，则需要添加一个属性：`required_field_match=false`

![](https://cdn.xn2001.com/img/2021/20210921234739.png)

> DSL 总体结构如下：

![](https://cdn.xn2001.com/img/2021/20210921235330.png)

## RestClient文档查询

### 发起查询请求

![](https://cdn.xn2001.com/img/2021/20211016170057.png)

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/16 17:05
 */
@SpringBootTest
public class HotelSearchTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    public void match_All() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

- 第一步，创建`SearchRequest`对象，指定索引库名
- 第二步，利用`request.source()`构建 DSL，DSL 中可以包含查询、分页、排序、高亮等
  - `query()`：代表查询条件，利用 `QueryBuilders.matchAllQuery()` 构建一个 match_all 查询的 DSL
- 第三步，利用 `client.search()` 发送请求，得到响应

关键的 API 有两个，一个是 `request.source()`，其中包含了查询、排序、分页、高亮等所有功能

![](https://cdn.xn2001.com/img/2021/20211016170203.png)

另一个是 `QueryBuilders`，其中包含 match、term、function_score、bool 等各种查询

![](https://cdn.xn2001.com/img/2021/20211016170234.png)

### 解析查询响应

![](https://cdn.xn2001.com/img/2021/20211016174631.png)

Elasticsearch 返回的结果是一个 JSON 字符串，结构包含

- `hits`：命中的结果
  - `total`：总条数，其中的value是具体的总条数值
  - `max_score`：所有结果中得分最高的文档的相关性算分
  - `hits`：搜索结果的文档数组，其中的每个文档都是一个 json 对象
    - `_source`：文档中的原始数据，也是 json 对象

因此，我们解析响应结果，就是逐层解析 JSON 字符串，流程如下

- `SearchHits`：通过 `response.getHits()` 获取，就是 json 中的最外层的 hits，代表命中的结果
  - `SearchHits.getTotalHits().value`：获取总条数信息
  - `SearchHits.getHits()`：获取 SearchHit 数组，也就是文档数组
    - `SearchHit.getSourceAsString()`：获取文档结果中的 `_source`，也就是原始的 json 文档数据

```java
/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/16 17:05
 */
@SpringBootTest
public class HotelSearchTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    public void match_All() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @BeforeEach
    void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
```

### match查询

![](https://cdn.xn2001.com/img/2021/20211016182859.png)

```java
@Test
public void matchQuery() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source()
            .query(QueryBuilders.matchQuery("all","如家"));
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}

@Test
public void multiMatchQuery() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source()
            .query(QueryBuilders.multiMatchQuery("如家","name","brand"));
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 精确查询

 精确查询主要是两者

- term：词条精确匹配
- range：范围查询

![](https://cdn.xn2001.com/img/2021/20211016192658.png) 

### 布尔查询

布尔查询是用 must、must_not、filter等方式组合其它查询，代码示例如下

![](https://cdn.xn2001.com/img/2021/20211016192745.png)

```java
@Test
void testBool() throws IOException {
    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    request.source()
            .query(
                    QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery("city", "上海"))
                            .filter(QueryBuilders.rangeQuery("price").lte(300))
            );
    // 3.发送请求
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 排序、分页

搜索结果的排序和分页是与 query 同级的参数，因此同样是使用 `request.source()` 来设置。

对应的API如下

![](https://cdn.xn2001.com/img/2021/20211016202447.png)

```java
@Test
void testPageAndSort() throws IOException {
    // 页码，每页大小
    int page = 1, size = 5;

    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    // 2.1.query
    request.source().query(QueryBuilders.matchAllQuery());
    // 2.2.排序 sort
    request.source().sort("price", SortOrder.ASC);
    // 2.3.分页 from、size
    request.source().from((page - 1) * size).size(5);
    // 3.发送请求
    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    System.out.println("hits.getTotalHits().条数 = " + searchHits.getTotalHits().value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 高亮

- 查询的 DSL：其中除了查询条件，还需要添加高亮条件，同样是与 query 同级。
- 结果解析：结果除了要解析 `_source` 文档数据，还要解析高亮结果

**高亮请求的构建 API**

![](https://cdn.xn2001.com/img/2021/20211016202707.png)

上述代码省略了查询条件部分，但是高亮查询必须使用全文检索查询，并且要有搜索关键字，将来才可以对关键字高亮.

```java
@Test
void testHighlight() throws IOException {
    // 1.准备Request
    SearchRequest request = new SearchRequest("hotel");
    // 2.准备DSL
    // 2.1.query
    request.source().query(QueryBuilders.matchQuery("all", "如家"));
    // 2.2.高亮
    request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
    // 3.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    // 4.解析响应
    handleResponse(response); //代码在下文
}
```

**高亮结果解析**

高亮的结果与查询的文档结果默认是分离的，并不在一起。

因此解析高亮的代码需要额外处理：

![](https://cdn.xn2001.com/img/2021/20211016202853.png)

- 第一步：从结果中获取 source。`hit.getSourceAsString()`，这部分是非高亮结果，json 字符串，需要反序列为 HotelDoc 对象
- 第二步：获取高亮结果。`hit.getHighlightFields()`，返回值是一个 Map，key 是高亮字段名称，值是HighlightField 对象，代表高亮值
- 第三步：从 map 中根据高亮字段名称，获取高亮字段值对象 HighlightField
- 第四步：从 HighlightField 中获取 Fragments，并且转为字符串。**这部分是真正的高亮字符串**
- 第五步：用高亮的结果替换 HotelDoc 中的非高亮结果

完整代码如下：

```java
private void handleResponse(SearchResponse response) {
    // 4.解析响应
    SearchHits searchHits = response.getHits();
    // 4.1.获取总条数
    long total = searchHits.getTotalHits().value;
    System.out.println("共搜索到" + total + "条数据");
    // 4.2.文档数组
    SearchHit[] hits = searchHits.getHits();
    // 4.3.遍历
    for (SearchHit hit : hits) {
        // 获取文档source
        String json = hit.getSourceAsString();
        // 反序列化
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        // 获取高亮结果
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        if (!CollectionUtils.isEmpty(highlightFields)) {
            // 根据字段名获取高亮结果
            HighlightField highlightField = highlightFields.get("name");
            if (highlightField != null) {
                // 获取高亮值
                String name = highlightField.getFragments()[0].string();
                // 覆盖非高亮结果
                hotelDoc.setName(name);
            }
        }
        System.out.println("hotelDoc = " + hotelDoc);
    }
}
```

### 地理坐标查询

![](https://cdn.xn2001.com/img/2021/20211019000540.png)

### 相关性得分

function_score 查询结构如下

![](https://cdn.xn2001.com/img/2021/20211019011433.png)



对应的 JavaAPI 如下

![](https://cdn.xn2001.com/img/2021/20211019011439.png)

## DSL数据聚合

**[聚合（](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[）](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)**可以让我们极其方便的实现对数据的统计、分析、运算。

- 什么品牌的手机最受欢迎？
- 这些手机的平均价格、最高价格、最低价格？
- 这些手机每月的销售情况如何？

在 Elasticsearch 实现这些统计功能比数据库的 sql 要方便的多，而且查询速度非常快，可以实现近实时搜索效果。

聚合常见的有三类

- **桶（Bucket）**聚合：用来对文档做分组
  - TermAggregation：按照文档字段值分组，例如按照品牌值分组、按照国家分组
  - Date Histogram：按照日期阶梯分组，例如一周为一组，或者一月为一组

- **度量（Metric）**聚合：用以计算一些值，比如：最大值、最小值、平均值等
  - Avg：求平均值
  - Max：求最大值
  - Min：求最小值
  - Stats：同时求 max、min、avg、sum 等
- **管道（pipeline）**聚合：其它聚合的结果为基础做聚合

> **注意**：参加聚合的字段必须是keyword、日期、数值、布尔类型

### Bucket聚合语法

例如：我们要统计所有数据中的酒店品牌有几种，其实就是按照品牌对数据分组。此时可以根据酒店品牌的名称做聚合，也就是 Bucket 聚合。

```json
GET /hotel/_search
{
  "size": 0,  // 设置size为0，结果中不包含文档，只包含聚合结果
  "aggs": { // 定义聚合
    "brandAgg": { //给聚合起个名字
      "terms": { // 聚合的类型，按照品牌值聚合，所以选择term
        "field": "brand", // 参与聚合的字段
        "size": 20 // 希望获取的聚合结果数量
      }
    }
  }
}
```

![](https://cdn.xn2001.com/img/2021/20211022184007.png)

默认情况下，Bucket 聚合会统计 Bucket 内的文档数量，记为 `_count`，并且按照 `_count` 降序排序。

我们可以指定 order 属性，自定义聚合的排序方式

```json
GET /hotel/_search
{
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "order": {
          "_count": "asc" // 按照_count升序排列
        },
        "size": 20
      }
    }
  }
}
```

默认情况下，Bucket 聚合是对索引库的所有文档做聚合，但真实场景下，用户会输入搜索条件，因此聚合必须是对搜索结果聚合。那么聚合必须添加限定条件。

我们可以限定要聚合的文档范围，只要添加 query 条件即可；

```json
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "lte": 200 // 只对200元以下的文档聚合
      }
    }
  }, 
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20
      }
    }
  }
}
```

这次，聚合得到的品牌明显变少了

![](https://cdn.xn2001.com/img/2021/20211022184549.png)

### Metric聚合语法

上面，我们对酒店按照品牌分组，形成了一个个桶。现在我们需要对桶内的酒店做运算，获取每个品牌的用户评分的 min、max、avg 等值。

这就要用到 Metric 聚合了，例如 stats 聚合：就可以获取 min、max、avg 等结果

```json
GET /hotel/_search
{
  "size": 0, 
  "aggs": {
    "brandAgg": { 
      "terms": { 
        "field": "brand", 
        "size": 20
      },
      "aggs": { // 是brands聚合的子聚合，也就是分组后对每组分别计算
        "score_stats": { // 聚合名称
          "stats": { // 聚合类型，这里stats可以计算min、max、avg等
            "field": "score" // 聚合字段，这里是score
          }
        }
      }
    }
  }
}
```

这次的 score_stats 聚合是在 brandAgg 的聚合内部嵌套的子聚合。因为我们需要在每个桶分别计算。

另外，我们还可以给聚合结果做个排序，例如按照每个桶的酒店平均分做排序

![](https://cdn.xn2001.com/img/2021/20211022184847.png)

## RestAPI数据聚合

聚合条件与 query 条件同级别，因此需要使用 `request.source()` 来指定聚合条件

![](https://cdn.xn2001.com/img/2021/20211022190429.png)

聚合的结果也与查询结果不同，API 也比较特殊。不过同样是 JSON 逐层解析

![](https://cdn.xn2001.com/img/2021/20211022190457.png)

```java
@Test
public void testAggregation() throws IOException {
    SearchRequest request = new SearchRequest("hotel");
    request.source().aggregation(AggregationBuilders.terms("brandAgg").field("brand").size(20));
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    Terms brandAgg = response.getAggregations().get("brandAgg");
    List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
    for (Terms.Bucket bucket : buckets) {
        String key = bucket.getKeyAsString();
        System.out.println("key = " + key);
    }
}
```

## 自动补全

当用户在搜索框输入字符时，我们应该提示出与该字符有关的搜索项，提示完整词条的功能，就是自动补全了。

### 拼音分词器

如果我们需要根据拼音字母来推断，因此要用到拼音分词功能。

要实现根据字母做补全，就必须对文档按照拼音分词。插件地址：https://github.com/medcl/elasticsearch-analysis-pinyin

![](https://cdn.xn2001.com/img/2021/20211023015636.png)

使用 `docker volume inspect es-plugins` 查看插件目录，将下载的文件解压上传，重启 Elasticsearch

测试用法如下：

```json
POST /_analyze
{
  "text": "如家酒店还不错",
  "analyzer": "pinyin"
}
```

结果：

![](https://cdn.xn2001.com/img/2021/20211023021713.png) 



### 自定义分词器

默认的拼音分词器会将每个汉字单独分为拼音，而我们希望的是每个词条形成一组拼音，需要对拼音分词器做个性化定制，形成自定义分词器。

elasticsearch 中分词器（analyzer）的组成包含三部分：

- character filters：在 tokenizer 之前对文本进行处理。例如删除字符、替换字符
- tokenizer：将文本按照一定的规则切割成词条（term）。例如 keyword，就是不分词；还有 ik_smart
- tokenizer filter：将 tokenizer 输出的词条做进一步处理。例如大小写转换、同义词处理、拼音处理等

文档分词时会依次由这三部分来处理文档：

   ![](https://cdn.xn2001.com/img/2021/20211023021451.png)

声明自定义分词器的语法如下：

```json
PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { // 自定义分词器
        "my_analyzer": {  // 分词器名称
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },
      "filter": { // 自定义tokenizer filter
        "py": { // 过滤器名称
          "type": "pinyin", // 过滤器类型，这里是pinyin
		  "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}
```

测试

![](https://cdn.xn2001.com/img/2021/20211023021532.png)

注意**：为了避免搜索到同音字，搜索时不要使用拼音分词器**

![](https://cdn.xn2001.com/img/2021/20211023022355.png)

### 自动补全查询

elasticsearch 提供了 [Completion Suggester](https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-suggesters.html) 查询来实现自动补全功能。这个查询会匹配以用户输入内容开头的词条并返回；为了提高补全查询的效率，对于文档中字段的类型有一些约束

- 参与补全查询的字段必须是 completion 类型。

- 字段的内容一般是用来补全的多个词条形成的数组。

```json
// 创建索引库
PUT test
{
  "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}
```

然后插入下面的数据

```json
// 示例数据
POST test/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test/_doc
{
  "title": ["Nintendo", "switch"]
}
```

查询的 DSL 语句如下

```json
// 自动补全查询
GET /test/_search
{
  "suggest": {
    "title_suggest": {
      "text": "s", // 关键字
      "completion": {
        "field": "title", // 补全查询的字段
        "skip_duplicates": true, // 跳过重复的
        "size": 10 // 获取前10条结果
      }
    }
  }
}
```

例如一个酒店的索引库完整案例

```json
// 酒店数据索引库
PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}
```

### JavaAPI

![](https://cdn.xn2001.com/img/2021/20211025113007.png)



解析响应的代码如下

![](https://cdn.xn2001.com/img/2021/20211025113011.png)

## 数据同步

elasticsearch 中的数据来自于 mysq l数据库，因此 mysql 数据发生改变时，elasticsearch 也必须跟着改变，这个就是 elasticsearch 与 mysql 之间的**数据同步**



![](https://cdn.xn2001.com/img/2021/20211025163219.png)

常见的数据同步方案有三种

- 同步调用
- 异步通知
- 监听 binlog

### 同步调用

方案一：同步调用

![](https://cdn.xn2001.com/img/2021/20211025163313.png)

- hotel-demo对外提供接口，用来修改 elasticsearch 中的数据
- 酒店管理服务在完成数据库操作后，直接调用 hotel-demo 提供的接口

### 异步通知

方案二：异步通知

![](https://cdn.xn2001.com/img/2021/20211025163346.png)

- hotel-admin 对 mysql 数据库数据完成增、删、改后，发送 MQ 消息
- hotel-demo监听 MQ，接收到消息后完成 elasticsearch 数据修改

### 监听binlog

方案三：监听binlog

![](https://cdn.xn2001.com/img/2021/20211025163428.png)

- mysql 开启 binlog 功能
- mysql 完成增、删、改操作都会记录在 binlog 中
- hotel-demo 基于canal 监听 binlog 变化，实时更新 elasticsearch 中的内容

### 优缺点

方式一：同步调用

- 优点：实现简单，粗暴
- 缺点：业务耦合度高

方式二：异步通知

- 优点：低耦合，实现难度一般
- 缺点：依赖 mq 的可靠性

方式三：监听binlog

- 优点：完全解除服务间耦合
- 缺点：开启 binlog 增加数据库负担、实现复杂度高

### 实现方式

我们以**异步通知**为例，使用 MQ 消息中间件

MQ结构如图：

![](https://cdn.xn2001.com/img/2021/20211025163734.png)

**引入依赖**，在 hotel-admin、hotel-demo 中引入 rabbitmq 的依赖：

```xml
<!--amqp-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**声明队列交换机**

```java
public class MQConstants {
    /**
     * 交换机
     */
    public final static String HOTEL_EXCHANGE = "hotel.topic";
    /**
     * 监听新增和修改的队列
     */
    public final static String HOTEL_INSERT_QUEUE = "hotel.insert.queue";
    /**
     * 监听删除的队列
     */
    public final static String HOTEL_DELETE_QUEUE = "hotel.delete.queue";
    /**
     * 新增或修改的RoutingKey
     */
    public final static String HOTEL_INSERT_KEY = "hotel.insert";
    /**
     * 删除的RoutingKey
     */
    public final static String HOTEL_DELETE_KEY = "hotel.delete";
}
```

消息接收方


```java
@Configuration
public class MqConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MqConstants.HOTEL_EXCHANGE, true, false);
    }

    @Bean
    public Queue insertQueue() {
        return new Queue(MqConstants.HOTEL_INSERT_QUEUE, true);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(MqConstants.HOTEL_DELETE_QUEUE, true);
    }

    @Bean
    public Binding insertQueueBinding() {
        return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MqConstants.HOTEL_INSERT_KEY);
    }

    @Bean
    public Binding deleteQueueBinding() {
        return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MqConstants.HOTEL_DELETE_KEY);
    }
}
```

消息发送方

```java
rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_INSERT_KEY, hotel.getId());

rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_DELETE_KEY, id);
```

 消息接收方

```java
@Override
public void insertById(Long id) {
    try {
        // 根据id查询酒店数据
        Hotel hotel = getById(id);
        // 转换为文档类型
        HotelDoc hotelDoc = new HotelDoc(hotel);

        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

@Override
public void deleteById(Long id) {
    try {
        DeleteRequest request = new DeleteRequest("hotel", id.toString());
        client.delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

```java
@Component
public class HotelListener {

    @Autowired
    private HotelService hotelService;

    /**
     * 监听酒店新增或修改的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id) {
        hotelService.insertById(id);
    }

    /**
     * 监听酒店删除的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_DELETE_QUEUE)
    public void listenHotelDelete(Long id) {
        hotelService.deleteById(id);
    }
}
```

# Elasticsearch集群

单机的 Elasticsearch 做数据存储，必然面临两个问题：海量数据存储问题、单点故障问题。

解决方案：

- 海量数据存储问题：将索引库从逻辑上拆分为N个分片（shard），存储到多个节点
- 单点故障问题：将分片数据在不同节点备份（replica ）

**ES集群相关概念**：

* 集群（cluster）：一组拥有共同的 cluster name 的 节点。

* 节点（node)  ：集群中的一个 Elasticearch 实例

* 分片（shard）：索引可以被拆分为不同的部分进行存储，称为分片。**在集群环境下，一个索引的不同分片可以拆分到不同的节点中，解决数据量太大，单点存储量有限的问题。**

![](https://cdn.xn2001.com/img/2022/202205071006783.png)

> 此处，我们把数据分成3片：shard0、shard1、shard2
>
> 主分片（Primary shard）：相对于副本分片的定义。
>
> 副本分片（Replica shard）每个主分片可以有一个或者多个副本，数据和主分片一样。

数据备份可以保证高可用，但是每个分片备份一份在节点上，所需要的节点数量就会翻倍，成本太高。为了在高可用和成本间寻求平衡

- 首先对数据分片，存储到不同节点
- 然后对每个分片进行备份，放到对方节点，**完成互相备份**

这样可以大大减少所需要的服务节点数量，如图，我们以3分片，每个分片备份一份为例：

![](https://cdn.xn2001.com/img/2022/202205071006790.png)

现在，每个分片都有1个备份，存储在3个节点：

- node0：保存了分片0和1
- node1：保存了分片0和2
- node2：保存了分片1和2

## 部署集群

### 搭建Elasticsearch 

我们会在单机上利用 Docker 容器运行多个 Elasticsearch 实例来模拟集群。

可以直接使用 docker-compose 来完成，这要求你的Linux虚拟机至少有**4G**以上的内存空间。

docker-compose.yml

```yml
version: '2.2'
services:
  es01:
    image: elasticsearch:7.12.1
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic
  es02:
    image: elasticsearch:7.12.1
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data02:/usr/share/elasticsearch/data
    ports:
      - 9201:9200
    networks:
      - elastic
  es03:
    image: elasticsearch:7.12.1
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic
    ports:
      - 9202:9200
volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local
networks:
  elastic:
    driver: bridge
```

修改 Linux 系统权限，修改 `/etc/sysctl.conf` 文件

```sh
vi /etc/sysctl.conf
```

添加下面的内容

```sh
vm.max_map_count=262144
```

让配置生效：

```sh
sysctl -p
```

通过docker-compose启动集群

```sh
docker-compose up -d
```

### 集群状态监控

kibana 可以监控 Elasticsearch 集群，但是更推荐使用 [cerebro](https://github.com/lmenezes/cerebro)

下载解压打开 /bin/cerebro.bat

访问 http://localhost:9000 即可进入管理界面

![](https://cdn.xn2001.com/img/2022/202205071018030.png)

输入任意节点的地址和端口，点击 connect 

![](https://cdn.xn2001.com/img/2022/202205071018761.png)

绿色的线条代表集群处于绿色（健康状态）

### 创建索引库

我们还可以通过 cerebro 创建索引库，当然你需要使用 kibana 也可以。

![](https://cdn.xn2001.com/img/2022/202205071020771.png)

填写索引库信息

![](https://cdn.xn2001.com/img/2022/202205071023380.png)

回到首页，即可查看索引库分片效果

![](https://cdn.xn2001.com/img/2022/202205071024397.png)

## 集群职责划分

Elasticsearch 中集群节点有不同的职责划分

![](https://cdn.xn2001.com/img/2022/202205071029258.png)

**默认情况下，集群中的任何一个节点都同时兼职上述四种角色。**

真实的集群一定要将集群职责分离

- master 节点：对 CPU 要求高，但是内存要求低
- data 节点：对 CPU 和内存要求都高
- coordinating 节点：对网络带宽、CPU 要求高

职责分离可以让我们根据不同节点的需求分配不同的硬件去部署。而且避免业务之间的互相干扰。

![](https://cdn.xn2001.com/img/2022/202205071029263.png)



## 集群脑裂问题

脑裂是因为集群中的节点失联导致的。

例如一个集群中，主节点 node1 与其它节点失联。

![](https://cdn.xn2001.com/img/2022/202205071029267.png)

此时，node2 和 node3 认为 node1 宕机，就会重新选主。

![](https://cdn.xn2001.com/img/2022/202205071344676.png)

当 node3 当选后，集群继续对外提供服务，node2 和 node3 自成集群，node1 自成集群，两个集群数据不同步，出现数据差异。

当网络恢复后，因为集群中有两个 master 节点，集群状态的不一致，出现脑裂的情况。

![](https://cdn.xn2001.com/img/2022/202205071344829.png)



解决脑裂的方案是，要求选票超过 **(eligible节点数量+1)/2** 才能当选为 master，因此 eligible 节点数量最好是奇数。对应配置项是`discovery.zen.minimum_master_nodes`，在版本 7.0 以后，已经成为默认配置，因此一般不会发生脑裂问题。

例如：3个节点形成的集群，选票必须超过 (3+1)/2 ，也就是 2 票。node3 得到 node2 和 node3 的选票，当选为 master。node1 只有自己 1 票，没有当选。集群中依然只有1个主节点，没有出现脑裂。

## 集群分布式存储

当新增文档时，应该保存到不同分片，保证数据均衡，那么 coordinating node 如何确定数据该存储到哪个分片呢？

Elasticsearch 会通过 hash 算法来计算文档应该存储到哪个分片

![](https://cdn.xn2001.com/img/2022/202205071412275.png)

- _routing 默认是文档的 id
- 算法与分片数量有关，因此索引库一旦创建，分片数量不能修改！

新增文档的流程如下图，

1. 新增一个 id=1 的文档
2. 对 id 做 hash 运算，假如得到的是 2，则应该存储到 shard-2
3. shard-2 的主分片在 node3 节点，将数据路由到 node3，node3 保存文档
4. 同步给 shard-2 的副本分片2(R-2)，在 node2 节点
5. 返回结果给 coordinating-node 节点(node1)

![](https://cdn.xn2001.com/img/2022/202205071412283.png)

## 集群分布式查询

Elasticsearch 查询分成两个阶段

- scatter phase：分散阶段，coordinating node 会把请求分发到每一个分片。

- gather phase：聚集阶段，coordinating node 汇总 data node 的搜索结果，并处理为最终结果集返回给用户。

![](https://cdn.xn2001.com/img/2022/202205071422105.png)



## 集群故障转移

集群的 master 节点会监控集群中的节点状态，如果发现有节点宕机，会立即将宕机节点的分片数据迁移到其它节点，确保数据安全，这个叫做故障转移。

例如一个集群结构如图，三个都是健康的。

![](https://cdn.xn2001.com/img/2022/202205071516134.png)

现在，node1 是主节点，其它两个节点是从节点。突然，node1 发生了故障

![](https://cdn.xn2001.com/img/2022/202205071516139.png)



宕机后的第一件事，需要重新选主，例如选中了 node2

![](https://cdn.xn2001.com/img/2022/202205071516685.png)



node2 成为主节点后，会检测集群监控状态，将 node1 上的数据迁移到 node2、node3，确保数据依旧正常访问。

![](https://cdn.xn2001.com/img/2022/202205071516805.png)

# JMeter压力测试

## 安装启动

JMeter 依赖于JDK，所以必须确保当前计算机上已经安装了 JDK，并且配置了环境变量。

Apache Jmeter官网下载，地址：http://jmeter.apache.org/download_jmeter.cgi

![](https://cdn.xn2001.com/img/2022/202205081544073.png)

解压缩即可使用，目录结构如下

![](https://cdn.xn2001.com/img/2022/202205081545678.png)

其中的 bin 目录就是执行的脚本，其中包含启动脚本

![](https://cdn.xn2001.com/img/2022/202205081545976.png)

双击即可运行，但是有两点注意

- 启动时速度比较慢，要耐心等待。
- 启动后终端（黑窗口）不能关闭，否则 JMeter 也跟着关闭。

![](https://cdn.xn2001.com/img/2022/202205081545691.png)

## 修改中文

默认 JMeter 的语言是英文，需要设置

![](https://cdn.xn2001.com/img/2022/202205081547848.png)

![](https://cdn.xn2001.com/img/2022/202205081547110.png)

上面的配置只能保证本次运行是中文，如果要永久中文，需要修改 JMeter 的配置文件。

打开 JMeter 文件夹，在 bin 目录中找到 **jmeter.properties**，添加下面配置：

```properties
language=zh_CN
```

![](https://cdn.xn2001.com/img/2022/202205081546219.png)

## 基本使用

在测试计划上点鼠标右键，选择「添加 > 线程（用户） > 线程组」

![](https://cdn.xn2001.com/img/2022/202205081548692.png)

在新增的线程组中，填写线程信息

![](https://cdn.xn2001.com/img/2022/202205081549330.png)

在线程组这里点鼠标右键，添加 http 请求

![](https://cdn.xn2001.com/img/2022/202205081551101.png)

编写取样器内容

![](https://cdn.xn2001.com/img/2022/202205081551053.png)

添加监听报告

![](https://cdn.xn2001.com/img/2022/202205081551627.png)

汇总报告结果

![](https://cdn.xn2001.com/img/2022/202205081548975.png)

添加监听结果树

![](https://cdn.xn2001.com/img/2022/202205231440680.png)

察看结果树

![](https://cdn.xn2001.com/img/2022/202205081548982.png)

# Sentine流量组件

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/10-sentinel-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/10-sentinel-demo

## 雪崩问题

> 微服务之间相互调用，因为调用链中的一个服务故障，引起整个链路都无法访问的情况。

微服务中，服务间调用关系错综复杂，一个微服务往往依赖于多个其它微服务。

 ![](https://cdn.xn2001.com/img/2022/202205071911137.png)

如图，如果服务提供者I 发生了故障，当前的应用的部分业务因为依赖于服务I，因此也会被阻塞。此时，其它不依赖于服务I 的业务似乎不受影响。

 ![](https://cdn.xn2001.com/img/2022/202205071911461.png)

但是，依赖服务I 的业务请求被阻塞，则 tomcat 的这个线程不会释放，于是越来越多的用户请求到来，越来越多的线程会阻塞。

 ![](https://cdn.xn2001.com/img/2022/202205071911464.png)

服务器支持的线程和并发数有限，请求一直阻塞，会导致服务器资源耗尽，**从而导致所有其它服务都不可用**。

综上，依赖于当前服务的其它服务随着时间的推移，最终也都会变的不可用，形成级联失败，这就是雪崩问题。

![](https://cdn.xn2001.com/img/2022/202205071911480.png)

解决雪崩问题的常见方式有四种

1. 超时处理
2. 线程隔离
3. 降级熔断
4. 限流

> **限流**是对服务的保护，避免因瞬间高并发流量而导致服务故障，进而避免雪崩。是一种**预防**措施。
>
> **超时处理、线程隔离、降级熔断**是在部分服务故障时，将故障控制在一定范围，避免雪崩。是一种**补救**措施。

1.超时处理：设定超时时间，请求超过一定时间没有响应就返回错误信息，不会无休止等待。

![](https://cdn.xn2001.com/img/2022/202205071936072.png)

2.线程隔离

![](https://cdn.xn2001.com/img/2022/202205071953573.png)

是一种舱壁模式，如上图，船舱都会被隔板分离为多个独立空间，当船体破损时，只会导致部分空间进入，将故障控制在一定范围内，避免整个船体都被淹没。于此类似，我们可以限定每个业务能使用的线程数，避免耗尽整个 tomcat 的资源，因此也叫线程隔离。

![](https://cdn.xn2001.com/img/2022/202205071935351.png)

3.降级熔断

是一种断路器模式：由**断路器**统计业务执行的异常比例，如果超出阈值则会**熔断**该业务，拦截访问该业务的一切请求。

断路器会统计访问某个服务的请求数量，异常比例。

![](https://cdn.xn2001.com/img/2022/202205071956512.png)

当发现访问服务 D 的请求异常比例过高时，认为服务 D 有导致雪崩的风险，会拦截访问服务 D 的一切请求，形成熔断。

![](https://cdn.xn2001.com/img/2022/202205071956665.png)

4.限流

**流量控制**：限制业务访问的 QPS，避免服务因流量的突增而故障。

![](https://cdn.xn2001.com/img/2022/202205071935370.png)

在 SpringCloud 当中支持多种服务保护技术

- [Netfix Hystrix](https://github.com/Netflix/Hystrix)
- [Sentinel](https://github.com/alibaba/Sentinel)
- [Resilience4J](https://github.com/resilience4j/resilience4j)

早期比较流行的是 Hystrix 框架，但目前国内实用最广泛的还是阿里巴巴的 Sentinel 框架，这里我们做下对比：

|                | **Sentinel**                                   | **Hystrix**                   |
| -------------- | ---------------------------------------------- | ----------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离         |
| 熔断降级策略   | 基于慢调用比例或异常比例                       | 基于失败比率                  |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于 RxJava）       |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                |
| 扩展性         | 多个扩展点                                     | 插件的形式                    |
| 基于注解的支持 | 支持                                           | 支持                          |
| 限流           | 基于 QPS，支持基于调用关系的限流               | 有限的支持                    |
| 流量整形       | 支持慢启动、匀速排队模式                       | 不支持                        |
| 系统自适应保护 | 支持                                           | 不支持                        |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                        |
| 常见框架的适配 | Servlet、Spring Cloud、Dubbo、gRPC  等         | Servlet、Spring Cloud Netflix |

## 初识Sentinel

Sentinel是阿里巴巴开源的一款微服务流量控制组件。官网地址：https://sentinelguard.io/zh-cn/index.html

Sentinel 具有以下特征

- **丰富的应用场景**：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。
- **完备的实时监控**：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
- **广泛的开源生态**：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
- **完善的** **SPI** **扩展点**：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。

## 整合Sentinel

下载后 jar 包后，运行代码：`java -jar sentinel-dashboard-1.8.1.jar`

如果要修改 Sentinel 的默认端口、账户、密码，可以通过下列配置：

| **配置项**                       | **默认值** | **说明**   |
| -------------------------------- | ---------- | ---------- |
| server.port                      | 8080       | 服务端口   |
| sentinel.dashboard.auth.username | sentinel   | 默认用户名 |
| sentinel.dashboard.auth.password | sentinel   | 默认密码   |

例如，修改端口：

```sh
java -Dserver.port=8090 -jar sentinel-dashboard-1.8.1.jar
```

访问 http://localhost:8080 页面，就可以看到 Sentinel 的控制台了。

![](https://cdn.xn2001.com/img/2022/202205072010468.png)

账号和密码默认都是：sentinel

![](https://cdn.xn2001.com/img/2022/202205072018383.png)

此时空白一片，还需要我们来整合进 SpringCloud

准备好我们的项目，在资料中，结构如下：

> 这里老师讲的疏忽了，没有走 gateway 网关，但不影响。
>
> 提供的资料中的 gateway 网关带了权限配置导致这里我们访问不了网关（之前上课留下的），需要删除掉其中的 AuthorizeFilter.jar

![](https://cdn.xn2001.com/img/2022/202205072033318.png)

我们在 order-service 中整合 Sentinel，并连接 Sentinel 的控制台，步骤如下

1）引入 Sentinel 依赖

```xml
<!--sentinel-->
<dependency>
    <groupId>com.alibaba.cloud</groupId> 
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

2）配置控制台

修改 application.yml 文件，添加下面内容：

```yaml
server:
  port: 8088
spring:
  cloud: 
    sentinel:
      transport:
        dashboard: localhost:8080
```

3）访问 order-service 的任意端点

打开浏览器，访问 http://localhost:10010/order/101，多访问几次，多点几次刷新，这样才能触发 Sentinel 的监控。

然后再访问 Sentinel 的控制台，查看效果。

![](https://cdn.xn2001.com/img/2022/202205081430741.png)

## 流量控制

雪崩问题虽有四种方案，但是**限流是避免服务因突发的流量而发生故障，是对微服务雪崩问题的预防。**学过毛中特的都知道，《预判风险所在是防范风险的前提》，我们先学习流量控制。

### 簇点链路

当请求进入微服务时，首先会访问 DispatcherServlet，然后进入 Controller、Service、Mapper，这样的一个调用链就叫做 **簇点链路**。

**簇点链路中被监控的每一个接口就是一个资源**。默认情况下 Sentinel 会监控 SpringMVC 的每一个端点（Endpoint，也就是 Controller 中的方法），因此 SpringMVC 的每一个端点（Endpoint）就是调用链路中的一个资源。

例如，我们刚才访问的 order-service 中的 OrderController 中的端点：/order/{orderId}

![](https://cdn.xn2001.com/img/2022/202205081445779.png)



流控、熔断等都是针对簇点链路中的资源来设置的，因此我们可以点击对应资源后面的按钮来设置规则：

- 流控：流量控制
- 降级：降级熔断
- 热点：热点参数限流，是限流的一种
- 授权：请求的权限控制

点击资源 /order/{orderId} 后面的流控按钮，就可以弹出表单。

![](https://cdn.xn2001.com/img/2022/202205081509045.png)

表单中可以填写限流规则，如下

![](https://cdn.xn2001.com/img/2022/202205081509055.png)

其含义是限制 /order/{orderId} 这个资源的单机 QPS 为 1，即每秒只允许 1 次请求，超出的请求会被拦截并报错。

> 需求：给 /order/{orderId} 这个资源设置流控规则，QPS 不能超过 5，然后测试。

1）首先在 sentinel 控制台添加限流规则

![](https://cdn.xn2001.com/img/2022/202205081513259.png)

2）利用 jmeter 测试，下面有讲到 JMeter 基础使用。

![](https://cdn.xn2001.com/img/2022/202205081514678.png)

20 个用户，2 秒内运行完，这样的话 QPS 就是 10，超过了我们在 sentinel 设置的 5

右键运行：

![](https://cdn.xn2001.com/img/2022/202205081512845.png)



> 注意，不要点击菜单中的执行按钮来运行。

结果：

![](https://cdn.xn2001.com/img/2022/202205081512850.png)

可以看到，成功的请求每次只有 5 个。

![](https://cdn.xn2001.com/img/2022/202205081525292.png)

### 流控模式

在添加限流规则时，点击高级选项，可以选择三种**流控模式**

#### 直接模式

- 直接：统计当前资源的请求，触发阈值时对当前资源直接限流，也是默认的模式
  - 直接对当前资源限流

- 关联：统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流
  - 相当于高优先级资源触发阈值，对低优先级资源限流。

- 链路：统计从指定链路访问到本资源的请求，触发阈值时，对指定链路限流
  - 是针对请求来源的限流


![](https://cdn.xn2001.com/img/2022/202205081553073.png)



上面我们测试的就是直接模式，默认就是直接模式。

#### 关联模式

统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流。

**使用场景**：比如用户支付时需要修改订单状态，同时用户要查询订单。查询和修改操作会争抢数据库锁，产生竞争。业务需求是优先支付和更新订单的业务，因此当修改订单业务触发阈值时，需要对查询订单业务限流。

![](https://cdn.xn2001.com/img/2022/202205081602290.png)

例如：配置规则，**当 /write 资源访问量触发阈值时，就会对 /read 资源限流，避免影响 /write 资源。**

![](https://cdn.xn2001.com/img/2022/202205081602252.png)

我们去程序中模拟：

- 在 OrderController 新建两个端点：/order/query 和 /order/update，无需实现业务

- 配置流控规则，当 /order/ update 资源被访问的 QPS 超过 5 时，对 /order/query 请求限流

1）定义 /order/query 端点，模拟订单查询

```java
@GetMapping("/query")
public String queryOrder() {
    return "查询订单成功";
}
```

2）定义 /order/update 端点，模拟订单更新

```java
@GetMapping("/update")
public String updateOrder() {
    return "更新订单成功";
}
```

重启服务，查看 Sentinel 控制台的簇点链路。

![](https://cdn.xn2001.com/img/2022/202205081618267.png)



3）配置流控规则

想要对哪个端点限流，就点击哪个端点后面的按钮。我们是对订单查询 /order/query 限流

![](https://cdn.xn2001.com/img/2022/202205081619163.png)在表单中填写流控规则

![](https://cdn.xn2001.com/img/2022/202205081602274.png)

4）在 JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205081619453.png)

可以看到 1000 个用户，100 秒，因此 QPS 为10，超过了我们设定的阈值：5

查看 http 请求

![](https://cdn.xn2001.com/img/2022/202205081602280.png)

请求的目标是 /order/update，但限流的目标是 /order/query，这就是关联模式；我们在浏览器访问 /order/query，可以发现确实被限流了。

![](https://cdn.xn2001.com/img/2022/202205081620364.png)

#### 链路模式

只针对从指定链路访问到本资源的请求做统计，判断是否超过阈值。

例如有两条请求链路

- /test1 --> /common

- /test2 --> /common

如果只希望统计从 /test2 进入到 /common 的请求，则可以这样配置

![](https://cdn.xn2001.com/img/2022/202205081632892.png)

**实战案例**

有查询订单和创建订单业务，两者都需要查询商品。针对从查询订单进入到查询商品的请求统计，并设置限流。

1. 在 OrderService 中添加一个 queryGoods 方法，不用实现业务

2. 在 OrderController 中，改造 /order/query 端点，调用 OrderService 中的 queryGoods 方法

3. 在 OrderController 中添加一个 /order/save 端点，调用 OrderService 的 queryGoods 方法

4. 给 queryGoods 设置限流规则，从 /order/query 进入 queryGoods 的方法限制 QPS 必须小于 2

1）添加查询商品方法

在order-service服务中，给 OrderService 类添加一个 queryGoods 方法

```java
public void queryGoods(){
    System.err.println("查询商品");
}
```

2）查询订单时，查询商品

在 order-service 的 OrderController 中，修改 /order/query 端点的业务逻辑

```java
@GetMapping("/query")
public String queryOrder() {
    // 查询商品
    orderService.queryGoods();
    // 查询订单
    System.out.println("查询订单");
    return "查询订单成功";
}  
```

3）新增订单，查询商品

在 order-service 的 OrderController 中，修改 /order/save 端点，模拟新增订单：

```java
@GetMapping("/save")
public String saveOrder() {
    // 查询商品
    orderService.queryGoods();
    // 查询订单
    System.out.println("新增订单");
    return "新增订单成功";
}
```

4）给查询商品添加资源标记

默认情况下，OrderService 中的方法是不被 Sentinel 监控的，需要我们自己通过注解来标记要监控的方法。

给 OrderService 的 queryGoods 方法添加 `@SentinelResource` 注解。

```java
@SentinelResource("goods")
public void queryGoods(){
    System.err.println("查询商品");
}
```

链路模式中，是对不同来源的两个链路做监控。但是 Sentinel 默认会给进入 SpringMVC 的所有请求设置同一个 root 资源，会导致链路模式失效。我们需要关闭这种对 SpringMVC 的资源聚合，修改 order-service 服务的 application.yml 文件

```yaml
spring:
  cloud:
    sentinel:
      web-context-unify: false # 关闭context整合
```

重启服务，访问 /order/query 和 /order/save，可以查看到 Sentinel 的簇点链路规则中，出现了新的资源

![](https://cdn.xn2001.com/img/2022/202205081633195.png)



5）添加流控规则

点击 goods 资源后面的流控按钮，在弹出的表单中填写下面信息

![](https://cdn.xn2001.com/img/2022/202205081633212.png)



只统计从 /order/query 进入 /goods 的资源，QPS 阈值为 2，超出则被限流。

6）Jmeter测试

![](https://cdn.xn2001.com/img/2022/202205081633203.png)

可以看到这里200个用户，50秒内发完，QPS为4，超过了我们设定的阈值2

一个 http 请求是访问 /order/save

![](https://cdn.xn2001.com/img/2022/202205081858739.png)

运行的结果是 /order/save 完全不受影响。

![](https://cdn.xn2001.com/img/2022/202205081858880.png)

访问 /order/query

![](https://cdn.xn2001.com/img/2022/202205081633221.png)

运行结果是 /order/query，每次只有2个通过。

![](https://cdn.xn2001.com/img/2022/202205081633245.png)

## 流控效果

### 快速失败

细心的小伙伴会发现在流控的高级选项中，还有一个流控效果选项，前面我们的测试都是基本快速失败的。

![](https://cdn.xn2001.com/img/2022/202205081907653.png)

流控效果是指请求达到流控阈值时应该采取的措施，包括三种

- 快速失败：达到阈值后，新的请求会被立即拒绝并抛出 FlowException 异常，是默认的处理方式。

- Warm Up：预热模式，对超出阈值的请求同样是拒绝并抛出异常。但这种模式阈值会动态变化，从一个较小值逐渐增加到最大阈值。

- 排队等待：让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长。

### Warm up

阈值一般是一个微服务能承担的最大 QPS，但是一个服务刚刚启动时，一切资源尚未初始化（**冷启动**），如果直接将 QPS 跑到最大值，可能导致服务瞬间宕机。

Warm Up 也叫**预热模式**，是应对服务冷启动的一种方案。请求阈值初始值是 `maxThreshold / coldFactor`，持续指定时长后，逐渐提高到 maxThreshold 值。而 coldFactor 的默认值是 3.

例如，我设置 QPS 的 maxThreshold 为 10，预热时间为 5 秒，那么初始阈值就是 10 / 3 = 3，然后在 5 秒后逐渐增长到 10

![](https://cdn.xn2001.com/img/2022/202205081907114.png)



**案例**：给 /order/{orderId} 这个资源设置限流，最大 QPS 为 10，利用 Warm Up 效果，预热时长为 5 秒。

1）配置流控规则

![](https://cdn.xn2001.com/img/2022/202205090008173.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205090008283.png)

刚刚启动时，大部分请求失败，成功的只有 3 个，说明 QPS 被限定在 3 

![](https://cdn.xn2001.com/img/2022/202205090009927.png)

随着时间推移，成功比例越来越高

![](https://cdn.xn2001.com/img/2022/202205090009448.png)



到 Sentinel 控制台查看实时监控

![](https://cdn.xn2001.com/img/2022/202205081907395.png)

一段时间后

![](https://cdn.xn2001.com/img/2022/202205081907398.png)

用官方的话讲，该方式主要用于系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮的情况。

### 排队等待

当请求超过 QPS 阈值时，「快速失败」和 「Warm Up」会拒绝新的请求并抛出异常。

而排队等待则是让所有请求进入一个队列中，然后按照阈值允许的时间间隔依次执行。后来的请求必须等待前面执行完成，如果请求预期的等待时间超出最大时长，则会被拒绝。这种方式严格控制了请求通过的间隔时间，也即是让请求以均匀的速度通过，对应的是漏桶算法。

例如：QPS = 5，意味着每 200ms 处理一个队列中的请求；timeout = 2000，意味着**预期等待时长**超过 2000ms 的请求会被拒绝并抛出异常。

比如现在一下子来了 12  个请求，因为每 200ms 执行一个请求，那么预期等待时长就是：

- 第6个请求的**预期等待时长** =  200 * (6 - 1) = 1000ms
- 第12个请求的预期等待时长 = 200 * (12-1) = 2200ms

又比如下图：

![](https://cdn.xn2001.com/img/2022/202205090037622.png)

现在，第 1 秒同时接收到 10 个请求，但第 2 秒只有 1 个请求，此时 QPS 的曲线这样的

![](https://cdn.xn2001.com/img/2022/202205081907396.png)

如果使用排队等待的流控效果，所有进入的请求都要排队，以固定的 200ms 的间隔执行，QPS 会变的很平滑

![](https://cdn.xn2001.com/img/2022/202205090016702.png)

这种方式主要用于处理间隔性突发的流量，例如消息队列。想象一下这样的场景，在某一秒有大量的请求到来，而接下来的几秒则处于空闲状态，我们希望系统能够在接下来的空闲期间逐渐处理这些请求，而不是在第一秒直接拒绝多余的请求。

**案例**：给 /order/{orderId} 这个资源设置限流，最大 QPS 为 10，利用排队等待的流控效果，超时时长设置为 5s

1）添加流控规则

![](https://cdn.xn2001.com/img/2022/202205090011577.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205081907171.png)

QPS 为 15，已经超过了我们设定的 10，如果是之前的「快速失败」 、「Warm Up」模式，超出的请求应该会直接报错。

但是我们看看「排队等待」的运行结果

![](https://cdn.xn2001.com/img/2022/202205081907254.png)

再去 Sentinel 查看实时监控的 QPS 曲线

![](https://cdn.xn2001.com/img/2022/202205081907426.png)

QPS 非常平滑，一致保持在 10，但是超出的请求没有被拒绝，而是放入队列。因此**响应时间**（等待时间）会越来越长。

当队列满了以后，才会有部分请求失败

![](https://cdn.xn2001.com/img/2022/202205081907443.png)

## 热点参数限流

之前的限流是统计访问某个资源的所有请求，判断是否超过 QPS 阈值。而「热点参数限流」是**分别统计参数值相同的请求**，判断是否超过 QPS 阈值。

### 全局参数限流

例如，一个根据 id 查询商品的接口

![](https://cdn.xn2001.com/img/2022/202205090045292.png)

访问 /goods/{id} 的请求中，id 参数值会有变化，「热点参数限流」会根据参数值分别统计 QPS，统计结果：

![](https://cdn.xn2001.com/img/2022/202205090046762.png)

当 id=1 的请求触发阈值被限流时，id值不为1的请求则不受影响。

配置示例：对 hot 这个资源的 0 号参数（也就是第一个参数）做统计，每 1s **相同参数值**的请求数不能超过 5

![](https://cdn.xn2001.com/img/2022/202205090042929.png)

### 热点参数限流

假设上面的例子是一个商品查询接口，那么刚才的配置中，对这个接口的所有商品一视同仁，QPS 都限定为 5

而在实际开发中，可能部分商品是热点商品，例如秒杀商品，我们希望这部分商品的 QPS 限制与其它商品不一样，高一些。那就需要配置「热点参数限流」的高级选项了。

![](https://cdn.xn2001.com/img/2022/202205090050071.png)

结合上一个配置，这里的含义是对 0 号的 long 类型参数限流，每 1 个相同参数的 QPS 不能超过 5，有如下两个例外

- 如果参数值是 100，则每 1s 允许的 QPS 为 10
- 如果参数值是 101，则每 1s 允许的 QPS 为 15

**案例需求**：给 /order/{orderId} 这个资源添加「热点参数限流」，规则如下

- 默认的热点参数规则是每 1s 请求量不超过 2
- 给 102 这个参数设置例外：每 1s 请求量不超过 4
- 给 103 这个参数设置例外：每 1s 请求量不超过 10

**注意事项**：热点参数限流对默认的 SpringMVC 资源无效，需要利用 `@SentinelResource` 注解标记资源。

1）标记资源

给 order-service 中的 OrderController 中的 /order/{orderId} 资源添加注解

![](https://cdn.xn2001.com/img/2022/202205090042942.png)

2）热点参数限流规则

访问该接口，可以看到我们标记的 hot 资源出现了

![](https://cdn.xn2001.com/img/2022/202205090136368.png)

点击左侧菜单中**热点规则**菜单

![](https://cdn.xn2001.com/img/2022/202205090137049.png)

点击新增，填写表单

![](https://cdn.xn2001.com/img/2022/202205090137574.png)

3）JMeter 测试

这里发起请求的 QPS 为 5，包含 3 个 http 请求

![](https://cdn.xn2001.com/img/2022/202205090138955.png)

普通参数，QPS 阈值为 2

![](https://cdn.xn2001.com/img/2022/202205090042964.png)

![](https://cdn.xn2001.com/img/2022/202205090042203.png)



例外项，QPS 阈值为 4

![](https://cdn.xn2001.com/img/2022/202205090140941.png)

![](https://cdn.xn2001.com/img/2022/202205090042238.png)



**例外项，QPS 阈值为 10**

![](https://cdn.xn2001.com/img/2022/202205090042244.png)

![](https://cdn.xn2001.com/img/2022/202205090140933.png)

## 隔离和降级

限流只是一种预防措施，虽然限流可以尽量避免因高并发而引起的服务故障，但服务还会因为其它原因而故障。

而要将这些故障控制在一定范围，避免雪崩，就要靠**线程隔离**（舱壁模式）和**熔断降级**手段了。

**线程隔离**：调用者在调用服务提供者时，给每个调用的请求分配独立线程池，出现故障时，最多消耗这个线程池内资源，避免把调用者的所有资源耗尽。

![](https://cdn.xn2001.com/img/2022/202205091001949.png)

**熔断降级**：是在调用方这边加入断路器，统计对服务提供者的调用，如果调用的失败比例过高，则熔断该业务，不允许访问该服务的提供者了。

![](https://cdn.xn2001.com/img/2022/202205091203825.png)

可以看到，不管是线程隔离还是熔断降级，都是对**客户端**（调用方）的保护。需要在**调用方**发起远程调用时做线程隔离、或者服务熔断。

而我们的微服务远程调用都是基于 Feign 来完成的，因此我们需要将 Feign 与 Sentinel 整合，在 Feign 里面实现线程隔离和服务熔断。

## Feign整合Sentinel

SpringCloud中，微服务调用都是通过Feign来实现的，因此做客户端保护必须整合 Feign 和 Sentinel

修改配置，开启 Sentinel 功能，修改 OrderService 的 application.yml 文件，开启 Feign 的 Sentinel 功能

```yaml
feign:
  sentinel:
    enabled: true # 开启feign对sentinel的支持
```

服务降级：访问失败后，服务

**编写失败降级逻辑代码**，业务失败后，不能直接报错，而应该返回用户一个友好提示或者默认结果，这个就是失败降级逻辑。

给 FeignClient 编写失败后的降级逻辑

①方式一：FallbackClass，但无法对远程调用的异常做处理。

②方式二：FallbackFactory，可以对远程调用的异常做处理，我们选择这种

这里我们演示方式二的失败降级处理。

**步骤一**：在 feing-api 项目中定义类，实现 FallbackFactory

![](https://cdn.xn2001.com/img/2022/202205091209104.png)

```java
package com.xn2001.feign.clients.fallback;

import com.xn2001.feign.clients.UserClient;
import com.xn2001.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2022/5/9 14:24
 */

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
       return userClient -> {
           log.error("查询用户失败",throwable);
           return new User();
       };
    }
}
```

**步骤二**：在 feing-api 项目中的 DefaultFeignConfiguration 类中将 UserClientFallbackFactory 注册为一个Bean

```java
@Bean
public UserClientFallbackFactory userClientFallbackFactory(){
    return new UserClientFallbackFactory();
}
```

**步骤三**：在 feing-api 项目中的 UserClient 接口中使用 UserClientFallbackFactory

```java
@FeignClient(value = "userservice", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
```

重启后，访问一次订单查询业务，然后查看 Sentinel 控制台，可以看到新的簇点链路

![](https://cdn.xn2001.com/img/2022/202205091210616.png)

## 线程隔离

线程隔离（舱壁模式）有两种方式实现

- 线程池隔离：给每个服务调用业务分配一个线程池，利用线程池本身实现隔离效果。

- 信号量隔离（Sentinel默认采用）：不创建线程池，而是计数器模式，记录业务使用的线程数量，达到信号量上限时，禁止新的请求。

两者的优缺点

- 线程池隔离：基于线程池模式，有额外开销，但隔离控制更强
- 信号量隔离：基于计数器模式，简单，开销小

![](https://cdn.xn2001.com/img/2022/202205091501180.png)

![](https://cdn.xn2001.com/img/2022/202205091502961.png)

**Sentinel 使用的是信号量隔离**，而 Hystrix 则两种线程隔离都可以，18 年Hystrix已经停止更新。

如何使用呢，在添加限流规则时，可以选择两种阈值类型

![](https://cdn.xn2001.com/img/2022/202205091500754.png)

- QPS：就是每秒的请求数，之前已经演示过。

- 线程数：是该资源能使用的 Tomcat 线程数的最大值。也就是通过限制线程数量，实现**线程隔离**（舱壁模式）。

**案例需求**：给 order-service 服务中的 UserClient 的查询用户接口设置流控规则，线程数不能超过 2，然后利用 JMeter 测试。

1）配置隔离规则，选择 feign 接口后面的流控按钮

![](https://cdn.xn2001.com/img/2022/202205091507487.png)

![](https://cdn.xn2001.com/img/2022/202205091508076.png)

2）JMeter 测试

![](https://cdn.xn2001.com/img/2022/202205091500777.png)

一次发生 10 个请求，有较大概率并发线程数超过 2，而超出的请求会走之前定义的失败降级逻辑。

查看运行结果

![](https://cdn.xn2001.com/img/2022/202205091500739.png)

发现虽然结果都是通过了，不过部分请求得到的响应是「降级」返回的 null 信息。也就是我们写的降级方法。

## 熔断降级

熔断降级是解决雪崩问题的重要手段。其思路是由**断路器**统计服务调用的异常比例、慢请求比例，如果超出阈值则会**熔断**该服务。即拦截访问该服务的一切请求；而当服务恢复时，断路器会放行访问该服务的请求。

断路器控制熔断和放行是通过状态机来完成的，如下图就是一个断路器的状态机

![](https://cdn.xn2001.com/img/2022/202205091510676.png)

状态机包括三个状态

- closed：关闭状态，断路器放行所有请求，并开始统计异常比例、慢请求比例。会去判断是否达到熔断条件，这一步我们叫做「熔断策略」，达到该条件则切换到 open 状态，打开断路器。
- open：打开状态，服务调用被**熔断**，访问被熔断服务的请求会被拒绝，快速失败，直接走降级逻辑。Open 状态 5 秒后会进入 half-open 状态。
- half-open：半开状态，会一段时间放行一次请求，根据执行结果来判断接下来的操作。请求成功：则切换到 closed 状态；请求失败：则切换到 open 状态。

断路器熔断策略有三种：慢调用、异常比例、异常数

#### 慢调用

业务的响应时长（RT）大于指定时长的请求认定为慢调用请求。在指定时间内，如果请求数量超过设定的最小数量，慢调用比例大于设定的阈值，则触发熔断。

例如下图，设置 RT 超过 500ms 的调用是慢调用，统计最近 10000ms 内的请求，如果请求量超过 10 次，并且慢调用比例不低于 0.5，则触发熔断，熔断时长为 5s，然后进入 half-open 状态，放行一次请求做测试。

![](https://cdn.xn2001.com/img/2022/202205091520916.png)

**案例需求**：给 UserClient 的查询用户接口设置降级规则，慢调用的 RT 阈值为 50ms，统计时间为 1s ，最小请求数量为 5，失败阈值比例为 0.4，熔断时长为 5s；

1）设置慢调用

修改 user-service 中的 /user/{id} 这个接口的业务。通过休眠模拟一个延迟时间。

![](https://cdn.xn2001.com/img/2022/202205091538605.png)

此时，orderId = 101 的订单，关联的是 id 为 1 的用户，调用时长为 60ms

![](https://cdn.xn2001.com/img/2022/202205091520928.png)

orderId = 102 的订单，关联的是 id 为 2 的用户，调用时长为非常短

![](https://cdn.xn2001.com/img/2022/202205091520932.png)

2）设置熔断规则

下面，给 feign 接口设置降级规则，超过 50ms 的请求都会被认为是慢请求。

![](https://cdn.xn2001.com/img/2022/202205091624147.png)

![](https://cdn.xn2001.com/img/2022/202205091624535.png)

3）测试

在浏览器访问：http://localhost:8088/order/101，快速刷新5次，可以发现，触发了熔断，请求时长缩短至 5ms，快速失败了，并且走降级逻辑，返回的 null

![](https://cdn.xn2001.com/img/2022/202205091624280.png)

此时在浏览器访问：http://localhost:8088/order/102，也被熔断了

![](https://cdn.xn2001.com/img/2022/202205091625297.png)

#### 异常

**异常比例或异常数**：统计指定时间内的调用，如果调用次数超过指定请求数，并且出现异常的比例达到设定的比例阈值（或超过指定异常数），则触发熔断。

例如，异常比例设置如下，统计最近 1000ms 内的请求，如果请求量超过 10 次，并且异常比例不低于 0.4，则触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091520106.png)

异常数设置如下，统计最近 1000ms 内的请求，如果请求量超过 10 次，并且异常比例不低于 2 次，则触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091520416.png)

**案例需求**：给 UserClient 的查询用户接口设置降级规则，统计时间为 1s，最小请求数量为 5，失败阈值比例为 0.4，熔断时长为 5s

1）设置异常请求

首先，修改 user-service 中的 /user/{id} 这个接口的业务。手动抛出异常，以触发异常比例的熔断，也就是说，id 为 2时，就会触发异常。

![](https://cdn.xn2001.com/img/2022/202205091520469.png)

2）设置熔断规则

在 5 次请求中，只要异常比例超过 0.4，也就是有 2 次以上的异常，就会触发熔断。

![](https://cdn.xn2001.com/img/2022/202205091818818.png)

![](https://cdn.xn2001.com/img/2022/202205091520694.png)

3）测试

在浏览器快速访问：http://localhost:8088/order/102，快速刷新 5 次，触发熔断降级。

![](https://cdn.xn2001.com/img/2022/202205091821790.png)

此时，我们去访问本应该正常的 103，发现也被降级了。

![](https://cdn.xn2001.com/img/2022/202205091520623.png)

## 授权规则

授权规则可以对请求方来源做判断和控制。授权规则可以对调用方的来源做控制，有白名单和黑名单两种方式。

比如我们希望控制对资源 `test` 的访问设置白名单，只有来源为 `appA` 和 `appB` 的请求才可通过。

- 白名单：来源（origin）在白名单内的调用者允许访问

- 黑名单：来源（origin）在黑名单内的调用者不允许访问

点击左侧菜单的授权，可以看到授权规则

![](https://cdn.xn2001.com/img/2022/202205100008770.png)

- 资源名：就是受保护的资源，例如 /order/{orderId}

- 流控应用：是来源者的名单
  - 如果是勾选白名单，则名单中的来源被许可访问。
  - 如果是勾选黑名单，则名单中的来源被禁止访问。

比如我们允许请求从 gateway 到 order-service，不允许浏览器访问 order-service，那么白名单中就要填写**网关的来源名称（origin）**。

![](https://cdn.xn2001.com/img/2022/202205100008679.png)

Sentinel 是通过 RequestOriginParser 这个接口的 parseOrigin() 方法来获取请求的来源的。

```java
public interface RequestOriginParser {
    /**
     * 从请求request对象中获取origin，获取方式自定义
     */
    String parseOrigin(HttpServletRequest request);
}
```

这个方法的作用就是从 request 对象中，获取请求者的 origin 值并返回。默认情况下，Sentinel 不管请求者从哪里来，返回值永远是 default，也就是说一切请求的来源都被认为是一样的值 default

因此，我们需要自定义这个接口的实现，让**不同的请求，返回不同的 origin**

例如 order-service 服务中，我们定义一个 RequestOriginParser 的实现类

```java
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 1.获取请求头
        String origin = request.getHeader("origin");
        // 2.非空判断
        if (StringUtils.isEmpty(origin)) {
            origin = "blank";
        }
        return origin;
    }
}
```

我们必须让**所有从 Gateway 路由到微服务的请求都带上 origin 头**。这个需要利用之前学习的一个 GatewayFilter 来实现，使用 AddRequestHeaderGatewayFilter，修改 Gateway 服务中的 application.yml

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=origin,gateway #origin前面不能带空格
```

这样，从 Gateway 路由的所有请求都会带上 origin 头，值为 gateway。而从其它地方到达微服务的请求一般没有这个头。

接下来，我们添加一个授权规则，放行 origin 值为 gateway 的请求。

![](https://cdn.xn2001.com/img/2022/202205100104446.png)

![](https://cdn.xn2001.com/img/2022/202205100104442.png)

现在，我们直接跳过网关，访问 order-service 服务

![](https://cdn.xn2001.com/img/2022/202205100104100.png)

通过网关访问

![](https://cdn.xn2001.com/img/2022/202205100104271.png)

## 自定义异常结果

默认情况下，发生限流、降级、授权拦截时，都会抛出异常到调用方。异常结果都是 flow limmiting（限流）。这样不够友好，无法得知是限流还是降级还是授权拦截。

而如果要自定义异常时的返回结果，需要实现 BlockExceptionHandler 接口

```java
public interface BlockExceptionHandler {
    /**
     * 处理请求被限流、降级、授权拦截时抛出的异常：BlockException
     */
    void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception;
}
```

这个方法有三个参数：

- HttpServletRequest request：request 对象
- HttpServletResponse response：response 对象
- BlockException e：被 Sentinel 拦截时抛出的异常

这里的 BlockException 包含多个不同的子类

| **异常**             | **说明**           |
| -------------------- | ------------------ |
| FlowException        | 限流异常           |
| ParamFlowException   | 热点参数限流的异常 |
| DegradeException     | 降级异常           |
| AuthorityException   | 授权规则异常       |
| SystemBlockException | 系统规则异常       |

自定义异常处理，下面我们就在 order-service 定义一个自定义异常处理类

```java
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        String msg = "未知异常";
        int status = 429;

        if (e instanceof FlowException) {
            msg = "请求被限流了";
        } else if (e instanceof ParamFlowException) {
            msg = "请求被热点参数限流";
        } else if (e instanceof DegradeException) {
            msg = "请求被降级了";
        } else if (e instanceof AuthorityException) {
            msg = "没有权限访问";
            status = 401;
        }

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        response.getWriter().println("{\"msg\": " + msg + ", \"status\": " + status + "}");
    }
}
```

重启测试，在不同场景下，会返回不同的异常消息。

限流：

![](https://cdn.xn2001.com/img/2022/202205100112438.png)

授权拦截时：

![](https://cdn.xn2001.com/img/2022/202205100122438.png)

## 规则持久化

现在，Sentinel 的所有规则都是内存存储，重启后所有规则都会丢失。在生产环境下，我们必须确保这些规则的持久化，避免丢失。

规则是否能持久化，取决于规则管理模式，Sentinel 支持三种规则管理模式

- 原始模式：Sentinel 的默认模式，将规则保存在内存，重启服务会丢失。
- pull 模式
- push 模式

### pull模式

pull 模式：控制台将配置的规则推送到 Sentinel 客户端，而客户端会将配置规则保存在本地文件或数据库中。以后会定时去本地文件或数据库中查询，更新本地规则。

![](https://cdn.xn2001.com/img/2022/202205100126168.png)

### push模式

push 模式：控制台将配置规则推送到远程配置中心，例如 Nacos，Sentinel 客户端监听 Nacos，获取配置变更的推送消息，完成本地配置更新。这种模式是比较好的一种。

![](https://cdn.xn2001.com/img/2022/202205100128564.png)

我们简单了解一下这种方式，首先修改 OrderService，让其监听 Nacos 中的 Sentinel 规则配置。

在 order-service 中引入Sentinel 监听 Nacos 的依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

配置 Nacos 地址，在 order-service 中的 application.yml 文件配置 Nacos 地址及监听的配置信息

```yaml
spring:
  cloud:
    sentinel:
      datasource:
        flow:
          nacos:
            server-addr: localhost:8848 # nacos地址
            dataId: orderservice-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow # 还可以是：degrade、authority、param-flow
```

然后需要修改 sentinel-dashboard 源码，SentinelDashboard 默认不支持 Nacos 的持久化，需要修改源码。

> 相当于开源开一半，主要给阿里云用户商业使用。
>
> 以下以供了解，未实测。

解压 Sentinel 源码包

![](https://cdn.xn2001.com/img/2022/202205101404663.png)

然后并用 IDEA 打开这个项目，结构如下

![](https://cdn.xn2001.com/img/2022/202205101404666.png)

在 sentinel-dashboard 源码的pom文件中，nacos 的依赖默认的 scope 是 test，只能在测试时使用，这里要去除

![](https://cdn.xn2001.com/img/2022/202205101405160.png)

将 sentinel-datasource-nacos 依赖的 scope 去掉，改成下面这样。

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

在 sentinel-dashboard 的 test 包下，已经编写了对 nacos 的支持，我们需要将其拷贝到 main 中。

![](https://cdn.xn2001.com/img/2022/202205101405035.png)

然后，还需要修改测试代码中的 NacosConfig 类 

![](https://cdn.xn2001.com/img/2022/202205101406914.png)

修改其中的 Nacos 地址，让其读取 application.properties 中的配置

![](https://cdn.xn2001.com/img/2022/202205101404688.png)

在 sentinel-dashboard 的 application.properties 中添加 Nacos 地址配置

```properties
nacos.addr=localhost:8848
```

另外，还需要修改 `com.alibaba.csp.sentinel.dashboard.controller.v2` 包下的 FlowControllerV2

![](https://cdn.xn2001.com/img/2022/202205101404531.png)

让我们添加的 Nacos 数据源生效

![](https://cdn.xn2001.com/img/2022/202205101407560.png)

接下来，还要修改前端页面，添加一个支持 nacos 的菜单。

修改 `src/main/webapp/resources/app/scripts/directives/sidebar/` 目录下的 sidebar.html 文件

![](https://cdn.xn2001.com/img/2022/202205101404891.png)

将其中的这部分注释打开

![](https://cdn.xn2001.com/img/2022/202205101408124.png)

修改其中的文本

![](https://cdn.xn2001.com/img/2022/202205101408073.png)

运行 IDEA 中的 maven 插件，编译和打包修改好的 sentinel-dashboard

![](https://cdn.xn2001.com/img/2022/202205101408008.png)

启动方式跟官方一样

```sh
java -jar sentinel-dashboard.jar
```

如果要修改 Nacos 地址，可以添加参数

```sh
java -jar -Dnacos.addr=localhost:8848 sentinel-dashboard.jar
```

# Seata分布式事务

> 代码参考：
>
> Gitee：https://gitee.com/xn2001/cloudcode/tree/master/11-seata-demo
>
> GitHub：https://github.com/lexinhu/cloudcode/tree/master/11-seata-demo

## 分布式事务问题

> 此处可结合 MySQL 事务一起学习，可直接查看该笔记
>
> <a class="post_link" href="https://www.xn2001.com/archives/677.html"><i data-feather="file-text"></i>MySQL入门到精通？</a>

在传统数据库事务中，必须要满足四个原则，我们把他称为 ACID

![](https://cdn.xn2001.com/img/2022/202205231445816.png)

**分布式事务**，就是指不是在单个服务或单个数据库架构下，产生的事务，例如

- 跨数据源的分布式事务
- 跨服务的分布式事务
- 综合情况

在数据库水平拆分、服务垂直拆分之后，一个业务操作通常要跨多个数据库、服务才能完成。例如电商行业中比较常见的下单付款案例，包括下面几个行为：

1. 创建新订单
2. 扣减商品库存
3. 从用户账户余额扣除金额

完成上面的操作需要访问三个不同的微服务和三个不同的数据库。

![](https://cdn.xn2001.com/img/2022/202205231447738.png)

订单的创建、库存的扣减、账户扣款在每一个服务和数据库内是一个本地事务，可以保证 ACID 原则。

但是当我们把三件事情看做一个"业务"，要满足保证“业务”的原子性，要么所有操作全部成功，要么全部失败，不允许出现部分成功部分失败的现象，这就是**分布式系统下的事务**。此时 ACID 难以满足，这是分布式事务要解决的问题。

以下是一个项目演示，如下图有一个微服务项目

![](https://cdn.xn2001.com/img/2022/202205231502038.png)

其中：seata-demo 是父工程，负责管理项目依赖

- account-service：账户服务，负责管理用户的资金账户。提供扣减余额的接口
- storage-service：库存服务，负责管理商品库存。提供扣减库存的接口
- order-service：订单服务，负责管理订单。创建订单时，需要调用 account-service 和 storage-service

我们去创建订单，发送 POST 请求

```
curl --location --request POST 'http://localhost:8082/order?userId=user202103032042012&amp;commodityCode=100202003032041&amp;count=20&amp;money=200'
```

测试发现，当库存不足时，此时账户余额已经扣减，并不会回滚，出现了分布式事务问题。

![](https://cdn.xn2001.com/img/2022/202205231505539.png)

## 解决分布式事务

### CAP定理

1998年，加州大学的计算机科学家 Eric Brewer 提出，分布式系统有三个指标。

> - Consistency（一致性）
> - Availability（可用性）
> - Partition tolerance （分区容错性）

它们的第一个字母分别是 C、A、P。

Eric Brewer 说，这三个指标不可能同时做到。这个结论就叫做 CAP 定理。

![](https://cdn.xn2001.com/img/2022/202205231506789.png)

#### Consistency一致性

Consistency（一致性）：用户访问分布式系统中的任意节点，得到的数据必须一致。

比如现在包含两个节点，其中的初始数据是一致的

![](https://cdn.xn2001.com/img/2022/202205231506787.png)

当我们修改其中一个节点的数据时，两者的数据产生了差异

![](https://cdn.xn2001.com/img/2022/202205231506790.png)

要想保住一致性，就必须实现 node01 到 node02 的数据同步

![](https://cdn.xn2001.com/img/2022/202205231506801.png)



#### Availability可用性

Availability （可用性）：用户访问集群中的任意健康节点，必须能得到响应，而不是超时或拒绝。

如图，有三个节点的集群，访问任何一个都可以及时得到响应

![](https://cdn.xn2001.com/img/2022/202205231506803.png)

当有部分节点因为网络故障或其它原因无法访问时，代表节点不可用

![](https://cdn.xn2001.com/img/2022/202205231506810.png)

#### Partition Tolerance分区容错

Partition（分区）：因为网络故障或其它原因导致分布式系统中的部分节点与其它节点失去连接，形成独立分区。

Tolerance（容错）：在集群出现分区时，整个系统也要持续对外提供服务。

![](https://cdn.xn2001.com/img/2022/202205231506703.png)

在分布式系统中，系统间的网络不能 100% 保证健康，一定会有故障的时候，而服务有必须对外保证服务。**因此 Partition Tolerance 不可避免。**当节点接收到新的数据变更时，就会出现问题了。

![](https://cdn.xn2001.com/img/2022/202205231506722.png)

如果此时要保证**一致性**，就必须等待网络恢复，完成数据同步后，整个集群才对外提供服务，服务处于阻塞状态，不可用。

如果此时要保证**可用性**，就不能等待网络恢复，那 node01、node02 与 node03 之间就会出现数据不一致。

也就是说，在 P 一定会出现的情况下，A 和 C 之间只能实现一个。

### BASE理论

BASE 理论是对 CAP 的一种解决思路，包含三个思想：

- **Basically Available** **（基本可用）**：分布式系统在出现故障时，允许损失部分可用性，即保证核心可用。
- **Soft State（软状态）**：在一定时间内，允许出现中间状态，比如临时的不一致状态。
- **Eventually Consistent（最终一致性）**：虽然无法保证强一致性，但是在软状态结束后，最终达到数据一致。

分布式事务最大的问题是各个子事务的一致性问题，因此可以借鉴 CAP定理 和 BASE理论，有两种解决思路

- AP 模式：各子事务分别执行和提交，允许出现结果不一致，然后采用弥补措施恢复数据即可，实现最终一致。

- CP 模式：各个子事务执行后互相等待，同时提交，同时回滚，达成强一致。但事务等待过程中，处于弱可用状态。

> 前面我们所学的 Elasticsearch 集群就是 CP 模式，保证了数据的一致性。

解决分布式事务，各个子系统之间必须能感知彼此的事务状态，才能保证状态一致，因此需要一个事务协调者来协调每一个事务的参与者，也就是需要一个事务协调者。（TC）

另外，这里的子系统事务，称为**分支事务**；有关联的各个分支事务在一起称为**全局事务**。

![](https://cdn.xn2001.com/img/2022/202205231506031.png)

## 部署Seata

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。

官网地址：http://seata.io/，其中的文档、播客中提供了大量的使用说明、源码分析。

![](https://cdn.xn2001.com/img/2022/202205231707983.png)

### Seata的架构

Seata 事务管理中有三个重要的角色

- **TC (Transaction Coordinator) -** **事务协调者**：维护全局和分支事务的状态，协调全局事务提交或回滚。

- **TM (Transaction Manager) -** **事务管理器**：定义全局事务的范围、开始全局事务、提交或回滚全局事务。

- **RM (Resource Manager) -** **资源管理器**：管理分支事务处理的资源，与 TC 交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

![](https://cdn.xn2001.com/img/2022/202205231704030.png)

Seata 基于上述架构提供了四种不同的分布式事务解决方案

- XA 模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
- TCC 模式：最终一致的分阶段事务模式，有业务侵入
- AT 模式：最终一致的分阶段事务模式，无业务侵入，也是 Seata 的默认模式
- SAGA 模式：长事务模式，有业务侵入

无论哪种方案，都离不开 TC，也就是事务的协调者。

![](https://cdn.xn2001.com/img/2022/202205232202909.png)

### 部署TC服务

下载 seata-server 包，https://seata.io/zh-cn/blog/download.html

在非中文目录解压缩，其目录结构如下

![](https://cdn.xn2001.com/img/2022/202205241709346.png)

修改 conf 目录下的 registry.conf 文件

![](https://cdn.xn2001.com/img/2022/202205241709343.png)

```properties
registry {
  # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
  type = "nacos"

  nacos {
    # seata tc 服务注册到 nacos的服务名称，可以自定义
    application = "seata-tc-server"
    serverAddr = "127.0.0.1:8848"
    group = "DEFAULT_GROUP"
    namespace = ""
    cluster = "SH"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
  type = "nacos"
  # 配置nacos地址等信息
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "DEFAULT_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
```

为了让 TC 服务的集群可以共享配置，我们选择了 Nacos 作为统一配置中心。因此服务端配置文件 `seataServer.properties` 文件需要在Nacos 中配好。在 Nacos 后台新建一个配置文件：http://localhost:8848/nacos/

![](https://cdn.xn2001.com/img/2022/202205241709375.png)



配置内容如下

> 注意：一定要把注释删掉！要修改你的数据库信息。

```properties
# 数据存储方式，db代表数据库
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
# 这是MySQL8的驱动，MySQL5使用的是com.mysql.jdbc.Driver
store.db.driverClassName=com.mysql.cj.jdbc.Driver
# 数据库地址、用户名、密码都需要修改成你自己的数据库信息。
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;serverTimezone=GMT%2B8
store.db.user=root
store.db.password=123456
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
# 事务、日志等配置
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
# 客户端与服务端传输方式
transport.serialization=seata
transport.compressor=none
# 关闭metrics功能，提高性能
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```

TC 服务在管理分布式事务时，需要记录事务相关数据到数据库中，你需要提前创建好这些表。新建一个名为 seata 的数据库，运行 SQL

这些表主要记录全局事务、分支事务、全局锁信息。

```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `resource_group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `branch_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE,
  INDEX `idx_xid`(`xid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of branch_table
-- ----------------------------

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
  `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_service_group` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `timeout` int(11) NULL DEFAULT NULL,
  `begin_time` bigint(20) NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`xid`) USING BTREE,
  INDEX `idx_gmt_modified_status`(`gmt_modified`, `status`) USING BTREE,
  INDEX `idx_transaction_id`(`transaction_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of global_table
-- ----------------------------


-- ----------------------------
-- Records of lock_table
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

```

进入 bin 目录，运行其中的 seata-server.bat 即可。

![](https://cdn.xn2001.com/img/2022/202205241709380.png)

启动成功后，打开浏览器，访问 Nacos 地址：http://localhost:8848，然后进入服务列表页面，可以看到 seata-tc-server 的信息。

![](https://cdn.xn2001.com/img/2022/202205241709137.png)

### Seata微服务集成

> 很多人说这里很难，启动老是失败，注意检查好上一步配置是否正确，比如集群是否是 SH，分区是否是 DEFAULT_GROUP，实例名是否是 seata-tc-server

首先，我们需要在微服务中引入 Seata 依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <exclusions>
        <!--版本较低，1.3.0，因此排除-->
        <exclusion>
            <artifactId>seata-spring-boot-starter</artifactId>
            <groupId>io.seata</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!--seata starter 采用1.4.2版本-->
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>${seata.version}</version>
</dependency>
```

需要修改 application.yml 文件，添加一些配置，例如在 order-service 服务中的 application.yml，配置 TC 服务信息，通过注册中心 Nacos，结合服务名称获取 TC 地址

```yaml
seata:
  registry: # TC服务注册中心的配置，微服务根据这些信息去注册中心获取tc服务地址
    type: nacos # 注册中心类型 nacos
    nacos:
      server-addr: 127.0.0.1:8848 # nacos地址
      namespace: "" # namespace，默认为空
      group: DEFAULT_GROUP # 分组，默认是DEFAULT_GROUP
      application: seata-tc-server # seata服务名称
      username: nacos
      password: nacos
  tx-service-group: seata-demo # 事务组名称
  service:
    vgroup-mapping: # 事务组与cluster的映射关系
      seata-demo: SH
```

微服务如何根据这些配置寻找 TC 地址的，我们知道注册到 Nacos 中的微服务，确定一个具体实例需要四个信息，`namespace+group+application+cluster`

- namespace：命名空间，为空就是默认的 public
- group：分组
- application：服务名
- cluster：集群名

以上四个信息，在刚才的 yml 文件中都能找到。

![](https://cdn.xn2001.com/img/2022/202205241726339.png)

结合起来，TC 服务的信息就是：`public@DEFAULT_GROUP@seata-tc-server@SH`，这样就能确定 TC 服务集群了。然后就可以去 Nacos拉取对应的实例信息。

启动微服务后，Seata 控制台会显示连接上的服务。

![](https://cdn.xn2001.com/img/2022/202205242305852.png)

### TC服务异地容灾

1.模拟异地容灾的 TC 集群

计划启动两台 Seata 的 TC 服务节点

| 节点名称 | ip地址    | 端口号 | 集群名称 |
| -------- | --------- | ------ | -------- |
| seata    | 127.0.0.1 | 8091   | SH       |
| seata2   | 127.0.0.1 | 8092   | HZ       |

之前我们已经启动了一台 seata 服务，端口是 8091，集群名为 SH。

现在，将 seata 目录复制一份，起名为 seata2

修改 seata2/conf/registry.conf 内容如下

```nginx
registry {
  # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
  type = "nacos"

  nacos {
    # seata tc 服务注册到 nacos的服务名称，可以自定义
    application = "seata-tc-server"
    serverAddr = "127.0.0.1:8848"
    group = "DEFAULT_GROUP"
    namespace = ""
    cluster = "HZ"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
  type = "nacos"
  # 配置nacos地址等信息
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
```

进入 seata2/bin 目录，然后运行命令

```powershell
seata-server.bat -p 8092
```

打开 nacos 控制台，查看服务列表

![](https://cdn.xn2001.com/img/2022/202205241709152.png)

![](https://cdn.xn2001.com/img/2022/202205241709154.png)

2.将事务组映射配置到 nacos

接下来，我们需要将 tx-service-group 与 cluster 的映射关系都配置到 Nacos 配置中心。

新建一个配置

![](https://cdn.xn2001.com/img/2022/202205241709167.png)

配置的内容如下

```properties
# 事务组映射关系
service.vgroupMapping.seata-demo=SH
service.enableDegrade=false
service.disableGlobalTransaction=false
# 与TC服务的通信配置
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=false
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
# RM配置
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=false
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
# TM配置
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000

# undo日志配置
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
client.log.exceptionRate=100
```

3.微服务读取nacos配置

接下来，需要修改每一个微服务的 application.yml 文件，让微服务读取 Nacos 中的 client.properties 文件

```yaml
seata:
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SEATA_GROUP
      data-id: client.properties
```

重启微服务，现在微服务到底是连接 TC 的 SH 集群，还是 TC 的 HZ 集群，都统一由 Nacos 的 client.properties 来决定了。

大体先了解这么多，具体参考官方文档学习：https://seata.io/zh-cn/docs/overview/what-is-seata.html

## XA模式

XA 规范 是 X/Open 组织定义的分布式事务处理（DTP，Distributed Transaction Processing）标准，XA 规范描述了全局的 TM 与局部的 RM 之间的接口，几乎所有主流的数据库都对 XA 规范提供了支持。

XA 是规范，目前主流数据库都实现了这种规范，实现的原理都是**基于两阶段提交**。

正常情况：

![](https://cdn.xn2001.com/img/2022/202205242231750.png)

异常情况：

![](https://cdn.xn2001.com/img/2022/202205242231755.png)

一阶段：

1.事务协调者通知每个事物参与者执行本地事务

2.本地事务执行完成后报告事务执行状态给事务协调者，此时事务不提交，继续持有数据库锁

二阶段：

- 事务协调者基于一阶段的报告来判断下一步操作
  - 如果一阶段都成功，则通知所有事务参与者，提交事务
  - 如果一阶段任意一个参与者失败，则通知所有事务参与者回滚事务

Seata 对原始的 XA 模式做了简单的封装和改造，以适应自己的事务模型，基本架构如下图

![](https://cdn.xn2001.com/img/2022/202205242231760.png)

RM 一阶段的工作：

① 注册分支事务到TC

② 执行分支业务 SQL 但不提交

③ 报告执行状态到 TC

TC 二阶段的工作：

- TC 检测各分支事务执行状态

  a.如果都成功，通知所有 RM 提交事务

  b.如果有失败，通知所有 RM 回滚事务

RM 二阶段的工作：

- 接收 TC 指令，提交或回滚事务

### 优缺点

XA 模式的优点是什么？

- 事务的强一致性，满足 ACID 原则。
- 常用数据库都支持，实现简单，并且没有代码侵入。

XA 模式的缺点是什么？

- 因为一阶段需要锁定数据库资源，等待二阶段结束才释放，性能较差。
- 依赖关系型数据库实现事务。

### 实现XA模式

Seata 的 starter 已经完成了 XA 模式的自动装配，实现非常简单，步骤如下

1）修改每一个微服务的 application.yml 文件（每个参与事务的微服务），开启XA模式：

```yaml
seata:
  data-source-proxy-mode: XA
```

2）给发起全局事务的入口方法添加 `@GlobalTransactional` 注解

本例中是 OrderServiceImpl 中的 create 方法.

![](https://cdn.xn2001.com/img/2022/202205242231783.png)

3）重启服务并测试

重启 order-service，再次测试，发现无论怎样异常情况，三个微服务都能成功回滚。

## AT模式

AT 模式同样是分阶段提交的事务模型，不过缺弥补了XA模型中资源锁定周期过长的缺陷。

基本流程图：

![](https://cdn.xn2001.com/img/2022/202205242258121.png)

阶段一 RM 的工作：

- 注册分支事务
- **记录 undo-log（数据快照）**
- **执行业务 SQL 并提交事务**
- 报告事务状态

阶段二提交时 RM 的工作：

- 删除 undo-log 即可

阶段二回滚时 RM 的工作：

- 根据 undo-log 恢复数据到更新前

### 流程梳理

我们用一个真实的业务来梳理下 AT 模式的原理。

比如，现在有一个数据库表，记录用户余额

| **id** | **money** |
| ------ | --------- |
| 1      | 100       |

其中一个分支业务要执行的 SQL 为

```sql
update tb_account set money = money - 10 where id = 1
```

AT模式下，当前分支事务执行流程如下：

一阶段：

1）TM 发起并注册全局事务到 TC

2）TM 调用分支事务

3）分支事务准备执行业务 SQL

4）RM 拦截业务 SQL，根据 where 条件查询原始数据，形成快照。

```json
{
    "id": 1, "money": 100
}
```

5）RM 执行业务 SQL，提交本地事务，释放数据库锁。此时 `money = 90`

6）RM 报告本地事务状态给 TC

二阶段：

1）TM 通知 TC 事务结束

2）TC 检查分支事务状态。如果都成功，则立即删除快照；如果有分支事务失败，需要回滚。读取快照数据`{"id": 1, "money": 100}`，将快照恢复到数据库。此时数据库再次恢复为 100。

![](https://cdn.xn2001.com/img/2022/202205242258118.png)

### AT与XA的区别

简述 AT 模式与 XA 模式最大的区别是什么？

- XA 模式一阶段不提交事务，锁定资源；AT 模式一阶段直接提交，不锁定资源。
- XA 模式依赖数据库机制实现回滚；AT 模式利用数据快照实现数据回滚。
- XA 模式强一致；AT 模式最终一致。

### 脏写问题

> 注意：此处脏写是在多线程环境下的问题

在**多线程并发**访问 AT 模式的分布式事务时，有可能出现脏写问题。如图，当事务 1 因为某些原因要恢复快照时，另一个线程的事务 2 白更新了一次数据，出现了脏写问题。

![](https://cdn.xn2001.com/img/2022/202205242258122.png)

解决思路就是引入了全局锁的概念。在提交事务之前，先去拿全局锁，避免同一时刻有另外一个事务在操作当前数据，拿不到全局锁超过一定时间则回滚。如图，这样一来事务 2 就更新失败了，此时事务 1 恢复数据不会给另一个线程事务 2 造成改了又没改的离谱现象。

![](https://cdn.xn2001.com/img/2022/202205242258136.png)

### 优缺点

AT 模式的优点：

- 一阶段完成直接提交事务，释放数据库资源，性能比较好
- 利用全局锁实现读写隔离
- 没有代码侵入，框架自动完成回滚和提交

AT 模式的缺点：

- 两阶段之间属于软状态，属于最终一致
- 框架的快照功能会影响性能，但比XA模式要好很多

### 实现AT模式

AT 模式中的快照生成、回滚等动作都是由框架自动完成，没有任何代码侵入，因此实现非常简单。

只不过，AT模式需要一个表来记录全局锁、另一张表来记录数据快照 undo_log

1）导入数据库表，记录全局锁

lock_table 表导入到 TC 服务关联的数据库，我这里的 TC 服务数据库是 seata

```sql
-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
  `row_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `xid` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pk` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`row_key`) USING BTREE,
  INDEX `idx_branch_id`(`branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
```

undo_log 表导入到微服务关联的数据库，我这里的微服务数据库是 seata_demo

```sql
-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;
```

2）修改 application.yml 文件，将事务模式修改为 AT 模式即可。

```yaml
seata:
  data-source-proxy-mode: AT # 默认就是AT
```

3）重启服务并测试。

## TCC模式

TCC 模式与 AT 模式非常相似，每阶段都是独立事务，不同的是 TCC 模式通过人工编码来实现数据恢复。需要实现三个方法：

- Try：资源的检测和预留； 

- Confirm：完成资源操作业务；要求 Try 成功 Confirm 一定要能成功。

- Cancel：预留资源释放，可以理解为 try 的反向操作。

例如，一个扣减用户余额的业务。假设账户 A 原来余额是 100，需要余额扣减 30 元。

**阶段一（ Try ）**：检查余额是否充足，如果充足则冻结金额增加 30 元，可用余额扣除 30

初始余额

![](https://cdn.xn2001.com/img/2022/202205261907452.png)

余额充足，可以冻结

![](https://cdn.xn2001.com/img/2022/202205261907455.png)

此时，总金额 = 冻结金额 + 可用金额，数量依然是 100 不变。事务直接提交无需等待其它事务。

**阶段二（Confirm)**：假如要提交（Confirm），则冻结金额扣减 30

确认可以提交，不过之前可用金额已经扣减过了，这里只要清除冻结金额就好了

![](https://cdn.xn2001.com/img/2022/202205261907456.png)

此时，总金额 = 冻结金额 + 可用金额 = 0 + 70  = 70元

**阶段二(Canncel)**：如果要回滚（Cancel），则冻结金额扣减 30，可用余额增加 30

需要回滚，那么就要释放冻结金额，恢复可用金额

![](https://cdn.xn2001.com/img/2022/202205261907454.png)

Seata 中的 TCC 模型依然延续之前的事务架构，如图：

![](https://cdn.xn2001.com/img/2022/202205261907469.png)

### 优缺点

TCC 模式的每个阶段是做什么的？

- Try：资源检查和预留
- Confirm：业务执行和提交
- Cancel：预留资源的释放

TCC 的优点是什么？

- 一阶段完成直接提交事务，释放数据库资源，性能好
- 相比 AT 模型，无需生成快照，无需使用全局锁，性能最强
- 不依赖数据库事务，而是依赖补偿操作，可以用于非事务型数据库

TCC 的缺点是什么？

- 有代码侵入，需要人为编写 try、Confirm 和 Cancel 接口，太麻烦
- 软状态，事务是最终一致
- 需要考虑 Confirm 和 Cancel 的失败情况，做好幂等处理

### 空回滚

当某分支事务的 try 阶段**阻塞**时，可能导致全局事务超时而触发二阶段的 cancel 操作。在未执行 try 操作时先执行了 cancel 操作，这时cancel 不能做回滚，就是**空回滚**。执行 cancel 操作时，应当判断 try 是否已经执行，如果尚未执行，则应该空回滚。

![](https://cdn.xn2001.com/img/2022/202205261921656.png)

### 业务悬挂

空回滚后出现的一个新问题：对于已经空回滚的业务，之前被阻塞的 try 操作恢复，继续执行 try，可此时整个业务都已经结束了，难道我们可以让他再去走 confirm 或 cancel 吗，显然不行。因此事务一直处于中间状态，这就是**业务悬挂**，我们应当去避免这种情况。所以执行 try 操作时，应当判断 cancel 是否已经执行过了，如果已经执行，**应当阻止空回滚后的 try 操作，避免悬挂。**

### 实现TCC模式

#### 设计数据表

实现 TCC 模式我们都知道要去记录冻结状态，所以这就需要一个数据表。

xid：是全局事务 id

freeze_money：用来记录用户冻结金额

state：用来记录事务状态

```sql
CREATE TABLE `account_freeze_tbl` (
  `xid` varchar(128) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `freeze_money` int(11) unsigned DEFAULT '0' COMMENT '冻结金额',
  `state` int(1) DEFAULT NULL COMMENT '事务状态，0:try，1:confirm，2:cancel',
  PRIMARY KEY (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
```

- Try 业务
  - 记录冻结金额和事务状态到 account_freeze 表
  - 扣减 account 表可用金额
- Confirm 业务
  - 根据 xid 删除 account_freeze 表的冻结记录
- Cancel 业务
  - 修改 account_freeze 表，冻结金额为 0，state 为 2
  - 修改 account 表，恢复可用金额
- 如何判断是否空回滚
  - cancel 业务中，根据 xid 查询 account_freeze，如果为 null 则说明 try 还没做，需要空回滚
- 如何避免业务悬挂
  - try 业务中，根据 xid 查询 account_freeze，如果已经存在则证明 Cancel 已经执行，拒绝执行 try 业务

接下来，我们根据实际业务修改 account-service，利用 TCC 实现余额扣减功能。

#### 声明TCC接口

> Seata 全局事务的 id 可以通过 `RootContext.getXID();` 获取，
>
> 也可以通过 BusinessActionContext 参数的 getXid() 方法获取。

TCC 的 Try、Confirm、Cancel 方法都需要在接口中基于注解来声明，首先是接口上要用 `@LocalTCC`，try 逻辑方法用注解`@TwoPhaseBusinessAction(name="try 方法名", commitMethod="confirm 方法名", rollbackMethod="cancel 方法名") `注明 ，在该方法参数上加入 `@BusinessActionContextParameter(paramName="try 方法的参数")`，可以使得该参数传入 `BusinessActionContext` 类，便于 confirm 和 cancel 读取。

![](https://cdn.xn2001.com/img/2022/202205281606688.png)

我们在 account-service 项目中的 service 包中新建一个接口，声明 TCC 三个接口。

```java
@LocalTCC
public interface AccountTCCService {

    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money")int money);

    boolean confirm(BusinessActionContext ctx);

    boolean cancel(BusinessActionContext ctx);
}
```

#### 编写实现类

在 account-service 服务中的 service.impl 包下新建一个类，实现 TCC 业务

```java
@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper freezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        // 0.获取事务id
        String xid = RootContext.getXID();
        // 处理业务悬挂
        if (freezeMapper.selectById(xid) != null) {
            return;
        }
        // 1.扣减可用余额
        accountMapper.deduct(userId, money);
        // 2.记录冻结金额，事务状态
        AccountFreeze freeze = new AccountFreeze();
        freeze.setUserId(userId);
        freeze.setFreezeMoney(money);
        freeze.setState(AccountFreeze.State.TRY);
        freeze.setXid(xid);
        freezeMapper.insert(freeze);
    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        // 1.获取事务id
        String xid = ctx.getXid();
        // 2.根据id删除冻结记录
        int count = freezeMapper.deleteById(xid);
        return count == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        // 0.查询冻结记录
        String xid = ctx.getXid();
        AccountFreeze freeze = freezeMapper.selectById(xid);
        // 处理空回滚
        if (freeze == null) {
            //空回滚
            freeze = new AccountFreeze();
            String userId = ctx.getActionContext("userId").toString();
            freeze.setUserId(userId);
            freeze.setFreezeMoney(0);
            freeze.setState(AccountFreeze.State.CANCEL);
            freeze.setXid(xid);
            freezeMapper.insert(freeze);
            return true;
        }
        // 幂等判断
        if(freeze.getState() == AccountFreeze.State.CANCEL){
            // 已经处理过了cancel，无需重复
            return true;
        }
        // 1.恢复可用余额
        accountMapper.refund(freeze.getUserId(), freeze.getFreezeMoney());
        // 2.将冻结金额清零，状态改为CANCEL
        freeze.setFreezeMoney(0);
        freeze.setState(AccountFreeze.State.CANCEL);
        int count = freezeMapper.updateById(freeze);
        return count == 1;
    }
}
```

## SAGA模式

Saga 模式是 Seata 即将开源的长事务解决方案，将由蚂蚁金服主要贡献。Seata 官网对于 Saga 的指南：https://seata.io/zh-cn/docs/user/saga.html

分布式事务执行过程中，依次执行各参与者的正向操作，如果所有正向操作均执行成功，那么分布式事务提交。如果任意一个正向操作执行失败，那么分布式事务会去退回去执行前面各参与者的逆向回滚操作，回滚已提交的参与者，使分布式事务回到初始状态。

![](https://cdn.xn2001.com/img/2022/202205281708899.png)

Saga 模式也分为两个阶段

- 一阶段：直接提交本地事务
- 二阶段：成功则什么都不做；失败则通过编写补偿业务来回滚

### 优缺点

优点：

- 事务参与者可以基于事件驱动实现异步调用，吞吐高
- 一阶段直接提交事务，无锁，性能好
- 不用编写 TCC 中的三个阶段，实现简单

缺点：

- 软状态持续时间不确定，时效性差
- 没有锁，没有事务隔离，会有脏写

# Redis分布式缓存

> 陈年旧事 Redis：https://www.xn2001.com/archives/598.html

单机的 Redis 存在以下四大问题，我们将学着去解决。

![](https://cdn.xn2001.com/img/2022/202206142215082.png)

## Redis持久化

- RDB 持久化
- AOF 持久化

### RDB持久化

RDB 全称 Redis Database Backup file（Redis数据备份文件），也被叫做 Redis 数据快照。简单来说就是把内存中的所有数据都记录到磁盘中。当 Redis 实例故障重启后，从磁盘读取快照文件，恢复数据。快照文件称为 RDB 文件，**默认是保存在当前运行目录。**

RDB 持久化在四种情况下会执行

- 执行 save 命令
- 执行 bgsave 命令
- Redis 停机时
- 触发 RDB 条件时

**save 命令**

执行下面的命令，可以立即执行一次 RDB

![](https://cdn.xn2001.com/img/2022/202206142216902.png)

save 命令会导致主进程执行 RDB，这个过程中其它所有命令都会被阻塞。只有在数据迁移时可能用到。

**bgsave 命令**

下面的命令可以异步执行 RDB

![](https://cdn.xn2001.com/img/2022/202206142216906.png)

这个命令执行后会开启独立进程完成 RDB，主进程可以持续处理用户请求，不受影响。

**停机时**

Redis 停机时会执行一次 save 命令，实现 RDB 持久化。

**自动触发 RDB 条件**

Redis 内部有触发 RDB 的机制，可以在 redis.conf 文件中找到，格式如下：

```properties
# 900秒内，如果至少有1个key被修改，则执行bgsave
# save "" 则表示禁用RDB
save 900 1  
save 300 10  
save 60 10000 
```

RDB 的其它配置也可以在 redis.conf 文件中设置

```properties
# 是否压缩 ,建议不开启，压缩也会消耗cpu，磁盘的话不值钱
rdbcompression yes
# RDB文件名称
dbfilename dump.rdb  
# 文件保存的路径目录
dir ./ 
```

bgsave 开始时会 fork 主进程得到子进程，子进程共享主进程的内存数据。完成 fork 后读取内存数据并写入 RDB 文件。

fork 采用的是 copy-on-write 技术：当主进程执行读操作时，访问共享内存；当主进程执行写操作时，则会拷贝一份数据，执行写操作。

![](https://cdn.xn2001.com/img/2022/202206142216920.png)

RDB 方式 bgsave 的基本流程？

- fork主进程得到一个子进程，共享内存空间
- 子进程读取内存数据并写入新 的RDB 文件
- 用新 RDB 文件替换旧的 RDB 文件

RDB 会在什么时候自动执行？save 60 1000代表什么含义？

- 默认是服务停止时
- 代表 60s 内至少执行 1000 次修改则触发 RDB

RDB 的缺点？

- RDB 执行间隔时间长，两次 RDB 之间写入数据有丢失的风险
- fork 子进程、压缩、写出 RDB 文件都比较耗时

### AOF持久化

AOF 全称为 Append Only File（追加文件），Redis 处理的每一个写命令都会记录在 AOF 文件，可以看做是命令日志文件。

![](https://cdn.xn2001.com/img/2022/202206142216922.png)

AOF 默认是关闭的，需要修改 redis.conf 配置文件来开启 AOF

```properties
# 是否开启AOF功能，默认是no
appendonly yes
# AOF文件的名称
appendfilename "appendonly.aof"
```

AOF 的命令记录的频率也可以通过 redis.conf 文件来配

```properties
# 表示每执行一次写命令，立即记录到AOF文件
appendfsync always 
# 写命令执行完先放入AOF缓冲区，然后表示每隔1秒将缓冲区数据写到AOF文件，是默认方案
appendfsync everysec 
# 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
appendfsync no
```

三种策略对比

![](https://cdn.xn2001.com/img/2022/202206142216924.png)

**AOF文件重写**

因为是记录命令，AOF 文件会比 RDB 文件大的多。而且 AOF 会记录对同一个 key 的多次写操作，但只有最后一次写操作才有意义。通过执行 bgrewriteaof 命令，可以让 AOF 文件执行重写功能，用最少的命令达到相同效果。

![](https://cdn.xn2001.com/img/2022/202206142216934.png)

如图，AOF 原本有三个命令，但是这三个都是对 num 的操作，第二次会覆盖第一次的值，因此第一个命令记录下来没有意义。

所以重写命令后，AOF文件内容就是：`mset name jack num 666`

Redis 也会在触发阈值时自动去重写 AOF 文件。阈值也可以在 redis.conf 中配置

```properties
# AOF文件比上次文件 增长超过多少百分比则触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写 
auto-aof-rewrite-min-size 64mb 
```

RDB 和 AOF 各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会结合两者来使用。

![](https://cdn.xn2001.com/img/2022/202206142216414.png)

Redis 支持同时开启 RDB 和 AOF，在这种情况下当 Redis 重启的时候会优先载入 AOF 文件来恢复原始的数据，因为在通常情况下 AOF 文件保存的数据集要比 RDB 文件保存的数据集完整。

## Redis主从复制

单节点 Redis 的并发能力是有上限的，要进一步提高 Redis 的并发能力，就需要搭建主从集群，实现读写分离。

![](https://cdn.xn2001.com/img/2022/202206142243954.png)

### 搭建主从

> 跳过部署三个 Redis，很简单。

共包含三个节点，一个主节点，两个从节点。

这里我们在同一台虚拟机中开启 3 个 Redis 实例，模拟主从集群，信息如下：

|       IP        | PORT |  角色  |
| :-------------: | :--: | :----: |
| 192.168.150.101 | 7001 | master |
| 192.168.150.101 | 7002 | slave  |
| 192.168.150.101 | 7003 | slave  |

为了方便查看日志，我们打开 3 个 ssh 窗口，分别启动 Redis 实例，启动命令：

```sh
# 第1个
redis-server 7001/redis.conf
# 第2个
redis-server 7002/redis.conf
# 第3个
redis-server 7003/redis.conf
```

![](https://cdn.xn2001.com/img/2022/202206150213482.png)

如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

**开启主从关系**

现在三个实例还没有任何关系，要配置主从可以使用 `replicaof` 或者 `slaveof`（5.0以前）命令。

有临时和永久两种模式：

1.修改配置文件（永久生效）

在 redis.conf 中添加一行配置：```slaveof <masterip> <masterport>```

2.使用 redis-cli 客户端连接到redis服务，执行slaveof命令（重启后失效）

`slaveof <masterip> <masterport>`

>  在 5.0 以后新增命令 replicaof，与 salveof 效果一致。

这里我们为了演示方便，使用方式二。通过 redis-cli 命令连接 7002，执行下面命令

```sh
# 连接 7002
redis-cli -p 7002
# 执行slaveof
slaveof 192.168.150.101 7001
```

通过 redis-cli 命令连接 7003，执行下面命令

```sh
# 连接 7003
redis-cli -p 7003
# 执行slaveof
slaveof 192.168.150.101 7001
```

然后连接 7001 节点，查看集群状态：

```sh
# 连接 7001
redis-cli -p 7001
# 查看状态
info replication
```

![](https://cdn.xn2001.com/img/2022/202206150213485.png)

执行下列操作以测试

- 利用 redis-cli 连接7001，执行```set num 123```

- 利用 redis-cli 连接7002，执行```get num```，再执行```set num 666```

- 利用 redis-cli 连接7003，执行```get num```，再执行```set num 888```

可以发现，只有在 7001 这个 master 节点上可以执行写操作，7002 和 7003 这两个 slave 节点只能执行读操作。

### 同步原理

#### 全量同步

主从第一次建立连接时，会执行**全量同步**，将 master 节点的所有数据都拷贝给 slave 节点，流程如下

![](https://cdn.xn2001.com/img/2022/202206150222378.png)

有几个概念需要知道：

- **Replication Id**：简称 replid，是数据集的标记，id 一致则说明是同一数据集。每一个 master 都有唯一的replid，slave 则会继承 master 节点的 replid；
- **offset**：偏移量，随着记录在 repl_baklog 中的数据增多而逐渐增大。slave 完成同步时也会记录当前同步的offset，即 slave 的 offset 永远小于等于 master 的 offset；当 slave 的 offset 小于 master 的 offset，说明 slave 数据落后于 master，需要更新。

因此 slave 做数据同步，必须向 master 声明自己的 replid 和 offset，master 才可以判断到底需要同步哪些数据。而 slave 原本也是一个 master，有自己的 replid 和 offset，当第一次变成 slave，与 master 建立连接时，发送的 replid 和 offset 是自己的 replid 和 offset。master 判断发现 slave 发送来的 replid 与自己的不一致，说明这是一个全新的 slave，就知道要做全量同步了。master 会将自己的 replid 和 offset 都发送给这个 slave，slave 保存这些信息。以后 slave 的replid 就与 master 一致了。因此，**master判断一个节点是否是第一次同步的依据，就是看 replid 是否一致**。

![](https://cdn.xn2001.com/img/2022/202206150222382.png)

完整流程描述：

- slave 节点请求增量同步
- master 节点判断 replid，发现不一致，拒绝增量同步，选择全量同步
- master 将完整内存数据生成 RDB，发送 RDB 到 slave
- slave 清空本地数据，加载 master 的 RDB
- master 将 RDB 期间的命令记录在 repl_baklog，并持续将 log 中的命令发送给 slave
- slave 执行接收到的命令，保持与 master 之间的同步

#### 增量同步

全量同步需要先做 RDB，然后将 RDB 文件通过网络传输给 slave，成本太高。因此除了第一次做全量同步，其它大多数时候 slave 与 master 都是做**增量同步**。

什么是增量同步？就是只更新 slave 与 master 存在差异的部分数据。

![](https://cdn.xn2001.com/img/2022/202206150222386.png)

**repl_backlog 原理**

master 怎么知道 slave 与自己的数据差异在哪里？

这就要说到全量同步时的 repl_baklog 文件了。

这个文件是一个固定大小的数组，只不过数组是环形，也就是说**角标到达数组末尾后，会再次从 0 开始读写**，这样数组头部的数据就会被覆盖。repl_baklog 中会记录 Redis 处理过的命令日志及 offset，包括 master 当前的 offset 和 slave 已经拷贝到的 offset

![](https://cdn.xn2001.com/img/2022/202206150222384.png) 

slave 与 master 的 offset 之间的差异，就是 salve 需要增量拷贝的数据了。

随着不断有数据写入，master 的 offset 逐渐变大，slave 也不断的拷贝，追赶 master 的 offset，直到数组被填满：

![](https://cdn.xn2001.com/img/2022/202206150222403.png) 

![](https://cdn.xn2001.com/img/2022/202206150222407.png) 

此时，如果有新的数据写入，就会覆盖数组中的旧数据。不过，旧的数据只要是绿色的，说明是已经被同步到slave 的数据，即便被覆盖了也没什么影响。因为未同步的仅仅是红色部分。但是，如果 slave 出现网络阻塞，导致 master 的 offset 远远超过了 slave 的 offset，如下图

![](https://cdn.xn2001.com/img/2022/202206150222063.png) 

如果 master 继续写入新数据，其 offset 就会覆盖旧的数据，直到将 slave 现在的 offset 也覆盖了

![](https://cdn.xn2001.com/img/2022/202206150222082.png) 

棕色框中的红色部分，就是尚未同步，但是却已经被覆盖的数据。此时如果 slave 恢复，需要同步，却发现自己的 offset 都没有了，**无法完成增量同步了，只能做全量同步。**

![](https://cdn.xn2001.com/img/2022/202206150222104.png)

### 主从同步优化

主从同步可以保证主从数据的一致性，非常重要。可以从以下几个方面来优化 Redis 主从集群

- 在 master 中配置 `repl-diskless-sync yes` 启用无磁盘复制，避免全量同步时的磁盘 IO
- Redis 单节点上的内存占用不要太大，减少 RDB 导致的过多磁盘IO
- 适当提高 repl_baklog 的大小，发现 slave 宕机时尽快实现故障恢复，尽可能避免全量同步
- 限制一个 master 上的 slave 节点数量，如果实在是太多 slave，则可以采用**主-从-从**链式结构，减少 master 压力

![](https://cdn.xn2001.com/img/2022/202206150222112.png)

简述全量同步和增量同步区别？

- 全量同步：master 将完整内存数据生成 RDB，发送 RDB 到 slave。后续命令则记录在 repl_baklog，逐个发送给slave
- 增量同步：slave 提交自己的 offset 到 master，master 获取 repl_baklog 中从 offset 之后的命令给slave

什么时候执行全量同步？

- slave 节点第一次连接 master 节点时
- slave 节点断开时间太久，repl_baklog 中的 offset 已经被覆盖时

什么时候执行增量同步？

- slave 节点断开又恢复，并且在 repl_baklog 中能找到 offset 时

## Redis哨兵



## Redis分片集群