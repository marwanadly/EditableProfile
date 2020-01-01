package com.sparknetworks.editableprofile.data.profile.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.sparknetworks.editableprofile.data.fixedValues.model.City

@IgnoreExtraProperties
data class Profile(
    var displayName: String = "",
    var profilePicture: String = "",
    var realName: String = "",
    var birthday: String = "",
    var gender: String = "",
    var ethnicity: String = "",
    var religion: String = "",
    var height: Int = 0,
    var figure: String = "",
    var maritalStatus: String = "",
    var occupation: String = "",
    var aboutMe: String = "",
    var location: City? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        val profileMap = mapOf(
            "displayName" to displayName,
            "realName" to realName,
            "birthday" to birthday,
            "gender" to gender,
            "ethnicity" to ethnicity,
            "religion" to religion,
            "height" to height,
            "figure" to figure,
            "maritalStatus" to maritalStatus,
            "occupation" to occupation,
            "aboutMe" to aboutMe,
            "location" to location
        ) as HashMap
        if(profilePicture != "") profileMap.set("profilePicture",profilePicture)
        return profileMap
    }
}