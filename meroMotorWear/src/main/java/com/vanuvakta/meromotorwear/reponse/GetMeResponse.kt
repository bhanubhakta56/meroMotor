package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.User


data class GetMeResponse(
        val success:Boolean?=null,
        val message: String?=null,
        val data: User?=null
)