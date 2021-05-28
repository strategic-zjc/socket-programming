package com.networkcourse.httpclient.message.component.request;

/**
 * Request Method
 * Please refer to https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1#sec5.1.1
 * Method         = "OPTIONS"
 *                | "GET"
 *                | "HEAD"
 *                | "POST"
 *                | "PUT"
 *                | "DELETE"
 *                | "TRACE"
 *                | "CONNECT"
 *                | extension-method
 *  extension-method = token
 * @author fguohao
 * @date 2021/05/27
 */
public class Method {
    public static final String OPTIONS = "OPTIONS";
    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String TRACE = "TRACE";
    public static final String CONNECT = "CONNECT";
}
