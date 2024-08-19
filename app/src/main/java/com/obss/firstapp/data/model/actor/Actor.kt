package com.obss.firstapp.data.model.actor

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("homepage") val homepage: Any?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("gender") val gender: Int?,
)
