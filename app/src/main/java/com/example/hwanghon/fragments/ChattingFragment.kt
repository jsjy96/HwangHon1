package com.example.hwanghon.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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





//        binding.logoutBtn.setOnClickListener {
//            FBAuth.getAuth().signOut()
//            val intent = Intent(context, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//
//        }

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
            var textView_title : TextView = itemView.findViewById(R.id.usernameArea)
            val textView_lastMessage : TextView = itemView.findViewById(R.id.lastmessageArea)

        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            var destinationUid: String? = null
        //채팅방에 있는 유저 모두 체크
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
                    val friend = snapshot.getValue<FriendModel>()
                    val frienduid = friend?.frienduid
                    val storageReference = Firebase.storage.reference.child(frienduid + ".png")

                    Glide.with(holder.itemView.context)
                        .load(storageReference)
                        .into(holder.imageView)

                    getNickName(frienduid.toString())

                }
            })
        //메세지 내림차순 정렬 후 마지막 메세지의 키값을 가져
            val commentMap = TreeMap<String, ChatModel.Comment>(reverseOrder())
            commentMap.putAll(chatModel[position].comments)
//            val lastMessageKey = commentMap.keys.toTypedArray()[0]
//            holder.textView_lastMessage.text = chatModel[position].comments[lastMessageKey]?.message
        //채팅창 선책 시 이동
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChattingRoomActivity::class.java)
                intent.putExtra("destinationUid", destinationUsers[position])
                context?.startActivity(intent)
            }
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

}