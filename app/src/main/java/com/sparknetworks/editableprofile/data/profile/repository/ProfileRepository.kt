package com.sparknetworks.editableprofile.data.profile.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.sparknetworks.editableprofile.data.profile.model.Profile
import io.reactivex.Completable
import io.reactivex.Observable

object ProfileRepository {

    private val profilesCollection by lazy {
        FirebaseDatabase.getInstance().reference.child("profile")
    }
    private val currentUser by lazy {
        FirebaseAuth.getInstance().currentUser
    }
    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    fun updateProfile(profile: Profile) = Completable.create { emitter ->
        profilesCollection.child(currentUser!!.uid)
            .updateChildren(profile.toMap())
            .addOnSuccessListener {
                if (!emitter.isDisposed){
                    emitter.onComplete()
                }
            }.addOnFailureListener {
               emitter.onError(it)
            }
    }

    fun updateProfile(profile: Profile, profileImageUri: Uri) = Completable.create { emitter ->
        val firebaseStoragePath = firebaseStorage.getReference("pics/${currentUser!!.uid}")
        firebaseStoragePath.putFile(profileImageUri)
            .addOnCompleteListener { storage ->
                if (storage.isSuccessful) firebaseStoragePath.downloadUrl.addOnSuccessListener { imageUri ->
                    profile.profilePicture = imageUri.toString()
                    profilesCollection.child(currentUser!!.uid).setValue(profile)
                        .addOnSuccessListener {
                            if (!emitter.isDisposed){
                                emitter.onComplete()
                            }
                        }.addOnFailureListener {
                           emitter.onError(it)
                        }
                }

            }.addOnFailureListener {
                emitter.onError(it)
            }
    }

    fun getCurrentUserProfile() = Observable.create<Profile> { emitter ->
        profilesCollection.child(currentUser!!.uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) { emitter.onError(error.toException()) }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profile = dataSnapshot.getValue(Profile::class.java)
                if (profile != null) {
                    emitter.onNext(profile)
                    emitter.onComplete()
                }
            }
        })
    }


}