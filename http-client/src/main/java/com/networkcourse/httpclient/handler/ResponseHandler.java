package com.networkcourse.httpclient.handler;

import com.networkcourse.httpclient.client.Client;
import com.networkcourse.httpclient.client.ClientCache;
import com.networkcourse.httpclient.client.ClientModifiedCache;
import com.networkcourse.httpclient.client.ClientRedirectCache;
import com.networkcourse.httpclient.exception.InvalidHttpRequestException;
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
    private ClientRedirectCache clientRedirectCache;
    private ClientModifiedCache clientModifiedCache;
    private Client client;
    private History history;

    public ResponseHandler(ClientRedirectCache clientRedirectCache, ClientModifiedCache clientModifiedCache, Client client, History history) {
        this.clientRedirectCache = clientRedirectCache;
        this.clientModifiedCache = clientModifiedCache;
        this.client = client;
        this.history = history;
    }

    public HttpResponse handle(HttpRequest httpRequest, InputStream inputStream) throws URISyntaxException, UnsupportedHostException, IOException, MissingHostException, ParseException, InvalidHttpRequestException {
        ResponseLine responseLine;
        MessageHeader messageHeader;
        MessageBody messageBody;
        HttpResponse httpResponse = null;

        responseLine = new ResponseLine(InputStreamReaderHelper.readLine(inputStream));
        messageHeader = new MessageHeader(inputStream);
        messageBody = new MessageBody( inputStream , messageHeader);
        httpResponse =  new HttpResponse(responseLine,messageHeader,messageBody);
        history.addHistory(httpRequest, httpResponse);//log
        switch (responseLine.getStatusCode()){
            case StatusCode.Moved_Permanently:
                httpRequest = handleMovedPermanently(httpRequest,httpResponse);
                return client.sendHttpRequest(httpRequest);
            case StatusCode.Found:
                httpRequest = handleMoved(httpRequest,httpResponse);
                return client.sendHttpRequest(httpRequest);
            case StatusCode.Not_Modified:
                messageBody = handleLocalStorage(httpRequest);
                history.addLog("Add not modified body to this new response",History.LOG_LEVEL_INFO);
                break;
            default:;
        }
        // add to localstorage
        String modifiedTime = messageHeader.get(Header.Last_Modified);
        if(modifiedTime!=null){
            String contentType = messageHeader.get(Header.Content_Type);
            String uri = httpRequest.getRequsetLine().getRequestURI();
            String path = httpRequest.getPath();
            String host = httpRequest.getHost();
            clientModifiedCache.putModified(host,path,modifiedTime,messageBody,contentType);
            history.addLog("Add a Modified Cache entry and save the body in cache, path="+host+path, History.LOG_LEVEL_INFO);
        }

        return httpResponse;
    }

    private HttpRequest handleMovedPermanently(HttpRequest oldhttpRequest,HttpResponse httpResponse) throws URISyntaxException, UnsupportedHostException {
        RequsetLine oldRequsetLine = oldhttpRequest.getRequsetLine();
        String newLocation = httpResponse.getMessageHeader().get(Header.Location);

        if(newLocation==null){
        }
        String oldpath = oldhttpRequest.getPath();
        String oldHost = oldhttpRequest.getHost();

        clientRedirectCache.putRedirect(oldHost, oldpath, newLocation);
        history.addLog("Add a Redirect Cache entry, oldPath="+oldHost+oldpath+" , newPath="+newLocation,History.LOG_LEVEL_INFO);
        return handleMoved(oldhttpRequest, httpResponse);
    }

    private HttpRequest handleMoved(HttpRequest oldhttpRequest,HttpResponse httpResponse) throws URISyntaxException, UnsupportedHostException {
        RequsetLine oldRequsetLine = oldhttpRequest.getRequsetLine();
        MessageHeader oldRequestHeader = oldhttpRequest.getMessageHeader();
        MessageBody oldRequestBody = oldhttpRequest.getMessageBody();

        String newLocation = httpResponse.getMessageHeader().get(Header.Location);

        if(newLocation==null){
        }
        URI newLocationURI = new URI(newLocation);
        String host = newLocationURI.getHost()==null?oldRequestHeader.get(Header.Host):newLocationURI.getHost();
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
        history.addLog("Reconstructing HttpRequest caused by redirect",History.LOG_LEVEL_INFO);
        return httpRequest;
    }

    private MessageBody handleLocalStorage(HttpRequest httpRequest) throws URISyntaxException {

        String path = httpRequest.getPath();
        String host = httpRequest.getHost();
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
