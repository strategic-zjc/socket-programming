package com.networkcourse.httpclient.message.component.commons;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class Header {
    //General Headers
    public static final String Cache_Control = "Cache-Control";
    public static final String Connection = "Connection";
    public static final String Date = "Date";
    public static final String Pragma = "Pragma";
    public static final String Trailer = "Trailer";
    public static final String Transfer_Encoding = "Transfer-Encoding";
    public static final String Upgrade = "Upgrade";
    public static final String Via = "Via";
    public static final String Warning = "Warning";

    //Entity Header
    public static final String Allow = "Allow";
    public static final String Content_Encoding = "Content-Encoding";
    public static final String Content_Language = "Content-Language";
    public static final String Content_Length = "Content-Length";
    public static final String Content_Location = "Content-Location";
    public static final String Content_MD5 = "Content-MD5";
    public static final String Content_Range = "Content-Range";
    public static final String Content_Type = "Content-Type";
    public static final String Expires = "Expires";
    public static final String Last_Modified = "Last-Modified";

    //Request Headers
    public static final String Accept = "Accept";
    public static final String Accept_Charset = "Accept-Charset";
    public static final String Accept_Encoding = "Accept-Encoding";
    public static final String Accept_Language = "Accept-Language";
    public static final String Authorization = "Authorization";
    public static final String Expect = "Expect";
    public static final String From = "From";
    public static final String Host = "Host";
    public static final String If_Match = "If-Match";
    public static final String If_Modified_Since = "If-Modified-Since";
    public static final String If_None_Match = "If-None-Match";
    public static final String If_Range = "If-Range";
    public static final String If_Unmodified_Since = "If-Unmodified-Since";
    public static final String Max_Forwards = "Max-Forwards";
    public static final String Proxy_Authorization = "Proxy-Authorization";
    public static final String Range = "Range";
    public static final String Referer = "Referer";
    public static final String TE = "TE";
    public static final String User_Agent = "User-Agent";

    //Response Headers
    public static final String Accept_Ranges = "Accept-Ranges";
    public static final String Age = "Age";
    public static final String ETag = "ETag";
    public static final String Location = "Location";
    public static final String Proxy_Authenticate = "Proxy-Authenticate";
    public static final String Retry_After = "Retry-After";
    public static final String Server = "Server";
    public static final String Vary = "Vary";
    public static final String WWW_Authenticate = "WWW-Authenticate";
}
