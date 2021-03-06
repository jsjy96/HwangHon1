package com.example.hwanghon.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {

        //lateinit var myRef : DatabaseReference
        private val database = Firebase.database

        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")
        val bookmarkRef = database.getReference("bookmark_list")
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
        val profileRef = database.getReference("profile")
        val friendRef = database.getReference("friend")
        val chatRef = database.getReference("chat")
    }

}