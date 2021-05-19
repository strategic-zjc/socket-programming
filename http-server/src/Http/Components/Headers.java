package Http.Components;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Headers implements Component{
    HashMap<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    @Override
    public String ToString() {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        sb.append('\n');
        return sb.toString();
    }

    @Override
    public byte[] ToBytes() {
        return this.ToString().getBytes(StandardCharsets.UTF_8);
    }

    public void addHeader(String s){
        String[] tmp = s.split(": ");
        this.addHeader(tmp[0], tmp[1]);
    }
}
