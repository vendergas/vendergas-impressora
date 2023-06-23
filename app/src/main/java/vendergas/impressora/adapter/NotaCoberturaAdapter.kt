package vendergas.impressora.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import android.database.DataSetObserver
import android.view.View
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.cobertura_item.view.*
import vendergas.impressora.R
import vendergas.impressora.models.NotaCobertura

class NotaCoberturaAdapter(val context: Context, val listCoberturas: List<NotaCobertura> = listOf()): BaseAdapter() {

    override fun registerDataSetObserver(observer: DataSetObserver) {}
    override fun unregisterDataSetObserver(observer: DataSetObserver) {}

    override fun areAllItemsEnabled(): Boolean = false
    override fun isEnabled(position: Int): Boolean = true
    override fun getCount(): Int = listCoberturas.size
    override fun getViewTypeCount(): Int = if (count > 0) count else super.getViewTypeCount()
    override fun isEmpty(): Boolean = false
    override fun getItem(position: Int): Any = position
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = false
    override fun getItemViewType(position: Int): Int = position

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val cobertura = listCoberturas.get(position)
        val newView = if (view == null) LayoutInflater.from(context).inflate(R.layout.cobertura_item, null) else view;

        newView.chave?.text = "${cobertura.serie}-${cobertura.numero}";
        newView.valor?.text = "R$ ${String.format("%.2f", cobertura.valor ?: 0f)}";
        newView.emissao?.text = cobertura.emissao

        var estabelecimento_info = "";

        if (cobertura.empresa != null) estabelecimento_info += cobertura.empresa?.nomeFantasia;
        if (cobertura.loja != null) estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + cobertura.loja?.nomeFantasia;

        cobertura.empresas?.forEach {
            estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia;
        }

        cobertura.lojas?.forEach {
            estabelecimento_info += (if (estabelecimento_info != "") ", " else "") + it.nomeFantasia;
        }

        newView.estabelecimento?.text = estabelecimento_info

        return newView;
    }

}