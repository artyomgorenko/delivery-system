package ru.delivery.system.views

import de.saring.leafletmap.*
import io.github.rybalkinsd.kohttp.ext.httpGet
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.controllers.LoginController
import ru.delivery.system.models.json.OrderRoute
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.styles.MyStyle
import tornadofx.*

class MapView : View() {
    override val root = BorderPane()
    private val mapView = LeafletMapView()
    private val httpHelper = HttpHelper("localhost", 8080)
    private val loginController: LoginController by inject()

    init {
        with (root) {
            left {
                vbox {
                    button("Show order") {
                        action { println("Handle show button") }
                        // addClass(MyStyle.customButtonStyle) // TODO: solve css conflict
                    }
                }
            }

            center {
                stackpane {
                    children.add(mapView)
                    mapView.displayMap(MapConfig(
                        layers = listOf(MapLayer.HIKE_BIKE_MAP, MapLayer.OPENSTREETMAP),
                        zoomControlConfig = ZoomControlConfig(true, ControlPosition.BOTTOM_LEFT),
                        scaleControlConfig = ScaleControlConfig(true, ControlPosition.BOTTOM_LEFT, metric = true)
                    ))
                }
            }

            top {
                hbox {
                    button("T_Button 1") { action { println("Handle T_button 1") } }
                    button("T_Button 2") { action { println("Handle T_button 2") } }
                    button("T_Button 3") { action { println("Handle T_button 3") } }
                }
            }

            bottom {
                hbox {
                    button("Add marker") { action { mapView.addMarker(LatLong(51.1, -0.2), "TEST MARKER", ColorMarker.BLACK_MARKER, 0) } }
                    button("Draw order route") {
                        style {
                            baseColor = Color.RED
                        }
                        var trackName: String? = null
                        action {
                            val response: Response = "http://localhost:8080/delivery-system/order/getOrderRoute?orderId=4".httpGet()
                            if (response.isSuccessful) {
                                response.body()?.string()?.let { json ->
                                    mapView.clearMarkersAndTracks()
                                    val orderRoute = JsonSerializer().toEntity<OrderRoute>(json)
                                    val points = ArrayList<LatLong>()
                                    orderRoute.routePoints?.forEach { point ->
                                        val latLong = LatLong(point.geoPoint!!.latitude!!.toDouble(), point.geoPoint!!.longitude!!.toDouble())
                                        points.add(latLong)
                                    }
                                    trackName = mapView.addTrackWithMarkers(points)
                                }
                            }
                        }
                    }
                    button("Log out").action(loginController::logout)
                }
            }
        }
    }
}

class MapApp : App(MapView::class, MyStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.width = 600.0
        stage.height = 400.0
        super.start(stage)
    }
}

fun main(args: Array<String>) = launch<MapApp>(args)