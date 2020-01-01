package com.sparknetworks.editableprofile.ui.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory (
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}