package com.pinteraktif.myfoody.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.adapters.PagerAdapter
import com.pinteraktif.myfoody.data.database.entities.FavoritesEntity
import com.pinteraktif.myfoody.ui.fragments.ingredients.IngredientsFragment
import com.pinteraktif.myfoody.ui.fragments.instructions.InstructionsFragment
import com.pinteraktif.myfoody.ui.fragments.overviews.OverviewsFragment
import com.pinteraktif.myfoody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var saveRecipeId = 0

    private lateinit var menuItem: MenuItem

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSaveRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveFavoriteRecipe(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFavoriteRecipe(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSaveRecipes(menuItem: MenuItem?) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoriteEntity ->
            try {
                for (saveRecipe in favoriteEntity) {
                    if (saveRecipe.result.id == args.result.id) {
                        menuItem?.let { changeMenuItemColor(it, R.color.yellow) }
                        saveRecipeId = saveRecipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("Detail Activity", e.message.toString())
            }

        })
    }


    private fun saveFavoriteRecipe(item: MenuItem) {
        val favoriteEntity = FavoritesEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true

    }

    private fun removeFavoriteRecipe(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(saveRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favorites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}