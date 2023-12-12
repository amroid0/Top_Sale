package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.LocalizedModel
import com.google.gson.annotations.SerializedName




data class Types (

    @SerializedName("title") val title : LocalizedModel,
    @SerializedName("_id") val _id : String
)