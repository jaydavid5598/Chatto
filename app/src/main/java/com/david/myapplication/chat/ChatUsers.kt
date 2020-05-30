package com.david.myapplication.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.david.myapplication.R
import com.david.myapplication.register_login.user_model.User
import com.david.myapplication.chat.view.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class ChatUsers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "All People"

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    d("NewMessage",it.toString())
                    val user = it.getValue(User::class.java)
                   if(user != null){
                       adapter.add(UserItem(user))
                   }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context,ChatLog::class.java)
                    intent.putExtra("username",userItem.user)
                    startActivity(intent)
                    finish()

                }
                recyclerview_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                d("NewMessage",p0.message)
            }
        })
    }
}
