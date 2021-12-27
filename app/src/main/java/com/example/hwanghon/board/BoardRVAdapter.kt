package com.example.hwanghon.board

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwanghon.R
import com.example.hwanghon.comment.CommentModel

class BoardRVAdapter(private val boardList : MutableList<BoardModel>) :
    RecyclerView.Adapter<BoardRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_board_list_lvadaptor, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BoardRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(boardList[position])
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(boardList : BoardModel) {
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,boardList,pos)
                }
            }

            val title = itemView.findViewById<TextView>(R.id.titleArea)
            val time = itemView.findViewById<TextView>(R.id.timeArea)
            val content = itemView.findViewById<TextView>(R.id.contentArea)
            val username = itemView.findViewById<TextView>(R.id.usernameArea)



            title!!.text = boardList.title
            time!!.text = boardList.time
            content!!.text = boardList.content
            username!!.text = boardList.uid



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