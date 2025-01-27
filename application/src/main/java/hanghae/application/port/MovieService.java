package hanghae.application.port;

import hanghae.application.dto.MoviePlayingResponse;
import hanghae.domain.type.Genre;

import java.util.List;

public interface MovieService {
    List<MoviePlayingResponse> findMoviesPlayingWithFilters(
            String title,
            Genre genre
    );
}
