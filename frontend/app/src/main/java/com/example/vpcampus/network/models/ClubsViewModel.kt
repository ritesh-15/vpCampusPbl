package com.example.vpcampus.network.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpcampus.api.clubs.*
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.ScreenState
import kotlinx.coroutines.launch

class ClubsViewModel(
    private val repository: ClubRepository,
) : ViewModel() {

    // get all clubs response
    private var _allClubs: MutableLiveData<ScreenState<GetAllClubsResponse>> = MutableLiveData()
    val allClubs: LiveData<ScreenState<GetAllClubsResponse>>
        get() = _allClubs

    fun getAllClubs() {
        viewModelScope.launch {
            _allClubs.value = ScreenState.Loading(null)
            val response = repository.getAllClubs()
            if (response.isSuccessful) {
                _allClubs.value = ScreenState.Success(response.body())
            } else {
                _allClubs.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }


    // create club response
    private var _createClub: MutableLiveData<ScreenState<CreateClubResponse>> = MutableLiveData()
    val createClub: LiveData<ScreenState<CreateClubResponse>>
        get() = _createClub

    fun createClub(
        body: CreateClubBody,
    ) {
        viewModelScope.launch {
            _createClub.value = ScreenState.Loading(null)
            val response = repository.createNewClub(body)
            if (response.isSuccessful) {
                _createClub.value = ScreenState.Success(response.body())
            } else {
                _createClub.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }


    // create chat response
    private var _createChat: MutableLiveData<ScreenState<NewChatResponse>> = MutableLiveData()
    val createChat: LiveData<ScreenState<NewChatResponse>>
        get() = _createChat

    fun createChat(
        body: CreateChatBody,
        clubId: String,
    ) {
        viewModelScope.launch {
            _createChat.value = ScreenState.Loading(null)
            val response = repository.createChat(body, clubId)
            if (response.isSuccessful) {
                _createChat.value = ScreenState.Success(response.body())
            } else {
                _createChat.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }

    // get all chats
    private var _allChats: MutableLiveData<ScreenState<AllChatsResponse>> = MutableLiveData()
    val allChats: LiveData<ScreenState<AllChatsResponse>>
        get() = _allChats

    fun allChats(
        clubId: String,
    ) {
        viewModelScope.launch {
            _allChats.value = ScreenState.Loading(null)
            val response = repository.getAllChats(clubId)
            if (response.isSuccessful) {
                _allChats.value = ScreenState.Success(response.body())
            } else {
                _allChats.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }
    }

    // delete club
    private var _deleteClub: MutableLiveData<ScreenState<DeleteClubResponse>> = MutableLiveData()
    val deleteClub: LiveData<ScreenState<DeleteClubResponse>>
        get() = _deleteClub

    fun deleteClub(clubId: String) {

        viewModelScope.launch {
            _deleteClub.value = ScreenState.Loading(null)
            val response = repository.deleteClub(clubId)
            if (response.isSuccessful) {
                _deleteClub.value = ScreenState.Success(response.body())
            } else {
                _deleteClub.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }

    }

    // update club information
    private var _updateClub: MutableLiveData<ScreenState<CreateClubResponse>> = MutableLiveData()
    val updateClub: LiveData<ScreenState<CreateClubResponse>>
        get() = _updateClub

    fun updateClub(clubId: String, body: UpdateClubBody) {

        viewModelScope.launch {
            _updateClub.value = ScreenState.Loading(null)
            val response = repository.updateClub(clubId, body)
            if (response.isSuccessful) {
                _updateClub.value = ScreenState.Success(response.body())
            } else {
                _updateClub.value = ScreenState.Error(errorBody = response.errorBody())
            }
        }

    }

}