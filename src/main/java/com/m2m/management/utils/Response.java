package com.m2m.management.utils;

import com.m2m.management.former.MsgFormer;
public class Response {
    /**
     * 请求成功返回
     * @param object
     * @return
     */
    public static MsgFormer success(Object object){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("success");
        msg.setData(object);
        return msg;
    }
    public static MsgFormer success(){
        return success(null);
    }

    public static MsgFormer error(String resultmsg){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("error");
        msg.setData(resultmsg);
        return msg;
    }
}
