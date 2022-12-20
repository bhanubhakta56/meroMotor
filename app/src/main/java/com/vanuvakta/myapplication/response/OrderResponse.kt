package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Order

data class OrderResponse(
        val success: Boolean?= null,
        val message: String?=null,
        val data:Order?=null
)