package br.com.iagofontes.easystop

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_initial)

        Handler().postDelayed({

            val sharedPreferences = getSharedPreferences("meuapp", Context.MODE_PRIVATE)
            if(sharedPreferences.getBoolean("MANTER_CONECTADO", false) && !sharedPreferences.getString("USUARIO", "").equals("")) {
                startActivity(Intent(this@InitialActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@InitialActivity, LoginActivity::class.java))
            }
            finish()
        }, 4000)

    }
}
