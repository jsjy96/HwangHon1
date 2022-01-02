package com.example.hwanghon.friend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.comment.CommentModel
import com.example.hwanghon.comment.CommentRVAdapter
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FriendRVAdapter(private val friendDataList: MutableList<FriendModel>) :
    RecyclerView.Adapter<FriendRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FriendRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(holder, friendDataList[position])
    }

    override fun getItemCount(): Int {
        return friendDataList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(holder: FriendRVAdapter.ViewHolder, friendDataList : FriendModel) {

            val username = itemView.findViewById<TextView>(R.id.usernameArea)
            val profilemessage = itemView.findViewById<TextView>(R.id.profilemessageArea)
            val uid = friendDataList.frienduid

            getNickName(uid)
            getProfilemessage(uid)
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


            val authuid = FBAuth.getUid()

            val friendprofile = itemView.findViewById<CardView>(R.id.cardview1)
            friendprofile.setOnClickListener {

                    val intent = Intent(holder.itemView?.context, FriendProfileActivity::class.java)
                    intent.putExtra("uid", uid)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)

            }


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

        private fun getProfilemessage(uid: String) {

            val postListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val item = dataSnapshot.getValue(ProfileModel::class.java)
//                    val nickname = item?.nickname.toString()
                    val profilemessage = itemView.findViewById<TextView>(R.id.profilemessageArea)
//                binding.usernameArea.text = nickname
                    if(item?.profilemessage.toString().length > 0) {
                        profilemessage!!.text = item?.profilemessage
                    }
                    else{
                        profilemessage.visibility = View.GONE
                    }


                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }

            FBRef.profileRef.child(uid).addValueEventListener(postListener).toString()

        }
    }

}