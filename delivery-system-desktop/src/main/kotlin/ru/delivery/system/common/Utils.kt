package ru.delivery.system.common

import de.saring.leafletmap.LatLong
import ru.delivery.system.models.json.NominatimResponse
import ru.delivery.system.rest.HttpHelper
import tornadofx.*
import tornadofx.FX.Companion.primaryStage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

inline fun <reified T : View> openModal() {
    find<T>().openModal()
}

inline fun <reified T : View> navigateTo() {
    primaryStage.uiComponent<UIComponent>()?.replaceWith(T::class)
}

inline fun <reified T : View> navigateToLeft() {
    primaryStage.uiComponent<UIComponent>()?.replaceWith(T::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)/*, sizeToScene = true, centerOnScreen = true*/)
}

inline fun <reified T : View> navigateToRight() {
    primaryStage.uiComponent<UIComponent>()?.replaceWith(T::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)/*, sizeToScene = true, centerOnScreen = true*/)
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

fun getCoordsFromAddress(city: String, street: String, houseNumber: String) : LatLong? {
    val resp = HttpHelper.syncGetFullUrl("https://nominatim.openstreetmap.org/search?city=$city&street=$street $houseNumber&format=geocodejson&limit=1")
    resp.use { response ->
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val nResp = JsonSerializer().toEntity<NominatimResponse>(body.string())
                body.close()
                val latLongArray = nResp.features!![0].geometry!!.coordinates!!
                return LatLong(latLongArray[0].toDouble(), latLongArray[1].toDouble())
            }
        }
    }
    return null
}