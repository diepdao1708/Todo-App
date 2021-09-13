package com.android.diepdao1708.todo4.fragments.ghichu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.databinding.RecyclerviewItemBinding


class GhiChuAdapter : RecyclerView.Adapter<GhiChuAdapter.MyViewHolder>() {

    var ghichuList = emptyList<ToDoData>()

    class MyViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(todDoData: ToDoData){
            binding.toDoData = todDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = ghichuList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return ghichuList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(ghichuList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.ghichuList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }

}