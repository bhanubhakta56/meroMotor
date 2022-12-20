package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.ConnectionAPI
import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.response.ConnectionResponse

class ConnectionRepository: MyAPIRequest() {

    private var connectionAPI = ServiceBuilder.buildService(ConnectionAPI::class.java)
    //get My Cart
    suspend fun checkConnection(): ConnectionResponse{
        return apiRequest {
            connectionAPI.checkConnection()
        }
    }
}