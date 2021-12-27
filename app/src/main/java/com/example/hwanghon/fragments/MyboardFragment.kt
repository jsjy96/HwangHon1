package com.example.hwanghon.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.hwanghon.R
import com.example.hwanghon.board.BoardInsideActivity
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.board.BoardRVAdapter
import com.example.hwanghon.databinding.FragmentMeetboardBinding
import com.example.hwanghon.databinding.FragmentMyboardBinding
import com.example.hwanghon.utils.FBAuth
import com.example.hwanghon.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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

    private val boardDataList = mutableListOf<BoardModel>()

    private val boardKeyList = mutableListOf<String>()
    lateinit var rvAdapter : BoardRVAdapter


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

        rvAdapter = BoardRVAdapter(boardDataList, requireContext())
        binding.boardRV.adapter = rvAdapter

        rvAdapter.setOnItemClickListener(object : BoardRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data : BoardModel, pos : Int) {
                Intent(context, BoardInsideActivity::class.java).apply {
                    putExtra("key", boardKeyList[pos])
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }

        })



//        binding.logoutBtn.setOnClickListener {
//            FBAuth.getAuth().signOut()
//            val intent = Intent(context, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//
//        }

        getMyFBBoardData()



        return binding.root
    }

    private fun getMyFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복돼서 나올 때 초기화
                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(BoardModel::class.java)

                    val myUid = FBAuth.getUid()
                    val writerUid = item?.uid

                    if(myUid.equals(writerUid)){
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                    } else {
                    }

                }


                boardKeyList.reverse()
                boardDataList.reverse()
                rvAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

}