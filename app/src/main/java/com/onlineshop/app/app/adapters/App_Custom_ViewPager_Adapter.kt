package com.onlineshop.app.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onlineshop.app.app.fragments.CartFragment
import com.onlineshop.app.app.fragments.OrdersFragment
import com.onlineshop.app.app.fragments.ProfileFragment
import com.onlineshop.app.app.fragments.ShopFragment

class App_Custom_ViewPager_Adapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                ShopFragment()
            }
            1->{
                CartFragment()
            }
            2->{
                OrdersFragment()
            }
            3->{
                ProfileFragment()
            }
            else-> Fragment()
        }
       }

}