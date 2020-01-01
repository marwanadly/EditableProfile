package com.sparknetworks.editableprofile.data.fixedValues.repository

import com.sparknetworks.editableprofile.api.ServiceProvider
import com.sparknetworks.editableprofile.data.fixedValues.model.FixedValues
import com.sparknetworks.editableprofile.data.fixedValues.model.SingleFixedValue
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function5
import timber.log.Timber

object FixedValuesRepository {

    private val fixedValuesApis = ServiceProvider.getService()

    fun getFixedValues(): Single<FixedValues> {
        return Single.zip(
            fixedValuesApis.getGenders(),
            fixedValuesApis.getEthnicities(),
            fixedValuesApis.getReligions(),
            fixedValuesApis.getFigures(),
            fixedValuesApis.getMaritalStatus(),
            Function5<ArrayList<SingleFixedValue>,
                    ArrayList<SingleFixedValue>,
                    ArrayList<SingleFixedValue>,
                    ArrayList<SingleFixedValue>,
                    ArrayList<SingleFixedValue>,
                    FixedValues> { genders, ethnicities, religions, figures, maritalStatus ->
                FixedValues(
                    genders.map { fixedValue -> fixedValue.name }.toTypedArray(),
                    ethnicities.map { fixedValue -> fixedValue.name }.toTypedArray(),
                    religions.map { fixedValue -> fixedValue.name }.toTypedArray(),
                    figures.map { fixedValue -> fixedValue.name }.toTypedArray(),
                    maritalStatus.map { fixedValue -> fixedValue.name }.toTypedArray()
                )
            })
    }
}