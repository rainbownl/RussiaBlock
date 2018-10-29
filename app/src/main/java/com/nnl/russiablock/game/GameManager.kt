package com.nnl.russiablock.game

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.SurfaceView
import com.nnl.russiablock.board.BaseBrick
import com.nnl.russiablock.board.Board
import com.nnl.russiablock.board.BrickFactory
import java.util.*
import kotlin.concurrent.timerTask

//MVP的P
class GameManager {
    var score = 0
    private var board: Board? = null
    private var view: SurfaceView? = null
    private var updatePeriod = 20
    private var brickFactory = BrickFactory()
    private var updateTimer: Timer? = null
    private var downTimer: Timer? = null
    private var brickInitX = 10
    private var brickInitY = 0
    private var downTime: Long = 500

    fun init(width: Int, height:  Int, view: SurfaceView) {
        board = Board(width, height)
        this.view = view
        autoUpdate()
        board?.brick = createRandomBrick()
        startDownTimer()
    }

    fun uninit() {
        stopDownTimer()
        stopUpdateTimer()
    }

    fun createRandomBrick() : BaseBrick?{
        var type = Random(Calendar.getInstance().timeInMillis).nextInt()%BrickFactory.MAX_BRICK
        return brickFactory.createBrick(type, brickInitX, brickInitY)
    }

    fun update() {
        var canvas = view!!.holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.LTGRAY)
            var margin = Rect(50, 100, 50, 10)
            board?.draw(canvas, margin)
            view!!.holder.unlockCanvasAndPost(canvas)
        }
    }

    fun autoUpdate() {
        if (updateTimer == null) {
            updateTimer = Timer()
            updateTimer!!.schedule(timerTask{
                update()
            }, 0, updatePeriod.toLong())
        }
    }

    fun stopUpdateTimer() {
        updateTimer?.cancel()
        updateTimer = null
    }

    fun startDownTimer() {
        if (downTimer == null) {
            downTimer = Timer()
            downTimer!!.schedule(timerTask {
                if (board?.brick != null) {
                    if (board!!.isShapeHitBottom()) {
                        board!!.merge()
                        board!!.brick = createRandomBrick()
                    } else {
                        board?.brick?.moveDown()
                        board?.wipe()
                    }
                }
            }, 0, downTime)
        }
    }

    fun stopDownTimer() {
        downTimer?.cancel()
        downTimer = null
    }

    fun moveLeft() : Boolean{
        if (board!!.canMoveLeft()) {
            board!!.brick?.moveLeft()
            return true
        }

        return false
    }

    fun moveRight() : Boolean{
        if (board!!.canMoveRight()) {
            board!!.brick?.moveRight()
            return true
        }
        return false
    }

    fun rotate() : Boolean{
        board?.brick?.rotate()
        if (!board!!.isBrickValid()) {
            board!!.brick!!.recover()
        }
        return true
    }
}