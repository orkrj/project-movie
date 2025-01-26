package hanghae.infrastructure.repository;

import hanghae.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSeatRepository extends JpaRepository<Seat, Long> {
}
