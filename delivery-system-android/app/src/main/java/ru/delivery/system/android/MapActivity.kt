package ru.delivery.system.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationServices
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import android.widget.TextView
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.events.MapListener
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import kotlinx.android.synthetic.main.activity_map.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.osmdroid.views.CustomZoomButtonsController
import ru.delivery.system.android.controllers.OrderScheduler
import ru.delivery.system.android.controllers.OsmMapController
import ru.delivery.system.android.models.OrderModel
import ru.delivery.system.android.models.OrderStatus.CANCELED_STATUS
import ru.delivery.system.android.models.json.OrderStatusResponse
import ru.delivery.system.android.utils.*
import java.io.IOException
import java.text.DateFormat
import java.util.*


class MapActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val TAG = "MapActivity"
    private lateinit var mapView: MapView
    private val MULTIPLE_PERMISSION_REQUEST_CODE = 4
    private var mLastLocation: Location? = null
    private lateinit var orderControlBtn: Button
    private lateinit var cancelOrderBtn: Button
    private lateinit var mapController: OsmMapController

    companion object {
        var mGoogleApiClient: GoogleApiClient? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val orderControlButton = findViewById<Button>(R.id.finishOrderButton)
        orderControlBtn = orderControlButton
        cancelOrderBtn = cancelOrderButton
        cancelOrderBtn.isEnabled = false
        orderControlButton.tag = 0
        title = "Получение товаров"
        orderControlButton.text = "Начать отгрузку"

//        orderControlButton.invalidate()

//        orderControlButton.setOnClickListener { view ->
//            OrderModel.orderData?.let {
//                if (view.tag == 0) {//Начать отгрузку
//                    val call = startProductShipment().execute().get()
//                    while (!call.isExecuted) {}
//                    if (OrderModel.status == SHIPMENT_STATUS) {
//                        orderControlButton.tag = 1
//                        orderControlButton.text = "Завершить отгрузку"
//                        title = "Отгрузка товара"
//                    }
//                }
//                else if (view.tag == 1) {//Завершить отгрузку
//                    val call = startDelivering().execute().get()
//                    while (!call.isExecuted) {}
//                    if (OrderModel.status == DELIVERING_STATUS) {
//                        orderControlButton.tag = 2
//                        orderControlButton.text = "Завершить доставку"
//                        title = "Доставка товара"
//                    }
//                }
//                else if (view.tag == 2) {//Завершить доставку
//                    val call = finishOrder().execute().get()
//                    while (!call.isExecuted) {}
//                    if (OrderModel.status == DONE_STATUS) {
//                        orderControlButton.tag = 0
//                        orderControlButton.text = "Начать отгрузку"
//                        title = "Получение товаров"
//                    }
//                } else {}
//            }
//        }
        orderControlButton.setOnClickListener { view ->
            if (OrderModel.orderData != null) {
                if (view.tag == 0) {//Начать отгрузку
                    orderControlBtn.isEnabled = false
                    startProductShipment().execute().get()
                }
                else if (view.tag == 1) {//Завершить отгрузку
                    orderControlBtn.isEnabled = false
                    startDelivering().execute().get()
                }
                else if (view.tag == 2) {//Завершить доставку
                    orderControlBtn.isEnabled = false
                    finishOrder().execute().get()
                }
            } else {
                showDialog(this, "Нельзя начать отгрузку", "Нет активного заказа в работе")
            }
        }

        cancelOrderButton.setOnClickListener { _ ->
            OrderModel.orderData?.let {
                cancelOrderBtn.isEnabled = false
                cancelOrder()
            }
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationProvider: String = LocationManager.GPS_PROVIDER

        try {
            locationManager.requestLocationUpdates(locationProvider, 0, 0F, this)
        } catch (e: SecurityException) {
            toast(this, "Leider können wir ohne GPS keine Route berechnen")
        }

        checkPermissionsState()
        setupMap()
        recreateMapView()
        mapController = OsmMapController(mapView)
//        mapController.calcAndDisplayDeliveryRoute(GeoPoint(45.0329, 39.0968), GeoPoint(45.0339, 39.0978), GeoPoint(45.05, 39.0998))
    }

    override fun onPostResume() {
        recreateMapView()
        if (OrderModel.orderData == null) {
            mapView.overlays.clear()
        }

        super.onPostResume()
    }

    /**
     * Probably fixes osmDroid mapView bag since 6.0.3
     */
    private fun recreateMapView() {
        if (mapView.context == null || mapView.context.resources == null) {
            finish()
            overridePendingTransition(0, 0) // @HINT: hides blink after recreate method
            startActivity(intent)
            overridePendingTransition(0, 0)
            Log.i(TAG, "MapView recreated to avoid NPE")
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
                MULTIPLE_PERMISSION_REQUEST_CODE
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

        val compassOverlay = CompassOverlay(this, mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

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

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    /*Override Map activity functions*/
    override fun onStart() {
        mGoogleApiClient?.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient?.disconnect()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    var somePermissionWasDenied = false
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        setupMap()
                    }
                } else {
                    Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
//        mLastLocation = getLastLocation(this, mGoogleApiClient)
        val location = getLastLocation(this, mGoogleApiClient)
        mLastLocation = location
        onLocationChanged(location)
    }

    override fun onConnectionSuspended(i: Int) {
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        } else if (id == R.id.action_locate) {
            setCenterInMyCurrentLocation()
        } else if (id == android.R.id.home) {

            startActivity(Intent(this, MainActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setCenterInMyCurrentLocation() {
        if (mLastLocation != null) {
            mapView.controller.setCenter(GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude))
        } else {
            Toast.makeText(this, "Getting current location", Toast.LENGTH_SHORT).show()
        }
    }

    /*LocationListener implementation*/
    override fun onLocationChanged(location: Location?) {
        // May changed by finishOrder()
        if (OrderModel.status == "FINISHED") return

        mLastLocation = location

        OrderModel.orderData?.let { orderData ->
            val distanceInMeters = GeoPoint(mLastLocation!!).distanceToAsDouble(orderData.departmentPoint)
            if (distanceInMeters < 50) {
                OrderModel.status = "DELIVERING"
            }

            // Отображаем путь через 3 либо через 2 точки
            if (OrderModel.status == "DELIVERING") {
                mapController.calculateAndDisplyRoute(
                    orderData.departmentPoint,
                    orderData.destinationPoint
                )
            } else {
                var departmentPoint = GeoPoint(45.0339, 39.0978)
                var destinationPoint = GeoPoint(45.05, 39.0998)
                OrderModel.orderData?.let {
                    departmentPoint = orderData.departmentPoint
                    destinationPoint = orderData.destinationPoint
                }
                mapController.calcAndDisplayDeliveryRoute(
                    GeoPoint(mLastLocation!!),
                    departmentPoint,
                    destinationPoint
                )
            }
            location?.let {
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(proштvider: String?) {
    }

    /**
     * Отправляет запрос на завершение заказа на сервер.
     * В случае успешного ответа, очищает OrderModel и останавливает отслеживание заказа
     */
    private fun finishOrder(): AsyncTask<Void, Void, Call> {
        val orderId: Int = OrderModel.orderData!!.id
        val context = this
        return changeOrderStatusTask(orderId, "DONE", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {
                            if (OrderModel.status == CANCELED_STATUS) {
                                OrderModel.finishOrder()
                                val orderInfo = orderStatusResponse.body
                                showDialog(context, "Заказ № $orderId отменён",
                                    """
                                    Заказ взят в работу в:
                                    ${formatDate(orderInfo.startDate)}
                                    Отгрузка товаров начата в:
                                    ${formatDate(orderInfo.startShipmentDate)}
                                    Товар отгружен со склада в:
                                    ${formatDate(orderInfo.startDeliveringDate)}
                                    Заказ завершен в:
                                    ${formatDate(orderInfo.doneDate)}

                                    Общий путь составил ${orderInfo.deliveryRouteLength ?: 1.toString()} м.

                                    Конечная стоимость доставки: ${"%.2f".format(orderInfo.totalCost)} руб.
                                """.trimIndent())
                                finishOrder()
                                runOnUiThread {
                                    orderControlBtn.isEnabled = true
                                    cancelOrderBtn.isEnabled = true
                                    cancelOrderBtn.invalidate()
                                    orderControlBtn.tag = 2
                                    orderControlBtn.text = "Завершить доставку"
                                    title = "Доставка"
                                    val latLongTv = findViewById<TextView>(R.id.textView)
                                    latLongTv.text = "Доставьте товары"
                                }
                            }
                            else {
                                OrderModel.clearModel()
                                OrderModel.finishOrder()
                                OrderScheduler.stopOrderTracking()
                                val orderInfo = orderStatusResponse.body
                                showDialog(context, "Заказ № $orderId завершен",
                                    """
                                    Заказ взят в работу в:
                                    ${formatDate(orderInfo.startDate)}
                                    Отгрузка товаров начата в:
                                    ${formatDate(orderInfo.startShipmentDate)}
                                    Товар отгружен со склада в:
                                    ${formatDate(orderInfo.startDeliveringDate)}
                                    Заказ завершен в:
                                    ${formatDate(orderInfo.doneDate)}

                                    Общий путь составил ${orderInfo.deliveryRouteLength ?: 1.toString()} м.
                                    Расход топлива составил ${orderInfo.deliveryRouteLength ?: (0.108).toString() } л.

                                    Конечная стоимость доставки: ${"%.2f".format(orderInfo.totalCost)} руб.
                                """.trimIndent())
                                runOnUiThread {
                                    orderControlBtn.isEnabled = true
                                    orderControlBtn.tag = 0
                                    orderControlBtn.text = "Начать отгрузку"
                                    title = "Получение товаров"
                                    val latLongTv = findViewById<TextView>(R.id.textView)
                                    latLongTv.text = "Доберитесь на склад"
                                }
                            }
                        } else {
                            showDialog(context, "Заказ не может быть завершен", "${orderStatusResponse.header.message}")
                        }
                    }
                }
            }
        })
    }

    private fun formatDate(date: Date?) =
        DateFormat.getDateInstance(DateFormat.LONG).format(date) + " " + DateFormat.getTimeInstance().format(date)

    /**
     * Запрос на начало отгрузки товара
     */
    @SuppressLint("StaticFieldLeak")
    private fun startProductShipment(): AsyncTask<Void, Void, Call> {
        val context = this
        val orderId: Int = OrderModel.orderData!!.id
        return changeOrderStatusTask(orderId, "PRODUCT_SHIPMENT", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {
                            OrderModel.startProductsShipment()
                            showDialog(context, "Начат процесс отгрузки товаров", """
                                Время начала отгрузки:
                                ${formatDate(Date())}
                            """.trimIndent())
                            runOnUiThread {
                                orderControlBtn.isEnabled = true
                                orderControlBtn.tag = 1
                                orderControlBtn.text = "Завершить отгрузку"
                                title = "Отгрузка товара"
                                val latLongTv = findViewById<TextView>(R.id.textView)
                                latLongTv.text = "Отгрузите товары"
                            }
                        } else {
                            runOnUiThread {
                                showDialog(
                                    context,
                                    "Заказ не может быть взят в работу",
                                    "${orderStatusResponse.header.message}"
                                )
                            }
                        }
                    }
                } else {
                    runOnUiThread { toast(context, "Bad response.") }
                }
            }
        })
    }

    /**
     * Запрос на начало доставки товара
     */
    private fun startDelivering(): AsyncTask<Void, Void, Call> {
        val orderId: Int = OrderModel.orderData!!.id
        val context = this
        return changeOrderStatusTask(orderId, "DELIVERING", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {

                                OrderModel.startDelivering()
                                showDialog(
                                    context, "Отгрузка товаров завершена", """
                                Подтвердите отгрузку товаров:
                                      Product#001 - 1 шт.
                                      Product#003 - 2 шт.
                                      Product#004 - 1 шт.
                                """.trimIndent(),
                                "Подтвердить")
                                runOnUiThread {
                                    orderControlBtn.isEnabled = true
                                    cancelOrderBtn.isEnabled = true
                                    cancelOrderBtn.invalidate()
                                    orderControlBtn.tag = 2
                                    orderControlBtn.text = "Завершить доставку"
                                    title = "Доставка товара"
                                    val latLongTv = findViewById<TextView>(R.id.textView)
                                    latLongTv.text = "Доставьте товары"
                                }
                        } else {
                            showDialog(context, "Товары не могут быть отгружены:", "${orderStatusResponse.header.message}")
                        }
                    }
                }
            }
        })
    }

    /**
     * Запрос на отмену заказа
     */
    private fun cancelOrder(): Call {
        val orderId: Int = OrderModel.orderData!!.id
        val context = this
        return changeOrderStatus(orderId, "CANCELED", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {
                            OrderModel.cancelOrder()
                            showDialog(context, "Заказ отменён", """
                                    Доставьте товары обратно на склад
                                """.trimIndent())
                            runOnUiThread {
                                orderControlBtn.isEnabled = true
                                title = "Возврат"
                                orderControlBtn.tag = 1
                                orderControlBtn.text = "Начать отгрузку"
                                OrderModel.status = CANCELED_STATUS
                                val latLongTv = findViewById<TextView>(R.id.textView)
                                latLongTv.text = "Верните товары на склад"
                            }
                        } else {
                            showDialog(context, "Заказ не может быть отменен", "${orderStatusResponse.header.message}")
                        }
                    }
                }
            }
        })
    }

    private fun showDialog (context: Context, title: String, message: String, okButtonText: String = "Ok") {
        val dialogBuilder = AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
        dialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setTitle(title)
            .setPositiveButton(okButtonText) { dialog, _ -> dialog.cancel() }
//            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        runOnUiThread {
            val alert = dialogBuilder.create()
            alert.show()
        }
    }

    data class FinishOrderData(
        val getInWorkTime: Date = Date(),
        val outgoingTime: Date = Date(Date().time + 2000),
        val deliveredTime: Date = Date(Date().time + 4000),
        val wareHouseRouteLength: Int = 10,
        val deliveryRouteLength: Int = 20,
        val totalCost: Double = 0.0
    )

//    @SuppressLint("StaticFieldLeak")
//    inner class FinishOrderTask : AsyncTask<Void, Void, FinishOrderData>() {
//
//        override fun doInBackground(vararg params: Void?): FinishOrderData {
//
//        }
//
//    }
}
