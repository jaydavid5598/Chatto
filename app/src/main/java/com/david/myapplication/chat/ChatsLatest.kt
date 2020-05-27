package com.david.myapplication.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.david.myapplication.R
import com.david.myapplication.model.chat_model.ChatMessage
import com.david.myapplication.model.user_model.User
import com.david.myapplication.register_login.Register
import com.david.myapplication.view.LatestMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*

class ChatsLatest : AppCompatActivity() {
companion object{
    var currentUser: User? = null
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)
        supportActionBar?.title = "Chats"
        verifyIfUserIsLoggedIn()

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this,ChatLog::class.java)
            val row = item as LatestMessage
            row.chatPartnerUser
            intent.putExtra("username",row.chatPartnerUser)
            startActivity(intent)
        }

        listenForLatestMessages()
        fetchUser()

    }

    val latestMessagesMap = HashMap<String,ChatMessage>()

    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessage(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?:return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?:return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    val adapter = GroupAdapter<ViewHolder>()

    private fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun verifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, Register::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Register::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_new_messages -> {
                val intent = Intent(this, ChatUsers::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
