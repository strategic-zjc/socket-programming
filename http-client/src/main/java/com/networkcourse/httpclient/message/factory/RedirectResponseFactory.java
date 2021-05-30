package com.networkcourse.httpclient.message.factory;

import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.RequsetLine;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.message.component.response.StatusCode;

/**
 * @author fguohao
 * @date 2021/05/31
 */
public class RedirectResponseFactory {
    private static RedirectResponseFactory INSTANCE;
    private RedirectResponseFactory(){}

    public static RedirectResponseFactory getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new RedirectResponseFactory();
        }
        return INSTANCE;
    }

    public HttpResponse getRedirectResponse(String newURI){
        ResponseLine responseLine = new ResponseLine(RequsetLine.DEFAULT_HTTP_VERSION, 301, StatusCode.Moved_Permanently_STR);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Location,newURI);
        return new HttpResponse(responseLine, messageHeader, new MessageBody());
    }
}
