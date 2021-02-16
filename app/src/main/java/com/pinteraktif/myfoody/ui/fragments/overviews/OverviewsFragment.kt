package com.pinteraktif.myfoody.ui.fragments.overviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.databinding.FragmentOverviewBinding
import com.pinteraktif.myfoody.models.Result
import com.pinteraktif.myfoody.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewsFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments

        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        binding?.mainImageView?.load(myBundle?.image)
        binding?.likesTextView?.text = myBundle?.aggregateLikes.toString()
        binding?.timeTextView?.text = myBundle?.readyInMinutes.toString()
        binding?.tittleTextView?.text = myBundle?.title

//        view.summary_textView.text = myBundle?.summary
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding?.summaryTextView?.text = summary
        }

        if (myBundle?.vegetarian == true) {
            binding?.vegetarianImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.vegetarianTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.vegan == true) {
            binding?.veganImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.veganTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.glutenFree == true) {
            binding?.glutenFreeImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.glutenFreeTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.dairyFree == true) {
            binding?.dairyFreeImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.dairyFreeTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.veryHealthy == true) {
            binding?.healthyImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.healthyTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.cheap == true) {
            binding?.cheapImageView?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding?.cheapTextView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}