package com.networkcourse.httpclient.message.component.response;

import com.networkcourse.httpclient.message.component.request.RequsetLine;

import java.io.BufferedReader;

/**
 * Request-Line
 * Please refer to https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1
 * Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
 * Here CRLF = "/r/n"
 * @author fguohao
 * @date 2021/05/27
 */
public class ResponseLine {
    private String HTTPVersion;
    private Integer statusCode;
    private String reasonPhrase;

    public ResponseLine(String HTTPVersion, Integer statusCode, String reasonPhrase) {
        this.HTTPVersion = HTTPVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public ResponseLine(String responseLineString){
        if(!responseLineString.contains(" ")){
            //TODO
            //throw new Exception("some error occured when reading ResponseLine");
        }else{
            int index = responseLineString.indexOf(" ");
            this.HTTPVersion = responseLineString.substring(0,index);
            responseLineString = responseLineString.substring(index+1);
        }
        if(!responseLineString.contains(" ")){
            //TODO
            //throw new Exception("some error occured when reading ResponseLine");
        }else{
            int index = responseLineString.indexOf(" ");
            String statusCode = responseLineString.substring(0,index);
            try{
                this.statusCode = Integer.parseInt(statusCode);
            }catch (Exception e){
                //TODO
            }
            this.reasonPhrase = responseLineString.substring(index+1);
        }
    }

    public String getHTTPVersion() {
        return HTTPVersion;
    }

    public void setHTTPVersion(String HTTPVersion) {
        this.HTTPVersion = HTTPVersion;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.HTTPVersion);
        sb.append(" ");
        sb.append(this.statusCode);
        sb.append(" ");
        sb.append(this.reasonPhrase);
        sb.append("\r\n"); //CRLF
        return sb.toString();
    }

    @Override
    public ResponseLine clone(){
        return new ResponseLine(HTTPVersion, (int)statusCode, reasonPhrase);
    }


}
