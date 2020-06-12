package ru.delivery.system.views

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import ru.delivery.system.controllers.LoginController
import tornadofx.*

class LoginScreen : View("Login") {

    private val model = ViewModel()
    private val username = model.bind { SimpleStringProperty() }
    private val password = model.bind { SimpleStringProperty() }
    private val loginController: LoginController by inject()

    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            fieldset("Username") {
                textfield(username).required()
            }
            fieldset("Password") {
                textfield(password).required()
            }
            button("Login") {
                enableWhen(model.valid)
                isDefaultButton = true
                useMaxWidth = true
                action {
                    runAsyncWithProgress {
                        loginController.login(username.value, password.value)
                    }
                }
            }
        }
        label(loginController.statusProperty) {
            style {
                paddingTop = 10
                textFill = Color.RED
                fontWeight = FontWeight.BOLD
            }
        }
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        model.clearDecorators()
    }
}