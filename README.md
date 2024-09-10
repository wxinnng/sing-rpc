# `Sing-RPC`

## 一、项目介绍

### 1. 项目特点：

**`sing-RPC`**,是一款基于**`JAVA`**的`RPC`框架，旨在解决多个远程服务之间调用的问题。作为一款`RPC`框架，它具有以下特点：

* **高可用**:基于 `Etcd` 云原生中间件实现了高可用的分布式注册中心，同时采用服务的负载均衡、出错重试、容错机制等，保证了服务的高可用。
* **高性能**:基于 `Vert.x TCP` 服务器 + 类 `Dubbo` 的紧凑型消息结构（字节数组）自实现了 `RPC `协议，提升网络传输性能。
* **可扩展**:使用工厂模式 + 单例模式简化创建和获取序列化器对象的操作。并通过扫描资源路径 + 反射自实现了 `SPI `机制，用户可通过配置的方式，指定单独的序列化器、注册中心、容错机制、过滤器等。
* **易操作**:封装了服务提供者和消费者启动类；并开发了基于注解驱动的 Spring Boot Starter，一个注解就能快速注册 Bean 为服务、以及注入服务调用代理对象,同时实现XML和Properties两种配置方式。
* **安全:**基于过滤器链实现请求的校验，`token`信息由注册中心发放，避免非法请求绕过注册中心，去请求服务。
* **分区:** 对同一服务进行分区、分版本，支持更加细颗粒度的请求。


### 2.版本迭代：

当前最新版本：**1.1.7**

| 版本  |                           更新内容                           |
| :---: | :----------------------------------------------------------: |
|  0.5  |                        简易版本`RPC`                         |
| 1.0.0 | 较为完整的`RPC`,局部注册中心、多种序列化器、容错和重试机制。 |
| 1.0.2 |                       支持Yml配置方式                        |
| 1.1.0 | 实现过滤器（请求发送前，请求接收前，支持`SPI`扩展,支持过滤链排除） |
| 1.1.5 |         实现请求安全验证，更改了两种过滤链的执行位置         |
| 1.1.7 | 服务分区（简易版）、连接异常提示、过滤器链重构（责任链模式）。 |
| 1.2.0 |                实现Zookeeper作为服务注册中心                 |

## 二、系统设计

### 1.整体流程

