package Http.Components;

public class StartLine implements Component{
    String method;
    String target;
    double version;

    public StartLine(String method, String target, double version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }


    @Override
    public String toText() {
        return String.format("%s %s HTTP/%.1f\n", method, target, version);
    }

    public static StartLine String2StartLine(String s){
        String[] tmp = s.split(" ");
        double version = Double.parseDouble(tmp[2].split("/")[1]);
        return new StartLine(tmp[0], tmp[1], version);
    }
}
