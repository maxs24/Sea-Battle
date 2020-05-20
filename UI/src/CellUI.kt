package ru.max.networking

import ru.max.windows.components.Status
import java.awt.Color

class CellUI(val row: Int, val col: Int){

    companion object{
        var fieldWidth: Int = 0
        var fieldHeight: Int = 0
        val ROW_COUNT = 10
        val COL_COUNT = 10
        val FIREGROUND = Color.GRAY
    }


    var status = Status.NONE
        set(value){
            field = value
        }

    var isFired = false
        set(value){
            field = value
        }
}