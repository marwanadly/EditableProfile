package com.sparknetworks.editableprofile.api

import com.sparknetworks.editableprofile.data.fixedValues.model.SingleFixedValue
import io.reactivex.Single
import retrofit2.http.GET

interface FixedValuesApis {
    @GET("gender")
    fun getGenders(): Single<ArrayList<SingleFixedValue>>

    @GET("ethnicity")
    fun getEthnicities(): Single<ArrayList<SingleFixedValue>>

    @GET("religion")
    fun getReligions(): Single<ArrayList<SingleFixedValue>>

    @GET("figure")
    fun getFigures(): Single<ArrayList<SingleFixedValue>>

    @GET("marital_status")
    fun getMaritalStatus(): Single<ArrayList<SingleFixedValue>>
}