package com.vanuvakta.meromotorwear.api

import com.vanuvakta.meromotorwear.reponse.GetMeResponse
import com.vanuvakta.meromotorwear.reponse.GetOwnerResponse
import com.vanuvakta.meromotorwear.reponse.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

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

}