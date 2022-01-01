package com.example.hwanghon.board

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.comment.CommentModel
import com.example.hwanghon.comment.CommentRVAdapter
import com.example.hwanghon.databinding.ActivityBoardInsideBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key: String

    private val commentDataList = mutableListOf<CommentModel>()

    lateinit var rvAdapter : CommentRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

//첫번째 방법
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time

        //두번째방법

        key = intent.getStringExtra("key").toString()
        getBoarddata(key)
        getImagedata(key)


        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        rvAdapter = CommentRVAdapter(commentDataList)
        binding.commentRV.adapter = rvAdapter


            getCommentData(key)

    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복돼서 나올 때 초기화
//                boardDataList.clear()
//
                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)



    }


    fun insertComment(key : String){
        // comment
        //   - BoardKey
        //      - CommentKey
        //         - CommentData
        //         - CommentData
        //         - CommentData
        val comment = binding.commentArea.text.toString()
        if(comment.length > 0) {

            FBRef.commentRef
                .child(key)
                .push()
                .setValue(
                    CommentModel(
                        binding.commentArea.text.toString(),
                        FBAuth.getTime(),
                        FBAuth.getUid()
                    )
                )

            Toast.makeText(this, "댓글이 입력되었습니다", Toast.LENGTH_LONG).show()
            binding.commentArea.setText("")
        }
        else {
            Toast.makeText(this, "댓글을 한 글자 이상 작성해주세요", Toast.LENGTH_LONG).show()
        }

    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
//            Toast.makeText(this, "수정버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            finish()
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "게시글이 삭제되었습니다", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getImagedata(key : String){

        var storageReference1 = Firebase.storage.reference.child(key + "0.png")
        var storageReference2 = Firebase.storage.reference.child(key + "1.png")
        var storageReference3 = Firebase.storage.reference.child(key + "2.png")
        var storageReference4 = Firebase.storage.reference.child(key + "3.png")
        var storageReference5 = Firebase.storage.reference.child(key + "4.png")
        var storageReference6 = Firebase.storage.reference.child(key + "5.png")
        var storageReference7 = Firebase.storage.reference.child(key + "6.png")
        var storageReference8 = Firebase.storage.reference.child(key + "7.png")
        var storageReference9 = Firebase.storage.reference.child(key + "8.png")
        var storageReference10 = Firebase.storage.reference.child(key + "9.png")

// ImageView in your Activity
        val imageViewFromFB1 = binding.getImageArea1
        storageReference1.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB1)
            } else {binding.getImageArea1.isVisible = false}
        })
        val imageViewFromFB2 = binding.getImageArea2
        storageReference2.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB2)
            } else {binding.getImageArea2.isVisible = false}
        })
        val imageViewFromFB3 = binding.getImageArea3
        storageReference3.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB3)
            } else {binding.getImageArea3.isVisible = false}
        })
        val imageViewFromFB4 = binding.getImageArea4
        storageReference4.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB4)
            } else {binding.getImageArea4.isVisible = false}
        })
        val imageViewFromFB5 = binding.getImageArea5
        storageReference5.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB5)
            } else {binding.getImageArea5.isVisible = false}
        })
        val imageViewFromFB6 = binding.getImageArea6
        storageReference6.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB6)
            } else {binding.getImageArea6.isVisible = false}
        })
        val imageViewFromFB7 = binding.getImageArea7
        storageReference7.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB7)
            } else {binding.getImageArea7.isVisible = false}
        })
        val imageViewFromFB8 = binding.getImageArea8
        storageReference8.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB8)
            } else {binding.getImageArea8.isVisible = false}
        })
        val imageViewFromFB9 = binding.getImageArea9
        storageReference9.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB9)
            } else {binding.getImageArea9.isVisible = false}
        })
        val imageViewFromFB10 = binding.getImageArea10
        storageReference10.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB10)
            } else {binding.getImageArea10.isVisible = false}
        })
    }

    private fun getBoarddata(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                binding.titleArea.text = dataModel?.title
                binding.textArea.text = dataModel?.content
                binding.timeArea.text = dataModel?.time
                binding.usernameArea.text = dataModel?.nickname

                val myNickname = FBAuth.getNickName()
                val writerUid = dataModel?.nickname

                if(myNickname.equals(writerUid)){
                    binding.boardSettingIcon.isVisible = true
                } else {
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}