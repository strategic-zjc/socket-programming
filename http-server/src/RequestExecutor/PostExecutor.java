package RequestExecutor;
import java.io.BufferedReader;
import java.io.IOException;
import Server.*;
import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StartLine;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import StatusCode.StatusCode;


public class PostExecutor extends BasicExecutor {
    BufferedReader inFromClient;

    public PostExecutor(BufferedReader br){
        this.inFromClient = br;
    }
    public HttpResponse handle(HttpRequest request) {
        //
        StringBuffer sb = new StringBuffer();

        HttpResponse response = null;
        Headers headers = request.getHeaders();
        String contentType = headers.getValue("Content-Type");
        String contentLength = headers.getValue("Content-Length");
        if(contentType != null && contentLength != null) {
            int length = Integer.parseInt(headers.getValue("Content-Length"));
            try {
                char[] cbuf = new char[length];
                inFromClient.read(cbuf, 0, length);
                sb.append(cbuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            request.getBody().setData(sb.toString());
//            System.out.println(sb.toString());
//            System.out.println(contentType);
            StartLine startLine = request.getStartLine();
            String url = startLine.getTarget();
            switch (url){
                case "/login":
                    System.out.println("login");
                    response = handlePost("login", request);
                    break;
                case "/register":
                    System.out.println("register");
                    response = handlePost("register",request);
                    break;
                default:
                    response = generateStatusCode_405();
                    break;
            }
        }else{
            // this is the same as GET method
            response = generateStatusCode_405();
        }
        return response;
    }


    private HttpResponse handlePost(String type, HttpRequest request){
        HttpResponse response = null;
        Headers headers = request.getHeaders();
        String contentType = headers.getValue("Content-Type").split(";")[0].trim();
        Body body = request.getBody();
        if(contentType.equals("application/x-www-form-urlencoded")){
            String[] key_val = body.ToString().split("&");
            assert (key_val.length == 2);
            String username = null;
            String password = null;
            for(int i = 0; i < key_val.length; i++){
                String[] tmp = key_val[i].split("=");
                assert (tmp.length == 2);
                if(tmp[0].equals("username")){
                    username = tmp[1].trim();
                }else if (tmp[0].equals("password")){
                    password = tmp[1].trim();
                }
            }
            if(username == null || password == null){
                response = generateStatusCode_405();
            }else {
                if (type.equals("login")) {
                    if (SimpleServer.getLogin().containsKey(username)) {
                        String hint = "You have successfully login in!";
                        response = generateStatusCode_200(hint);
                    } else {
                        response = generateStatusCode_405();
                    }
                }else if (type.equals("register")){
                    if (!SimpleServer.getLogin().containsKey(username)) {
                        String hint = "You have successfully registered!";
                        SimpleServer.getLogin().put(username, password);
                        response = generateStatusCode_200(hint);
                    } else {
                        response = generateStatusCode_405();
                    }
                }
            }
        }else {
            response = generateStatusCode_405();
        }
        return response;
    }

    private HttpResponse generateStatusCode_405(){
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
        HttpResponse response = new HttpResponse(statusLine, headers, body);
        return response;
    }
    private HttpResponse generateStatusCode_200(String hint){
        StatusLine statusLine = new StatusLine(1.1, StatusCode.OK.getCode(),"OK");
        Headers headers = new Headers();
        String html_200 = "<html>\n" +
                "<head><title>Login succeed</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>" + hint +"</h1><h6>simple http-server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_200.length()));
        Body body = new Body(html_200);
        HttpResponse response = new HttpResponse(statusLine, headers, body);
        return response;
    }

}
