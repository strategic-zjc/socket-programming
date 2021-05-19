package Http.Components;

import java.nio.charset.StandardCharsets;

public class StatusLine implements Component{
    double version;
    int code;
    String text;

    public StatusLine(double version, int code, String text) {
        this.version = version;
        this.code = code;
        this.text = text;
    }

    @Override
    public String ToString() {
        return String.format("HTTP/%.1f %d %s\n", version, code, text);
    }

    @Override
    public byte[] ToBytes() {
        return this.ToString().getBytes(StandardCharsets.UTF_8);
    }

    public static StatusLine String2StatusLine(String s){
        String[] tmp = s.split(" ");
        double version = Double.parseDouble(tmp[0].split("/")[1]);
        return new StatusLine(version, Integer.parseInt(tmp[1]), tmp[2]);
    }
}
