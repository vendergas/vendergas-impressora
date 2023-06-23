package vendergas.impressora.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*
import vendergas.impressora.models.*

interface RetrofitInterface {

    @POST("/entregadores/auth/email")
    fun authByEmail(@Body data: Entregador): Observable<AuthResponse>

    @GET("/notaCobertura/entregador/{idEntregador}")
    fun getListaNotaCobertura(
        @Path("idEntregador") _idEntregador: String,
        @Query("searchString") searchString: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): Observable<List<NotaCobertura>>

    @GET("/notaCobertura/vendas/entregador/{idEntregador}")
    fun getListaVendasCobertura(@Path("idEntregador") _idEntregador: String): Observable<List<NotaCobertura.Itinerario>>

    @GET("/clientes/list/{idEntregador}")
    fun getListaCliente(
        @Path("idEntregador") _idEntregador: String,
        @Query("searchString") searchString: String,
        @Query("multiEmpresa") multiEmpresa: String? = null,
        @Query("multiLoja") multiLoja: String? = null,
        @Query("per_page") perPage: Int? = 10,
        @Query("page") page: Int? = 1,
        @Query("role") role: String? = "entregador"
    ): Observable<Page<Cliente>>

    @GET("/produtos/list/{idEntregador}")
    fun getListaProduto(
        @Path("idEntregador") _idEntregador: String,
        @Query("searchString") searchString: String,
        @Query("multiEmpresa") multiEmpresa: String? = null,
        @Query("multiLoja") multiLoja: String? = null,
        @Query("per_page") perPage: Int? = 10,
        @Query("page") page: Int? = 1,
        @Query("role") role: String? = "entregador"
    ): Observable<Page<NotaCobertura.Itinerario.Produto>>

    @POST("/pedidos/iniciarVendaItinerario/{idEntregador}")
    fun iniciarVenda(@Path("idEntregador") _idEntregador: String, @Body venda: Venda): Observable<NotaCobertura.Itinerario>

    @GET("/boleto/emitir_boleto/entregador/{idEntregador}")
    fun iniciarBoleto(
        @Path("idEntregador") _idEntregador: String,
        @Query("idPedido") pedido: String?,
        @Query("idCliente") cliente: String?,
        @Query("cpf_cnpj") cpfCnpjCliente: String?,
        @Query("nome") nomeCliente: String?,
        @Query("dataVencimento") dataVencimento: String?,
        @Query("notaCobertura") notaCobertura: String?,
        @Query("numeroDocumento") numeroDocumento: String?
    ): Observable<BoletoResponse>

    /*
    @PUT("/pedidos/entregadores/{deliverymanId}/{orderId}/{latitude}/{longitude}/{radius}")
    fun refuseOrder(
            @Path("deliverymanId") deliverymanId: String,
            @Path("orderId") orderId: String,
            @Path("latitude") latitude: Double,
            @Path("longitude") longitude: Double,
            @Path("radius") radius: Int
    ): Observable<Order?>
    */

    /*
    @PUT("/pedidos/{deliverymanId}")
    fun updateOrder(
            @Path("deliverymanId") deliverymanId: String,
            @Body data: Order
    ): Observable<Order>
    */

    /*
    @PUT("/pedidos/entregadores/{deliverymanId}/{orderId}")
    fun changeOrderStatus(
            @Path("deliverymanId") deliverymanId: String,
            @Path("orderId") orderId: String,
            @Body params: Order.Status
    ): Observable<ResponseBody>
    */

    /**
     * Recupera as informações de um determinado cep
    @GET("/estados/cep/{cep}")
    fun getCepInfo(@Path("cep") cep: String): Observable<CompleteAddress?>
     */

    /**
     * Recupera as informações de um pedido
    @GET("/pedidos/{idEntregador}/{idOrder}")
    fun getOrderInfo(@Path("idEntregador") idEntregador: String, @Path("idOrder") idOrder: String): Observable<Order>
     */
}
