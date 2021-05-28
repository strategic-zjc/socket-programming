package com.networkcourse.httpclient.exception;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class UnsupportedHostException extends Exception{
    public UnsupportedHostException(String msg){
        super(msg);
    }

    public UnsupportedHostException(){
        super("An error occurred during extracting Host");
    }
}
