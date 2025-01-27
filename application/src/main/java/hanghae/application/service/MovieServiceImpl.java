package hanghae.application.service;

import hanghae.application.dto.MoviePlayingResponse;
import hanghae.application.port.MovieService;
import hanghae.domain.entity.Movie;
import hanghae.domain.port.MovieRepository;
import hanghae.domain.type.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<MoviePlayingResponse> findMoviesPlayingWithFilters(String title, Genre genre) {
        List<Movie> movies =
                movieRepository.findMoviesPlayingWithFilters(LocalDate.now(), title, genre);

        return movies.stream()
                .map(MoviePlayingResponse::of)
                .toList();
    }
}
