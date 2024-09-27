package com.mirea.hristov.movieproject.domain.use_cases;

import com.mirea.hristov.movieproject.domain.models.Movie;
import com.mirea.hristov.movieproject.domain.repositories.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private final MovieRepository movieRepository;

    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie){
        return this.movieRepository.saveMovie(movie);
    }
}