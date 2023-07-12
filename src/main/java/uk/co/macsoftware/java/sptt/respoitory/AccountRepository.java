package uk.co.macsoftware.java.sptt.respoitory;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.macsoftware.java.sptt.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
