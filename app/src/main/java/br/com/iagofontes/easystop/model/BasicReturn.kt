package br.com.iagofontes.easystop.model

import com.google.gson.annotations.SerializedName

data class BasicReturn (

    @SerializedName("mensagem") val message: String

)