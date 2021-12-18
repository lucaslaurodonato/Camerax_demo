package com.example.camerax.di

import androidx.appcompat.app.AppCompatActivity
import com.example.camerax.mechanism.permission.AppPermissionUtils
import com.example.camerax.mechanism.permission.PermissionListener
import org.koin.dsl.module

val mechanismModules = module {
    factory { (activity: AppCompatActivity, listener: PermissionListener) ->
        AppPermissionUtils(activity, listener)
    }
}

val applicationModules = mechanismModules
