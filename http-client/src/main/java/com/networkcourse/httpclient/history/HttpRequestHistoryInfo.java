package com.networkcourse.httpclient.history;

import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.sql.Timestamp;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class HttpRequestHistoryInfo extends HistoryInfo{
    HttpRequest httpRequest;
    HttpResponse httpResponse;

    HttpRequestHistoryInfo(HttpRequest httpRequest,HttpResponse httpResponse){
        super();
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;

    }

    @Override
    public String toString() {
        return "Send Result:\r\nRequestBody:\r\n"+httpRequest.toString()+"ResponseBody:\r\n"+httpResponse.toString();
    }
}
