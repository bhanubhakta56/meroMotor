package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.CompanyAPI
import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody

class CompanyRepository:MyAPIRequest() {
    private var companyAPI =  ServiceBuilder.buildService(CompanyAPI::class.java)

    //add company
    suspend fun addCompany(company: Company):AddCompanyResponse{
        return apiRequest{
            companyAPI.addCompany(ServiceBuilder.token!!, company)
        }
    }

    //upload image
    suspend fun uploadImage(id:String, body: MultipartBody.Part):CompanyImageResponse{
        return apiRequest {
            companyAPI.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }


    //get my company
    suspend fun getMyCompany(): MyCompanyResponse {
        return apiRequest {
            companyAPI.getMyCompany(ServiceBuilder.token!!)
        }
    }

    //get company
    suspend fun getCompany(id:String): MyCompanyResponse {
        return apiRequest {
            companyAPI.getCompany(ServiceBuilder.token!!, id)
        }
    }

    //update Company
    suspend fun updateCompany(id:String, company: Company):AddCompanyResponse{
        return apiRequest {
            companyAPI.updateCompany(ServiceBuilder.token!!, id, company)
        }
    }

}