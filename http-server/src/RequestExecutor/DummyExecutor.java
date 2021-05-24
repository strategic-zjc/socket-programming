package RequestExecutor;

import Http.HttpRequest;
import Http.HttpResponse;

public class DummyExecutor extends BasicExecutor{
    @Override
    public HttpResponse handle(HttpRequest request) {
        return null;
    }
}
