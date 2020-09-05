package com.example.trackerhabits.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.Habit
import com.example.domain.usecases.HabitIntercept
import com.example.domain.HabitType
import com.example.trackerhabits.MainActivity
import com.example.trackerhabits.R
import com.example.trackerhabits.viewModels.AddOrEditHabitViewModel
import kotlinx.android.synthetic.main.activity_add_habit.*
import kotlinx.android.synthetic.main.activity_add_habit.view.*
import java.util.*
import javax.inject.Inject

class AddOrEditHabitFragment : Fragment() {

    private val colors = arrayOf(
        "#FF5722",
        "#F44336",
        "#BDBDBD",
        "#FF4081",
        "#E040FB",
        "#7C4DFF",
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

    private var currentColor: Int = 0

    private lateinit var mModel: AddOrEditHabitViewModel

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    @Inject
    lateinit var habitIntercept: HabitIntercept

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.activity_add_habit, container, false)

        view.toolbar_text.text = "Новая привычка"
        view.button_add_habit.text = "Добавить привычку"

        view.button_delete.visibility = View.GONE

        view.toolbar_add_habit.setNavigationOnClickListener()
        {
            requireActivity().onBackPressed()
        }

        return view
    }

    private fun createSpinnerPriority()
    {
        val types = arrayOf("Высокий приоритет", "Средний приоритет", "Низкий приоритет")
        spinnerAdapter = ArrayAdapter(requireContext().applicationContext,
            android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        requireView().spinner_priority_habit.adapter = spinnerAdapter
    }

    private fun settingHabitTypeTag()
    {
        for (i in 0..requireView().radio_group_types.childCount)
        {
            val radioButton = requireView().radio_group_types.getChildAt(i)
            when (radioButton?.id)
            {
                R.id.radio_button_type_first -> radioButton.tag = HabitType.GOOD
                R.id.radio_button_type_second -> radioButton.tag = HabitType.BAD
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).appComponent.inject(this)

        mModel = ViewModelProvider(this, object : ViewModelProvider.Factory
        {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddOrEditHabitViewModel(requireActivity().application, habitIntercept) as T
            }

        }).get(AddOrEditHabitViewModel::class.java)

        //Setting habit type
        settingHabitTypeTag()

        //Create Spinner Priority
        createSpinnerPriority()

        //Setting Color Picker
        createColorPicker()

        val oldHabit: Habit? = arguments?.getSerializable("habit") as Habit?
        if (oldHabit != null)
        {
            view.toolbar_text.text = "Изменение привычки"
            view.button_add_habit.text = "Изменить привычку"
            view.button_delete.visibility = View.VISIBLE
            view.button_delete.setOnClickListener {
                deleteHabit(oldHabit)
                Toast.makeText(requireContext(), "Привычка успешно удалена", Toast.LENGTH_SHORT)
                   .show()
                requireActivity().onBackPressed()
            }
            loadHabitData(oldHabit)
        }

        button_add_habit.setOnClickListener()
        {
            if (fieldsIsEmpty())
            {
                val builder = AlertDialog.Builder(activity)
                builder
                    .setMessage("Введите все данные")
                    .setTitle("Ошибка!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }
            else
            {
                val habit = createHabit()
                if (oldHabit != null)
                {
                    val newHabit = habit.apply {
                        id = oldHabit.id
                        uid = oldHabit.uid
                    }
                    mModel.updateHabit(newHabit)
                }
                else
                    mModel.addHabit(habit)
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun deleteHabit(habit: Habit)
    {
        mModel.deleteHabit(habit)
    }

    private fun createHabit(): Habit
    {
        val checkedTypeId = requireView().radio_group_types.checkedRadioButtonId

        val type = requireView().findViewById<RadioButton>(checkedTypeId).tag as HabitType
        val stringPriority = requireView().spinner_priority_habit.selectedItem.toString()
        val priority = spinnerAdapter.getPosition(stringPriority)
        val frequency = requireView().et_period.text.toString().toInt()
        val count = requireView().et_count.text.toString().toInt()
        val title = requireView().et_title_habit.text.toString()
        val description = requireView().et_desc_habit.text.toString()
        val date = Date().time

        return Habit(
            0,
            null,
            title,
            description,
            currentColor,
            type,
            priority,
            frequency,
            date,
            count
        )
    }

    private fun fieldsIsEmpty(): Boolean
    {
        return  requireView().et_title_habit.text.isEmpty() ||
                requireView().et_desc_habit.text.isEmpty() ||
                requireView().et_count.text.isEmpty() ||
                requireView().et_period.text.isEmpty() ||
                requireView().radio_group_colors.checkedRadioButtonId == -1 ||
                requireView().radio_group_types.checkedRadioButtonId == -1
    }

    private fun loadHabitData(editHabit: Habit)
    {
        requireView().et_title_habit.setText(editHabit.title)
        requireView().et_desc_habit.setText(editHabit.describe)
        requireView().et_count.setText(editHabit.count.toString())
        requireView().et_period.setText(editHabit.frequency.toString())

        requireView().spinner_priority_habit.setSelection(editHabit.priority)

        if (editHabit.type == HabitType.GOOD)
            requireView().radio_group_types.check(R.id.radio_button_type_first)
        else
            requireView().radio_group_types.check(R.id.radio_button_type_second)

        currentColor = editHabit.color
        val button = requireView().radio_group_colors.findViewWithTag<RadioButton>(currentColor)
        requireView().radio_group_colors.check(button!!.id)
    }

    private fun createColorPicker()
    {
        for (i in colors)
        {
            val radioButton = RadioButton(activity)
            val layerDrawable = ContextCompat.getDrawable(requireActivity(),
                R.drawable.color_picker_capsule)
            radioButton.buttonDrawable = layerDrawable
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
            requireView().radio_group_colors.addView(radioButton)
        }
    }
}
