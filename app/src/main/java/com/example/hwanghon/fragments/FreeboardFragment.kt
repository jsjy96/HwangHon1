package com.example.hwanghon.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.hwanghon.board.BoardInsideActivity
import com.example.hwanghon.board.BoardListLVAdaptor
import com.example.hwanghon.board.BoardModel
import com.example.hwanghon.board.BoardWriteActivity
import com.example.hwanghon.databinding.FragmentFreeboardBinding
import com.example.hwanghon.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

    private val boardDataList = mutableListOf<BoardModel>()

    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardLVadapter : BoardListLVAdaptor


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

        boardLVadapter = BoardListLVAdaptor(boardDataList)
        binding.boardListView.adapter = boardLVadapter

        binding.boardListView.setOnItemClickListener { parent, view, position, id ->

            //첫번째 방법은 listview에 있는 데이터 다 다른 액티비티로 전달해서 만들기
//            val intent = Intent(context, BoardinsideActivity::class.java)
//            intent.putExtra("title", boardDataList[position].title)
//            intent.putExtra("content", boardDataList[position].content)
//            intent.putExtra("time", boardDataList[position].time)
//            startActivity(intent)

            //두번째 방법은 Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)

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

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복돼서 나올 때 초기화
                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {



                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }

                boardKeyList.reverse()
                boardDataList.reverse()
                boardLVadapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

}