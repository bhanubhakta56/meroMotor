package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Product

data class AddProductResponse(
        val success:Boolean?=null,
        val message: String?=null,
        val data: Product?=null
)