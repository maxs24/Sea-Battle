package ru.max

import ru.max.windows.components.Status

class GameData(){

    companion object{
        private val gd = GameData()
        fun getInstance() = gd
    }

    private val onSetPosition = mutableListOf<(Int,Int)->Unit>()

    fun addSetPositionListener(l: (Int,Int)->Unit){
        onSetPosition.add(l)
    }

    fun removeSetPositionListener(l: (Int,Int)->Unit){
        onSetPosition.remove(l)
    }

    private val onGotPosition = mutableListOf<(Int,Int)->Unit>()

    fun addGotPositionListener(l: (Int,Int)->Unit){
        onGotPosition.add(l)
    }

    fun removeGotPositionListener(l: (Int,Int)->Unit){
        onGotPosition.remove(l)
    }

    private val onSetStatus = mutableListOf<(Status,Boolean)->Unit>()

    fun addSetStatusListener(l:(Status,Boolean)->Unit){
        onSetStatus.add(l)
    }

    fun removeSetStatusListener(l:(Status,Boolean)->Unit){
        onSetStatus.remove(l)
    }

    private val onGotStatus = mutableListOf<(Status,Boolean)->Unit>()

    fun addGotStatusListener(l:(Status, Boolean)->Unit){
        onGotStatus.add(l)
    }

    fun removeGotStatusListener(l:(Status,Boolean)->Unit){
        onGotStatus.remove(l)
    }

    private val onGotST = mutableListOf<(Order)->Unit>()

    fun addGotStListener(l:(Order)->Unit){
        onGotST.add(l)
    }

    fun removeGotStListener(l:(Order)->Unit){
        onGotST.remove(l)
    }

    private val onGotReady = mutableListOf<()->Unit>()

    fun addGotReadyListener(l:()->Unit){
        onGotReady.add(l)
    }

    fun removeGotReadyListener(l:()->Unit){
        onGotReady.remove(l)
    }

    private val onSetReady = mutableListOf<()->Unit>()

    fun addSetReadyListener(l:()->Unit){
        onSetReady.add(l)
    }

    fun removeSetReadyListener(l:()->Unit){
        onSetReady.remove(l)
    }

    private val onWin = mutableListOf<()->Unit>()

    fun addWinListener(l: ()->Unit){
        onWin.add(l)
    }

    fun removeWinListener(l: ()->Unit){
        onWin.remove(l)
    }

    private val onWinOP = mutableListOf<()->Unit>()

    fun addWinOPListener(l: ()->Unit){
        onWinOP.add(l)
    }

    fun removeWinOPListener(l: ()->Unit){
        onWinOP.remove(l)
    }

    var clickRole = Order.NONE
    set(value){
        field = value
        if(!isclear) {
            onGotST.forEach {
                it.invoke(field)
            }
        }
    }
    var clickable = false
    var clickable_op = false

    var isreadyset = false
    set(value){
        if(!isclear) {
            field = value
            onSetReady.forEach { it.invoke() }
        }
    }

    var isreadygot = false
        set(value){
            field = value
            if(!isclear) {
                onGotReady.forEach { it.invoke() }
            }
        }

    var lastGotPos: Pair<Int,Int> = Pair(-1,-1)
    set(value){
        field = value
        if(!isclear) {
            onGotPosition.forEach {
                it.invoke(value.first, value.second)
            }
        }
    }

    var lastGotStatus: Pair<Status,Boolean> = Pair(Status.NONE,false)
    set(value){
        field = value
        if(!isclear) {
            onGotStatus.forEach {
                it.invoke(value.first, value.second)
            }
        }
    }

    var lastSetPosition: Pair<Int,Int> = Pair(-1,-1)
    set(value){
        field = value
        if(!isclear) {
            onSetPosition.forEach {
                it.invoke(value.first, value.second)
            }
        }
    }

    var lastSetStatus: Pair<Status,Boolean> = Pair(Status.NONE,false)
    set(value){
        field = value
        if(!isclear) {
            onSetStatus.forEach {
                it.invoke(value.first, value.second)
            }
        }
    }

    var win = false
    set(value){
        field = value
        if(!isclear) {
            onWin.forEach {
                it.invoke()
            }
        }
    }

    var op_win = false
    set(value){
        field = value
        if(!isclear) {
            onWinOP.forEach {
                it.invoke()
            }
        }
    }

    private var isclear = false

    fun clear(){
        isclear = true
        op_win = false
        win = false
        lastSetStatus = Pair(Status.NONE,false)
        lastSetPosition = Pair(-1,-1)
        lastGotStatus = Pair(Status.NONE,false)
        lastGotPos = Pair(-1,-1)
        isreadygot = false
        isreadyset = false
        clickable = false
        clickable_op = false
        clickRole = Order.NONE
        isclear = false
    }
}

enum class Order(){
    FIRST,
    SECOND,
    NONE
}