package com.example.hwanghon.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwanghon.R
import com.example.hwanghon.databinding.ActivityBoardWriteBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    var list = ArrayList<Uri>()
    val adapter = MultiImageAdapter(list, this)

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var imageUri = toString()

    private var imageNumber = toString()

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn1.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)

            val key = FBRef.boardRef.push().key.toString()

            if(title.length > 0) {

                if(content.length > 0) {
                    FBRef.boardRef
                        .child(key)
                        .setValue(BoardModel(title, content, uid, time, key))

                    Toast.makeText(this, "게시글이 입력되었습니다", Toast.LENGTH_LONG).show()

                    if (isImageUpload == true) {
                        imageUpload(key)
                    }

                    finish()
                }
                else {
                    Toast.makeText(this, "'내용'을 작성해주세요", Toast.LENGTH_LONG).show()


                }
            }

            else {
                Toast.makeText(this, "'제목'을 작성해주세요", Toast.LENGTH_LONG).show()
            }



        }

        var getImage_btn = findViewById<Button>(R.id.getImage)
        var recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        getImage_btn.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 100)
            isImageUpload = true
        }

        val layoutManager = GridLayoutManager(this, 2)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter

//        binding.imageArea.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, 100)
//            isImageUpload = true
//        }


    }
//
    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference


        var recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        for (idx in 0..recyclerview.layoutManager!!.itemCount - 1) {
            var view = recyclerview.layoutManager!!.findViewByPosition(idx)
            Log.d("테스트2", view.toString())
            var imageView = view!!.findViewById<ImageView>(R.id.image)
            Log.d("테스트1", imageView.toString())
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()

            val mountainsRef = storageRef.child(key + idx.toString() + ".png")
            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Log.d("성공", "성공")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
//            binding.imageArea.setImageURI(data?.data)
            list.clear()

            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if (count > 10) {
                    Toast.makeText(this, "사진은 10장까지 선택 가능합니다", Toast.LENGTH_LONG).show()
                    return
                }

                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    list.add(imageUri)
                }

            } else {
                data?.data?.let { uri ->
                    val imageUri : Uri? = data?.data
                    if (imageUri != null) {
                        list.add(imageUri)
                    }
                }
            }
        }

        adapter.notifyDataSetChanged()
    }
}