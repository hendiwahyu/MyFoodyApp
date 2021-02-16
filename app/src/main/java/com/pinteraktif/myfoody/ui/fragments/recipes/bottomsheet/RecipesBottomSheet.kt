package com.pinteraktif.myfoody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pinteraktif.myfoody.databinding.RecipesBottomSheetBinding
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.pinteraktif.myfoody.viewmodels.RecipesViewModel
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {


    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)


        /** Casting data from layout variable to data store variable */
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedChip = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedChip
            mealTypeChipId = selectedChipId
        }


        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedChip = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedChip
            dietTypeChipId = selectedChipId
        }


        /** Store data from UI to data store */
        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            /**  Intent to Recipes Fragment  and pass data from bottom sheet with args */
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        /** Read data from data store */
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
            //viewing text type on chip
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            // Viewing checked id
            updateChip(it.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId, binding.dietTypeChipGroup)
        })

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

}