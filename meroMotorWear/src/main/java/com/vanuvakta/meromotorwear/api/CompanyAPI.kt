package com.vanuvakta.meromotorwear.api

import com.vanuvakta.meromotorwear.entity.Company
import com.vanuvakta.meromotorwear.reponse.MyCompanyResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CompanyAPI {

    //get my company
    @GET("company/getMyCompany")
    suspend fun getMyCompany(
        @Header("Authorization") token : String,
    ): Response<MyCompanyResponse>
}