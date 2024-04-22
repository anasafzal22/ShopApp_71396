package com.onlineshop.app.app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.onlineshop.app.app.R
import com.onlineshop.app.app.app_webapi.WebApiClient
import com.onlineshop.app.app.api_response.AppSignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn_Activity : AppCompatActivity() {
    private  lateinit var usernameEditText: EditText
    private  lateinit var usernamePasswordEditText: EditText
    private  lateinit var  signupUserButton: Button
    private  lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_sign_in)
        findView()
    }
    private fun findView() {
        usernameEditText = findViewById(R.id.app_sign_in_username_edit_view)
        usernamePasswordEditText = findViewById(R.id.app_sign_in_password_edit_view)
        signupUserButton=findViewById(R.id.sign_in_button)
        progressBar=findViewById(R.id.app_sign_in_progress_bar)
        businessLogic()
    }

    private fun businessLogic() {
        supportActionBar?.hide()
        signupUserButton.setOnClickListener {
            val username = usernameEditText.text
            val userPassword = usernamePasswordEditText.text
            if (username.toString().trim().isEmpty()) {
                usernameEditText.error = "enter name"
                usernameEditText.requestFocus()
                return@setOnClickListener
            }
            if (userPassword.toString().trim().isEmpty()) {
                usernamePasswordEditText.error = "enter password"
                usernamePasswordEditText.requestFocus()
                return@setOnClickListener
            }
            progressBar.visibility= View.VISIBLE
            logUserIn(username.toString(),userPassword.toString())
        }
    }

    private fun logUserIn(username: String, password: String) {
     WebApiClient.getWebApiInterface().loginUser(username,password).enqueue(object:Callback<AppSignUpResponse>{
         override fun onResponse(call: Call<AppSignUpResponse>, response: Response<AppSignUpResponse>) {
             Log.d("SignIn",response.code().toString())
             if(response.code()==200){
                 getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("userToken",response.body()?.token).apply()
                 response.body()?.token?.let { Log.d("SignIn", it) }
                 getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("userAccName",usernameEditText.text.toString()).apply()
                 response.body()?.token?.let { Log.d("SignIn", it) }
                 val intent= Intent(this@SignIn_Activity,Home_Activity::class.java)
                 startActivity(intent)
                 finish()
                 progressBar.visibility= View.GONE
             }

         }

         override fun onFailure(call: Call<AppSignUpResponse>, t: Throwable) {
             Log.d("SignIn",t.message!!)
             progressBar.visibility= View.GONE
             Toast.makeText(this@SignIn_Activity,"failed try again",Toast.LENGTH_LONG).show()
         }

     })
    }

}