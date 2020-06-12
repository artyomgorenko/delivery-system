package ru.delivery.system.views

import de.saring.leafletmap.*
import io.github.rybalkinsd.kohttp.ext.httpGet
import javafx.application.Platform
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import okhttp3.Response
import ru.delivery.system.common.ColorConstatnts
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.common.navigateToLeft
import ru.delivery.system.controllers.LoginController
import ru.delivery.system.controllers.OrderListController
import ru.delivery.system.executors.ScheduledMapUpdater
import ru.delivery.system.httpcontrollers.MapUpdater
import ru.delivery.system.models.json.OrderRoute
import ru.delivery.system.views.MapView.Map.mapView
import ru.delivery.system.views.child_screens.DriverScreen
import ru.delivery.system.views.child_screens.OrderScreen
import ru.delivery.system.views.child_screens.OrderCreateScreen
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
    private val loginController: LoginController by inject()
    private var mapUpdater: MapUpdater

    object Map {
        val mapView = LeafletMapView()
    }

    init {
        ScheduledMapUpdater.runScheduling()
        mapUpdater = MapUpdater()
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
                            scaleControlConfig = ScaleControlConfig(true, ControlPosition.BOTTOM_LEFT, metric = true),
                            initialCenter = LatLong(45.030754, 39.066941)
                        )
                    )
                }
            }

            bottom {
                hbox {
                    button("Создать заказ").action { find<OrderCreateScreen>().openModal() }
                    button("Add marker").action { mapView.addMarker(LatLong(51.1, -0.2), "TEST MARKER", ColorMarker.BLACK_MARKER, 0) }
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
                }
            }
        }
    }
}

/**
 * Список заказов
 */
class OrderListView : View() {

    private val orderListController = OrderListController
    private val openedOrders: ArrayList<Int> = arrayListOf()

    override val root = borderpane {
        top {

            form {
                fieldset {
                    minWidth = 250.0
                    maxWidth = 250.0
                    field {
                        val textField = textfield()
                        textField.promptText = "Номер заказа"
                        button("Поиск").action { orderListController.findById(textField.text) }
                    }
                    field {
                        checkbox("Done") { isSelected = true }
                        checkbox("In progress") { isSelected = true }
                        checkbox("Completed") { isSelected = true }
                        checkbox("Canceled") { isSelected = true }
                    }
                }
            }
        }

        left {
            scrollpane {
                vbox {
                    children.bind(orderListController.displayedValues) { order ->
                        titledpane("Order# ${order.orderId}") {

                            expandedProperty().addListener { _, _, new ->
                                if (!new) {
                                    openedOrders.remove(order.orderId!!)
                                } else {
                                    openedOrders.add(order.orderId!!)
                                }
                                println(openedOrders.toString())
                            }

                            isExpanded = openedOrders.findLast { it == order.orderId } != null
                            form {
                                style {
                                    when (order.status) {
                                        "IN_PROGRESS" -> backgroundColor += ColorConstatnts.ORDER_IN_PROGRESS
                                        "DONE" -> backgroundColor += ColorConstatnts.ORDER_DONE
                                        "CANCELED" -> backgroundColor += ColorConstatnts.ORDER_CANCELED
                                        "NEW" -> backgroundColor += ColorConstatnts.ORDER_NEW
                                    }
                                }

                                fieldset {
                                    minWidth = 250.0
                                    maxWidth = 250.0

                                    field(order.orderId.toString())
                                    field("Products count: ${order.productList?.size}") {
                                        button("->") {
                                            tooltip("Shows order info")
                                            action { navigateToLeft<OrderScreen>() }
                                        }
                                    }
                                    field("Driver id: ${order.driverId}") {
                                        button("->") {
                                            tooltip("Shows order info")
                                            action { navigateToLeft<DriverScreen>() }
                                        }
                                    }
                                    field("Status: ${order.status}")
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