package com.angymause.example.ui.fragment.shopscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angymause.example.data.local.SharedService
import com.angymause.example.model.Ball
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(private val sharedService: SharedService) : ViewModel() {

    private val _balList = MutableLiveData<List<Ball>>()
    val balList: LiveData<List<Ball>> get() = _balList

    init {
        getBalList()
    }

    private fun getBalList() {
        _balList.value = sharedService.getBalList()
    }

    fun saveScoreLeft(score: Long) {
        viewModelScope.launch {
            sharedService.writeScore(score)
        }
    }

    fun getScore(): Flow<Long> {
        return sharedService.readSore()
    }

    fun saveBal(ball: List<Ball>) {
        sharedService.saveBallList(ball)
    }

}