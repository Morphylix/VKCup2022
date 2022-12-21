package com.morphylix.android.dzentest.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.dzentest.R
import com.morphylix.android.dzentest.databinding.CardViewCategoryBinding
import com.morphylix.android.dzentest.domain.model.Category

class CategoryAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_view_category,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.category = categories[position]
    }

    override fun getItemCount(): Int = categories.size
}

class CategoryViewHolder(val binding: CardViewCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)