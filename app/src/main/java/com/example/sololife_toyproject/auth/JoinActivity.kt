package com.example.sololife_toyproject.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.sololife_toyproject.MainActivity
import com.example.sololife_toyproject.R
import com.example.sololife_toyproject.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.sql.DatabaseMetaData

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_join)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.joinBtn.setOnClickListener{

            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.pwdArea1.text.toString()
            val password2 = binding.pwdArea2.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()){
                Toast.makeText(this,"password1을 입력해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password2.isEmpty()){
                Toast.makeText(this,"Password2를 입력해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(!password2.equals(password2)){
                Toast.makeText(this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(password1.length<6){
                Toast.makeText(this,"비밀번호를 6자리 이상으로 설정해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(isGoToJoin){
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                            val intent  = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,"실패",Toast.LENGTH_SHORT).show()
                    }
                 }
            }
        }
    }
}