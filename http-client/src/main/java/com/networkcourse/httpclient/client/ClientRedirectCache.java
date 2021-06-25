package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.message.component.commons.URI;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 重定向本地缓存
 * @author fguohao
 * @date 2021/05/30
 */
public class ClientRedirectCache {

    HashMap<String, HashMap<String, URI>> localStorage = new LinkedHashMap<>();

    public void putRedirect(String hostname, String oldPath, String newURI) throws URISyntaxException {
        HashMap<String, URI> hostLocalStorage = localStorage.get(hostname);
        if(hostLocalStorage==null){
            hostLocalStorage = new LinkedHashMap<String, URI>();
            localStorage.put(hostname, hostLocalStorage);
        }
        URI uri = new URI(newURI);
        if(uri.getHost()==null){
            uri.setHost(hostname);
        }
        hostLocalStorage.put(oldPath,uri);
    }

    public URI getRedirect(String hostname, String oldPath){
        HashMap<String, URI> hostLocalStorage = localStorage.get(hostname);
        if(hostLocalStorage!=null){
            return hostLocalStorage.get(oldPath);
        }
        return null;
    }

}
