package com.mirea.hristov.movieproject.domain.repositories;

import com.mirea.hristov.movieproject.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
