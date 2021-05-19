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
    public String ToString() {
        return statusLine.ToString() + headers.ToString() + '\n'+ body.ToString();
    }

    @Override
    public byte[] ToBytes() {
        byte[] statusLine_b = statusLine.ToBytes();
        byte[] headers_b = headers.ToBytes();
        byte[] body_b = body.ToBytes();
        byte[] ret = new byte[statusLine_b.length + headers_b.length + body_b.length];
        System.arraycopy(statusLine_b, 0, ret, 0, statusLine_b.length);
        System.arraycopy(headers_b, 0, ret, statusLine_b.length, headers_b.length);
        System.arraycopy(body_b, 0, ret, statusLine_b.length + headers_b.length, body_b.length);
        return ret;
    }
}
