package br.com.iagofontes.easystop

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.com.iagofontes.easystop.model.Veiculo
import com.facebook.stetho.inspector.domstorage.SharedPreferencesHelper
import java.lang.Exception
import java.util.logging.Logger

class VehicleDialogFragment : DialogFragment() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var txtMontadora: EditText
    private lateinit var txtModelo: EditText
    private lateinit var txtAno: EditText
    private lateinit var txtPlaca: EditText

    private var veiculo : Veiculo = Veiculo()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        builder = AlertDialog.Builder(activity)

        val v = activity.layoutInflater.inflate(R.layout.dialog_vehicle, null)

        var veiculoController = VeiculoController()

        this.txtMontadora = v.findViewById(R.id.edtMontadora)
        this.txtModelo = v.findViewById(R.id.edtModelo)
        this.txtAno = v.findViewById(R.id.edtAno)
        this.txtPlaca = v.findViewById(R.id.edtPlaca)

        loadVehicle()
        updateView(
            this.veiculo.marca!!,
            this.veiculo.modelo!!,
            this.veiculo.ano!!,
            this.veiculo.placa!!
        )

        builder.setView(v)
        builder.setTitle("Novo Veículo")

        builder.setPositiveButton("Adicionar") { _, _ ->

            updateLocalVariables()

            if(this.veiculo.codigo > 0) {
                veiculoController.updateVehicle(this.veiculo) {
                    try {
                        var iResult : ResultOperation = activity as ResultOperation
                        iResult.onResultInsertVehicle(it.status, it.message)
                    } catch (e: Exception) {
                        Log.println(Log.ERROR, "CONVERSION_ERROR", e.message)
                    }
                }
            } else {
                veiculoController.adicionarVeiculo(this.veiculo) {
                    (this.activity.applicationContext as ResultOperation).onResultInsertVehicle(it.status, it.message)
                    dismiss()
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)
        return builder.create()

    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(activity.applicationContext, "Teste no fragment em onStop", Toast.LENGTH_LONG)
    }

    private fun loadVehicle() {

        this.veiculo.codigo = arguments.getLong("veiculoCodigo", 0)
        this.veiculo.motorista = arguments.getLong("veiculoMotorista", 0)
        this.veiculo.marca = arguments.getString("veiculoMontadora", "")
        this.veiculo.modelo = arguments.getString("veiculoModelo")
        this.veiculo.ano = arguments.getString("veiculoAno", "1965")
        this.veiculo.placa = arguments.getString("veiculoPlaca")

    }

    private fun updateView(montadora: String, modelo: String, ano: String, placa: String) {

        txtMontadora.text.replace(0, 0, montadora)
        txtModelo.text.replace(0, 0, modelo)
        txtAno.text.replace(0, 0, ano)
        txtPlaca.text.replace(0, 0, placa)

    }

    private fun updateLocalVariables() {

        this.veiculo.marca = txtMontadora.text.toString()
        this.veiculo.modelo = txtModelo.text.toString()
        this.veiculo.ano = txtAno.text.toString()
        this.veiculo.placa = txtPlaca.text.toString()

    }

    interface ResultOperation {
        fun onResultInsertVehicle( response: Boolean, message: String )
    }

}