package com.networkcourse.httpclient.history;

import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class History {
    private static History INSTANCE;
    private List<HttpRequestHistoryInfo> historyInfos = new LinkedList<>();


    private History(){}

    public static History getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE = new History();
        }
        return INSTANCE;
    }

    public void addHistory(HttpRequest httpRequest, HttpResponse httpResponse){
        historyInfos.add(new HttpRequestHistoryInfo(httpRequest,httpResponse));
    }

    public void printHistory(){
        for(HttpRequestHistoryInfo historyInfo:historyInfos){
            System.out.println(historyInfo.toString());
        }
    }
}
