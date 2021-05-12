# JAVA中BIO、NIO、AIO的分析理解

[TOC]

**简介：** 本文分析阻塞、非阻塞、同步和异步概念上的区别以及各种IO模型的操作流程，同时分析BIO、 NIO、 AIO的通信机制，并通过demo深入比较三种IO的优缺点。输入输出（IO）是指计算机同任何外部设备之间的数据传递。常见的输入输出设备有文件、键盘、打印机、屏幕等。数据可以按记录（或称数据块）的方式传递，也可以 流的方式传递 。所谓记录，是指有着内部结构的数据块。记录内部除了有需要处理的实际数据之外，还可能包含附加信息，这些附加信息通常是对本记录数据的描述。

# 理解 IO

> 输入输出（IO）是指计算机同任何外部设备之间的数据传递。常见的输入输出设备有文件、键盘、打印机、屏幕等。数据可以按记录（或称数据块）的方式传递，也可以 流的方式传递 。
>
> 所谓记录，是指有着内部结构的数据块。记录内部除了有需要处理的实际数据之外，还可能包含附加信息，这些附加信息通常是对本记录数据的描述。

## 同步和异步

### 同步

- 概念：指的是用户进程触发 IO 操作并等待或者轮询的去查看 IO 操作是否就绪。
- 例子：每天要吃饭，下班后自己跑去菜市场买菜，回来在做饭，所有的事情都是自己一件接着一件执行的。

### 异步

- 概念：异步是指用户进程触发IO操作以后便开始做自己的事情，而当 IO 操作已经完成的时候会得到 IO 完成的通知（异步的特点就是通知）。
- 例子：在某超市网上APP上选中食材后下单，委托快递员送菜上门，超市给你备货，快递员送货，而你在他们备货送菜的阶段还可以做其他事情。（使用异步 IO 时，Java 将 IO 读写委托给 OS 处理，需要将数据缓冲区地址和大小传给 OS ）。

### 区别

IO 操作主要分为两个步骤，即发起 IO 请求和实际 IO 操作，同步与异步的区别就在于第二个步骤是否阻塞。

若实际 IO 操作阻塞请求进程，即请求进程需要等待或者轮询查看 IO 操作是否就绪，则为同步 IO；若实际 IO 操作并不阻塞请求进程，而是由操作系统来进行实际 IO 操作并将结果返回，则为异步 IO。

计算把内存分为用户内存和系统内存两部分，同步和异步是针对应用程序(用户内存)和内核(系统内存)的交互而言的。

## 阻塞和非阻塞

### 阻塞

- 概念：所谓阻塞方式就是指，当视图对文件描述符或者网络套接字进行读写时，如果当时没有东西可读，或者暂时不可写，程序就进入等待状态，直到有东西读或者写。
- 例子：去公交站充值，发现这个时候，充值员不在（可能上厕所去了），然后我们就在这里等待，一直等到充值员回来为止。

### 非阻塞

- 概念：所谓的非阻塞方式就是指，当视图对文件描述符或者网络套接字进行读写时，如果没有东西可读，或者不可写，读写函数马上返回，无须等待。
- 例子：银行里取款办业务时，领取一张小票，领取完后我们自己可以玩玩手机，或者与别人聊聊天，当轮我们时，银行的喇叭会通知，这时候我们就可以去了。

### 区别

IO 操作主要分为两个步骤，即发起 IO 请求和实际 IO 操作，阻塞与非阻塞的区别就在于第一个步骤是否阻塞。

**若发起 IO 请求后请求线程一直等待实际 IO 操作完成，则为阻塞 IO；若发起 IO 请求后请求线程返回而不会一直等待，即为非阻塞 IO。**

**阻塞和非阻塞是针对于进程在访问数据的时候，根据 IO 操作的就绪状态来采取的不同方式，说白了是一种读取或者写入操作函数的实现方式，阻塞方式下读取或者写入函数将一直等待，而非阻塞方式下，读取或者写入函数会立即返回一个状态值。**

## 同步阻塞和同步非阻塞

### 同步阻塞（JAVA BIO）

同步并阻塞 IO，服务器实现模式一个连接一个线程，即客户端有连接请求时服务端就需要启动一个线程进行处理，如果这个连接不做任何事情，就会造成不必要的线程开销，当然可以通过线迟（Thread-Pool）程机制改善。



