package com.angymause.example.ui.fragment.gamescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angymause.example.data.local.SharedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val sharedService: SharedService) : ViewModel() {

    private val _score = MutableLiveData<Long>(0)
    val score: LiveData<Long> get() = _score

    init {
        viewModelScope.launch {
            sharedService.readSore().collect{
                _score.postValue(it)
            }
        }
    }



    fun loadSelectBal(): Int {
        return sharedService.getSelectBal()
    }


    fun writeScore(value: Long) {
        viewModelScope.launch {
            sharedService.writeScore(value)
        }
    }


}