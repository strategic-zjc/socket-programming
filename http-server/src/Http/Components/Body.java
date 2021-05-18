package Http.Components;

public class Body implements Component{
    String data;

    public Body(String data) {
        this.data = data;
    }

    @Override
    public String toText() {
        return data + '\n';
    }
}
