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
    private var score = 0
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
        board?.setNextBrick(createRandomBrick()!!)
        startDownTimer()
    }

    fun unInit() {
        stopDownTimer()
        stopUpdateTimer()
    }

    private fun createRandomBrick() : BaseBrick?{
        val type = Math.abs(Random(Calendar.getInstance().timeInMillis).nextInt())%BrickFactory.MAX_BRICK
        return brickFactory.createBrick(type, brickInitX, brickInitY)
    }

    private fun update() {
        val canvas = view!!.holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.LTGRAY)
            val margin = Rect(50, 100, 50, 10)
            board?.draw(canvas, margin)
            view!!.holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun autoUpdate() {
        if (updateTimer == null) {
            updateTimer = Timer()
            updateTimer!!.schedule(timerTask{
                update()
            }, 0, updatePeriod.toLong())
        }
    }

    private fun stopUpdateTimer() {
        updateTimer?.cancel()
        updateTimer = null
    }

    /**
     */
    private fun gameProcess() : ProcessResult {
        var result = ProcessResult.Normal
            if (board!!.getBrick() != null && !board!!.isBrickValid()) {
                gameOver()
                result = ProcessResult.GameOver
            } else if (board!!.isShapeHitBottom()) {
                board!!.merge()
                board!!.setBrick(board!!.getNextBrick())
                board!!.setNextBrick(createRandomBrick()!!)
                result = ProcessResult.Bottom
            } else {
                board?.getBrick()?.moveDown()
                if (board?.wipe() == true) {
                    score++
                    board?.score = score
                    result = ProcessResult.Wipe
                }
            }
        return result
    }

    private fun startDownTimer() {
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

    private fun stopDownTimer() {
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
        while (gameProcess() == ProcessResult.Normal){}
    }

    fun pause(pause: Boolean) {
        isPause = pause
    }

    fun isPause() : Boolean { return isPause }
    private fun gameOver() {
        stopDownTimer()
    }

    fun reset() {
        board!!.reset()
    }
}