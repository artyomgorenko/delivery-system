package ru.delivery.system.rest

import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.dsl.httpPost
import okhttp3.Response

class HttpHelper(val serverHost: String, val serverPort: Int) {

    fun postStringJson(pathVar: String, json: String) : Response {
        return httpPost {
            host = serverHost
            port = serverPort
            path = pathVar
            body("application/json") {
                string(json)
            }
        }
    }

    fun getStringJson(pathVar: String) : Response {
        return httpGet {
            host = serverHost
            port = serverPort
            path = pathVar
        }
    }
}