package org.niit.MovieService.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.niit.MovieService.domain.Movie;
import org.niit.MovieService.domain.User;
import org.niit.MovieService.exceptions.MovieAlreadyPresent;
import org.niit.MovieService.exceptions.MovieNotFound;
import org.niit.MovieService.exceptions.UserAlreadyPresent;
import org.niit.MovieService.exceptions.UserNotFound;
import org.niit.MovieService.services.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2")
public class UserMovieController {

    UserMovieService userMovieService;

    @Autowired
    public UserMovieController(UserMovieService userMovieService) {
        this.userMovieService = userMovieService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyPresent {
        try{
            User newUser = userMovieService.registerUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
        catch (UserAlreadyPresent e){
            throw e;
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favMovie/movies")
    public ResponseEntity<?> getAllFavMovies(HttpServletRequest request)throws UserNotFound{

        String email = getUsernameFromClaims(request);

        List<Movie> movieList = userMovieService.getAllUserFavouriteMovies(email);

        return new ResponseEntity<>(movieList, HttpStatus.OK);
    }

    @PostMapping("/favMovie/movie")
    public ResponseEntity<?> saveMovieToList(HttpServletRequest request,@RequestBody Movie movie) throws UserNotFound, MovieAlreadyPresent {

        String email = getUsernameFromClaims(request);

        User user = userMovieService.saveMovieToFavouriteList(email, movie);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/favMovie/movie/{id}")
    public ResponseEntity<?> deleteFavMovieFromList(HttpServletRequest request, @PathVariable int id) throws UserNotFound, MovieNotFound {

        String email = getUsernameFromClaims(request);

        User user = userMovieService.deleteFavouriteMovie(email, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/favMovie/singleMovie/{id}")
    public ResponseEntity getSingleMovie(HttpServletRequest request,@PathVariable int id) throws UserNotFound, MovieNotFound {
        String email = getUsernameFromClaims(request);

        Movie movie = userMovieService.getSingleMovie(email, id);
        return new ResponseEntity(movie, HttpStatus.OK);

    }

    @GetMapping("/favMovie/getUser")
    public ResponseEntity<?> getUser(HttpServletRequest request) throws UserNotFound
    {
        String email = getUsernameFromClaims(request);
        User user = userMovieService.getUserDetails(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public String getUsernameFromClaims(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String email = (String) claims.get("email");
        return email;
    }
}
