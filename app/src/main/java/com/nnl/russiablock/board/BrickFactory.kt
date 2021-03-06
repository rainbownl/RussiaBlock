package com.nnl.russiablock.board

class BrickFactory {
    companion object {
        var LINE_BRICK = 0
        var T_BRICK = 1
        var L_BRICK = 2
        var RL_BRICK = 3
        var RECT_BRICK = 4

        var MAX_BRICK = 5
    }

    fun createBrick(type: Int, x: Int, y: Int) : BaseBrick?{
        when(type){
            LINE_BRICK -> return LineBrick(x, y)
            T_BRICK -> return TBrick(x, y)
            L_BRICK -> return LBrick(x, y)
            RL_BRICK -> return RLBrick(x, y)
            RECT_BRICK -> return RectBrick(x, y)
        }
        return null
    }
}