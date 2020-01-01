package com.sparknetworks.editableprofile.ui.register.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuthException
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository
import com.sparknetworks.editableprofile.ui.login.viewModel.LoginViewModel
import com.sparknetworks.editableprofile.utli.Common
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RegisterViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    lateinit var registerViewModel: RegisterViewModel

    @Mock
    lateinit var authRepository: AuthRepository

    @Mock
    lateinit var onSuccussLiveDataObserver: Observer<String>

    @Mock
    lateinit var onErrorLiveDataObserver: Observer<String>

    @Mock
    lateinit var firebaseAuthException: FirebaseAuthException

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        registerViewModel = RegisterViewModel(authRepository)
        registerViewModel.setRxScheduler(Schedulers.trampoline())
        registerViewModel.getOnSuccussLiveData().observeForever(onSuccussLiveDataObserver)
        registerViewModel.getOnErrorLiveData().observeForever(onErrorLiveDataObserver)
    }

    @Test
    fun `Register successfully`() {
        val email = "test@test.com"
        val password = "123123"
        whenever(authRepository.register(email = email, password = password))
            .doReturn(Completable.complete())
        registerViewModel.register(email = email, password = password)
        verify(onSuccussLiveDataObserver, times(1)).onChanged(Common.AUTH_SUCCESS)
    }

    @Test
    fun `Registration failed`() {
        val email = "test@test.com"
        val password = "123123"
        whenever(authRepository.register(email = email, password = password))
            .doReturn(Completable.error(firebaseAuthException))
        registerViewModel.register(email = email, password = password)
        verify(onErrorLiveDataObserver, times(1)).onChanged(firebaseAuthException.message)
    }
}