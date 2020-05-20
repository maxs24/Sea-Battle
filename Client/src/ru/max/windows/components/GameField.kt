package ru.max.windows.components

import ru.max.GameData
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JPanel

class GameField(val side: Boolean,val gameData: GameData): JPanel() {

    private val cells: Array<GameCell>

    init{
        layout = null
        GameCell.fieldHeight = height
        GameCell.fieldWidth = width
        gameData.addGotPositionListener(::onExternalClick)
        gameData.addGotStatusListener(::onExternalStatus)
        cells = Array(GameCell.COL_COUNT*GameCell.ROW_COUNT){
            GameCell(it / GameCell.ROW_COUNT,
                it % GameCell.COL_COUNT,
                side)
        }

        cells.forEach {
            add(it)
            if(side){
                it.status = Status.SEA
            }
        }

        addComponentListener(object :ComponentAdapter(){
            override fun componentShown(e: ComponentEvent?) {
                super.componentShown(e)
                GameCell.fieldHeight = height
                GameCell.fieldWidth = width
                cells.forEach { it.updateBounds() }
            }

            override fun componentResized(e: ComponentEvent?) {
                GameCell.fieldHeight = height
                GameCell.fieldWidth = width
                cells.forEach { it.updateBounds() }
            }
        })
    }

    private fun onExternalClick(row: Int,col: Int){
        val id = getCellId(row,col)
        if(id >= 0 && side && !cells[id].isFired){
            if(cells[id].status == Status.SHIP){
                cells[id].isFired = true
                gameData.clickable_op = false
                if(isKill(row,col)){
                    drawkill(row,col)
                    gameData.lastSetStatus = Pair(cells[id].status,true)
                }else{
                    gameData.lastSetStatus = Pair(cells[id].status,false)
                }
            }else {
                cells[id].isFired = true
                gameData.clickable_op = true
                gameData.lastSetStatus = Pair(cells[id].status,false)
            }
            cells[id].repaint()
        }
    }

    private fun onExternalStatus(status: Status,bol: Boolean){
        val id = getCellId(gameData.lastSetPosition.first,gameData.lastSetPosition.second)
        if(id>=0 && !side){
            if(status == Status.SHIP){
                cells[id].status = status
                cells[id].isFired = true
                gameData.clickable_op = true
            }else{
                cells[id].status = status
                cells[id].isFired = true
                gameData.clickable_op = false
            }
            if(bol){
                drawkill(gameData.lastSetPosition.first,gameData.lastSetPosition.second)
                if(isWin()){
                    gameData.win = true
                }
            }
        }
    }

    private fun getCellId(row: Int,col: Int): Int{
        return row*GameCell.COL_COUNT + col
    }

    fun countShips(): Array<Int>{
        val count = arrayOf(0,0,0,0)
        var id = 0
        while(id < 100){
            if(cells[id].status == Status.SHIP){
                val ship = isShipGor(id)
                if(ship!= -1){
                    when(ship){
                        1 -> {
                            count[0] += 1
                        }
                        2 -> {
                            count[1] += 1
                            id += 1
                        }
                        3 -> {
                            count[2] += 1
                            id += 2
                        }
                        4 -> {
                            count[3] += 1
                            id += 3
                        }
                    }
                }else{
                    return arrayOf(-1,-1,-1,-1)
                }
            }
            id += 1
        }
        for(i in 0..9){
            var j = 0
            while(j < 10){
                val idg = i + j*10
                if(cells[idg].status == Status.SHIP){
                    val ship = isShipVer(idg)
                    if(ship != -1){
                        when(ship){
                            1 -> {
                                count[0] += 1
                            }
                            2 -> {
                                count[1] += 1
                                j += 1
                            }
                            3 -> {
                                count[2] += 1
                                j += 2
                            }
                            4 -> {
                                count[3] += 1
                                j += 3
                            }
                        }
                    }else{
                        return arrayOf(-1,-1,-1,-1)
                    }
                }
                j++
            }
        }
        return count
    }

