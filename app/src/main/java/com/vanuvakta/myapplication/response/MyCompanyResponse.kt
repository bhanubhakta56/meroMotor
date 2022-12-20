package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Company

data class MyCompanyResponse(
    val success:Boolean?=null,
    val message: String?=null,
    val company: Company?=null
)