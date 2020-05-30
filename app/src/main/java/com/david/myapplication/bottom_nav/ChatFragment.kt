package com.david.myapplication.bottom_nav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.david.myapplication.R
import com.david.myapplication.chat.ChatLog
import com.david.myapplication.model.chat_model.ChatMessage
import com.david.myapplication.model.user_model.User
import com.david.myapplication.register_login.Login
import com.david.myapplication.view.LatestMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.frag_chat.*


class ChatFragment : Fragment(){
    companion object{
        var currentUser: User? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_chat, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Chats"

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(activity, ChatLog::class.java)
            val row = item as LatestMessage
            row.chatPartnerUser
            intent.putExtra("username",row.chatPartnerUser)
            startActivity(intent)
        }
        verifyIfUserIsLoggedIn()
        listenForLatestMessages()
        fetchUser()
        userLoggedProfile()
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessage(it))
        }
    }

    private fun verifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
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

    private val adapter = GroupAdapter<ViewHolder>()

    private fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun userLoggedProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val ref = FirebaseDatabase.getInstance().getReference("users");
        if (userId != null) {
            ref.child(userId).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val userProfile = p0.getValue(User::class.java)
                    user_name.text = userProfile?.username
                    Picasso.get().load(userProfile?.profileImageUrl).into(user_prof)
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

//putangina lord
}