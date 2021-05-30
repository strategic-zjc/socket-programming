package com.networkcourse.httpclient.message;


import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/27
 */
public class HttpResponse {
    ResponseLine responseLine;
    MessageHeader messageHeader;
    MessageBody messageBody;

    public HttpResponse(ResponseLine responseLine, MessageHeader messageHeader, MessageBody messageBody) {
        this.responseLine = responseLine;
        this.messageHeader = messageHeader;
        this.messageBody = messageBody;
    }

    public ResponseLine getResponseLine() {
        return responseLine;
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
        sb.append(responseLine.toString());
        sb.append(messageHeader.toString());
        sb.append("\r\n");
        //TODO messageBodyToSring
        return sb.toString();
    }
}
