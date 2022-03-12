package com.example.vpcampus.network.models

import androidx.lifecycle.*
import com.example.vpcampus.api.authApi.AuthBodyState
import com.example.vpcampus.api.authApi.AuthResponse
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val repository: AuthRepository
):ViewModel() {

    private val _loginResponse = MutableLiveData<ScreenState<AuthResponse.LoginResponse>>()

    private val _refreshResponse = MutableLiveData<ScreenState<AuthResponse.RefreshResponse>>()

    val loginResponse:LiveData<ScreenState<AuthResponse.LoginResponse>>
        get() = _loginResponse

    val refreshResponse:LiveData<ScreenState<AuthResponse.RefreshResponse>>
        get() = _refreshResponse

    fun login(email:String,password:String){
        val body = AuthBodyState.LoginRequestBody(email,password)

        viewModelScope.launch {
            val response = repository.login(body)
            _loginResponse.value = ScreenState.Loading(null)

            try {
                if(response.isSuccessful){
                    _loginResponse.value = ScreenState.Success(response.body())
                }else{
                    _loginResponse.value = ScreenState.Error(response.code().toString())
                }

            }catch (e:Exception){
                _loginResponse.value = ScreenState.Error(e.message!!)
            }

        }

    }

    fun refresh(headers:Map<String,String>){
        _refreshResponse.value = ScreenState.Loading(null)

        viewModelScope.launch {
            try {
                val response = repository.refresh(headers)

                if(response.isSuccessful){
                    _refreshResponse.value = ScreenState.Success(response.body())
                }else{
                    _refreshResponse.value = ScreenState.Error(response.code().toString())
                }

            }catch (e:Exception){
                _refreshResponse.value = ScreenState.Error(e.message!!)
            }
        }
    }
}