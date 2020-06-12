package ru.delivery.system.views.common

import ru.delivery.system.common.navigateToLeft
import ru.delivery.system.models.viewmodels.UserModel
import ru.delivery.system.views.child_screens.DriverScreen
import ru.delivery.system.views.child_screens.OrderScreen
import ru.delivery.system.views.child_screens.ReportScreen
import tornadofx.*

class MainScreenHeader : View() {

    val user: UserModel by inject()
    val label = label("Logged as ${user.name.value}")

    override val root = borderpane {
        top {
            left {
                hbox {
                    button("Driver screen") { action { navigateToLeft<DriverScreen>() } }
                    button("Order screen") { action { navigateToLeft<OrderScreen>() } }
                    button("Report screen") { action { navigateToLeft<ReportScreen>() } }
                }
            }
            right {
                label
            }
        }
    }

    override fun onDock() {
        label.text = "Logged as ${user.name.value}"
    }
}
