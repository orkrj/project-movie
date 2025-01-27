package hanghae.infrastructure.adapter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.domain.entity.*;
import hanghae.domain.port.MovieRepository;
import hanghae.domain.type.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMovieRepositoryAdapter implements MovieRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Movie> findMoviesPlayingWithFilters(
            LocalDate now,
            String title,
            Genre genre
    ) {

        QMovie movie = QMovie.movie;
        QSchedule schedule = QSchedule.schedule;
        QScreen screen = QScreen.screen;
        QScreenSchedule screenSchedule = QScreenSchedule.screenSchedule;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(movie.releaseDate.loe(now))
                .and(movie.deletedAt.isNull());

        if (title != null && !title.trim().isEmpty()) {
            builder.and(movie.title.startsWith(title));
        }

        if (genre != null) {
            builder.and(movie.genre.eq(genre));
        }

        return queryFactory
                .selectDistinct(movie)
                .from(movie)
                .leftJoin(movie.schedules, schedule).fetchJoin()
                .leftJoin(schedule.screenSchedules, screenSchedule).fetchJoin()
                .leftJoin(screenSchedule.screen, screen).fetchJoin()
                .where(builder)
                .orderBy(
                        movie.releaseDate.desc(),
                        schedule.startDateTime.asc(),
                        screen.screenName.asc()
                )
                .fetch();
    }
}
