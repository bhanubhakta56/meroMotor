package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.response.ConnectionResponse
import retrofit2.Response
import retrofit2.http.GET

interface ConnectionAPI {
    //get My Cart
    @GET("connection/checkConnection")
    suspend fun checkConnection(
    ): Response<ConnectionResponse>
}