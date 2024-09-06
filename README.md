# `Sing-RPC`

## 一、项目介绍

### 1. 项目特点：



### 2.版本迭代：

当前最新版本：**1.0.2**

| 版本  |                           更新内容                           |
| :---: | :----------------------------------------------------------: |
|  0.5  |                        简易版本`RPC`                         |
| 1.0.0 | 较为完整的`RPC`,局部注册中心、多种序列化器、容错和重试机制。 |
| 1.0.2 |                       支持Yml配置方式                        |

## 二、系统设计

### 1.整体流程


### 2.详细设计

**使用流程?**

​	**生产者**使用`sing-RPC`需要开启TCP服务器，监听**消费者**的请求，调用`init()`方法的时候，会首先加载`Application.properties`中的配置，生成全局的`RpcConfig`配置对象，`SPI`机制也会加载用户或者系统在`META-INF/rpc/system or custom` 中配置的类信息，这包括序列化方式、容错机制、注册中心的实现还有重试机制等等；紧接着就会对注册中心进行初始化，然后把对应的服务放到远程注册中心和本地注册中心，这样生产者就成功启动！

​	**消费者**使用`sing-RPC` 无需开启TCP服务器，消费者只需要通过接口的`class`对象和服务名称拿到代理对象，进行调用即可。

**怎么拿到`SPI`加载的类对象?**

​	通过`SPI`加载的所有的类，都放在了一个`concurrentHashMap`中，`key`是一个顶层接口，`value`是也是一个`HashMap<className,class>`，这样通过两次哈希操作，就能拿到想要的类了。

**支持哪些序列化机制?**

* `JSON`
* `JDK`
* `kryo`
* `Hessian`

**如何解决粘包和半包的?**

​	因为实现了自定义协议，请求头中的内容和长度是固定的，将请求的数据分两次拿，拿到`header`中的内容后，在`byte`数据中对应的位置，拿到请求体的长度，然后再次向下拿去对应长度的`byte`。

**......**

## 三、技术介绍

### 1. 技术选型

#### 1.1自定义协议：

| 技术名称    | 简介                                                         | 官网链接                                                     |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `Etcd`      | `Etcd` 是一个高可用、强一致性的键值存储系统，常用于分布式系统中的配置管理、服务发现和分布式锁。 | [https://etcd.io/](https://etcd.io/)                         |
| `Vert.x`    | `Vert.x` 是一个用于构建响应式应用程序的工具包，基于事件驱动和非阻塞I/O模型，适用于构建高性能的分布式系统。 | [https://vertx.io/](https://vertx.io/)                       |
| `Hutool`    | `Hutool` 是一个Java工具包，旨在简化Java开发过程中的常用操作，提供了丰富的工具类和方法，涵盖了字符串处理、日期处理、加密解密等多个方面。 | [https://hutool.cn/](https://hutool.cn/)                     |
| `Zookeeper` | `Zookeeper` 是一个分布式的、开源的协调服务，用于分布式应用程序，提供配置维护、命名服务、分布式同步等功能。 | [https://zookeeper.apache.org/](https://zookeeper.apache.org/) |
| `Redis`     | `Redis` 是一个开源的内存数据结构存储系统，可以用作数据库、缓存和消息中间件，支持多种数据结构如字符串、哈希、列表、集合等。 | [https://redis.io/](https://redis.io/)                       |
| `Logback`   | `Logback` 是一个Java日志框架，是`Log4j`项目的后继者，提供了更高的性能和更多的功能，如自动重载配置、过滤器、异步日志记录等。 | [https://logback.qos.ch/](https://logback.qos.ch/)           |

​	本项目的自定义协议，参考了`Dubbo`的设计方案。


* 魔数：安全校验
* 版本号：保证请求和响应的一致即可
* 序列化方式：便于解码和编码
* 状态：记录响应结果
* 类型：请求、响应、心跳检测或者其他

#### 1.2 注册中心搭建

**`Etcd`搭建**

https://blog.csdn.net/qq_58804301/article/details/129985271

**`Redis`搭建**

https://blog.csdn.net/fengyuyeguirenenen/article/details/123826575

**`Zookeeper`搭建**

https://blog.csdn.net/weixin_47025166/article/details/125415538

### 2.用到的设计模式

#### 2.1 单例模式

* `Rpc`全局配置

#### 2.2工厂模式

* 序列器
* 注册中心
* 容错
* 重试

#### 2.3 装饰器模式

* 序列化和反序列化

### 3.用到的常用算法

* 一致性Hash

## 四、框架使用

### 1.`SpringBoot`应用


#### 1.1 引入依赖

```xml
<dependency>
    <groupId>com.xing</groupId>
    <artifactId>sing-rpc-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 1.2 生产者

1. 在启动类中加入`@EnableRpc`注解

   ```java
   @SpringBootApplication
   @EnableRpc
   public class ExampleSpringbootProviderApplication {
       public static void main(String[] args) {
           SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
       }
   
   }
   ```

2. 在对应的服务实现类中加入`@RpcService`注解

   ```java
   @Service
   @RpcService
   public class UserServiceImpl implements UserService {
       @Override
       public User getUser(User user) {
           System.out.println(user.getName());
           user.setName("王八羔子");
           return user;
       }
   }
   ```

#### 1.3消费者

1. 启动类加上`@EnableRpc`注解，**注意！`needServer`参数得是`false`**

   ```java
   @SpringBootApplication
   //消费端不需要开启服务器
   @EnableRpc(needServer = false)
   public class ExampleSpringbootConsumerApplication {
       public static void main(String[] args) {
           SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
       }
   }
   ```

2. 在使用到服务的使用，使用`@RpcReference`注解

   ```java
   @Service
   public class ExampleServiceImpl {
       @RpcReference
       private UserService userService;
       public void test(){
           User user = new User();
           user.setName("lv");
           user = userService.getUser(user);
           System.out.println(user.getName());
       }
   }
   ```

### 2. 非`SpringBoot`应用


#### 2.1引入依赖

```java
<dependency>
    <groupId>com.xing</groupId>
    <artifactId>sing-rpc-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 2.2 生产者

```java
public class ProviderExample2 {

    public static void main(String[] args) {
        //提供要注册的服务列表
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        //构建服务注册信息
        ServiceRegisterInfo<UserServiceImpl> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        //添加到注册列表
        serviceRegisterInfoList.add(serviceRegisterInfo);
        ProviderBootstrap.init(serviceRegisterInfoList);

    }

}
```

#### 2.3消费者

```java
public static void main(String[] args) {
    //服务初始化
    ConsumerBootstrap.init();

    //获取代理
    UserService userService = ServiceProxyFactory.getProxy(UserService.class);
    User user = new User();
    user.setName("wangxing");
    User newUser = userService.getUser(user);
    if(newUser != null){
        System.out.println(newUser.getName());
    }else{
        System.out.println("null");
    }
}
```

## 五、框架不足

- [ ] 本地注册中心、服务列表做的缓存较为简单
- [ ] 自定义协议中长度有点浪费
- [ ] 1.0版本中，只实现了`Etcd`一种注册中心
- [ ] `RPC`请求没有做安全验证
- [ ] 没有一种类似于`Nacos`的可视化面板
- [x] 1.0版本中只支持使用`properties`文件配置的方式
  - [x] 1.0.2版本已经支持yml的配置。
  - [x] 注意优先级：properties > yml > 默认，推荐properties或yml使用一种即可。
- [ ] 注册的服务不支持分区
- [ ] 缺少拦截器机制
- [ ] 容错机制单一，Fail-back，Fail-over等机制都没有实现，更复杂的还有：限流、熔断、超时控制等等