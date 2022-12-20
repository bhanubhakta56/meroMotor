package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.response.AddToCartResponse
import com.vanuvakta.myapplication.response.CancelOrderResponse
import com.vanuvakta.myapplication.response.GetMyCartResponse
import com.vanuvakta.myapplication.response.RemoveFromCartResponse
import retrofit2.Response
import retrofit2.http.*

interface CartAPI {
    //insert into cart
    @POST("cart/addToCart/{id}/android")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Path("id") id:String,
        @Body cart: Cart
    ):Response<AddToCartResponse>

    //get My Cart
    @GET("cart/getMyCart/android")
    suspend fun getMyCart(
        @Header("Authorization") token : String,
    ): Response<GetMyCartResponse>

    //delete Cart
    @DELETE("cart/deleteFromCart/{id}")
    suspend fun removeCart(
        @Header("Authorization") token: String,
        @Path("id") id:String,
    ): Response<RemoveFromCartResponse>


}