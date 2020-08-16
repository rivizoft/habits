package com.example.trackerhabits

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat

class AddHabitActivity : AppCompatActivity() {

    private val colors = arrayOf(
        "#FF5722",
        "#F44336",
        "#BDBDBD",
        "#FF4081",
        "#E040FB",
        "#7C4DFF",
        "#448AFF",
        "#448AFF",
        "#03A9F4",
        "#0097A7",
        "#FFC107",
        "#FFEB3B",
        "#FF5722",
        "#607D8B",
        "#795548",
        "#FF9800"
    )

    var currentColor: Int? = null
    var title: EditText? = null
    var description: EditText? = null
    var spinnerPriorities: Spinner? = null
    var groupTypes: RadioGroup? = null
    var count: EditText? = null
    var period: EditText? = null
    var radioGroupColorPicker: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        title = findViewById(R.id.et_title_habit)
        description = findViewById(R.id.et_desc_habit)
        spinnerPriorities = findViewById(R.id.spinner_priority_habit)
        groupTypes = findViewById(R.id.radio_group_types)
        count = findViewById(R.id.et_count)
        period = findViewById(R.id.et_period)

        //Create Spinner Priorities
        val spinner = findViewById<Spinner>(R.id.spinner_priority_habit)
        val types = arrayOf("Высокий", "Средний", "Низкий")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Create Color Picker
        radioGroupColorPicker = findViewById(R.id.radio_group_colors)
        createColorPicker()

        loadData()

        //Create habit
        val createButton = findViewById<Button>(R.id.button_add_habit)
        createButton.setOnClickListener()
        {
            if (title!!.text.isEmpty() || description!!.text.isEmpty() ||
                    count!!.text.isEmpty() || period!!.text.isEmpty() ||
                    radioGroupColorPicker?.checkedRadioButtonId == -1 ||
                groupTypes?.checkedRadioButtonId == -1)
            {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder
                    .setMessage("Введите все данные")
                    .setTitle("Ошибка!")
                    .setPositiveButton("OK", null)
                    .create()
                builder.show()
            }
            else
            {
                val habit = Habit(title!!.text.toString(),
                    description!!.text.toString(), currentColor!!)
                val returnIntent = Intent()
                returnIntent.putExtra("result", habit)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    private fun loadData()
    {
        val habit = (intent.getSerializableExtra("habit") ?: return) as Habit
        title?.setText(habit.title)
        description?.setText(habit.describe)
        val radioButtonColorId = radioGroupColorPicker?.findViewWithTag<RadioButton>(habit.color)?.id
        radioGroupColorPicker?.check(radioButtonColorId!!)
    }

    private fun createColorPicker() {
        for (i in colors)
        {
            val radioButton = RadioButton(this)
            val layerDrawable = ContextCompat.getDrawable(this, R.drawable.color_picker_capsule)
            radioButton.setButtonDrawable(layerDrawable)
            val drawableContainer = layerDrawable!!.constantState
                    as DrawableContainer.DrawableContainerState
            val children = drawableContainer.children
            val selectedItem = children[0] as LayerDrawable
            val enabledItem = children[1] as LayerDrawable
            val drawableSelectedItem = selectedItem.findDrawableByLayerId(R.id.item_selected)
                    as GradientDrawable
            val drawableEnabledItem = enabledItem.findDrawableByLayerId(R.id.item_enabled)
                    as GradientDrawable
            drawableEnabledItem.setColor(Color.parseColor(i))
            drawableSelectedItem.setColor(Color.parseColor(i))
            radioButton.tag = Color.parseColor(i)
            radioButton.setOnClickListener()
            {
                    v -> currentColor = v.tag as Int
            }
            radioGroupColorPicker?.addView(radioButton)
        }
    }
}
