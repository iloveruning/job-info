package com.hfutonline.jobinfo.utils;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date 2017/12/20
 */
public class Result implements Serializable{

    private Boolean status;

    private String msg;

    private Object data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
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

    public Result(Boolean status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }
}
