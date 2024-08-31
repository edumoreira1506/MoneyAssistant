package br.edu.utfpr.moneyassistant

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.moneyassistant.adapter.RegisterAdapter
import br.edu.utfpr.moneyassistant.database.DatabaseHandler

class MainActivity : AppCompatActivity() {
    private lateinit var database : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = DatabaseHandler(this)

        val recyclerView = findViewById<RecyclerView>(R.id.main_container)
        val registers = database.list()

        recyclerView.adapter = RegisterAdapter(this, registers)

        recyclerView.setHasFixedSize(false)
    }
}