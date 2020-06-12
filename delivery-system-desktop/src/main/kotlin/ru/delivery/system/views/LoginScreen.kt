package ru.delivery.system.views

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import ru.delivery.system.controllers.LoginController
import tornadofx.*

class LoginScreen : View("Login") {

    private val model = ViewModel()
    private val username = model.bind { SimpleStringProperty() }
    private val password = model.bind { SimpleStringProperty() }
    private val loginController: LoginController by inject()

    override val root = BorderPane()

    init {
        primaryStage.width = 640.0
        primaryStage.height = 480.0

        with(root) {
            center {
                form {
                    alignmentProperty().set(Pos.BASELINE_CENTER)
                    fieldset(labelPosition = Orientation.VERTICAL) {
                        maxWidthProperty().set(200.0)
                        field("Username") {
                            textfield(username).required(ValidationTrigger.OnChange(), "Enter your username")
                        }
                        field("Password") {
                            textfield(password).required(ValidationTrigger.OnChange(), "Enter your  password")
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
                            style {
                                baseColor = Color.GREEN
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
                }
            }
        }
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        model.clearDecorators()
    }

}