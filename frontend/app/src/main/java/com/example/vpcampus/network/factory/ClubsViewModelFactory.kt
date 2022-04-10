package com.example.vpcampus.network.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository

class ClubsViewModelFactory(
    private val repository: ClubRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClubsViewModel(repository) as T
    }
}