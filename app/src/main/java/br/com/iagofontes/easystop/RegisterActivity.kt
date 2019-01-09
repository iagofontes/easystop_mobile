package br.com.iagofontes.easystop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.iagofontes.easystop.api.EasyStopAPI
import br.com.iagofontes.easystop.model.BasicReturn
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            registerDriver()
        }

    }

    private fun registerDriver() {

        var calendar = Calendar.getInstance()
        calendar.set(
            edtNascimento.text.toString().substring(6,10).toInt(),
            edtNascimento.text.toString().substring(3,5).toInt()-1,
            edtNascimento.text.toString().substring(0,2).toInt())
        var sdf = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

        val okhttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://easystop.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()

        val esAPI = retrofit.create<EasyStopAPI>(EasyStopAPI::class.java)

        if(this.validarMotorista()) {
            esAPI
                .register(
                    edtNome.text.toString(),
                    edtEmail.text.toString(),
                    edtPasswordReg.text.toString(),
                    edtCpf.text.toString(),
                    edtTelefone.text.toString(),
                    sdf.toString()
                )
                .enqueue(object : Callback<BasicReturn> {

                    override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                        Toast.makeText(this@RegisterActivity, t?.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {
                        if((response?.isSuccessful == true) && (response?.code() == 200)) {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, response?.body()?.message, Toast.LENGTH_LONG).show()
                        }
                    }

                })
        } else {
            Toast.makeText(this@RegisterActivity, "Verifique os dados informados", Toast.LENGTH_LONG).show()
        }

    }


    private fun validarMotorista() : Boolean {

        if(
            (edtNome.text.toString().isNullOrEmpty()) ||
            (edtEmail.text.toString().isNullOrEmpty()) ||
            (edtPasswordReg.text.toString().isNullOrEmpty()) ||
            (edtCpf.text.toString().isNullOrEmpty()) ||
            (edtTelefone.text.toString().isNullOrEmpty()) ||
            (edtNascimento.text.toString().isNullOrEmpty())
        ) {
            return false
        }

        return true

    }

}
