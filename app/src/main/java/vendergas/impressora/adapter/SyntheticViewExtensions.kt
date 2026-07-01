package vendergas.impressora.adapter

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import vendergas.impressora.R

val View.button_remove_produto: Button get() = findViewById(R.id.button_remove_produto)
val View.chave: TextView get() = findViewById(R.id.chave)
val View.cliente: TextView get() = findViewById(R.id.cliente)
val View.emissao: TextView get() = findViewById(R.id.emissao)
val View.endereco: TextView get() = findViewById(R.id.endereco)
val View.estabelecimento: TextView get() = findViewById(R.id.estabelecimento)
val View.nome_produto: TextView get() = findViewById(R.id.nome_produto)
val View.pagamento: TextView get() = findViewById(R.id.pagamento)
val View.produtos: TextView get() = findViewById(R.id.produtos)
val View.quantidade_input: EditText get() = findViewById(R.id.quantidade_input)
val View.status: TextView get() = findViewById(R.id.status)
val View.valor: TextView get() = findViewById(R.id.valor)
val View.valor_input: EditText get() = findViewById(R.id.valor_input)
