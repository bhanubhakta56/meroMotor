package com.vanuvakta.meromotorwear.api

import com.vanuvakta.meromotorwear.reponse.GetAllProductResponse
import com.vanuvakta.meromotorwear.reponse.GetMyProductResponse
import com.vanuvakta.meromotorwear.reponse.GetProductResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProductAPI {


    @GET("product/getAllProduct")
    suspend fun getAllProduct(
            @Header("Authorization") token : String,
    ): Response<GetAllProductResponse>

    @GET("product/getMyProduct")
    suspend fun getMyProduct(
        @Header("Authorization") token : String,
    ): Response<GetMyProductResponse>

    @GET("product/{id}/viewProduct")
    suspend fun getProduct(
            @Header("Authorization") token: String,
            @Path ("id") id: String
    ): Response<GetProductResponse>

}