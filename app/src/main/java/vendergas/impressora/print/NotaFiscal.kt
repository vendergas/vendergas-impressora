package vendergas.impressora.print

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import inputservice.printerLib.NfePrinterA7
import org.apache.commons.io.IOUtils
import org.vudroid.core.DecodeServiceBase
import org.vudroid.pdfdroid.codec.PdfContext
import vendergas.impressora.R
import vendergas.impressora.driver.A7.BarCode
import vendergas.impressora.models.CommonFields
import vendergas.impressora.models.Endereco
import vendergas.impressora.models.NotaCobertura
import vendergas.impressora.models.enums.ItinerarioStatus
import java.io.*
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

class NotaFiscal {

    companion object {

        fun formatEnderecoFirstLine(e: Endereco?): String {
            var endFormated: String? = ""
            if (e != null) {
                if (e.endereco != null) endFormated = e.endereco
                if (e.numero != null && e.numero != "") endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.numero
                if (e.bairro != null && e.bairro != "") endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.bairro
            }
            return if (endFormated != null && endFormated != "") endFormated else "--";
        }

        fun formatEnderecoSecondLine(e: Endereco?): String {
            var endFormated: String? = ""
            if (e != null) {
                if (e.cidade != null) endFormated += (if (endFormated != null && endFormated != "") ", " else "") + e.cidade
                if (e.estadoAcronimo != null) endFormated += (if (endFormated != null && endFormated != "") (if (e.cidade != null) " - " else ", ") else "") + e.estadoAcronimo
            }
            return if (endFormated != null && endFormated != "") endFormated else "--";
        }

        fun stripAccents(s: String?): String {
            var _s = s ?: ""
            try {
                _s = Normalizer.normalize(_s, Normalizer.Form.NFD)
                _s = _s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return _s
        }

        fun createSample(ctx: Context?): Bitmap {
            var mBitmapLogo: Bitmap? = null
            var mBitmapBanco: Bitmap? = null

            try {
                // mBitmapLogo = BitmapFactory.decodeStream(assets.open("rtsys.png"))
                // mBitmapBanco = BitmapFactory.decodeStream(assets.open("santander.png"))
                // mBitmapLogo = BitmapFactory.decodeStream(assets.open("santander.png"))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var x = 0
            var y = 0
            val w = 576
            var h = w * 5
            val size_text = 32
            val size_legend = 22

            val fontTitleBold = Paint(Color.BLACK)
            fontTitleBold.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            fontTitleBold.textSize = (size_text * 1.2).toInt().toFloat()

            val fontText = Paint(Color.BLACK)
            fontText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            fontText.textSize = size_text.toFloat()

            val fontTextBold = Paint(Color.BLACK)
            fontTextBold.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            fontTextBold.textSize = size_text.toFloat()

            val fontLegend = Paint(Color.BLACK)
            fontLegend.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            fontLegend.textSize = size_legend.toFloat()

            val fontLegendBold = Paint(Color.BLACK)
            fontLegendBold.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            fontLegendBold.textSize = size_legend.toFloat()

            val p = Paint(Color.BLACK)
            p.style = Paint.Style.STROKE
            p.strokeWidth = 2f

            //------------------------------------------------
            //Bitmap não rotacionado usado para DANFE
            //------------------------------------------------

            val GRAPHICS_CONFIG = android.graphics.Bitmap.Config.RGB_565;

            val BitmapDanfe = Bitmap.createBitmap(w, h, GRAPHICS_CONFIG)
            val canvas_bmpDanfe = Canvas(BitmapDanfe)
            canvas_bmpDanfe.drawColor(Color.WHITE)

            // canvas_bmpDanfe.drawRect(x.toFloat(), y.toFloat(), w.toFloat(), (y + 90).toFloat(), p)

            /*
            if (mBitmapLogo != null) {
                x += 10
                // y += 10
                canvas_bmpDanfe.drawBitmap(mBitmapLogo, 0f, y.toFloat(), p)
                // x += (mBitmapLogo?.getWidth() ?: 0) + 10
                // y += (mBitmapLogo?.height ?: 0) + 10
            }
            */

            /**
             * CONVERTE PDF PARA PNG
             */
            if (ctx != null) {
                val decodeService = DecodeServiceBase(PdfContext())
                decodeService.setContentResolver(ctx.getContentResolver())

                val resource = ctx.resources.openRawResource(R.raw.test);
                // val resource = assets.open("santander.png");
                val pdf = File.createTempFile("stream2file", ".tmp")
                pdf.deleteOnExit()
                FileOutputStream(pdf).use { out -> IOUtils.copy(resource, out) }

                Log.d("Bitmap", "File isFile > ${pdf.isFile}")
                Log.d("Bitmap", "File name > ${pdf.name}")

                decodeService.open(Uri.fromFile(pdf))

                val pageCount = decodeService.getPageCount()
                for (i in 0 until pageCount) {
                    val page = decodeService.getPage(i)
                    val rectF = RectF(0f, 0f, 1f, 1f)

                    // val with = page.getWidth()
                    // val height = page.getHeight()

                    Log.d("Bitmap", "Page width > ${page.width}")
                    Log.d("Bitmap", "Page height > ${page.height}")

                    // do a fit center
                    val scaleBy = 576f / page.width.toFloat();

                    Log.d("Bitmap", "scaleBy > ${scaleBy}")

                    val with = (page.width * scaleBy).toInt()
                    val height = (page.height * scaleBy).toInt()

                    val bitmap = page.renderBitmap(with, height, rectF)

                    try {
                        // val outputFile = File("./", "${System.currentTimeMillis()}.jpeg")
                        // val outputStream = FileOutputStream(outputFile)

                        // a bit long running
                        // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                        // outputStream.close()

                        Log.d("Bitmap", "ByteCount > ${bitmap.byteCount}")

                        val out = ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        val bitmap_img = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()));
                        out.close();

                        if (bitmap_img != null) {
                            Log.d("Bitmap", "bitmap_img width > ${bitmap_img.width}")
                            Log.d("Bitmap", "bitmap_img height > ${bitmap_img.height}")

                            // x += 10
                            y += 10;
                            canvas_bmpDanfe.drawBitmap(bitmap_img, 1f, y.toFloat(), p)
                            y += bitmap_img.height + 10
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            /*
            y += size_text
            canvas_bmpDanfe.drawText("EXEMPLO GRAFICO", x.toFloat(), y.toFloat(), fontTitleBold)

            y += size_text
            canvas_bmpDanfe.drawText("LEOPARDO A7 ESC/P", x.toFloat(), y.toFloat(), fontTextBold)

            x = 0
            y = 90
            canvas_bmpDanfe.drawRect(x.toFloat(), y.toFloat(), w.toFloat(), (y + size_legend + size_text).toFloat(), p)
            x = 10
            y += size_legend
            canvas_bmpDanfe.drawText("Exemplo de CHAVE DE ACESSO", x.toFloat(), y.toFloat(), fontLegendBold)
            y += size_legend

            val chave = "00123789456963258741147852369987456321015973"
            canvas_bmpDanfe.drawText(chave, x.toFloat(), y.toFloat(), fontLegend)

            y += 20
            BarCode.drawBarCode128C(canvas_bmpDanfe, chave, x, y, w, 80)

            x = 0
            y += 105
            p.pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
            canvas_bmpDanfe.drawLine(0f, y.toFloat(), w.toFloat(), y.toFloat(), p)
            p.pathEffect = null

            y += 70
            */

            /*
            x = 0
            val ay = y

            //------------------------------------------------
            //Bitmap rotacionado usado para Boletos
            //------------------------------------------------

            val W = 1200
            val BitmapBoleto = Bitmap.createBitmap(w, W, android.graphics.Bitmap.Config.RGB_565)
            val g2 = Canvas(BitmapBoleto)
            g2.drawColor(Color.WHITE)

            g2.rotate(90f, (w / 2).toFloat(), (w / 2).toFloat())

            val linhaDigitavel = "03399.64355 86600.000003 08288.001012 1 61950000000123"
            val codigoBarras = "03391619500000001239643586600000000828800101"

            x = 0
            y = 0
            if (mBitmapBanco != null) {
                g2.drawBitmap(mBitmapBanco, x.toFloat(), y.toFloat(), p)
            }

            g2.drawLine(x.toFloat(), (y + 50).toFloat(), W.toFloat(), (y + 50).toFloat(), p)
            g2.drawRect(x.toFloat(), (y + 50).toFloat(), W.toFloat(), (w - 100).toFloat(), p)

            g2.drawText(linhaDigitavel, 250f, 40f, fontTextBold)

            g2.drawText("EXEMPLO DE ROTACIONAMENTO PARA IMPRESSÃO DE BOLETO.", 20f, (w / 2).toFloat(), fontTextBold)
            val b25 = BarI25()
            val i25 = b25.createI25(codigoBarras)
            g2.drawBitmap(i25, 20f, (w - 97).toFloat(), p)

            //------------------------------------------------

            x = 0
            y = ay
            g.drawBitmap(BitmapBoleto, x.toFloat(), y.toFloat(), p)

            //------------------------------------------------
            x = 0
            */

            y += 200 //  y += 1300

            h = y / 64
            h = (h + 1) * 64

            val BitmapReturn = Bitmap.createBitmap(BitmapDanfe.getWidth(), h, GRAPHICS_CONFIG)
            val g3 = Canvas(BitmapReturn)

            g3.drawBitmap(BitmapDanfe, 0f, 0f, p)

            Log.d("Bitmap", "BitmapReturn width > ${BitmapReturn.width}")
            Log.d("Bitmap", "BitmapReturn height > ${BitmapReturn.height}")

            return BitmapReturn
        }

        fun genRelacaoNFRemessa(nfeprinter: NfePrinterA7?, cobertura: NotaCobertura) {
            if (nfeprinter == null || cobertura == null) return;

            // #Impressão
            // header
            nfeprinter.getMobilePrinter().Reset()

            // descrição
            var valornfeStr = StringBuilder()
            valornfeStr.append("      RELACAO DE NOTAS FISCAIS POR REMESSA      \n")
            valornfeStr.append("------------------------------------------------\n")
            valornfeStr.append("Numero\n")
            valornfeStr.append("Modelo\n")
            valornfeStr.append("Dt.Emissao\n")
            valornfeStr.append("Destinatario\n")
            valornfeStr.append("Chave de Acesso\n")
            valornfeStr.append("Total(R$)\n")
            valornfeStr.append("------------------------------------------------\n")
            valornfeStr.append("                    REMESSA                     \n")
            valornfeStr.append("                                                \n")

            var estabelecimento: CommonFields.Estabelecimento? = null;

            if (cobertura.empresas?.size!! > 0) estabelecimento = cobertura.empresas?.get(0)
            else if (cobertura.lojas?.size!! > 0) estabelecimento = cobertura.lojas?.get(0)

            valornfeStr.append("${stripAccents(cobertura.serie)}-${stripAccents(cobertura.numero)}\n")
            valornfeStr.append("NF-e\n")
            valornfeStr.append("${cobertura.emissao}\n")
            valornfeStr.append("${stripAccents(estabelecimento?.nomeFantasia)}\n")
            valornfeStr.append("${stripAccents(cobertura.chave)}\n")
            valornfeStr.append("R$ ${String.format("%.2f", cobertura.valor ?: 0f)}\n")


            valornfeStr.append("                                                \n")
            valornfeStr.append("                  NOTAS FISCAIS                 \n")
            valornfeStr.append("                                                \n")

            (cobertura.itinerario ?: listOf()).forEach {
                if (it.status == ItinerarioStatus.VENDA_AUTORIZADA || it.status == ItinerarioStatus.VENDA_EMITIDA) {
                    var notaTipo = ""

                    when(it.tipoNota) {
                        "nfe" -> notaTipo = "NF-e"
                        "nfce" -> notaTipo = "NFc-e"
                        else -> notaTipo = it.tipoNota ?: ""
                    }

                    var emissao_nota_data: String? = null;
                    var emissao_nota_hora: String? = null;

                    if (it.emissaoNota != null) {
                        try {
                            val baseDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(it.emissaoNota)
                            emissao_nota_data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(baseDate)
                            emissao_nota_hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(baseDate)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    valornfeStr.append("${stripAccents(it.serieNota)}-${stripAccents(it.numeroNota)}\n")
                    valornfeStr.append("${stripAccents(notaTipo)}\n")
                    valornfeStr.append("${emissao_nota_data} ${emissao_nota_hora}\n")
                    valornfeStr.append("${stripAccents(it.natOperacao)}\n")
                    valornfeStr.append("${stripAccents(it.cliente?.getClienteNome() ?: "--")}\n")
                    valornfeStr.append("${it.chaveNota}\n")
                    valornfeStr.append("R$ ${String.format("%.2f", it.valor ?: 0f)}\n")
                    valornfeStr.append("                                                \n")
                }
            }


            try { nfeprinter.getMobilePrinter().SendString(valornfeStr.toString()) } catch (e: UnsupportedEncodingException) { e.printStackTrace() }

            // descrição
            nfeprinter.getMobilePrinter().Reset()
            valornfeStr = StringBuilder()
            valornfeStr.append("\n\n\n\n\n")

            try { nfeprinter.getMobilePrinter().SendString(valornfeStr.toString()) } catch (e: UnsupportedEncodingException) { e.printStackTrace() }
        }

        fun genNotaByClassA7(nfeprinter: NfePrinterA7?, venda: NotaCobertura.Itinerario, cobertura: NotaCobertura?) {
            if (nfeprinter == null) return;

            var estabelecimento: CommonFields.Estabelecimento? = null;

            if (venda.empresas?.size!! > 0) estabelecimento = venda.empresas?.get(0)
            else if (venda.lojas?.size!! > 0) estabelecimento = venda.lojas?.get(0)

            // #Impressão
            // header

            nfeprinter.getMobilePrinter().Reset()
            nfeprinter.genNotaHeaderByFields(
                if (venda.tipoNota == "nfe") NfePrinterA7.templateHeader else NfePrinterA7.templateHeaderNFCE,
                NfePrinterA7.tagsHeader,
                arrayOf(stripAccents(estabelecimento?.razaoSocial) ?: "", venda.numeroNota ?: "", venda.serieNota ?: "")
            )

            val tipoDocumentoAuxiliar = /* "NFe" */  if (venda.tipoNota == "nfe") "NFe" else "NFce";
            val numeroDocumentoAuxiliar = /* cobertura.numero */  venda.numeroNota;
            val serieDocumentoAuxiliar = /* cobertura.serie */  venda.serieNota;

            // descrição
            var valornfeStr = StringBuilder()
            valornfeStr.append("     Danfe Simplificado          1-Saida    \n")
            valornfeStr.append("     Documento Auxiliar da       $tipoDocumentoAuxiliar $numeroDocumentoAuxiliar\n")
            valornfeStr.append("     Nota Fiscal Eletronica      Serie $serieDocumentoAuxiliar\n")
            valornfeStr.append("                                                ")

            try {
                nfeprinter.getMobilePrinter().SendString(valornfeStr.toString())
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            if ((venda.chaveNota ?: "").length >= 43) {

                // accesskey
                nfeprinter.genAccessKey(venda.chaveNota)

                // barcode
                nfeprinter.genBarCode128HorNRI6(venda.chaveNota)
            } else {
                val vfds = StringBuilder()
                vfds.append("     ${stripAccents("CHAVE INVÁLIDA OU NÃO ENCONTRADA!")}    \n")
                nfeprinter.getMobilePrinter().SendString(vfds.toString())
            }

            // natop - é necessário resetar a impressora após a impressão do
            // código de barras, pois é nesta função é colocado o texto em
            // negrito

            nfeprinter.getMobilePrinter().Reset()
            nfeprinter.genAllByFields(
                NfePrinterA7.templateNatOp1,
                NfePrinterA7.tagsNatOp1, arrayOf(stripAccents(venda.natOperacao ?: ""), "") // NfePrinterA7.tagsNatOp1, arrayOf("Venda de Mercadoria, adquirida de", "terceiros")
            )

            val emitEnd: Endereco? = if ((estabelecimento?.enderecos ?: listOf()).size > 0)  estabelecimento?.enderecos?.get(0) else null

            // emitente
            nfeprinter.getMobilePrinter().Reset();
            nfeprinter.genAllByFields(
                NfePrinterA7.templateEmitter,
                NfePrinterA7.tagsEmitter,
                arrayOf(
                    stripAccents(estabelecimento?.razaoSocial),
                    stripAccents(formatEnderecoFirstLine(emitEnd)),
                    stripAccents(formatEnderecoSecondLine(emitEnd)),
                    emitEnd?.cep ?: "",
                    estabelecimento?.telefone ?: "",
                    estabelecimento?.cnpj ?: "",
                    estabelecimento?.inscricaoEstadual ?: ""
                )
            )

            val destEnd: Endereco? = if ((venda.cliente?.enderecos ?: listOf()).size > 0)  venda.cliente?.enderecos?.get(0) else null

            // destinatario

            var emissao_nota_data: String? = null;
            var emissao_nota_hora: String? = null;

            if (venda.emissaoNota != null) {
                try {
                    val baseDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(venda.emissaoNota)
                    emissao_nota_data = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(baseDate)
                    emissao_nota_hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(baseDate)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            nfeprinter.getMobilePrinter().Reset();
            nfeprinter.genAllByFields(
                NfePrinterA7.templateReceiver,
                NfePrinterA7.tagsReceiver,
                arrayOf(
                    stripAccents(venda.cliente?.getClienteNome()),
                    emissao_nota_data ?: "--",
                    stripAccents(formatEnderecoFirstLine(destEnd)),
                    stripAccents(formatEnderecoSecondLine(destEnd)),
                    emissao_nota_data ?: "--",
                    destEnd?.cep ?: "--",
                    venda.cliente?.telefone ?: "--",
                    (venda.cliente?.cnpj ?: venda.cliente?.cpf) ?: "--",
                    emissao_nota_hora ?: "--",
                    venda.cliente?.inscricaoEstadual ?: "--"
                ),
                1,
                NfePrinterA7.sizeslastitem,
                1
            )

            val produtos = mutableListOf<Array<String>>()

            venda.produtos?.forEach {
                produtos.add(
                    mutableListOf(
                        stripAccents(it.nome),
                        stripAccents(it.unCom),
                        (it.quantidade ?: "").toString(),
                        (it.valorUnitario ?: "").toString(),
                        ((it.valorUnitario ?: 0f) * (it.quantidade ?: 0).toFloat()).toString()
                    ).toTypedArray()
                )
            }

            // produtos
            nfeprinter.getMobilePrinter().Reset();
            nfeprinter.genNotaProductsByFields(
                NfePrinterA7.templateProduct,
                NfePrinterA7.tagsProduct, NfePrinterA7.sizesProduct,
                produtos.toTypedArray(),
                String.format("%.2f", venda.valor ?: 0f)
            )
            // descrição
            nfeprinter.getMobilePrinter().Reset()
            valornfeStr = StringBuilder()
            valornfeStr.append("\n\n\n\n\n")
            try {
                nfeprinter.getMobilePrinter()
                    .SendString(valornfeStr.toString())
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }

}