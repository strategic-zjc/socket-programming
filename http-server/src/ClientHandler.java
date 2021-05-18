
import Http.Components.Body;
import Http.Components.Header;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
    @Override
    public void run(){
        // do something to handle the request
        try {
            StatusLine statusLine = new StatusLine(1.1, 200, "OK");
            Header header1 = new Header("Content-Type", "text/plain");
            Header header2 = new Header("Content-Length", "12");
            Headers headers = new Headers();
            headers.addHeader(header1);
            headers.addHeader(header2);
            Body body = new Body("hello world");

            HttpResponse response = new HttpResponse(statusLine, headers, body);
            System.out.println(response.toText());
            outToClient.writeBytes(response.toText());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}