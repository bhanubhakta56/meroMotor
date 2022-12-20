package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Product
import com.vanuvakta.meromotorwear.entity.User

class GetProductResponse (
        val success: Boolean?= null,
        val message:String?= null,
        val product: Product?=null,
        val owner: User?=null //bcz user will be owner if he/she added any product.
)