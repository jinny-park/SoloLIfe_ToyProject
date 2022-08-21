package com.example.sololife_toyproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.sololife_toyproject.R
import com.example.sololife_toyproject.board.BoardInsideActivity
import com.example.sololife_toyproject.board.BoardListVAdapter
import com.example.sololife_toyproject.board.BoardModel
import com.example.sololife_toyproject.board.BoardWriteActivity
import com.example.sololife_toyproject.contentsList.BookmarkRVAdapter
import com.example.sololife_toyproject.contentsList.ContentModel
import com.example.sololife_toyproject.databinding.FragmentTalkBinding
import com.example.sololife_toyproject.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    private val TAG = TalkFragment::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardLVAdapter: BoardListVAdapter
    private var boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)


        // listView 연결
        boardLVAdapter = BoardListVAdapter(boardDataList)

        binding.boardListView.adapter = boardLVAdapter


        binding.boardListView.setOnItemClickListener { parent, view, position, id ->

            // 게시글 세부 페이지 이동 방법 1
            // listView에 있는 내용들을 다 다른 액티비티로 전달해줘서 만들기

//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title",boardDataList[position].title)
//            intent.putExtra("content",boardDataList[position].content)
//            intent.putExtra("time",boardDataList[position].time)
//
//            startActivity(intent)

            // 방법2
            // firebase에 있는 uid 값을 기반으로 해당 데이터를 받아올 것

            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)



        }



        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }


        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }

        getFBBoardData()

        return binding.root

    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear() // 데이터 추가로 인한 변경이 생길때 중복 데이터가 생기는 파베의 특징으로 인해서 클리어 실시
                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardDataList.reverse() // stack 구조로 만들어주기 위해서
                boardKeyList.reverse()

                boardLVAdapter.notifyDataSetChanged()
                Log.d(TAG, boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }
}