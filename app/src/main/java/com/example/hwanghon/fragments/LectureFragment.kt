package com.example.hwanghon.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardWriteActivity
import com.example.hwanghon.databinding.FragmentFreeboardBinding
import com.example.hwanghon.databinding.FragmentLectureBinding
import com.example.hwanghon.lecture.LectureInsideActivity
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LectureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LectureFragment : Fragment() {

    private lateinit var binding : FragmentLectureBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lecture, container, false)

        binding.textArea.setOnClickListener {
            val intent = Intent(context, LectureInsideActivity::class.java)
            startActivity(intent)
        }

        binding.friendTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_lectureFragment_to_friendFragment)
        }

        binding.chattingTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_lectureFragment_to_chattingFragment)
        }

        binding.mylectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_lectureFragment_to_mylectureFragment)
        }

        binding.communityTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_lectureFragment_to_freeboardFragment)
        }



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