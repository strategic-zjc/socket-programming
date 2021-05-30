package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.handler.RequestHandler;
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

    private static ClientPool INSTANCE;

    private ClientPool(){

    }

    public static ClientPool getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new ClientPool();
        }
        return INSTANCE;
    }

    HashMap<String, ClientServer> clientServerHashMap = new LinkedHashMap<>();


    public ClientServer sendHttpRequest(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, IOException, URISyntaxException {
        ClientServer clientServer = createClientServer(httpRequest);
        clientServer.getSendStream().write(httpRequest.toBytes());

        return clientServer;
    }

    public ClientServer createClientServer(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, URISyntaxException {
        String hostString = null;
        if(!httpRequest.getRequsetLine().getRequestURI().startsWith("/")){
            URI uri = new URI(httpRequest.getRequsetLine().getRequestURI());
            hostString = uri.getHost();
            int port = uri.getPort();
            if(hostString.isEmpty()){
                //todo some error occurred
                throw new MissingHostException();
            }
            if(port!=80){
                //todo some error occurred
            }
        }else{
            hostString = httpRequest.getMessageHeader().get(Header.Host);
        }

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
        Host host = new Host(hostString);
        Integer port = 80;
        if(host.getPort()!=-1) {
            port = host.getPort();
        }
        Boolean keepAlive = httpRequest.isKeepAlive();
        clientServer = new ClientServer(host.getHost(), port, keepAlive);
        if(keepAlive){
            clientServerHashMap.put(hostString,clientServer);
        }
        clientServer.create();
        return clientServer;
    }
}
