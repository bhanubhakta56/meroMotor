package com.vanuvakta.meromotorwear.repository

import com.vanuvakta.meromotorwear.api.MyAPIRequest
import com.vanuvakta.meromotorwear.api.OrderAPI
import com.vanuvakta.meromotorwear.api.ServiceBuilder
import com.vanuvakta.meromotorwear.reponse.CancelOrderResponse
import com.vanuvakta.meromotorwear.reponse.GetMyOrderResponse
import com.vanuvakta.meromotorwear.reponse.NewOrderResponse

class OrderRepository : MyAPIRequest(){
    private var orderAPI = ServiceBuilder.buildService(OrderAPI::class.java)

    //get My Cart
    suspend fun getMyOrder(): GetMyOrderResponse {
        return apiRequest {
            orderAPI.getMyOrder(ServiceBuilder.token!!)
        }
    }

    //get New Order
    suspend fun getNewOrder(): NewOrderResponse {
        return apiRequest {
            orderAPI.getNewOrder(ServiceBuilder.token!!)
        }
    }

    //delete from cart
    suspend fun cancelOrder(id:String): CancelOrderResponse {
        return apiRequest {
            orderAPI.cancelOrder(ServiceBuilder.token!!, id)
        }
    }
}