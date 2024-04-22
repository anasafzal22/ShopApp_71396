package com.onlineshop.app.app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onlineshop.app.app.R
import com.onlineshop.app.app.adapters.AppShop_Fragment_RecyclerView_Adapter
import com.onlineshop.app.app.app_webapi.WebApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().actionBar?.let { it.title = "Categories" }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.app_fragment_shop, container, false)
        requireActivity().title = "Categories"
        val recycler=view.findViewById<RecyclerView>(R.id.app_fragment_shop_recyclerview)
        val progress=view.findViewById<ProgressBar>(R.id.progress_frag_shop)
        WebApiClient.getWebApiInterface().getProductCategories().enqueue(object:Callback<MutableList<String>>{
            override fun onResponse(
                call: Call<MutableList<String>>,
                response: Response<MutableList<String>>
            ) {
               Log.d("ShopFrag",response.code().toString())
                if(response.code()==200){
                    recycler.layoutManager=LinearLayoutManager(requireContext())
                    recycler.adapter=
                        response.body()
                            ?.let { AppShop_Fragment_RecyclerView_Adapter(requireContext(), it) }
                       progress.visibility=View.GONE

                }
            }

            override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                t.message?.let { Log.d("ShopFrag", it) }
            }

        })

        return  view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}