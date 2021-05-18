package Http.Components;

public class Header implements Component{
    String key;
    String value;

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toText() {
        return String.format("%s: %s\n", key, value);
    }
}
