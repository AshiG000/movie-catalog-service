package io.javabrains.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@Autowired
	private WebClient.Builder builder;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		// get all rated movie IDs
		// for each movie ID, call movie info and get details
		// Put them all together

		//RestTemplate restTemplate = new RestTemplate(); //it is bad to create inside function, because 
		//everytime it will create object and then dispose it off! 
		//WebClient.Builder builder = WebClient.builder();
		
		List<Rating> ratings = Arrays.asList(new Rating("1234", 4), new Rating("5678", 3));

		return ratings.stream().map(rating -> {
			
			//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			Movie movie	= builder.build()
			.get()
			.uri("http://localhost:8082/movies/" + rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class)
			.block();
			
			return new CatalogItem(movie.getName(), rating.getRating(), "Test");

		}).collect(Collectors.toList());
		// return Collections.singletonList(new CatalogItem("Transformers", 4, "Test"));
	}
	
	//WebClient Builder is asynchronous in comparison to RestTemplate!

}
