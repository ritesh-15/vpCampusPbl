package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.userApi.UpdateProfileBody
import com.example.vpcampus.api.userApi.UpdateProfileResponse
import com.example.vpcampus.repository.UserRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
):ViewModel() {

    private var _updateProfileResponse = MutableLiveData<ScreenState<UpdateProfileResponse>>()

    val updateProfileResponse:LiveData<ScreenState<UpdateProfileResponse>>
        get() = _updateProfileResponse


    private var _userDetailsResponse = MutableLiveData<ScreenState<UpdateProfileResponse>>()

    val userDetailsResponse:LiveData<ScreenState<UpdateProfileResponse>>
        get() = _userDetailsResponse

    fun getUserDetails(
        headers: Map<String, String>
    ){
        viewModelScope.launch {
            _userDetailsResponse.value = ScreenState.Loading(null)

            val response = repository.getUserDetails(headers)

            if(response.isSuccessful){
                _userDetailsResponse.value = ScreenState.Success(response.body())
            }else{
                _userDetailsResponse.value = ScreenState.Error(response.message(),response.code())
            }

        }
    }

    fun updateProfile(
        body:UpdateProfileBody,
        headers:Map<String,String>
    ){
        viewModelScope.launch {
            _updateProfileResponse.value = ScreenState.Loading(null)

            val response = repository.updateProfile(body,headers)

            if(response.isSuccessful){
                _updateProfileResponse.value = ScreenState.Success(response.body())
            }else{
                _updateProfileResponse.value = ScreenState.Error(response.message(),response.code())
            }

        }
    }

}