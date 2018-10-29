package com.nnl.russiablock.board

import com.nnl.russiablock.debug.RbLog

class TBrick(x: Int, y: Int) : BaseBrick(4) {
    init {
        shape[0].x = x - 1
        shape[0].y = y
        shape[1].x = x
        shape[1].y = y
        shape[2].x = x + 1
        shape[2].y = y
        shape[3].x = x
        shape[3].y = y + 1
        RbLog.d("TBrick", "create TBrick")
    }
}