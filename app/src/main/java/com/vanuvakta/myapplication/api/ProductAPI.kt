package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProductAPI {
    //register User
    @POST("product/addProduct")
    suspend fun addProduct(
            @Header("Authorization") token : String,
            @Body product: Product
    ): Response<AddProductResponse>

    @GET("product/android/getAllProduct")
    suspend fun getAllProduct(
            @Header("Authorization") token : String,
    ): Response<GetAllProductResponse>

    @GET("product/getMyProduct")
    suspend fun getMyProduct(
        @Header("Authorization") token : String,
    ): Response<GetMyProductResponse>

    @GET("product/{id}/viewProduct/android")
    suspend fun getProduct(
            @Header("Authorization") token: String,
            @Path ("id") id: String
    ): Response<GetProductResponse>

    //for product image upload
    @Multipart
    @PUT("product/{id}/photo")
    suspend fun uploadImage(
            @Header("Authorization") token: String,
            @Path ("id") id: String,
            @Part file: MultipartBody.Part
    ): Response<ProductImageResponse>

    @PUT("product/updateProduct/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path ("id") id: String,
        @Body product: Product
    ): Response<AddProductResponse>
}