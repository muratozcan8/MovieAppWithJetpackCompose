package com.obss.firstapp.data.model.credit

import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("gender") val gender: Int?,
)
