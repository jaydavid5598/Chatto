package com.david.myapplication.register_login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.david.myapplication.chat.ChatsLatest
import com.david.myapplication.R
import com.david.myapplication.extension.hideKeyboard
import com.david.myapplication.model.user_model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Register"

        register_button_register.setOnClickListener {
          register()
        }

        already_have_an_account_tv_register.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        choose_profile_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
            d("RegisterActivity","Choose Profile Picture")
        }
    }


    private var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            d("RegisterActivity", "Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            choose_profile_civ_register.setImageBitmap(bitmap)
            choose_profile_button_register.alpha = 0f
        }
    }

    private fun register() {
        val email = email_et_register.text.toString()
        val password = password_et_register.text.toString()
        val username = username_et_register.text.toString()
        val profile = choose_profile_civ_register.drawable

        when {
            profile == null -> {
                Toast.makeText(this,"Please insert an image",Toast.LENGTH_SHORT).show()
                return
            }username.isEmpty() -> {
                Toast.makeText(this,"Please insert username",Toast.LENGTH_SHORT).show()
                return
            }email.isEmpty() -> {
                Toast.makeText(this,"Please insert email",Toast.LENGTH_SHORT).show()
                return
            }password.isEmpty() -> {
                Toast.makeText(this,"Please insert password",Toast.LENGTH_SHORT).show()
                return
            }else -> {
                d("RegisterActivity", "Email is : $email")
                d("RegisterActivity", "Password is: $password")

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            uploadImageToFirebaseStorage()
                        } else {
                            Log.w("Register", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
//                    .addOnCompleteListener {
//                        if (!it.isSuccessful) return@addOnCompleteListener
//                        d(
//                            "RegisterActivity",
//                            "Successfully created user uid: ${it.result?.user?.uid}"
//                        )
//                        uploadImageToFirebaseStorage()
//                    }
//                    .addOnFailureListener {
//                        d("RegisterActivity", "Failed to create user: ${it.message}")
//                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
//                    }
            }
        }
        }

    }

    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUri == null) return

        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    d("RegisterActivity","Successfully uploaded image:${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        d("RegisterActivity","File Location: $uri")
                        saveUserToFirebaseDatabase(uri.toString())
                    }
                }
                .addOnFailureListener{
                    d("RegisterActivity","Failed to upload image to firebase storage: ${it.message}")
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String) {
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,username_et_register.text.toString(),profileImageUrl,password_et_register.text.toString())
        ref.setValue(user)
            .addOnSuccessListener {
                d("RegisterActivity","Successfully saved user to firebase database")
                val intent = Intent(this, ChatsLatest::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                d("RegisterActivity","Unsuccessful to saved user to firebase database: ${it.message}")
            }
    }
}
