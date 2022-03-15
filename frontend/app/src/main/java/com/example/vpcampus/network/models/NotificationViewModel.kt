package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.api.notification.NotificationBody
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch
import java.lang.Exception

class NotificationViewModel(
    private val repository : NotificationRepository
):ViewModel() {

    private var _createNotificationResponse = MutableLiveData<ScreenState<CreateNotificationResponse>>()

    val createNotificationResponse:LiveData<ScreenState<CreateNotificationResponse>>
        get() = _createNotificationResponse

    private var _allNotificationsResponse = MutableLiveData<ScreenState<AllNotificationResponse>>()

    val allNotificationsResponse:LiveData<ScreenState<AllNotificationResponse>>
        get() = _allNotificationsResponse

    fun createNewNotification(
        title:String,
        description:String,
        headers:Map<String,String>
    ){

        viewModelScope.launch {
            _createNotificationResponse.value = ScreenState.Loading(null)
            val response = repository.createNewNotification(NotificationBody(title, description),headers)

            try {

                if(response.isSuccessful){
                    _createNotificationResponse.value = ScreenState.Success(response.body())
                }else{
                    _createNotificationResponse.value = ScreenState.Error(response.message(),response.code())
                }

            }catch (e:Exception){
                _createNotificationResponse.value = ScreenState.Error(e.message!!,response.code())
            }

        }

    }

    fun getAllNotifications(headers: Map<String, String>){

        viewModelScope.launch {
            _allNotificationsResponse.value = ScreenState.Loading(null)
            val response = repository.getAllNotifications(headers)

            try {

                if(response.isSuccessful){
                    _allNotificationsResponse.value = ScreenState.Success(response.body())
                }else{
                    _allNotificationsResponse.value = ScreenState.Error(response.message(),response.code())
                }

            }catch (e:Exception){
                _allNotificationsResponse.value = ScreenState.Error(e.message!!,response.code())
            }
        }

    }

}