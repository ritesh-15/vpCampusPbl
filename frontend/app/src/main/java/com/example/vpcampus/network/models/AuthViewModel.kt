package com.example.vpcampus.network.models


import androidx.lifecycle.*
import com.example.vpcampus.api.authApi.LoginRequestBody
import com.example.vpcampus.api.authApi.LoginResponse
import com.example.vpcampus.api.authApi.RefreshResponse
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val repository: AuthRepository
):ViewModel() {

   private var  _loginResponse = MutableLiveData<ScreenState<LoginResponse>>()

    val loginResponse:LiveData<ScreenState<LoginResponse>>
    get()  = _loginResponse

    private var _refreshResponse = MutableLiveData<ScreenState<RefreshResponse>>()

    val refreshResponse:LiveData<ScreenState<RefreshResponse>>
        get()  = _refreshResponse


    fun login(email:String,password:String){
        val body = LoginRequestBody(email, password)

        viewModelScope.launch {
            _loginResponse.value = ScreenState.Loading(null)
            val response = repository.login(body)
            try {
                if(response.isSuccessful){
                    _loginResponse.value = ScreenState.Success(response.body())
                }else{
                    _loginResponse.value = ScreenState.Error(response.code().toString())
                }
            }catch (e:Exception){
                _loginResponse.value = ScreenState.Error(response.code().toString())
            }
        }
    }

    fun refresh(headers:Map<String,String>){

        viewModelScope.launch {
            _refreshResponse.value = ScreenState.Loading(null)
            val response = repository.refresh(headers)

            try {
                if(response.isSuccessful){
                    _refreshResponse.value = ScreenState.Success(response.body())
                }else{
                    _refreshResponse.value = ScreenState.Error(response.code().toString())
                }
            }catch (e:Exception){
                _refreshResponse.value = ScreenState.Error(response.code().toString())
            }
        }

    }
}