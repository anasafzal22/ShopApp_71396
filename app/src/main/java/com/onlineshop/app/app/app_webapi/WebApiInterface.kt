package com.onlineshop.app.app.app_webapi

import com.onlineshop.app.app.models.AppSignUpModel
import com.onlineshop.app.app.api_response.AppSignUpResponse
import com.onlineshop.app.app.api_response.AppSpecificItemResponse
import com.onlineshop.app.app.api_response.AppUserSignUpResponse
import retrofit2.Call
import retrofit2.http.*

interface WebApiInterface {
    companion object {
        const val BASE_URL: String = "https://fakestoreapi.com"
    }

    @POST("/users")
    fun signUpUser(@Body signUpModel: AppSignUpModel):Call<AppUserSignUpResponse>
    @GET("/products/categories")
    fun getProductCategories():Call<MutableList<String>>
    @GET("/products/category/" +
            "{specific}")
    fun getProductCategoriesSpecific(@Path("specific")  specific:String):Call<MutableList<AppSpecificItemResponse>>
    @FormUrlEncoded
    @POST("/auth/login")
    fun loginUser(@Field("username") username:String,@Field("password") password:String):Call<AppSignUpResponse>
}