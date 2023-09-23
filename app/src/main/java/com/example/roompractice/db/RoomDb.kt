package com.example.roompractice.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [SoccerEntity::class],
    version = 2,
    exportSchema = false // 스키마 내보내기를 비활성화하면 빌드 경고가 제거됩니다.
)
abstract class RoomDb : RoomDatabase() {
    abstract fun soccer(): SoccerDao


    companion object {
        @Volatile
        private var roomDb: RoomDb? = null
        operator fun invoke(context: Context) = roomDb ?: synchronized(Any()) {
            roomDb ?: buildDataBase(context).also {
                roomDb = it
            }
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context,
            RoomDb::class.java,
            "Soccer_DB"
        )
            .addMigrations(migration1to2) // 여기에 마이그레이션 경로를 추가합니다.
            .build()
    }
}

// 데이터가 변경되면 아래와 같은 과정이 필요합니다
// 마이그레이션 스크립트를 작성합니다.
val migration1to2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SOCCER ADD COLUMN isSelected INTEGER NOT NULL DEFAULT 0")
    }
}


