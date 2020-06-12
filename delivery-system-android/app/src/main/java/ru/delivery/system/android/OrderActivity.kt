package ru.delivery.system.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.databinding.DataBindingUtil
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_order.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.delivery.system.android.controllers.OrderController
import ru.delivery.system.android.controllers.OrderScheduler
import ru.delivery.system.android.controllers.OsmMapController
import ru.delivery.system.android.databinding.ActivityOrderBinding
import ru.delivery.system.android.models.OrderModel
import ru.delivery.system.android.models.json.OrderStatusRequest
import ru.delivery.system.android.models.json.OrderStatusResponse
import ru.delivery.system.android.utils.HttpHelper
import ru.delivery.system.android.utils.JsonSerializer
import ru.delivery.system.android.utils.snackbar
import ru.delivery.system.android.utils.toast
import java.io.IOException


/**
 * Activity для отобрадения подробной информации по одному заказу
 */
class OrderActivity : AppCompatActivity() {
    companion object {
        var orderId = ""
    }
    private lateinit var mapView: MapView
    private val orderController = OrderController
    private val httpHelpler = HttpHelper
    private val orderModel = OrderModel
    private lateinit var mapController: OsmMapController

    lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        binding.setVariable(BR.order, orderModel.orderData)
        binding.executePendingBindings()

        getInWorkButton.setOnClickListener { view ->
            getOrderInWork()
        }

        checkPermissionsState()
        setupMap()
        recreateMapView()

        var departmentPoint = GeoPoint(45.0339, 39.0978)
        var destinationPoint = GeoPoint(45.05, 39.0998)
        OrderModel.orderData?.let {
            departmentPoint = OrderModel.orderData!!.departmentPoint
            destinationPoint = OrderModel.orderData!!.destinationPoint
        }
        mapController = OsmMapController(mapView)
        mapController.addWarehouseMarker(departmentPoint)
        mapController.addEndpointMarker(destinationPoint)
        mapView.controller.setCenter(destinationPoint)
    }

    private fun recreateMapView() {
        if (mapView.context == null || mapView.context.resources == null) {
            finish()
            overridePendingTransition(0, 0) // @HINT: hides blink after recreate method
            startActivity(intent)
            overridePendingTransition(0, 0)
            Log.i("OrderActivity", "MapView recreated to avoid NPE")
        }
    }

    private fun checkPermissionsState() {
        val internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        val networkStatePermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        val writeExternalStoragePermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val coarseLocationPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocationPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)

        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
            writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED &&
            coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
            wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED
        ) {
//            setupMap()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                4
            )
        }
    }

    private fun setupMap() {
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)
        mapView.isClickable = true
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
        mapView.controller.setZoom(15.0)
//        mapView.setDestroyMode(false)????!
//        setContentView(mapView); //displaying the MapView

//        mapView.getController().setCenter(ONCATIVO);
        //mapView.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.
        mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP)

        val oMapLocationOverlay = MyLocationNewOverlay(mapView) // ?????????
        mapView.overlays.add(oMapLocationOverlay)
        oMapLocationOverlay.enableFollowLocation()
        oMapLocationOverlay.enableMyLocation()
        oMapLocationOverlay.enableFollowLocation()


//        val compassOverlay = CompassOverlay(this, mapView)
//        compassOverlay.enableCompass()
//        mapView.overlays.add(compassOverlay)

        mapView.addMapListener(DelayedMapListener(object : MapListener {
            override fun onZoom(e: ZoomEvent): Boolean {
                val mapView = findViewById<MapView>(R.id.map)

                val latitudeStr = "" + mapView.mapCenter.latitude
                val longitudeStr = "" + mapView.mapCenter.longitude

                val latitudeFormattedStr = latitudeStr.substring(0, Math.min(latitudeStr.length, 7))
                val longitudeFormattedStr = longitudeStr.substring(0, Math.min(longitudeStr.length, 7))

                Log.i("zoom", "" + mapView.mapCenter.latitude + ", " + mapView.mapCenter.longitude)
//                val latLongTv = findViewById<TextView>(R.id.textView)
//                latLongTv.text = "$latitudeFormattedStr, $longitudeFormattedStr"
                return true
            }

            override fun onScroll(e: ScrollEvent): Boolean {
                val mapView = findViewById<MapView>(R.id.map)

                val latitudeStr = "" + mapView.mapCenter.latitude
                val longitudeStr = "" + mapView.mapCenter.longitude

                val latitudeFormattedStr = latitudeStr.substring(0, Math.min(latitudeStr.length, 7))
                val longitudeFormattedStr = longitudeStr.substring(0, Math.min(longitudeStr.length, 7))

                Log.i("scroll", "" + mapView.mapCenter.latitude + ", " + mapView.mapCenter.longitude)
//                val latLongTv = findViewById<TextView>(R.id.textView)
//                latLongTv.text = "$latitudeFormattedStr, $longitudeFormattedStr"
                return true
            }
        }, 1000))
    }

    private fun getOrderInWork() {
        getOrderInWork(orderModel.orderData!!.id)
    }

    fun getOrderInWork(orderId: Int) {
        val context = this
        changeOrderStatus(orderId, "PRODUCT_PICKING", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(context, "Request failed. ${e.message}", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {
                            if (OrderScheduler.currentOrderId == null) {
                                OrderScheduler.startOrderTracking(orderId)
                                runOnUiThread { toast(context, "Заказ взят в работу") }
                                val intent = Intent(context, MapActivity::class.java)
                                startActivity(intent)
                            } else {
                                runOnUiThread { toast(context, "Заказ c id=${OrderScheduler.currentOrderId} еще не завершен") }
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(context, "Заказ не может быть взят в работу:\n" + "${orderStatusResponse.header.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
    }

    fun cancelOrder(orderId: Int) {
        return changeOrderStatus(orderId, "CANCEL", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }

    private fun changeOrderStatus(orderId: Int, newStatus: String, callback: Callback) {
        val requestEntity = OrderStatusRequest().apply {
            body.orderId = orderId
            body.newStatus = newStatus
            body.driverId = 1
            body.transportId = 1
        }
        val json = JsonSerializer().toJson(requestEntity)
        HttpHelper.post("order/changeOrderStatus", json, callback)
    }

}
