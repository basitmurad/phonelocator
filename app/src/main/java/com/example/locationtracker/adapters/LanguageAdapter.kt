package com.example.locationtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R

class LanguageAdapter(
    private val languages: List<String>,
    private val onLanguageClick: (position: Int) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
        holder.itemView.setOnClickListener {
            onLanguageClick(position)
        }
    }

    override fun getItemCount(): Int = languages.size

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val languageTextView: TextView = itemView.findViewById(R.id.languageName)

        fun bind(language: String) {
            languageTextView.text = language
        }
    }
}
