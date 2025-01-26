package hanghae.domain.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ScreenSchedule> screenSchedules = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public void setStartDateTimeAndEndDateTime(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
