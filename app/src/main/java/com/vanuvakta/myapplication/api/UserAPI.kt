package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    //register User
    @POST("user/register")
    suspend fun registerUser(
            @Body user: User
    ): Response<LoginResponse>

    //login user
    @FormUrlEncoded
    @POST("user/login")
    suspend fun checkUser(
            @Field("email") email :String,
            @Field("password") password :String
    ) : Response<LoginResponse>

    //current User
    @GET("user/me")
    suspend fun getMe(
            @Header("Authorization") token : String
    ) : Response<GetMeResponse>

    //owner
    @GET("user/{id}/owner")
    suspend fun getOwner(
            @Header("Authorization") token: String,
            @Path ("id") id: String
    ): Response<GetOwnerResponse>

    //update company
    @PUT("user/updateUser/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path ("id") id: String,
        @Body user: User
    ): Response<LoginResponse>

    //for product image upload
    @Multipart
    @PUT("user/{id}/photo")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Path ("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<UserImageResponse>
}