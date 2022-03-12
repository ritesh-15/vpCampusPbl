package com.example.vpcampus.network.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }

}