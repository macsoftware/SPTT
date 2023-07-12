package uk.co.macsoftware.java.sptt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.macsoftware.java.sptt.model.Account;
import uk.co.macsoftware.java.sptt.model.ElecReading;
import uk.co.macsoftware.java.sptt.respoitory.AccountRepository;
import uk.co.macsoftware.java.sptt.respoitory.ElecReadingRepository;

import java.util.List;

@Service
public class ElecReadingService {

    private final AccountRepository accountRepository;
    private final ElecReadingRepository elecReadingRepository;

    @Autowired
    public ElecReadingService(AccountRepository accountRepository, ElecReadingRepository elecReadingRepository) {
        this.accountRepository = accountRepository;
        this.elecReadingRepository = elecReadingRepository;
    }

    public List<ElecReading> findAllElecReadings() {
        return elecReadingRepository.findAll();
    }

    public List<ElecReading> findAllElecReadingsByAccountId(Long accountId) {
        return elecReadingRepository.findAllByAccountId(accountId);
    }

    public ElecReading findElecReadingById(Long id) {
        return elecReadingRepository.findById(id).orElseThrow(() -> new RuntimeException("ElecReading not found"));
    }

    public ElecReading save(Long accountId, ElecReading elecReading) {
        Account account = accountRepository.findById(accountId).orElse(null);
        elecReading.setAccount(account);
        return elecReadingRepository.save(elecReading);
    }

    public void deleteAllByAccountId(Long accountId) {
        elecReadingRepository.deleteAllByAccountId(accountId);
    }

    public void deleteById(Long id) {
        elecReadingRepository.deleteById(id);
    }

    public void deleteAll() {
        elecReadingRepository.deleteAll();
    }

}
