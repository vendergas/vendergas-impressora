package vendergas.impressora.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Page<T> {

    @Expose
    @SerializedName("items")
    var items: List<T>? = null

    @Expose
    @SerializedName("total_count")
    var total_count: Int? = null

    @Expose
    @SerializedName("current_page")
    var current_page: Int? = null

    @Expose
    @SerializedName("page_size")
    var page_size: Int? = null

    @Expose
    @SerializedName("total_pages")
    var total_pages: Int? = null

    @Expose
    @SerializedName("per_page")
    var per_page: Int? = null

}