package Http.Components;

public class Body implements Component{
    // 在传输二进制文件时有问题
    String data;

    public Body(){
    }

    public Body(String data) {
        this.data = data;
    }

    public void append(String s){
        this.data += s;
    }

    @Override
    public String toText() {
        return data + '\n';
    }
}
