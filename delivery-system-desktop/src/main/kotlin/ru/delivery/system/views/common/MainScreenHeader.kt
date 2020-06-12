package ru.delivery.system.views.common

import javafx.beans.property.SimpleObjectProperty
import ru.delivery.system.common.ColorConstatnts
import ru.delivery.system.common.navigateToLeft
import ru.delivery.system.controllers.LoginController
import ru.delivery.system.models.viewmodels.UserModel
import ru.delivery.system.views.child_screens.DriverScreen
import ru.delivery.system.views.child_screens.OrderScreen
import ru.delivery.system.views.child_screens.ReportScreen
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class MainScreenHeader : View() {

    val user: UserModel by inject()
    val label = label("Logged as ${user.name.value}")
    val userName = stringBinding(user.name) { user.name.value }
    val loginController: LoginController by inject()

    override val root = borderpane {
        top {
            style {
                backgroundColor += ColorConstatnts.MAIN_APP_COLOR
            }

            left {
                hbox {
                    button("Driver screen") { action { navigateToLeft<DriverScreen>() } }
                    button("Order screen") { action { navigateToLeft<OrderScreen>() } }
                    button("Report screen") { action { navigateToLeft<ReportScreen>() } }
                }
            }
            right {
                hbox {
                    label("Logged as:") {
                        userName.onChange { text = "Logged as: $it"}
                    }
                    button("Log out").action(loginController::logout)
                }
            }
        }
    }

    override fun onDock() {
        label.text = "Logged as ${user.name.value}"
    }
}
