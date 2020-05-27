package com.david.myapplication.register_login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.myapplication.R
import com.david.myapplication.chat.ChatsLatest
import com.david.myapplication.extension.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Login"

        login_button_login.setOnClickListener {
            val email = email_et_login.text.toString()
            val password = password_et_login.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this,"Please insert email", Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty()){
                Toast.makeText(this,"Please insert password", Toast.LENGTH_SHORT).show()
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener
                    val intent = Intent(this, ChatsLatest::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }
        }

        back_to_registration_tv_login.setOnClickListener {
            finish()
        }
    }
}