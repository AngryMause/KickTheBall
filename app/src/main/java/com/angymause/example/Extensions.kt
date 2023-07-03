package com.angymause.example

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.angymause.example.databinding.CustomToastBinding


fun Activity.toast(message: String) {
    val binding = CustomToastBinding.inflate(layoutInflater)
    val toast = Toast(this)
    toast.view = binding.root
    binding.tvCustomToast.text = message
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}
