package ru.delivery.system.styles

import tornadofx.*

class MyStyle: Stylesheet() {

    companion object {
        val customButtonStyle by cssclass()

        private val topColor = c("#999999")
    }

    init {
        customButtonStyle {
            rotate = 10.deg
            borderColor += box(topColor, topColor)
        }
    }
}