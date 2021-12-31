package com.example.hwanghon.friend

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardEditActivity
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.databinding.ActivityMyProfileBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.github.drjacky.imagepicker.ImagePicker

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        binding.imagechangeBtn.setOnClickListener {
            showDialog()
        }
//        binding.editBtn.setOnClickListener {
//
//            val profileMessage = binding.profilemessage.text.toString()
//            val nickname = binding.contentArea.text.toString()
//            val uid = FBAuth.getUid()
//            val time = FBAuth.getTime()
//
//
//            FBRef.profileRef
//                .child(uid)
//                .setValue(BoardModel(nickname, profileMessage))
//
//            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()
//
//            if(isImageUpload == true) {
//                imageUpload(key)
//            }
//
//            finish()
//
//
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == RESULT_OK && requestCode == 100) {
//            binding.profileimageArea.setImageURI(data?.data)
//            binding.basicimageArea.visibility= View.INVISIBLE
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.profileimageArea.setImageURI(uri)
            binding.basicimageArea.visibility= View.INVISIBLE
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "사진 올리기를 실패하였습니다", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "사진 올리기를 취소하였습니다", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.profile_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("사진 변경")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.albumBtn)?.setOnClickListener {



            launcher.launch(
                ImagePicker.with(this)
                    .cropSquare()
                    .galleryOnly()
                    .maxResultSize(500, 500)
                    .createIntent()
            )

//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, 100)

            binding.profileimageArea.visibility= View.VISIBLE

            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.basicBtn)?.setOnClickListener {
            binding.basicimageArea.visibility= View.VISIBLE
            binding.profileimageArea.visibility= View.GONE

            alertDialog.dismiss()
        }
    }


}