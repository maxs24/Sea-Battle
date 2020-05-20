package ru.max.networking

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import kotlin.concurrent.thread

class Communicator(val s: Socket) {

    private var active: Boolean
    private var communicationProcess: Thread? = null
    private var dataReceivedListeners = mutableListOf<(String)->Unit>()
    val isAlive: Boolean
        get() = active && s.isConnected

    init{
        active = true
    }

    fun addDataReceivedListeners(l:(String)->Unit){
        dataReceivedListeners.add(l)
    }

    fun removeDataReceivedListeners(l:(String)->Unit){
        dataReceivedListeners.remove(l)
    }

    private fun communicate(){
        while(isAlive){
            try{
                val value = receivedData()
                if(value!=null) {
                    dataReceivedListeners.forEach {
                        it.invoke(value)
                    }
                }
            }catch(e: Exception){
                active = false
                if(!s.isClosed) s.close()
                print("Обмен данными неожиданно прекращен")
            }
        }
    }

    private fun receivedData(): String?{
        var data: String? = null
        if(isAlive){
            try {
                val br = BufferedReader(InputStreamReader(s.getInputStream()))
                data = br.readLine()
            }catch (e: Exception){
                active = false
                println("Не удалось прочитать данные из сети")
                data = null
            }
        }
        return data
    }

    fun sendData(data: String){
        try {
            if (isAlive) {
                val pw = PrintWriter(s.getOutputStream())
                pw.println(data)
                pw.flush()
            }
        }catch (e: Exception){
            println("Не удалось отправить данные в сеть.")
            active = false
        }
    }

    fun start(){
        thread {
            if(communicationProcess?.isAlive == true){
                stop()
            }
            active = true
            communicationProcess = thread {
                communicate()
            }
        }
    }

    fun stop(){
        active = false
        if(communicationProcess?.isAlive == true){
            communicationProcess?.interrupt()
            communicationProcess?.join()
        }
        if(!s.isClosed) s.close()
    }

}
