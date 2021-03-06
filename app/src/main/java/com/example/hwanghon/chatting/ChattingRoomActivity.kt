package com.example.hwanghon.chatting

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hwanghon.R
import com.example.hwanghon.databinding.ActivityChattingRoomBinding
import com.example.hwanghon.fragments.ChattingFragment
import com.example.hwanghon.friend.FriendModel
import com.example.hwanghon.friend.FriendProfileActivity
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.text.SimpleDateFormat
import java.util.*

class ChattingRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChattingRoomBinding

    private lateinit var frienduid: String
    private lateinit var myuid: String
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var chatRoomUid : String? = null
    private var destinationUid : String? = null
    private var uid : String? = null
    private var recyclerView : RecyclerView? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_room)

        myuid = intent.getStringExtra("myuid").toString()
        frienduid = intent.getStringExtra("frienduid").toString()

        uid = Firebase.auth.currentUser?.uid.toString()
        recyclerView = findViewById(R.id.chatRV)


        binding.sendBtn.setOnClickListener {
            val chatModel = ChatModel()
            chatModel.users.put(uid.toString(), true)
            chatModel.users.put(frienduid, true)

            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("MM??? dd??? hh:mm")
            val curTime = dateFormat.format(Date(time)).toString()

            val comment = ChatModel.Comment(uid, binding.messageArea.text.toString(), curTime)
            if(binding.messageArea.length()>0) {

                if (chatRoomUid == null) {
                    binding.sendBtn.isEnabled = false
                    fireDatabase.child("chat").push().setValue(chatModel).addOnSuccessListener {
                        //????????? ??????
                        checkChatRoom()
                        //????????? ?????????
                        Handler().postDelayed({
                            println(chatRoomUid)
                            fireDatabase.child("chat").child(chatRoomUid.toString())
                                .child("comments").push().setValue(comment)
                            binding.messageArea.text = null
                        }, 500L)
                        Log.d("chatUidNull dest", "$frienduid")
                    }

                } else {
                    fireDatabase.child("chat").child(chatRoomUid.toString()).child("comments")
                        .push().setValue(comment)
                    binding.messageArea.text = null
                    Log.d("chatUidNotNull dest", "$frienduid")

                }
            }
            else{}

        }
        checkChatRoom()

        getNickName(frienduid)
        RecyclerViewAdapter().notifyDataSetChanged()
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(ChatLeft())
//        adapter.add(ChatRight())
//
//        binding.chatRV.adapter = adapter


    }
    private fun checkChatRoom(){
        fireDatabase.child("chat").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        println(item)
                        val chatModel = item.getValue<ChatModel>()
                        if(chatModel?.users!!.containsKey(frienduid)){
                            chatRoomUid = item.key
                            binding.sendBtn.isEnabled = true
                            recyclerView?.layoutManager = LinearLayoutManager(this@ChattingRoomActivity)
                            recyclerView?.adapter = RecyclerViewAdapter()

                            RecyclerViewAdapter().notifyDataSetChanged()
                        }
                    }
                }

            })
    }
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder>() {
        private val comments = ArrayList<ChatModel.Comment>()
        private var friend : FriendModel? = null
        init{
            fireDatabase.child("users").child(frienduid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    friend = snapshot.getValue<FriendModel>()
                    getMessageList()
                }
            })
        }
        fun getMessageList(){
            fireDatabase.child("chat").child(chatRoomUid.toString()).child("comments").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    for(data in snapshot.children){
                        val item = data.getValue<ChatModel.Comment>()
                        comments.add(item!!)
                        println(comments)
                    }

                    notifyDataSetChanged()
                //???????????? ?????? ??? ????????? ??? ????????? ??????
                recyclerView?.scrollToPosition(comments.size - 1)
                }
            })
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)

            return MessageViewHolder(view)
        }

        @SuppressLint("RtlHardcoded")
        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.textView_message.textSize = 20F
            holder.textView_message.text = comments[position].message
            holder.textView_time.text = comments[position].time
            val storageReference = Firebase.storage.reference.child(frienduid + ".png")
            if(comments[position].uid.equals(uid)){ // ?????? ??????
                holder.textView_message.setBackgroundResource(R.drawable.background_radius_yellow)
                holder.textView_name.visibility = View.GONE
                holder.imageView_profile.visibility = View.GONE
//                holder.layout_destination.visibility = View.INVISIBLE
                holder.layout.gravity = Gravity.RIGHT
            }else{ // ????????? ??????
                storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(holder.itemView.context)
                            .load(task.result)
                            .into(holder.imageView_profile)
                    } else {}
                })
                holder.textView_name.text = binding.friendname.text
//                holder.layout_destination.visibility = View.VISIBLE
                holder.textView_name.visibility = View.VISIBLE
                holder.imageView_profile.visibility = View.VISIBLE
                holder.textView_message.setBackgroundResource(R.drawable.background_radius)
                holder.layout.gravity = Gravity.LEFT
                holder.imageView_profile.setOnClickListener {
                    val intent = Intent(holder.itemView?.context, FriendProfileActivity::class.java)
                    intent.putExtra("uid", frienduid)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
            }
        }
        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView_message: TextView = view.findViewById(R.id.commentArea)
            val textView_name: TextView = view.findViewById(R.id.usernameArea)
            val imageView_profile: ImageView = view.findViewById(R.id.imageView)
//            val layout_destination: LinearLayout = view.findViewById(R.id.messageItem_layout_destination)
//            val layout_main: LinearLayout = view.findViewById(R.id.messageItem_linearlayout_main)
            val textView_time : TextView = view.findViewById(R.id.timeArea)
            val layout : LinearLayout = view.findViewById(R.id.layout)

        }
        override fun getItemCount(): Int {
            return comments.size
        }

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
