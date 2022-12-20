package com.vanuvakta.myapplication

import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.repository.CompanyRepository
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MeroMotorTesting {
    private lateinit var companyRepository: CompanyRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var userRepository: UserRepository

    @Test
    fun checkUser() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.checkUser("a@gmail.com", "password")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkSignUp() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.registerUser(
            User(
                _id="1",
                first_name = "Unish",
                last_name = "Bhattrai",
                gender="Male",
                email="unish@gmail.com",
                mobile="986745234",
                password = "password"
            ))
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkAddCompany() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.checkUser("a@gmail.com", "password").token
        companyRepository = CompanyRepository()
        val company = Company(companyName = "B&B Suppliers", owner = "6072b925e157fe0a5404a98d" ,contact = "9867542356", email = "bnbsuppliers@gmail.com", address = "Thapathali, Kathmandu")
        val response = companyRepository.addCompany(company)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkAddProduct() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.checkUser("a@gmail.com", "password").token

        productRepository = ProductRepository()
        val product = Product(_id = "this will not be added",owner="6072b925e157fe0a5404a98d", productName = "Clutch Plate", price = 234, brand = "Honda", description = "honda is aways honda. its like anaconda", available = 12)
        val response = productRepository.addProduct(product)

        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkGetAllProduct() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.checkUser("a@gmail.com", "password").token
        productRepository = ProductRepository()
        val response = productRepository.getAllProduct()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkGetMyCompany() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.checkUser("a@gmail.com", "password").token
        productRepository = ProductRepository()
        val response = productRepository.getAllProduct()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

}