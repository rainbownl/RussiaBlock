package com.nnl.russiablock.msg

class MsgId {
    companion object {
        private var curId = -1
        fun getId() : Int {
            curId ++
            return curId
        }
    }
}