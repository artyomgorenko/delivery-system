package ru.delivery.system.controllers

import javafx.beans.property.SimpleStringProperty
import ru.delivery.system.common.navigateTo
import ru.delivery.system.models.viewmodels.UserModel
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.LoginScreen
import ru.delivery.system.views.MainScreen
import tornadofx.*

class LoginController : Controller() {
    val statusProperty = SimpleStringProperty()
    var status by statusProperty
    val user: UserModel by inject()
    private val httpHelper = HttpHelper


    fun login(username: String?, password: String?) {
        runLater {
            user.name.value = username
            navigateTo<MainScreen>()
        }

        // TODO: uncomment with 1.0 release
//        runLater {
//            var response : Response? = null
//            status = ""
//
//            try {
//                val loginRequest = UserLoginRequest(username, password)
//                response = httpHelper.postStringJson("/delivery-system/user/login", JsonSerializer().toJson(loginRequest))
//            } catch (e: Exception) {
//                status = "Cant't get response"
//                println("Login error. ${e.message}")
//            }
//
//            try {
//                response?.use {
//                    if (response.isSuccessful) {
//                        val loginResponse: UserLoginResponse = JsonSerializer().toEntity(response.body()!!.string())
//                        if (loginResponse.errorMessage == null) {
//                            find(LoginScreen::class).replaceWith(MapView::class, sizeToScene = true, centerOnScreen = true)
//                        } else {
//                            status = loginResponse.errorMessage ?: "Login failed"
//                        }
//                    } else {
//                        status = "Login failed"
//                    }
//                }
//            } catch (e: Exception) {
//                status = "Login error"
//                println("Login error. ${e.message}")
//            }
//        }
    }

    fun logout() {
        user.item = null
        navigateTo<LoginScreen>()
    }
}
