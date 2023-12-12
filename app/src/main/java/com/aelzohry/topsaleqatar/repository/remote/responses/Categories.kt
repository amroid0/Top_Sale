package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.LocalizedModel
import com.google.gson.annotations.SerializedName


data class Categories (

    @SerializedName("title") val title : LocalizedModel,
    @SerializedName("type") val type : String,
    @SerializedName("subcategories") val subcategories : List<String>,
    @SerializedName("types") val types : List<Types>,
    @SerializedName("_id") val _id : String,
    @SerializedName("image") val image : String,
    @SerializedName("realmId") val realmId : String,
    @SerializedName("mainCategory") val mainCategory : String,
    @SerializedName("mainCategoryId") val mainCategoryId : String
)