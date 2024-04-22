package com.onlineshop.app.app.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onlineshop.app.app.R
import com.onlineshop.app.app.activities.Specific_Product_Activity
import com.onlineshop.app.app.models.AppCartItemAddedModel
import com.google.gson.Gson

class AppOrder_Adapter(val context: Context, private val mutableList:MutableList<AppCartItemAddedModel>): RecyclerView.Adapter<AppOrder_Adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView =itemView.findViewById(R.id.app_cart_specific_text)
        val totalAmount: TextView =itemView.findViewById(R.id.app_cart_specific_text_3)
        val quantity: TextView =itemView.findViewById(R.id.app_cart_specific_text_2)
        val image: ImageView =itemView.findViewById(R.id.app_cart_specific_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.app_cart_template,parent,false)
        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //  holder.title.ellipsize=TextUtils.TruncateAt.valueOf()
        holder.title.text="productId:".plus(mutableList[position].productId.toString())
        holder.totalAmount.text=mutableList[position].amount
        holder.quantity.text=mutableList[position].productQuantity
        Glide.with(context).asBitmap().load(mutableList[position].imageUrl).into(holder.image)
        holder.itemView.setOnClickListener {
            val data=mutableList[position]
          context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE).edit().putString("display",Gson().toJson(data)).apply()
            context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE).edit().putBoolean("isDisplayCalled",true).apply()
            val intent=Intent(context,Specific_Product_Activity::class.java)
            intent.putExtra("category",mutableList[position].category )
            Log.d("FragCartc",mutableList[position].category)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  mutableList.size
    }
}