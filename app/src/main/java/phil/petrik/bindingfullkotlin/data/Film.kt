package phil.petrik.bindingfullkotlin.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class Film(
    id: Int?,
    cim: String?,
    kategoria: String?,
    hossz: Int?,
    ertekeles: Int?
) {

    var id: Int? = null

    var cim: String? = null

    var kategoria: String? = null

    var hossz: Int? = null

    @field:SerializedName("ertekels")
    var ertekeles: Int? = null

    var hosszString: String
        get() = if (hossz == null) "" else hossz.toString()
        set(hossz) {
            try {
                this.hossz = hossz.toInt()
            } catch (e: Exception) {
                this.hossz = 0
            }
        }

    var ertekelesString: String
        get() = if (ertekeles == null) "" else ertekeles.toString()
        set(ertekels) {
            try {
                ertekeles = ertekels.toInt()
            } catch (e: Exception) {
                ertekeles = 0
            }
        }


    init {
        this.id = id
        this.cim = cim
        this.kategoria = kategoria
        this.hossz = hossz
        this.ertekeles = ertekeles
    }

    override fun toString(): String {
        return ("id:" + id + ", cim:" + cim + ", kategoria:" + kategoria
                + ", hossz:" + hossz + ", ertekeles:" + ertekeles)
    }

    fun toJson(): String {
        val gson: Gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        return gson.toJson(this)
    }

    companion object {
        fun emptyFilm(): Film {
            return Film(null, null, null, null, null)
        }
    }
}