package com.example.trackerhabits.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Habit
import com.example.trackerhabits.R

class HabitsAdapter() : RecyclerView.Adapter<HabitsAdapter.HabitsHolder>()
{
    private var listener: ClickOnHabitListener? = null
    private var listenerDone: ClickDoneHabitListener? = null
    private var habits: List<Habit> = listOf()

    inner class HabitsHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false))
    {
        private var _title: TextView? = null
        private var _description: TextView? = null
        private var _color: ImageView? = null
        var _buttonDone: ImageButton? = null

        init
        {
            _title = itemView.findViewById(R.id.tv_title_card)
            _description = itemView.findViewById(R.id.tv_describe)
            _color = itemView.findViewById(R.id.iv_color_picker)
            _buttonDone = itemView.findViewById(R.id.button_done)
        }

        fun bind(habit: Habit)
        {
            _title?.text = habit.title
            _description?.text = habit.describe
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

        holder._buttonDone?.setOnClickListener()
        {
            listenerDone?.doneHabitListener(habit)
        }

        holder.itemView.setOnClickListener()
        {
            listener?.onHabitItemClicked(habit, position)
        }
    }

    fun setOnClickListener(listener: ClickOnHabitListener)
    {
        this.listener = listener
    }

    fun setDoneClickListener(listener: ClickDoneHabitListener)
    {
        listenerDone = listener
    }

    fun setDataList(habitsList: List<Habit>)
    {
        habits = habitsList
        notifyDataSetChanged()
    }
}

interface ClickDoneHabitListener
{
    fun doneHabitListener(habit: Habit)
}

interface ClickOnHabitListener
{
    fun onHabitItemClicked(habit: Habit, position: Int)
}