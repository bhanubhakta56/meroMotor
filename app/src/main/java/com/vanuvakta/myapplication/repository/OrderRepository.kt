package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.OrderAPI
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.response.*

class OrderRepository : MyAPIRequest(){
    private var orderAPI = ServiceBuilder.buildService(OrderAPI::class.java)

    //add to Cart
    suspend fun orderThis(id: String, order: Order): OrderResponse {
        return apiRequest {
            orderAPI.orderThis(ServiceBuilder.token!!,id,order)
        }
    }

    //get My Cart
    suspend fun getMyOrder():GetMyOrderResponse{
        return apiRequest {
            orderAPI.getMyOrder(ServiceBuilder.token!!)
        }
    }

    //get New Cart
    suspend fun getNewOrder():NewOrderResponse{
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