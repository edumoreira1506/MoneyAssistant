package br.edu.utfpr.moneyassistant

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.moneyassistant.database.DatabaseHandler
import br.edu.utfpr.moneyassistant.databinding.ActivityFormBinding
import br.edu.utfpr.moneyassistant.model.Register
import java.text.SimpleDateFormat
import java.util.Locale

class FormActivity : AppCompatActivity() {
    private lateinit var database : DatabaseHandler
    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseHandler(this)

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                if (selectedItem == "Crédito") {
                    this@FormActivity.setCreditDetailOptions()
                } else {
                    this@FormActivity.setDebitDetailOptions()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nothing selected")
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            binding.spinnerType.adapter = adapter
        }

        this.setCreditDetailOptions()

        binding.cancelButton.setOnClickListener{
            finish()
        }

        binding.saveButton.setOnClickListener {
            this.saveRegister()
        }
    }

    private fun saveRegister() {
        try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(binding.dateInput.text.toString());

            database.insert(
                Register(
                    _id = 0,
                    type = binding.spinnerType.selectedItem.toString(),
                    detail = binding.spinnerDetails.selectedItem.toString(),
                    value = binding.valueInput.text.toString().toInt(),
                    date = date!!
                )
            )

            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_LONG).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao salvar registro ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setDebitDetailOptions() {
        ArrayAdapter.createFromResource(
            this,
            R.array.debit_details,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            binding.spinnerDetails.adapter = adapter
        }
    }

    private fun setCreditDetailOptions() {
        ArrayAdapter.createFromResource(
            this,
            R.array.credit_details,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            binding.spinnerDetails.adapter = adapter
        }
    }
}