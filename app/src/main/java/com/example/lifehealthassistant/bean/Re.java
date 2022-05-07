package com.example.lifehealthassistant.bean;


public class Re<T>{
    private Boolean flag;
    private T data;

    public Re(){}

    public Re(Boolean flag){
        this.flag=flag;
        this.data=null;
    }


    public Re(Boolean flag, T data){
        this.flag=flag;
        this.data=data;
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

    @Override
    public String toString() {
        return "R{" +
                "flag=" + flag +
                ", data=" + data +
                '}';
    }
}
