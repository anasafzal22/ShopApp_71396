package com.onlineshop.app.app.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onlineshop.app.app.R
import com.onlineshop.app.app.activities.Home_Activity
import com.onlineshop.app.app.adapters.App_Cart_Adapter
import com.onlineshop.app.app.models.AppCartItemAddedModel
import com.onlineshop.app.app.app_shop_interface.CartInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(),CartInterface {
    private lateinit var recyclerView: RecyclerView
    private lateinit var  adapter:App_Cart_Adapter
    private   lateinit var floatingActionButton:FloatingActionButton
    private lateinit var  sharedPreferences: SharedPreferences


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.app_fragment_cart, container, false)
        sharedPreferences=requireActivity().getSharedPreferences(requireActivity().packageName, Context.MODE_PRIVATE)
        recyclerView=view.findViewById(R.id.cart_frag_rex)
        floatingActionButton=view.findViewById(R.id.app_floatingBarOrder)
        floatingActionButton.setImageResource(R.drawable.ic_baseline_local_printshop_24)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        val type=object: TypeToken<MutableList<AppCartItemAddedModel>>(){}.type
        if(sharedPreferences.getString("dataCart","")?.isNotEmpty() == true){
            val dataList=Gson().fromJson(sharedPreferences.getString("dataCart",""),type) as MutableList<AppCartItemAddedModel>
            val adapter= App_Cart_Adapter(requireContext(),dataList)
            recyclerView.adapter=adapter
            floatingActionButton.setOnClickListener {

                val alertDialog=AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Order Product")
                alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24)
                alertDialog.setMessage("Thank you for choosing to shop with us! You're almost done with your purchase.The total amount is:${ sharedPreferences.getString("totalAmount","0.0€")}")
                alertDialog.setNeutralButton("Continue Shopping", DialogInterface.OnClickListener { dialogInterface, i ->
                    run {
                        dialogInterface.cancel()
                    }
                })

                alertDialog.setPositiveButton("order", DialogInterface.OnClickListener { dialogInterface, i ->
                    run {

                        if(sharedPreferences.getString("orderItems","")?.isNotEmpty() == true){

                            val type1=object:TypeToken<MutableList<AppCartItemAddedModel>>(){}.type
                            val dataList1=Gson().fromJson(sharedPreferences.getString("orderItems",""),type1) as MutableList<AppCartItemAddedModel>
                            Log.d("FragCartAllLoad!List!",sharedPreferences.getString("orderItems","")!!)
                           val list2=Gson().fromJson(sharedPreferences.getString("dataCart",""),type1 )as MutableList<AppCartItemAddedModel>
                            Log.d("FragCartAllLoad!List2",sharedPreferences.getString("dataCart","")!!)
                            dataList1.addAll(list2)
                            val storeData=Gson().toJson(dataList1)


                            sharedPreferences.edit().putString("orderItems",storeData).apply()
                            Log.d("FragCartAllLoad!",storeData!!)

                        }else {
                            sharedPreferences.edit().putString("orderItems", sharedPreferences.getString("dataCart","")).apply()
                            Log.d("FragCartAllLoad22",sharedPreferences.getString("orderItems","")!!)
                        }

                        showNotification()
                        sharedPreferences.edit().putString("totalAmount","0.0€").apply()
                        ( requireActivity() as Home_Activity).supportActionBar?.subtitle="Total Amount:".plus(sharedPreferences.getString("totalAmount","€0.0"))
                        sharedPreferences.edit().putString("dataCart","").apply()
                        sharedPreferences.edit().putBoolean("ordered",true).apply()
                        dataList.clear()
                        adapter.notifyDataSetChanged()


                       // sharedPreferences.edit().putString("orderItem",sharedPreferences.getString("data","")).apply()


                    }
                })

                alertDialog.create().show()
            }

        }



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        private  var dataFrag:String=""
      fun getData(data:String){
         dataFrag=data
      }


    }

    override fun addCart(cartItemAddedModel: AppCartItemAddedModel) {
        Log.d("FragCart",cartItemAddedModel.productQuantity)
        val dataList= mutableListOf<AppCartItemAddedModel>()
        dataList.add(cartItemAddedModel)
        val storeData=Gson().toJson(dataList)
        Log.d("FragCart",storeData!!+dataList.size.toString())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun  showNotification(){
        val notificationManager=requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel=NotificationChannel(requireActivity().packageName,"MyChannel",NotificationManager.IMPORTANCE_HIGH)
        val notificationBuilder=NotificationCompat.Builder(requireContext(),requireActivity().packageName)
        val intent= Intent(requireActivity(),Home_Activity::class.java)
   val pendingIntent=PendingIntent.getActivity(requireContext(),0,intent,
       PendingIntent.FLAG_IMMUTABLE)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationBuilder.setSmallIcon(R.drawable.ic_baseline_shopping_cart_24)
        notificationBuilder.setContentTitle("Order Notification")
        notificationBuilder.setContentText("Successfully ordered product total amount ${ sharedPreferences.getString("totalAmount","0.0$")}")
        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(0,notificationBuilder.build())
    }

}