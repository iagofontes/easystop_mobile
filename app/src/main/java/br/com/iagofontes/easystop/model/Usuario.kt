package br.com.iagofontes.easystop.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Usuario {

    @PrimaryKey(autoGenerate = true)
    var codigo: Long = 0
    var email : String = ""
    var senha : String = ""


    constructor()

    constructor( codigo: Long, email: String, senha: String) {

        this.codigo = codigo
        this.email = email
        this.senha = senha

    }


}