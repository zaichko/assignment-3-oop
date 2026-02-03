package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.Movie;
import com.zaichko.digitalstore.repository.MovieRepository;

import java.util.List;

public class MovieService {

    private final CreatorService creatorService;
    private final MovieRepository movieRepo;

    public MovieService(CreatorService creatorService,
                        MovieRepository movieRepo){
        this.creatorService = creatorService;
        this.movieRepo = movieRepo;
    }

    public void createMovie(Movie movie) {
        movie.validate();

        Creator creator = creatorService.getCreatorById(movie.getCreator().getId());

        if (creator == null) {
            throw new ResourceNotFoundException("Creator with id " + movie.getCreator().getId() + " does not exist");
        }

        movieRepo.create(movie);
    }

    public List<Movie> getAllMovies(){
        return movieRepo.getAll();
    }

    public Movie getMovieById(int id){

        Movie movie = movieRepo.getById(id);

        if(movie == null){
            throw new ResourceNotFoundException("Movie not found with ID " + id);
        }

        return movie;

    }

    public void updateMovie(int id, Movie movie){

        movie.validate();

        if(movieRepo.getById(id) == null){
            throw new ResourceNotFoundException("Movie not found");
        }

        movieRepo.update(id, movie);

    }

    public void deleteMovie(int id){

        if(movieRepo.getById(id) == null){
            throw new ResourceNotFoundException("Movie not found");
        }

        movieRepo.delete(id);

    }

}
