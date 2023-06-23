package vendergas.impressora.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import android.database.DataSetObserver
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.itinerario_item.view.*
import vendergas.impressora.R
import vendergas.impressora.models.Endereco
import vendergas.impressora.models.NotaCobertura
import vendergas.impressora.models.enums.ItinerarioStatus

class ItinerarioAdapter(val context: Context, val listItinerario: List<NotaCobertura.Itinerario> = listOf()): BaseAdapter() {

    override fun registerDataSetObserver(observer: DataSetObserver) {}
    override fun unregisterDataSetObserver(observer: DataSetObserver) {}

    override fun areAllItemsEnabled(): Boolean = false
    override fun isEnabled(position: Int): Boolean = true
    override fun getCount(): Int = listItinerario.size
    override fun getViewTypeCount(): Int = if (count > 0) count else super.getViewTypeCount()
    override fun isEmpty(): Boolean = false
    override fun getItem(position: Int): Any = position
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = false
    override fun getItemViewType(position: Int): Int = position

    fun formatEndereco(e: Endereco?): String {
        var endFormated: String? = null
        if (e != null) {
            if (e.endereco != null) endFormated = e.endereco
            if (e.numero != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.numero
            if (e.bairro != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.bairro
            if (e.cidade != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.cidade
            if (e.estadoAcronimo != null) endFormated += (if (endFormated != null && endFormated != "") (if (e.cidade != null) " - " else ", ") else "") + e.estadoAcronimo
            if (e.cep != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.cep
        }
        return endFormated ?: "--"
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val itinerario = listItinerario.get(position)
        val newView = if (view == null) LayoutInflater.from(context).inflate(R.layout.itinerario_item, null) else view ;

        newView.valor?.text = "R$ ${String.format("%.2f", itinerario.valor ?: 0f)}"
        newView.pagamento?.text = itinerario.pagamento?.getFormatedValue()
        newView.cliente?.text = itinerario.cliente?.getClienteNome() ?: "--"
        newView.endereco?.text = formatEndereco(itinerario.endereco)
        newView.status?.text = itinerario.status?.getFormatedValue()

        when(itinerario.status) {
            ItinerarioStatus.VENDA_AUTORIZADA -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
            ItinerarioStatus.VENDA_CANCELADA -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            ItinerarioStatus.VENDA_EMITIDA -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light))
            ItinerarioStatus.VENDA_NAO_INICIADA -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            ItinerarioStatus.ERRO_AO_EMITIR_NF -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            else -> newView.status?.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
        }

        var produtos = ""
        var count = 0
        (itinerario.produtos ?: listOf()).forEach { p ->
            produtos += "${if (count > 0) "\n" else ""}${p.quantidade}x ${p.nome} - ${p.tipoEstoque?.getFormatedValue()} | UN R$ ${String.format("%.2f", p.valorUnitario ?: 0f)}"
            ++count
        }

        newView.produtos?.text = produtos

        return newView;
    }

}