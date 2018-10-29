package com.nnl.russiablock.board

import com.nnl.russiablock.debug.RbLog

class LBrick(x: Int, y: Int) : BaseBrick(4) {
    init {
        shape[0].x = x
        shape[0].y = y
        shape[1].x = x
        shape[1].y = y + 1
        shape[2].x = x + 1
        shape[2].y = y + 1
        shape[3].x = x + 2
        shape[3].y = y + 1
        RbLog.d("LBrick", "LBrick init")
    }
}