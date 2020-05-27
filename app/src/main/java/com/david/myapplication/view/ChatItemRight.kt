package com.david.myapplication.view

import com.david.myapplication.R
import com.david.myapplication.model.user_model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_log_right_row.view.*


class ChatItemRight(val text: String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_et_chat_log_right.text = text
        val uri = user.profileImageUrl
        val miniProfile = viewHolder.itemView.mini_profile_chat_log_right
        Picasso.get().load(uri).into(miniProfile)
    }
    override fun getLayout(): Int {
        return R.layout.chat_log_right_row
    }
}
