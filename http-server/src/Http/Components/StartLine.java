package Http.Components;

import java.nio.charset.StandardCharsets;

public class StartLine implements Component{
    String method;
    String target;
    double version;

    public StartLine(String method, String target, double version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    @Override
    public String ToString() {
        return String.format("%s %s HTTP/%.1f\n", method, target, version);
    }

    @Override
    public byte[] ToBytes() {
        return this.ToString().getBytes(StandardCharsets.UTF_8);
    }

    public static StartLine String2StartLine(String s){
        String[] tmp = s.split("\\s+");
        double version = Double.parseDouble(tmp[2].split("/")[1]);
        return new StartLine(tmp[0], tmp[1], version);
    }


}
