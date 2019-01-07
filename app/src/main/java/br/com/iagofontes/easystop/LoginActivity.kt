package br.com.iagofontes.easystop

import android.content.Intent
import android.inputmethodservice.Keyboard
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import br.com.iagofontes.easystop.api.EasyStopAPI
import br.com.iagofontes.easystop.model.BasicReturn
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            loginClient()
        }

        btnCadastrar.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CadastroActivity::class.java))
        }

    }

    private fun loginClient() {
        Keyboard.KEYCODE_DONE
        val okhttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://easystop.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()

        val esAPI = retrofit.create<EasyStopAPI>(EasyStopAPI::class.java)

        esAPI
            .login(
                edtEmail.text.toString(),
                edtPassword.text.toString())
            .enqueue(object : Callback<BasicReturn> {

                override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                    Toast.makeText(this@LoginActivity, t?.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {
//                    Keyboard.KEYCODE_DONE
                    if((response?.isSuccessful == true) && (response?.code() == 200)) {
//                        verificar se está selecionado para manter-se conectado
//                        caso esteja preciso salvar os dados para lembrar na splashscreen
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Impossível realizar login", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

}