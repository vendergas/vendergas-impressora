package vendergas.impressora.activity

import android.app.Activity
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vendergas.impressora.R

val Activity.autocomplete_cobertura: AutoCompleteTextView get() = findViewById(R.id.autocomplete_cobertura)
val Activity.autocomplete_destinatario: AutoCompleteTextView get() = findViewById(R.id.autocomplete_destinatario)
val Activity.autocomplete_produto: AutoCompleteTextView get() = findViewById(R.id.autocomplete_produto)
val Activity.baixar_boleto: Button get() = findViewById(R.id.baixar_boleto)
val Activity.boleto_inputs: LinearLayout get() = findViewById(R.id.boleto_inputs)
val Activity.buscar_cobertura: Button get() = findViewById(R.id.buscar_cobertura)
val Activity.busucar_venda: Button get() = findViewById(R.id.busucar_venda)
val Activity.button_sing_in: Button get() = findViewById(R.id.button_sing_in)
val Activity.chave: TextView get() = findViewById(R.id.chave)
val Activity.cliente_endereco: TextView get() = findViewById(R.id.cliente_endereco)
val Activity.cliente_nome: TextView get() = findViewById(R.id.cliente_nome)
val Activity.cnpj_input: EditText get() = findViewById(R.id.cnpj_input)
val Activity.coberturas_list: ListView get() = findViewById(R.id.coberturas_list)
val Activity.cpf_input: EditText get() = findViewById(R.id.cpf_input)
val Activity.dataEmissao: TextView get() = findViewById(R.id.dataEmissao)
val Activity.edit_email: EditText get() = findViewById(R.id.edit_email)
val Activity.editPassword: EditText get() = findViewById(R.id.edit_password)
val Activity.emissao: TextView get() = findViewById(R.id.emissao)
val Activity.emitir_boleto: Button get() = findViewById(R.id.emitir_boleto)
val Activity.emitir_nota: Button get() = findViewById(R.id.emitir_nota)
val Activity.erro_nf: TextView get() = findViewById(R.id.erro_nf)
val Activity.estabelecimento: TextView get() = findViewById(R.id.estabelecimento)
val Activity.fab: FloatingActionButton get() = findViewById(R.id.fab)
val Activity.forma_pagamento: Spinner get() = findViewById(R.id.forma_pagamento)
val Activity.imageView: ImageView get() = findViewById(R.id.imageView)
val Activity.imprimir_relacao_remessa: Button get() = findViewById(R.id.imprimir_relacao_remessa)
val Activity.iniciar_venda: Button get() = findViewById(R.id.iniciar_venda)
val Activity.itinerario_info: LinearLayout get() = findViewById(R.id.itinerario_info)
val Activity.itinerario_list: ListView get() = findViewById(R.id.itinerario_list)
val Activity.itinerario_title: TextView get() = findViewById(R.id.itinerario_title)
val Activity.loading: ProgressBar get() = findViewById(R.id.loading)
val Activity.loading_cliente_list: ProgressBar get() = findViewById(R.id.loading_cliente_list)
val Activity.loading_produtos_list: ProgressBar get() = findViewById(R.id.loading_produtos_list)
val Activity.minhas_coberturas: Button get() = findViewById(R.id.minhas_coberturas)
val Activity.motivo_erro_nf: LinearLayout get() = findViewById(R.id.motivo_erro_nf)
val Activity.nf_section_info: LinearLayout get() = findViewById(R.id.nf_section_info)
val Activity.nf_section_input: LinearLayout get() = findViewById(R.id.nf_section_input)
val Activity.nome_destinatario_selecionado: TextView get() = findViewById(R.id.nome_destinatario_selecionado)
val Activity.nova_venda: Button get() = findViewById(R.id.nova_venda)
val Activity.nova_venda_popup: ScrollView get() = findViewById(R.id.nova_venda_popup)
val Activity.nova_venda_voltar: Button get() = findViewById(R.id.nova_venda_voltar)
val Activity.observacoes_input: EditText get() = findViewById(R.id.observacoes_input)
val Activity.opFis: TextView get() = findViewById(R.id.opFis)
val Activity.pagamento: TextView get() = findViewById(R.id.pagamento)
val Activity.produto_list: ListView get() = findViewById(R.id.produto_list)
val Activity.produtos_info: TextView get() = findViewById(R.id.produtos_info)
val Activity.rota_title: TextView get() = findViewById(R.id.rota_title)
val Activity.status: TextView get() = findViewById(R.id.status)
val Activity.tipo_documento: Spinner get() = findViewById(R.id.tipo_documento)
val Activity.tipoNf: TextView get() = findViewById(R.id.tipoNf)
val Activity.title_boleto: TextView get() = findViewById(R.id.title_boleto)
val Activity.toolbar: Toolbar get() = findViewById(R.id.toolbar)
val Activity.valor: TextView get() = findViewById(R.id.valor)
val Activity.valorNota: TextView get() = findViewById(R.id.valorNota)
val Activity.vencimento_input: EditText get() = findViewById(R.id.vencimento_input)
