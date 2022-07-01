package com.example.sololife_toyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sololife_toyproject.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        if(auth.currentUser?.uid==null){ // 사용자가 없는 경우 인트로액티비티로 이동
            Handler().postDelayed({
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }, 3000)
        }else{
            Handler().postDelayed({ // 로그인한 사용자 정보가 있는 경우에 메인액티비티로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}