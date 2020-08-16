package com.example.trackerhabits

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class HabitsAdapter(private val habits: List<Habit>) : RecyclerView.Adapter<HabitsAdapter.HabitsHolder>()
{
    var listener: ClickOnHabitListener? = null

    inner class HabitsHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false))
    {
        private var _title: TextView? = null
        private var _description: TextView? = null
        private var _color: ImageView? = null

        init
        {
            _title = itemView.findViewById(R.id.tv_title_card)
            _description = itemView.findViewById(R.id.tv_describe)
            _color = itemView.findViewById(R.id.iv_color_picker)
        }

        fun bind(habit: Habit)
        {
            _title?.setText(habit.title)
            _description?.setText(habit.describe)
            _color?.setColorFilter(habit.color)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitsHolder(inflater, parent)
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsHolder, position: Int) {
        val habit: Habit = habits[position]
        holder.bind(habit)

        holder.itemView.setOnClickListener()
        {
            listener?.onHabitItemClicked(habit, position)
        }
    }

    fun setOnClickListener(listener: ClickOnHabitListener)
    {
        this.listener = listener
    }
}

interface ClickOnHabitListener
{
    fun onHabitItemClicked(habit: Habit, position: Int)
}