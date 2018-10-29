package com.nnl.russiablock.game

import android.graphics.Color
import android.graphics.Rect
import android.view.SurfaceView
import com.nnl.russiablock.board.BaseBrick
import com.nnl.russiablock.board.Board
import com.nnl.russiablock.board.BrickFactory
import com.nnl.russiablock.debug.RbLog
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
    private var isPause = false

    enum class ProcessResult{
        Normal, Bottom, Wipe, GameOver
    }

    fun init(width: Int, height:  Int, view: SurfaceView) {
        board = Board(width, height)
        this.view = view
        autoUpdate()
        board?.setBrick(createRandomBrick())
        startDownTimer()
    }

    fun uninit() {
        stopDownTimer()
        stopUpdateTimer()
    }

    fun createRandomBrick() : BaseBrick?{
        var type = Math.abs(Random(Calendar.getInstance().timeInMillis).nextInt())%BrickFactory.MAX_BRICK
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

    /**
     */
    fun gameProcess() : ProcessResult {
        var result = ProcessResult.Normal
        RbLog.d("GameManager", "game process thread id: " + Thread.currentThread().id)
            if (board!!.getBrick() != null && !board!!.isBrickValid()) {
                gameOver()
                result = ProcessResult.GameOver
            } else if (board!!.isShapeHitBottom()) {
                board!!.merge()
                board!!.setBrick(createRandomBrick())
                result = ProcessResult.Bottom
            } else {
                board?.getBrick()?.moveDown()
                if (board?.wipe() == true) {
                    score++
                    result = ProcessResult.Wipe
                }
            }
        return result
    }

    fun startDownTimer() {
        RbLog.d("GameManager", "start down timer thread id: " + Thread.currentThread().id)
        if (downTimer == null) {
            downTimer = Timer()
            downTimer!!.schedule(timerTask {
                if (board?.getBrick() != null) {
                    if (!isPause) {
                        gameProcess()
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
            board!!.getBrick()?.moveLeft()
            return true
        }

        return false
    }

    fun moveRight() : Boolean{
        if (board!!.canMoveRight()) {
            board!!.getBrick()?.moveRight()
            return true
        }
        return false
    }

    fun rotate() : Boolean{
        board?.getBrick()?.rotate()
        if (!board!!.isBrickValid()) {
            board!!.getBrick()!!.recover()
        }
        return true
    }

    fun fallDown() {
        do {
            var result = gameProcess()
        } while (result == ProcessResult.Normal)
    }

    fun pause(pause: Boolean) {
        isPause = pause
    }

    fun gameOver() {
        stopDownTimer()
    }

    fun reset() {
        board!!.reset()
    }
}