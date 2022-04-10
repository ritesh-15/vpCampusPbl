package com.example.vpcampus.network.models


import android.util.Log
import androidx.lifecycle.*
import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.authApi.*
import com.example.vpcampus.models.ErrorMessage
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.utils.ScreenState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class AuthViewModel(
    private val repository: AuthRepository
):ViewModel() {

    // login
   private var  _loginResponse = MutableLiveData<ScreenState<LoginResponse>>()

    val loginResponse:LiveData<ScreenState<LoginResponse>>
    get()  = _loginResponse

    // refresh
    private var _refreshResponse = MutableLiveData<ScreenState<RefreshResponse>>()

    val refreshResponse:LiveData<ScreenState<RefreshResponse>>
        get()  = _refreshResponse


    // send otp
    private val _sendOtpResponse = MutableLiveData<ScreenState<SendOtpResponse>>()

    val sendOtpResponse:LiveData<ScreenState<SendOtpResponse>>
        get() = _sendOtpResponse

    // verify otp
    private val _verifyOtpResponse = MutableLiveData<ScreenState<VerifyOtpResponse>>()

    val verifyOtpResponse:LiveData<ScreenState<VerifyOtpResponse>>
        get() = _verifyOtpResponse

    // register
    private val _registerResponse = MutableLiveData<ScreenState<RegisterResponse>>()

    val registerResponse:LiveData<ScreenState<RegisterResponse>>
        get() = _registerResponse

    // activate
    private val _activateResponse = MutableLiveData<ScreenState<ActivateResponse>>()

    val activateResponse:LiveData<ScreenState<ActivateResponse>>
        get() = _activateResponse

    // logout
    private val _logoutResponse = MutableLiveData<ScreenState<LogOutResponse>>()

    val logoutResponse:LiveData<ScreenState<LogOutResponse>>
        get() = _logoutResponse

    fun login(email:String,password:String){
        val body = LoginRequestBody(email, password)

        viewModelScope.launch {
            _loginResponse.value = ScreenState.Loading(null)
            val response = repository.login(body)
            try {
                if(response.isSuccessful){
                    _loginResponse.value = ScreenState.Success(response.body())
                }else{
                    _loginResponse.value = ScreenState.Error(response.code().toString(),response.code())
                }
            }catch (e:Exception){
                Log.d("ERROR_RESPONSE",e.message!!)
                _loginResponse.value = ScreenState.Error(response.code().toString(),response.code())
            }
        }
    }

    fun refresh(){

        viewModelScope.launch {
            _refreshResponse.value = ScreenState.Loading(null)
            val response = repository.refresh()

            try {
                if(response.isSuccessful){
                    _refreshResponse.value = ScreenState.Success(response.body())
                }else{
                    _refreshResponse.value = ScreenState.Error(response.code().toString(),response.code())
                }
            }catch (e:Exception){
                _refreshResponse.value = ScreenState.Error(response.code().toString(),response.code())
            }
        }

    }

    fun sendOtp(email:String,headers: Map<String, String>){
        val body = SendOtpBody(email)
        viewModelScope.launch {
            _sendOtpResponse.value = ScreenState.Loading(null)
            val response = repository.sendOtp(body,headers)
            try {
                if(response.isSuccessful){
                    _sendOtpResponse.value = ScreenState.Success(response.body())
                }else{
                    _sendOtpResponse.value = ScreenState.Error(response.message(),response.code())
                }
            }catch (e:Exception){
                _sendOtpResponse.value = ScreenState.Error(e.message!!,response.code())
            }

        }
    }

    fun verifyOtp(body: VerifyOtpBody,headers: Map<String, String>){
        viewModelScope.launch {
            _verifyOtpResponse.value = ScreenState.Loading(null)
            val response = repository.verifyOtp(body,headers)
            try {
                if(response.isSuccessful){
                    _verifyOtpResponse.value = ScreenState.Success(response.body())
                }else{
                    _verifyOtpResponse.value = ScreenState.Error(response.message(),response.code())
                }
            }catch (e:Exception){
                _verifyOtpResponse.value = ScreenState.Error(e.message!!,response.code())
            }

        }
    }

    fun register(body: RegisterBody){
        viewModelScope.launch {
            _registerResponse.value = ScreenState.Loading(null)
            val response = repository.register(body)
            try {
                if(response.isSuccessful){
                    _registerResponse.value = ScreenState.Success(response.body())
                }else{
                    _registerResponse.value = ScreenState.Error(response.message(),response.code())
                }
            }catch (e:Exception){
                _registerResponse.value = ScreenState.Error(e.message!!,response.code())
            }

        }
    }


    fun activate(body: ActivateBody,headers: Map<String, String>){
        viewModelScope.launch {
            _activateResponse.value = ScreenState.Loading(null)
            val response = repository.activate(body,headers)
            try {
                if(response.isSuccessful){
                    _activateResponse.value = ScreenState.Success(response.body())
                }else{
                    _activateResponse.value = ScreenState.Error(response.message(),response.code())
                }
            }catch (e:Exception){
                _activateResponse.value = ScreenState.Error(e.message!!,response.code())
            }

        }
    }

    fun logout(headers: Map<String, String>){
        viewModelScope.launch {
            _logoutResponse.value = ScreenState.Loading(null)
            val response = repository.logout(headers)

            if(response.isSuccessful){
                    _logoutResponse.value = ScreenState.Success(response.body())
            }else{
                _logoutResponse.value = ScreenState.Error(response.message(),response.code())
            }

        }
    }
}