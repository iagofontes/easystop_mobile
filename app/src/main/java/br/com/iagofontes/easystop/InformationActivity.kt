package br.com.iagofontes.easystop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.iagofontes.easystop.model.Veiculo
import kotlinx.android.synthetic.main.activity_information.*
import java.lang.Exception

class InformationActivity : AppCompatActivity() {

    private var veiculo : Veiculo = Veiculo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        var veiculoController = VeiculoController()

        try {
            var bundle: Bundle = getIntent().extras
            loadCar(bundle)
            updateView(
                this.veiculo.marca!!,
                this.veiculo.modelo!!,
                this.veiculo.ano!!,
                this.veiculo.placa!!
            )
        } catch (e: Exception) {
            Log.println(Log.ERROR, "BUNDLE_ERROR", e.message)
        }

        btnAddCar.setOnClickListener {
            changeButtonState(false)
            updateVehicle()
            if(this.veiculo.codigo > 0) {
                veiculoController.updateVehicle(this.veiculo) {
                    changeButtonState(true)
                    if(it.status) {
                        this.successWay()
                    }
                    this.unsuccessWay(it.message)
                }
            } else {
                veiculoController.adicionarVeiculo(this.veiculo) {
                    changeButtonState(true)
                    if(it.status) {
                        this.successWay()
                    }
                    this.unsuccessWay(it.message)
                }
            }

        }

    }

    private fun loadCar(bndl: Bundle) {

        this.veiculo.codigo = bndl.getLong("veiculoCodigo", 0)
        this.veiculo.motorista = bndl.getLong("veiculoMotorista", 0)
        this.veiculo.marca = bndl.getString("veiculoMontadora", "")
        this.veiculo.modelo = bndl.getString("veiculoModelo", "")
        this.veiculo.ano = bndl.getString("veiculoAno", "1965")
        this.veiculo.placa = bndl.getString("veiculoPlaca", "")

    }

    private fun updateView(montadora: String, modelo: String, ano: String, placa: String) {

        this.txtMontadora.editText!!.text.replace(0,0,montadora)
        this.txtModelo.editText!!.text.replace(0,0,modelo)
        this.txtAno.editText!!.text.replace(0,0, ano)
        this.txtPlaca.editText!!.text.replace(0,0, placa)

    }

    private fun updateVehicle() {

        this.veiculo.marca = this.txtMontadora.editText!!.text.toString()
        this.veiculo.modelo = this.txtModelo.editText!!.text.toString()
        this.veiculo.ano = this.txtAno.editText!!.text.toString()
        this.veiculo.placa = this.txtPlaca.editText!!.text.toString()

    }

    private fun successWay() {
        startActivity(Intent(this@InformationActivity, CarsActivity::class.java))
        this.finish()
    }

    private fun unsuccessWay(message: String) {
        Toast.makeText(this@InformationActivity, message, Toast.LENGTH_LONG)
    }

    private fun changeButtonState(state: Boolean) {
        this.btnAddCar.isClickable = state
    }

}