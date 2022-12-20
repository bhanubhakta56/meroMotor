package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Product

data class GetMyProductResponse (
    val success: Boolean?=null,
    val message: String?=null,
    val count: Int?=null,
    val data: ArrayList<Product>?=null
)