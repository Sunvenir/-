package softgroup.backend.entity;

public class Result<T> {
    private int code;
    private String message;
    private T detail;
    private T data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public Result(int code, String message, T detail, T data) {
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.data = data;
    }
    //getter
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getDetail() {
        return detail;
    }

    public T getData(){ return data;}

    //setter
    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    public void setData(T data){this.data = data;}


}
