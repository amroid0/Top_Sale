package com.aelzohry.topsaleqatar.model;

import android.os.Parcel;
import android.os.Parcelable;
import kotlinx.android.parcel.Parcelize;

/**
 * # Created by Mousa Hashihso on 22/12/2021.
 */
@Parcelize
public class CustomImage implements Parcelable{
    private int id;
    private Boolean isChecked;
    private String text;

    public CustomImage(int id, String text, Boolean isChecked) {
        this.id = id;
        this.isChecked = isChecked;
        this.text = text;
    }

    protected CustomImage(Parcel in) {
        id = in.readInt();
        byte tmpIsChecked = in.readByte();
        isChecked = tmpIsChecked == 0 ? null : tmpIsChecked == 1;
    }

    public static final Creator<CustomImage> CREATOR = new Creator<CustomImage>() {
        @Override
        public CustomImage createFromParcel(Parcel in) {
            return new CustomImage(in);
        }

        @Override
        public CustomImage[] newArray(int size) {
            return new CustomImage[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeByte((byte) (isChecked == null ? 0 : isChecked ? 1 : 2));
    }
}

