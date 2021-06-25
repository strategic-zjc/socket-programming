package com.networkcourse.httpclient.handler;

import com.networkcourse.httpclient.client.*;
import com.networkcourse.httpclient.exception.InvalidHttpRequestException;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.commons.URI;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.message.factory.RedirectResponseFactory;
import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class RequestHandler {

    private ClientRedirectCache clientRedirectCache ;
    private ClientModifiedCache clientModifiedCache ;
    private History history;

    public RequestHandler(ClientRedirectCache clientRedirectCache, ClientModifiedCache clientModifiedCache,History history) {
        this.clientRedirectCache = clientRedirectCache;
        this.clientModifiedCache = clientModifiedCache;
        this.history = history;
    }



    public HttpRequest handle(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, URISyntaxException, IOException, ParseException, InvalidHttpRequestException {
        //未提供有效的request请求，抛出异常
        if(httpRequest==null){
            throw new InvalidHttpRequestException("invaild httpRequest, the request is null");
        }

        //refering redirect
        httpRequest = findRedirectCache(httpRequest);

        //refering localStorage
        httpRequest = findLastModifiedCache(httpRequest);

        return httpRequest;


    }

    private HttpRequest findRedirectCache(HttpRequest httpRequest){
        String host = httpRequest.getHost();
        String path = httpRequest.getPath();
        URI newURI = clientRedirectCache.getRedirect(host, path);
        if(newURI!=null){
            history.addHistory(httpRequest, RedirectResponseFactory.getINSTANCE().getRedirectResponse(newURI.toString()));
            httpRequest = httpRequest.clone();
            httpRequest.getRequsetLine().setRequestURI(newURI.getPath());
            httpRequest.getMessageHeader().put(Header.Host, newURI.getHost());
            history.addLog("Successfully find Redirect Cache entry, oldPath="+host+path+" , newPath="+newURI.toString(),History.LOG_LEVEL_INFO);
        }
        return httpRequest;
    }

    private HttpRequest findLastModifiedCache(HttpRequest httpRequest){
        String host = httpRequest.getHost();
        String path = httpRequest.getPath();
        Long timestamp =  clientModifiedCache.getModifiedTime(host,path);
        if(timestamp!=null){
            history.addLog("Successfully find Last Modified Cache entry, Path="+host+path+" , modifiedTime="+TimeUtil.toTimeString(timestamp),History.LOG_LEVEL_INFO);
            httpRequest.getMessageHeader().put(Header.If_Modified_Since, TimeUtil.toTimeString(timestamp));
        }
        return httpRequest;
    }


}
