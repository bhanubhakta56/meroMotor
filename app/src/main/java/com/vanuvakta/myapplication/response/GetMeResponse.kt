package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.User

data class GetMeResponse(
        val success:Boolean?=null,
        val message: String?=null,
        val data: User?=null
)