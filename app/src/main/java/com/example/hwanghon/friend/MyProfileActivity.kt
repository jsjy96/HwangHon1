package com.example.hwanghon.friend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardEditActivity
import com.example.hwanghon.databinding.ActivityMyProfileBinding
import com.example.hwanghon.utils.FBRef

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        binding.imagechangeBtn.setOnClickListener {
            showDialog()



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.profileimageArea.setImageURI(data?.data)
            binding.basicimageArea.visibility= View.INVISIBLE
        }
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.profile_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("사진 변경")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.albumBtn)?.setOnClickListener {

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

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