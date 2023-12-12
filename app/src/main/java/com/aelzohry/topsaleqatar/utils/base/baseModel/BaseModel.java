package com.aelzohry.topsaleqatar.utils.base.baseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseModel {


    public BaseModel() {
    }

    public BaseModel(Boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @SerializedName("message")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
