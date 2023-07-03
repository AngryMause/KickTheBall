package com.angymause.example.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.angymause.example.R
import com.angymause.example.toast

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container!!, false)
        return _binding!!.root
    }

    protected fun replace(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    protected fun alertMassage(int: Int, massage: String): Int {
        var count = int
        if (count == 1) {
            requireActivity().toast(massage)
        } else if (count == 10) {
            count = 0
        }
        return count
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}