package com.aelzohry.topsaleqatar.ui.autoCompleteSearchLocation.viewModel;

import static com.aelzohry.topsaleqatar.helper.Constants.MAP_API_KEY;

import androidx.lifecycle.MutableLiveData;

import com.aelzohry.topsaleqatar.repository.googleApi.GoogleNetworkShared;
import com.aelzohry.topsaleqatar.repository.googleApi.RequestListener;
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete;
import com.aelzohry.topsaleqatar.utils.SingleLiveEvent;
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * # Created by Mousa Hashihso on 26/01/2022.
 */
public class SelectMultipleLocationViewModel extends BaseViewModel {

    public SingleLiveEvent<Boolean> myLoading = new SingleLiveEvent<>();

    public MutableLiveData<List<PlaceAutocomplete>> autoCompleteResult = new MutableLiveData<>();

    public void performSearch(CharSequence charSequence) {

        if (!charSequence.toString().equals("")) {
            GoogleNetworkShared.getAsp().getGeneral().getAddressAutoComplete(MAP_API_KEY, charSequence + "", new RequestListener<ArrayList<PlaceAutocomplete>>() {
                @Override
                public void onSuccess(ArrayList<PlaceAutocomplete> data) {
                    autoCompleteResult.postValue(data);
                }

                @Override
                public void onFail(String message, int code) {

                }
            });
        }
    }

}
