package com.onlineshop.app.app.app_webapi



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WebApiClient {

    private var webApiInterface: WebApiInterface
    init {
       val retrofit=Retrofit.Builder().baseUrl(WebApiInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        webApiInterface= retrofit.create(WebApiInterface::class.java)
    }

    fun  getWebApiInterface():WebApiInterface{
        return  webApiInterface
    }
}