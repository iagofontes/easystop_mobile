package br.com.iagofontes.easystop.api

import br.com.iagofontes.easystop.model.BasicReturn
import br.com.iagofontes.easystop.model.Veiculo
import retrofit2.Call
import retrofit2.http.*

interface EasyStopAPI {

    @GET(value="/api/v1/motoristas/{driver}/carros")
    fun getMyCars(@Path("driver") driver: Long) : Call<MutableList<Veiculo>>

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

//    adicionar veiculos
    @POST(value="/api/v1/motoristas/carros")
    @FormUrlEncoded
    fun registerVehicle(
        @Field("motorista") motorista: Long,
        @Field("marca") marca: String,
        @Field("modelo") modelo: String,
        @Field("ano") ano: String,
        @Field("placa") placa: String
    ) : Call<BasicReturn>

//    atualizar veiculos
    @PUT(value="/api/v1/motoristas/carros")
    @FormUrlEncoded
    fun updateVehicle(
        @Field("codigo") codigo: Long,
        @Field("motorista") motorista: Long,
        @Field("marca") marca: String,
        @Field("modelo") modelo: String,
        @Field("ano") ano: String,
        @Field("placa") placa: String
    ) : Call<BasicReturn>

//    remover veiculos
    @DELETE(value="/api/v1/motoristas/carros/{veiculo}")
    fun removeVehicle( @Path("veiculo") veiculo: Long ) : Call<BasicReturn>

}