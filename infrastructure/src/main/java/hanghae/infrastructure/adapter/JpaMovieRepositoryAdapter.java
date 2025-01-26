package hanghae.infrastructure.adapter;

import hanghae.domain.entity.Movie;
import hanghae.domain.port.MovieRepository;
import hanghae.domain.type.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMovieRepositoryAdapter implements MovieRepository {
    @Override
    public List<Optional<Movie>> findMoviesPlayingWithFilters(String title, Genre genre) {
        return List.of();
    }
}
