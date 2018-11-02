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
    private var controlPanel: ControlPanel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<SurfaceView>(R.id.main_surfaceview)
        gameManager = GameManager()

        gameManager?.init(15, 30, surfaceView)
        controlPanel = findViewById(R.id.control_panel)
        controlPanel?.setOnActionListener(this)

        controlPanel?.setDrawables(ControlPanel.upIndex, resources.getDrawable(R.drawable.rotate, null),
            resources.getDrawable(R.drawable.rotate_1, null))
        controlPanel?.setDrawables(ControlPanel.leftIndex, resources.getDrawable(R.drawable.left, null),
            resources.getDrawable(R.drawable.left_1, null))
        controlPanel?.setDrawables(ControlPanel.centerIndex, resources.getDrawable(R.drawable.pause, null),
            resources.getDrawable(R.drawable.pause_1))
        controlPanel?.setDrawables(ControlPanel.rightIndex, resources.getDrawable(R.drawable.right, null),
            resources.getDrawable(R.drawable.right_1, null))
        controlPanel?.setDrawables(ControlPanel.downIndex, resources.getDrawable(R.drawable.down, null),
            resources.getDrawable(R.drawable.down_1, null))
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
            ControlPanel.centerIndex -> {
                gameManager?.pause(gameManager?.isPause() ?: false ?: true)
                if (gameManager != null && gameManager!!.isPause()){
                    controlPanel?.setDrawables(ControlPanel.centerIndex, resources.getDrawable(R.drawable.continuegame, null),
                        resources.getDrawable(R.drawable.continue_1, null))

                } else {
                    controlPanel?.setDrawables(ControlPanel.centerIndex, resources.getDrawable(R.drawable.pause, null),
                        resources.getDrawable(R.drawable.pause_1, null))
                }
            }
            ControlPanel.upIndex -> gameManager?.rotate()
        }
    }

    override fun onLongClick(index: Int) {

    }
}