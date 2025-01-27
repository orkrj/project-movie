package hanghae.domain.port;

import hanghae.domain.entity.Movie;
import hanghae.domain.type.Genre;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository {
    List<MovieScheduleScreenDto> findMoviesPlayingWithFilters(
            LocalDate now,
            String title,
            Genre genre
    );
}
