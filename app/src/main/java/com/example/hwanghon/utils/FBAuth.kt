package com.example.hwanghon.utils

import android.util.Log
import com.example.hwanghon.comment.CommentModel
import com.example.hwanghon.friend.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.ktx.storageMetadata
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object {

        private lateinit var auth: FirebaseAuth

        fun getUid() : String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun getAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        fun getTime() : String {

            val currentDataTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDataTime)

            return dateFormat

        }

        fun getNickName(uid : String) : String {

//            val postListener = object : ValueEventListener {
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                        val item = dataSnapshot.getValue(ProfileModel::class.java)
//                        val nickname = item?.nickname.toString()
//                    Log.d("FBAuth", nickname)
//
//
//                }
//                override fun onCancelled(databaseError: DatabaseError) {
//                }
//            }
            val uid = getUid()

//            FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()
            val aa = FBRef.profileRef.child(uid).getKey().toString()

            Log.d("FBAuth", aa)
            return aa

        }


    }



}

//            val postListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                    val profileModel = dataSnapshot.getValue(ProfileModel::class.java)
//
//                    val nickname = profileModel?.nickname
//
//                    println(nickname)
//
//                }
//                override fun onCancelled(databaseError: DatabaseError) {
//                }
//            }
//            val profileModel = dataSnapshot.getValue(ProfileModel::class.java)
//
//            val nickname = profileModel?.nickname

//
//            FBRef.profileRef.child(uid).addValueEventListener(postListener)