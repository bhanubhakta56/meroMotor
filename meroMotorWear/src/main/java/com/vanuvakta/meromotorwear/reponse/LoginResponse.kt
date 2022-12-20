package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.User

data class LoginResponse(
    val success: Boolean?= null,
    val message: String?=null,
    val token:String?=null,
    val data: User?=null
)