![img](https://raw.githubusercontent.com/joyven/draw.io/master/%E5%90%8C%E6%AD%A5%E9%98%BB%E5%A1%9E.png)



图1 同步阻塞

Note: linux 内核中通常基于 Netlink 协议簇实现用户空间和内核空间的通讯。这里的系统调用指代是`recvfrom`过程，其实现逻辑大致如下：



![img](https://images0.cnblogs.com/blog/479389/201402/202305259961651.jpg)



图2 Netlink协议簇用户态和内核态通讯流程 图片来源：https://www.cnblogs.com/mosp/p/3558506.html

● 用户空间：

创建流程大体如下：

- ① 创建 socket 套接字
- ② 调用 bind 函数完成地址的绑定，不过同通常意义下 server 端的绑- 定还是存在一定的差别的，server端通常绑定某个端口或者地址，而此处的绑定则是 将 socket 套接口与本进程的 pid 进行绑定 ；
- ③ 通过 sendto 或者sendmsg函数发送消息；
- ④ 通过 recvfrom 或者 rcvmsg 函数接受消息。

● 内核空间：

内核空间主要完成以下三方面的工作：

- ① 创建 netlinksocket，并注册回调函数，注册函数将会在有消息到达 netlinksocket 时会执行；
- ② 根据用户请求类型，处理用户空间的数据；
- ③ 将数据发送回用户。

### 同步非阻塞（JAVA NIO）

同步非阻塞，服务器实现模式一个请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有 I/O 请求时才启动一个线程处理。用户进程也需要时不时地询问IO操作是否就绪，这就要求用户进程不停的去询问。



![img](https://raw.githubusercontent.com/joyven/draw.io/master/%E5%90%8C%E6%AD%A5%E9%9D%9E%E9%98%BB%E5%A1%9E.png)



图3 同步非阻塞

## 异步阻塞和异步非阻塞

### 异步阻塞（Java NIO）

异步阻塞，应用发起一个 IO 操作以后，不需要等待内核 IO 操作完成，等待内核完成 IO 操作以后会通知应用程序，这其实就是异步和同步的关键区别，同步必须等待或者主动去询问 IO 操作是否完成。那为什么说阻塞呢？因为此时是通过 select 系统调用来完成的，而 select 函数本身的实现方式就是阻塞的，但采用 select 函数有个好处就是它可以同时监听多个文件句柄（如果从UNP的角度看，select 属于同步操作。因为 select 之后，进程还需要读写数据），从而提高系统的并发性。



![img](https://raw.githubusercontent.com/joyven/draw.io/master/%E5%BC%82%E6%AD%A5%E9%98%BB%E5%A1%9E.png)



图4 异步阻塞

### 异步非阻塞（Java AIO）

异步非阻塞，此种方式下，用户进程只需要发起一个IO操作便立即返回，等 IO 操作真正完成以后，应用程序会得到IO操作完成的通知，此时用户进程只需要对数据处理就好了，不需要进行实际的 IO 读写操作，因为真正的 IO 操作已经由操作系统内核完成了。



![img](https://raw.githubusercontent.com/joyven/draw.io/master/%E5%BC%82%E6%AD%A5%E9%9D%9E%E9%98%BB%E5%A1%9E.png)



图5 异步非阻塞

# BIO、NIO、AIO适用场景分析

BIO 适用于连接数目比较小且固定的结构。它对服务器资源要求比较高，并发局限于应用中，JDK1.4之前唯一选择，但程序直观简单易理解，如之前在 Apache 中使用。

NIO 适用于连接数目多且连接比较短的架构，比如聊天服务器，并发局限于应用中，变成比较复杂。JDK1.4开始支持，如在 Nginx、Netty 中使用。

AIO 适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用 OS 参与并发操作，编程比较复杂，JDK7 开始支持，在成长中，Netty 曾经使用过，后来放弃。

> 在大多数场景下，不建议直接使用 JDK 的 NIO 类库（门槛很高），除非精通 NIO 编程或者有特殊的需求。在绝大多数的业务场景中，可以使用 NIO 框架 Netty 来进行 NIO 编程，其既可以作为客户端也可以作为服务端，且支持 UDP 和异步文件传输，功能非常强大。
>
> NIO 比 BIO 把一些无效的连接挡在了启动线程之前，减少了这部分资源的浪费。因为我们都知道每创建一个线程，就要为这个线程分配一定的内存空间。
>
> AIO 比 NIO 进一步改善是，将一些暂时可能无效的请求挡在了启动线程之前，比如在 NIO 的处理方式中，当一个请求来的话，开启线程进行处理，但这个请求所需要的资源还没有就绪，此时必须等待后端的应用资源，这时线程就被阻塞了。

# Java对BIO、NIO、AIO的支持

Java IO（Java 数据流）主要就是 Java 用来读取和输出数据流。它有对应的一系列API。主要是 `java.io.*` 和 `java.nio.*`。

Java 中 IO 主要有两类：字节流（读写以字节（8bit）为单位，InputStream 和 OutputStream 为主要代表；字符流（读写以字符为单位，Reader 和 Writer 为主要代表） 。

读取纯文本数据优选用字符流，其他使用字节流。因为字符流是以虚拟机的 encode 来处理，一次能读多个字节；而图片和视频类的文件是根据二进制进行存储，当用字符流处理的时候有些会找不到映射的码表，会造成数据缺失。下面介绍下经常用到的io类：

- 基本流:从特定的地方读写的流类，如磁盘或一块内存区域。既有来源。

FileInputStream/FileOutputStream(文件字节输出流)、

- 处理流（高级流，过滤流）：没有数据源，不能独立存在，它的存在是用于处理基本流的，是使用一个已经存在的输入流或输出流连接创建。

1. 缓冲字节高级流：BufferedOutputStream/BufferdInputStream，内部维护者一个缓冲区，每次都尽可能的读取更多的字节放入到缓冲区，再将缓冲区中的内容部分或全部返回给用户，因此可以提高读写效率。
2. 基本数据类型高级流：DataOutputStream/DataInputStream
3. 字符高级流：OutputStreamWriter/InputStreamReader
4. 缓冲字符高级流：BufferWriter/BufferReader
5. 文件字符高级流：FileWriter/FileReader，用于读写“文本文件”

# BIO、NIO、AIO 通信机制

## BIO 编程模型

采用 BIO 通信模型的服务端，通常有一个独立的 Acceptor 线程负责监听客户端的连接，它接收到客户端的连接请求之后，为每个客户端创建一个新的线程进行链路处理，处理完之后，通过输出流返回应答客户端，线程销毁。这就是典型的`一请求一应答`通信模型。这个是在多线程情况下执行的。当在单线程环境条件下时，在 while 循环中服务端会调用 accept 方法等待接收客户端的连接请求，一旦收到这个连接请求，就可以建立 socket，并在 socket 上进行读写操作，此时不能再接收其他客户端的连接请求，只能等待同当前服务端连接的客户端的操作完成或者连接断开。

该模型最大的问题就是缺乏弹性伸缩能力，当客户端并发访问量增加后，服务端的线程个数和客户端并发访问数呈 1:1 的正比关系，由于线程是 Java 虚拟机非常宝贵的系统资源，当线程数膨胀之后，系统的性能将急剧下降，随着并发访问量的继续增大，系统会发生线程堆栈溢出、创建新线程失败等问题，并最终导致进程宕机或者僵死，不能对外提供服务。

## 伪异步 I/O 编程

为了解决同步阻塞I/O面临的一个链路需要一个线程处理的问题，后来有人对它的线程模型进行了优化，后端通过一个线程池来处理多个客户端的请求接入，形成客户端个数M：线程池最大线程数N的比例关系，其中M可以远远大于N，通过线程池可以灵活的调配线程资源。设置线程的最大值，防止由于海量并发接入导致线程耗尽。
采用线程池和任务队列可以实现一种叫做伪异步的I/O通信框架。

当有新的客户端接入时，将客户端的 Socket 封装成一个 Task(该任务实现 Java.lang.Runnablle 接口)投递到后端的线程池中进行处理，JDK 的线程池维护一个消息队列和N个活跃线程对消息队列中的任务进行处理。由于线程池可以设置消息队列的大小和最大线程数，因此，它的资源占用是可控的，无论多少个客户端并发访问，都不会导致资源的耗尽和宕机。

由于线程池和消息队列都是有界的，因此，无论客户端并发连接数多大，它都不会导致线程个数过于膨胀或者内存溢出，相对于传统的一连接一线程模型，是一种改良。

伪异步I/O通信框架采用了线程池实现，因此避免了为每个请求都创建一个独立线程造成的线程资源耗尽问题。但是由于它底层的通信依然采用同步阻塞模型，因此无法从根本上解决问题。

通过对输入和输出流的 API 文档进行分析，我们了解到读和写操作都是同步阻塞的，阻塞的时间取决于对方 IO 线程的处理速度和网络 IO 的传输速度，本质上讲，我们无法保证生产环境的网络状况和对端的应用程序能足够快，如果我们的应用程序依赖对方的处理速度，它的可靠性就会非常差。

## NIO编程模型

与 Socket 类和 ServerSocket 类对应，NIO 也提供了 SocketChannel 和 ServerSocketChannel 两种不同的套接字通道实现，在 JDK1.4 中引入。这两种新增的通道都支持阻塞和非阻塞两种模式。阻塞模式非常简单，但性能和可靠性都不好，非阻塞模式正好相反。我们可以根据自己的需求来选择合适的模式，一般来说，低负载、低并发的应用程序可以选择同步阻塞 IO 以降低编程复杂度，但是对于高负载、高并发的网络应用，需要使用 NIO 的非阻塞模式进行开发。

- (1)缓冲区 Buffer

Buffer 是一个对象，它包含一些要写入或者要读出的数据，在 NIO 库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的；在写入数据时，写入到缓冲区中，任何时候访问 NIO 中的数据，都是通过缓冲区进行操作。
缓冲区实质上是一个数组。通常它是一个字节数组(ByteBuffer)，也可以使用其他种类的数组，但是一个缓冲区不仅仅是一个数组，缓冲区提供了对数据的结构化访问以及维护读写位置(limit)等信息。常用的有ByteBuffer，其它还有CharBuffer、ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。

- (2)通道 Channel

Channel 是一个通道，可以通过它读取和写入数据，它就像自来水管一样，网络数据通过 Channel 读取和写入。通道与流的不同之处在于通道是双向的，流只是一个方向上移动(一个流必须是 InputStream 或者 OutputStream 的子类)，而且通道可以用于读、写或者用于读写。同时Channel 是全双工的，因此它可以比流更好的映射底层操作系统的API。特别是在Unix网络编程中，底层操作系统的通道都是全双工的，同时支持读写操作。我们常用到的 ServerSocketChannnel 和 SocketChannel 都是SelectableChannel 的子类。

- (3)多路复用器Selector

多路复用器 Selector 是 Java NIO 编程的基础，多路复用器提供选择已经就绪的任务的能力，简单的说，Selector 会不断的轮询注册在其上的 Channel，如果某个 Channel 上面有新的 TCP 连接接入、读和写事件，这个 Channel 就处于就绪状态，会被 Selector 轮询出来，然后通过 SelectionKey 可以获取就绪 Channel 的集合，进行后续的 I/O 操作。

一个多用复用器 Selector 可以同时轮询多个 Channel，由于 JDK 使用了 epoll() 代替传统的 select() 实现，所以它并没有最大连接句柄 1024/2048 的限制，这也意味着只需要一个线程负责 Selector 的轮询，就可以接入成千上万的客户端。

尽管 NIO 编程难度确实比同步阻塞 BIO 大很多，但是我们要考虑到它的优点：

(1)客户端发起的连接操作是异步的，可以通过在多路复用器注册 OP_CONNECT 等后续结果，不需要像之前的客户端那样被同步阻塞。

(2)SocketChannel 的读写操作都是异步的，如果没有可读写的数据它不会同步等待，直接返回，这样IO通信线程就可以处理其它的链路，不需要同步等待这个链路可用。

(3)线程模型的优化：由于 JDK 的 Selector 在 Linux 等主流操作系统上通过 epoll 实现，它没有连接句柄数的限制(只受限于操作系统的最大句柄数或者对单个进程的句柄限制)，这意味着一个 Selector 线程可以同时处理成千上万个客户端连接，而且性能不会随着客户端的增加而线性下降，因此，它非常适合做高性能、高负载的网络服务器。

## AIO编程模型

JDK1.7 升级了 NIO 类库，升级后的 NIO 类库被称为NIO2.0。也就是我们要介绍的 AIO。NIO2.0 引入了新的异步通道的概念，并提供了异步文件通道和异步套接字通道的实现。异步通道提供两种方式获取操作结果。

(1)通过 `Java.util.concurrent.Future` 类来表示异步操作的结果；

(2)在执行异步操作的时候传入一个`Java.nio.channels.CompletionHandler`接口的实现类作为操作完成的回调。

NIO2.0 的异步套接字通道是真正的异步非阻塞 IO，它对应 UNIX 网络编程中的事件驱动 IO(AIO)，它不需要通过多路复用器(Selector)对注册的通道进行轮询操作即可实现异步读写，从而简化了 NIO 的编程模型。

我们可以得出结论：异步 Socket Channel是被动执行对象，我们不需要想NIO编程那样创建一个独立的IO线程来处理读写操作。对于`AsynchronousServerSocketChannel`和`AsynchronousSocketChannel`，它们都由 JDK 底层的线程池负责回调并驱动读写操作。正因为如此，基于 NIO2.0 新的异步非阻塞 Channel 进行编程比 NIO 编程更为简单。

# BIO、NIO和AIO使用样例

## BIO Demo

- 原始BIO

```java
package com.openmind.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:14
 * @desc
 */
public class BIODemo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            InputStream is = socket.getInputStream();
            byte[] data = new byte[1024];
            is.read(data);

            System.out.println(new String(data, UTF_8));
            OutputStream out = socket.getOutputStream();
            out.write(data);
            socket.close();
        }
    }
}
```

- 新线程处理客户端读写

```java
package com.openmind.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:19
 * @desc
 */
public class NewThreadBIODemo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            final Socket clientSocket = socket;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = clientSocket.getInputStream();
                        byte[] data = new byte[1024];
                        is.read(data);

                        OutputStream out = clientSocket.getOutputStream();
                        out.write(data);
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
```

- 线程池处理客户端读写

```java
package com.openmind.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:25
 * @desc
 */
public class ThreadPoolBIODemo {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;

        while ((socket = serverSocket.accept()) != null) {
            final Socket clientSocket = socket;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = clientSocket.getInputStream();
                        byte[]  data = new byte[1024];
                        is.read(data);

                        OutputStream out = clientSocket.getOutputStream();
                        out.write(data);
                        clientSocket.close();
                    } catch (Exception e) {
                        ;
                    }
                }
            });
        }
    }
}
```

## NIO Demo

```java
package com.openmind.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:54
 * @desc
 */
public class NIODemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    int read = clientChannel.read(buffer);

                    if (read == -1) {
                        key.cancel();
                        clientChannel.close();
                    } else {
                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                }
            }
            iterator.remove();
        }
    }
}
```

## AIO Demo

```java
package com.openmind.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AIO服务端
 *
 * @author zhoujunwen
 * @date 2019-11-08
 * @time 17:50
 * @desc
 */
public class AIO_Server {
    static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws InterruptedException {
        int port = 7890;

        new Thread(new AioServer(port)).start();
        TimeUnit.MINUTES.sleep(60);
    }

    static class AioServer implements Runnable {

        int port;
        AsynchronousChannelGroup group;
        AsynchronousServerSocketChannel serverSocketChannel;

        public AioServer(int port) {
            this.port = port;
            init();
        }

        public void init() {
            try {
                // 创建处理线程池
                group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 5);
                // 创建服务channel
                serverSocketChannel = AsynchronousServerSocketChannel.open(group);
                // 丙丁端口
                serverSocketChannel.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 接收请求
            // accept的第一个参数附件，第二个参数是收到请求后的接收处理器
            // 接收处理器AcceptHandler泛型的第一个参数的处理结果，这里是AsynchronousSocketChannel，即接收到的请求的channel
            // 第二个参数是附件，这里是AioServer，即其实例
            serverSocketChannel.accept(this, new AcceptHandler());
        }
    }

    /**
     * 接收请求处理器
     * completionHandler泛型的第一个参数的处理结果，这里是AsynchronousSocketChannel，即接收到的请求的channel，
     * 第二个参数是附件，这里是AioServer，即其实例
     */
    static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

        @Override
        public void completed(AsynchronousSocketChannel result, AioServer attachment) {
            // 继续接收下一个请求，构成循环调用
            attachment.serverSocketChannel.accept(attachment, this);

            try {
                System.out.println("接收到连接请求：" + result.getRemoteAddress().toString());

                // 定义数据读取缓存
                ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
                // 读取数据，并传入数据到达时的处理器
                // read的第一个参数数据读取到目标缓存，第二个参数是附件，第三个传输的读取结束后的处理器
                // 读取处理器泛型的第一个参数是读取的字节数，第二个参数输附件对象
                result.read(buffer, buffer, new ReadHandler(result));

                // 新开新城发送数据
                new Thread(new WriteThread(result)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AioServer attachment) {

        }
    }

    /**
     * 读取数据处理器
     * completionHandler第一个参数是读取的字节数，第二个参数输附件对象
     */
    static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel socketChannel;

        public ReadHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (result == -1) {
                attachment.clear();
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            attachment.flip();
            String readMsg = charset.decode(attachment).toString();
            System.out.println("服务端接收到的数据：" + readMsg);
            attachment.compact();

            // 继续接收数据，构成循环
            socketChannel.read(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }

    /**
     * 写出数据处理器
     */
    static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel socketChannel;
        Scanner scanner;

        public WriteHandler(AsynchronousSocketChannel socketChannel, Scanner scanner) {
            this.socketChannel = socketChannel;
            this.scanner = scanner;
        }


        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.compact();
            String msg = scanner.nextLine();

            System.out.println("服务端即将发送的数据：" + msg);
            attachment.put(charset.encode(msg));
            attachment.flip();

            // 继续写数据，构成循环
            socketChannel.write(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }

    static class WriteThread implements Runnable {

        private AsynchronousSocketChannel channel;

        public WriteThread(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            // 第一缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            System.out.println("服务端输入数据：" + msg);
            buffer.put(charset.encode(msg + System.lineSeparator()));
            buffer.flip();

            // 写入数据，并有写数据时的处理器
            // write的第一个参数是数据写入的缓存，第二个参数是附件，第三个参数写结束后的处理器
            // 读取处理器泛型的第一个参数是写入的字节数，第二个是附件类型
            channel.write(buffer, buffer, new WriteHandler(channel, scanner));
        }
    }
}
package com.openmind.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AIO客户端
 *
 * @author zhoujunwen
 * @date 2019-11-11
 * @time 09:31
 * @desc
 */
public class AIO_Client {
    static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws InterruptedException {
        int port = 7890;
        String host  = "127.0.0.1";

        // 启动客户端
        new Thread(new AIOClient(port, host)).start();
        TimeUnit.MINUTES.sleep(100);

    }

    static class AIOClient implements Runnable {

        int port;
        String host;
        AsynchronousChannelGroup group;
        AsynchronousSocketChannel channel;
        InetSocketAddress address;


        public AIOClient(int port, String host) {
            this.port = port;
            this.host = host;

            // 初始化
            init();
        }

        private void init() {
            try {
                // 创建处理线程组
                group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 5);
                // 创建客户端channel
                channel = AsynchronousSocketChannel.open(group);
                address = new InetSocketAddress(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 接收请求，并传入收到请求后的处理器
            // connect 方法的第一二个参数是目标地址，第二个参数是附件对象，第三个参数是连接处理器
            // 连接处理器的泛型的第一个参数为空(即Void)，第二个参数为附件
            channel.connect(address, channel, new ConnectHandler());

        }
    }

    /**
     * 连接处理器
     */
    static class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

        @Override
        public void completed(Void result, AsynchronousSocketChannel attachment) {
            try {
                System.out.println("connect server: " + attachment.getRemoteAddress().toString());

                // 定义数据读取缓存
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // 读取数据，并传入到数据到达时的处理器
                attachment.read(buffer, buffer, new ReadHandler(attachment));

                // 新开线程，发送数据
                new WriteThread(attachment).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

        }
    }

    /**
     * 读处理器
     */
    static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel channel;

        public ReadHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            String readMsg = charset.decode(attachment).toString();
            System.out.println("client receive msg: " + readMsg);
            attachment.compact();

            // 继续接收数据，构成循坏
            channel.read(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }

    /**
     * 写处理器
     */
    static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel channel;
        Scanner scanner;

        public WriteHandler(AsynchronousSocketChannel channel, Scanner scanner) {
            this.channel = channel;
            this.scanner = scanner;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.compact();

            System.out.print("client input data: ");
            String msg = scanner.nextLine();

            System.out.println("clinet will send msg:" + msg);

            attachment.put(charset.encode(msg));
            attachment.flip();

            // 继续写入数据，构成循环
            channel.write(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }


    /**
     * 写处理独立创建线程
     */
    static class WriteThread extends Thread {
        private AsynchronousSocketChannel channel;

        public WriteThread(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner scanner = new Scanner(System.in);
            System.out.print("client input data:");
            String msg = scanner.nextLine();

            System.out.println("client send msg:" + msg);

            buffer.put(charset.encode(msg));
            buffer.flip();

            channel.write(buffer, buffer, new WriteHandler(channel, scanner));
        }
    }
}
```