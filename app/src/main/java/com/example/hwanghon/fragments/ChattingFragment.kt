package com.example.hwanghon.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardInsideActivity
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.board.BoardRVAdapter
import com.example.hwanghon.chatting.*
import com.example.hwanghon.databinding.FragmentChattingBinding
import com.example.hwanghon.databinding.FragmentFriendBinding
import com.example.hwanghon.friend.FriendModel
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBAuth.Companion.getNickName
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
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
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChattingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChattingFragment : Fragment() {
    companion object{ fun newInstance() : ChattingFragment { return ChattingFragment() } }

    private lateinit var binding : FragmentChattingBinding
    private lateinit var auth: FirebaseAuth

    private val fireDatabase = FirebaseDatabase.getInstance().reference

    private val chatDataList = mutableListOf<ChatModel>()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) { super.onAttach(context) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatting, container, false)

        binding.friendTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_friendFragment)
        }

        binding.communityTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_freeboardFragment)
        }

        binding.lectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_lectureFragment)
        }



        val recyclerView = binding.chatlistRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter()

        //getFBChatData()

        return binding.root
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
        private val chatModel = ArrayList<ChatModel>()
        private var uid : String? = null
        private val destinationUsers : ArrayList<String> = arrayListOf()

        init {
            uid = Firebase.auth.currentUser?.uid.toString()
            println(uid)

            fireDatabase.child("chat").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatModel.clear()
                    for(data in snapshot.children){
                        chatModel.add(data.getValue<ChatModel>()!!)
                        println(data)
                    }
                    notifyDataSetChanged()
                }
            })
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.chatting_list_item, parent, false))
        }
        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.profileimage)
            var textView_name : TextView = itemView.findViewById(R.id.usernameArea)
            val textView_lastMessage : TextView = itemView.findViewById(R.id.lastmessageArea)
            val textView_lastMessagetime : TextView = itemView.findViewById(R.id.lastmessageTime)

        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            var destinationUid: String? = null
        //???????????? ?????? ?????? ?????? ??????
            for (user in chatModel[position].users.keys) {
                if (!user.equals(uid)) {
                    destinationUid = user
                    destinationUsers.add(destinationUid)
                }
            }
            fireDatabase.child("users").child("$destinationUid").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val friend = snapshot.getValue<FriendModel>()
//                    val frienduid = friend?.frienduid
                    val storageReference = Firebase.storage.reference.child(destinationUid + ".png")

                    storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Glide.with(holder.itemView.context)
                                .load(task.result)
                                .into(holder.imageView)
                        } else {}
                    })
//                    holder.textView_title.text=destinationUid
//                    getNickName(destinationUid.toString())
                }
            })
        //????????? ???????????? ?????? ??? ????????? ???????????? ????????? ??????
            val commentMap = TreeMap<String, ChatModel.Comment>(reverseOrder())
            commentMap.putAll(chatModel[position].comments)
            val lastMessageKey = commentMap.keys.toTypedArray()[0]
            holder.textView_lastMessage.text = chatModel[position].comments[lastMessageKey]?.message
            holder.textView_lastMessagetime.text = chatModel[position].comments[lastMessageKey]?.time
        //????????? ?????? ??? ??????
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChattingRoomActivity::class.java)
                intent.putExtra("frienduid", destinationUsers[position])
                context?.startActivity(intent)
            }
            val postListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val item = dataSnapshot.getValue(ProfileModel::class.java)
//                    val nickname = item?.nickname.toString()

//                binding.usernameArea.text = nickname
                    holder.textView_name.text = item?.nickname



                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }

            FBRef.profileRef.child(destinationUsers[position]).addValueEventListener(postListener).toString()

//            fun s(uid: String) {
//
//                val postListener = object : ValueEventListener {
//
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                        val item = dataSnapshot.getValue(ProfileModel::class.java)
//                        val nickname = item?.nickname.toString()
//
//                        holder.textView_title.text = nickname
//
//                    }
//                    override fun onCancelled(databaseError: DatabaseError) {
//                    }
//                }
//
//                FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()
//
//            }
        }
        override fun getItemCount(): Int {
            return chatModel.size
        }


    }
    private fun getNickName(uid: String) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val item = dataSnapshot.getValue(ProfileModel::class.java)
//                    val nickname = item?.nickname.toString()
                val username = view?.findViewById<TextView>(R.id.usernameArea)
//                binding.usernameArea.text = nickname
                username!!.text = item?.nickname



            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()

    }


}