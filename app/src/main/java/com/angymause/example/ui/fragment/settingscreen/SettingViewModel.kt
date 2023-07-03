package com.angymause.example.ui.fragment.settingscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angymause.example.data.local.SharedService
import com.angymause.example.model.Ball
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val sharedService: SharedService) : ViewModel() {

    private val _bal = MutableLiveData<List<Ball>>()
    val bal: LiveData<List<Ball>> get() = _bal


    init {
        initBalList()
    }

    private fun initBalList() {
        viewModelScope.launch {
            _bal.value = sharedService.getBalList()
        }
    }

    fun getSelectBal(): Int {
        return sharedService.getSelectBal()
    }


    fun saveSelectBal(bal: Int) {
        sharedService.saveSelectBall(bal)
    }

}