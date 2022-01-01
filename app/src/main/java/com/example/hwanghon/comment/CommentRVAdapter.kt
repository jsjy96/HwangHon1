package com.example.hwanghon.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwanghon.R
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommentRVAdapter(private val commentDataList : MutableList<CommentModel>) :
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