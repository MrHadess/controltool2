# Control Tool
--------------
###### (Target JDK 1.8)
### 针对RestFull API需求定制，基于Servlet运行的IOC框架
简单且极速部署至相关项目，内置多种缺省配置，仅需极少配置项即可使框架运行
利用注解为基础，对程序进行初始化加载

将Servlet作为对象集合容器
IOC框架支持对象实例化及DL(依赖注入)，以实现对Bean进行初始化
Control支持多类型数据数据注入，以实现灵活的网络请求处理

不同Servlet将拥有独立的容器对象，以及读入的URL匹配器
##### Function
* 支持使用@Bean作为对象注入对象源
* 支持Gson，Jackson，FastJson 任意Json库作为，内置JSON序列化及反序列化机制，将会自动选取相关Lib库
* 支持外部配置文件作为参数配置注入，可使用"properties"格式的配置文件。注入参数类型支持基本类型作为诸如对象进行注入
* 支持简单的拦截器
* 支持Control控制器异常拦截层对其进行全局异常处理
* 支持手动组成Bean对象
* 支持@Autowired自动注入相关对象
* 支持@RequestBody注解实现对请求信息自动进行反序列化，注入对象


由于架构设计，当前仅支持一下方向类型：
* 对象实例化支持类使用构造构造方法进行对象注入
* Control层支持请求方法参数注入

##### How use it
###### 仅需对 web.xml 进行配置即可
```xml
<web-app>
  <servlet>
    <servlet-name>Test</servlet-name>
    <!-- 指定使用Framework -->
    <servlet-class>com.mh.controltool2.FrameworkServlet</servlet-class>
    <!-- 配置扫描 (如：扫描多个Package则使用‘;’进行分隔) -->
    <init-param>
      <param-name>ScanPkg</param-name>
      <param-value>com.mh.controltool2.sample;com.mh.controltool2.sample2</param-value>
    </init-param>
    <!-- 定义Value参数读取文件，支持定义文件名(使用当前Class运行主目录)或定义绝对路径的文件 (缺省值：controltool.properties) -->
    <init-param>
        <param-name>ScanPkg</param-name>
        <param-value>com.mh.controltool2.sample</param-value>
    </init-param>
    <!-- WEB容器启动时立刻实例化（可选） -->
    <load-on-startup>1</load-on-startup>
  </servlet>

  <!-- Define the Manager Servlet Mapping -->
  <servlet-mapping>
    <servlet-name>Test</servlet-name>
    <url-pattern>/*</url-pattern>
  </servlet-mapping>
</web-app>
```


##### 支持较多常用SpringMVC注解配置特性 如：
```
@RestController
@RequestMapping
@RequestBody
@RequestHeader
@PathVariable
@RequestParam
@Bean
@Autowired
@Configuration
@Value
```
 
##### 对象容器初始化流程
1. Framework assembly object load
2. Annotation from 'Configuration' register to context
3. Annotation from 'Bean' try load to context
4. Create dispatcher servlet


###### 由于架构设计及实现差异，部分注解功能无法完全按照Spring模式进行使用或者运行
 
##### 注解说明

###### @Bean
作用域-类，构造函数参数，方法参数
* 类-将会加载相关实例到容器，供参数注入。
```
@Bean() // 支持实例
@Bean("class name") // 可赋值Bean name，如接口类供参数注入时，使用接口类调用该实现类
```
* 构造函数参数
* 方法参数
```
@Bean() // 自动根据当前注入类注入相关实例参数
@Bean("class name") // 指定相关实现类的且拥有馆匹配的bean name
```

###### @Autowired
作用域-构造函数参数，方法参数
* 构造函数参数 - 含有该注解的构造函数，将会优先使用相关的构造函数，实例化类。当遇到无法注入的参数，将会尝试下一个含有该注解的构造函数。如果都Fail，则会检查是否存在口构造函数，如无则会中断实例化
* 方法参数注入 - 根据当前实例类作为bean name注入参数
```
void access(@Autowrid() ClassName impl) {
   // can use it impl 
}
```

###### @Value
作用域-构造函数参数，方法参数
* 构造函数参数注入，方法参数注入 - 将会读取Properties文件，注释中的value将作为key对应的参数，进行参数注入。注入参数支持基本数据类型及基本数据类型的包装类
```
@Value("key name")
```

###### @RestController
作用域-类
* 类 - 包含该注解时，该类将作为控制器

###### @RequestMapping
作用域-类，方法
* 类 - 将作为该控制器下所有访问的URL前缀，Request method 参数将会被忽略
* 方法 - 支持缺省Request method，以及指定Request method
```
@RequestMapping("/Hello")
// 默认 RequestMethod 为full，即所有类型的Request method都将转发至该方法
@RequestMapping(value = "/Hello" ,method = RequestMethod.GET)
// RequestMethod 支持GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
```

###### @RequestBody
作用域-方法参数
* 方法 - 加载Request数据流，并尝试使用Json格式反序列化至当前参数的对象

###### @RequestHeader
作用域-方法参数
* 方法 - 根据注解参数作为key，获取Request当中header参数进行注入，注入参数支持基本数据类型及基本数据类型的包装类
```
void functionName(@RequestHeader("content-type") String contentType) {}
```

###### @RequestParam
作用域-方法参数
* 方法 - 根据注解参数作为key，获取Request当中parameter参数进行注入，注入参数支持基本数据类型及基本数据类型的包装类
```
void functionName(@RequestParam("id") Integer id) {}
```

###### @PathVariable
作用域-方法参数
* 方法 - 根据注解参数作为key，获取URL当中参数进行注入，注入参数支持基本数据类型及基本数据类型的包装类。与'@RequestMapping'组合使用
```
@RequestMapping("/image/{id}")
void functionName(@PathVariable("id") String id) {}
```

###### @Configuration
作用域-类
* 类 - 配置controltool参数，需实现接口'com.mh.controltool2.config.annotation.Configurer2'。如有多个该注解，则为先到先得原则且只匹配一次，仅对注解的其中一个实现生效

  
##### URL匹配规则
内部并无Spring对URL进行评分机制，内部使用匹配模式对Request的处理实现
* 当声明RequestMapping时，如使用缺省值或指定为full，所有请求的类型都可转发至相关控制器
* 当存在多组相同URL，其RequestMethod声明除了一般类型，还有full类型时，内部则会优先匹配指定声明类型的URL，如不匹配则再转至full类型进行请求
* 当存在多组相同URL相同RequestMethod时，则只匹配第一个URL，如后面出现相同URL且重叠RequestMethod时，则不会纳入URL匹配队列中



 