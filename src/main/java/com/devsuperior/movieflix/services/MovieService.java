package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> listAllPaged(PageRequest pageRequest, Long genreId){
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
		Page<Movie> list = repository.findAllByGenre(genre, pageRequest);
		return list.map(x-> new MovieDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		try {
			Movie movie = repository.getOne(id);
			MovieDTO dto = new MovieDTO();
			dto.setId(movie.getId());
			dto.setTitle(movie.getTitle());
			dto.setSubTitle(movie.getSubTitle());
			dto.setYear(movie.getYear());
			dto.setImgUrl(movie.getImgUrl());
			dto.setSynopsis(movie.getSynopsis());
			dto.setGenre(new GenreDTO(movie.getGenre()));
			return dto;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> findReviewsByMovie(Long id){
		List<Review> list = reviewRepository.findAllByMovie(id);
		return list.stream().map(x -> new ReviewDTO(x)).collect(Collectors.toList());
		
	}
}
