# jsidekiq
java 版后台任务，参考：sidekiq

#### 使用场景：
- 异步队列
	用例：用户注册异步发送邮件
```
@JSidekiqLabel(retry = 3,description = "异步发送注册邮件",queue = "sendMail")
public Boolean sendMail(String email)
```

- 延迟队列
	用例： 用户下单，30分钟后不付款，订单无效，库存恢复
```
	@JSidekiqLabel(retry = 3,description = "订单监控",queue = "tradeMonit",at=1800000)
	public Boolean tradeMonit(Long tradeId)
```

- JSidekiqLabel 标签解释：
	- retry：默认：3，业务执行失败后，重试的次数
	- description： 业务解释
	- queue: 默认：DEFAULT，存放的队列名称，可用于队列的隔离
	- at: 表示延迟多长时间执行

#### 极速感受
- 安装redis
- 运行io.liang.jsidekiq.Application
- dashboard:  http://localhost:8080/jsidekiq/home/index
- 异步方法模拟： http://127.0.0.1:8080/jsidekiq/dead?name=zyl
- 账户密码：admin / admin


#### 技术栈
- java
	- spring , spring boot
	- aop
	- velocity

- redis

#### 正式使用
- 下载项目maven 编译
- 生成的client.jar 放入你的项目

- 启动配置
	- spring boot 参考：application.yml
		- 根据实际情况修改 jsidekiq:configUrl信息

	-  非spring boot 项目：
		-  web.xml 配置：io.liang.jsidekiq.client.start.JsidekiqListener 注入参数：configUrl

	-  jsidekiq:configUrl 参数说明：

	```
     configUrl: http://jsidekiq?nameSpace=http://jsidekiq?nameSpace=tvtao::sidekiq&provider=springDataRedis&consumerQueue=queue1,queue2&maxTotal=2&deadMaxJob=90&deadTimeout=60000000&admin=true&adminName=zyl&adminPassword=admin
    ```

	- 参数解释：
		- nameSpace:： 命名空间，用于多个后台任务的隔离
		- provider：默认：springDataRedis，数据提供者，暂时只支持spring data redis
		- consumerQueue： 监控的队列名称，多个逗号风格，如果为空，表示监控所有队列
		- maxTotal：开启的最大消费线程数
		- deadMaxJob: 出错的任务保存的最大个数
		- deadTimeout：出错的任务超时时间，超时后删除
		- admin：true | false 是否是dashboard，是表示不运行消费者线程
		- adminName： dashboard 的账户
		- adminPassword：dashboard 的密码

	- redis 信息配置 最终需要在spring 中提供一个 StringRedisTemplate

- 同步方法设置标签标识为异步方法：
	在方法上增加标签：@JSidekiqLabel(retry = 3,description = "订单监控",queue = "tradeMonit",at=1800000)


#### 时序图：

```sequence
业务方 -> jsidekiq proxy : 调用业务代理类，aop 拦截
jsidekiq proxy -> redis : 如果使用标签，标明异步方法
redis -> jsidekiq proxy : ok
jsidekiq proxy -> 业务方 : ok
业务方 -> jsidekiq proxy : 消费者线程
jsidekiq proxy -> redis : bpop
redis -> jsidekiq proxy : 业务的meta信息
jsidekiq proxy -> 业务方 : 调用real的业务方法
```


- 延迟队列
	使用redies 的 有序队列

```sequence
业务方 -> jsidekiq : 调用方法，aop拦截
jsidekiq -> redis : zadd 类，方法，参数
redis -> jsidekiq : ok
jsidekiq -> 业务方 : ok
```


#### 改进：
- 耗时任务，在重启过程后 能够不丢失任务，进行重新启动

#### 资料：
- 后台  X-admin :  http://x.xuebingsi.com
- 图表 echarts ： http://echarts.baidu.com
- 实现参考 sidekiq： https://github.com/mperham/sidekiq
