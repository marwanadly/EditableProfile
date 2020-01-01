package com.sparknetworks.editableprofile.ui.profile.viewModel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sparknetworks.editableprofile.data.fixedValues.model.City
import com.sparknetworks.editableprofile.data.fixedValues.model.FixedValues
import com.sparknetworks.editableprofile.data.fixedValues.repository.CitiesRepository
import com.sparknetworks.editableprofile.data.fixedValues.repository.FixedValuesRepository
import com.sparknetworks.editableprofile.data.profile.model.Profile
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository
import com.sparknetworks.editableprofile.utli.Common
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class EditProfileViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    private lateinit var editProfileViewModel: EditProfileViewModel

    @Mock
    lateinit var profileRepository: ProfileRepository

    @Mock
    lateinit var citiesRepository: CitiesRepository

    @Mock
    lateinit var fixedValuesRepository: FixedValuesRepository

    @Mock
    lateinit var fixedValuesObserver: Observer<FixedValues>

    @Mock
    lateinit var searchCityObserver: Observer<List<City?>>

    @Mock
    lateinit var onSuccussLiveDataObserver: Observer<Int>

    @Mock
    lateinit var onErrorLiveDataObserver: Observer<String>

    @Mock
    lateinit var fixedValues: FixedValues

    @Mock
    lateinit var citiesList: List<City?>

    @Mock
    lateinit var exception: Exception

    @Mock
    lateinit var userProfile: Profile

    @Mock
    lateinit var profilePicture: Uri

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        editProfileViewModel = EditProfileViewModel(fixedValuesRepository, citiesRepository, profileRepository)
        editProfileViewModel.setRxScheduler(Schedulers.trampoline())
        editProfileViewModel.getSearchCitiesResultLiveData().observeForever(searchCityObserver)
        editProfileViewModel.getOnUpdateSuccess().observeForever(onSuccussLiveDataObserver)
        editProfileViewModel.getOnUpdateError().observeForever(onErrorLiveDataObserver)
        editProfileViewModel.getFixedValuesLiveData().observeForever(fixedValuesObserver)
    }

    @Test
    fun `Fetch fixed values from server`(){
        whenever(fixedValuesRepository.getFixedValues()).doReturn(Single.just(fixedValues))
        editProfileViewModel.getFixedValues()
        verify(fixedValuesObserver, times(1)).onChanged(fixedValues)
    }


    @Test
    fun `Search for city by name`() {
        whenever(citiesRepository.searchForCity("Berlin")).doReturn(Observable.just(citiesList))
        editProfileViewModel.searchForCity("Berlin")
        verify(searchCityObserver, times(1)).onChanged(citiesList)
    }

    @Test
    fun `Update profile without profile picture`(){
        whenever(profileRepository.updateProfile(userProfile)).doReturn(Completable.complete())
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.just(userProfile))
        editProfileViewModel.updateProfile(userProfile, null)
        editProfileViewModel.getCurrentUserProfile()
        verify(onSuccussLiveDataObserver, times(1)).onChanged(Common.SUCCESS)
    }

    @Test
    fun `Failed to update profile without profile picture`(){
        whenever(profileRepository.updateProfile(userProfile)).doReturn(Completable.error(exception))
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.just(userProfile))
        editProfileViewModel.updateProfile(userProfile, null)
        editProfileViewModel.getCurrentUserProfile()
        verify(onErrorLiveDataObserver, times(1)).onChanged(exception.message)
    }

    @Test
    fun `Update profile with profile picture`(){
        whenever(profileRepository.updateProfile(userProfile, profilePicture)).doReturn(Completable.complete())
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.just(userProfile))
        editProfileViewModel.updateProfile(userProfile, profilePicture)
        editProfileViewModel.getCurrentUserProfile()
        verify(onSuccussLiveDataObserver, times(1)).onChanged(Common.SUCCESS)
    }

    @Test
    fun `Failed to update profile with profile picture`(){
        whenever(profileRepository.updateProfile(userProfile, profilePicture)).doReturn(Completable.error(exception))
        whenever(profileRepository.getCurrentUserProfile()).doReturn(Observable.just(userProfile))
        editProfileViewModel.updateProfile(userProfile, profilePicture)
        editProfileViewModel.getCurrentUserProfile()
        verify(onErrorLiveDataObserver, times(1)).onChanged(exception.message)
    }

}