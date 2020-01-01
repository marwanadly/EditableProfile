package com.sparknetworks.editableprofile.data.auth.repository

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable

object AuthRepository {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!emitter.isDisposed) {
                    emitter.onComplete()
                }
            }.addOnFailureListener { emitter.onError(it) }
    }

    fun login(email: String, password: String) = Completable.create { emitter ->
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (!emitter.isDisposed) {
                emitter.onComplete()
            }
        }.addOnFailureListener { emitter.onError(it) }
    }
}