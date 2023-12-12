package com.aelzohry.topsaleqatar.repository.googleApi;

import android.annotation.SuppressLint;
import android.util.Log;

import com.aelzohry.topsaleqatar.App;
import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete;
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceResult;
import com.google.gson.Gson;

import java.util.ArrayList;

public class General {

    private static final String TAG_SUCCESS = "Response success";
    private static final String TAG_ERROR = "Response error";
    private static final String TAG_PARAMS = "Params";
    private final GoogleRetrofitModel googleRetrofitModel = new GoogleRetrofitModel();
    private Gson gson = new Gson();

    public General() {
    }


    @SuppressLint("CheckResult")
    public void getAddressAutoComplete(String key, String input, final RequestListener<ArrayList<PlaceAutocomplete>> mRequestListener) {
        googleRetrofitModel.getAddressAutoComplete(key, input)
                .subscribe(appResponse -> {
                    Log.e(TAG_SUCCESS, appResponse.toString());
                    if (appResponse.getStatus().equalsIgnoreCase("OK")) {
                        ArrayList<PlaceAutocomplete> placeAutocompletes = appResponse.getPredictions();
                        if (placeAutocompletes != null && placeAutocompletes.size() > 0) {
                            mRequestListener.onSuccess(placeAutocompletes);
                        }
                    } else {
                        mRequestListener.onFail(App.Companion.getContext().getString(R.string.error), 0);

                    }
                }, throwable -> {
                    Log.e(TAG_ERROR, throwable.getMessage() + "");
                    mRequestListener.onFail(App.Companion.getContext().getString(R.string.error), 0);
                });
    }


    @SuppressLint("CheckResult")
    public void getPlaceById(String key, String placeId, String fields, final RequestListener<PlaceResult> mRequestListener) {
        googleRetrofitModel.getPlaceById(key, placeId, fields)
                .subscribe(appResponse -> {
                    Log.e(TAG_SUCCESS, appResponse.toString());
                    if (appResponse.getStatus().equalsIgnoreCase("OK")) {
                        PlaceResult placeResult = appResponse.getResult();
                        mRequestListener.onSuccess(placeResult);
                    } else {
                        mRequestListener.onFail(App.Companion.getContext().getString(R.string.error), 0);
                    }
                }, throwable -> {
                    Log.e(TAG_ERROR, throwable.getMessage() + "");
                    mRequestListener.onFail(App.Companion.getContext().getString(R.string.error), 0);
                });
    }


}
