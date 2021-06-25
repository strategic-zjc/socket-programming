# HttpClient实验报告

## 功能列表

1. 基本的客户端收发功能（使用API接口调用和使用CommandLine两种形式展示）
2. 客户端keep-alive支持
3. 301，302，304状态码支持
4. 文本类型文件接收与显示支持
5. 任意类型body保存为文件支持，从文件中读入body支持
6. 特殊格式body的解析与保存（以下载百度站点为例）
7. 实现了不同级别的日志保存与显示，便于观察客户端行为
8. 客户端存储隔离，可以在使用api调用时启动多个客户端，从而隔离客户端行为

## 功能展示

### 基本客户端收发功能

```
GET / HTTP/1.1
Host: ditu.yjdy.org

```

上面是一个基本的http Get请求，我们可以使用命令行模式直接输入上面的内容，按下回车便可以发送请求，并接收完整的响应文本

同样，对于一个基本的http Post 请求，也可以这样使用

下面是服务端实现的注册登录功能

```
POST /login HTTP/1.1
Host: localhost:5000
Content-Type: application/x-www-form-urlencoded
Content-Length: 30

username=admin&password=123456
```

控制台客户端如果不手动停止会一直接收输入

在输入请求后，客户端会对请求的合法性做出一定检查，该选项针对api调用和控制台输入都生效，这是因为使用控制台标准输入输出和使用API调用复用了同一套代码，所以他们的行为是一致的。

对于api请求，一般的格式是这样的

```java
RequsetLine requsetLine = new RequsetLine(Method.GET,"/");//创建请求方法行，指明请求的地址和方法
MessageHeader messageHeader = new MessageHeader();//创建请求头，并向请求头中添加内容
messageHeader.put(Header.Host,"www.baidu.com");
messageHeader.put(Header.Accept,"*/*");
messageHeader.put(Header.Connection,"keep-alive");
messageHeader.put(Header.Accept_Encoding,"gzip, deflate, br");
MessageBody messageBody= new MessageBody();//创建请求体，向请求体中添加信息
HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader,messageBody);
Client client = new Client();
try {
	HttpResponse httpResponse =client.sendHttpRequest(httpRequest);//发送请求，接收response
	httpResponse.saveBody("./123.html");//将收到的数据保存在本地
} catch (Exception e) {
	e.printStackTrace();//处理异常
}
```

客户端基于```maven```构建，可以方便地被集成到其他项目之中，也可以直接在控制台使用

### 客户端keep-alive支持

下面的内容将使用java api调用测试来展示

我们连续调用6次同一请求，其中前三次不携带keep-alive头，后三次携带keep-alive头,发现前三次请求时都会创建新的连接，第四次也会创建，第五次和第六次则不会创建。

### 301,302,304

#### 301

服务端地址```localhost:5000/movedPic.png```已经永久变更为了```localhost:5000/pic.png```,我们尝试进行两次请求

发现第二次请求时不会请求```movedPic.png```,而是直接请求```pic.png```

#### 302

服务端地址```localhost:5000/movedIndex2.html```已经临时变更为了```localhost:5000/index.html```,我们尝试进行两次请求

发现第二次请求时仍然会请求```movedIndex2.html```,接收到302码进行重定向

#### 304

```
GET /images/zl_bg5.png HTTP/1.1
Host: www.historychina.net

```

这个网站实现了304重定向，可以对客户端行为进行观察。

### 特殊格式

使用Get请求访问baidu.com时，当携带```Accept-Encoding: gzip, deflate, br```时，百度默认会使用gzip的压缩方式以chunk的格式进行传输，client实现了对这一特殊格式的解析。

经过多次测试，在传输中遇到网络缓慢也能正常完成传输。

### 日志

客户端实现了DETAIL（显示包括完整报文在内的所有信息），INFO（显示一般日志信息），WARNING（显示警告信息），ERROR（显示错误信息）四个级别，便于使用与故障检查。
