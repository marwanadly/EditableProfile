package com.sparknetworks.editableprofile.ui.profile.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparknetworks.editableprofile.data.profile.model.Profile
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ProfileViewModel(private val profileRepository:ProfileRepository) : ViewModel() {

    private val userProfileLiveData = MutableLiveData<Profile>()
    private val onError = MutableLiveData<String>()
    private var ioScheduler: Scheduler = Schedulers.io()

    fun setRxScheduler(scheduler: Scheduler){
        ioScheduler = scheduler
    }

    @SuppressLint("CheckResult")
    fun getCurrentUserProfile(){
        profileRepository.getCurrentUserProfile()
            .subscribeOn(ioScheduler)
            .subscribe ({ userProfileLiveData.postValue(it) },
                {error -> onError.postValue(error.message)})
    }

    fun getUserProfileLiveData(): LiveData<Profile> {
        return userProfileLiveData
    }

    fun getOnError(): LiveData<String> {
        return onError
    }

}