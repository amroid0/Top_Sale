package com.aelzohry.topsaleqatar.repository.googleApi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetPlaceDetailsResponse implements Serializable {

    @SerializedName("result")
    @Expose
    private PlaceResult result;
    @SerializedName("status")
    @Expose
    private String status;

    public PlaceResult getResult() {
        return result;
    }

    public void setResult(PlaceResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
