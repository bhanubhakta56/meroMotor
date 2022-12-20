package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.CartAPI
import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.response.AddProductResponse
import com.vanuvakta.myapplication.response.AddToCartResponse
import com.vanuvakta.myapplication.response.GetMyCartResponse
import com.vanuvakta.myapplication.response.RemoveFromCartResponse

class CartRepository : MyAPIRequest(){
    private var cartAPI = ServiceBuilder.buildService(CartAPI::class.java)

    //add to Cart
    suspend fun addToCart(id: String, cart: Cart): AddToCartResponse {
        return apiRequest {
            cartAPI.addToCart(ServiceBuilder.token!!,id,cart)
        }
    }

    //get My Cart
    suspend fun getMyCart():GetMyCartResponse{
        return apiRequest {
            cartAPI.getMyCart(ServiceBuilder.token!!)
        }
    }

    //delete from cart
    suspend fun removeCart(id:String): RemoveFromCartResponse{
        return apiRequest {
            cartAPI.removeCart(ServiceBuilder.token!!, id)
        }
    }
}