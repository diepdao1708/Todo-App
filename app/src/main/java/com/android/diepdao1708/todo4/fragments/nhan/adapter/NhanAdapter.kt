package com.android.diepdao1708.todo4.fragments.nhan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.diepdao1708.todo4.databinding.NhanItemBinding

class NhanAdapter (private val listener: OnItemClickListener): RecyclerView.Adapter<NhanAdapter.MyViewHolder>() {

    var nhanList = emptyList<String>()

    inner class MyViewHolder(val binding: NhanItemBinding) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener{

        fun bind(title: String){
            binding.textViewTitleNhan.text = title
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position, binding.textViewTitleNhan.text.toString())
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int, title: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(NhanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = nhanList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return nhanList.size
    }

    fun setData(title: List<String>){
        val toDoDiffUtil = ToDoDiffUtil(nhanList, title)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.nhanList = title
        toDoDiffResult.dispatchUpdatesTo(this)
    }

}
class ToDoDiffUtil(private val oldList: List<String>, private val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}