    private fun isShipGor(id: Int): Int{
        if(id == 0){
            if(cells[id+10].status == Status.SHIP){
                return 0
            }
            if(cells[id+11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                    count += 1
                }
                else{
                    break
                }
            }
            return count
        }
        if(id == 9){
            if(cells[id+10].status == Status.SHIP){
                return 0
            }
            if(cells[id+9].status == Status.SHIP){
                return -1
            }
            return 1
        }
        if(id == 90){
            if(cells[id-10].status == Status.SHIP){
                return 0
            }
            if(cells[id-9].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                    count += 1
                }
                else{
                    break
                }
            }
            return count
        }
        if(id == 99){
            if(cells[id-10].status == Status.SHIP){
                return 0
            }
            if(cells[id-11].status == Status.SHIP){
                return -1
            }
            return 1
        }
        if(id % 10 == 0){
            if(cells[id-9].status == Status.SHIP || cells[id+11].status == Status.SHIP){
                return -1
            }
            if(cells[id+10].status == Status.SHIP || cells[id-10].status == Status.SHIP){
                return 0
            }
            var count = 1
            for(i in 1..3){
                if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                    count += 1
                }
                else{
                     break
                }
            }
            return count
        }
        if(id % 10 == 9){
            if(cells[id+9].status == Status.SHIP || cells[id-11].status == Status.SHIP){
                return -1
            }
            if(cells[id+10].status == Status.SHIP || cells[id-10].status == Status.SHIP){
                return 0
            }
            return 1
        }
        if(id > 0 && id < 9){
            if(cells[id+10].status == Status.SHIP){
                return 0
            }
            if(cells[id+9].status == Status.SHIP || cells[id+11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                    count += 1
                }
                else{
                    break
                }
            }
            return count
        }
        if(id > 90 && id < 99){
            if(cells[id-10].status == Status.SHIP){
                return 0
            }
            if(cells[id-9].status == Status.SHIP || cells[id-11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                    count += 1
                }
                else{
                    break
                }
            }
            return count
        }
        if(cells[id+10].status == Status.SHIP || cells[id-10].status == Status.SHIP){
            return 0
        }
        if(cells[id-9].status == Status.SHIP || cells[id-11].status == Status.SHIP||cells[id+9].status == Status.SHIP || cells[id+11].status == Status.SHIP){
            return -1
        }
        var count = 1
        for(i in 1..3){
            if((id+i <= (id - id%10 + 9)) && cells[id+i].status == Status.SHIP){
                count += 1
            }
            else{
                break
            }
        }
        return count
    }

    private fun isShipVer(id: Int): Int{
        if(id == 0){
            if(cells[id+1].status == Status.SHIP){
                return 0
            }
            if(cells[id+11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                    count+=1
                }else{
                    break
                }
            }
            if(count != 1){
                return count
            }else{
                return 0
            }
        }
        if(id == 9){
            if(cells[id-1].status == Status.SHIP){
                return 0
            }
            if(cells[id+9].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                    count+=1
                }else{
                    break
                }
            }
            if(count != 1){
                return count
            }else{
                return 0
            }
        }
        if(id == 90){
            if(cells[id+1].status == Status.SHIP){
                return 0
            }
            if(cells[id-9].status == Status.SHIP){
                return -1
            }
            return 0
        }
        if(id == 99){
            if(cells[id-1].status == Status.SHIP){
                return 0
            }
            if(cells[id-11].status == Status.SHIP){
                return -1
            }
            return 0
        }
        if(id % 10 == 0){
            if(cells[id+1].status == Status.SHIP){
                return 0
            }
            if(cells[id-9].status == Status.SHIP||cells[id+11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                    count+=1
                }else{
                    break
                }
            }
            if(count != 1){
                return count
            }else{
                return 0
            }
        }
        if(id % 10 == 9){
            if(cells[id-1].status == Status.SHIP){
                return 0
            }
            if(cells[id+9].status == Status.SHIP||cells[id-11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                    count+=1
                }else{
                    break
                }
            }
            if(count != 1){
                return count
            }else{
                return 0
            }
        }
        if(id > 0 && id < 9){
            if(cells[id-1].status == Status.SHIP||cells[id+1].status == Status.SHIP){
                return 0
            }
            if(cells[id+9].status == Status.SHIP||cells[id+11].status == Status.SHIP){
                return -1
            }
            var count = 1
            for(i in 1..3){
                if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                    count+=1
                }else{
                    break
                }
            }
            if(count != 1){
                return count
            }else{
                return 0
            }
        }
        if(id > 90 && id < 99){
            if(cells[id-1].status == Status.SHIP||cells[id+1].status == Status.SHIP){
                return 0
            }
            if(cells[id-9].status == Status.SHIP||cells[id-11].status == Status.SHIP){
                return -1
            }
            return 0
        }
        if(cells[id-1].status == Status.SHIP||cells[id+1].status == Status.SHIP){
            return 0
        }
        if(cells[id-9].status == Status.SHIP||cells[id-11].status == Status.SHIP||cells[id+9].status == Status.SHIP||cells[id+11].status == Status.SHIP){
            return -1
        }
        var count = 1
        for(i in 1..3){
            if((id+10*i <= id%10 + 90) && cells[id+10*i].status == Status.SHIP){
                count+=1
            }else{
                break
            }
        }
        if(count != 1){
            return count
        }else{
            return 0
        }
    }

    private fun shipKill(row: Int,col: Int): MutableList<Pair<Int,Int>>{
        val id = getCellId(row,col)
        val mas_ship = mutableListOf<Pair<Int,Int>>()
        mas_ship.add(Pair(row,col))
        val mas_d = arrayOf(-1,1,10,-10)
        for(i in mas_d){
            for(j in 1..3){
                val id_new = id + i*j
                if(id_new > 0 && id_new < 100){
                    if(i == 1 || i == -1){
                        if(id_new - id_new % 10 == id - id % 10){
                            if(cells[id_new].status == Status.SHIP){
                                if(cells[id_new].isFired == true){
                                    mas_ship.add(Pair((id_new - id_new%10)/10,id_new%10))
                                }else{
                                    mas_ship.add(Pair(-1,-1))
                                }
                            }else{
                                break
                            }
                        }
                    }else{
                        if(id_new % 10 == id % 10){
                            if(cells[id_new].status == Status.SHIP){
                                if(cells[id_new].isFired == true){
                                    mas_ship.add(Pair((id_new - id_new%10)/10,id_new%10))
                                }else{
                                    mas_ship.add(Pair(-1,-1))
                                }
                            }else{
                                break
                            }
                        }
                    }
                }
            }
        }
        return mas_ship
    }

    private fun isKill(row: Int,col: Int): Boolean{
        val mas = shipKill(row,col)
        for(i in mas){
            if(i.first == -1 || i.second == -1){
                return false
            }
        }
        return true
    }

    private fun drawkill(row: Int, col: Int){
        val mas = shipKill(row,col)
        for (k in mas){
            for(i in -1..1){
                for(j in -1..1) {
                    if (k.second + j > -1 && k.second + j < 10 && k.first + i > -1 && k.first + i < 10) {
                        if (cells[getCellId(k.first+i, k.second + j)].status != Status.SHIP) {
                            cells[getCellId(k.first+i, k.second + j)].isFired = true
                            cells[getCellId(k.first+i, k.second + j)].status = Status.SEA
                        }
                    }
                }
            }
        }
    }

    private fun isWin(): Boolean{
        val mas_ships = countShips()
        if(mas_ships[0] == 4 && mas_ships[1] == 3 && mas_ships[2] == 2 && mas_ships[3] == 1){
            return true
        }
        return false
    }

    fun clear(){
        cells.forEach {
            it.clear()
        }
    }
}