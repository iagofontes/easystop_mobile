package br.com.iagofontes.easystop.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Veiculo {

    @PrimaryKey(autoGenerate = true)
    var codigo : Long = 0

    var motorista : Number? = 0
    var marca : String? = ""
    var modelo : String? = ""
    var ano : String? = ""
    var placa : String? = ""
    var status : Boolean? = false

    constructor()

    constructor( motorista: Number, marca: String, modelo: String, ano:String,
                 placa: String, status: Boolean) {

        this.motorista = motorista
        this.marca = marca
        this.modelo = modelo
        this.ano = ano
        this.placa = placa
        this.status = status

    }


}