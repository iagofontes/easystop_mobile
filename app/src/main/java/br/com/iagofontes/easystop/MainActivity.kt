package br.com.iagofontes.easystop

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.iagofontes.easystop.dao.BancoDeDados
import br.com.iagofontes.easystop.model.Usuario
import com.facebook.stetho.inspector.domstorage.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.item_park -> {
                Toast.makeText(this@MainActivity, "Estacionamentos", Toast.LENGTH_LONG).show()
            }

            R.id.item_userdata -> {
                Toast.makeText(this@MainActivity, "Dados do usuário", Toast.LENGTH_LONG).show()
            }

            R.id.item_mycars -> {

                myCars()

            }

            R.id.item_about -> {

                aboutApp()

            }

            R.id.item_logout -> {

                logoutApp()

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun myCars() {

        startActivity(Intent(this@MainActivity, CarsActivity::class.java))

    }

    private fun aboutApp() {

        startActivity(Intent(this@MainActivity, AboutActivity::class.java))

    }

    private fun logoutApp()  {

        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()

    }


    private fun removeUser( usuario: Usuario ) {

        if(!getSharedPreferences("meuapp", Context.MODE_PRIVATE).getBoolean("MANTER_CONECTADO", false)) {
            val db = BancoDeDados.getDatabase(this)
            if (usuario.codigo > 0)
                InsertAsyncTask(db!!, 2).execute(usuario)
        }

    }

    private inner class InsertAsyncTask internal constructor(appDatabase: BancoDeDados, operationCode: Int) : AsyncTask<Usuario, Void, String>() {
        /*

        >>> OPERATION CODE           <<<
        >>> 1 = INSERIR NOVO USUARIO <<<
        >>> 2 = REMOVER USUARIO      <<<

         */
        private val db: BancoDeDados = appDatabase
        private val operCode: Int = operationCode
        override fun doInBackground(vararg params: Usuario): String {
            if(operCode == 1)
                db.usuarioDAO().inserir(params[0])
            else if(operCode == 2)
                db.usuarioDAO().apagar(params[0])
            return ""

        }
    }

}
