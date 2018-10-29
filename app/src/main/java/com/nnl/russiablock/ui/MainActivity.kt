package com.nnl.russiablock.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.SurfaceView
import com.nnl.russiablock.R
import com.nnl.russiablock.game.GameManager

class MainActivity : AppCompatActivity() {
    var gameManager: GameManager?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var surfaceView = findViewById<SurfaceView>(R.id.main_surfaceview)
        gameManager = GameManager()

        gameManager?.init(15, 30, surfaceView)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameManager?.uninit()
    }

    override fun onBackPressed() {
        gameManager?.pause(true)
        AlertDialog.Builder(this).setMessage("是否要退出程序？")
            .setPositiveButton("退出"){ dialog, whitch ->
            finish()
        }.setNegativeButton("取消") { dialog, which ->
                gameManager?.pause(false)
            }.show()
    }
}