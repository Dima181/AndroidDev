package com.mirea.hristov.movieproject.domain.use_cases;

import com.mirea.hristov.movieproject.domain.models.Movie;
import com.mirea.hristov.movieproject.domain.repositories.MovieRepository;

public class GetFavoriteFilmUseCase {
    private final MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie execute() {
        return this.movieRepository.getMovie();
    }
}

