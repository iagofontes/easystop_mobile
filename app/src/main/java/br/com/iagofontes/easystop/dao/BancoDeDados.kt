package br.com.iagofontes.easystop.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import br.com.iagofontes.easystop.model.Usuario

@Database(entities = arrayOf(Usuario::class), version = 1)
abstract class BancoDeDados: RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDao

    companion object {

        var INSTANCE: BancoDeDados? = null

        fun getDatabase(context: Context): BancoDeDados? {

            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    BancoDeDados::class.java,
                    "usuariosdb").build()
            }

            return INSTANCE

        }

    }

}