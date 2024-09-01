package br.edu.utfpr.moneyassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.moneyassistant.R
import br.edu.utfpr.moneyassistant.model.Register
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterAdapter (val context: Context, val dataset: List<Register>) : RecyclerView.Adapter<RegisterAdapter.ItemViewHolder>() {
    class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textViewValue = view.findViewById<TextView>(R.id.textViewValue)
        val textViewDetail = view.findViewById<TextView>(R.id.textViewDetail)
        val testViewDate = view.findViewById<TextView>(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.textViewValue.text = (if (item.type ==  "Crédito") "+" else "-") + item.value.toString()
        holder.textViewDetail.text = item.detail

        val formatter = SimpleDateFormat("dd/MM/yy", Locale.US)

        holder.testViewDate.text = formatter.format(item.date)
    }
}