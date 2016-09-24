## API基础层
提供了service层,domain层,异常处理,表单和一些帮助类。

### 跨域控制
添加了`Cors`配置，支持所有`Cors`请求，后续支持配置化。

### 异常处理
定义了几种常见异常

| 异常类型        | 含义           | 场景  |
| ------------- |:-------------:| -----:|
| ConflictStateException      | 数据状态冲突 | 用户重复操作 |
| InvalidArgumentException      | 无效的参数      |   用户数据无效 |
| OperationNotPermittedException | 不允许的操作 | 如余额不足，不能购买 |
| ServerLogicException      | 服务器逻辑错误      |   如不想给用户看到的服务器错误，如NullPointerException等 |
| UnAuthorizedException      | 未授权      |   如未登录的用户不能点赞 |

#### 异常输出
异常以HTTP code, HTTP message组成。
如`throw new UnAuthorizedException("USER_NOT_LOGIN")`返回给客户端的输出以HTTP code 401,返回体为

	{
		"errors": [
			{
				"message": "USER_NOT_LOGIN",
				"message_human": "USER_NOT_LOGIN"
			}
		]
	}

若在`i18n/message`中有映射配置，

	USER_NOT_LOGIN,请您先登录

那么输出就变成,

	{
		"errors": [
			{
				"message": "USER_NOT_LOGIN",
				"message_human": "USER_NOT_LOGIN"
			}
		]
	}

### 表单
客户端提交数据，服务端可将表单转换成指定`model`，满足大多数简单场景。

### url映射
可以读取指定`json`或者其他格式的url映射文件，用于配置第三方`API`的`url`。


### 国际化
利用`MessageSource`读取i18n目录下的`messages`配置。

### 工具类
#### DateUtils
Unix时间戳生成工具，获取当前Unix时间戳等。

