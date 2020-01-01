package com.sparknetworks.editableprofile.ui.profile.viewModel

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparknetworks.editableprofile.data.fixedValues.model.City
import com.sparknetworks.editableprofile.data.fixedValues.model.FixedValues
import com.sparknetworks.editableprofile.data.fixedValues.repository.CitiesRepository
import com.sparknetworks.editableprofile.data.fixedValues.repository.FixedValuesRepository
import com.sparknetworks.editableprofile.data.profile.model.Profile
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository
import com.sparknetworks.editableprofile.utli.Common
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class EditProfileViewModel(private var fixedValuesRepository: FixedValuesRepository,
                           private var citiesRepository: CitiesRepository,
                           private var profileRepository: ProfileRepository) : ViewModel() {

    private val searchCitiesResultLiveData = MutableLiveData<List<City?>>()
    private val onUpdateSuccess = MutableLiveData<Int>()
    private val onUpdateError = MutableLiveData<String>()
    private val onLoading = MutableLiveData<Int>()
    private val userProfileLiveData = MutableLiveData<Profile>()
    private val fixedValuesLiveData = MutableLiveData<FixedValues>()
    private val disposable = CompositeDisposable()

    private var ioScheduler: Scheduler = Schedulers.io()

    fun setRxScheduler(scheduler: Scheduler){
        ioScheduler = scheduler
    }

    @SuppressLint("CheckResult")
    fun getFixedValues() {
        fixedValuesRepository.getFixedValues()
            .subscribeOn(ioScheduler)
            .subscribe({
                fixedValuesLiveData.postValue(it)
            },{ error-> Timber.e(error) })
    }

    @SuppressLint("CheckResult")
    fun searchForCity(cityName: String) {
        citiesRepository.searchForCity(cityName = cityName)
            .subscribeOn(ioScheduler)
            .subscribe({ searchCitiesResultLiveData.value = it },
                { error -> Timber.e(error) })
    }

    @SuppressLint("CheckResult")
    fun updateProfile(profile: Profile, profileImageUri: Uri?) {
        onLoading.value = View.VISIBLE
        if (profileImageUri != null) {
            profileRepository.updateProfile(profile = profile, profileImageUri = profileImageUri)
                .subscribeOn(ioScheduler)
                .subscribe({ onUpdateSuccess.postValue(Common.SUCCESS)
                    onLoading.postValue(View.GONE)
                    getCurrentUserProfile()
                },
                    { error -> onUpdateError.postValue(error.message); onLoading.postValue(View.GONE) })
        } else {
            profileRepository.updateProfile(profile = profile)
                .subscribeOn(ioScheduler)
                .subscribe({ onUpdateSuccess.postValue(Common.SUCCESS)
                    onLoading.postValue(View.GONE)
                    getCurrentUserProfile()
                },
                    { error -> onUpdateError.postValue(error.message); onLoading.postValue(View.GONE) })
        }
    }

    @SuppressLint("CheckResult")
    fun getCurrentUserProfile(){
        profileRepository.getCurrentUserProfile()
            .subscribeOn(ioScheduler)
            .subscribe{ userProfileLiveData.postValue(it) }
    }

    fun clearDisposable() {
        disposable.clear()
    }

    fun getSearchCitiesResultLiveData(): LiveData<List<City?>> {
        return searchCitiesResultLiveData
    }

    fun getOnUpdateSuccess(): LiveData<Int> {
        return onUpdateSuccess
    }

    fun getOnUpdateError(): LiveData<String> {
        return onUpdateError
    }

    fun getOnLoading(): LiveData<Int> {
        return onLoading
    }

    fun getUserProfileLiveData(): LiveData<Profile> {
        return userProfileLiveData
    }

    fun getFixedValuesLiveData(): LiveData<FixedValues> {
        return fixedValuesLiveData
    }

}