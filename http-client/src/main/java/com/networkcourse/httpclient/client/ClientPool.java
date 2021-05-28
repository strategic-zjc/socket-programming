package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.Host;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ClientPool {
    HashMap<String, ClientThread> clientThreadHashMap = new LinkedHashMap<>();

    public HttpResponse sendHttpRequest(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, IOException {
        ClientThread clientThread = createClientThread(httpRequest);
        System.out.println(httpRequest.toString());
        clientThread.sendStream.writeBytes(httpRequest.toString());
        HttpResponse httpResponse = new HttpResponse(clientThread);
        System.out.println(httpResponse.toString());
        clientThread.closeConnection();
        return null;
    }

    private ClientThread createClientThread(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException {
        String hostString = httpRequest.getMessageHeader().get(Header.Host);
        if(hostString==null){
            throw new MissingHostException();
        }
        ClientThread clientThread = clientThreadHashMap.get(hostString);
        if(clientThread!=null){
            if(clientThread.isClosed()){
                clientThreadHashMap.remove(hostString);
            }else{
                return clientThread;
            }
        }
        Host host = new Host(hostString);
        Integer port = 80;
        if(host.getPort()!=-1) {
            port = host.getPort();
        }
        Boolean keepAlive = httpRequest.isKeepAlive();
        clientThread = new ClientThread(host.getHost(), port, keepAlive);
        if(keepAlive){
            clientThreadHashMap.put(hostString,clientThread);
        }
        clientThread.run();
        return clientThread;
    }
}
