package com.networkcourse.httpclient;

import com.networkcourse.httpclient.client.ClientPool;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.Method;
import com.networkcourse.httpclient.message.component.request.RequsetLine;

import java.io.IOException;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class Main {
    public static void main(String[] args) {
        RequsetLine requsetLine = new RequsetLine(Method.GET,"/");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"www.nju.edu.cn");
        messageHeader.put(Header.Accept,"*/*");
        messageHeader.put(Header.Connection,"keep-alive");
        messageHeader.put(Header.Accept_Encoding,"gzip, deflate, br");
        MessageBody messageBody= new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader,messageBody);
        ClientPool clientPool = new ClientPool();
        try {
            clientPool.sendHttpRequest(httpRequest);
            //clientPool.sendHttpRequest(httpRequest);
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
