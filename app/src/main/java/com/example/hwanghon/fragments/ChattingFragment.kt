package com.example.hwanghon.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.hwanghon.R
import com.example.hwanghon.databinding.FragmentChattingBinding
import com.example.hwanghon.databinding.FragmentFriendBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChattingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChattingFragment : Fragment() {

    private lateinit var binding : FragmentChattingBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatting, container, false)

        binding.friendTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_friendFragment)
        }

        binding.communityTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_freeboardFragment)
        }

        binding.lectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_chattingFragment_to_lectureFragment)
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