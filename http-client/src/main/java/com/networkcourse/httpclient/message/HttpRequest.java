package com.networkcourse.httpclient.message;

import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.RequsetLine;

/**
 * @author fguohao
 * @date 2021/05/27
 */
public class HttpRequest {
    private RequsetLine requsetLine;
    private MessageHeader messageHeader;
    private MessageBody messageBody;

    public HttpRequest(RequsetLine requsetLine, MessageHeader messageHeader, MessageBody messageBody) {
        this.requsetLine = requsetLine;
        this.messageHeader = messageHeader;
        this.messageBody = messageBody;
    }

    public RequsetLine getRequsetLine() {
        return requsetLine;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(requsetLine.toString());
        sb.append(messageHeader.toString());
        sb.append("\r\n");
        //TODO: body to string
        return sb.toString();
    }

    public Boolean isKeepAlive(){
        String connection = messageHeader.get(Header.Connection);
        if(connection==null){
            return false;
        }
        return connection.equals("keep-alive");
    }
}
