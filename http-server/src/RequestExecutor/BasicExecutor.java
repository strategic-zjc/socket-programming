package RequestExecutor;

import Http.HttpRequest;
import Http.HttpResponse;

public  abstract class BasicExecutor {
    public abstract HttpResponse handle (HttpRequest request);
}
