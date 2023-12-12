package com.aelzohry.topsaleqatar.repository.googleApi.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AutoCompleteResponse {

    @SerializedName("predictions")
    @Expose
    private ArrayList<PlaceAutocomplete> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<PlaceAutocomplete> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<PlaceAutocomplete> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
