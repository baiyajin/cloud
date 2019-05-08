package com.baiyajin.util.u;

import java.util.Map;

public class ReturnModel {
    private Boolean success;
    private String msg;
    private Object data;
    /**
     * 返回页面实体封装
     * @param i 返回状态，0：失败，1：成功
     */
    public ReturnModel(int i){
        if(i==0){
            this.success = false;
            this.msg = "操作失败";
        }else{
            this.success = true;
            this.msg = "操作成功";
        }
    }

    /**
     * 返回页面实体封装
     * @param i 返回状态，0：失败，1：成功
     * @param  bodyMap 返回数据内容
     */
    public ReturnModel(int i,Object bodyMap){
        if(i==0){
            this.success = false;
            this.msg = "操作失败";
        }else{
            this.success = true;
            this.msg = "操作成功";
        }
        this.data = bodyMap;
    }



    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
