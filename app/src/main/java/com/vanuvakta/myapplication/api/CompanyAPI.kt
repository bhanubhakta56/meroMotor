package com.vanuvakta.myapplication.api

import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CompanyAPI {
    //add company
    @POST("company/addCompany")
    suspend fun addCompany(
        @Header("Authorization") token:String,
        @Body company:Company
    ):Response<AddCompanyResponse>


    //for product image upload
    @Multipart
    @PUT("company/{id}/photo")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Path ("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<CompanyImageResponse>

    //get my company
    @GET("company/getMyCompany/android")
    suspend fun getMyCompany(
        @Header("Authorization") token : String,
    ): Response<MyCompanyResponse>

    //get company
    @GET("company/getCompany/android/{id}")
    suspend fun getCompany(
        @Header("Authorization") token : String,
        @Path ("id") id: String,
    ): Response<MyCompanyResponse>

    //update company
    @PUT("company/updateCompany/{id}")
    suspend fun updateCompany(
        @Header("Authorization") token: String,
        @Path ("id") id: String,
        @Body company: Company
    ): Response<AddCompanyResponse>
}