package com.vanuvakta.myapplication.repository

import com.vanuvakta.myapplication.api.MyAPIRequest
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.api.UserAPI
import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.response.*
import okhttp3.MultipartBody

class UserRepository : MyAPIRequest() {
    private val userAPI= ServiceBuilder.buildService(UserAPI::class.java)

    //register user
    suspend fun registerUser(user: User):LoginResponse{
        return apiRequest {
            userAPI.registerUser(user)
        }
    }

    //login user
    suspend fun checkUser(email: String, password: String):LoginResponse{
        return apiRequest {
            userAPI.checkUser(email, password)
        }
    }

    //get current user
    suspend fun getMe():GetMeResponse{
        return apiRequest {
            userAPI.getMe(ServiceBuilder.token!!)
        }
    }

    //get owner
    suspend fun getOwner(id: String):GetOwnerResponse{
        return apiRequest {
            userAPI.getOwner(ServiceBuilder.token!!, id)
        }
    }

    //update User
    suspend fun updateUser(id:String, user:User): LoginResponse {
        return apiRequest {
            userAPI.updateUser(ServiceBuilder.token!!, id, user)
        }
    }

    //upload image
    suspend fun uploadUserImage(id:String, body: MultipartBody.Part): UserImageResponse{
        return apiRequest {
            userAPI.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }

}