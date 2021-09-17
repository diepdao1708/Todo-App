package com.android.diepdao1708.todo4.fragments.loinhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.databinding.LoinhacItemBinding
import com.android.diepdao1708.todo4.fragments.ToDoDiffUtil

class LoiNhacAdapter : RecyclerView.Adapter<LoiNhacAdapter.MyViewHolder>() {

    var loinhacList = emptyList<ToDoData>()

    class MyViewHolder(val binding: LoinhacItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoinhacItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = loinhacList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return loinhacList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(loinhacList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.loinhacList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}