package com.example.projetofinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetofinal.data.dao.ViagemDao
import com.example.projetofinal.data.dao.UsuarioDao
import com.example.projetofinal.data.model.Viagem
import com.example.projetofinal.data.model.Usuario

@Database(entities = [Usuario::class, Viagem::class], version = 2, exportSchema = false)
abstract class UsuarioDatabase : RoomDatabase() {
    abstract fun userDao(): UsuarioDao
    abstract fun travelDao(): ViagemDao

    companion object {
        @Volatile
        private var INSTANCE: UsuarioDatabase? = null

        fun getDatabase(context: Context): UsuarioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsuarioDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
