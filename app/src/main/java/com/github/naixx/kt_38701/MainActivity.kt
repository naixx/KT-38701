package com.github.naixx.kt_38701

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lisitem.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

abstract class SimpleListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T?> = object : DiffUtil.ItemCallback<T?>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
) : ListAdapter<T, SimpleListAdapter.ViewHolder>(diffCallback) {
    //we use kotlin android extensions for views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    abstract val layoutId: Int

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
}

class DiffCallback<T : WithId?> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem?.id == newItem?.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}

interface WithId {
    val id: Long
}

class GroupEntity(val id: Int)

private data class GroupModel(val group: GroupEntity?) : WithId {
    var selected: Boolean = false
    override val id = group?.id?.toLong() ?: -1
}

typealias ItemCallback<T> = (item: T) -> Unit


private class GroupAdapter(
    private val onItem: ItemCallback<GroupEntity>,
    private val onAdd: ItemCallback<Any?>
) : SimpleListAdapter<GroupModel>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item.group != null) {
            //TODO Here
            holder.itemView.group_feed_circle_item_text
            holder.itemView.apply {
                setOnClickListener {
                    onItem(item.group)
                }
            }
        } else
            holder.itemView.setOnClickListener { onAdd(Unit) }
    }

    fun select(e: GroupEntity) {
        currentList.forEach { it.selected = false }
        currentList.firstOrNull { it.group == e }?.let { it.selected = true }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun getItemViewType(position: Int): Int = R.layout.lisitem
    override val layoutId = 0
}

