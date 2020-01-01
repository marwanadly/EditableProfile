package com.sparknetworks.editableprofile.utli

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import org.jetbrains.annotations.NotNull


class CitiesListAdapter(
    @NotNull
    context: Context,
    resource: Int
) : ArrayAdapter<String>(context, resource), Filterable {

    private val mlistData: ArrayList<String> = ArrayList()

    fun setData(list: List<String>) {
        mlistData.clear()
        mlistData.addAll(list)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = mlistData.size

    @Nullable
    override fun getItem(position: Int): String {
        return mlistData[position]
    }

    fun getObject(position: Int): String {
        return mlistData[position]
    }

    @NonNull
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mlistData
                    filterResults.count = mlistData.size
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}