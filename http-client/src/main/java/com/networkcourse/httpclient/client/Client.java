package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.handler.RequestHandler;
import com.networkcourse.httpclient.handler.ResponseHandler;
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

    ClientModifiedCache clientModifiedCache = ClientModifiedCache.getINSTANCE();
    ClientRedirectCache clientRedirectCache = ClientRedirectCache.getINSTANCE();

    ClientPool clientPool = ClientPool.getINSTANCE();

    RequestHandler requestHandler = RequestHandler.getINSTANCE();
    ResponseHandler responseHandler = ResponseHandler.getINSTANCE();

    public HttpResponse sendHttpRequest(HttpRequest httpRequest) throws URISyntaxException, MissingHostException, UnsupportedHostException, ParseException {
        HttpResponse httpResponse = null;
        try {
            httpResponse=requestHandler.handle(httpRequest);
        } catch (IOException e) {
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
