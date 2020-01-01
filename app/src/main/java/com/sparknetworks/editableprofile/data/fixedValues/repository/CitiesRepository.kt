package com.sparknetworks.editableprofile.data.fixedValues.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sparknetworks.editableprofile.data.fixedValues.model.City
import io.reactivex.Observable

object CitiesRepository {

    private val citiesCollection by lazy {
        FirebaseDatabase.getInstance().reference.child("cities")
            .child("cities")
    }

    fun searchForCity(cityName: String) = Observable.create<List<City?>> { emitter ->
        citiesCollection
            .orderByChild("city")
            .startAt(cityName)
            .endAt(cityName + "\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val searchResult =
                        dataSnapshot.children.map { cities -> cities.getValue(City::class.java) }
                    if(!emitter.isDisposed){
                        emitter.onNext(searchResult)
                        emitter.onComplete()
                    }
                }
            })
    }
}