package com.aelzohry.topsaleqatar.utils.imageSlider;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageSlider implements Serializable {

    @Expose
    private String smallMediaUrl;
    @SerializedName("medium_media_url")
    @Expose
    private String mediumMediaUrl;
    @SerializedName("large_media_url")
    @Expose
    private String largeMediaUrl;

    @Expose
    private Boolean isImage = true;

    public ImageSlider() {
    }

    public ImageSlider(String smallMediaUrl, String mediumMediaUrl, String largeMediaUrl,Boolean isImage) {
        this.smallMediaUrl = smallMediaUrl;
        this.mediumMediaUrl = mediumMediaUrl;
        this.largeMediaUrl = largeMediaUrl;
        this.isImage = isImage;
    }


    public ImageSlider(String mediumMediaUrl) {
        this.mediumMediaUrl = mediumMediaUrl;
    }

    public String getMediumMediaUrl() {
        return mediumMediaUrl;
    }

    public void setMediumMediaUrl(String mediumMediaUrl) {
        this.mediumMediaUrl = mediumMediaUrl;
    }

    public String getLargeMediaUrl() {
        return largeMediaUrl;
    }

    public void setLargeMediaUrl(String largeMediaUrl) {
        this.largeMediaUrl = largeMediaUrl;
    }

    public String getSmallMediaUrl() {
        return smallMediaUrl;
    }

    public Boolean getImage() {
        return isImage!=null && isImage;
    }
}
