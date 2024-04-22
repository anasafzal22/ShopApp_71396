package com.onlineshop.app.app.models

import com.onlineshop.app.app.app_webapi_objects.address
import com.onlineshop.app.app.app_webapi_objects.name

data class AppSignUpModel(val email:String, val username:String, val password:String, val name: name, val address: address, val phone:String)
