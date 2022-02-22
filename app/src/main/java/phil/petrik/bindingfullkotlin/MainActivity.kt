package phil.petrik.bindingfullkotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import phil.petrik.bindingfullkotlin.data.RequestTask
import phil.petrik.bindingfullkotlin.data.Film
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var buttonNew: MaterialButton? = null
    var buttonSync: MaterialButton? = null
    var buttonClose: MaterialButton? = null
    var buttonCloseEditor: MaterialButton? = null
    var buttonAlter: MaterialButton? = null
    var buttonSend: MaterialButton? = null
    var layoutFilmEditor: ConstraintLayout? = null
    var layoutFilmInspector: ConstraintLayout? = null
    var textViewFilmCim: TextView? = null
    var textViewFilmKategoria: TextView? = null
    var textViewFilmHossz: TextView? = null
    var textViewFilmErtekeles: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        handleListeners()
        buttonSync!!.callOnClick()
    }

    private fun handleListeners() {
        buttonNew!!.setOnClickListener {
            layoutFilmEditor!!.visibility = View.VISIBLE
            layoutFilmInspector!!.visibility = View.GONE
        }
        buttonSync!!.setOnClickListener { setFilms() }
        buttonClose!!.setOnClickListener {
            layoutFilmInspector!!.visibility = View.GONE
        }
        buttonCloseEditor!!.setOnClickListener {
            layoutFilmEditor!!.visibility = View.GONE
        }
        buttonAlter!!.setOnClickListener {
            layoutFilmEditor!!.visibility = View.VISIBLE
            layoutFilmInspector!!.visibility = View.GONE
        }
        buttonSend!!.setOnClickListener {
            sendFilm(
                Film.emptyFilm()
            )
        }
    }

    private fun init() {
        buttonNew = findViewById(R.id.button_New)
        buttonSync = findViewById(R.id.button_Sync)
        buttonClose = findViewById(R.id.buttonClose)
        buttonCloseEditor = findViewById(R.id.buttonCloseEditor)
        buttonAlter = findViewById(R.id.buttonAlter)
        buttonSend = findViewById(R.id.buttonSend)
        layoutFilmEditor = findViewById(R.id.layout_Film_Editor)
        layoutFilmInspector = findViewById(R.id.layout_Film_Inspector)
        textViewFilmCim = findViewById(R.id.filmCim)
        textViewFilmKategoria = findViewById(R.id.filmKategoria)
        textViewFilmHossz = findViewById(R.id.filmHossz)
        textViewFilmErtekeles = findViewById(R.id.filmErtekeles)
    }

    private fun setFilm(id: Int) {
        try {
            //TODO
            throw IOException()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setFilms() {
        try {
            //TODO
            throw IOException()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendFilm(film: Film) {
        if (film.id != null) {
            sendFilm(film, "PATCH")
            return
        }
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Módosítás")
        alertDialog.setMessage("Elvégzi a módosításokat?")
        alertDialog.setPositiveButton("Igen") { _, _ ->
            Toast.makeText(this@MainActivity, "Film: $film", Toast.LENGTH_SHORT).show()
            sendFilm(film, "POST")
        }
        alertDialog.setNegativeButton(
            "Nem"
        ) { _, _ ->
            layoutFilmEditor!!.visibility = View.GONE
        }
        alertDialog.show()
    }

    private fun sendFilm(film: Film, method: String) {
        Log.d("FilmJSON", film.toJson())
        try {
            val requestTask = RequestTask(
                "/film" + if (film.id == null) "" else "/" + film.id.toString(),
                method,
                film.toJson()
            )
            requestTask.lastTask = lastTask@{
                var toastText = "módosítás"
                if (method == "POST") {
                    toastText = "felvétel"
                }
                if (requestTask.response!!.code < 300) {
                    Toast.makeText(this@MainActivity, "Sikeres $toastText", Toast.LENGTH_SHORT)
                        .show()
                    layoutFilmEditor!!.visibility = View.GONE
                    return@lastTask
                }
                Log.d(
                    "Hívás / " + requestTask.response!!.code,
                    requestTask.response!!.content
                )
                Toast.makeText(this@MainActivity, "Sikertelen $toastText", Toast.LENGTH_SHORT)
                    .show()
            }
            requestTask.execute()
            setFilms()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleteFilm(id: Int) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Törlés")
        alertDialog.setMessage("Biztos törölni szeretné?")
        alertDialog.setPositiveButton("Igen") { _, _ ->
            try {
                val requestTask = RequestTask("/film/$id", "DELETE")
                requestTask.lastTask = lastTask@{
                    if (requestTask.response!!.code < 300) {
                        Toast.makeText(this@MainActivity, "Sikeresen törölve!", Toast.LENGTH_SHORT)
                            .show()
                        return@lastTask
                    }
                    Log.d(
                        "Hívás / " + requestTask.response!!.code,
                        requestTask.response!!.content
                    )
                    Toast.makeText(this@MainActivity, "Sikertelen törlés!", Toast.LENGTH_SHORT)
                        .show()
                }
                requestTask.execute()
                setFilms()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        alertDialog.setNegativeButton("Nem", null)
        alertDialog.show()
    }

    private fun createFilmButton(film: Film): MaterialButton {
        val buttonFilm = MaterialButton(this@MainActivity)
        buttonFilm.text = film.cim
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonFilm.layoutParams = lp
        buttonFilm.setOnClickListener { setFilm(film.id!!) }
        buttonFilm.setOnLongClickListener {
            deleteFilm(film.id!!)
            true
        }
        return buttonFilm
    }
}