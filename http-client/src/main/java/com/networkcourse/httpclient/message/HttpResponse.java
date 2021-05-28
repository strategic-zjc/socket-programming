package com.networkcourse.httpclient.message;

import com.networkcourse.httpclient.client.ClientThread;
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

    public HttpResponse(ClientThread clientThread){
        try {
            /*while (true){
                System.out.println(clientThread.recvStream.readLine());
            }*/

            //String respLine = clientThread.recvStream.readLine();
            String respLine = InputStreamReaderHelper.readLine(clientThread.recvByteStream);
            this.responseLine = new ResponseLine(respLine);
            List<String> headers = new ArrayList<>();
            String temp;
//            while (!(temp=clientThread.recvStream.readLine()).equals("")){
//                headers.add(temp);
//            }
            while (!(temp= InputStreamReaderHelper.readLine(clientThread.recvByteStream)).equals("")){
                headers.add(temp);
            }
            this.messageHeader = new MessageHeader(headers);
            byte[] b = null;
            String transferEncoding = this.messageHeader.get(Header.Transfer_Encoding);
            String contentLength = this.messageHeader.get(Header.Content_Length);
            if(transferEncoding!=null&&transferEncoding.equals("chunked")){
                //b = ChunkReader.readChunk(clientThread.recvStream);
                b = ChunkReader.readChunk(clientThread.recvByteStream);
                // Content-Encoding: gzip 解压缩
                String contentEncoding = this.messageHeader.get(Header.Content_Encoding);
                if(contentEncoding!=null&&contentEncoding.equals("gzip")){
                    GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(b));
                    b = InputStreamReaderHelper.readInputStream(gzipInputStream);
                }
            }else if(contentLength!=null){
                //clientThread.recvStream.reset();
                int length = Integer.parseInt(contentLength);
                b = ByteReader.readByte(clientThread.recvByteStream,length);

            }
            //System.out.println(new String(b, StandardCharsets.UTF_8));
            System.out.println();


        } catch (IOException e) {
            e.printStackTrace();
        }
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
