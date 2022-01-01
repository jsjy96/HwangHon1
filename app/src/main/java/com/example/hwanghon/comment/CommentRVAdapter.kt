package com.example.hwanghon.comment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CommentRVAdapter(private val commentDataList: MutableList<CommentModel>, val context : Context) :
        RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(commentDataList[position])

    }

    override fun getItemCount(): Int {
        return commentDataList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(commentDataList : CommentModel) {

            val title = itemView.findViewById<TextView>(R.id.titleArea)
            val time = itemView.findViewById<TextView>(R.id.timeArea)
            val uid = commentDataList.uid

            title!!.text = commentDataList.commentTitle
            time!!.text = commentDataList.commentCreatedTime

            getNickName(uid)

            val storageReference = Firebase.storage.reference.child(uid + ".png")

// ImageView in your Activity
            val imageViewFromFB : ImageView = itemView.findViewById(R.id.userimage)
            storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                if(task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(imageViewFromFB)
                } else {

                }
            })

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



    }


}