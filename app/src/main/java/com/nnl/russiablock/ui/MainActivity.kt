package com.nnl.russiablock.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.SurfaceView
import com.nnl.russiablock.R
import com.nnl.russiablock.game.GameManager
import com.nnl.russiablock.ui.custom_view.ControlPanel

class MainActivity : AppCompatActivity() , ControlPanel.OnActionListener{
    var gameManager: GameManager?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<SurfaceView>(R.id.main_surfaceview)
        gameManager = GameManager()

        gameManager?.init(15, 30, surfaceView)
        var controlPanel = findViewById<ControlPanel>(R.id.control_panel)
        controlPanel.setOnActionListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameManager?.unInit()
    }

    override fun onBackPressed() {
        gameManager?.pause(true)
        AlertDialog.Builder(this).setMessage("是否要退出程序？")
            .setPositiveButton("退出"){ _, _ ->
            finish()
        }.setNegativeButton("取消") { _, _ ->
                gameManager?.pause(false)
            }.show()
    }

    override fun onClick(index: Int) {
        when (index) {
            ControlPanel.leftIndex -> gameManager?.moveLeft()
            ControlPanel.rightIndex -> gameManager?.moveRight()
            ControlPanel.downIndex -> gameManager?.fallDown()
            ControlPanel.centerIndex -> gameManager?.pause(gameManager?.isPause() ?: false ?: true)
            ControlPanel.upIndex -> gameManager?.rotate()
        }
    }

    override fun onLongClick(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}