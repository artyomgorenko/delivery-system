package ru.delivery.system.controllers

import javafx.beans.property.SimpleStringProperty
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.UserModel
import ru.delivery.system.models.json.UserLoginRequest
import ru.delivery.system.models.json.UserLoginResponse
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.LoginScreen
import ru.delivery.system.views.MapView
import tornadofx.*

class LoginController : Controller() {
    val statusProperty = SimpleStringProperty()
    var status by statusProperty
    val user: UserModel by inject()
    private val httpHelper = HttpHelper("localhost", 8080)


    fun login(username: String?, password: String?) {
        runLater { status = "" }

        val loginRequest = UserLoginRequest(username, password)
        val response = httpHelper.postStringJson("/delivery-system/user/login", JsonSerializer().toJson(loginRequest))

        runLater {
            if (response.isSuccessful) {
                val loginResponse: UserLoginResponse = JsonSerializer().toEntity(response.body()!!.string())
                if (loginResponse.errorMessage == null) {
                    find(LoginScreen::class).replaceWith(MapView::class, sizeToScene = true, centerOnScreen = true)
                } else {
                    status = loginResponse.errorMessage ?: "Login failed"
                }
            } else {
                status = "Login failed"
            }
        }
    }

    fun logout() {
        user.item = null
        primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreen::class, sizeToScene = true, centerOnScreen = true)
    }
}
