package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.User

data class LoginResponse(
    val success: Boolean?= null,
    val message: String?=null,
    val token:String?=null,
    val data: User?=null
)