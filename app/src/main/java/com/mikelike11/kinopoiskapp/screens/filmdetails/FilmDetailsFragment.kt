package com.mikelike11.kinopoiskapp.screens.filmdetails

import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mikelike11.kinopoiskapp.databinding.FragmentFilmDetailsBinding
import com.mikelike11.kinopoiskapp.screens.searchfilms.ViewModelFactory
import com.mikelike11.kinopoiskapp.utils.DataState

class FilmDetailsFragment : Fragment() {

    companion object {
        fun newInstance(id: Int): FilmDetailsFragment {
            val fragment = FilmDetailsFragment()
            val args = Bundle()
            args.putInt("FILM_ID", id)
            fragment.arguments = args
            return fragment
        }
    }

    private val film by lazy { requireArguments().getInt("FILM_ID") }

    private val viewModel: FilmDetailsViewModel by lazy {
        val factory = ViewModelFactory(
            requireActivity().application
        )
        ViewModelProvider(this, factory).get(FilmDetailsViewModel::class.java)
    }
    private lateinit var fragmentFilmDetailsBinding: FragmentFilmDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFilmDetailsBinding = FragmentFilmDetailsBinding.inflate(inflater)
        val view = fragmentFilmDetailsBinding.root
        viewModel.film.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Error -> {
                    fragmentFilmDetailsBinding.detailError.visibility = View.VISIBLE
                    fragmentFilmDetailsBinding.progressDialog.visibility = View.GONE
                }
                is DataState.Loading -> {
                    fragmentFilmDetailsBinding.detailError.visibility = View.GONE
                    fragmentFilmDetailsBinding.progressDialog.visibility = View.VISIBLE
                }
                is DataState.Success -> {
                    fragmentFilmDetailsBinding.detailError.visibility = View.GONE
                    fragmentFilmDetailsBinding.progressDialog.visibility = View.GONE
                    Glide.with(this).load(it.data.posterUrl)
                        .into(fragmentFilmDetailsBinding.filmDetailPoster)
                    fragmentFilmDetailsBinding.apply {
                        filmDetailTitle.text = it.data.nameRu
                        filmDetailDescription.text = it.data.description

                        filmDetailGenres.text = SpannableStringBuilder().bold { append("Жанры: ") }
                            .append(it.data.genres.map { it.genre }.joinToString(separator = ", "))
                        filmDetailCountries.text =
                            SpannableStringBuilder().bold { append("Страны: ") }
                                .append(it.data.countries.map { it.country }
                                    .joinToString(separator = ", "))
                    }
                }

            }
        }
        fragmentFilmDetailsBinding.retryDetail.setOnClickListener {
            viewModel.getFilmFullInfo(film)
        }
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentFilmDetailsBinding.filmDetailBack.visibility = View.VISIBLE
        } else {
            fragmentFilmDetailsBinding.filmDetailBack.visibility = View.GONE
        }
        fragmentFilmDetailsBinding.filmDetailBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        viewModel.getFilmFullInfo(film)
        return view
    }


}