package Http;

import Http.Components.*;

public class HttpRequest implements Component {
    StartLine startLine;
    Headers headers;
    Body body;

    public HttpRequest(StartLine startLine, Headers headers, Body body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public void setStartLine(StartLine startLine) {
        this.startLine = startLine;
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
        return startLine.toText() + headers.toText() + '\n' + body.toText();
    }
}
