package Http;

import Http.Components.*;

public class HttpResponse implements Component {
    StatusLine statusLine;
    Headers headers;
    Body body;

    public HttpResponse(StatusLine statusLine, Headers headers, Body body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public String toText() {
        return statusLine.toText() + headers.toText() + '\n'+ body.toText();
    }
}
