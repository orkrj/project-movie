package hanghae.application.dto;

import hanghae.application.dto.common.MovieResponse;
import hanghae.application.dto.common.ScheduleResponse;
import hanghae.application.dto.common.ScreenResponse;
import hanghae.domain.entity.Movie;
import hanghae.domain.entity.Schedule;
import hanghae.domain.entity.Screen;
import hanghae.domain.port.MovieScheduleScreenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class MoviePlayingResponse {
    private MovieResponse movieResponse;
    private ScheduleResponse scheduleResponse;
    private ScreenResponse screenResponse;

    public static MoviePlayingResponse from(MovieScheduleScreenDto dto) {
        return MoviePlayingResponse.builder()
                .movieResponse(MovieResponse.of(dto))
                .scheduleResponse(ScheduleResponse.of(dto))
                .screenResponse(ScreenResponse.of(dto))
                .build();
    }
}

