package com.angymause.example.ui.fragment.menuscreen

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Process
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import com.angymause.example.data.local.SharedService
import com.angymause.example.databinding.FragmentMenuBinding
import com.angymause.example.ui.fragment.BaseFragment
import com.angymause.example.ui.fragment.gamescreen.GameFragment
import com.angymause.example.ui.fragment.settingscreen.SettingFragment
import com.angymause.example.ui.fragment.shopscreen.ShopFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    @Inject
    lateinit var sharedService: SharedService
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateMenuImage()
        navigate()
        addOnBackPresCallback()
    }


    private fun animateMenuImage() {
        val animation = ObjectAnimator.ofFloat(binding.icon, View.ROTATION_Y, 0f, 360f)
        animation.duration = 20000
        animation.repeatCount = 5000
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    private fun addOnBackPresCallback() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                android.os.Process.killProcess(Process.myPid())
            }
        })
    }

    private fun navigate() {
        binding.tvStatGame.setOnClickListener {
            replace(GameFragment())
        }
        binding.tvShop.setOnClickListener {
            replace(ShopFragment())
        }
        binding.tvSetting.setOnClickListener {
            replace(SettingFragment())
        }
        binding.tvExit.setOnClickListener {
            activity?.finishAndRemoveTask()
        }
    }


}
