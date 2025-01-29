package hanghae.application.service;

import hanghae.application.dto.request.ReservationRequest;
import hanghae.application.dto.response.ReservationResponse;
import hanghae.application.port.MemberService;
import hanghae.application.port.ReservationService;
import hanghae.application.port.ScheduleService;
import hanghae.application.port.SeatService;
import hanghae.domain.entity.*;
import hanghae.domain.port.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ReservationResponse reserveSeat(ReservationRequest request) {
        Reservation reservation = initReservation(request);
        List<Seat> seats = getSeats(request);
        List<ReservationSeat> reservationSeats = toReservationSeats(reservation, seats);

        checkDoubleBooking(request);
        checkReservationPolicy(reservation, reservationSeats, 5);

        reservation.setReservationSeats(reservationSeats);
        reservationSeats.forEach(reservationSeat -> reservationSeat.setReserved(true));

        return ReservationResponse.from(reservationRepository.reserve(reservation));
    }

    private Reservation initReservation(ReservationRequest request) {
        Member member = memberService.findMemberById(request.memberId());
        Schedule schedule = scheduleService.findScheduleById(request.scheduleId());

        return Reservation.of(member, schedule);
    }

    private List<Seat> getSeats(ReservationRequest request) {
        return request.seatNames()
                .stream()
                .map(seatName -> seatService.findSeatBySeatNameAndScreenId(seatName, request.scheduleId()))
                .toList();
    }

    private List<ReservationSeat> toReservationSeats(Reservation reservation, List<Seat> seats) {
        return seats.stream()
                .map(seat -> ReservationSeat.of(reservation, seat))
                .toList();
    }

    private void checkDoubleBooking(ReservationRequest request) {
        List<Reservation> reservations = reservationRepository.findReservationByScheduleId(request.scheduleId())
                .orElse(new ArrayList<>());

        if (reservations.isEmpty()) {
            return;
        }

        if (isReserved(reservations)) {
            throw new IllegalArgumentException("이미 예매된 좌석입니다.");
        }
    }

    private boolean isReserved(List<Reservation> reservations) {
        return reservations.stream()
                .flatMap(reservation -> reservation.getReservationSeats().stream())
                .anyMatch(ReservationSeat::isReserved);
    }

    private void checkReservationPolicy(Reservation reservation, List<ReservationSeat> reservationSeats, int limit) {
        if (isReservationOverLimit(reservationSeats, limit)
                || isTotalReservationOverLimit(reservation, reservationSeats, limit)
        ) {
            throw new IllegalArgumentException("하나의 상영에 대해 한 사람당 최대 5개의 좌석만 예매 가능합니다.");
        }

        if (isAbusing(reservationSeats, limit)) {
            throw new IllegalArgumentException("하나의 상영에 대해 5개의 표를 예매하는 경우 좌석은 붙어있어야 합니다.");
        }
    }

    private boolean isReservationOverLimit(List<ReservationSeat> reservationSeats, int limit) {
        return reservationSeats.size() > limit;
    }

    private boolean isTotalReservationOverLimit(
            Reservation reservation,
            List<ReservationSeat> reservationSeats,
            int limit
    ) {
        List<Reservation> reservations = reservationRepository.findReservationByMemberIdAndScheduleId(
                reservation.getMemberId(),
                reservation.getScheduleId()
        ).orElse(new ArrayList<>());

        if (reservations.isEmpty()) {
            return false;
        }

        if (reservations.size() == 2) {
            throw new IllegalArgumentException("하나의 상영에 대해 최대 2개까지 예약할 수 있습니다.");
        }

        int count = reservations.getFirst().getReservationSeats().size();
        return count + reservationSeats.size() > limit;
    }

    private boolean isAbusing(List<ReservationSeat> reservationSeats, int limit) {
        if (reservationSeats.size() == limit) {
            return !isConsecutiveSeats(reservationSeats, limit);
        } return false;
    }

    private boolean isConsecutiveSeats(List<ReservationSeat> reservationSeats, int limit) {
        List<String> seatNames = getSeatNames(reservationSeats);
        return isSameRow(seatNames);
    }

    private List<String> getSeatNames(List<ReservationSeat> reservationSeats) {
        return reservationSeats.stream()
                .map(reservationSeat -> reservationSeat.getSeat().getSeatName())
                .sorted()
                .toList();
    }

    private boolean isSameRow(List<String> seatNames) {
        char row = seatNames.getFirst().charAt(0);

        return seatNames.stream().allMatch(seatName -> row == seatName.charAt(0));
    }
}
