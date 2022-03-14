package com.example.vpcampus.network.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.network.models.UploadViewModel
import com.example.vpcampus.repository.UploadRepository

class UploadViewModelFactory(
    private val repository:UploadRepository
) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UploadViewModel(repository) as T
    }
}