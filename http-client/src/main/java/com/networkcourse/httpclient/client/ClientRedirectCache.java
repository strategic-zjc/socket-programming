package com.networkcourse.httpclient.client;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class ClientRedirectCache {
    private static ClientRedirectCache INSTANCE ;
    private ClientRedirectCache(){}

    public static ClientRedirectCache getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new ClientRedirectCache();
        }
        return INSTANCE;
    }

    HashMap<String, HashMap<String, String>> localStorage = new LinkedHashMap<>();

    public void putRedirect(String hostname, String oldPath, String newURI){
        HashMap<String, String> hostLocalStorage = localStorage.get(hostname);
        if(hostLocalStorage==null){
            hostLocalStorage = new LinkedHashMap<String, String>();
            localStorage.put(hostname, hostLocalStorage);
        }
        hostLocalStorage.put(oldPath,newURI);
    }

    public String getRedirect(String hostname, String oldPath){
        HashMap<String, String> hostLocalStorage = localStorage.get(hostname);
        if(hostLocalStorage!=null){
            return hostLocalStorage.get(oldPath);
        }
        return null;
    }

}
