package com.angymause.example.data.local

import android.content.SharedPreferences
import com.angymause.example.R
import com.angymause.example.model.Ball
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedService @Inject constructor(private val sharedPreferences: SharedPreferences) {
    private val score = "score"
    private val balList = "bal_list"
    private val selectBal = "select_bal"
    private val defaultBal = R.mipmap.il_ball

    private val sharedEditor = sharedPreferences.edit()
    private val gson = Gson()

    private val flowScore = MutableStateFlow<Long>(0)

    init {
        flowScore.value = sharedPreferences.getLong(score, 0L)
    }

    fun saveBallList(ballList: List<Ball>) {
        val listOfBal = gson.toJson(ballList)
        sharedEditor.putString(balList, listOfBal).apply()
    }

    fun getBalList(): List<Ball> {
        val gson = Gson()
        val json = sharedPreferences.getString(balList, "")
        if (json!!.isEmpty()) return emptyList()
        val itemType = object : TypeToken<List<Ball>>() {}.type
        return gson.fromJson(json, itemType)
    }

    suspend fun writeScore(value: Long) {
        sharedEditor.putLong(score, value).apply()
        flowScore.emit(value)
    }

    fun readSore(): Flow<Long> {
        return flowScore
    }

    fun saveSelectBall(selectedBal: Int) {
        sharedEditor.putInt(selectBal, selectedBal).apply()
    }

    fun getSelectBal(): Int {
        return sharedPreferences.getInt(selectBal, defaultBal)
    }
}
