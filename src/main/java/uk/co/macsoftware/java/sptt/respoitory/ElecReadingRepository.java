package uk.co.macsoftware.java.sptt.respoitory;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.macsoftware.java.sptt.model.ElecReading;

import java.util.List;

public interface ElecReadingRepository extends JpaRepository<ElecReading, Long> {
    void deleteAllByAccountId(Long accountId);

    List<ElecReading> findAllByAccountId(Long accountId);
}
