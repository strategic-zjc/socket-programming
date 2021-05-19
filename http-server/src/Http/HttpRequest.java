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
    public String ToString() {
        return startLine.ToString() + headers.ToString() + body.ToString();
    }

    @Override
    public byte[] ToBytes() {
        byte[] startLine_b = startLine.ToBytes();
        byte[] headers_b = headers.ToBytes();
        byte[] body_b = body.ToBytes();
        byte[] ret = new byte[startLine_b.length + headers_b.length + body_b.length];
        System.arraycopy(startLine_b, 0, ret, 0, startLine_b.length);
        System.arraycopy(headers_b, 0, ret, startLine_b.length, headers_b.length);
        System.arraycopy(body_b, 0, ret, startLine_b.length + headers_b.length, body_b.length);
        return ret;
    }

}
