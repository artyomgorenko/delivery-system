package ru.delivery.system.views.child_screens

import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.View
import tornadofx.borderpane
import tornadofx.center
import tornadofx.label

class OrderScreen : View("Order info") {
    override val root = borderpane {
        top(ChildScreenHeader::class)
        center {
            label("Order screen")
        }
    }
}