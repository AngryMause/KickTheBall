package com.angymause.example.ui.fragment.shopscreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.angymause.example.R
import com.angymause.example.databinding.FragmentShopBinding
import com.angymause.example.model.Ball
import com.angymause.example.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopFragment : BaseFragment<FragmentShopBinding>(FragmentShopBinding::inflate) {
    private var balList = mutableListOf<Ball>()
    private val foreground = 0.toDrawable()
    private val balGradientPrice = 2500
    private val balGreenPrice = 1000
    private val balPurplePrice = 500
    private var score = 0L
    private val notEnoughPoint = "Not enough point"
    private val youHaveThis = "You have this ball"
    private val shopViewModel: ShopViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bal = shopViewModel.balList.value
        balList.addAll(bal!!)
        initializeData()
        buyBal()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeData() {
        lifecycleScope.launch {
            shopViewModel.getScore().collect {
                binding.tvPoint.text = "Point: $it"
            }
        }
    }

    private fun buyBal() {
        for (bal in balList) {
            checkIsBuy(bal)
        }
        buyGradientBal()
        buyPurpleBal()
        buyGreenBal()
    }

    private fun buyPurpleBal() {
        var count = 0
        binding.ivPurpleBall.setOnClickListener {
            count++
            if (binding.ivPurpleBall.foreground != foreground) {
                if (checkIsCanBuy(balPurplePrice.toLong())) {
                    showBuyAlert(R.mipmap.il_ball_purple,
                        balPurplePrice.toLong(),
                        binding.ivPurpleBall)
                } else count = alertMassage(count, notEnoughPoint)
            } else count = alertMassage(count, youHaveThis)
        }
    }

    private fun buyGreenBal() {
        var count = 0
        binding.ivGreenBall.setOnClickListener {
            count++
            if (binding.ivGreenBall.foreground != foreground) {
                if (checkIsCanBuy(balGreenPrice.toLong())) {
                    showBuyAlert(R.mipmap.il_ball_green,
                        balPurplePrice.toLong(),
                        binding.ivGreenBall)
                } else count = alertMassage(count, notEnoughPoint)
            } else count = alertMassage(count, youHaveThis)
        }
    }

    private fun buyGradientBal() {
        var count = 0
        binding.ivGradient.setOnClickListener {
            count++
            if (binding.ivGradient.foreground != foreground) {
                if (checkIsCanBuy(balGradientPrice.toLong())) {
                    showBuyAlert(R.mipmap.il_ball_gradient,
                        balPurplePrice.toLong(),
                        binding.ivGradient)
                } else count = alertMassage(count, notEnoughPoint)
            } else count = alertMassage(count, youHaveThis)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showBuyAlert(bal: Int, price: Long, view: View) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage("You won Buy? ")
            .setPositiveButton("Yes") { _, _ ->
                savePointLeft(price)
                setForeground(view)
                saveBalBuy(bal)
                alertDialog.create().dismiss()
            }
            .setNegativeButton("No") { _, _ ->
                alertDialog.create().dismiss()
            }
        alertDialog.show()
    }

    private fun savePointLeft(price: Long) {
        val sum = score - price
        shopViewModel.saveScoreLeft(sum)
    }

    private fun saveBalBuy(bal: Int) {
        shopViewModel.saveBal(addNewBall(bal))
    }

    private fun setForeground(view: View) {
        view.foreground = foreground
    }

    private fun checkIsBuy(bal: Ball) {
        when (bal.name) {
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

    private fun addNewBall(bal: Int, isBuy: Boolean = true): List<Ball> {
        balList.add(Ball(bal, isBuy))
        return balList
    }

    private fun checkIsCanBuy(price: Long): Boolean {
        var isCan = false
        lifecycleScope.launch {
            shopViewModel.getScore().collectLatest { score ->
                if (price <= score) {
                    isCan = true
                }
            }
        }
        return isCan
    }


}




