package br.com.iagofontes.easystop.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import br.com.iagofontes.easystop.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun inserir(usuario: Usuario)

    @Delete
    fun apagar(usuario: Usuario)

    @Update
    fun atualizar(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE codigo = :id")
    fun buscarPorId(id: Long): Usuario


}