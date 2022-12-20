package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.response.*
import retrofit2.Response
import retrofit2.http.*

interface OrderAPI {

    //order product
    @POST("order/orderProduct/{id}")
    suspend fun orderThis(
            @Header("Authorization") token: String,
            @Path("id") id:String,
            @Body order: Order
    ): Response<OrderResponse>

    //get My Order
    @GET("order/getMyOrder/android")
    suspend fun getMyOrder(
            @Header("Authorization") token : String,
    ): Response<GetMyOrderResponse>

    //get My Order
    @GET("order/getNewOrder/android")
    suspend fun getNewOrder(
        @Header("Authorization") token : String,
    ): Response<NewOrderResponse>

    //delete Cart
    @DELETE("order/cancelOrder/{id}")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") id:String,
    ): Response<CancelOrderResponse>


}