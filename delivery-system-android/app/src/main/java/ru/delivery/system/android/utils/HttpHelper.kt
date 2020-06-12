package ru.delivery.system.android.utils

import okhttp3.*
import android.preference.PreferenceManager
import ru.delivery.system.android.MainActivity


object HttpHelper {
    private var client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private val prefix = "http://192.168.0.103:8080/delivery-system/"

    private fun getUrl() : String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context)
        val ip = prefs.getString("connect_ip", "192.168.0.103")
        return "http://$ip:8080/delivery-system/"
    }

    fun get(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(getUrl() + url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun post(url: String, jsonBody: String, callback: Callback): Call {
        val body = RequestBody.create(JSON, jsonBody)
        val request = Request.Builder()
            .url(getUrl() + url)
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun syncGet(url: String): Response {
        val request = Request.Builder()
            .url(getUrl() + url)
            .build()
        return client.newCall(request).execute()
    }

    fun syncPost(url: String, jsonBody: String): Response {
        val body = RequestBody.create(JSON, jsonBody)
        val request = Request.Builder()
            .url(getUrl() + url)
            .post(body)
            .build()
        return client.newCall(request).execute()
    }

    private fun createFormBody(parameters: HashMap<String, String>) : FormBody {
        val builder = FormBody.Builder()
        val it = parameters.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }
        return builder.build()
    }

}