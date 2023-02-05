package com.mikelike11.kinopoiskapp.screens.favoritefilms

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikelike11.kinopoiskapp.R
import com.mikelike11.kinopoiskapp.data.model.Film
import com.mikelike11.kinopoiskapp.databinding.FragmentFavoriteFilmsBinding
import com.mikelike11.kinopoiskapp.screens.filmdetails.FilmDetailsFragment
import com.mikelike11.kinopoiskapp.screens.searchfilms.SearchFilmsAdapter
import com.mikelike11.kinopoiskapp.screens.searchfilms.ViewModelFactory
import com.mikelike11.kinopoiskapp.utils.DataState

class FavoriteFilmsFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFilmsFragment()
    }

    private val viewModel: FavoriteFilmsViewModel by lazy {
        val factory = ViewModelFactory(
            requireActivity().application
        )
        ViewModelProvider(this, factory).get(FavoriteFilmsViewModel::class.java)
    }
    private lateinit var fragmentFavoriteFilmsBinding: FragmentFavoriteFilmsBinding
    private lateinit var adapter: SearchFilmsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavigationView =
            requireActivity().findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.visibility = View.VISIBLE
        fragmentFavoriteFilmsBinding = FragmentFavoriteFilmsBinding.inflate(inflater)
        val view = fragmentFavoriteFilmsBinding.root
        adapter =
            SearchFilmsAdapter(onItemClick = ::onItemClick, onItemLongClick = ::onItemLongClick)
        viewModel.favoriteFilms.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Error -> {
                    fragmentFavoriteFilmsBinding.favoriteSearch.visibility = View.GONE
                    fragmentFavoriteFilmsBinding.progressDialog.visibility = View.GONE
                }
                is DataState.Loading -> {
                    fragmentFavoriteFilmsBinding.favoriteSearch.visibility = View.GONE
                    fragmentFavoriteFilmsBinding.progressDialog.visibility = View.VISIBLE
                }
                is DataState.Success -> {
                    fragmentFavoriteFilmsBinding.progressDialog.visibility = View.GONE
                    fragmentFavoriteFilmsBinding.favoriteSearch.visibility = View.VISIBLE
                    adapter.films.clear()
                    adapter.setFilmList(it.data)
                }

            }
        }
        fragmentFavoriteFilmsBinding.favoriteSearch.setOnSearchClickListener {
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            fragmentFavoriteFilmsBinding.favoriteTitleText.visibility = View.GONE
        }
        fragmentFavoriteFilmsBinding.favoriteSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getFavoriteFilmsByName(newText ?: "")
                return true
            }

        })
        fragmentFavoriteFilmsBinding.favoriteSearch.setOnCloseListener {
            fragmentFavoriteFilmsBinding.favoriteSearch.layoutParams.width = 0
            fragmentFavoriteFilmsBinding.favoriteTitleText.visibility = View.VISIBLE
            false
        }
        viewModel.getFavoriteFilms()
        fragmentFavoriteFilmsBinding.favoriteFilmList.adapter = adapter
        return view
    }

    private fun onItemClick(film: Film) {
        val bottomNavigationView =
            requireActivity().findViewById(R.id.navigation) as BottomNavigationView
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bottomNavigationView.visibility = View.GONE
            fragmentFavoriteFilmsBinding.favoriteSearch.layoutParams.width = 0
            fragmentFavoriteFilmsBinding.favoriteSearch.onActionViewCollapsed()
            fragmentFavoriteFilmsBinding.favoriteTitleText.visibility = View.VISIBLE
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, FilmDetailsFragment.newInstance(film.filmId))
                .addToBackStack(null)
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.fragment_container_view4,
                    FilmDetailsFragment.newInstance(film.filmId)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun onItemLongClick(film: Film, position: Int) {
        viewModel.deleteFavoriteFilm(film)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeRemoved(position, adapter.itemCount)
    }
}