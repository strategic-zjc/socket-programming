package RequestExecutor;

import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import jdk.internal.util.xml.impl.Pair;

import java.io.File;
import java.io.FileInputStream;

public class StaticResourceHandler extends BasicExecutor{

    public static boolean isStaticTarget(String target){
        target = target.substring(target.lastIndexOf("/") + 1);
        return target.contains(".");
    }

    public HttpResponse handle(HttpRequest request){
        StatusLine statusLine = new StatusLine(1.1, 200, "OK");
        Headers headers = new Headers();
        Body body = new Body();
        String target = request.getStartLine().getTarget();

        if(target.endsWith(".html")){
            headers.addHeader("Content-Type", "text/html");
        }
        else if(target.endsWith(".png")){
            headers.addHeader("Content-Type", "image/png");
        }
        // todo: 添加其他格式

        String path = target.substring(target.lastIndexOf("/") + 1);
        File f = new File(path);
        headers.addHeader("Content-Length", Long.toString(f.length()));

        byte[] bytesArray = new byte[(int) f.length()];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResponse(new StatusLine(1.1, 404, "Not Found"), new Headers(), new Body());
        }
        body.setData(bytesArray);

        return new HttpResponse(statusLine, headers, body);
    }

}
