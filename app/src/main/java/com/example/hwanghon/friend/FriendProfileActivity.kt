package com.example.hwanghon.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

        FBRef.profileRef.child(uid).addValueEventListener(postListener)

        getImagedata(uid)

        val myuid = FBAuth.getUid()

        val posttwo = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val friendModel = dataSnapshot.getValue(FriendModel::class.java)
                if(friendModel?.frienduid.equals(uid)){
                        binding.friendremoveBtn.visibility= View.VISIBLE
                        binding.friendplusBtn.visibility= View.INVISIBLE
                    }
                    else{
                    binding.friendplusBtn.visibility= View.VISIBLE
                    binding.friendremoveBtn.visibility= View.INVISIBLE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.friendRef.child(myuid).child(uid).addValueEventListener(posttwo)


        binding.friendplusBtn.setOnClickListener {
            FBRef.friendRef
                .child(myuid)
                .child(uid)
                .setValue(FriendModel(uid))
            Toast.makeText(this, "친구가 추가 되었습니다", Toast.LENGTH_LONG).show()
            binding.friendplusBtn.visibility= View.INVISIBLE
            binding.friendremoveBtn.visibility= View.VISIBLE

        }


        binding.friendremoveBtn.setOnClickListener {
            //키값을 찾고
            FBRef.friendRef
                .child(myuid)
                .child(uid)
                .removeValue()
            Toast.makeText(this, "친구가 삭제 되었습니다", Toast.LENGTH_LONG).show()
            binding.friendremoveBtn.visibility= View.INVISIBLE
            binding.friendplusBtn.visibility= View.VISIBLE
        }

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