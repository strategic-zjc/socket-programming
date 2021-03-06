package Server;

import Http.HttpRequest;
import Http.HttpResponse;
import Http.Util;
import RequestExecutor.BasicExecutor;
import Common.Template;
import RequestExecutor.StaticResourceHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
    // new thread
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket socket;
    boolean ServerSwitch;
    boolean isTimeout = false;
    public static TimerTask timerTask = null;


    public ClientHandler(Socket clientSock) {
        try {
            socket = clientSock;
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
            ServerSwitch = true;
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

            while (true) {
                if (isTimeout) {
                    socket.close();
                    return;
                }
                // read all bytes from socket stream
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = inFromClient.readLine()) != null) {
                    sb.append(line).append('\n');
                    if (line.isEmpty())
                        break;
                }
                if (sb.toString().equals("")) return;

                System.out.println(sb.toString());

                HttpRequest request = Util.String2Request(sb.toString());

                if (request.getHeaders().getValue("Keep-Alive") != null) {
                    String timeout = request.getHeaders().getValue("Keep-Alive");
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            isTimeout = true;
                        }
                    };
                    SimpleServer.timer.schedule(timerTask, Integer.parseInt(timeout.substring(8)) * 1000L);
                }


                String contentLength = request.getHeaders().getValue("Content-Length");

                if (contentLength != null) {

                    int length = Integer.parseInt(contentLength);

                    char[] cbuf = new char[length];
                    inFromClient.read(cbuf, 0, length);

                    request.getBody().setData(String.valueOf(cbuf));
                }


                String target = request.getStartLine().getTarget();
                String method = request.getStartLine().getMethod();

                HttpResponse response = null;
                BasicExecutor executor = null;

                // ???????????????????????????????????????StaticResourceHandler
                if (StaticResourceHandler.isStaticTarget(target) && method.toLowerCase().equals("get")) {
                    executor = new StaticResourceHandler();
                } else {
                    // ?????????????????????executor??????????????????????????????executor????????????
                    for (BasicExecutor e : SimpleServer.Executors) {
                        if (target.endsWith(e.getUrl()) && method.toLowerCase().equals(e.getMethod().toLowerCase())) {
                            executor = e;
                            break;
                        }
                    }
                }

                // ??????????????????executor
                // 404: ???????????????url 405: ????????????url?????????????????????method
                if (executor == null) {
                    response = Template.generateStatusCode_404();
                    //todo ??????post?????????????????????bug???????????????404
                    for (BasicExecutor e : SimpleServer.Executors) {
                        if (target.endsWith(e.getUrl())) {
                            response = Template.generateStatusCode_405();
                            break;
                        }
                    }

                } else {
                    response = executor.handle(request);
                }


                outToClient.write(response.ToBytes());
                //timer ?????????????????????????????????timer??????????????????

            }
//            outToClient.close();
        }catch (SocketException e){

        } catch (Exception e) {
            HttpResponse response = Template.generateStatusCode_500();
            try {
                outToClient.write(response.ToBytes());
            }catch (Exception ee){}
            e.printStackTrace();
        }
    }
}
