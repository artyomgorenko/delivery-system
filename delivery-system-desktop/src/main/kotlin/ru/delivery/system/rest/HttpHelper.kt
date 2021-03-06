package ru.delivery.system.rest

//import io.github.rybalkinsd.kohttp.dsl.httpGet
//import io.github.rybalkinsd.kohttp.dsl.httpPost
import okhttp3.*
import ru.delivery.system.common.JsonSerializer
import java.lang.Exception

object HttpHelper {

    private var client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private val prefix = "http://192.168.0.103:8080/delivery-system/"

    fun get(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(prefix + url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun post(url: String, jsonBody: String, callback: Callback): Call {
        val body = RequestBody.create(JSON, jsonBody)
        val request = Request.Builder()
            .url(prefix + url)
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun syncGet(url: String): Response {
        val request = Request.Builder()
            .url(prefix + url)
            .build()
        return client.newCall(request).execute()
    }

    fun syncPost(url: String, jsonBody: String): Response {
        val body = RequestBody.create(JSON, jsonBody)
        val request = Request.Builder()
            .url(prefix + url)
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

    // TODO: перевести http запросы в android и desktop на эту функцию
    private fun syncGet(
        url: String,
        onSuccess: (response: Response) -> Unit,
        onError: (response: Response?, e: Exception) -> Unit
    ) {
        var response: Response? = null
        try {
            response = syncGet(url)
            response.use {
                if (response.isSuccessful) {
                    onSuccess(response)
                }
            }
        } catch (e: Exception) {
            onError(response, e)
        }
    }

}