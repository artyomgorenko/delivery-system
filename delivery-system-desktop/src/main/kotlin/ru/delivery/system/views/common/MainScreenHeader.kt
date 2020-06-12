package ru.delivery.system.views.common

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import ru.delivery.system.common.ColorConstatnts
import ru.delivery.system.common.navigateToLeft
import ru.delivery.system.controllers.LoginController
import ru.delivery.system.models.viewmodels.UserModel
import ru.delivery.system.views.child_screens.DriverScreen
import ru.delivery.system.views.child_screens.OrderScreen
import ru.delivery.system.views.child_screens.ProductScreen
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
                    button("Заказы") { action { navigateToLeft<OrderScreen>() } }
                    button("Товары") { action { navigateToLeft<ProductScreen>() } }
                    button("Пользователи") { action { navigateToLeft<DriverScreen>() } }
                    button("Транспорт") { action { navigateToLeft<DriverScreen>() } }
                    button("Отчетность") { action { navigateToLeft<ReportScreen>() } }
                }
            }
            right {
                hbox {
                    label("Авторизирован как: user123") {
                        userName.onChange { text = "Logged as: $it"}
                    }
                    button("Выйти").action(loginController::logout)
                }
            }
        }
    }

    override fun onDock() {
        label.text = "Logged as ${user.name.value}"
    }
}
