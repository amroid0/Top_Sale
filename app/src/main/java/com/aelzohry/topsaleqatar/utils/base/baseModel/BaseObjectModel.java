package com.aelzohry.topsaleqatar.utils.base.baseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseObjectModel<T> extends BaseModel {

    @SerializedName("data")
    @Expose
    private T response= null;



    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
