package br.com.iagofontes.easystop.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Motorista (

    @SerializedName("nome")          val name     : String,
    @SerializedName("email")         val email    : String,
    @SerializedName("senha")         val senha    : String,
    @SerializedName("cpf")           val cpf      : String,
    @SerializedName("telefone")      val telefone : String,
    @SerializedName("nascimento")    val date     : Date

)