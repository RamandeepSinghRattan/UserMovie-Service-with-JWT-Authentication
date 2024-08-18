package org.niit.MovieService.repository;

import org.niit.MovieService.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRepository extends MongoRepository<User, String> {
}
