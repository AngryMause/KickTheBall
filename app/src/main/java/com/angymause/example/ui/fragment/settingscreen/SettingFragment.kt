package com.angymause.example.ui.fragment.settingscreen

import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.angymause.example.R
import com.angymause.example.databinding.FragmentSettingBinding
import com.angymause.example.model.Ball
import com.angymause.example.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val balSelected = mutableListOf<Ball>()
    private var bal = R.mipmap.il_ball
    private val massage = "You did no have this bal yet"
    private val foreground = 0.toDrawable()
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bal = settingViewModel.bal.value
        balSelected.addAll(bal!!)
        setStrokeToSelectBal()
        val selectBal = settingViewModel.getSelectBal()
        checkingWhichBallIsSelected(selectBal)
        checkIsBuy()
    }

    private fun checkingWhichBallIsSelected(bal: Int) {
        when (bal) {
            R.mipmap.il_ball -> {
                binding.ivBall.setBackgroundResource(R.drawable.select_item)
                binding.ivGradient.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(0)
            }
            R.mipmap.il_ball_purple -> {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(R.drawable.select_item)
                binding.ivGradient.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(0)
            }
            R.mipmap.il_ball_green -> {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(R.drawable.select_item)
                binding.ivGradient.setBackgroundResource(0)
            }
            R.mipmap.il_ball_gradient -> {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(0)
                binding.ivGradient.setBackgroundResource(R.drawable.select_item)
            }
        }
    }

    private fun setStrokeToSelectBal() {
        selectSimpleBal()
        selectPurpleBal()
        selectGreenBal()
        selectGradientBal()
    }

    private fun selectSimpleBal() {
        binding.ivBall.setOnClickListener {
            binding.ivBall.setBackgroundResource(R.drawable.select_item)
            binding.ivPurpleBall.setBackgroundResource(0)
            binding.ivGreenBall.setBackgroundResource(0)
            binding.ivGradient.setBackgroundResource(0)
            saveSelectBal(bal = bal)
        }
    }

    private fun selectPurpleBal() {
        var count = 0
        binding.ivPurpleBall.setOnClickListener {
            count++
            if (binding.ivPurpleBall.foreground == foreground) {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(R.drawable.select_item)
                binding.ivGreenBall.setBackgroundResource(0)
                binding.ivGradient.setBackgroundResource(0)
                saveSelectBal(R.mipmap.il_ball_purple)
            } else count = alertMassage(count, massage)
        }
    }


    private fun selectGreenBal() {
        var count = 0
        binding.ivGreenBall.setOnClickListener {
            count++
            if (binding.ivGreenBall.foreground == foreground) {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(R.drawable.select_item)
                binding.ivGradient.setBackgroundResource(0)
                saveSelectBal(R.mipmap.il_ball_green)
            } else count = alertMassage(count, massage)
        }
    }

    private fun selectGradientBal() {
        var count = 0
        binding.ivGradient.setOnClickListener {
            count++
            if (binding.ivGradient.foreground == foreground) {
                binding.ivBall.setBackgroundResource(0)
                binding.ivPurpleBall.setBackgroundResource(0)
                binding.ivGreenBall.setBackgroundResource(0)
                binding.ivGradient.setBackgroundResource(R.drawable.select_item)
                saveSelectBal(R.mipmap.il_ball_gradient)
            } else count = alertMassage(count, massage)
        }
    }


    private fun saveSelectBal(bal: Int) {
        lifecycleScope.launch {
            settingViewModel.saveSelectBal(bal)
        }
    }

    private fun checkIsBuy() {
        for (bal in balSelected) {
            if (bal.isBuy) {
                check(bal.name)
            } else return
        }
    }


    private fun check(bal: Int) {
        when (bal) {
            R.mipmap.il_ball_purple -> {
                binding.ivPurpleBall.foreground = foreground
            }
            R.mipmap.il_ball_green -> {
                binding.ivGreenBall.foreground = foreground
            }
            R.mipmap.il_ball_gradient -> {
                binding.ivGradient.foreground = foreground
            }
        }
    }


}