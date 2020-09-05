package com.example.trackerhabits.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Habit
import com.example.domain.usecases.HabitIntercept
import com.example.domain.HabitType
import com.example.trackerhabits.MainActivity
import com.example.trackerhabits.R
import com.example.trackerhabits.adapters.ClickOnHabitListener
import com.example.trackerhabits.adapters.ClickDoneHabitListener
import com.example.trackerhabits.adapters.HabitsAdapter
import com.example.trackerhabits.viewModels.HabitsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class HabitsListFragment() : Fragment()
{
    private var recyclerList: RecyclerView? = null

    private lateinit var mModel: HabitsViewModel
    private lateinit var mRecyclerAdapter: HabitsAdapter
    private lateinit var mSearchEditText: EditText

    private lateinit var typeOfHabits: HabitType

    @Inject
    lateinit var habitIntercept: HabitIntercept

    companion object
    {
        fun getInstance(typeOfHabits: HabitType): HabitsListFragment
        {
            val bundle = Bundle().apply {
                putSerializable("typeOfHabits", typeOfHabits)
            }

            return HabitsListFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_list_habits, container, false)

        recyclerList = view.findViewById(R.id.list_habits_good)
        recyclerList?.itemAnimator = null

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeOfHabits = arguments?.getSerializable("typeOfHabits") as HabitType

        mRecyclerAdapter = HabitsAdapter()
        recyclerList?.adapter = mRecyclerAdapter

        (requireActivity() as MainActivity).appComponent.inject(this)

        mModel = ViewModelProvider(this, object : ViewModelProvider.Factory
        {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitsViewModel(requireActivity().application, habitIntercept, typeOfHabits) as T
            }
        }).get(HabitsViewModel::class.java)


        mModel.allHabitsLiveData.observe(viewLifecycleOwner)
        {
            mRecyclerAdapter.setDataList(it)
        }

        mModel.toastMessagesData.observe(viewLifecycleOwner)
        {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        mRecyclerAdapter.setOnClickListener(object : ClickOnHabitListener
        {
            override fun onHabitItemClicked(habit: Habit, position: Int)
            {
                val bundle = Bundle().apply()
                {
                    putSerializable("habit", habit)
                }
                val fragment = AddOrEditHabitFragment().apply { arguments = bundle }
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })
        
        mRecyclerAdapter.setDoneClickListener(object : ClickDoneHabitListener
        {
            override fun doneHabitListener(habit: Habit)
            {
                mModel.doneHabit(habit)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        //Setting search
        mSearchEditText = activity?.findViewById(R.id.et_search)!!
        mSearchEditText.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                val searchResult = mModel.getListOfHabitsFromQuery(s.toString())
                mRecyclerAdapter.setDataList(searchResult)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //Setting bottom sheet when scrolling
        recyclerList?.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0)
                {
                    (requireActivity() as MainActivity).bottomSheetBehavior.isHideable = true
                    (requireActivity() as MainActivity).bottomSheetBehavior.state =
                        BottomSheetBehavior.STATE_HIDDEN
                }
                else
                {
                    (requireActivity() as MainActivity).bottomSheetBehavior.isHideable = false
                    (requireActivity() as MainActivity).bottomSheetBehavior.state =
                        BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }


}
