package com.onlineshop.app.app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.onlineshop.app.app.R
import com.onlineshop.app.app.adapters.App_Custom_ViewPager_Adapter
import com.onlineshop.app.app.models.AppCartItemAddedModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Home_Activity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var pagerView: ViewPager2
    private lateinit var storeDataLocally: SharedPreferences
    private lateinit var mydatalist: MutableList<AppCartItemAddedModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_home)
        if (getSharedPreferences(packageName, MODE_PRIVATE).getString("userStoreName", "")
                ?.isNotEmpty()?.equals(true) == true
        ) {
            if (getSharedPreferences(packageName, MODE_PRIVATE).getString(
                    "userAccName",
                    ""
                ) != getSharedPreferences(packageName, MODE_PRIVATE).getString("userStoreName", "")
            ) {
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                    .putString("dataCart", "").apply()
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                    .putString("orderItems", "").apply()

            }
        }

        findMyViews()
    }

    //function to initialize views
    private fun findMyViews() {
        bottomNavView = findViewById(R.id.app_home_bottom_nav)
        pagerView = findViewById(R.id.app_home_viewpager)
        businessLogic()
    }

    //function to perform logic
    private fun businessLogic() {
        storeDataLocally = getSharedPreferences(packageName, MODE_PRIVATE)
        mydatalist = mutableListOf()
        val customViewPagerAdapter = App_Custom_ViewPager_Adapter(this@Home_Activity)
        pagerView.adapter = customViewPagerAdapter
        pagerView.isUserInputEnabled = false
        bottomNavView.setOnItemSelectedListener(this@Home_Activity)

        if (storeDataLocally.getBoolean("ordered", false)) {
            bottomNavView.selectedItemId = R.id.nav_bar_order_icon
            pagerView.setCurrentItem(2, false)
            storeDataLocally.edit().putBoolean("ordered", false).apply()
        }
        val data = intent.getStringExtra("toCart")

        if (data?.isNotEmpty() == true) {

            if (storeDataLocally.getString("dataCart", "")?.isNotEmpty() == true) {

                val type = object : TypeToken<MutableList<AppCartItemAddedModel>>() {}.type
                mydatalist = Gson().fromJson(
                    storeDataLocally.getString("dataCart", ""),
                    type
                ) as MutableList<AppCartItemAddedModel>
                mydatalist.add(Gson().fromJson(data, AppCartItemAddedModel::class.java))
                val storeData = Gson().toJson(mydatalist)
                Log.d("FragCart", storeData!!)
                storeDataLocally.edit().putString("dataCart", storeData).apply()


                var totalAmount = 0F
                for (mData in mydatalist) {
                    totalAmount += (mData.amount.replace(Regex("[^0-9 .]"), "").toFloat())
                    Log.d("FragCart", totalAmount.toString())
                }
                storeDataLocally.edit()
                    .putString("totalAmount", totalAmount.toString().plus("€").format("%.2f"))
                    .apply()


            } else {
                mydatalist.add(Gson().fromJson(data, AppCartItemAddedModel::class.java))
                val storeData = Gson().toJson(mydatalist)
                storeDataLocally.edit().putString("dataCart", storeData).apply()
                var totalAmount = 0F
                for (mData in mydatalist) {
                    totalAmount += (mData.amount.replace(Regex("[^0-9 .]"), "").toFloat())
                }
                storeDataLocally.edit().putString(
                    "totalAmount", totalAmount.toString().format(
                        Locale.UK, ".%2f"
                    ).plus("€")
                ).apply()
                Log.d("FragCart", totalAmount.toString())

                //  Log.d("FragCart",totalAmount.toString())

            }


            // Log.d("FragCart",storeData!!)


            //   Log.d("Act",dataList.size.toString())
            bottomNavView.selectedItemId = R.id.nav_bar_cart_icon
            pagerView.setCurrentItem(1, false)

        }
    }

    //a listener to listen to item selected on the bottom bar
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_bar_shop_icon -> {
                supportActionBar?.title = "Categories"
                supportActionBar?.subtitle = ""
                pagerView.setCurrentItem(0, false)

                true
            }
            R.id.nav_bar_cart_icon -> {
                supportActionBar?.title = "Cart"
                supportActionBar?.subtitle =
                    "Total Amount:".plus(storeDataLocally.getString("totalAmount", "$0.0"))
                pagerView.setCurrentItem(1, false)

                true
            }
            R.id.nav_bar_order_icon -> {
                supportActionBar?.title = "Orders"
                supportActionBar?.subtitle = ""
                pagerView.setCurrentItem(2, false)
                true
            }
            R.id.nav_bar_profile_icon -> {
                supportActionBar?.title = "Profile"
                supportActionBar?.subtitle = ""
                pagerView.setCurrentItem(3, false)
                true
            }
            else -> {
                false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this@Home_Activity)
        inflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                getSharedPreferences(packageName, MODE_PRIVATE).edit().putString("userToken", "")
                    .apply()
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                    .putString("userStoreName", storeDataLocally.getString("userAccName", ""))
                    .apply()
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                    .putString("userAccName", "").apply()
                startActivity(Intent(this@Home_Activity, SignIn_Activity::class.java))
                finish()
                true
            }

            else -> {
                false
            }
        }
        //return super.onOptionsItemSelected(item)
    }
}