package StatusCode;
public enum StatusCode {
    OK(200),
    MOVED_PERMANENTLY(301),FOUND(302),NOT_MODIFIED(304),
    NOT_FOUND(404),METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private int code;

    StatusCode(int code){
        this.code=code;
    }
    public int getCode(){return code;}
    public void setCode(int code){
        this.code = code;
    }
}
