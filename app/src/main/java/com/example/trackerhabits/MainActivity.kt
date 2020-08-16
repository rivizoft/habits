package com.example.trackerhabits

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    val habitsList: MutableList<Habit> = mutableListOf();
    var buttonAdd: AppCompatImageButton? = null
    var recyclerList: RecyclerView? = null
    var textNotHabits: TextView? = null
    var floatingButton: FloatingActionButton? = null
    var positionOfSelectedItem: Int? = null

    val REQUEST_CODE_ADD_HABIT: Int = 1337
    val REQUEST_CODE_EDIT_HABIT: Int = 1488

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd = findViewById(R.id.button_add)
        recyclerList = findViewById(R.id.list_habits)
        textNotHabits = findViewById(R.id.tv_not_habits)
        floatingButton = findViewById(R.id.fab_main)

        habitsList.add(Habit("Сделать зарядку", "Тестовое описание", Color.parseColor("#FF9800")))
        habitsList.add(Habit("Помыться", "Тестовое описание, и немного подлинее даже очень", Color.parseColor("#FF9800")))
        recyclerList?.adapter = HabitsAdapter(habitsList)

        floatingButton?.setOnClickListener()
        {
            val intent = Intent(this, AddHabitActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_HABIT)
        }

        (recyclerList?.adapter as HabitsAdapter).setOnClickListener(object : ClickOnHabitListener {
            override fun onHabitItemClicked(habit: Habit, position: Int) {
                val intent = Intent(applicationContext, AddHabitActivity::class.java)
                intent.putExtra("habit", habit)
                startActivityForResult(intent, REQUEST_CODE_EDIT_HABIT)
                positionOfSelectedItem = position
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_HABIT && resultCode == Activity.RESULT_OK)
        {
            val habit = data?.getSerializableExtra("result") as Habit
            habitsList.add(0, habit)
            recyclerList?.adapter?.notifyDataSetChanged()
        }
        else if(requestCode == REQUEST_CODE_EDIT_HABIT)
        {
            val newHabit = data?.getSerializableExtra("result") as Habit
            habitsList[positionOfSelectedItem!!] = newHabit
            recyclerList?.adapter?.notifyDataSetChanged()
        }
    }
}
