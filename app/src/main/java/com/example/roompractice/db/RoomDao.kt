package com.example.roompractice.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SoccerDao {
    @Query("SELECT * FROM SOCCER")
    suspend fun select(): List<SoccerEntity>

    @Insert
    suspend fun insert(entity: SoccerEntity)

    @Query("DELETE FROM SOCCER")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(soccerEntity: SoccerEntity)

    // 데이터 업데이트를 위한 메서드
    @Update
    suspend fun update(soccerEntity: SoccerEntity)
}