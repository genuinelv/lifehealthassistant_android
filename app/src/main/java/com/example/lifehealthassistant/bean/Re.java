package com.example.lifehealthassistant.bean;


public class Re<T>{
    private Boolean flag;
    private String message;
    private T data;

    public Re(){}

    public Re(Boolean flag){
        this.flag=flag;
        this.data=null;
    }


    public Re(Boolean flag, String message,T data){
        this.flag=flag;
        this.message=message;
        this.data=data;
    }

    @Override
    public String toString() {
        return "Re{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
