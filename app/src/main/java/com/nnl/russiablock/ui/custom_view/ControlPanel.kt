package com.nnl.russiablock.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ControlPanel(context : Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        val upIndex = 0
        val leftIndex = upIndex + 1
        val centerIndex = leftIndex + 1
        val rightIndex = centerIndex + 1
        val downIndex = rightIndex + 1
        val maxRect = downIndex + 1

    }
    private val panColor : Int = 0x55000000
    private val clickColor : Int = 0xaa000000.toInt()
    private var rects = Array(maxRect){
        CRect()
    }

    private var clickDownIndex = -1
    private var actionListener: OnActionListener? = null
    private var upDrawable: Drawable? = null
    private var upClickDrawable : Drawable? = null
    private var leftDrawable : Drawable? = null
    private var leftClickDrawable : Drawable? = null
    private var centerDrawable : Drawable? = null
    private var centerClickDrawable : Drawable? = null
    private var rightDrawable : Drawable? = null
    private var rightClickDrawable : Drawable? = null
    private var downDrawable : Drawable? = null
    private var downClickDrawable : Drawable? = null

    private class CRect{
        var x = 0f
        var y = 0f
        var width = 0
        var height = 0
    }

    fun setDrawables(index: Int, normal: Drawable, click: Drawable) {
        when (index) {
            upIndex -> {
                upDrawable = normal
                upClickDrawable = click
            }
            leftIndex -> {
                leftDrawable = normal
                leftClickDrawable = click
            }
            centerIndex -> {
                centerDrawable = normal
                centerClickDrawable = click
            }
            rightIndex -> {
                rightDrawable = normal
                rightClickDrawable = click
            }
            downIndex -> {
                downDrawable = normal
                downClickDrawable = click
            }
        }
    }

    fun setOnActionListener(in_listener: OnActionListener) {
        this.actionListener = in_listener
    }

    private fun calRects(w: Int) {
        var rectWidth = w.toFloat()/3
        rects[upIndex].x = rectWidth
        rects[upIndex].y = 0f
        rects[leftIndex].x = 0f
        rects[leftIndex].y = rectWidth
        rects[centerIndex].x = rectWidth
        rects[centerIndex].y = rectWidth
        rects[rightIndex].x = rectWidth * 2
        rects[rightIndex].y = rectWidth
        rects[downIndex].x = rectWidth
        rects[downIndex].y = rectWidth * 2
        for (rect in rects) {
            rect.width = w/3
            rect.height = rect.width
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        calRects(canvas!!.width)
        var paint = Paint()
        for ((index , rect) in rects.withIndex()) {
            var drawable: Drawable? = null
            if (index == clickDownIndex) {
                paint.color = clickColor
                drawable = when (index) {
                    upIndex -> upClickDrawable
                    leftIndex -> leftClickDrawable
                    centerIndex -> centerClickDrawable
                    rightIndex -> rightClickDrawable
                    downIndex -> downClickDrawable
                    else -> null
                }
            } else {
                paint.color = panColor
                drawable = when(index) {
                    upIndex -> upDrawable
                    leftIndex -> leftDrawable
                    centerIndex -> centerDrawable
                    rightIndex -> rightDrawable
                    downIndex -> downDrawable
                    else -> null
                }
            }
            if (drawable != null) {
                drawable.setBounds(rect.x.toInt(), rect.y.toInt(), rect.x.toInt() + rect.width, rect.y.toInt() + rect.height)
                drawable.draw(canvas)
            } else {
                canvas?.drawRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, paint)
            }
        }
    }

    fun getHitIndex(x: Float, y: Float) : Int{
        var tx = x
        var ty = y
        for ((index, rect) in rects.withIndex()) {
            if (tx >= rect.x && tx <= rect.x + rect.width
                && ty >= rect.y && ty <= rect.y + rect.height){
                return index
            }
        }
        return -1
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> clickDownIndex = getHitIndex(event.x, event.y)
                MotionEvent.ACTION_UP -> {
                    val clickUpIndex = getHitIndex(event.x, event.y)
                    if (clickUpIndex == clickDownIndex && actionListener != null) {
                        actionListener!!.onClick(clickUpIndex)
                    }
                }
            }
        }
        return true
    }

    interface OnActionListener{
        fun onClick(index: Int)
        fun onLongClick(index: Int)
    }
}