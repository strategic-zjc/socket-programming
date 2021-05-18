package Http.Components;

import java.util.ArrayList;

public class Headers implements Component{
    ArrayList<Header> headers;

    public Headers() {
        this.headers = new ArrayList<>();
    }

    public void addHeader(Header header){
        this.headers.add(header);
    }

    @Override
    public String toText() {
        StringBuffer sb = new StringBuffer();
        for(Header h : headers){
            sb.append(h.toText());
        }
        return sb.toString();
    }
}
