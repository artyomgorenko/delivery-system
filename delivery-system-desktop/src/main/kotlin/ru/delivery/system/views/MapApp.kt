package ru.delivery.system.views

import javafx.stage.Stage
import ru.delivery.system.styles.MyStyle
import tornadofx.*

class MapApp : App(MapView::class, MyStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.width = 640.0
        stage.height = 480.0
        super.start(stage)
    }
}

fun main(args: Array<String>) = launch<MapApp>(args)