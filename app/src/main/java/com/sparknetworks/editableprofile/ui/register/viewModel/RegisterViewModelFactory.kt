package com.sparknetworks.editableprofile.ui.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory (
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(repository) as T
    }
}