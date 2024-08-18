package org.niit.MovieService.services;

import org.niit.MovieService.domain.Movie;
import org.niit.MovieService.domain.User;
import org.niit.MovieService.exceptions.MovieAlreadyPresent;
import org.niit.MovieService.exceptions.MovieNotFound;
import org.niit.MovieService.exceptions.UserAlreadyPresent;
import org.niit.MovieService.exceptions.UserNotFound;
import org.niit.MovieService.proxy.UserProxy;
import org.niit.MovieService.repository.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserMovieServiceImpl implements UserMovieService {

    private UserMovieRepository userMovieRepository;
    private UserProxy userProxy;

    @Autowired
    public UserMovieServiceImpl(UserMovieRepository userMovieRepository, UserProxy userProxy) {
        this.userMovieRepository = userMovieRepository;
        this.userProxy = userProxy;
    }


    @Override
    public User registerUser(User user) throws UserAlreadyPresent {

        if (userMovieRepository.findById(user.getEmail()).isPresent()) {
            throw new UserAlreadyPresent();
        } else {
            User user1 = userMovieRepository.save(user);
            userProxy.saveUser(user);
            return user1;

        }
    }

    @Override
    public User getUserDetails(String email) throws UserNotFound {
        if (userMovieRepository.findById(email).isEmpty()){
            throw  new UserNotFound();
        }
        else{
            return userMovieRepository.findById(email).get();
        }
    }



    @Override
    public List<Movie> getAllUserFavouriteMovies(String email) throws UserNotFound {

        if (userMovieRepository.findById(email).isEmpty()){
            throw new UserNotFound();
        }
        else {
            User user = userMovieRepository.findById(email).get();
            List<Movie> movieList = user.getFavouriteMovies();
            return movieList;
        }
    }


    @Override
    public User saveMovieToFavouriteList(String email, Movie movie)throws UserNotFound, MovieAlreadyPresent {

        if (userMovieRepository.findById(email).isEmpty()){
            throw new UserNotFound();
        }

        User user = userMovieRepository.findById(email).get();

        if (user.getFavouriteMovies() == null){
            List<Movie> movieList = new ArrayList<>();
            movieList.add(movie);
            user.setFavouriteMovies(movieList);
            return userMovieRepository.save(user);
        }
        List<Movie> favMovieList = user.getFavouriteMovies();

        for (Movie existingMovie : favMovieList) {

            if (existingMovie.getId() == movie.getId()) {
                throw new MovieAlreadyPresent();
            }
        }

        favMovieList.add(movie);
        user.setFavouriteMovies(favMovieList);


        return userMovieRepository.save(user);

    }

    @Override
    public User deleteFavouriteMovie(String email, int movieId)  throws UserNotFound, MovieNotFound {

        if (userMovieRepository.findById(email).isEmpty()){
            throw new UserNotFound();
        }

        User user = userMovieRepository.findById(email).get();

        List<Movie> movieList = user.getFavouriteMovies();
        boolean flag = false;

        Iterator<Movie> iterator = movieList.iterator();

        while(iterator.hasNext()){

            if (iterator.next().getId() == movieId){
                flag = true;
                iterator.remove();
                break;
            }
        }

        if (flag == false){
            throw new MovieNotFound();
        }

        user.setFavouriteMovies(movieList);

        return userMovieRepository.save(user);
    }


    @Override
    public Movie getSingleMovie(String email, int id) throws UserNotFound, MovieNotFound{

        if (userMovieRepository.findById(email).isEmpty()){
            throw new UserNotFound();
        }

        User user = userMovieRepository.findById(email).get();
        List<Movie> movieList = user.getFavouriteMovies();

        for (Movie movie : movieList){
            if (movie.getId() == id){
                return movie;
            }
        }

        throw new MovieNotFound();

    }

}
