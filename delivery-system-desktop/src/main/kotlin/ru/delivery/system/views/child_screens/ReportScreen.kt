package ru.delivery.system.views.child_screens

import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*

class ReportScreen : View("Reports") {
    override val root = borderpane {
        top(ChildScreenHeader::class)
        center {
            label("Reports")
        }
    }
}
