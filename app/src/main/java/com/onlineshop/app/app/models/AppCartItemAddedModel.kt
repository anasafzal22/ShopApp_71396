package com.onlineshop.app.app.models

data class AppCartItemAddedModel(val position:Int, val imageUrl:String, val productId:Int, val productQuantity: String, val title:String, val amount:String, val category:String)
