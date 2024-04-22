package com.onlineshop.app.app.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.NestedScrollView
import com.onlineshop.app.app.R
import com.onlineshop.app.app.models.AppSignUpModel
import com.onlineshop.app.app.app_webapi.WebApiClient
import com.onlineshop.app.app.app_webapi_objects.address
import com.onlineshop.app.app.app_webapi_objects.geolocation
import com.onlineshop.app.app.app_webapi_objects.name
import com.onlineshop.app.app.api_response.AppUserSignUpResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup_Activity : AppCompatActivity() {
    private  lateinit var usernameEditTextView:EditText
    private  lateinit var usernamePasswordEditTextView:EditText
    private  lateinit var userFirstnameEditText:EditText
    private  lateinit var userLastnamePasswordEditText:EditText
    private  lateinit var userAddressNumberEditText:EditText
    private  lateinit var addressStreetEditTextView:EditText
    private  lateinit var cityEditTextView:EditText
    private  lateinit var userZipCodePasswordEditText:EditText
    private  lateinit var userPhoneNumberEditText:EditText
    private  lateinit var userEmailEditText:EditText
    private  lateinit var progressBar: ProgressBar
    private  lateinit var  signupButton: Button
    private  lateinit var  scrollView: NestedScrollView
    private  lateinit var  storeUserLocally: SharedPreferences
    private  lateinit var  signinButton:TextView
    private  var  viewHeight:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_signup)

        if(!(getSharedPreferences(packageName, Context.MODE_PRIVATE).getString("userToken","").isNullOrEmpty())){
            val intent= Intent(this@Signup_Activity,Home_Activity::class.java)
            startActivity(intent)
            finish()
        }
        findView()
    }
    // a function to initialize views
    private fun findView(){
        usernameEditTextView=findViewById(R.id.signup_username_edit_view)
        usernamePasswordEditTextView=findViewById(R.id.signup_password_edit_view)
        userFirstnameEditText=findViewById(R.id.signup_firstname_edit_view)
        userLastnamePasswordEditText=findViewById(R.id.signup_lastname_edit_view)
       userAddressNumberEditText=findViewById(R.id.signup_addressnumber_edit_view)
        addressStreetEditTextView=findViewById(R.id.signup_addressstreet_edit_view)
       cityEditTextView=findViewById(R.id.signup_city_edit_view)
        userZipCodePasswordEditText=findViewById(R.id.signup_zipcode_edit_view)
        userPhoneNumberEditText=findViewById(R.id.signup_phonenumber_edit_view)
        userEmailEditText=findViewById(R.id.signup_user_email_edit_view)
        progressBar=findViewById(R.id.signup_progress_bar)
        signupButton=findViewById(R.id.signup_button)
        signinButton=findViewById(R.id.signup_username_edit_sign_in)
        scrollView=findViewById(R.id.nestedScroll)
        businessLogic()
    }
    // function to perform logic
    private fun businessLogic(){

        storeUserLocally=getSharedPreferences(packageName, Context.MODE_PRIVATE)
       signupButton.setOnClickListener {
           val username=usernameEditTextView.text
           val userPassword=usernamePasswordEditTextView.text
           val userFirstName=userFirstnameEditText.text
           val userLastName=userLastnamePasswordEditText.text
           val userAddressNumber=userAddressNumberEditText.text
           val userAddressStreet=addressStreetEditTextView.text
           val userCity=cityEditTextView.text
           val userZipCode=userZipCodePasswordEditText.text
           val userPhoneNumber=userPhoneNumberEditText.text
           val userEmail=userEmailEditText.text
           viewHeight=resources.displayMetrics.heightPixels
           signinButton.setOnClickListener {
               startActivity(Intent(this@Signup_Activity,SignIn_Activity::class.java))
               finish()
           }
           if(TextUtils.isEmpty(username.toString().trim())){
               usernameEditTextView.error = "enter name"
               usernameEditTextView.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userPassword.toString().trim())){
               usernamePasswordEditTextView.error = "enter password"
               usernamePasswordEditTextView.requestFocus()
               return@setOnClickListener
           }
           if( TextUtils.isEmpty(userEmail.toString().trim())){
               userEmailEditText.error = "enter email"
               userEmailEditText.requestFocus()
               return@setOnClickListener
           }
           if( TextUtils.isEmpty(userFirstName.toString().trim())){
               userFirstnameEditText.error = "first name"
               userFirstnameEditText.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userLastName.toString().trim())){
               userLastnamePasswordEditText.error = "last name"
               userLastnamePasswordEditText.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userAddressNumber.toString().trim())){
               userAddressNumberEditText.error = "enter address"
               userAddressNumberEditText.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userAddressStreet.toString().trim())){
               addressStreetEditTextView.error = "enter street"
               addressStreetEditTextView.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userCity.toString().trim())){
               cityEditTextView.error = "enter city"
               cityEditTextView.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userZipCode.toString().trim())){
               userZipCodePasswordEditText.error = "enter zipCode"
               userZipCodePasswordEditText.requestFocus()
               return@setOnClickListener
           }
           if(TextUtils.isEmpty(userPhoneNumber.toString().trim())){
               userPhoneNumberEditText.error = "enter phone"
               userPhoneNumberEditText.requestFocus()
               return@setOnClickListener
           }
           scrollView.post {
                  scrollView.fling(viewHeight)
                   scrollView.fullScroll(View.FOCUS_DOWN)

           }
           progressBar.visibility= View.VISIBLE
          val signUp= AppSignUpModel(userEmail.toString(),username.toString(),userPassword.toString(), name(userFirstName.toString(),userLastName.toString()),
               address(userCity.toString(),userAddressStreet.toString(),userAddressNumber.toString(),userZipCode.toString(),geolocation("379292","00488488"))
               ,userPhoneNumber.toString())
           signUpUser(signUp)

           val gson=Gson()
          Log.d("Json", gson.toJson(signUp))
       }
    }
    //function to sign up user connecting to api using retrofit
    private fun   signUpUser( signUpModel: AppSignUpModel){

        WebApiClient.getWebApiInterface().signUpUser(signUpModel).enqueue(object:Callback<AppUserSignUpResponse>{
            override fun onResponse(
                call: Call<AppUserSignUpResponse>,
                response: Response<AppUserSignUpResponse>
            ) {
                if (response.code() == 200) {
                    storeUserLocally.edit {
                        putString("userId", response.body()?.id)
                        putString("username", response.body()?.username)
                        putString("userpassword", response.body()?.password)
                        apply()
                    }
                    Log.d("SignUp",storeUserLocally.getString("userId","")!!)
                    val intent=Intent(this@Signup_Activity,SignIn_Activity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            override fun onFailure(call: Call<AppUserSignUpResponse>, t: Throwable) {
                Log.d("SignUp",t.message!!)
                progressBar.visibility= View.GONE
                Toast.makeText(this@Signup_Activity,"failed try again",Toast.LENGTH_LONG).show()
            }

        })
    }

}