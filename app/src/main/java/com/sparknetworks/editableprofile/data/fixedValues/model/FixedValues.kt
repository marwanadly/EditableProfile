package com.sparknetworks.editableprofile.data.fixedValues.model

data class FixedValues (
    var genderArray: Array<String>,

    var ethnicityList: Array<String>,

    var religionList: Array<String>,

    var figureList: Array<String>,

    var maritalStatusList: Array<String>

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FixedValues

        if (!genderArray.contentEquals(other.genderArray)) return false
        if (!ethnicityList.contentEquals(other.ethnicityList)) return false
        if (!religionList.contentEquals(other.religionList)) return false
        if (!figureList.contentEquals(other.figureList)) return false
        if (!maritalStatusList.contentEquals(other.maritalStatusList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = genderArray.contentHashCode()
        result = 31 * result + ethnicityList.contentHashCode()
        result = 31 * result + religionList.contentHashCode()
        result = 31 * result + figureList.contentHashCode()
        result = 31 * result + maritalStatusList.contentHashCode()
        return result
    }
}