package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author fguohao
 * @date 2021/05/31
 */
public class ClientModifiedCache {
    private static ClientModifiedCache INSTANCE ;
    private ClientModifiedCache(){}

    public static ClientModifiedCache getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new ClientModifiedCache();
        }
        return INSTANCE;
    }

    HashMap<String, HashMap<String, LocalStorageResource>> localStorage = new LinkedHashMap<>();

    public void putModified(String hostname, String path, String time, MessageBody messageBody, String contentType) throws ParseException {
        HashMap<String, LocalStorageResource> sourceLocalStorage = this.localStorage.get(hostname);
        long timestamp = TimeUtil.getTimestamp(time);
        if(sourceLocalStorage==null){
            sourceLocalStorage = new LinkedHashMap<String, LocalStorageResource>();
            localStorage.put(hostname, sourceLocalStorage);
        }
        LocalStorageResource localStorageResource = new LocalStorageResource(timestamp,messageBody,contentType);
        sourceLocalStorage.put(path,localStorageResource);
    }

    public Long getModifiedTime(String host, String path){
        HashMap<String, LocalStorageResource> sourceLocalStorage = localStorage.get(host);
        if(sourceLocalStorage!=null){
            return sourceLocalStorage.get(path).getTimestamp();
        }
        return null;
    }

    public MessageBody getLocalStorage(String host, String path){
        HashMap<String, LocalStorageResource> sourceLocalStorage = localStorage.get(host);
        if(sourceLocalStorage!=null){
            return sourceLocalStorage.get(path).getMessageBody();
        }
        return null;
    }

}
