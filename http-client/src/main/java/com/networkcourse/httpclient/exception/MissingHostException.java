package com.networkcourse.httpclient.exception;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class MissingHostException extends Exception{
    public MissingHostException(String msg){
        super(msg);
    }

    public MissingHostException(){
        super("Host is not given in headers");
    }
}

