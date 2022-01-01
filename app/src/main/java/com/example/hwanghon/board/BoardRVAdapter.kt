package com.example.hwanghon.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.comment.CommentModel
import com.example.hwanghon.databinding.ActivityBoardWriteBinding
import com.example.hwanghon.friend.ProfileModel
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

    override fun getItemViewType(position: Int): Int {
        return position
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

            val key = boardDataList.key
            val uid = boardDataList.uid

            title!!.text = boardDataList.title
            time!!.text = boardDataList.time
            content!!.text = boardDataList.content

            getNickName(uid)

            val storageRef1 = Firebase.storage.reference.child(key + "0.png")
            val storageRef2 = Firebase.storage.reference.child(key + "1.png")
            val storageRef3 = Firebase.storage.reference.child(key + "2.png")
            val storageRef4 = Firebase.storage.reference.child(key + "3.png")
            val image1: ImageView =itemView.findViewById(R.id.image1Area)
            val image2: ImageView =itemView.findViewById(R.id.image2Area)
            val image3: ImageView =itemView.findViewById(R.id.image3Area)
            val moretext = itemView.findViewById<TextView>(R.id.moreText)
            val imageArea = itemView.findViewById<LinearLayout>(R.id.imageArea)
            val image3layout = itemView.findViewById<FrameLayout>(R.id.image3layout)


            storageRef1.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                if(task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(image1)
                    image1.visibility=View.VISIBLE
                    storageRef2.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                        if(task.isSuccessful) {
                            Glide.with(context)
                                .load(task.result)
                                .into(image2)
                            image2.visibility=View.VISIBLE
                            storageRef3.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                                if(task.isSuccessful) {
                                    Glide.with(context)
                                        .load(task.result)
                                        .into(image3)
                                    image3layout.visibility=View.VISIBLE
                                    storageRef4.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
                                        if(task.isSuccessful) {
                                            moretext.visibility=View.VISIBLE
                                        } else {
                                            moretext.visibility=View.GONE
                                            //더보기, 블러 빼줘
                                        }
                                    })

                                } else {
                                    moretext.visibility=View.GONE
                                    image3layout.visibility=View.GONE
                                    //3, 더보기, 블러 삭제
                                }
                            })
                        } else {
                            moretext.visibility=View.GONE
                            image3layout.visibility=View.GONE
                            image2.visibility=View.GONE
                            //2,3,더보기, 블러 삭제
                        }
                    })
                } else {
                    imageArea.visibility=View.GONE
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

    interface OnItemClickListener{
        fun onItemClick(v:View, data: BoardModel, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }



}