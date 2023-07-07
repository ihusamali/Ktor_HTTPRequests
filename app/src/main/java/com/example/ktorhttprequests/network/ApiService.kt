package com.example.ktorhttprequests.network

import com.example.ktorhttprequests.models.RequestModel
import com.example.ktorhttprequests.models.ResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

interface ApiService {

    suspend fun getProducts() : List<ResponseModel>

    suspend fun createProducts(requestModel: RequestModel) : ResponseModel?

    companion object{
        fun create() : ApiService {
            return ApiServiceImpl(
                client = HttpClient(Android) {

                    //Logging
                    install(Logging) {
                        level = LogLevel.ALL
                    }

                    //JSON
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(json)
                    }

                    //Timeout
                    install(HttpTimeout) {
                        requestTimeoutMillis = 15000L
                        connectTimeoutMillis = 15000L
                        socketTimeoutMillis = 15000L
                    }

                    //apply to all requests
                    defaultRequest {
                        if (method != HttpMethod.Get) {
                            contentType(ContentType.Application.Json)

                        }
                    }
                }
            )
        }

        private val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults =false
        }
    }

}