package com.example.sololife_toyproject.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.sololife_toyproject.R
import com.example.sololife_toyproject.comment.CommentLVAdapter
import com.example.sololife_toyproject.comment.CommentModel
import com.example.sololife_toyproject.contentsList.BookmarkRVAdapter
import com.example.sololife_toyproject.contentsList.ContentModel
import com.example.sololife_toyproject.databinding.ActivityBoardInsideBinding
import com.example.sololife_toyproject.utils.FBAuth
import com.example.sololife_toyproject.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Comment
import java.io.ByteArrayOutputStream

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private val TAG = BoardInsideActivity::class.java.simpleName
    private lateinit var key: String

    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentLVAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        // 첫번째 방법
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.timeArea.text = time
//        binding.textArea.text = content

        // 두번째 방법
        key = intent.getStringExtra("key").toString()
        getBoardData(key)

        getImageData(key)


        commentLVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentLVAdapter

        getCommentData(key)

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

    }

    private fun insertComment(key : String){
        //comment
        //  - commentKey
        //      commentData
        val comment = binding.commentArea.text.toString()
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(comment,FBAuth.getTime()))
        binding.commentArea.setText("")

    }

    private fun getCommentData(key : String ){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentLVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

         val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this,"수정버튼을 눌렀습니다.",Toast.LENGTH_SHORT).show()
            val intent  = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
            alertDialog.dismiss()
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_SHORT).show()
            finish()
            alertDialog.dismiss()
        }

    }


    private fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key+".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener( { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }

        })
    }



    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try{
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java) // 데이터 딱 하나만 가져옴
                    binding.titleArea.text = dataModel?.title.toString()
                    binding.timeArea.text = dataModel?.time.toString()
                    binding.textArea.text = dataModel?.content.toString()

                    if(FBAuth.getUid().equals(dataModel?.uid)){
                        binding.boardSettingIcon.isVisible= true
                    }else{

                        binding.getImageArea.isVisible = false // 이미지 로딩 없을 시 이미지 영역 없애버림

                    }


                }catch (e : Exception){
                    Log.d(TAG,"삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}