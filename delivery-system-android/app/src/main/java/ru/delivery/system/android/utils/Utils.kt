package ru.delivery.system.android.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.util.GeoPoint

fun snackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun distanseBetween(startPoint: GeoPoint, endPoint: GeoPoint) {
    startPoint.distanceToAsDouble(endPoint)
}

fun getLastLocation(context: Context, client: GoogleApiClient?) : Location? {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }
    return LocationServices.FusedLocationApi.getLastLocation(client)
}