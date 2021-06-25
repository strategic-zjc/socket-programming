package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.message.component.commons.MessageBody;

/**
 * 
 * @author fguohao
 * @date 2021/05/31
 */
public class LocalStorageResource {
    private long timestamp;
    private MessageBody messageBody;
    private String content_type;

    public LocalStorageResource(long timestamp, MessageBody messageBody,String content_type) {
        this.timestamp = timestamp;
        this.messageBody = messageBody;
        this.content_type = content_type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
