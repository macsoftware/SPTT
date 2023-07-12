package uk.co.macsoftware.java.sptt.respoitory;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.macsoftware.java.sptt.model.GasReading;

import java.util.List;

public interface GasReadingRepository extends JpaRepository<GasReading, Long> {
    List<GasReading> findAllByAccountId(Long accountId);
    void deleteAllByAccountId(Long accountId);

}
