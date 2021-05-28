package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.Host;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ClientThread implements Runnable{

    public DataOutputStream sendStream;
    public BufferedReader recvStream;
    public InputStream recvByteStream;
    Socket clientSocket = null;
    String host;
    Integer port;
    Boolean keepAlive;

    public ClientThread(String host, Integer port, Boolean keepAlive) {
        this.host = host;
        this.port = port;
        this.keepAlive = keepAlive;
        System.out.println("new thread created");
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(host, port);
            clientSocket.setKeepAlive(keepAlive);
            sendStream = new DataOutputStream(clientSocket.getOutputStream());
            recvByteStream = clientSocket.getInputStream();
            recvStream = new BufferedReader(new InputStreamReader(recvByteStream));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        clientSocket.close();
    }

    public boolean isClosed(){
        return clientSocket.isClosed();
    }
}
