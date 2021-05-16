# socket-programming

## 基于 Java Socket API 搭建简单的 HTTP 客户端和服务器端程序

1. 不允许基于 netty 等框架，完全基于 Java Socket API 进行编写 
2. 不分区使用的 IO 模型，BIO、NIO 和 AIO 都可以 
3. 实现基础的 HTTP 请求、响应功能，具体要求如下：
   - HTTP 客户端可以发送请求报文、呈现响应报文（命令行和 GUI 都可以） 
   - HTTP 客户端对 301、302、304 的状态码做相应的处理 
   - HTTP 服务器端支持 GET 和 POST 请求 
   - HTTP 服务器端支持 200、301、302、304、404、405、500 的状态码 
   - HTTP 服务器端实现长连接 3.6 MIME 至少支持三种类型，包含一种非文本类型 
4. 基于以上的要求，实现注册，登录功能(数据无需持久化，存在内存中即可， 只需要实现注册和登录的接口，可以使用 postman 等方法模拟请求发送，无需客 户端)。

### 分析

- socket是使用TCP协议为我们建立了传输层的连接，因此我们要实现基于应用层HTTP协议的通信应用
  - 约束客户端服务端的通信内容，达到实现协议的目的
- 功能是实现**注册登录**

![时序图](https://courseimg-loopchen.oss-cn-guangzhou.aliyuncs.com/Computer_Network/project_img/%E6%97%B6%E5%BA%8F%E5%9B%BE.png)

### 

### 项目规格

- 使用BIO（可以用线程和线程池）
- 客户端采用GUI
- 无需持久化，因此重启客户端即会清空之前注册记录

