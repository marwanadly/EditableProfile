package com.sparknetworks.editableprofile.ui.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparknetworks.editableprofile.data.fixedValues.repository.CitiesRepository
import com.sparknetworks.editableprofile.data.fixedValues.repository.FixedValuesRepository
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository

@Suppress("UNCHECKED_CAST")
class EditProfileViewModelFactory (
    private val fixedValuesRepository: FixedValuesRepository,
    private val citiesRepository: CitiesRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(fixedValuesRepository, citiesRepository, profileRepository) as T
    }
}