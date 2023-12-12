package com.aelzohry.topsaleqatar.utils.base.baseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseListModel<T> extends BaseModel {

    @SerializedName("data")
    @Expose
    private List<T> response= null;

    public List<T> getResponse() {
        return response;
    }

    public void setResponse(List<T> response) {
        this.response = response;
    }

}
