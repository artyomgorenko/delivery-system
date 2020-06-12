package ru.delivery.system

import ru.delivery.system.views.LoginScreen
import tornadofx.App
import tornadofx.launch

class DeliverySystemApp : App(LoginScreen::class)
fun main() = launch<DeliverySystemApp>()