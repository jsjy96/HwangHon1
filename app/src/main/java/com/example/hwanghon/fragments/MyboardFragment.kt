package com.example.hwanghon.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.hwanghon.R
import com.example.hwanghon.databinding.FragmentMeetboardBinding
import com.example.hwanghon.databinding.FragmentMyboardBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyboardFragment : Fragment() {

    private lateinit var binding : FragmentMyboardBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myboard, container, false)

        binding.friendTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_myboardFragment_to_friendFragment)
        }

        binding.chattingTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_myboardFragment_to_chattingFragment)
        }

        binding.freeboardTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_myboardFragment_to_freeboardFragment)
        }

        binding.meetboardTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_myboardFragment_to_meetboardFragment)
        }

        binding.lectureTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_myboardFragment_to_lectureFragment)
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