package com.mikelike11.kinopoiskapp.screens.searchfilms

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
import com.mikelike11.kinopoiskapp.databinding.FragmentSearchFilmsBinding
import com.mikelike11.kinopoiskapp.screens.filmdetails.FilmDetailsFragment
import com.mikelike11.kinopoiskapp.utils.DataState

class SearchFilmsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFilmsFragment()
    }

    private lateinit var fragmentSearchFilmsBinding: FragmentSearchFilmsBinding
    private lateinit var adapter: SearchFilmsAdapter
    private val viewModel: SearchFilmsViewModel by lazy {
        val factory = ViewModelFactory(
            requireActivity().application)
        ViewModelProvider(this, factory).get(SearchFilmsViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavigationView = requireActivity().findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.visibility = View.VISIBLE
        fragmentSearchFilmsBinding = FragmentSearchFilmsBinding.inflate(inflater)
        val view = fragmentSearchFilmsBinding.root
        adapter = SearchFilmsAdapter(onItemClick = ::onItemClick, onItemLongClick = ::onItemLongClick)
        viewModel.topFilms.observe(viewLifecycleOwner){
            when(it){
                is DataState.Error -> {
                    fragmentSearchFilmsBinding.popularSearch.visibility  = View.GONE
                    fragmentSearchFilmsBinding.progressDialog.visibility = View.GONE
                    fragmentSearchFilmsBinding.popularError.visibility = View.VISIBLE
                    fragmentSearchFilmsBinding.searchFilmList.visibility = View.GONE
                }
                is DataState.Loading -> {
                    fragmentSearchFilmsBinding.popularSearch.visibility  = View.GONE
                    fragmentSearchFilmsBinding.popularError.visibility  = View.GONE
                    fragmentSearchFilmsBinding.progressDialog.visibility = View.VISIBLE
                    fragmentSearchFilmsBinding.searchFilmList.visibility = View.GONE
                }
                is DataState.Success ->  {
                    fragmentSearchFilmsBinding.popularSearch.visibility  = View.VISIBLE
                    fragmentSearchFilmsBinding.popularError.visibility  = View.GONE
                    fragmentSearchFilmsBinding.progressDialog.visibility = View.GONE
                    fragmentSearchFilmsBinding.searchFilmList.visibility = View.VISIBLE
                    adapter.setFilmList(it.data)
                }

            }
        }
        fragmentSearchFilmsBinding.retryPopular.setOnClickListener {
            viewModel.getTopFilms()
        }
        fragmentSearchFilmsBinding.popularSearch.setOnSearchClickListener {
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            fragmentSearchFilmsBinding.popularTitleText.visibility = View.GONE
        }
        fragmentSearchFilmsBinding.popularSearch.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getFavoriteFilmsByName(newText?:"")
                return true
            }

        })
        fragmentSearchFilmsBinding.popularSearch.setOnCloseListener {
            fragmentSearchFilmsBinding.popularSearch.layoutParams.width = 0
            fragmentSearchFilmsBinding.popularTitleText.visibility = View.VISIBLE
            false
        }
        viewModel.getTopFilms()
        fragmentSearchFilmsBinding.searchFilmList.adapter = adapter
        return view
    }

    private fun onItemClick(film: Film) {
        val bottomNavigationView = requireActivity().findViewById(R.id.navigation) as BottomNavigationView
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bottomNavigationView.visibility = View.GONE
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, FilmDetailsFragment.newInstance(film.filmId))
                .addToBackStack(null)
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view4, FilmDetailsFragment.newInstance(film.filmId))
                .addToBackStack(null)
                .commit()
        }
    }
    private fun onItemLongClick(film: Film, position:Int){
        viewModel.addtoFavorite(film)
        adapter.notifyItemChanged(position)
    }
}