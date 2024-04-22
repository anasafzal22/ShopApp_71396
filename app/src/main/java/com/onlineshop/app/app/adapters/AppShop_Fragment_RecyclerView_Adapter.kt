package com.onlineshop.app.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onlineshop.app.app.R
import com.onlineshop.app.app.activities.Specific_Product_Activity

class AppShop_Fragment_RecyclerView_Adapter(val context: Context, private val mutableList: MutableList<String>): RecyclerView.Adapter<AppShop_Fragment_RecyclerView_Adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val title: TextView =itemView.findViewById(R.id.app_fragment_rec_shop_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     val view=LayoutInflater.from(context).inflate(R.layout.app_shop_frag_rec_categories,parent,false)

        return  MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = mutableList[position]
        holder.itemView.setOnClickListener{

        val intent =Intent(context,Specific_Product_Activity::class.java)
            intent.putExtra("category",mutableList[position])
            context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE).edit().putString("category",mutableList[position]).apply()
            context.startActivity(intent)





        }

    }

    override fun getItemCount(): Int {
        return  mutableList.size
    }

}