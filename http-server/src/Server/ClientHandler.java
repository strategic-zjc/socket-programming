package Server;

import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import Http.Util;
import RequestExecutor.BasicExecutor;
import RequestExecutor.StaticResourceHandler;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // new thread
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket socket;



    public ClientHandler(Socket clientSock) {
        try {
            socket = clientSock;
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * web server
     */
    @Override
    public void run() {
        try {
            // read all bytes from socket stream
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = inFromClient.readLine()) != null){
                sb.append(line).append('\n');
                if(line.isEmpty())
                    break;
            }

            if(sb.toString().equals("")) return;

            HttpRequest request = Util.String2Request(sb.toString());

            String contentLength = request.getHeaders().getValue("Content-Length");

            if(contentLength != null) {

                int length = Integer.parseInt(contentLength);

                char[] cbuf = new char[length];
                inFromClient.read(cbuf, 0, length);

                request.getBody().setData(String.valueOf(cbuf));
            }


            String target = request.getStartLine().getTarget();
            String method = request.getStartLine().getMethod();

            HttpResponse response = null;
            BasicExecutor executor = null;

            // 如果请求一个静态资源，调用StaticResourceHandler
            if(StaticResourceHandler.isStaticTarget(target) && method.toLowerCase().equals("get")){
                executor = new StaticResourceHandler();
            }
            else {
                // 否则，在持有的executor中找到合适的，用这个executor处理请求
                for (BasicExecutor e : SimpleServer.Executors) {
                    if (target.endsWith(e.getUrl()) && method.toLowerCase().equals(e.getMethod().toLowerCase())) {
                        executor = e;
                        break;
                    }
                }
            }

            // 找不到合适的executor
            // 404: 没有对应的url 405: 有对应的url但是没有对应的method
            if(executor == null) {
                response = new HttpResponse(new StatusLine(1.1, 404, "Not Found"), new Headers(), new Body());
                for(BasicExecutor e : SimpleServer.Executors) {
                    if (target.endsWith(e.getUrl())){
                        response = new HttpResponse(new StatusLine(1.1, 405, "Method Not Allowed"), new Headers(), new Body());
                        break;
                    }
                }
            }
            else {
                response = executor.handle(request);
            }

            outToClient.write(response.ToBytes());
            //timer 如果再次收到请求，重置timer，否则就关闭
            //
            outToClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}