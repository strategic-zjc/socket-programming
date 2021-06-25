package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.handler.RequestHandler;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.Host;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * only support absoulte uri and path
 * @author fguohao
 * @date 2021/05/28
 */
public class ClientPool {

    History history;

    public ClientPool(History history) {
        this.history = history;
    }

    HashMap<String, ClientServer> clientServerHashMap = new LinkedHashMap<>();


    public ClientServer sendHttpRequest(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, IOException, URISyntaxException {
        ClientServer clientServer = createClientServer(httpRequest);
        clientServer.getSendStream().write(httpRequest.toBytes());
        return clientServer;
    }

    public ClientServer createClientServer(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, URISyntaxException {
        String hostString = httpRequest.getHost();
        if(hostString==null){
            throw new MissingHostException();
        }
        ClientServer clientServer = clientServerHashMap.get(hostString);
        if(clientServer!=null){
            if(clientServer.isClosed()){
                clientServerHashMap.remove(hostString);
            }else{
                return clientServer;
            }
        }
        String destination = httpRequest.getDestination();
        Integer port = httpRequest.getPort()==null?80: httpRequest.getPort();
        Boolean keepAlive = httpRequest.isKeepAlive();
        clientServer = new ClientServer(destination, port, keepAlive);
        history.addLog("ClientServer Created, destination="+destination+" ,port="+port+" ,keepAlive="+keepAlive,History.LOG_LEVEL_INFO);
        if(keepAlive){
            clientServerHashMap.put(hostString,clientServer);
        }
        clientServer.create();
        return clientServer;
    }
}
