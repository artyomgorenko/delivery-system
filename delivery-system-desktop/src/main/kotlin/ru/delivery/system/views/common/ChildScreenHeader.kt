package ru.delivery.system.views.common

import ru.delivery.system.common.navigateToRight
import ru.delivery.system.views.MainScreen
import ru.delivery.system.views.MapView
import tornadofx.*

class ChildScreenHeader : Fragment() {
    override val root = borderpane {
        top {
            hbox {
                button("<<--").action { navigateToRight<MainScreen>() }
            }
        }
    }
}
