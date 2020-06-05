package com.david.myapplication.chat.list_users

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.david.myapplication.R
import com.david.myapplication.chat.list_users.view.UsersVh
import com.david.myapplication.chat.message.Message
import com.david.myapplication.register_login.user_model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_users.*

class Users : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        supportActionBar?.title = "All People"

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UsersVh(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UsersVh
                    val intent = Intent(
                        view.context,
                        Message::class.java
                    )
                    intent.putExtra("username", userItem.user)
                    startActivity(intent)
                    finish()

                }
                recyclerview_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                d("NewMessage", p0.message)
            }
        })
    }
}
