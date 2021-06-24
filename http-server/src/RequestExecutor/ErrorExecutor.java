package RequestExecutor;

import Http.HttpRequest;
import Http.HttpResponse;

public class ErrorExecutor extends BasicExecutor{

    public ErrorExecutor(){
        this.url = "/error";
        this.method = "get";
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws Exception{
        // do something bad
        throw new Exception("error");
    }
}
