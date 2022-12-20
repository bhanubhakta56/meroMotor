package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Product

data class GetAllProductResponse (
        val success:Boolean?=null,
        val message:String?=null,
        val data: MutableList<Product>?=null
        )