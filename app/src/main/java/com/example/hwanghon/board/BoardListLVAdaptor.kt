//package com.example.hwanghon.board

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.utils.FBAuth

class BoardListLVAdaptor(val boardList : MutableList<BoardModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
//        if (view == null) {
        view = LayoutInflater.from(parent?.context).inflate(R.layout.activity_board_list_lvadaptor, parent, false)
//        }

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        if(boardList[position].uid.equals(FBAuth.getUid())){
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        }
//
//        val title = view?.findViewById<TextView>(R.id.titleArea)
//        title!!.text = boardList[position].title
//
//        val content = view?.findViewById<TextView>(R.id.contentArea)
//        content!!.text = boardList[position].content
//
//        val time = view?.findViewById<TextView>(R.id.timeArea)
//        time!!.text = boardList[position].time

        return view!!
    }
}