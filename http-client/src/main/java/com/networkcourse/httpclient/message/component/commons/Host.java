package com.networkcourse.httpclient.message.component.commons;

import com.networkcourse.httpclient.exception.UnsupportedHostException;

/**
 * if no port was given, the port will be set -1
 * @author fguohao
 * @date 2021/05/28
 */
public class Host {
    String host;
    Integer port;

    public static final Integer DefaultPort = -1;

    public Host(String host) throws UnsupportedHostException {
        if(host==null||host.equals("")){
            throw new UnsupportedHostException("No Host is given");
        }
        if(host.contains(":")){
            int idx = host.indexOf(":");
            if(idx==0){
                throw new UnsupportedHostException("Missing host address");
            }
            this.host = host.substring(0,idx);
            String port  = host.substring(idx+1);
            try{
                this.port = Integer.parseInt(port);
            }catch (NumberFormatException e){
                throw new UnsupportedHostException("Unsupported port or missing port after colon(:)");
            }
        }else{
            this.host = host;
            this.port = DefaultPort;
        }
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