![image](https://github.com/user-attachments/assets/add119b5-d9f6-44ca-aca3-9d7fc2c1d53b)


### 2.详细设计

#### **使用流程?**

​	**生产者**使用`sing-RPC`需要开启TCP服务器，监听**消费者**的请求，调用`init()`方法的时候，会首先加载`Application.properties`中的配置，生成全局的`RpcConfig`配置对象，`SPI`机制也会加载用户或者系统在`META-INF/rpc/system or custom` 中配置的类信息，这包括序列化方式、容错机制、注册中心的实现还有重试机制等等；紧接着就会对注册中心进行初始化，然后把对应的服务放到远程注册中心和本地注册中心，这样生产者就成功启动！

​	**消费者**使用`sing-RPC` 无需开启TCP服务器，消费者只需要通过接口的`class`对象和服务名称拿到代理对象，进行调用即可。

#### **怎么拿到`SPI`加载的类对象?**

​	通过`SPI`加载的所有的类，都放在了一个`concurrentHashMap`中，`key`是一个顶层接口，`value`是也是一个`HashMap<className,class>`，这样通过两次哈希操作，就能拿到想要的类了。

#### **支持哪些序列化机制?**

* `JSON`
* `JDK`
* `kryo`
* `Hessian`

#### **如何解决粘包和半包的?**

​	因为实现了自定义协议，请求头中的内容和长度是固定的，将请求的数据分两次拿，拿到`header`中的内容后，在`byte`数据中对应的位置，拿到请求体的长度，然后再次向下拿去对应长度的`byte`。

#### **过滤器链的实现方式，过滤器的执行位置？**

​	考虑到过滤器链有执行类型和执行优先级，所以将消费者和生产者的过滤链分别放到了两个优先队列中：

```java
private static final PriorityQueue<Filter> CONSUMER_FILTER_SET = new PriorityQueue<>(Comparator.comparingInt(Filter::getOrder));

private static final PriorityQueue<Filter> PROVIDER_FILTER_SET = new PriorityQueue<>(Comparator.comparingInt(Filter::getOrder));
```

可以看到这里的排序是根据：Filter的一个方法`getOrder`,返回值是`Integer`,值越小，越优先执行。

​	**过滤器的加载**，是在`FilterComponent`的静态代码块里，首先会通过`SPI`机制，获得所有的配置的`Filter`,

```java
SpiLoader.load(Filter.class);
```

然后，从全局配置中拿到配置的排除掉的过滤器：

```java
Set<String> filterExclusionSet = RpcApplication.getRpcConfig().getFilterExclusionSet();
```

最后就是放到两个队列中(根据不同的类型)：

```java
for(String key: allClazzByClassName.keySet()){
    if(! filterExclusionSet.contains(key)){

        //创建实例
        Filter filter = FilterFactory.getInstance(key);

        if(Objects.equals(filter.getType(), FilterKeys.CONSUMER_FILTER)){
            CONSUMER_FILTER_SET.add(filter);
        }else if(Objects.equals(filter.getType(),FilterKeys.PROVIDER_FILTER)){
            PROVIDER_FILTER_SET.add(filter);
        }else{
            PROVIDER_FILTER_SET.add(filter);
            CONSUMER_FILTER_SET.add(filter);
        }
    }
```

​	**执行位置:**

* 消费者的过滤器：在代理对象中，请求发送前执行。
* 生产者的过滤器：在请求处理，拿到RpcRequest，但是还没有执行业务逻辑代码的时候。
* 最后一种过滤器，这两个位置都会被执行。

​	**请求安全校验**

​	请求安全校验就是基于过滤器，流程如图：

![image](https://github.com/user-attachments/assets/4b8513a8-9704-488c-b8b7-dd8b99e0b99d)



#### 服务的分区与版本？

![image](https://github.com/user-attachments/assets/798e347f-d7bc-44dc-97e9-7c139a87a82e)



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

**`Redis`搭建**（为实现Redis的注册中心）

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
* 过滤器链

#### 2.3 装饰器模式

* 序列化和反序列化

#### 2.4 责任链模式

* 过滤器链

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

## 五、框架扩展

- [ ] 本地注册中心、服务列表做的缓存较为简单
- [ ] 自定义协议中长度有点浪费
- [ ] 1.0版本中，只实现了`Etcd`一种注册中心
- [x] `RPC`请求没有做安全验证
  - [x] 在1.1.5版本中，实现了`RPC`请求参数的验证。
    - [x] 可以在配置中通过`identify=true`，来开启配置
    - [x] 通过服务中心的请求会自动携带上，通过请求；不经过服务中心的请求，则不会通过请求。
    - [x] 安全校验是基于过滤器来做的。
- [ ] 没有一种类似于`Nacos`的可视化面板
- [x] 1.0版本中只支持使用`properties`文件配置的方式
  - [x] 1.0.2版本已经支持yml的配置。
  - [x] 注意优先级：properties > yml > 默认，推荐properties或yml使用一种即可。
- [x] 注册的服务不支持分区
  - [x] 1.1.7版本实现分区
- [x] 缺少拦截器机制
  - [x] 在1.1.0版本中，实现了过滤器功能
    - [x] 过滤器一共分为三类：
      - [x] `ConsumerContextFilter` (用户请求发送前)
      - [x] `ProviderContextFilter()`
      - [x] `MinorFilter` （两端都执行的过滤器，优先级最低）
    - [x] 可以通过SPI机制，进行扩展，要实现Filter接口，实现:doFilter ，getOrder,getType三个方法。
    - [x] 支持优先级，order值越小，优先调用
    - [x] 支持过滤链排除
  - [x] 1.1.5 版本中，修改了过滤器链执行的位置。
    - [x] 消费端：在请求发送前，代理对象中执行。
    - [x] 服务端：请求到达服务器、业务代码执行前。
    - [x] 先前都是在代理对象中执行，如果不通过注册中心，直接访问服务端，会绕过所有的过滤器。
  - [x] 1.1.7版本中，重构了过滤器链，使用责任链模式。
- [ ] 容错机制单一，Fail-back，Fail-over等机制都没有实现，更复杂的还有：限流、熔断、超时控制等等