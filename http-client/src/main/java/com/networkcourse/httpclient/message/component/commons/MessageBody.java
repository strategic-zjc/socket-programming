package com.networkcourse.httpclient.message.component.commons;

import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.FileUtil;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        this.body = b;
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

    public String toStringByType(String contentType) {
        if(this.body.length==0){
            return "\r\n";
        }
        if(contentType!=null){
            String[] s = contentType.split(";");
            if(s.length>=1){
                if(s[0].equals("text/html")||s[0].equals("application/x-www-form-urlencoded")){
                    return new String(this.body);
                }
            }else{
                return "byte body not supported\r\n";
            }
        }
       return "byte body not supported\r\n";
    }

    public void save(String path) throws IOException {
        FileUtil.save(body,path);
    }

    public void readFromFile(String path) throws IOException {
        body = FileUtil.read(path);
    }


    @Override
    public MessageBody clone(){
        byte[] body = new byte[this.body.length];
        System.arraycopy(this.body, 0, body, 0, this.body.length);
        return new MessageBody(body);
    }
}
