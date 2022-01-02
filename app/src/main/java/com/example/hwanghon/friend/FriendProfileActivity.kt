package com.example.hwanghon.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.databinding.ActivityFriendProfileBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FriendProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendProfileBinding

    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_profile)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profileModel = dataSnapshot.getValue(ProfileModel::class.java)

                binding.usernameArea.setText(profileModel?.nickname)
                binding.profilemessage.setText(profileModel?.profilemessage)


            }

            override fun onCancelled(databaseError: DatabaseError) {


            }

        }
        uid = intent.getStringExtra("uid").toString()
        //여기 uid를 눌렀을때 보내주는 uid로 교체
        FBRef.profileRef.child(uid).addValueEventListener(postListener)

        getImagedata(uid)

    }
    private fun getImagedata(uid : String){

        val storageReference = Firebase.storage.reference.child(uid + ".png")

// ImageView in your Activity
        val imageViewFromFB = binding.profileimageArea
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        })

    }
}