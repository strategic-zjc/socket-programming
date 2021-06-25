package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.InvalidHttpRequestException;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.handler.RequestHandler;
import com.networkcourse.httpclient.handler.ResponseHandler;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/05/31
 */
public class Client {
    History history ;
    ClientRedirectCache clientRedirectCache ;
    ClientModifiedCache clientModifiedCache ;
    ClientPool clientPool;
    RequestHandler requestHandler;
    ResponseHandler responseHandler ;

    public Client(){
        clientRedirectCache = new ClientRedirectCache();
        clientModifiedCache = new ClientModifiedCache();
        history = new History();
        clientPool = new ClientPool(history);
        requestHandler = new RequestHandler(clientRedirectCache,clientModifiedCache,history);
        responseHandler = new ResponseHandler(clientRedirectCache, clientModifiedCache,this,history);
    }



    public HttpResponse sendHttpRequest(HttpRequest httpRequest) throws URISyntaxException, MissingHostException, UnsupportedHostException, ParseException {
        HttpResponse httpResponse = null;
        try {
            HttpRequest refactedHttpRequest = requestHandler.handle(httpRequest);
            ClientServer clientServer = this.clientPool.sendHttpRequest(httpRequest);
            httpResponse = this.responseHandler.handle(httpRequest,clientServer.getRecvStream());
            if(!httpRequest.isKeepAlive()){
                clientServer.closeConnection();
            }
            return httpResponse;
        } catch (IOException | InvalidHttpRequestException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    public void stdMode(){
        while (true){
            try {
                HttpRequest httpRequest = new HttpRequest(System.in);
                HttpResponse httpResponse = this.sendHttpRequest(httpRequest);
                System.out.println(httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (UnsupportedHostException e) {
                e.printStackTrace();
            } catch (MissingHostException e) {
                e.printStackTrace();
            }
        }
    }



}
