package hanghae.application.dto.common;

import hanghae.domain.entity.Movie;
import hanghae.domain.type.AgeRating;
import hanghae.domain.type.Genre;

import java.time.LocalDate;

public record MovieResponse(
        String title,
        AgeRating ageRating,
        LocalDate releaseDate,
        String thumbnailUrl,
        int runningTime,
        Genre genre
) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getTitle(),
                movie.getAgeRating(),
                movie.getReleaseDate(),
                movie.getThumbnailUrl(),
                movie.getRunningTime(),
                movie.getGenre()
        );
    }
}
