package com.example.hwanghon.chatting

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.friend.FriendModel
import com.example.hwanghon.friend.FriendProfileActivity
import com.example.hwanghon.friend.FriendRVAdapter
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ChatListRVAdapter (private val chatDataList: MutableList<ChatModel>) :
    RecyclerView.Adapter<ChatListRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chatting_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatListRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(holder, chatDataList[position])
    }

    override fun getItemCount(): Int {
        return chatDataList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(holder: ChatListRVAdapter.ViewHolder, chatDataList : ChatModel) {

            val uid = chatDataList.frienduid
            val lastmessage = chatDataList.lastmessage
            val lastmessagetime = chatDataList.lastmessagetime

            getNickName(uid)
            //getLastMessage(lastmessage)
            //getLastMessageTime(lastmessagetime)

            val storageReference = Firebase.storage.reference.child(uid + ".png")

// ImageView in your Activity
            val imageViewFromFB : ImageView = itemView.findViewById(R.id.profileimage)
            storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                if(task.isSuccessful) {
                    Glide.with(itemView)
                        .load(task.result)
                        .into(imageViewFromFB)
                } else {

                }
            })


        }

        private fun getNickName(uid: String) {

            val postListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val item = dataSnapshot.getValue(ProfileModel::class.java)
//                    val nickname = item?.nickname.toString()
                    val username = itemView.findViewById<TextView>(R.id.usernameArea)
//                binding.usernameArea.text = nickname
                    username!!.text = item?.nickname



                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }

            FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()

        }

    }

}