package RequestExecutor;

import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpResponse;
import StatusCode.StatusCode;
public class Common {

    public static HttpResponse LoginSuccess(String hint){
        StatusLine statusLine = new StatusLine(1.1, StatusCode.OK.getCode(),"OK");
        Headers headers = new Headers();
        String html_200 = "<html>\n" +
                "<head><title>200 OK</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>" + hint +"</h1><h6>simple http-server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_200.length()));
        Body body = new Body(html_200);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_405(){
        StatusLine statusLine = new StatusLine(1.1, StatusCode.METHOD_NOT_ALLOWED.getCode(),"Method Not Allowed");
        Headers headers = new Headers();
        String html405 = "<html>\n" +
                "<head><title>405 Not Allowed</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>405 Not Allowed</h1><h6>simple http-server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html405.length()));
        Body body = new Body(html405);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_500(){
        StatusLine statusLine = new StatusLine(1.1, StatusCode.INTERNAL_SERVER_ERROR.getCode(),"Internal Server Error");
        Headers headers = new Headers();
        String html_500 = "<html>\n" +
                "<head><title>500 Internal Server Error</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>500 Internal Server Error</h1><h6>simple http-server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_500.length()));
        Body body = new Body(html_500);
        return new HttpResponse(statusLine, headers, body);
    }
    public static HttpResponse generateStatusCode_301(String hint){
        StatusLine statusLine = new StatusLine(1.1, StatusCode.MOVED_PERMANENTLY.getCode(),"Moved Permanently");
        Headers headers = new Headers();
        String html_301 = "<html>\n" +
                "<head><title>301 Moved Permanently</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>301 Moved Permanently</h1><h6>simple http-server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_301.length()));
        Body body = new Body(html_301);
        return new HttpResponse(statusLine, headers, body);
    }


}
