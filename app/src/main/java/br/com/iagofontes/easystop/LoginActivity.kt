package br.com.iagofontes.easystop

import android.content.Context
import android.content.Intent
import android.inputmethodservice.Keyboard
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import br.com.iagofontes.easystop.api.EasyStopAPI
import br.com.iagofontes.easystop.dao.BancoDeDados
import br.com.iagofontes.easystop.model.BasicReturn
import br.com.iagofontes.easystop.model.Usuario
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

        cleanParameters()

        btnLogin.setOnClickListener {
            loginClient()
        }

        btnCadastrar.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

    }

    private fun loginClient() {
        Keyboard.KEYCODE_DONE

        this.changeButtonState(false)

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
                edtEmail.editText!!.text.toString(),
                edtPassword.editText!!.text.toString())
            .enqueue(object : Callback<BasicReturn> {

                override fun onFailure(call: Call<BasicReturn>, t: Throwable?) {
                    Log.println(Log.ERROR, "LOGIN_ERROR", t?.message)
                    changeButtonState(true)
                    Toast.makeText(this@LoginActivity, "Problemas ao realizar login", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BasicReturn>, response: Response<BasicReturn>?) {

                    if((response?.isSuccessful == true) && (response?.code() == 200)) {
                        keepConected(swtConectado.isChecked, response.body()?.message.toString())
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        changeButtonState(true)
                        Toast.makeText(this@LoginActivity, "Impossível realizar login", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun keepConected(save: Boolean, driverId: String) {
        val editor = getSharedPreferences("meuapp", Context.MODE_PRIVATE).edit()

        editor.putBoolean("MANTER_CONECTADO", swtConectado.isChecked)
        editor.putString("USUARIO", edtEmail.editText!!.text.toString())
        editor.putString("SENHA", edtPassword.editText!!.text.toString())
        editor.putString("DRIVERID", driverId)

        if(save == true) {
            this.saveUser(Usuario(driverId.toLong(), edtEmail.editText!!.text.toString(), edtPassword.editText!!.text.toString()))
        }

        editor.apply()
    }

    private fun cleanParameters() {
        val editor = getSharedPreferences("meuapp", Context.MODE_PRIVATE).edit()
        editor.putBoolean("MANTER_CONECTADO", false)
        editor.putString("USUARIO", "")
        editor.putString("SENHA", "")
        editor.putString("DRIVERID", "")
        editor.apply()
    }

    private fun saveUser( usuario: Usuario ) {
        val db = BancoDeDados.getDatabase(this.applicationContext)
        if (this.validarUsuario(usuario)) {

            if(!this.checkUser(usuario)) {
                InsertAsyncTask(db!!, 1).execute(usuario)
            }

        }
    }

    private fun checkUser( usuario: Usuario ) : Boolean {

        val db = BancoDeDados.getDatabase(this)
        if (usuario.codigo > 0) {
            var user = InsertAsyncTask(db!!, 2)
                .execute(usuario)
                .get()
            if(user != null) {
                return this.validarUsuario(user)
            }
        }
        return false
    }

    private fun validarUsuario(usuario: Usuario) : Boolean {
        if(
            (usuario.codigo > 0) &&
            (!usuario.email.equals("")) &&
            (!usuario.senha.equals(""))
        ){
            return true
        }
        return false
    }

    private fun changeButtonState(state: Boolean) {
        btnLogin.isClickable = state
        btnCadastrar.isClickable = state
    }

    private inner class InsertAsyncTask internal constructor(appDatabase: BancoDeDados, operationCode: Int) : AsyncTask<Usuario, Void, Usuario>() {
        /*

        >>> OPERATION CODE           <<<
        >>> 1 = INSERIR NOVO USUARIO <<<
        >>> 2 = BUSCAR USUARIO      <<<

         */
        private val db: BancoDeDados = appDatabase
        private val operCode: Int = operationCode
        override fun doInBackground(vararg params: Usuario): Usuario {
            if(operCode == 1){
                db.usuarioDAO().inserir(params[0])
            } else if(operCode == 2) {
                return db.usuarioDAO().buscarPorId(params[0].codigo)
            }
            return params[0]
        }
    }
}
