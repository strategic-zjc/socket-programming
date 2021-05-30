package com.networkcourse.httpclient.message.component.commons;

import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class MessageBody {
    byte[] body = new byte[0];

    public MessageBody(){

    }

    public MessageBody(InputStream inputStream,MessageHeader messageHeader) throws IOException {
        byte[] b = new byte[0];
        String transferEncoding = messageHeader.get(Header.Transfer_Encoding);
        String contentLength = messageHeader.get(Header.Content_Length);
        if(transferEncoding!=null&&transferEncoding.equals("chunked")){
            b = ChunkReader.readChunk(inputStream);
            // Content-Encoding: gzip 解压缩
            String contentEncoding = messageHeader.get(Header.Content_Encoding);
            if(contentEncoding!=null&&contentEncoding.equals("gzip")){
                GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(b));
                b = InputStreamReaderHelper.readInputStream(gzipInputStream);
            }
        }else if(contentLength!=null){
            int length = Integer.parseInt(contentLength);
            b = ByteReader.readByte(inputStream,length);
        }
    }

    public MessageBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public MessageBody clone(){
        byte[] body = new byte[this.body.length];
        System.arraycopy(this.body, 0, body, 0, this.body.length);
        return new MessageBody(body);
    }
}
