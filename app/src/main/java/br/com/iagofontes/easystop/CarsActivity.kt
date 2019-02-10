package br.com.iagofontes.easystop

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import br.com.iagofontes.easystop.model.Veiculo
import kotlinx.android.synthetic.main.activity_cars.*
import kotlinx.android.synthetic.main.card_veiculo.*
import kotlinx.android.synthetic.main.card_veiculo.view.*
import kotlinx.android.synthetic.main.content_lista.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsActivity : AppCompatActivity() {

    lateinit var listaViewModel: ListaViewModel
    lateinit var veiculoAdapter: VeiculoAdapter
    var driverId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        this.driverId = getSharedPreferences("meuapp", Context.MODE_PRIVATE).getString("DRIVERID", "0").toLong()

        iniciaViewModel()

        inicializaRecyclerView()

        listaViewModel
            .carregaVeiculos(veiculoAdapter, rvVeiculos, this.driverId)

        fabAddVehicle.setOnClickListener {

            var i = Intent(this@CarsActivity, InformationActivity::class.java)

            i.putExtra("veiculoCodigo", 0)
            i.putExtra("veiculoMotorista", this.driverId)
            i.putExtra("veiculoMontadora", "")
            i.putExtra("veiculoModelo", "")
            i.putExtra("veiculoAno", "")
            i.putExtra("veiculoPlaca", "")

            startActivity(i)
            this.finish()

        }

        rvVeiculos.addOnScrollListener( object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {
                    fabAddVehicle.hide()
                } else {
                    fabAddVehicle.show()
                }

                super.onScrolled(recyclerView, dx, dy)

            }
        } )

    }

    private fun inicializaRecyclerView() {
        //      cria uma lista na vertical
        rvVeiculos.layoutManager = LinearLayoutManager(this)
        veiculoAdapter = VeiculoAdapter(mutableListOf(), this@CarsActivity, listaViewModel, rvVeiculos)
        rvVeiculos.adapter = veiculoAdapter
    }

    private fun iniciaViewModel() {
        listaViewModel = ViewModelProviders
            .of(this)
            .get(ListaViewModel::class.java)
    }

}
