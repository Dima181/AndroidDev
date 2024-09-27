package com.mirea.hristov.movieproject.data.repository;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.mirea.hristov.movieproject.domain.models.Movie;
import com.mirea.hristov.movieproject.domain.repositories.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private SharedPreferences sharedPreferences = null;
    public MovieRepositoryImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean saveMovie(@NonNull Movie movie) {
        return sharedPreferences.edit()
                .putString("FAVOURITE_MOVIE_TITLE", movie.getName())
                .putInt("FAVOURITE_MOVIE_ID", movie.getId())
                .commit();
    }
    @Override
    public Movie getMovie() {
        return new Movie(
                sharedPreferences.getInt("FAVOURITE_MOVIE_ID", 0),
                sharedPreferences.getString("FAVOURITE_MOVIE_TITLE", null)
        );
    }
}
