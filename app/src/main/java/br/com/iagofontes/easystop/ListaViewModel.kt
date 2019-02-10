package br.com.iagofontes.easystop

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import br.com.iagofontes.easystop.api.EasyStopAPI
import br.com.iagofontes.easystop.model.BasicReturn
import br.com.iagofontes.easystop.model.Veiculo
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.content_lista.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaViewModel(application: Application): AndroidViewModel(application) {


    val okhttp = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://easystop.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp)
        .build()

    val esAPI = retrofit.create<EasyStopAPI>(EasyStopAPI::class.java)

    fun carregaVeiculos(vAdapter: VeiculoAdapter, rvVeiculo: RecyclerView, driver: Long) {

        esAPI
            .getMyCars(driver)
            .enqueue(object : Callback<MutableList<Veiculo>> {
                override fun onFailure(call: Call<MutableList<Veiculo>>, t: Throwable?) {
                    updateList(vAdapter, rvVeiculo, mutableListOf(Veiculo()))
                }
                override fun onResponse(call: Call<MutableList<Veiculo>>, response: Response<MutableList<Veiculo>>?) {
                    if((response?.isSuccessful == true) && (response?.code() == 200)) {
                        updateList(vAdapter, rvVeiculo, response.body()!!)
                    }
                }
            })
    }

    fun updateList(vAdapter: VeiculoAdapter, rvVeiculo: RecyclerView, veics: MutableList<Veiculo>) {
        vAdapter.setList(veics)
        rvVeiculo.adapter.notifyDataSetChanged()
    }

}