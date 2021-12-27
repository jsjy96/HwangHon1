package com.example.hwanghon.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.comment.CommentModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardRVAdapter(private val boardDataList : MutableList<BoardModel>, val context : Context) :
    RecyclerView.Adapter<BoardRVAdapter.ViewHolder>() {

//    private lateinit var key: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_board_list_lvadaptor, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BoardRVAdapter.ViewHolder, position: Int) {

        holder.bindItems(boardDataList[position])
    }

    override fun getItemCount(): Int {

        return boardDataList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(boardDataList : BoardModel) {
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,boardDataList,pos)
                }
            }

            val title = itemView.findViewById<TextView>(R.id.titleArea)
            val time = itemView.findViewById<TextView>(R.id.timeArea)
            val content = itemView.findViewById<TextView>(R.id.contentArea)
            val username = itemView.findViewById<TextView>(R.id.usernameArea)
            val key = boardDataList.key

            title!!.text = boardDataList.title
            time!!.text = boardDataList.time
            content!!.text = boardDataList.content
            username!!.text = boardDataList.uid

            val storageRef1 = Firebase.storage.reference.child(key + "0.png")
            val storageRef2 = Firebase.storage.reference.child(key + "1.png")
            val storageRef3 = Firebase.storage.reference.child(key + "2.png")
            val image1: ImageView =itemView.findViewById(R.id.image1Area)
            val image2: ImageView =itemView.findViewById(R.id.image2Area)
            val image3: ImageView =itemView.findViewById(R.id.image3Area)

            storageRef1.downloadUrl.addOnCompleteListener({ task ->
            if(task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .into(image1)
            } else {
            }
            })
            storageRef2.downloadUrl.addOnCompleteListener({ task ->
            if(task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .into(image2)
            } else {
            }
            })
            storageRef3.downloadUrl.addOnCompleteListener({ task ->
            if(task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .into(image3)
            } else {
            }
            })

//            val storageRef = Firebase.storage.reference.child(key + "0.png")
//
//            val image1 = itemView.findViewById<ImageView>(R.id.image1Area)
//            storageRef.downloadUrl.addOnCompleteListener ({ task ->
//            if(task.isSuccessful) {
//                Glide.with(context)
//                    .load(storageRef)
//                    .into(image1)
//            } else {
//
//            }
//        })



        }

    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: BoardModel, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

}