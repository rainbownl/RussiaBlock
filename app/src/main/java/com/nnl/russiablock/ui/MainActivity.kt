package com.nnl.russiablock.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
}