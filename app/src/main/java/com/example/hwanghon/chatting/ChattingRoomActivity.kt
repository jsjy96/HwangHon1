package com.example.hwanghon.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hwanghon.R
import com.example.hwanghon.databinding.ActivityChattingRoomBinding
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChattingRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChattingRoomBinding

    private lateinit var frienduid: String
    private lateinit var myuid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_room)

        myuid = intent.getStringExtra("myuid").toString()
        frienduid = intent.getStringExtra("frienduid").toString()

        getNickName(frienduid)

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatLeft())
        adapter.add(ChatRight())

        binding.chatRV.adapter = adapter
    }

    private fun getNickName(uid: String) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val item = dataSnapshot.getValue(ProfileModel::class.java)
                val nickname = item?.nickname.toString()

                binding.friendname.text = nickname

            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()

    }
}