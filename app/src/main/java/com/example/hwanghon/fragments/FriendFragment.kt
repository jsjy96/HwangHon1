package com.example.hwanghon.fragments

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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardInsideActivity
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.board.BoardRVAdapter
import com.example.hwanghon.board.BoardWriteActivity
import com.example.hwanghon.comment.CommentModel
import com.example.hwanghon.databinding.FragmentChattingBinding
import com.example.hwanghon.databinding.FragmentFriendBinding
import com.example.hwanghon.friend.FriendModel
import com.example.hwanghon.friend.FriendRVAdapter
import com.example.hwanghon.friend.MyProfileActivity
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.setting.SettingActivity
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendFragment : Fragment() {

    private lateinit var binding : FragmentFriendBinding
    private lateinit var auth: FirebaseAuth

    private val friendDataList = mutableListOf<FriendModel>()

    lateinit var rvAdapter : FriendRVAdapter


    private val TAG = FriendFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false)

        binding.cardview1.setOnClickListener {
            val intent = Intent(context, MyProfileActivity::class.java)
            startActivity(intent)
        }

        rvAdapter = FriendRVAdapter(friendDataList)
        binding.friendRV.adapter = rvAdapter




//        uid = intent.getStringExtra("uid").toString()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profileModel = dataSnapshot.getValue(ProfileModel::class.java)

                binding.usernameArea.text = profileModel?.nickname

                if(profileModel?.profilemessage.toString().length > 0 ) {
                    binding.profilemessageArea!!.text = profileModel?.profilemessage
                }
                else {
                    binding.profilemessageArea.visibility = View.GONE

                }
//                binding.usernameArea.setText(profileModel?.nickname)
//                binding.profilemessageArea.setText(profileModel?.profilemessage)


            }

            override fun onCancelled(databaseError: DatabaseError) {
// Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        val uid = FBAuth.getUid()
        FBRef.profileRef.child(uid).addValueEventListener(postListener)
//        val title = intent.getStringExtra("title").toString()

//        val ProfileModel = dataSnapshot.getValue(ProfileModel::class.java)
//
//        binding.usernameArea.text = ProfileModel.nickname


//        fun bindItems(profileDataList : ProfileModel) {
//
//            val nickname = itemView.findViewById<TextView>(R.id.usernameArea)
//
//            nickname!!.text = profileDataList.nickname
//
//        }
        getImagedata(uid)

        getFBFriendData(uid)





        binding.chattingTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_friendFragment_to_chattingFragment)
        }

        binding.communityTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_friendFragment_to_freeboardFragment)
        }

        binding.lectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_friendFragment_to_lectureFragment)
        }



//        binding.logoutBtn.setOnClickListener {
//            FBAuth.getAuth().signOut()
//            val intent = Intent(context, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//
//        }



        return binding.root
    }

    private fun getImagedata(uid : String){

        val storageReference = Firebase.storage.reference.child(uid + ".png")

// ImageView in your Activity
        val imageViewFromFB = binding.profileimage
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        })

    }

    private fun getFBFriendData(uid:String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복돼서 나올 때 초기화
                friendDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(FriendModel::class.java)
                    friendDataList.add(item!!)

                }
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.friendRef.child(uid).addValueEventListener(postListener)
    }



}