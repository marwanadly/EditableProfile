package com.sparknetworks.editableprofile.ui.login.viewModel

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository
import com.sparknetworks.editableprofile.utli.Common
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel(private var authRepository: AuthRepository) : ViewModel() {

    private val onSuccuss = MutableLiveData<String>()
    private val onError = MutableLiveData<String>()
    private val onLoading = MutableLiveData<Int>()
    private var ioScheduler: Scheduler = Schedulers.io()

    fun setRxScheduler(scheduler: Scheduler){
        ioScheduler = scheduler
    }

    @SuppressLint("CheckResult")
    fun login(email: String, password: String) {
        onLoading.value = View.VISIBLE
        authRepository.login(email = email, password = password)
            .subscribeOn(ioScheduler)
            .subscribe({ onSuccuss.postValue(Common.AUTH_SUCCESS); onLoading.postValue(View.GONE) },
                { error -> onError.postValue(error.message); onLoading.postValue(View.GONE) })
    }

    fun getOnSuccussLiveData(): LiveData<String> {
        return onSuccuss
    }

    fun getOnErrorLiveData(): LiveData<String> {
        return onError
    }

    fun getOnLoadingLiveData(): LiveData<Int> {
        return onLoading
    }
}