package com.david.myapplication.chat.message.view

import com.david.myapplication.R
import com.david.myapplication.register_login.user_model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_message_left_vh.view.*

class MessageLeftVh(val text: String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_et_chat_log_left.text = text
        val uri = user.profileImageUrl
        val miniProfile = viewHolder.itemView.mini_profile_chat_log_left
        Picasso.get().load(uri).into(miniProfile)

    }
    override fun getLayout(): Int {
        return R.layout.activity_message_left_vh
    }
}