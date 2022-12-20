package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Product

data class GetMyProductResponse (
    val success: Boolean?=null,
    val message: String?=null,
    val data: ArrayList<Product>?=null
)