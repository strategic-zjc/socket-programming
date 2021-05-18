package Http.Components;

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
    public String toText() {
        return String.format("HTTP/%.1f %d %s\n", version, code, text);
    }
}
