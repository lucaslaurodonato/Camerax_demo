package com.example.camerax.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(private val layout: Int) : AppCompatActivity() {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layout)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}