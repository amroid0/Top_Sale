package com.aelzohry.topsaleqatar.repository.googleApi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kotlinx.android.parcel.Parcelize;


public class PlaceAutocomplete implements Serializable, Parcelable {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("place_id")
    @Expose
    private String placeId="";
    @SerializedName("structured_formatting")
    @Expose
    private NameDetails nameDetails;

    @Expose
    private LatLng latLng;


    public PlaceAutocomplete(String placeId, String description) {
        this.placeId = placeId;
        this.description = description;
    }

    public PlaceAutocomplete(String description, LatLng latLng) {
        this.description = description;
        this.latLng = latLng;
    }


    protected PlaceAutocomplete(Parcel in) {
        description = in.readString();
        placeId = in.readString();
        nameDetails = in.readParcelable(NameDetails.class.getClassLoader());
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<PlaceAutocomplete> CREATOR = new Creator<PlaceAutocomplete>() {
        @Override
        public PlaceAutocomplete createFromParcel(Parcel in) {
            return new PlaceAutocomplete(in);
        }

        @Override
        public PlaceAutocomplete[] newArray(int size) {
            return new PlaceAutocomplete[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return TextUtils.isEmpty(placeId)?placeId:"";
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public NameDetails getNameDetails() {
        return nameDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(placeId);
        parcel.writeParcelable(nameDetails, i);
        parcel.writeParcelable(latLng, i);
    }


    public class NameDetails implements Serializable, Parcelable {

        @SerializedName("main_text")
        @Expose
        public String mainText;

        @SerializedName("secondary_text")
        @Expose
        public String secondaryText;

        protected NameDetails(Parcel in) {
            mainText = in.readString();
            secondaryText = in.readString();
        }

        public final Creator<NameDetails> CREATOR = new Creator<NameDetails>() {
            @Override
            public NameDetails createFromParcel(Parcel in) {
                return new NameDetails(in);
            }

            @Override
            public NameDetails[] newArray(int size) {
                return new NameDetails[size];
            }
        };

        public String getMainText() {
            return mainText;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(mainText);
            parcel.writeString(secondaryText);
        }
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
