package ru.max.windows.components

import ru.max.GameData
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.Integer.min
import javax.swing.JPanel
import kotlin.math.max

class GameCell(val row: Int, val col: Int, val side: Boolean): JPanel(){

    companion object{
        var fieldWidth: Int = 0
        var fieldHeight: Int = 0
        val ROW_COUNT = 10
        val COL_COUNT = 10
        val FIREGROUND = Color.GRAY
    }


    val size: Pair<Int,Int>
    get()= Pair(fieldWidth / COL_COUNT-2, fieldHeight / ROW_COUNT-2)

    private val gmd = GameData.getInstance()

    val xShift: Int
        get() = size.first*col
    val yShift: Int
        get() = size.second*row

    var status = Status.NONE
        set(value){
            field = value
            repaint()
        }

    var isFired = false
        set(value){
            field = value
        }// обстреляли ли ее

    init{
        addMouseListener(object :MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(gmd.clickable_op && !side){
                    if(!isFired){
                        gmd.lastSetPosition = Pair(row,col)
                    }
                }
                else{
                    if(gmd.clickable && side){
                        when(status){
                            Status.SEA -> status = Status.SHIP
                            Status.SHIP -> status = Status.SEA
                        }
                    }
                }
            }
        })
    }

    fun updateBounds(){
        setBounds(xShift,yShift,size.first+2,size.second+2)
    }

    private fun draw(g: Graphics){
        if(fieldHeight == 0 || fieldWidth == 0) return
            g.color = status.color
            g.fillRect(1,1,size.first-1,size.second-1)
    }

    private fun drawcrash(g:Graphics){
        if(status == Status.SEA){
            g.color = FIREGROUND
            g.fillOval(size.first/2-size.first/4,size.first/2-size.first/4,size.first/2,size.second/2)
        }
        if (status == Status.SHIP){
            g.color = Color.RED
            g.drawLine(1,1,size.first,size.second)
            g.drawLine(1,size.second,size.first,1)
        }
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        draw(g)
        if(isFired){
            drawcrash(g)
        }
    }

    fun clear(){
        if(!side) {
            status = Status.NONE
            isFired = false
        }else{
            status = Status.SEA
            isFired = false
        }
    }
}

enum class Status(var color: Color){
    SHIP(Color.DARK_GRAY),
    SEA(Color.CYAN),
    NONE(GameCell.FIREGROUND)
}