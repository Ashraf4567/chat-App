package com.example.chatapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemRoomBinding
import com.example.chatapp.ui.model.Category
import com.example.chatapp.ui.model.Room

class RoomsRecyclerAdapter(var rooms: List<Room>? = listOf()) :
    RecyclerView.Adapter<RoomsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemRoomBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(room: Room?) {
            itemBinding.image.setImageResource(
                Category.getCategoryImageByCategoryId(room?.categoryId)
            )
            itemBinding.title.text = room?.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(viewBinding)
    }

    override fun getItemCount() = rooms?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rooms!![position])
        onItemClickListener.let {
            holder.itemView.setOnClickListener { view ->
                it?.onItemClick(position, rooms!![position])
            }
        }
    }


    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, room: Room)
    }

    fun changeData(it: List<Room>?) {
        rooms = it
        notifyDataSetChanged()
    }
}