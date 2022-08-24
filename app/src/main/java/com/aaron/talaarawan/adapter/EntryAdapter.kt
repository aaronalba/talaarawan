package com.aaron.talaarawan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.getFormattedDate
import com.aaron.talaarawan.databinding.ListItemEntryBinding

/**
 * List adapter for the list of entries shown in EntryListFragment.
 */
class EntryAdapter(
    private val onEntrySelected: (Entry) -> Unit
): ListAdapter<Entry, EntryAdapter.EntryViewHolder>(DiffCallback) {

    /**
     * ViewHolder for the List of [Entry].
     */
    class EntryViewHolder(private val binding: ListItemEntryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: Entry) {
            binding.itemDate.text = entry.getFormattedDate()
            binding.itemTitle.text = entry.entryTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        return EntryViewHolder(
            ListItemEntryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val current: Entry = getItem(position)
        holder.apply {
            itemView.setOnClickListener { onEntrySelected(current) }
            bind(current)
        }
    }

    companion object {
        /**
         * Determines how the list adapter computes for the difference of two Entry List
         * to update the UI efficiently.
         */
        val DiffCallback = object : DiffUtil.ItemCallback<Entry>() {
            override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
                return oldItem == newItem
            }
        }
    }
}