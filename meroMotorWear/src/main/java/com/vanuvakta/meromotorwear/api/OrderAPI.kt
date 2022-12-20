package com.vanuvakta.meromotorwear.api

import com.vanuvakta.meromotorwear.reponse.CancelOrderResponse
import com.vanuvakta.meromotorwear.reponse.GetMyOrderResponse
import com.vanuvakta.meromotorwear.reponse.NewOrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderAPI {



    //get My Order
    @GET("order/getMyOrder")
    suspend fun getMyOrder(
            @Header("Authorization") token : String,
    ): Response<GetMyOrderResponse>

    //get My Order
    @GET("order/getNewOrder")
    suspend fun getNewOrder(
        @Header("Authorization") token : String,
    ): Response<NewOrderResponse>

    //delete Order
    @DELETE("order/cancelOrder/{id}")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") id:String,
    ): Response<CancelOrderResponse>


}