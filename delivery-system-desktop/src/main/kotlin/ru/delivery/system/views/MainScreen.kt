package ru.delivery.system.views

import de.saring.leafletmap.*
import io.github.rybalkinsd.kohttp.ext.httpGet
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.common.navigateToLeft
import ru.delivery.system.controllers.LoginController
import ru.delivery.system.executors.ScheduledMapUpdater
import ru.delivery.system.models.json.OrderRoute
import ru.delivery.system.models.viewmodels.Order
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.MapView.Map.mapView
import ru.delivery.system.views.child_screens.DriverScreen
import ru.delivery.system.views.child_screens.DriverViewMain
import ru.delivery.system.views.child_screens.OrderScreen
import ru.delivery.system.views.common.MainScreenFooter
import ru.delivery.system.views.common.MainScreenHeader
import tornadofx.*

class MainScreen : View("Main screen") {
    override val root = borderpane {
        top(MainScreenHeader::class)
        center(MapView::class)
        bottom(MainScreenFooter::class)
    }
}

/**
 * Main app view with map and orders list
 */
class MapView : View() {
    override val root = BorderPane()
    private val httpHelper = HttpHelper("localhost", 8080)
    private val loginController: LoginController by inject()

    object Map {
        val mapView = LeafletMapView()
    }

    init {
        ScheduledMapUpdater.runScheduling()

        with (root) {
            top(MainScreenHeader::class)
            left(OrderListView::class)

            center {
                stackpane {
                    children.add(mapView)
                    mapView.displayMap(
                        MapConfig(
                            layers = listOf(MapLayer.HIKE_BIKE_MAP, MapLayer.OPENSTREETMAP),
                            zoomControlConfig = ZoomControlConfig(true, ControlPosition.BOTTOM_LEFT),
                            scaleControlConfig = ScaleControlConfig(true, ControlPosition.BOTTOM_LEFT, metric = true)
                        )
                    )
                }
            }

            bottom {
                hbox {
                    button("Add marker") { action { mapView.addMarker(LatLong(51.1, -0.2), "TEST MARKER", ColorMarker.BLACK_MARKER, 0) } }
                    button("Draw orderList route") {
                        style {
                            baseColor = Color.RED
                        }
                        var trackName: String? = null
                        action {
                            val response: Response = "http://localhost:8080/delivery-system/orderList/getOrderRoute?orderId=4".httpGet()
                            if (response.isSuccessful) {
                                response.body()?.string()?.let { json ->
                                    mapView.clearMarkersAndTracks()
                                    val orderRoute = JsonSerializer().toEntity<OrderRoute>(json)
                                    val points = ArrayList<LatLong>()
                                    orderRoute.routePoints?.forEach { point ->
                                        val latLong = LatLong(point.geoPoint!!.latitude!!.toDouble(), point.geoPoint!!.longitude!!.toDouble())
                                        points.add(latLong)
                                    }
                                    trackName = mapView.addTrackWithMarkers(points, TrackColor.BLUE)
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

class OrderListView : View() {

    val ordersList = FXCollections.observableArrayList<Order>()

    override val root = borderpane {
        top {
            scrollpane {
                squeezebox {
                    minWidth = 200.0
                    // TODO: Отображение релаьных заказов
                    for (i in 1..10) {
                        fold("Order #$i") {
                            form {
                                fieldset {
                                    field("Products count: 4") {
                                        button("->") {
                                            tooltip("Shows order info")
                                            action { navigateToLeft<OrderScreen>() }
                                        }
                                    }
                                    field("Driver name: Alex") {
                                        button("->") {
                                            tooltip("Shows order info")
                                            action { navigateToLeft<DriverScreen>() }
                                        }
                                    }
                                    field("Status: active")
                                    field("Total cost: 1000 р.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}