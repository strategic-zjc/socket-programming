package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.Host;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ClientServer{

    private OutputStream sendStream;
    private InputStream recvStream;
    Socket clientSocket = null;
    String host;
    Integer port;
    Boolean keepAlive;

    public ClientServer(String host, Integer port, Boolean keepAlive) {
        this.host = host;
        this.port = port;
        this.keepAlive = keepAlive;
    }

    public OutputStream getSendStream() {
        return sendStream;
    }

    public InputStream getRecvStream() {
        return recvStream;
    }

    public void create() {
        try {
            clientSocket = new Socket(host, port);
            clientSocket.setKeepAlive(keepAlive);
            sendStream = clientSocket.getOutputStream();
            recvStream = clientSocket.getInputStream();

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
