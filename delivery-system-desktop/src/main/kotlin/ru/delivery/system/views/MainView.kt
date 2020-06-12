package ru.delivery.system.views

import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.MapMarker
import ru.delivery.system.rest.HttpHelper
import tornadofx.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainView : View() {

    private val httpHelper = HttpHelper("localhost", 8080)
    private val jsonSerializer = JsonSerializer()

    override val root = BorderPane()

    init {

        with (root) {

            title = "MainView"
            left {
                form {
                    fieldset {
                        var field1 : TextField? = null
                        var field2 : TextField? = null

                        field("Username") {
                            field1 = textfield().apply { }
                        }

                        field("Password") {
                            field2 = textfield {
//                                validator {
//                                    if (it.isNullOrBlank()) error("Password required") else null
//                                }
                            }
                        }

                        button("Try to connect") {
                            action {
                                sendGet("http://localhost:8080/delivery-system/echo/settings")
                            }
                        }

                        button("change view") {
                            action { replaceWith<MyView>() }
                        }
                    }
                }
            }

            center {
                button("Create marker") {
                    action {
                        val mapMarker = MapMarker(10.1, 20.1)
                        val response: Response = httpHelper.postStringJson("/delivery-system/map/addMarker", jsonSerializer.toJson(mapMarker))

                        if (response.code() == 200) {
                            println(response.body().toString())
                        } else {
                            println("Post request failed with code ${response.code()}")
                            println("${response.request().url()}")
                        }
                    }
                }

                button("Get all markers") {
                    action {
                        val mapMarkers : List<MapMarker>
                        httpHelper.getStringJson("/delivery-system/map/getMarkers")
                    }
                }
            }
        }
    }

    private fun sendGet(url: String) {
        val obj = URL(url)
        with (obj.openConnection() as HttpURLConnection) {
            // optional default is GET requestMethod = "GET"
            println("\nSending 'GET' request to URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    inputLine = it.readLine()
                    response.append(inputLine)
                }
                println("Response:$response")
            }
        }
    }
}

class TornadoApp : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.title = "Title"
        stage.height = 400.0
        stage.width = 600.0

        super.start(stage)
    }

    override fun createPrimaryScene(view: UIComponent): Scene = super.createPrimaryScene(view).apply {
        fill  = Color.valueOf("#EDEDED")
    }
}

fun main(args : Array<String> ) {
    launch<TornadoApp>(args)
}

