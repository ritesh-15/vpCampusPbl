package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.uploads.UploadResponse
import com.example.vpcampus.repository.UploadRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.lang.Exception

class UploadViewModel(
    private val repository: UploadRepository
):ViewModel() {

    private var _uploadSingleFileResponse = MutableLiveData<ScreenState<UploadResponse>>()

    val uploadSingleFileResponse:LiveData<ScreenState<UploadResponse>>
        get() = _uploadSingleFileResponse


    fun uploadSingleFile(
        file : MultipartBody.Part,
        headers:Map<String,String>
    ){
        viewModelScope.launch {
            _uploadSingleFileResponse.value = ScreenState.Loading(null)
            val response = repository.uploadSingleFile(file,headers)

            try {
                if(response.isSuccessful){
                    _uploadSingleFileResponse.value = ScreenState.Success(response.body())
                }else{
                    _uploadSingleFileResponse.value = ScreenState.Error(response.message(),response.code())
                }
            }catch (e:Exception){
                _uploadSingleFileResponse.value = ScreenState.Error(e.message!!,response.code())
            }
        }
    }

}