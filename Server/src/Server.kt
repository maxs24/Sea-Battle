package ru.max.networking

import java.net.ServerSocket
import java.net.Socket

class Server private constructor(){

    private val ss: ServerSocket
    private val stop = false
    private val connectedClients = mutableListOf<ConnectedClient>()

    companion object{
        private val PORT = 5603
        private val srv: Server = Server()
        fun getInstance(): Server{
            return srv
        }
    }

    inner class ConnectedClient(
        val connectedClients: MutableList<ConnectedClient>,
        skt: Socket,
        val id: Int){

        private val cmn: Communicator
        var id_op = 0
        set(value){
            field = value
            sendStatus(id%2==0)
        }

        init{
            cmn = Communicator(skt)
            cmn.addDataReceivedListeners(::dataReceived)
            cmn.start()
        }

        private fun dataReceived(data: String){
            val vls = data.split("=",limit = 2)
            if(vls.size==2){
                when(vls[0]){
                    "pos" -> setAction(data)
                    "statuscell" -> setAction(data)
                    "ready" -> setAction(data)
                    "win" -> setAction(data)
                }
            }
        }

        private fun sendAction(data: String){
            if(cmn.isAlive){
                cmn.sendData(data)
            }
        }

        private fun sendStatus(status: Boolean){
            if(cmn.isAlive){
                cmn.sendData("status=$status")
            }
        }

        private fun setAction(data: String){
            var partner: ConnectedClient? = null
            connectedClients.forEach {
                if(this.id_op == it.id){
                    partner = it
                }
            }
            partner?.sendAction(data)
        }
    }

    init{
        ss = ServerSocket(PORT)
        while(!stop){
            acceptClient()
        }
    }

    private fun acceptClient() {
        println("Ожидание подключения")
        val s = ss.accept()
        println("Новый клиент подключен")
        if (s!=null) {
            var id = 0
            if (connectedClients.isNotEmpty()) {
                if(connectedClients.size%2 == 0){
                    id = connectedClients.lastIndex + 1
                    connectedClients.add(ConnectedClient(connectedClients, s, id))
                }else {
                    id = connectedClients.lastIndex + 1
                    connectedClients.add(ConnectedClient(connectedClients, s, id))
                    connectedClients[id - 1].id_op = id
                    connectedClients[id].id_op = id - 1
                }
            }else {
                connectedClients.add(ConnectedClient(connectedClients, s, id))
            }
        }
    }
}