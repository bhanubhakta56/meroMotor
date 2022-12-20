package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.ProductAPI
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody

class ProductRepository :MyAPIRequest() {
    private var productAPI = ServiceBuilder.buildService(ProductAPI::class.java)

    //add Product
    suspend fun addProduct(product:Product):AddProductResponse{
        return apiRequest {
            productAPI.addProduct(ServiceBuilder.token!!,product)
        }
    }

    //get all student
    suspend fun getAllProduct():GetAllProductResponse{
        return apiRequest {
            productAPI.getAllProduct(ServiceBuilder.token!!)
        }
    }

    suspend fun getMyProduct():GetMyProductResponse{
        return apiRequest {
            productAPI.getMyProduct(ServiceBuilder.token!!)
        }
    }

    //get single Product
    suspend fun getProduct(id:String):GetProductResponse{
        return apiRequest {
            productAPI.getProduct(ServiceBuilder.token!!, id)
        }
    }

    //upload image
    suspend fun uploadImage(id:String, body: MultipartBody.Part):ProductImageResponse{
        return apiRequest {
            productAPI.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }

    //update Product
    suspend fun updateProduct(id:String, product: Product):AddProductResponse{
        return apiRequest {
            productAPI.updateProduct(ServiceBuilder.token!!, id, product)
        }
    }
}