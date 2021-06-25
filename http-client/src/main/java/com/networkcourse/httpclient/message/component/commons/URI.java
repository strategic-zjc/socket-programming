package com.networkcourse.httpclient.message.component.commons;

import java.net.URISyntaxException;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class URI {
    private String host;
    private String destination;
    private String path;
    private Integer port=80;

    public URI(String uri, String host) throws URISyntaxException {
        if(!uri.startsWith("/")){
            setURI(uri);
        }else{
            if(host==null){
                throw new URISyntaxException(uri,"missing header HOST");
            }
            this.setPath(uri);
            this.setHost(host);
        }

    }

    public URI(String uri) throws URISyntaxException {
        if(!uri.startsWith("/")){
            setURI(uri);
        }else{
            this.setPath(uri);
        }
    }

    //根据uri设置了host，port,destination和path
    private void setURI(String uri) throws URISyntaxException {
        if(!uri.startsWith("http://")) {
            uri="http://"+uri;
        }
        java.net.URI u = new java.net.URI(uri);
        String host  = u.getHost();
        Integer port = u.getPort();
        if(port!=-1){
            this.setHost( host+":"+port);
        }else{
            this.setHost( host+":"+80);
        }
        this.path = (u.getPath()==null||u.getPath().isEmpty())?"/":u.getPath();
    }

    public String getHost() {
        return host;
    }

    //set host同时根据host设置port和destination
    public void setHost(String host) throws URISyntaxException {
        this.host = host;

        if(!host.startsWith("http://")) {
            host="http://"+host;
        }
        java.net.URI u = new java.net.URI(host);
        Integer port = u.getPort();
        if(port!=-1){
            this.port = port;
        }else {
            this.port = 80;
        }
        this.destination = u.getHost();


    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPort() {
        return port;
    }


    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return host+path;
    }
}
