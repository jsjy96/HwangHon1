package com.example.hwanghon.utils

import android.util.Log
import com.example.hwanghon.friend.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

        fun getNickName() : String {

            val uid = getUid()

            return FBRef.profileRef.child(uid).child(nickname).getKey().toString()
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