package ru.delivery.system.views.common

import tornadofx.*

class ChildScreenFooter : View() {
    override val root = borderpane {
        top {
            label("Child screen footer")
        }
    }
}
