package com.sparknetworks.editableprofile.ui.login.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuthException
import com.nhaarman.mockitokotlin2.*
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository
import com.sparknetworks.editableprofile.utli.Common
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class LoginViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel

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
        loginViewModel = LoginViewModel(authRepository)
        loginViewModel.setRxScheduler(Schedulers.trampoline())
        loginViewModel.getOnSuccussLiveData().observeForever(onSuccussLiveDataObserver)
        loginViewModel.getOnErrorLiveData().observeForever(onErrorLiveDataObserver)
    }

    @Test
    fun `login successfully`() {
        val email = "test@test.com"
        val password = "123123"
        whenever(authRepository.login(email = email, password = password))
            .doReturn(Completable.complete())
        loginViewModel.login(email = email, password = password)
        verify(onSuccussLiveDataObserver, times(1)).onChanged(Common.AUTH_SUCCESS)
    }

    @Test
    fun `login with wrong credentials`() {
        val email = "test@test.com"
        val password = "123123"
        whenever(authRepository.login(email = email, password = password))
            .doReturn(Completable.error(firebaseAuthException))
        loginViewModel.login(email = email, password = password)
        verify(onErrorLiveDataObserver, times(1)).onChanged(firebaseAuthException.message)
    }
}