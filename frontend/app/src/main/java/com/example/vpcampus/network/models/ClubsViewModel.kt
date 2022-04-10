package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.clubs.CreateClubBody
import com.example.vpcampus.api.clubs.CreateClubResponse
import com.example.vpcampus.api.clubs.GetAllClubsResponse
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch

class ClubsViewModel(
    private val repository: ClubRepository,
) : ViewModel() {

    // get all clubs response
    private var _allClubs: MutableLiveData<ScreenState<GetAllClubsResponse>> = MutableLiveData()
    val allClubs:LiveData<ScreenState<GetAllClubsResponse>>
        get() = _allClubs

    fun getAllClubs(){
        viewModelScope.launch {
            _allClubs.value = ScreenState.Loading(null)
            val response = repository.getAllClubs()
            if(response.isSuccessful){
                _allClubs.value = ScreenState.Success(response.body())
            }else{
                _allClubs.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }


    // create club response
    private var _createClub: MutableLiveData<ScreenState<CreateClubResponse>> = MutableLiveData()
    val createClub:LiveData<ScreenState<CreateClubResponse>>
        get() = _createClub

    fun createClub(
        body: CreateClubBody
    ){
        viewModelScope.launch {
            _createClub.value = ScreenState.Loading(null)
            val response = repository.createNewClub(body)
            if(response.isSuccessful){
                _createClub.value = ScreenState.Success(response.body())
            }else{
                _createClub.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }

}