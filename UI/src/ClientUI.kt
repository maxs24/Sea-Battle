package ru.max.networking

import ru.max.Order
import java.lang.NumberFormatException
import java.net.Socket
import ru.max.windows.components.Status

class ClientUI {
    val s: Socket
    var stop = false
    private var cmn : Communicator
    val gameData = GameDataUI.getInstance()
    val isAlive
        get() = !stop && s.isConnected

    companion object{
        val port = 5603
        val serveraddres = "localhost"
    }

    init{
        s = Socket(serveraddres, port)
        cmn = Communicator(s)
        cmn.addDataReceivedListeners(::dataReceived)
        cmn.start()
        gameData.addSetPositionListener(::sendAction)
        gameData.addSetStatusListener(::sendStatus)
        gameData.addSetReadyListener(::sendReady)
        gameData.addWinListener(::sendWin)
    }

    private fun dataReceived(datastr: String){
        val str = datastr.split("=",limit = 2)
        if(str.size == 2){
            when(str[0]){
                "pos" -> acceptPosition(str[1])
                "statuscell" -> acceptStatusCell(str[1])
                "status" -> acceptStatus(str[1])
                "ready" -> acceptReady(str[1])
                "win" -> acceptWin(str[1])
            }
        }
    }

    private fun acceptPosition(str: String){
        val pos = str.split(";",limit = 2).map {
            try{
                it.toInt()

            }catch (e: NumberFormatException) {-1}
        }
        if(pos.size == 2){
            if(pos[0] != -1 && pos[1] != -1){
                gameData.lastGotPos = Pair(pos[0],pos[1])
            }
        }
    }

    private fun acceptStatusCell(str:String){
        val mas_s = str.split(";",limit = 2)
        var st: Status? = null
        when(mas_s[0]){
            "SEA" -> st = Status.SEA
            "SHIP" -> st = Status.SHIP
        }
        var bl: Boolean? = null
        when(mas_s[1]){
            "true" -> bl = true
            "false" -> bl = false
        }
        if(st != null && bl != null){
            gameData.lastGotStatus = Pair(st,bl)
        }
    }

    private fun acceptWin(str: String){
        if(str == "true"){
            cmn.stop()
        }
    }

    private fun acceptStatus(str: String){
        if(str == "true"){
            gameData.clickRole = Order.FIRST
        }
        else{
            gameData.clickRole = Order.SECOND
        }
    }

    private fun acceptReady(str: String){
        if(str == "true"){
            gameData.isreadygot = true
        }
    }

    private fun sendAction(row: Int, col: Int){
        if(cmn.isAlive){
            val v = "pos=$row;$col"
            cmn.sendData(v)
        }
    }

    private fun sendStatus(st: Status, bol: Boolean){
        if(cmn.isAlive){
            val v = "statuscell=$st;$bol"
            cmn.sendData(v)
        }
    }

    private fun sendReady(){
        if(cmn.isAlive){
            val v = "ready=true"
            cmn.sendData(v)
        }
    }

    private fun sendWin(){
        if(cmn.isAlive){
            val v = "win=true"
            cmn.sendData(v)
        }
    }
}