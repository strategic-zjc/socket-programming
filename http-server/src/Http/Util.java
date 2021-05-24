package Http;

import Http.Components.*;

public class Util {
    public static String lineBreak = System.getProperty("line.separator");
    public static HttpRequest String2Request(String s){
        String[] tmp = s.split("\n");

        StartLine startLine = StartLine.String2StartLine(tmp[0]);

        Headers headers = new Headers();

        int i = 1;
        for(; i < tmp.length; ++i){
            if(tmp[i].equals("")) break;
            headers.addHeader(tmp[i]);
        }

        Body body = new Body();
        for(; i < tmp.length; ++i){
            body.append(tmp[i]);
        }

        return new HttpRequest(startLine, headers, body);
    }

    public static HttpResponse String2Response(String s){
        String[] tmp = s.split("\n");

        StatusLine statusLine = StatusLine.String2StatusLine(tmp[0]);

        Headers headers = new Headers();
        int i = 1;
        for(; i < tmp.length; ++i){
            if(tmp[i].equals("")) break;
            headers.addHeader(tmp[i]);
        }

        Body body = new Body();
        for(; i < tmp.length; ++i){
            body.append(tmp[i]);
        }

        return new HttpResponse(statusLine, headers, body);
    }
}
