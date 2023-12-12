package com.aelzohry.topsaleqatar.repository.remote.responses

import com.google.gson.annotations.SerializedName

data class Title (

	@SerializedName("ar") val ar : String,
	@SerializedName("en") val en : String
)