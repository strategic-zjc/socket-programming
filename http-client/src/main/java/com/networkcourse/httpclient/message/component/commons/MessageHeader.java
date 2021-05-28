package com.networkcourse.httpclient.message.component.commons;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fguohao
 * @date 2021/05/27
 */
public class MessageHeader {
    private HashMap<String, String> header = new LinkedHashMap<>();

    public MessageHeader(){}

    public MessageHeader(List<String> headers){
        for(String header:headers){
            String formattedHeader = header.trim();
            if(formattedHeader.equals("")){
                continue;
            }
            if(!formattedHeader.contains(":")){
                //TODO
                continue;
            }
            int index = formattedHeader.indexOf(":");
            String fieldName = formattedHeader.substring(0,index).trim();
            String fieldValue = formattedHeader.substring(index+1).trim();
            this.header.put(fieldName,fieldValue);
        }
    }

    public void put(String fieldName, String fieldValue){
        header.put(fieldName, fieldValue);
    }

    public void remove(String fieldName){
        header.remove(fieldName);
    }

    public String get(String fieldName){
        return header.get(fieldName);
    }

    public Set<String> getAllFieldName(){
        return header.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String fieldName:header.keySet()){
            sb.append(String.format("%s: %s\r\n", fieldName, header.get(fieldName)));
        }
        return sb.toString();
    }


}
