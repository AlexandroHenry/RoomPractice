package com.example.roompractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.roompractice.databinding.ActivityMainBinding
import com.example.roompractice.db.RoomDb

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val naviController by lazy { findNavController(R.id.mainNavHostFragment) }

    internal lateinit var roomDb: RoomDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomDb = RoomDb.invoke(this)

//        deleteRoomDatabase()

        setContentView(binding.root)
    }

    // Room 데이터베이스를 제거하는 함수
    private fun deleteRoomDatabase() {
        try {
            val dbFile = getDatabasePath("Soccer_DB")
            dbFile.delete()
            roomDb = RoomDb.invoke(this)
            // 데이터베이스를 삭제하고 다시 생성합니다.
        } catch (e: Exception) {
            // 오류 처리
        }
    }

    // 이 함수를 호출하여 데이터베이스를 제거할 수 있습니다.
    private fun removeDatabase() {
        deleteRoomDatabase()
    }

}