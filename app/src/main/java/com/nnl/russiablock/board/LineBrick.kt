package com.nnl.russiablock.board

import com.nnl.russiablock.debug.RbLog

class LineBrick(x: Int, y: Int): BaseBrick(4) {
    init {
        shape[0].x = x
        shape[0].y = y
        shape[1].x = x
        shape[1].y = y + 1
        shape[2].x = x
        shape[2].y = y + 2
        shape[3].x = x
        shape[3].y = y + 3
        RbLog.d("LineBrick", "LineBrick init")
    }
}