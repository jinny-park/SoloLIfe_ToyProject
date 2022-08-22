package com.example.sololife_toyproject.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.sololife_toyproject.R
import com.example.sololife_toyproject.auth.IntroActivity
import com.example.sololife_toyproject.databinding.ActivitySettingBinding
import com.example.sololife_toyproject.utils.FBAuth

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val logoutBtn = binding.logOutBtn

        logoutBtn.setOnClickListener {
            FBAuth.getAuth().signOut()
            Toast.makeText(this,"로그아웃",Toast.LENGTH_SHORT).show()

            val intent = Intent(this, IntroActivity::class.java)
            // 모든 액티비티 화면 다 종료
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}