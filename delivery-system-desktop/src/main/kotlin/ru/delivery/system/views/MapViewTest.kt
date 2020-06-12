package ru.delivery.system.views

import de.saring.leafletmap.*
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import ru.delivery.system.styles.MyStyle
import tornadofx.*

class MapViewTest : View() {
    override val root = BorderPane()
    private val mapView = LeafletMapView()

    init {
        with (root) {
            left {
                vbox {
                    button("Show order") {
                        action { println("Handle show button") }
                        addClass(MyStyle.customButtonStyle)
                    }
                }
            }

            center {
                stackpane {
                    children.add(mapView)
                    mapView.displayMap(MapConfig(
                        layers = listOf(MapLayer.OPENSTREETMAP, MapLayer.HIKE_BIKE_MAP),
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
                    button("AddMarker") { action { mapView.addMarker(LatLong(51.1, -0.2), "TEST MARKER", ColorMarker.BLACK_MARKER, 0) } }
                    button("B_Button 2") { action { println("Handle B_button 2") } }
                    button("B_Button 3") { action { println("Handle B_button 3") } }
                }
            }
        }
    }
}

class MapApp : App(MapViewTest::class, MyStyle::class) {

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