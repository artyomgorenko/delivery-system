package ru.delivery.system.views.child_screens

import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*

class ReportScreen : View("Reports") {

    override val root = borderpane {
        top(ChildScreenHeader::class)
        center {
            piechart("Desktop/Laptop OS Market Share") {
                data("Windows", 77.62)
                data("OS X", 9.52)
                data("Other", 3.06)
                data("Linux", 1.55)
                data("Chrome OS", 0.55)
            }
        }
    }
}
