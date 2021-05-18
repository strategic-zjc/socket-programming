
import Http.Components.Body;
import Http.Components.Header;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{
    // new thread
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket socket;

    public ClientHandler(Socket clientSock){
        try{
            socket = clientSock;
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
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
    @Override
    public void run(){
        // do something to handle the request
        try {


            File file = new File("pic.png");
            byte[] bytesArray = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();

            Body body = new Body("");

            StatusLine statusLine = new StatusLine(1.1, 200, "OK");
//            Header header1 = new Header("Transfer-Encoding", "chunked");
//            Header header2 = new Header("Content-Encoding", "gzip");
            Header header3 = new Header("Content-Type", "image/png");
            Header header4 = new Header("Content-Length", Long.toString(file.length()));
            Header header5 = new Header("Accept-Ranges", "bytes");

            Headers headers = new Headers();
//            headers.addHeader(header1);
//            headers.addHeader(header2);
            headers.addHeader(header3);
            headers.addHeader(header4);
            headers.addHeader(header5);

            HttpResponse response = new HttpResponse(statusLine, headers, body);
            System.out.println(response.toText());
            String t = response.toText().substring(0, response.toText().length()-1);
            byte[] a = t.getBytes(StandardCharsets.UTF_8);
            byte[] res = new byte[a.length + bytesArray.length];
            System.arraycopy(a, 0, res, 0, a.length);
            System.arraycopy(bytesArray, 0, res, a.length, bytesArray.length);

            outToClient.write(res);
//            outToClient.flush();
//            outToClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

    /**
     * web server
     */
//    @Override
//    public void run(){
//
//    }