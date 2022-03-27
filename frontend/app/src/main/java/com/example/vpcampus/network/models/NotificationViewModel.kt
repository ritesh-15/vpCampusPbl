package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.api.notification.DeleteNotificationResponse
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

    fun getAllNotifications(headers: Map<String, String>,sent:String? = null){

        viewModelScope.launch {
            _allNotificationsResponse.value = ScreenState.Loading(null)
            val response = repository.getAllNotifications(headers,sent)

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


    private var _deleteNotificationResponse = MutableLiveData<ScreenState<DeleteNotificationResponse>>()

    val deleteNotificationResponse:LiveData<ScreenState<DeleteNotificationResponse>>
        get() = _deleteNotificationResponse

    fun deleteNotification(headers: Map<String, String>,id:String){

        viewModelScope.launch {
            _deleteNotificationResponse.value = ScreenState.Loading(null)
            val response = repository.deleteNotification(headers,id)
            if(response.isSuccessful){
                _deleteNotificationResponse.value = ScreenState.Success(response.body())
            }else{
                _deleteNotificationResponse.value = ScreenState.Error(response.message(),response.code())
            }
        }

    }


    private var _updateNotificationResponse  = MutableLiveData<ScreenState<CreateNotificationResponse>>()

    val updateNotificationResponse:LiveData<ScreenState<CreateNotificationResponse>>
        get() = _updateNotificationResponse

    fun updateNotification(headers: Map<String, String>,id:String,body: NotificationBody){
        viewModelScope.launch {
            _updateNotificationResponse.value = ScreenState.Loading(null)
            val response = repository.updateNotification(headers,id,body)
            if(response.isSuccessful){
                _updateNotificationResponse.value = ScreenState.Success(response.body())
            }else{
                _updateNotificationResponse.value = ScreenState.Error(response.message(),response.code())
            }
        }

    }

}