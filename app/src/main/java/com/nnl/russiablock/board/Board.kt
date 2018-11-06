package com.nnl.russiablock.board

import android.graphics.*
import kotlin.math.min

class Board(private var width: Int, private var height: Int) {
    private var blocks: Array<Array<Boolean>>? = null
    private var border = 4
    private var innerWidth = 2
    private var brick: BaseBrick? = null
    private var nextBrick: BaseBrick? = null
    var score = 0

    init {
        blocks = Array(height){_ ->
            Array(width){_ ->
                false
            }
        }
    }

    fun getBrick() : BaseBrick? {
        synchronized(this) {
            return brick
        }
    }

    fun setBrick(brick: BaseBrick?) {
        synchronized(this) {
            this.brick = brick
        }
    }

    fun getNextBrick() : BaseBrick? {
        return nextBrick
    }

    fun setNextBrick(nextBrick: BaseBrick) {
        this.nextBrick = nextBrick
    }

    fun merge() {
        getBrick()?.shape?.forEach {
            if (it.x < width && it.y < height) {
                blocks?.get(it.y)?.set(it.x, true)
            }
        }
    }

    /**
     * 指定形状中的任意一个格子下方的一个格子是黑的，或者到了最下一行，都认为是到底了
     */
    fun isShapeHitBottom() : Boolean {
        getBrick()?.shape?.forEach {
            if (it.y >= height - 1 || blocks!![it.y + 1][it.x]) {
                return true
            }
        }
        return false
    }

    fun canMoveLeft() : Boolean{
        getBrick()?.shape?.forEach {
            if (it.x <= 0 || blocks!![it.y][it.x - 1]) {
                return false
            }
        }
        return true
    }

    fun canMoveRight() : Boolean {
        getBrick()?.shape?.forEach {
            if (it.x >= width - 1 || blocks!![it.y][it.x + 1]) {
                return false
            }
        }
        return true
    }

    fun isBrickValid() : Boolean {
        getBrick()?.shape?.forEach {
            if (it.x < 0 || it.x >= width || it.y < 0 || it.y >= height || blocks!![it.y][it.x]) {
                return false
            }
        }
        return true
    }

    private fun isLineFull(line : Array<Boolean>) : Boolean{
        line.forEach {
            if (!it){
                return false
            }
        }
        return true
    }

    private fun wipeLine(line: Int) {
        for (i in line downTo 1) {
            blocks!![i] = blocks!![i - 1].copyOf()
        }
    }

    fun wipe(): Boolean {
        var ret = false
        synchronized(this) {
            for (i in IntRange(0, blocks!!.size - 1)) {
                if (isLineFull(blocks!![i])) {
                    wipeLine(i)
                    ret = true
                }
            }
        }
        return ret
    }

    private fun drawBrickInRect(canvas: Canvas, rect: Rect, brick: BaseBrick) {
        var w = min(rect.width(), rect.height())
        var blockWidth = w/4
        var offset = brick.shape[0].x
        for (point in brick.shape) {
            offset = min(offset, point.x)
        }
        var blockRect = Rect()
        var paint = Paint()
        paint.color = Color.BLACK
        for (point in brick.shape) {
            blockRect.left = rect.left + (point.x - offset) * blockWidth
            blockRect.top = rect.top + point.y * blockWidth
            blockRect.right = blockRect.left + blockWidth
            blockRect.bottom = blockRect.top + blockWidth
            canvas.drawRect(blockRect, paint)
        }
    }

    private fun drawHead(canvas: Canvas, left: Int, top: Int) {
        val paint = Paint()
        var x = 0f
        var y = 0f
        paint.textSize = 24f
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        x = left.toFloat()
        y = top.toFloat() + 10
        canvas.drawText("score: $score", x, y + 20, paint)

        if (nextBrick != null) {
            y += 30
            canvas.drawText("next: ", x, y + 30, paint)

            var brickRect = Rect()
            brickRect.left = left + 80
            brickRect.top = y.toInt()
            brickRect.right = brickRect.left + 40
            brickRect.bottom = brickRect.top + 40
            drawBrickInRect(canvas, brickRect, nextBrick!!)
        }
    }

    fun draw(canvas: Canvas, margin: Rect?) {
        var left = 0
        var top = 0
        var w = canvas.width
        var h = canvas.height

        synchronized(this) {
            if (margin != null) {
                left = margin.left
                top = margin.top
                w -= (margin.left + margin.right)
                h -= (margin.top + margin.bottom)
            }

            var blockWidth = w.toFloat() / width
            val th = blockWidth * height
            if (th <= h) {
                h = th.toInt()
            } else {
                blockWidth = h.toFloat() / height
                w = (blockWidth * width).toInt()
                left = (canvas.width - w)/2
            }

            drawHead(canvas, left, 0)
            val paint = Paint()
            paint.strokeWidth = innerWidth.toFloat()
            paint.color = Color.BLACK

            //draw vertical inner line
            for (i in IntRange(1, width - 1)) {
                canvas.drawLine(left + i * blockWidth, top.toFloat(), left + i * blockWidth, top + h.toFloat(), paint)
            }

            //draw vertical border line
            paint.strokeWidth = border.toFloat()
            canvas.drawLine(left.toFloat(), top.toFloat(), left.toFloat(), top + h.toFloat(), paint)
            canvas.drawLine(
                left + w - border.toFloat(),
                top.toFloat(),
                left + w - border.toFloat(),
                top + h.toFloat(),
                paint
            )

            //draw horizontal border line
            canvas.drawLine(left.toFloat(), top.toFloat(), left + w.toFloat(), top.toFloat(), paint)
            canvas.drawLine(
                left.toFloat(),
                top + h - border.toFloat(),
                left + w - border.toFloat(),
                top + h - border.toFloat(),
                paint
            )

            //draw horizontal inner line
            paint.strokeWidth = innerWidth.toFloat()
            for (i in IntRange(1, height - 1)) {
                canvas.drawLine(left.toFloat(), top + i * blockWidth, left + w.toFloat(), top + i * blockWidth, paint)
            }

            for (y in IntRange(0, height - 1)) {
                for (x in IntRange(0, width - 1)) {
                    if (blocks!![y][x]) {
                        val fx = x * blockWidth + left
                        val fy = y * blockWidth + top
                        canvas.drawRect(fx, fy, fx + blockWidth, fy + blockWidth, paint)
                    }
                }
            }

            //draw brick
            if (brick != null) {
                for (point in brick!!.shape) {
                    val fx = point.x * blockWidth + left
                    val fy = point.y * blockWidth + top
                    canvas.drawRect(fx, fy, fx + blockWidth, fy + blockWidth, paint)
                }
            }
        }
    }

    fun reset() {
        synchronized(this) {
            blocks = Array(height) {_ ->
                Array(width) {_ ->
                    false
                }
            }
            brick = null
        }
    }
}