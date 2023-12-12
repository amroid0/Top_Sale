package com.aelzohry.topsaleqatar.repository.remote.responses

import com.google.gson.annotations.SerializedName
data class CategoriesGroupd (

	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") var data : ArrayList<CategoryData>
)