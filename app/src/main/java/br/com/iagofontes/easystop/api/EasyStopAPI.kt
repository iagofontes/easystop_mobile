package br.com.iagofontes.easystop.api

import br.com.iagofontes.easystop.model.BasicReturn
import retrofit2.Call
import retrofit2.http.*

interface EasyStopAPI {

    @POST(value="/api/v1/motoristas/login")
    @FormUrlEncoded
    fun login( @Field("email") email: String, @Field("senha") senha: String ) : Call<BasicReturn>

    @POST(value="/api/v1/motoristas")
    @FormUrlEncoded
    fun register(
        @Field("nome") nome: String,
        @Field("email") email: String,
        @Field("senha") senha: String,
        @Field("cpf") cpf: String,
        @Field("telefone") telefone: String,
        @Field("nascimento") nascimento: String
    ) : Call<BasicReturn>

}