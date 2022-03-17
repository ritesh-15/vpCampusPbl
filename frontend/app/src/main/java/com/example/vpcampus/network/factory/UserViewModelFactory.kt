package com.example.vpcampus.network.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.network.models.UserViewModel
import com.example.vpcampus.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }

}