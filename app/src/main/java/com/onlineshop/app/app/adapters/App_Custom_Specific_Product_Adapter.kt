package com.onlineshop.app.app.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onlineshop.app.app.R
import com.onlineshop.app.app.activities.Home_Activity
import com.onlineshop.app.app.models.AppCartItemAddedModel
import com.onlineshop.app.app.api_response.AppSpecificItemResponse
import com.google.gson.Gson

class App_Custom_Specific_Product_Adapter(val context: Context, private val mutableList: MutableList<AppSpecificItemResponse>,
                                          private val dispaly:String): RecyclerView.Adapter<App_Custom_Specific_Product_Adapter.MyViewHolder>() {
    private var alertDialog:Dialog = Dialog(context)

    init {
        alertDialog.create()
    }
    class  MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
     val mText:TextView=itemView.findViewById(R.id.app_item_shop_specific_text)
        val image:ImageView=itemView.findViewById(R.id.app_item_specific_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.app_custom_specific_product_item,parent,false)
        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    holder.mText.text=mutableList[position].title
        Glide.with(context).asBitmap().load(mutableList[position].image).into(holder.image)
 if (dispaly.isNotEmpty() &&  context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE).getBoolean("isDisplayCalled",false)){
     val dataList=Gson().fromJson(dispaly,AppCartItemAddedModel::class.java) as AppCartItemAddedModel
     alertDialog.setContentView(R.layout.app_view_specific_item)
     val title:TextView=alertDialog.findViewById(R.id.spec_item_tittle)
     val decs:TextView=alertDialog.findViewById(R.id.app_spec_item_description)
     val price:TextView=alertDialog.findViewById(R.id.app_spec_item_price)
     val quantityValue:TextView=alertDialog.findViewById(R.id.app_spec_item_quantityValue)
     val addQuantity:ImageView=alertDialog.findViewById(R.id.spec_item_forward)
     val removeQuantity:ImageView=alertDialog.findViewById(R.id.spec_item_backward)
     val buy:Button=alertDialog.findViewById(R.id.app_spec_item_buy_btn)
     val iconImage:ImageView=alertDialog.findViewById(R.id.app_spec_item_image)
     var quantity:Float=1F
     addQuantity.setOnClickListener {
         if(quantity==0F)
             quantity=1F

         ++quantity
         quantityValue.text = quantity.toString()
     }
     removeQuantity.setOnClickListener {
         if(quantity>=1)
             quantityValue.text = quantity--.toString()
     }
     Glide.with(context).asBitmap().load(mutableList[dataList.position].image).into(iconImage)
     title.text="Tittle:"+mutableList[dataList.position].title
     decs.text="Description:".plus(mutableList[dataList.position].description)
     price.text="Price:€".plus(mutableList[dataList.position].price.format("%.2d"))
     buy.setOnClickListener {
         val totalAmount="Total Amount:".plus((mutableList[dataList.position].price.toFloat()*quantity).toString().plus("€"))
         val data=Gson().toJson(AppCartItemAddedModel(dataList.position,mutableList[dataList.position].image,mutableList[dataList.position].id,"quantity:".plus(quantity.toString()),mutableList[dataList.position].title,totalAmount,mutableList[dataList.position].category))
         val intent= Intent(context, Home_Activity::class.java)

         intent.putExtra("toCart",data)
         context.startActivity(intent)
         alertDialog.dismiss()
     }
     context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE).edit().putBoolean("isDisplayCalled",false).apply()
     alertDialog.show()

 }
           holder.itemView.setOnClickListener{
            alertDialog.setContentView(R.layout.app_view_specific_item)
            val title:TextView=alertDialog.findViewById(R.id.spec_item_tittle)
            val decs:TextView=alertDialog.findViewById(R.id.app_spec_item_description)
            val price:TextView=alertDialog.findViewById(R.id.app_spec_item_price)
            val quantityValue:TextView=alertDialog.findViewById(R.id.app_spec_item_quantityValue)
            val addQuantity:ImageView=alertDialog.findViewById(R.id.spec_item_forward)
            val removeQuantity:ImageView=alertDialog.findViewById(R.id.spec_item_backward)
            val buy:Button=alertDialog.findViewById(R.id.app_spec_item_buy_btn)
               val iconImage:ImageView=alertDialog.findViewById(R.id.app_spec_item_image)
            var quantity:Float=1F
             addQuantity.setOnClickListener {
               if(quantity==0F)
                   quantity=1F

                 ++quantity
                 quantityValue.text = quantity.toString()
             }
            removeQuantity.setOnClickListener {
                if(quantity>=1)
                quantityValue.text = quantity--.toString()
            }
               Glide.with(context).asBitmap().load(mutableList[position].image).into(iconImage)
            title.text="Tittle:"+mutableList[position].title
            decs.text="Description:"+mutableList[position].description
            price.text="Price:"+mutableList[position].price+"€"
            buy.setOnClickListener {
                val totalAmount="Total Amount:".plus((mutableList[position].price.toFloat()*quantity).toString().plus("€"))
                val data=Gson().toJson(AppCartItemAddedModel(position,mutableList[position].image,mutableList[position].id,"quantity:".plus(quantity.toString()),mutableList[position].title,totalAmount,mutableList[position].category))
                val intent= Intent(context, Home_Activity::class.java)

                intent.putExtra("toCart",data)
                context.startActivity(intent)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
       return mutableList.size
    }


}