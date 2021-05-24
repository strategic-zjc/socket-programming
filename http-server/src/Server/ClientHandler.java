package Server;

import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import Http.Util;
import RequestExecutor.BasicExecutor;
import RequestExecutor.DummyExecutor;
import RequestExecutor.GetExecutor;
import RequestExecutor.PostExecutor;

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
     * hello world
     */
//    @Override
//    public void run(){
//        // do something to handle the request
//        try {
//            StatusLine statusLine = new StatusLine(1.1, 200, "OK");
//            Header header1 = new Header("Content-Type", "text/plain");
//            Header header2 = new Header("Content-Length", "12");
//            Headers headers = new Headers();
//            headers.addHeader(header1);
//            headers.addHeader(header2);
//            Body body = new Body("hello world");
//
//            HttpResponse response = new HttpResponse(statusLine, headers, body);
//            System.out.println(response.toText());
//            outToClient.writeBytes(response.toText());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    /**
     * send pic demo
     */
//    @Override
//    public void run(){
//        // do something to handle the request
//        try {
//
//
//            File file = new File("pic.png");
//            byte[] bytesArray = new byte[(int) file.length()];
//
//            FileInputStream fis = new FileInputStream(file);
//            fis.read(bytesArray); //read file into bytes[]
//            fis.close();
//
//            Body body = new Body("");
//
//            StatusLine statusLine = new StatusLine(1.1, 200, "OK");
////            Header header1 = new Header("Transfer-Encoding", "chunked");
////            Header header2 = new Header("Content-Encoding", "gzip");
//
//
//            Headers headers = new Headers();
//
//            headers.addHeader("Content-Type", "image/png");
//            headers.addHeader("Content-Length", Long.toString(file.length()));
//            headers.addHeader("Accept-Ranges", "bytes");
//
//            HttpResponse response = new HttpResponse(statusLine, headers, body);
//            System.out.println(response.toText());
//            String t = response.toText().substring(0, response.toText().length()-1);
//            byte[] a = t.getBytes(StandardCharsets.UTF_8);
//            byte[] res = new byte[a.length + bytesArray.length];
//            System.arraycopy(a, 0, res, 0, a.length);
//            System.arraycopy(bytesArray, 0, res, a.length, bytesArray.length);
//
//            outToClient.write(res);
////            outToClient.flush();
////            outToClient.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}

    /**
     * web server
     */
    @Override
    public void run() {
        // read all bytes from socket stream
        try {
            String line;
            StringBuffer sb = new StringBuffer();
            while((line = inFromClient.readLine()) != null){
                sb.append(line + '\n');
                if(line.isEmpty())
                    break;
            }

            if(sb.toString().equals("")) return;
            HttpRequest request = Util.String2Request(sb.toString());

            String target = request.getStartLine().getTarget();
            // redirect


            String method = request.getStartLine().getMethod();
            BasicExecutor executor = new DummyExecutor();
            if(method.equals("GET")) {
                executor = new GetExecutor();

            }else if (method.equals("POST")){
                executor = new PostExecutor(inFromClient);
            }
            HttpResponse response = executor.handle(request);


            /*if(target.equals("/")){
                target = "/index.html";
            }
            else if(target.startsWith("http://")){
                target = target.substring(target.lastIndexOf("/"));// bug
            }
            String path = target.substring(1);
            File f = new File(path);

            StatusLine statusLine = new StatusLine(1.1, 200, "OK");
            Headers headers = new Headers();


            if(f.getName().endsWith(".html")) {
                headers.addHeader("Content-Type", "text/html");
            }
            else if(f.getName().endsWith(".png")){
                headers.addHeader("Content-Type", "image/png");
            }
            headers.addHeader("Content-Length", Long.toString(f.length()));

            byte[] bytesArray = new byte[(int) f.length()];

            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();

            Body body = new Body(
                    bytesArray);

            HttpResponse response = new HttpResponse(statusLine, headers, body);
*/
            outToClient.write(response.ToBytes());
            //timer 如果再次收到请求，重置timer，否则就关闭
            //
            outToClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}