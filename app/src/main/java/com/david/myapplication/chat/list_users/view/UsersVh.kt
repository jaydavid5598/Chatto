package com.david.myapplication.chat.list_users.view

import com.david.myapplication.R
import com.david.myapplication.register_login.user_model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activty_row_message.view.*

class UsersVh(val user : User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.profile_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.activty_row_message
    }
}
