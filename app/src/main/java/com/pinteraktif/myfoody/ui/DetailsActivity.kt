package com.pinteraktif.myfoody.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.adapters.PagerAdapter
import com.pinteraktif.myfoody.ui.fragments.ingredients.IngredientsFragment
import com.pinteraktif.myfoody.ui.fragments.instructions.InstructionsFragment
import com.pinteraktif.myfoody.ui.fragments.overviews.OverviewsFragment
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewsFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val tittles = ArrayList<String>()
        tittles.add("Overviews")
        tittles.add("Ingredients")
        tittles.add("Instructions")

        val adapter = PagerAdapter(resultBundle, fragments, tittles, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}