package com.david.myapplication.chat.message
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.david.myapplication.R
import com.david.myapplication.chat.list_history_messages.HistoryMessages
import com.david.myapplication.chat.data_model.ChatRequest
import com.david.myapplication.register_login.user_model.User
import com.david.myapplication.chat.message.view.MessageLeftVh
import com.david.myapplication.chat.message.view.MessageRightVh
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_message.*

class Message : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        toUser = intent.getParcelableExtra<User>("username")
        supportActionBar?.title = toUser?.username

        recyclerview_chat_log.adapter = adapter

        listenToMessage()

        send_button_chat_log.setOnClickListener {
            d("ChatLog","sent!")
            sendMessage()
        }
    }

    private fun listenToMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0 : DataSnapshot, p1 : String?) {
                val chatMessage= p0.getValue(ChatRequest::class.java)
                if (chatMessage != null) {
                    d("ChatLog",chatMessage.text)

                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = HistoryMessages.currentUser
                            ?: return
                        adapter.add(MessageRightVh(chatMessage.text, currentUser))
                    }
                    else{ adapter.add(MessageLeftVh(chatMessage.text, toUser!!))
                    }
                }
                recyclerview_chat_log.scrollToPosition(adapter.itemCount -1)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun sendMessage()  {

        val text = message_editText_chat_log.text.toString()
        if(text.isEmpty())return
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>("username")
        val toId = user.uid

        if(fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatRequest(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                d("ChatLog","Saved message ${reference.key}")
                message_editText_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount -1)
            }

        toReference.setValue(chatMessage)

        val  latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
         latestMessageReference.setValue(chatMessage)

        val  latestMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToReference.setValue(chatMessage)

    }
}


