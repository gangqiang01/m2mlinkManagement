package com.m2m.management.former;

public class MsgFormer<T> {

    /*提示信息 */
    private String msg;

    /*具体内容*/
    private  T data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
