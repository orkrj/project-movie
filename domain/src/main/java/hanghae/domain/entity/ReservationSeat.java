package hanghae.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "reservation_seat")
@NoArgsConstructor
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationSeatId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Getter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Getter @Setter
    private boolean isReserved;

    public static ReservationSeat of(Reservation reservation, Seat seat) {
        ReservationSeat reservationSeat = new ReservationSeat();

        reservationSeat.reservation = reservation;
        reservationSeat.seat = seat;

        return reservationSeat;
    }
}
