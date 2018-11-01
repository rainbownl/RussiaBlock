package com.nnl.russiablock.board

import android.graphics.Point

open class BaseBrick(var size: Int) {
    var shape: Array<Point> = Array(size){ Point() }
    private var rotate = 0

    private var backupShape : Array<Point>? = null
    private var backupRotate = 0

    private fun backup() {
        backupShape = Array(size){ Point()}
        for (i in IntRange(0, size - 1)) {
            backupShape!![i].x = shape[i].x
            backupShape!![i].y = shape[i].y
        }
        backupRotate = rotate
    }

    fun recover() {
        if (backupShape != null) {
            shape = backupShape!!
            rotate = backupRotate
        }
    }

    //rotate right once
    open fun rotate() {
        backup()
        shape.forEach {
            rotatePoint(it, shape[1])
        }
        rotate = (rotate + 90)%360
    }

    fun moveDown(){
        shape.forEach {
            it.y ++
        }
    }

    fun moveLeft(){
        shape.forEach {
            it.x --
        }
    }

    fun moveRight() {
        shape.forEach {
            it.x ++
        }
    }

    private fun rotatePoint(point: Point, basePoint: Point) {
        val offsetX = point.x - basePoint.x
        val offsetY = point.y - basePoint.y
        if (offsetX == 0 && offsetY != 0) {
            point.x -= offsetY
            point.y -= offsetY
        } else if(offsetX != 0 && offsetY == 0) {
            point.x -= offsetX
            point.y += offsetX
        }
    }
}