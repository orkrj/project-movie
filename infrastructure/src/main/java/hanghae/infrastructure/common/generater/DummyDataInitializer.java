package hanghae.infrastructure.common.generater;

import hanghae.domain.entity.*;
import hanghae.domain.type.AgeRating;
import hanghae.domain.type.Genre;
import hanghae.infrastructure.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

//@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements ApplicationRunner {

    private final JpaMovieRepository movieRepository;
    private final JpaScheduleRepository scheduleRepository;
    private final JpaScreenRepository screenRepository;
    private final JpaScreenScheduleRepository screenScheduleRepository;
    private final JpaSeatRepository seatRepository;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<Movie> allMovies = createMovies(500);
        List<Schedule> allSchedules = createSchedules(10000, allMovies);
        List<Screen> allScreens = createScreensAndSeats(50);

        createScreenSchedules(allSchedules, allScreens);
    }

    private List<Movie> createMovies(int movieCount) {
        List<Movie> movies = new ArrayList<>();
        AgeRating[] ageRatings = AgeRating.values();
        Genre[] genres = Genre.values();

        for (int i = 1; i <= movieCount; i++) {
            Movie movie = Movie.builder()
                    .title("Movie " + i)
                    .ageRating(ageRatings[random.nextInt(ageRatings.length)])
                    .releaseDate(getRandomDate())
                    .thumbnailUrl(getRandomThumbNailUrl(i))
                    .runningTime(90 + random.nextInt(91))
                    .genre(genres[random.nextInt(genres.length)])
                    .build();

            movieRepository.save(movie);
            movies.add(movie);
        }

        return movies;
    }

    private String getRandomThumbNailUrl(int i) {
        return "https://example.com/thumbnail/" + i + ".jpg";
    }

    private LocalDate getRandomDate() {
        return LocalDate.of(
                2024 + random.nextInt(6),
                1 + random.nextInt(12),
                1 + random.nextInt(28)
        );
    }

    private List<Schedule> createSchedules(int scheduleCount, List<Movie> allMovies) {
        List<Schedule> schedules = new ArrayList<>();
        int movieSize = allMovies.size();

        for (int i = 0; i < scheduleCount; i++) {
            Schedule schedule = new Schedule();

            Movie randomMovie = allMovies.get(random.nextInt(movieSize));
            schedule.setMovie(randomMovie);

            LocalDateTime startTime = LocalDateTime.now()
                    .plusDays(random.nextInt(60) - 30)
                    .withHour(random.nextInt(24))
                    .withMinute(0);

            int runningMin = randomMovie.getRunningTime();
            LocalDateTime endTime = startTime.plusMinutes(runningMin);
            schedule.setStartDateTimeAndEndDateTime(startTime, endTime);

            scheduleRepository.save(schedule);
            randomMovie.setSchedules(List.of(schedule));
            schedules.add(schedule);
        }

        return schedules;
    }

    private List<Screen> createScreensAndSeats(int screenCount) {
        List<Screen> screens = new ArrayList<>();

        for (int i = 1; i <= screenCount; i++) {
            Screen screen = new Screen();
            screen.setScreenName("Screen " + i);

            screenRepository.save(screen);

            // A~E, 1~5 좌석 25개
            for (char row = 'A'; row <= 'E'; row++) {
                for (int col = 1; col <= 5; col++) {
                    Seat seat = new Seat();
                    seat.setSeatNameAndScreen(row + String.valueOf(col), screen);

                    seatRepository.save(seat);
                }
            }

            screens.add(screen);
        }

        return screens;
    }

    private void createScreenSchedules(List<Schedule> allSchedules, List<Screen> allScreens) {
        int screenSize = allScreens.size();

        for (Schedule schedule : allSchedules) {
            int howMany = 1 + random.nextInt(3); // 1~3
            Set<Integer> chosenIndexes = new HashSet<>();

            while (chosenIndexes.size() < howMany) {
                chosenIndexes.add(random.nextInt(screenSize));
            }

            for (int screenIndex : chosenIndexes) {
                Screen screen = allScreens.get(screenIndex);

                ScreenSchedule sc = new ScreenSchedule(screen, schedule);
                screenScheduleRepository.save(sc);
            }
        }
    }
}
