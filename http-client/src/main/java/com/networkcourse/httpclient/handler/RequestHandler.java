package com.networkcourse.httpclient.handler;

import com.networkcourse.httpclient.client.*;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.message.factory.RedirectResponseFactory;
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
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class RequestHandler {

    private ClientRedirectCache clientRedirectCache = ClientRedirectCache.getINSTANCE();
    private ClientModifiedCache clientModifiedCache = ClientModifiedCache.getINSTANCE();
    private ClientPool clientPool = ClientPool.getINSTANCE();

    private static RequestHandler INSTANCE;

    private RequestHandler(){

    }

    public static RequestHandler getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new RequestHandler();
        }
        return INSTANCE;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, URISyntaxException, IOException, ParseException {
        if(httpRequest==null){
            System.out.println();
            return null;
        }
        String requestURI = httpRequest.getRequsetLine().getRequestURI();
        String host = null;
        String path = null;
        if(!requestURI.startsWith("/")){
            URI uri = new URI(requestURI);
            host = uri.getHost();
            Integer port = uri.getPort();
            if(port!=-1){
                host = host+":"+port;
            }
            path = uri.getPath();
        }else{
            host = httpRequest.getMessageHeader().get(Header.Host);
            path = requestURI;
        }

        //refering redirect
        String newURI = clientRedirectCache.getRedirect(host, path);
        if(newURI!=null){
            History.getINSTANCE().addHistory(httpRequest, RedirectResponseFactory.getINSTANCE().getRedirectResponse(newURI));

            httpRequest = httpRequest.clone();
            URI uri = new URI(newURI);

            host = uri.getHost();
            if(uri.getPort()!=-1){
                host = host+":"+uri.getPort();
            }
            path = uri.getPath();
            httpRequest.getRequsetLine().setRequestURI(path);
            httpRequest.getMessageHeader().put(Header.Host,host);

            System.out.println("Get from cache");
        }

        //refering localStorage
        Long timestamp =  clientModifiedCache.getModifiedTime(host,path);
        if(timestamp!=null){
            httpRequest.getMessageHeader().put(Header.If_Modified_Since, TimeUtil.toTimeString(timestamp));
        }

        ClientServer clientServer = this.clientPool.sendHttpRequest(httpRequest);
        HttpResponse httpResponse = ResponseHandler.getINSTANCE().handle(httpRequest,clientServer.getRecvStream());
        if(!httpRequest.isKeepAlive()){
            clientServer.closeConnection();
        }
        return httpResponse;
    }




}
