package com.networkcourse.httpclient.handler;

import com.networkcourse.httpclient.client.ClientCache;
import com.networkcourse.httpclient.client.ClientModifiedCache;
import com.networkcourse.httpclient.client.ClientRedirectCache;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.RequsetLine;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.message.component.response.StatusCode;
import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class ResponseHandler {
    private ClientRedirectCache clientRedirectCache = ClientRedirectCache.getINSTANCE();
    private ClientModifiedCache clientModifiedCache = ClientModifiedCache.getINSTANCE();
    private ResponseHandler(){}

    private static ResponseHandler INSTANCE;

    public static ResponseHandler getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE = new ResponseHandler();
        }
        return INSTANCE;
    }

    public HttpResponse handle(HttpRequest httpRequest, InputStream inputStream) throws URISyntaxException, UnsupportedHostException, IOException, MissingHostException, ParseException {
        ResponseLine responseLine;
        MessageHeader messageHeader;
        MessageBody messageBody;
        HttpResponse httpResponse = null;

        responseLine = readResponseLine(inputStream);
        messageHeader = readMessageHeader(inputStream);
        messageBody = readMessageBody(messageHeader, inputStream);
        httpResponse =  new HttpResponse(responseLine,messageHeader,messageBody);
        History.getINSTANCE().addHistory(httpRequest, httpResponse);//log
        switch (responseLine.getStatusCode()){
            case StatusCode.Moved_Permanently:
                httpRequest = handleMovedPermanently(httpRequest,httpResponse);

                return RequestHandler.getINSTANCE().handle(httpRequest);
            case StatusCode.Found:
                httpRequest = handleMoved(httpRequest,httpResponse);
                return RequestHandler.getINSTANCE().handle(httpRequest);
            case StatusCode.Not_Modified:
                messageBody = handleLocalStorage(httpRequest);
                break;
            default:;
        }
        // add to localstorage
        // todo add switch to enable/disable localstorage
        String modifiedTime = messageHeader.get(Header.Last_Modified);
        if(modifiedTime!=null){
            String contentType = messageHeader.get(Header.Content_Type);
            String uri = httpRequest.getRequsetLine().getRequestURI();
            String path = null;
            String host = null;
            if(!uri.startsWith("/")){
                URI u = new URI(uri);
                path = u.getPath();
                host = u.getHost();
                if(u.getPort()!=-1){
                    host = host+":"+u.getPort();
                }
                //todo check valid
            }else {
                path = uri;
                host = httpRequest.getMessageHeader().get(Header.Host);
                //todo check valid
            }
            clientModifiedCache.putModified(host,path,modifiedTime,messageBody,contentType);
        }

        return httpResponse;
    }

    private HttpRequest handleMovedPermanently(HttpRequest oldhttpRequest,HttpResponse httpResponse) throws URISyntaxException, UnsupportedHostException {
        RequsetLine oldRequsetLine = oldhttpRequest.getRequsetLine();
        String newLocation = httpResponse.getMessageHeader().get(Header.Location);

        if(newLocation==null){
            //todo an error occurred
        }
        String oldpath = null;
        String oldHost = null;

        String oldRequestURI = oldRequsetLine.getRequestURI();

        if(!oldRequestURI.startsWith("/")){
            URI u = new URI(oldRequestURI);
            oldpath = u.getPath();
            oldHost = u.getHost();
            if(u.getPort()!=-1){
                oldHost = oldHost+":"+u.getPort();
            }
            //todo check valid
        }else {
            oldpath = oldRequestURI;
            oldHost = oldhttpRequest.getMessageHeader().get(Header.Host);
            //todo check valid
        }

        clientRedirectCache.putRedirect(oldHost, oldpath, newLocation);

        return handleMoved(oldhttpRequest, httpResponse);
    }

    private HttpRequest handleMoved(HttpRequest oldhttpRequest,HttpResponse httpResponse) throws URISyntaxException, UnsupportedHostException {
        RequsetLine oldRequsetLine = oldhttpRequest.getRequsetLine();
        MessageHeader oldRequestHeader = oldhttpRequest.getMessageHeader();
        MessageBody oldRequestBody = oldhttpRequest.getMessageBody();

        String newLocation = httpResponse.getMessageHeader().get(Header.Location);

        if(newLocation==null){
            //todo an error occurred
        }
        URI newLocationURI = new URI(newLocation);
        String host = newLocationURI.getHost();
        String path = newLocationURI.getPath();
        if(newLocationURI.getPort()!=-1){
            host = host +":"+ newLocationURI.getPort();
        }else if(newLocation.startsWith("https")){
            //todo unsupported scheme https
            host = host+":"+443;
            System.out.println("Unsupported schema https");
            return null;
        }

        RequsetLine requsetLine = oldRequsetLine.clone();
        requsetLine.setRequestURI(path);

        MessageHeader messageHeader = oldRequestHeader.clone();
        messageHeader.put(Header.Host, host);

        MessageBody messageBody = oldRequestBody.clone();


        HttpRequest httpRequest = new HttpRequest(requsetLine, messageHeader, messageBody);
        return httpRequest;
    }

    private MessageBody handleLocalStorage(HttpRequest httpRequest) throws URISyntaxException {
        String uri = httpRequest.getRequsetLine().getRequestURI();
        String path = null;
        String host = null;
        if(!uri.startsWith("/")){
            URI u = new URI(uri);
            path = u.getPath();
            host = u.getHost();
            if(u.getPort()!=-1){
                host = host+u.getPort();
            }
            //todo check valid
        }else {
            path = uri;
            host = httpRequest.getMessageHeader().get(Header.Host);
            //todo check valid
        }
        return clientModifiedCache.getLocalStorage(host,path);
    }

    private ResponseLine readResponseLine(InputStream inputStream) throws IOException {
        String respLine = InputStreamReaderHelper.readLine(inputStream);
        return new ResponseLine(respLine);
    }

    private MessageHeader readMessageHeader(InputStream inputStream) throws IOException {
        List<String> headers = new ArrayList<>();
        String temp;
        while (!(temp= InputStreamReaderHelper.readLine(inputStream)).equals("")){
            headers.add(temp);
        }
        return new MessageHeader(headers);
    }

    private MessageBody readMessageBody(MessageHeader messageHeader, InputStream inputStream) throws IOException {
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
        return new MessageBody(b);
    }
}
