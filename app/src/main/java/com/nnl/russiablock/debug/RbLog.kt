package com.nnl.russiablock.debug

import android.util.Log

class RbLog {
    companion object {
        var isOpen = true
        fun d(tag: String, msg: String){
            if (isOpen) {
                Log.d(tag, msg)
            }
        }
    }
}