package com.vanuvakta.meromotorwear.repository

import com.vanuvakta.meromotorwear.api.MyAPIRequest
import com.vanuvakta.meromotorwear.api.ProductAPI
import com.vanuvakta.meromotorwear.api.ServiceBuilder
import com.vanuvakta.meromotorwear.reponse.GetAllProductResponse
import com.vanuvakta.meromotorwear.reponse.GetMyProductResponse
import com.vanuvakta.meromotorwear.reponse.GetProductResponse

class ProductRepository : MyAPIRequest() {
    private var productAPI = ServiceBuilder.buildService(ProductAPI::class.java)


    //get all student
    suspend fun getAllProduct(): GetAllProductResponse {
        return apiRequest {
            productAPI.getAllProduct(ServiceBuilder.token!!)
        }
    }

    suspend fun getMyProduct(): GetMyProductResponse {
        return apiRequest {
            productAPI.getMyProduct(ServiceBuilder.token!!)
        }
    }

    //get single Product
    suspend fun getProduct(id:String): GetProductResponse {
        return apiRequest {
            productAPI.getProduct(ServiceBuilder.token!!, id)
        }
    }
}