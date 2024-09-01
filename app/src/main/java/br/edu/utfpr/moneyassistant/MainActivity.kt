package br.edu.utfpr.moneyassistant

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.moneyassistant.adapter.RegisterAdapter
import br.edu.utfpr.moneyassistant.database.DatabaseHandler
import br.edu.utfpr.moneyassistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var database : DatabaseHandler
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseHandler(this)

        binding.addButton.setOnClickListener {
            this.onAddCLick()
        }

        this.updateList()
    }

    private fun updateList() {
        val registers = database.list()

        binding.mainContainer.adapter = RegisterAdapter(this, registers)
        binding.mainContainer.setHasFixedSize(false)
    }

    override fun onStart() {
        super.onStart()

        this.updateList()
    }

    private fun onAddCLick() {
        val intent = Intent(this, FormActivity::class.java)
        startActivity(intent)
    }
}