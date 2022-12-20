package com.vanuvakta.meromotorwear.repository

import com.vanuvakta.meromotorwear.api.MyAPIRequest
import com.vanuvakta.meromotorwear.api.ServiceBuilder
import com.vanuvakta.meromotorwear.api.UserAPI
import com.vanuvakta.meromotorwear.entity.User
import com.vanuvakta.meromotorwear.reponse.GetMeResponse
import com.vanuvakta.meromotorwear.reponse.GetOwnerResponse
import com.vanuvakta.meromotorwear.reponse.LoginResponse
import okhttp3.MultipartBody

class UserRepository : MyAPIRequest() {
    private val userAPI= ServiceBuilder.buildService(UserAPI::class.java)

    //login user
    suspend fun checkUser(email: String, password: String): LoginResponse {
        return apiRequest {
            userAPI.checkUser(email, password)
        }
    }

    //get current user
    suspend fun getMe(): GetMeResponse {
        return apiRequest {
            userAPI.getMe(ServiceBuilder.token!!)
        }
    }

    //get owner
    suspend fun getOwner(id: String): GetOwnerResponse {
        return apiRequest {
            userAPI.getOwner(ServiceBuilder.token!!, id)
        }
    }
}