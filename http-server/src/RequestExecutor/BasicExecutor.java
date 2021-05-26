package RequestExecutor;

import Http.HttpRequest;
import Http.HttpResponse;

public  abstract class BasicExecutor {
    /**
     * eg /login
     */
    String url;

    /**
     * eg POST
     */
    String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public abstract HttpResponse handle (HttpRequest request);
}
