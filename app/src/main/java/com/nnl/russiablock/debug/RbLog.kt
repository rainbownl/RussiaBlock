package com.nnl.russiablock.debug

import android.util.Log
import com.nnl.russiablock.BuildConfig

class RbLog {
    companion object {
        private var isOpen = BuildConfig.DEBUG
        fun d(tag: String, msg: String){
            if (isOpen) {
                Log.d(tag, msg)
            }
        }
    }
}