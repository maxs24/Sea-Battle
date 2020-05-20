package ru.max.networking

import ru.max.windows.components.Status
import kotlin.random.Random

class UI(val gameDataUI: GameDataUI){

    private val cells: Array<CellUI>
    private val cells_op: Array<CellUI>

    private var lastwound = Pair(-1,-1)
    private var lastwoundstatus = true

    init{
        gameDataUI.addGotStatusListener(::onExternalStatus)
        gameDataUI.addGotPositionListener(::ExternalClick)
        gameDataUI.addSetReadyListener(::arran)
        gameDataUI.addOnGenerateListener(::getPosition)
        cells = Array(CellUI.COL_COUNT*CellUI.ROW_COUNT){
            CellUI(it/CellUI.ROW_COUNT,
                it%CellUI.COL_COUNT)
        }
        cells.forEach {
            it.status = Status.SEA
        }
        cells_op = Array(CellUI.COL_COUNT*CellUI.ROW_COUNT){
            CellUI(it/CellUI.ROW_COUNT,
                it%CellUI.COL_COUNT)
        }
    }

    private fun getCellId(row: Int,col: Int): Int{
        return row*CellUI.COL_COUNT + col
    }

    fun countShips(cells: Array<CellUI>): Array<Int>{
        val count = arrayOf(0,0,0,0)
        var id = 0
        while(id < 100){
            if(cells[id].status == Status.SHIP){
                val ship = isShipGor(id,cells)
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
                    val ship = isShipVer(idg,cells)
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

    private fun isShipGor(id: Int,cells: Array<CellUI>): Int{
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

    private fun isShipVer(id: Int,cells: Array<CellUI>): Int{
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

    private fun shipKill(row: Int,col: Int,cells: Array<CellUI>): MutableList<Pair<Int,Int>>{
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

    private fun isKill(row: Int,col: Int,cells: Array<CellUI>): Boolean{
        val mas = shipKill(row,col,cells)
        for(i in mas){
            if(i.first == -1 || i.second == -1){
                return false
            }
        }
        return true
    }

    private fun drawkill(row: Int, col: Int,cells: Array<CellUI>){
        val mas = shipKill(row,col,cells)
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
        val mas_ships = countShips(cells_op)
        if(mas_ships[0] == 4 && mas_ships[1] == 3 && mas_ships[2] == 2 && mas_ships[3] == 1){
            return true
        }
        return false
    }

    private fun getPosition(){
        if(!lastwoundstatus){
            if(lastwound.first+1 < 10 && !cells_op[getCellId(lastwound.first+1,lastwound.second)].isFired) {
                gameDataUI.lastSetPosition = Pair(lastwound.first + 1, lastwound.second)
                return
            }
            if(lastwound.first - 1 >= 0 && !cells_op[getCellId(lastwound.first-1,lastwound.second)].isFired){
                gameDataUI.lastSetPosition = Pair(lastwound.first - 1, lastwound.second)
                return
            }
            if(lastwound.second+1 < 10 && !cells_op[getCellId(lastwound.first,lastwound.second+1)].isFired) {
                gameDataUI.lastSetPosition = Pair(lastwound.first, lastwound.second+1)
                return
            }
            if(lastwound.second-1 >= 0 && !cells_op[getCellId(lastwound.first,lastwound.second-1)].isFired) {
                gameDataUI.lastSetPosition = Pair(lastwound.first, lastwound.second-1)
                return
            }
        }else{
            var v: Pair<Int,Int>
            var f = true
            while(f) {
                v = Pair(Random.nextInt(0, 9), Random.nextInt(0, 9))
                if(!cells_op[getCellId(v.first,v.second)].isFired){
                    gameDataUI.lastSetPosition = v
                    f = false
                    break
                }
            }
        }
    }

    fun ExternalClick(row: Int,col: Int){
        val id = getCellId(row,col)
        if(id>= 0){
            cells[id].isFired = true
            if(cells[id].status == Status.SHIP) {
                if (isKill(row, col, cells)) {
                    drawkill(row, col, cells)
                    gameDataUI.lastSetStatus = Pair(cells[id].status, true)
                } else {
                    gameDataUI.lastSetStatus = Pair(cells[id].status, false)
                }
            }else{
                gameDataUI.lastSetStatus = Pair(cells[id].status, false)
                getPosition()
            }
        }
    }

    private fun onExternalStatus(status: Status,bol: Boolean){
        val id = getCellId(gameDataUI.lastSetPosition.first,gameDataUI.lastSetPosition.second)
        if(id >= 0) {
            cells_op[id].isFired = true
            cells_op[id].status = status
            if(status == Status.SHIP){
                lastwound = gameDataUI.lastSetPosition
                lastwoundstatus = bol
            }
            if (bol) {
                drawkill(gameDataUI.lastSetPosition.first, gameDataUI.lastSetPosition.second,cells_op)
                if (isWin()) {
                    gameDataUI.win = true
                }
            }
            if(status == Status.SHIP){
                getPosition()
            }
        }
    }

    private fun arran(){
        arranFour()
        for(i in 0..1){
            arranFree()
        }
        for(i in 0..2){
            arranTwo()
        }
        for(i in 0..3){
            arranOne()
        }
    }

    private fun arranOne(){
        var f = true
        var v: Pair<Int,Int>
        while(f){
            var k = true
            v = Pair(Random.nextInt(0, 9), Random.nextInt(0, 9))
            for(i in -1..1){
                for(j in -1..1){
                    val id = getCellId(v.first+i,v.second+j)
                    if(v.first+i >= 0 && v.first+i<10 && v.second+j >= 0 && v.second+j < 10){
                        if(cells[id].status == Status.SHIP){
                            k = false
                        }
                    }
                }
            }
            if(k){
                cells[getCellId(v.first,v.second)].status = Status.SHIP
                return
            }
        }
    }

    private fun arranTwo(){
        val f = true
        var v: Pair<Int,Int>
        while(f) {
            var k = true
            v = Pair(Random.nextInt(0, 9), Random.nextInt(0, 9))
            for(i in -1..1){
                for(j in -1..1){
                    if(v.first+i >= 0 && v.first+i<10 && v.second+j >= 0 && v.second+j < 10){
                        if(cells[getCellId(v.first+i,v.second+j)].status == Status.SHIP){
                            k = false
                        }
                    }
                }
            }
            if(k){
                for(i in 0..3){
                    when (i) {
                        0 -> {
                            for (i in -1..1) {
                                for (j in -1..1) {
                                    if (v.first + i + 1 >= 0 && v.first + i + 1 < 10 && v.second + j >= 0 && v.second + j < 10) {
                                        if(i != -1 || j != 0) {
                                            if (cells[getCellId(v.first + i + 1, v.second + j)].status == Status.SHIP) {
                                                k = false
                                            }
                                        }
                                    }else{
                                        k = false
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+1,v.second)].status = Status.SHIP
                                return
                            }
                        }
                        1 -> {
                            for (i in -1..1) {
                                for (j in -1..1) {
                                    if (v.first + i >= 0 && v.first + i < 10 && v.second + j + 1 >= 0 && v.second + j + 1 < 10) {
                                        if(i != 0 || j != -1) {
                                            if (cells[getCellId(v.first + i, v.second + j + 1)].status == Status.SHIP) {
                                                k = false
                                            }
                                        }
                                    }else{
                                        k = false
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+1)].status = Status.SHIP
                                return
                            }
                        }
                        2->{
                            for (i in -1..1) {
                                for (j in -1..1) {
                                    if (v.first + i >= 0 && v.first + i < 10 && v.second + j - 1 >= 0 && v.second + j - 1 < 10 ) {
                                        if(i != 0 || j != 1) {
                                            if (cells[getCellId(v.first + i, v.second + j - 1)].status == Status.SHIP) {
                                                k = false
                                            }
                                        }
                                    }else{
                                        k = false
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-1)].status = Status.SHIP
                                return
                            }
                        }
                        3->{
                            for (i in -1..1) {
                                for (j in -1..1) {
                                    if (v.first + i - 1 >= 0 && v.first + i - 1 < 10 && v.second + j >= 0 && v.second + j < 10) {
                                        if((i != 1 || j != 0)) {
                                            if (cells[getCellId(v.first + i - 1, v.second + j)].status == Status.SHIP) {
                                                k = false
                                            }
                                        }
                                    }else{
                                        k = false
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-1,v.second)].status = Status.SHIP
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    private fun arranFree(){
        val f = true
        var v: Pair<Int,Int>
        while(f) {
            var k = true
            v = Pair(Random.nextInt(0, 9), Random.nextInt(0, 9))
            for (i in -1..1) {
                for (j in -1..1) {
                    if (v.first + i >= 0 && v.first + i < 10 && v.second + j >= 0 && v.second + j < 10) {
                        if (cells[getCellId(v.first + i, v.second + j)].status == Status.SHIP) {
                            k = false
                        }
                    }
                }
            }
            if(k){
                for(i in 0..3) {
                    when (i) {
                        0->{
                            for(u in 1..2){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i + u >= 0 && v.first + i + u < 10 && v.second + j >= 0 && v.second + j < 10) {
                                            if (i != -1 || j != 0) {
                                                if (cells[getCellId(
                                                        v.first + i + u,
                                                        v.second + j
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+1,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+2,v.second)].status = Status.SHIP
                                return
                            }
                        }
                        1->{
                            for(u in 1..2){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i - u >= 0 && v.first + i - u < 10 && v.second + j >= 0 && v.second + j < 10) {
                                            if(i != 1 || j != 0) {
                                                if (cells[getCellId(
                                                        v.first + i - u,
                                                        v.second + j
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-1,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-2,v.second)].status = Status.SHIP
                                return
                            }
                        }
                        2->{
                            for(u in 1..2){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i  >= 0 && v.first + i < 10 && v.second + j + u >= 0 && v.second + j + u < 10) {
                                            if(i != 0 || j != -1) {
                                                if (cells[getCellId(
                                                        v.first + i,
                                                        v.second + j + u
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+1)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+2)].status = Status.SHIP
                                return
                            }
                        }
                        3->{
                            for(u in 1..2){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i  >= 0 && v.first + i < 10 && v.second + j - u >= 0 && v.second + j - u < 10 ) {
                                            if(i != 0 || j != 1) {
                                                if (cells[getCellId(
                                                        v.first + i,
                                                        v.second + j - u
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-1)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-2)].status = Status.SHIP
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    private fun arranFour(){
        val f = true
        var v: Pair<Int,Int>
        while(f) {
            var k = true
            v = Pair(Random.nextInt(0, 9), Random.nextInt(0, 9))
            for (i in -1..1) {
                for (j in -1..1) {
                    if (v.first + i >= 0 && v.first + i < 10 && v.second + j >= 0 && v.second + j < 10) {
                        if (cells[getCellId(v.first + i, v.second + j)].status == Status.SHIP) {
                            k = false
                        }
                    }
                }
            }
            if(k){
                for(i in 0..3){
                    when(i){
                        0->{
                            for(u in 1..3){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i + u >= 0 && v.first + i + u < 10 && v.second + j >= 0 && v.second + j < 10) {
                                            if(i != -1 || j != 0) {
                                                if (cells[getCellId(
                                                        v.first + i + u,
                                                        v.second + j
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+1,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+2,v.second)].status = Status.SHIP
                                cells[getCellId(v.first+3,v.second)].status = Status.SHIP
                                return
                            }
                        }
                        1->{
                            for(u in 1..3){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i - u >= 0 && v.first + i - u < 10 && v.second + j >= 0 && v.second + j < 10) {
                                            if(i != 1 || j != 0) {
                                                if (cells[getCellId(
                                                        v.first + i - u,
                                                        v.second + j
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-1,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-2,v.second)].status = Status.SHIP
                                cells[getCellId(v.first-3,v.second)].status = Status.SHIP
                                return
                            }
                        }
                        2->{
                            for(u in 1..3){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i  >= 0 && v.first + i < 10 && v.second + j + u >= 0 && v.second + j + u < 10) {
                                            if((i != 0 || j != -1)) {
                                                if (cells[getCellId(
                                                        v.first + i,
                                                        v.second + j + u
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+1)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+2)].status = Status.SHIP
                                cells[getCellId(v.first,v.second+3)].status = Status.SHIP
                                return
                            }
                        }
                        3->{
                            for(u in 1..3){
                                for (i in -1..1) {
                                    for (j in -1..1) {
                                        if (v.first + i  >= 0 && v.first + i < 10 && v.second + j - u >= 0 && v.second + j - u < 10) {
                                            if(i != 0 || j != 1) {
                                                if (cells[getCellId(
                                                        v.first + i,
                                                        v.second + j - u
                                                    )].status == Status.SHIP
                                                ) {
                                                    k = false
                                                }
                                            }
                                        }else{
                                            k = false
                                        }
                                    }
                                }
                            }
                            if(k){
                                cells[getCellId(v.first,v.second)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-1)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-2)].status = Status.SHIP
                                cells[getCellId(v.first,v.second-3)].status = Status.SHIP
                                return
                            }
                        }
                    }
                }
            }
        }
    }
}