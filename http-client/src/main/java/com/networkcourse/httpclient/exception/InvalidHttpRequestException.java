package com.networkcourse.httpclient.exception;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class InvalidHttpRequestException extends Exception{
    public InvalidHttpRequestException(String msg){
        super(msg);
    }

    public InvalidHttpRequestException(){
        super("Invalid httpRequest");
    }
}
