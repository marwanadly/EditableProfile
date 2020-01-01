package com.sparknetworks.editableprofile.ui.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository


@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(profileRepository) as T
    }
}