package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.entity.User

class GetProductResponse (
        val success: Boolean?= null,
        val message:String?= null,
        val product: Product?=null,
)