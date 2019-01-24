package com.m2m.management.former;

public class MsgFormer<T> {

    /*提示信息 */
    private String status;

    /*具体内容*/
    private  T data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
