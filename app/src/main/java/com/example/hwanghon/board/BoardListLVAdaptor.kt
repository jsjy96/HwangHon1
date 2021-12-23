package com.example.hwanghon.board

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
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.utils.FBAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.properties.Delegates

class BoardListLVAdaptor(private val boardList : MutableList<BoardModel>) : BaseAdapter() {


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
        val storageRef = Firebase.storage.reference


        if (view == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.activity_board_list_lvadaptor, parent, false)
    }

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        if(boardList[position].uid.equals(FBAuth.getUid())){
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        }
//
        val title = view?.findViewById<TextView>(R.id.titleArea)
        title?.text = boardList[position].title

        val content = view?.findViewById<TextView>(R.id.contentArea)
        content?.text = boardList[position].content

        val time = view?.findViewById<TextView>(R.id.timeArea)
        time?.text = boardList[position].time



        val image1 = view?.findViewById<ImageView>(R.id.image1Area)
        storageRef.child(boardList[position].key+"0"+ ".png").downloadUrl.addOnSuccessListener {
           Glide.with(this)
               .load(storageRef)
               .into(image1)

        }.addOnFailureListener {
            // Handle any errors
        }



        val image2 = view?.findViewById<ImageView>(R.id.image2Area)
        storageRef.child(boardList[position].key+"1"+ ".png").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
        }.addOnFailureListener {
            // Handle any errors
        }
        val image3 = view?.findViewById<ImageView>(R.id.image3Area)
        storageRef.child(boardList[position].key+"2"+ ".png").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
        }.addOnFailureListener {
            // Handle any errors
        }
        return view!!
    }
}