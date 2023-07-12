package uk.co.macsoftware.java.sptt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.macsoftware.java.sptt.model.Account;
import uk.co.macsoftware.java.sptt.model.GasReading;
import uk.co.macsoftware.java.sptt.respoitory.AccountRepository;
import uk.co.macsoftware.java.sptt.respoitory.GasReadingRepository;

import java.util.List;

@Service
public class GasReadingService {

    private final AccountRepository accountRepository;
    private final GasReadingRepository gasReadingRepository;

    @Autowired
    public GasReadingService(AccountRepository accountRepository, GasReadingRepository gasReadingRepository) {
        this.accountRepository = accountRepository;
        this.gasReadingRepository = gasReadingRepository;
    }

    public List<GasReading> findAllGasReadings() {
        return gasReadingRepository.findAll();
    }

    public List<GasReading> findAllGasReadingsByAccountId(Long accountId) {
        return gasReadingRepository.findAllByAccountId(accountId);
    }

    public GasReading findGasReadingById(Long id) {
        return gasReadingRepository.findById(id).orElseThrow(() -> new RuntimeException("GasReading not found"));
    }

    public GasReading save(Long accountId, GasReading gasReading) {
        Account account = accountRepository.findById(accountId).orElse(null);
        gasReading.setAccount(account);
        return gasReadingRepository.save(gasReading);
    }

    public void deleteAllByAccountId(Long accountId) {
        gasReadingRepository.deleteAllByAccountId(accountId);
    }

    public void deleteById(Long id) {
        gasReadingRepository.deleteById(id);
    }

    public void deleteAll() {
        gasReadingRepository.deleteAll();
    }


}
