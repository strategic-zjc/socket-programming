package com.networkcourse.httpclient.message;

import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.RequsetLine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author fguohao
 * @date 2021/05/27
 */
public class HttpRequest {
    private RequsetLine requsetLine;
    private MessageHeader messageHeader;
    private MessageBody messageBody;

    /*以下字段不被包含在rfc规范之中，仅用于处理*/
    private com.networkcourse.httpclient.message.component.commons.URI uri;

    public HttpRequest(RequsetLine requsetLine, MessageHeader messageHeader, MessageBody messageBody) throws URISyntaxException {
        this.requsetLine = requsetLine;
        this.messageHeader = messageHeader;
        this.messageBody = messageBody;

        this.uri = new com.networkcourse.httpclient.message.component.commons.URI(this.getRequsetLine().getRequestURI(),this.messageHeader.get(Header.Host));
    }

    public HttpRequest(InputStream inputStream) throws IOException, URISyntaxException {
        requsetLine = new RequsetLine(inputStream);
        messageHeader = new MessageHeader(inputStream);
        messageBody = new MessageBody(inputStream,messageHeader);
        this.uri = new com.networkcourse.httpclient.message.component.commons.URI(this.getRequsetLine().getRequestURI(),this.messageHeader.get(Header.Host));
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

    public String getHost() {
        return this.uri.getHost();
    }

    public String getPath() {
        return this.uri.getPath();
    }

    public Integer getPort() {
        return this.uri.getPort();
    }

    public String getDestination() {
        return this.uri.getDestination();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(requsetLine.toString());
        sb.append(messageHeader.toString());
        sb.append("\r\n");
        sb.append(messageBody.toStringByType(messageHeader.get(Header.Content_Type)));

        return sb.toString();
    }

    public byte[] toBytes(){
        byte[] requestLineB = this.requsetLine.toString().getBytes();
        byte[] messageHeaderB = this.messageHeader.toString().getBytes();
        byte[] crlf = "\r\n".getBytes();
        byte[] messageBodyB = this.messageBody.getBody();
        int len = requestLineB.length + messageHeaderB.length + crlf.length + messageBodyB.length;
        byte[] bytes = new byte[len];
        int index = 0;
        System.arraycopy(requestLineB,0,bytes,index,requestLineB.length);
        index += requestLineB.length;

        System.arraycopy(messageHeaderB,0,bytes,index,messageHeaderB.length);
        index += messageHeaderB.length;

        System.arraycopy(crlf,0,bytes,index,crlf.length);
        index += crlf.length;

        System.arraycopy(messageBodyB,0,bytes,index, messageBodyB.length);
        return bytes;
    }

    public Boolean isKeepAlive(){
        String connection = messageHeader.get(Header.Connection);
        if(connection==null){
            return false;
        }
        return connection.equals("keep-alive");
    }

    public void readBodyFromFile(String path) throws IOException {
        messageBody.readFromFile(path);
    }

    @Override
    public HttpRequest clone(){
        try {
            return new HttpRequest(this.requsetLine.clone(), this.messageHeader.clone(), this.messageBody.clone());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
