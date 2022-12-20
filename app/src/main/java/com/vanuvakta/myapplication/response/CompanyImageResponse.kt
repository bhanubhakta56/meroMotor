package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Company

data class CompanyImageResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data: String?=null
)