package ru.delivery.system.views

import javafx.stage.Stage
import ru.delivery.system.executors.ScheduledMapUpdater
import ru.delivery.system.styles.MyStyle
import tornadofx.*

class MapApp : App(MapView::class, MyStyle::class) {
    private val scheduledMapUpdater = ScheduledMapUpdater

    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.width = 1124.0
        stage.height = 786.0
        super.start(stage)
    }

    override fun stop() {
        scheduledMapUpdater.stopScheduling()
        super.stop()
    }
}

fun main(args: Array<String>) = launch<MapApp>(args)