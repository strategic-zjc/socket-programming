package RequestExecutor;

import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import jdk.internal.util.xml.impl.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class StaticResourceHandler extends BasicExecutor {

    public static HashMap<String, String> MovedPermanentlyResource = new HashMap<>();
    public static HashMap<String, String> MovedTemporarilyResource = new HashMap<>();
    // todo:304状态码

    public static HashMap<String, String> ModifiedTime = new HashMap<>();

    public StaticResourceHandler() {
        MovedPermanentlyResource.put("/movedPic.png", "/pic.png");
        MovedPermanentlyResource.put("/movedIndex.html", "/index.html");
        MovedTemporarilyResource.put("/movedPic2.png", "/pic.png");
        MovedTemporarilyResource.put("/movedIndex2.html", "/index.html");
    }

    public static boolean isStaticTarget(String target) {
        target = target.substring(target.lastIndexOf("/") + 1);
        return target.contains(".");
    }

    public HttpResponse handle(HttpRequest request) {
        StatusLine statusLine = null;
        Headers headers = new Headers();
        Body body = new Body();
        String target = request.getStartLine().getTarget();

        if (MovedPermanentlyResource.containsKey(target)) {
            statusLine = new StatusLine(1.1, 301, "Moved Permanently");
            target = MovedPermanentlyResource.get(target);
        }
        else if (MovedTemporarilyResource.containsKey(target)) {
            String hint = "The resource is temporarily moved to http://localhost:5000" + MovedTemporarilyResource.get(target);
            return new HttpResponse(new StatusLine(1.1, 302, "Found"), new Headers(), new Body(hint));
        }
        else {
            statusLine = new StatusLine(1.1, 200, "OK");
        }

        if (target.endsWith(".html")) {
            headers.addHeader("Content-Type", "text/html");
        } else if (target.endsWith(".png")) {
            headers.addHeader("Content-Type", "image/png");
        }
        // todo: 添加其他格式

        String path = target.substring(target.lastIndexOf("/") + 1);
        File f = new File(path);
        headers.addHeader("Content-Length", Long.toString(f.length()));

        byte[] bytesArray = new byte[(int) f.length()];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            //read file into bytes[]
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(new StatusLine(1.1, 404, "Not Found"), new Headers(), new Body());
        }


        body.setData(bytesArray);

        return new HttpResponse(statusLine, headers, body);
    }

}
