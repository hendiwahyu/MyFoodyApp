package com.pinteraktif.myfoody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.pinteraktif.myfoody.viewmodels.RecipesViewModel
import kotlinx.android.synthetic.main.recipes_bottom_sheet.*
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.lang.Exception
import java.util.*
import kotlin.properties.Delegates

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)


        /** Casting data from layout variable to data store variable */
        mView.mealType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedChip = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedChip
            mealTypeChipId = selectedChipId
        }


        mView.dietType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedChip = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedChip
            dietTypeChipId = selectedChipId
        }


        /** Store data from UI to data store */
        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            /**  Intent to Recipes Fragment  and pass data from bottom sheet with args */
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        /** Read data from data store */
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
            //viewing text type on chip
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            // Viewing checked id
            updateChip(it.selectedMealTypeId, mView.mealType_chipGroup)
            updateChip(it.selectedDietTypeId, mView.dietType_chipGroup)
        })

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception){
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

}