package br.com.iagofontes.easystop

import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import br.com.iagofontes.easystop.model.BasicReturn
import br.com.iagofontes.easystop.model.Veiculo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.IntentCompat
import android.util.Log
import android.widget.Button
import java.lang.Exception

class VeiculoAdapter(var veiculos: MutableList<Veiculo>, var context: Context, var listaViewModel: ListaViewModel, var rvVeiculos: RecyclerView) : RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder>() {

    override fun getItemCount(): Int {
        return veiculos.size
    }

    fun setList(veiculos: MutableList<Veiculo>) {
        this.veiculos = veiculos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeiculoViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_veiculo, parent, false)
        return VeiculoViewHolder(v)
    }

    override fun onBindViewHolder(holder: VeiculoViewHolder, i: Int) {

        val veiculo = veiculos[i]
        holder.tvMarca.text = veiculo.marca
        holder.tvModelo.text = veiculo.modelo
        holder.tvPlaca.text = veiculo.placa
        holder.btExcluir.setOnClickListener {
            removeVehicle(veiculo.codigo)
        }
        holder.btEditar.setOnClickListener {
            updateVehicle(veiculo)
        }

    }

    private fun updateVehicle(vehicle: Veiculo) {

        var i = Intent(context, InformationActivity::class.java)

        i.putExtra("veiculoCodigo", vehicle.codigo)
        i.putExtra("veiculoMotorista", vehicle.motorista!!.toLong())
        i.putExtra("veiculoMontadora", vehicle.marca)
        i.putExtra("veiculoModelo", vehicle.modelo)
        i.putExtra("veiculoAno", vehicle.ano)
        i.putExtra("veiculoPlaca", vehicle.placa)

        context.startActivity(i)
        (context as Activity).finish()

    }

    private fun removeVehicle(vehicle: Long) {

        VeiculoController()
            .removeVehicle(vehicle, this.context) {
                if(it.status) {
                    removeVehicleFromList(vehicle)
                    listaViewModel.updateList(this, rvVeiculos, veiculos)
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun removeVehicleFromList(vehicle: Long) {

        this.veiculos.removeAt(this.veiculos.indexOfFirst { it.codigo == vehicle })

    }

    class VeiculoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvMarca: TextView = v.findViewById(R.id.tvMarca)
        var tvModelo: TextView = v.findViewById(R.id.tvModelo)
        var tvPlaca: TextView = v.findViewById(R.id.tvPlaca)
        var btExcluir: Button = v.findViewById(R.id.btnRemove)
        var btEditar: Button = v.findViewById(R.id.btnEdit)
    }

}