package org.niit.MovieService.services;

import org.niit.MovieService.domain.Movie;
import org.niit.MovieService.domain.User;
import org.niit.MovieService.exceptions.MovieAlreadyPresent;
import org.niit.MovieService.exceptions.MovieNotFound;
import org.niit.MovieService.exceptions.UserAlreadyPresent;
import org.niit.MovieService.exceptions.UserNotFound;

import java.util.List;

public interface UserMovieService {

//    To get single user along with favorite movies list
    User getUserDetails(String email)throws UserNotFound;

//    To register user
    User registerUser(User user) throws UserAlreadyPresent;

//  To get a list of favourite movies
    List<Movie> getAllUserFavouriteMovies(String email) throws UserNotFound;

//    To save movie into user favourite list
      User saveMovieToFavouriteList(String email, Movie movie) throws UserNotFound, MovieAlreadyPresent;

//    To delete movie from user favourite list
    User deleteFavouriteMovie(String email, int movieId) throws UserNotFound, MovieNotFound;


//    To get a single movie
    Movie getSingleMovie(String email, int id) throws UserNotFound, MovieNotFound;

}
