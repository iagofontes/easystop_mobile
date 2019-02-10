package br.com.iagofontes.easystop

import android.content.Context
import android.util.Log
import android.widget.Toast
import br.com.iagofontes.easystop.api.EasyStopAPI
import br.com.iagofontes.easystop.model.AsyncReturn
import br.com.iagofontes.easystop.model.BasicReturn
import br.com.iagofontes.easystop.model.Veiculo
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VeiculoController {

    val okhttp = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://easystop.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp)
        .build()

    val esAPI = retrofit.create<EasyStopAPI>(EasyStopAPI::class.java)


    fun adicionarVeiculo(veiculo: Veiculo, callback: (AsyncReturn) -> Unit) {

        if(!this.validarVeiculo(veiculo)) {
            callback(AsyncReturn(false, "Dados do veículo são inválidos."))
        }

        esAPI
            .registerVehicle(
                veiculo.motorista!!.toLong(),
                veiculo.marca.toString(),
                veiculo.modelo.toString(),
                veiculo.ano.toString(),
                veiculo.placa.toString()
            )
            .enqueue(object : Callback<BasicReturn> {
                override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                    callback(AsyncReturn(false, t?.message.toString()))
                }
                override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {
                    if((response?.isSuccessful == true) && (response?.code() == 200)) {
                        callback(AsyncReturn(true, response.body()?.message.toString()))
                    }
                }
            })

    }

    fun updateVehicle(veiculo: Veiculo, callback: (AsyncReturn) -> Unit) {

        try {
            esAPI
                .updateVehicle(
                    veiculo.codigo,
                    veiculo.motorista!!.toLong(),
                    veiculo.marca.toString(),
                    veiculo.modelo.toString(),
                    veiculo.ano.toString(),
                    veiculo.placa.toString()
                )
                .enqueue(object : Callback<BasicReturn> {
                    override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                        callback(AsyncReturn(false, "Falha ao atualizar veículo"))
                    }
                    override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {
                        if((response?.isSuccessful == true) && (response?.code() == 200)) {
                            callback(AsyncReturn(true, response.body()?.message.toString()))
                        }
                    }
                })
        } catch(e: Exception) {
            Log.println(Log.ERROR, "UPDATE_ERROR", e.message)
            callback(AsyncReturn(false, "Problemas ao atualizar veículo"))
        }

    }

    fun removeVehicle(veiculo: Long, context: Context, callback: (AsyncReturn) -> Unit) {

        try {
            esAPI
                .removeVehicle(veiculo)
                .enqueue(object : Callback<BasicReturn> {
                    override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                        callback(AsyncReturn(false, "Problemas ao realizar exclusão de veículo."))
                    }
                    override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {
                        if((response?.isSuccessful == true) && (response?.code() == 200)) {
                            callback(AsyncReturn(true, response.body()!!.message))
                        }
                    }
                })
        } catch(e: Exception) {
            callback(AsyncReturn(false, "Problemas ao realizar exclusão de veículo."))
        }

    }

    fun validarVeiculo(veiculo: Veiculo) : Boolean {

        if(
            !veiculo.marca.equals("") &&
            !veiculo.modelo.equals("") &&
            !veiculo.placa.equals("") &&
            !veiculo.ano.equals("")
        ) {
            return true
        }
        return false
    }



}