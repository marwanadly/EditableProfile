package com.sparknetworks.editableprofile.ui.profile.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sparknetworks.editableprofile.data.profile.model.Profile
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProfileViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    private lateinit var profileViewModel: ProfileViewModel

    @Mock
    lateinit var profileRepository: ProfileRepository

    @Mock
    lateinit var userProfileLiveDataObserver: Observer<Profile>

    @Mock
    lateinit var onErrorLiveDataObserver: Observer<String>

    @Mock
    lateinit var mockException: Exception

    @Mock
    lateinit var userProfile: Profile

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        profileViewModel = ProfileViewModel(profileRepository)
        profileViewModel.setRxScheduler(Schedulers.trampoline())
        profileViewModel.getUserProfileLiveData().observeForever(userProfileLiveDataObserver)
        profileViewModel.getOnError().observeForever(onErrorLiveDataObserver)
    }

    @Test
    fun `Fetch user profile successfully`() {
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.just(userProfile))
        profileViewModel.getCurrentUserProfile()
        verify(userProfileLiveDataObserver, times(1)).onChanged(userProfile)
    }

    @Test
    fun `Error in fetching user profile`() {
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.error(mockException))
        profileViewModel.getCurrentUserProfile()
        verify(onErrorLiveDataObserver, times(1)).onChanged(mockException.message)
    }
}