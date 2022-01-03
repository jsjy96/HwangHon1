package com.example.hwanghon.chatting

import com.example.hwanghon.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatRight() : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_right
    }


}