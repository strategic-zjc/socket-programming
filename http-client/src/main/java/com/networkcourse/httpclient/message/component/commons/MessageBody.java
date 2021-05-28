package com.networkcourse.httpclient.message.component.commons;

import java.io.BufferedReader;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class MessageBody {
    char[] body;

    public MessageBody(){

    }

    public char[] getBody() {
        return body;
    }

    public void setBody(char[] body) {
        this.body = body;
    }
}
