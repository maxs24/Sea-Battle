package ru.max.windows

import ru.max.GameData
import ru.max.Order
import java.awt.Dimension
import javax.swing.*
import ru.max.windows.components.GameField
import ru.max.networking.Client
import ru.max.networking.ClientUI
import ru.max.networking.GameDataUI
import ru.max.networking.UI

class MainWindow: JFrame() {
    val field: GameField
    val field_op: GameField
    val btnStartUI: JButton
    val btnStart: JButton
    val statusBar : JTextField
    var countships: JLabel
    var client: Client? = null
    var clientui: ClientUI? = null
    val gameData = GameData.getInstance()
    val gameDataUI = GameDataUI.getInstance()
    var isstart = false

    init{
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(650,500)
        statusBar = JTextField("",45)
        statusBar.isEnabled = false
        field = GameField(true,gameData)
        field_op = GameField(false,gameData)
        btnStart = JButton("Начать с игроком")
        btnStart.addActionListener {
            if(!isstart) {
                client = Client()
                statusBar.text = "Ожидание противника"
            }
            else{
                val mas = field.countShips()
                if(mas[0] == 4 && mas[1] == 3 && mas[2] == 2 && mas[3] == 1){
                    gameData.isreadyset= true
                    gameData.clickable = false
                    val dg = Dialog("Ожидайте противника.")
                    dg.isVisible = true
                    btnStart.isEnabled = false
                }else{
                    val dg = Dialog("Не правильная расстановка.")
                    dg.isVisible = true
                }
            }
        }
        btnStartUI = JButton("Начать с компьютером")
        btnStartUI.addActionListener {
            if(!isstart){
                client = Client()
                val ui = UI(gameDataUI)
                clientui = ClientUI()
            }else{
                gameData.clear()
                field.clear()
                field_op.clear()
            }
        }
        countships = JLabel("4-х палубных - 1   3-х палубных - 2   2-х палубных - 3   1 палубных - 4")

        gameData.addGotPositionListener(::onExternalClick)
        gameData.addSetPositionListener(::onInnerClick)
        gameData.addGotStListener(::onStart)
        gameData.addGotReadyListener(::onReady)
        gameData.addWinListener(::win)
        gameData.addWinOPListener(::lose)

        val gl = GroupLayout(contentPane)
        layout = gl
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGap(5)
            .addGroup(gl.createParallelGroup()
            .addComponent(statusBar,400,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
                .addGroup(gl.createSequentialGroup()
                    .addGap(5)
                    .addComponent(field,300, GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
                    .addGap(5)
                    .addComponent(field_op,300,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE))
            .addGroup(gl.createSequentialGroup()
                .addGap(5)
                .addComponent(btnStart,100,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
                .addGap(5,5, Int.MAX_VALUE)
                .addComponent(countships,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                .addGap(5,5, Int.MAX_VALUE)
                .addComponent(btnStartUI,100,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
                .addGap(5)))
            .addGap(5))

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(5)
            .addComponent(statusBar,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            .addGap(5)
            .addGroup(gl.createParallelGroup()
                .addComponent(field,300,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE)
                .addComponent(field_op,300,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE))
            .addGap(5)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(btnStart,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(countships,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                .addComponent(btnStartUI,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
            .addGap(5))

        pack()
    }

    private fun onExternalClick(row: Int, col: Int){
        val str = StringBuilder()
        str.append("Ход противника")
        str.append(" ")
        str.append(row)
        str.append(";")
        str.append(col)
        statusBar.text = str.toString()
    }

    private fun onInnerClick(row: Int, col: Int){
        val str = StringBuilder()
        str.append("Ваш ход")
        str.append(" ")
        str.append(row)
        str.append(";")
        str.append(col)
        statusBar.text = str.toString()
    }

    private fun onStart(or: Order){
        when(or){
            Order.SECOND -> {
                statusBar.text = "Противник раставляется первым."
            }
            Order.FIRST ->{
                statusBar.text = "Вы расставляетесь первым."
            }
            Order.NONE -> {}
        }
        isstart = true
        btnStart.text = "Принять"
        btnStartUI.text = "Выйти"
        btnStartUI.isEnabled = false
    }

    private fun onReady(){
        if(gameData.isreadyset == true){
            statusBar.text = "Ваш ход."
        }
        else{
            statusBar.text = "Ваша очередь расставляться."
        }
    }

    private fun win(){
        val dg = Dialog("Вы выйграли")
        dg.isVisible = true
        btnStartUI.isEnabled = true
    }

    private fun lose(){
        val dg = Dialog("Вы проиграли")
        dg.isVisible = true
        btnStartUI.isEnabled = true
    }
 }