package br.com.iagofontes.easystop.api

import br.com.iagofontes.easystop.model.BasicReturn
import retrofit2.Call
import retrofit2.http.*

interface EasyStopAPI {

    @POST(value="/api/v1/motoristas/login")
    @FormUrlEncoded
    fun login( @Field("email") email: String, @Field("senha") senha: String   ) : Call<BasicReturn>

}