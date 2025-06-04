package com.example.projetofinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projetofinal.data.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertUser(usuario: Usuario)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): Usuario?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): Usuario?
}