package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Product


data class GetAllProductResponse (
        val success:Boolean?=null,
        val message:String?=null,
        val data: MutableList<Product>?=null
        )