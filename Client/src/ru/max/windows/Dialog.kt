package ru.max.windows

import java.awt.Dimension
import javax.swing.*

class Dialog(val str: String) : JFrame(){
    val btnOK: JButton
    val message: JLabel

    init{
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(200,200)
        btnOK = JButton("OK")
        btnOK.addActionListener {
            this.dispose()
        }
        message = JLabel(str)
        val gl = GroupLayout(contentPane)
        layout = gl

        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(message,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            .addComponent(btnOK,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(5,5, Int.MAX_VALUE)
            .addComponent(message,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            .addGap(5,5, Int.MAX_VALUE)
            .addComponent(btnOK,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
    }
}