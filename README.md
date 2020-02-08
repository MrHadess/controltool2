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
```
 
##### 对象容器初始化流程
1. Framework assembly object load
2. Annotation from 'Configuration' register to context
3. Annotation from 'Bean' try load to context
4. Create dispatcher servlet


###### 由于架构设计及实现差异，部分注解功能无法完全按照Spring模式进行使用或者运行 
 