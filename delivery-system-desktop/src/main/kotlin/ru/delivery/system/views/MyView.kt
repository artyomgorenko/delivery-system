package ru.delivery.system.views

import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import tornadofx.*

val WIDTH = 1024.0
val HEIGHT = 786.0

class MyView : View() {
    val colNum = 10

    override val root = group {
        gridpane {
            vgap = 15.0
            padding = insets(15)
            for (i in 1..colNum) {
                add(horizontalPane())
            }
        }
    }

    fun horizontalPane() : StackPane {
        return stackpane {
            rectangle {
                fill = c("4E9830")
                width = 100.0
                height = 100.0
            }
            rectangle {
                fill = c("919191")
                width = 100.0
                height = 40.0
            }
        }
    }
}

class MyApp : App(MyView::class) {
    override fun start(stage: Stage) {
        stage.height = HEIGHT
        stage.width = WIDTH
        super.start(stage)
    }

    override fun createPrimaryScene(view: UIComponent): Scene = super.createPrimaryScene(view).apply {
        fill  = Color.valueOf("#EDEDED")
    }
}

fun main(args : Array<String>) {
    launch<MyApp>(args)
}