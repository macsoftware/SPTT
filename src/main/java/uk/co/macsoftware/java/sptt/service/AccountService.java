package uk.co.macsoftware.java.sptt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.macsoftware.java.sptt.model.ElecReading;
import uk.co.macsoftware.java.sptt.model.GasReading;
import uk.co.macsoftware.java.sptt.model.ReadResponse;
import uk.co.macsoftware.java.sptt.utils.MeterReadingUtil;

import java.util.List;

@Service
public class AccountService {

    private final GasReadingService gasReadingService;
    private final ElecReadingService elecReadingService;

    @Autowired
    public AccountService(GasReadingService gasReadingService, ElecReadingService elecReadingService) {
        this.gasReadingService = gasReadingService;
        this.elecReadingService = elecReadingService;
    }

    public ReadResponse getAccountByAccountNumber(Long accountNumber) {

        ReadResponse readResponse = new ReadResponse();
        readResponse.setAccountId(accountNumber);

        List<GasReading> gasReadings = gasReadingService.findAllGasReadingsByAccountId(accountNumber);
        List<ElecReading> elecReadings = elecReadingService.findAllElecReadingsByAccountId(accountNumber);

        readResponse.setGasReadings(MeterReadingUtil.updateReadings(gasReadings));
        readResponse.setElecReadings(MeterReadingUtil.updateReadings(elecReadings));

        ///TODO: Implement comparison logic, requires further clarification on what we are comparing.
        readResponse.setGasComparison(100);
        readResponse.setElecComparison(100);

        return readResponse;
    }

}
