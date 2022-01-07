package com.devsuperior.movieflix.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository repository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private UserService userService;
	
	@Transactional
	public ReviewDTO insert(ReviewDTO review) {
		try {
			Review entity = new Review();
			entity.setText(review.getText());
			Movie movie = movieRepository.getOne(review.getMovieId());
			entity.setMovie(movie);
			UserDTO userDTO = userService.getProfileAuthenticated();
			entity.setUser(copyUser(userDTO));
			entity = repository.save(entity);
			
			return new ReviewDTO(entity);
		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + review.getMovieId());
		}
			
	}
	
	public User copyUser (UserDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		return user;
		
	}
	

}
