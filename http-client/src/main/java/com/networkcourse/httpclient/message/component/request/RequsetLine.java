package com.networkcourse.httpclient.message.component.request;

import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Request-Line
 * Please refer to https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1
 * Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
 * Here CRLF = "/r/n"
 * @author fguohao
 * @date 2021/05/27
 */
public class RequsetLine {
    private String method;
    private String requestURI;
    private String httpVersion;

    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    public RequsetLine(String method, String requestURI) {
        this.method = method;
        this.requestURI = requestURI;
        this.httpVersion = DEFAULT_HTTP_VERSION;
    }

    public RequsetLine(String method, String requestURI, String httpVersion) {
        this.method = method;
        this.requestURI = requestURI;
        this.httpVersion = httpVersion;
    }



    public RequsetLine(InputStream inputStream) throws IOException {
        String line = InputStreamReaderHelper.readLine(inputStream);
        if(!line.contains(" ")){
            //TODO
            //throw new Exception("some error occured when reading ResponseLine");
        }else{
            int index = line.indexOf(" ");
            this.method = line.substring(0,index);
            line = line.substring(index+1);
        }
        if(!line.contains(" ")){
            //TODO
            //throw new Exception("some error occured when reading ResponseLine");
        }else{
            int index = line.indexOf(" ");
            this.requestURI = line.substring(0,index);
            this.httpVersion = line.substring(index+1);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.method);
        sb.append(" ");
        sb.append(this.requestURI);
        sb.append(" ");
        sb.append(this.httpVersion);
        sb.append("\r\n"); //CRLF
        return sb.toString();
    }

    @Override
    public RequsetLine clone(){
        return new RequsetLine(method, requestURI, httpVersion);
    }
}
