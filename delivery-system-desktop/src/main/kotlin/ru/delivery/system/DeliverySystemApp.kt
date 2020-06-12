package ru.delivery.system

import ru.delivery.system.views.LoginScreen
import tornadofx.App
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus

class DeliverySystemApp : App(LoginScreen::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}
fun main() = launch<DeliverySystemApp>()