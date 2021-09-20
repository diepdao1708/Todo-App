package com.android.diepdao1708.todo4.fragments.thungrac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.databinding.ThungracItemBinding
import com.android.diepdao1708.todo4.fragments.ToDoDiffUtil

class ThungRacAdapter : RecyclerView.Adapter<ThungRacAdapter.MyViewHolder>(){

    var thungracList = emptyList<ToDoData>()

    class MyViewHolder(val binding: ThungracItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ThungracItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = thungracList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return thungracList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(thungracList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.thungracList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}