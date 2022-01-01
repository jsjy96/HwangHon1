package com.example.hwanghon.friend

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardEditActivity
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.databinding.ActivityMyProfileBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    private var isImageUpload = false

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

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profileModel = dataSnapshot.getValue(ProfileModel::class.java)

                binding.usernameArea.setText(profileModel?.nickname)
                binding.profilemessage.setText(profileModel?.profilemessage)


            }

            override fun onCancelled(databaseError: DatabaseError) {


            }

        }
        val uid = FBAuth.getUid()
        FBRef.profileRef.child(uid).addValueEventListener(postListener)

        getImagedata(uid)


        binding.editBtn.setOnClickListener {

            val profileMessage = binding.profilemessage.text.toString()
            val nickname = binding.usernameArea.text.toString()


            FBRef.profileRef
                .child(uid)
                .setValue(ProfileModel(nickname, profileMessage))


            if(isImageUpload == true) {
                imageUpload(uid)
            }

            finish()


        }
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

            isImageUpload = true

            binding.profileimageArea.visibility= View.VISIBLE

            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.basicBtn)?.setOnClickListener {

            val storage = Firebase.storage
            val storageRef = storage.reference
            val uid = FBAuth.getUid()
            val mountainsRef = storageRef.child(uid + ".png")

            mountainsRef.delete().addOnSuccessListener {
                // File deleted successfully
            }.addOnFailureListener {
                // Uh-oh, an error occurred!
            }

            binding.basicimageArea.visibility= View.VISIBLE
            binding.profileimageArea.visibility= View.GONE


           alertDialog.dismiss()
        }
    }

    private fun imageUpload(uid: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(uid + ".png")

        val profileimage = binding.profileimageArea

        profileimage.isDrawingCacheEnabled = true
        profileimage.buildDrawingCache()
        val bitmap = (profileimage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }

    private fun getImagedata(uid : String){

        val storageReference = Firebase.storage.reference.child(uid + ".png")

// ImageView in your Activity
        val imageViewFromFB = binding.profileimageArea
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        })

    }


}