package com.example.githubrepos.ui.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope

class AppDialogs(private val mActivity: AppCompatActivity,
                 private val mLifecycleScope: LifecycleCoroutineScope = mActivity.lifecycleScope) {

    companion object {
        private val TAG by lazy { "AppDialogs" }
    }

}