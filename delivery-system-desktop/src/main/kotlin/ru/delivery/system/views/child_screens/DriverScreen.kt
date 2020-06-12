package ru.delivery.system.views.child_screens

import javafx.collections.FXCollections
import javafx.scene.Node
import ru.delivery.system.views.common.ChildScreenFooter
import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*

/**
 * Screen with driver information and statistics
 */
class DriverScreen : View("Driver info") {
    override val root = borderpane {
        top(ChildScreenHeader::class)
        center(DriverViewMain::class)
        bottom(ChildScreenFooter::class)
    }
}

class DriverViewMain : View() {

    override val root = borderpane {
        center {
        }
    }
}