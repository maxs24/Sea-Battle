package ru.max.networking

import ru.max.Order
import ru.max.windows.components.Status

class GameDataUI {

    companion object{
        private val gameDataUI = GameDataUI()

        fun getInstance(): GameDataUI{
            return gameDataUI
        }
    }

    private val onGenerate = mutableListOf<()->Unit>()

    fun addOnGenerateListener(l: ()->Unit){
        onGenerate.add(l)
    }

    fun removeOnGenerateListener(l: ()->Unit){
        onGenerate.remove(l)
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
        if(value==Order.FIRST){
            isreadyset = true
        }
    }

    var isreadyset = false
        set(value){
            field = value
            onSetReady.forEach {
                it.invoke()
            }
        }
    var isreadygot = false
        set(value){
            field = value
            if(clickRole == Order.FIRST){
                onGenerate.forEach {
                    it.invoke()
                }
            }
            else{
                isreadyset = true
            }
        }

    var lastGotPos: Pair<Int,Int> = Pair(-1,-1)
        set(value) {
            field = value
            onGotPosition.forEach {
                it.invoke(value.first,value.second)
            }

        }

    var lastSetPosition: Pair<Int,Int> = Pair(-1,-1)
    set(value){
        field = value
        onSetPosition.forEach {
            it.invoke(value.first,value.second)
        }

    }

    var lastSetStatus:  Pair<Status,Boolean> = Pair(Status.NONE,false)
    set(value){
        field = value
        onSetStatus.forEach {
            it.invoke(value.first,value.second)
        }
    }

    var lastGotStatus: Pair<Status,Boolean> = Pair(Status.NONE,false)
        set(value) {
            field = value
            onGotStatus.forEach {
                it.invoke(value.first,value.second)
            }

        }

    var win = false
        set(value) {
            field = value
            onWin.forEach {
                it.invoke()
            }
        }

}