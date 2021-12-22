package com.example.hwanghon.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardWriteActivity
import com.example.hwanghon.databinding.FragmentChattingBinding
import com.example.hwanghon.databinding.FragmentFreeboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FreeboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreeboardFragment : Fragment() {

    private lateinit var binding : FragmentFreeboardBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_freeboard, container, false)


        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.friendTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_freeboardFragment_to_friendFragment)
        }

        binding.chattingTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_freeboardFragment_to_chattingFragment)
        }

        binding.meetboardTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_freeboardFragment_to_meetboardFragment)
        }

        binding.myboardTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_freeboardFragment_to_myboardFragment)
        }

        binding.lectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_freeboardFragment_to_lectureFragment)
        }

//
//        val rv = view?.findViewById<RecyclerView>(R.id.boardRV)
//
//        val items = ArrayList<String>()
//        items.add("a")
//        items.add("b")
//        items.add("c")
//
//        val rvAdapter = BoardListLVAdaptor(items)
//
//            rv?.adapter = rvAdapter
//
//            rv?.layoutManager = LinearLayoutManager(context)


//        binding.logoutBtn.setOnClickListener {
//            FBAuth.getAuth().signOut()
//            val intent = Intent(context, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//
//        }



        return binding.root
    }



}