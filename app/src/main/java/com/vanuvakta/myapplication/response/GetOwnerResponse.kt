package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.entity.User

class GetOwnerResponse (
        val success: Boolean?= null,
        val message: String?=null,
        val owner: User?=null //bcz user will be owner if he/she added any product.
)