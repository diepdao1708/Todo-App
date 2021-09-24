package com.android.diepdao1708.todo4.fragments.nhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.databinding.ItemBinding
import com.android.diepdao1708.todo4.fragments.ToDoDiffUtil

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.MyViewHolder>(){

    var itemList = emptyList<ToDoData>()

    class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(itemList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.itemList= toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}