package ru.delivery.system.android.models.json

abstract class BaseJsonModel {
    val header = Header()

    class Header {
        var message: String? = null
        var resultCode: Int? = null
    }
}