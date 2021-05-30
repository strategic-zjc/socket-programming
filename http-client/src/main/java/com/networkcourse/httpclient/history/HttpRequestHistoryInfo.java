package com.networkcourse.httpclient.history;

import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.sql.Timestamp;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class HttpRequestHistoryInfo {
    HttpRequest httpRequest;
    HttpResponse httpResponse;
    Long timestamp;

    HttpRequestHistoryInfo(HttpRequest httpRequest,HttpResponse httpResponse){
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return TimeUtil.toTimeString(timestamp)+"\r\n"+httpRequest.toString()+"\r\n"+httpResponse.toString();
    }
}
