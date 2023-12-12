package com.aelzohry.topsaleqatar.ui.ads.filter

import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.google.android.gms.maps.model.LatLng

interface FilterListener {
    fun onFilterClickedApplyListener(
        category: Category?,
        selectedSubCat: LocalStanderModel?,
        selectedType: LocalStanderModel?,
        selectedCarMake: StanderModel?,
        selectedModelLocal: ArrayList<StanderModel>?,
        selectedSubModelLocal: ArrayList<StanderModel>?,
        selectedCarShow: User?,
        selectedYear: ArrayList<StanderModel>?,
        selectedGearbox: ArrayList<StanderModel>?,
        selectedEngineSize: ArrayList<StanderModel>?,

        selectedFuelType: ArrayList<StanderModel>?,
        selectedEngineDriveSystem: ArrayList<StanderModel>?,
        selectedCarColor: ArrayList<StanderModel>?,

        selectedKm: ArrayList<StanderModel>?,
        selectedRegion: LocalStanderModel?,
        selectedCity: LocalStanderModel?,
        fromPrice: String?, toPrice: String?,
        fromRooms: String?, toRooms: String?,
        fromBathRooms: String?, toBathRooms: String?,
        selectedLatLng: LatLng?,
         distance: Int,
        locationList: ArrayList<PlaceAutocomplete>
    )
}