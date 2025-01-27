package hanghae.application.dto;

import hanghae.application.dto.common.MovieResponse;
import hanghae.application.dto.common.ScheduleResponse;
import hanghae.application.dto.common.ScreenResponse;
import hanghae.domain.entity.Movie;
import hanghae.domain.entity.Schedule;
import hanghae.domain.entity.Screen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record MoviePlayingResponse(
        MovieResponse movieResponse,
        Set<ScreenScheduleResponse> screenScheduleResponses
) {

    public static MoviePlayingResponse of(Movie movie) {

        MovieResponse movieResponse = MovieResponse.from(movie);
        
        Set<ScreenScheduleResponse> screenScheduleResponses = new HashSet<>();

        List<Schedule> schedules = movie.getSchedules();
        for (Schedule schedule : schedules) {
            schedule.getScreenSchedules().stream()
                    .map(screenSchedule 
                            -> ScreenScheduleResponse.of(screenSchedule.getScreen(), schedule)
                    )
                    .forEach(screenScheduleResponses::add);
        }

        return new MoviePlayingResponse(movieResponse, screenScheduleResponses);
    }

    record ScreenScheduleResponse(
            ScreenResponse screenResponse,
            ScheduleResponse scheduleResponse
    ) {

        public static ScreenScheduleResponse of(Screen screen, Schedule schedule) {
            return new ScreenScheduleResponse(
                    ScreenResponse.from(screen),
                    ScheduleResponse.from(schedule)
            );
        }
    }
}

