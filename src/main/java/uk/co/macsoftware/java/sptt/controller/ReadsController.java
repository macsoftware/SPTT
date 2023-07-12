package uk.co.macsoftware.java.sptt.controller;

import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.macsoftware.java.sptt.model.*;
import uk.co.macsoftware.java.sptt.service.AccountService;
import uk.co.macsoftware.java.sptt.service.ElecReadingService;
import uk.co.macsoftware.java.sptt.service.GasReadingService;
import uk.co.macsoftware.java.sptt.utils.MeterReadingUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/smart/reads")
public class ReadsController {

    private final AccountService accountService;
    private final GasReadingService gasReadingService;
    private final ElecReadingService elecReadingService;


    @Autowired
    public ReadsController(AccountService accountService, GasReadingService gasReadingService, ElecReadingService elecReadingService) {
        this.accountService = accountService;
        this.gasReadingService = gasReadingService;
        this.elecReadingService = elecReadingService;
    }

    @Operation(summary = "Get meter readings by account number", description = "View a list of available readings for a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource", content = @Content),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found", content = @Content)
    })
    @GetMapping("/{ACCOUNTNUMBER}")
    public ReadResponse getReadsByAccountNumber(@Parameter(description = "Account number for which to retrieve readings") @PathVariable Long ACCOUNTNUMBER) {
        return accountService.getAccountByAccountNumber(ACCOUNTNUMBER);
    }

    @Operation(summary = "Submit meter readings by account number", description = "Submit meter readings for a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new readings"),
            @ApiResponse(responseCode = "400", description = "Invalid input / Validation fail", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized to submit the readings", content = @Content)
    })
    @PostMapping("/{ACCOUNTNUMBER}/meter-readings")
    public ResponseEntity<?> submitMeterReadings(@Parameter(description = "Account number for which to retrieve readings") @PathVariable Long ACCOUNTNUMBER,
                                                 @Parameter(description = "Request object containing meter readings to be submitted") @RequestBody ReadRequest readRequest){

        String responseString = "";

        //Checks to see if there are any meter readings to validate and save.
        if(readRequest.getMeterReadings().size() == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //Get existing readings to validate against
        List<ElecReading> elecReadings = elecReadingService.findAllElecReadingsByAccountId(ACCOUNTNUMBER);
        List<GasReading> gasReadings = gasReadingService.findAllGasReadingsByAccountId(ACCOUNTNUMBER);

        for(MeterReadingRequest newMeterReading : readRequest.getMeterReadings()) {
            switch (newMeterReading.getMeterType()) {
                case ELEC:
                    for (ElecReading oldElecReading : elecReadings) {
                        //Validate new reading
                        responseString = MeterReadingUtil.validation(oldElecReading, newMeterReading);
                        if(responseString.length() > 0)
                            return new ResponseEntity<>(responseString, HttpStatus.BAD_REQUEST);
                    }
                    
                    ElecReading newReading = new ElecReading();
                    newReading.setMeterId(newMeterReading.getMeterId());
                    newReading.setReading(newMeterReading.getReading());
                    newReading.setDate(newMeterReading.getDate());
                    
                    elecReadingService.save(ACCOUNTNUMBER, newReading);
                    //Adding to the already retrieved list so that the next new reading is checked against this too.
                    elecReadings.add(newReading);

                    break;
                case GAS:
                    for (GasReading oldGasReading : gasReadings) {
                        //Check for duplicates
                        responseString = MeterReadingUtil.validation(oldGasReading, newMeterReading);
                        if(responseString.length() > 0)
                            return new ResponseEntity<>(responseString, HttpStatus.BAD_REQUEST);
                    }

                    GasReading newGasReading = new GasReading();
                    newGasReading.setMeterId(newMeterReading.getMeterId());
                    newGasReading.setReading(newMeterReading.getReading());
                    newGasReading.setDate(newMeterReading.getDate());

                    gasReadingService.save(ACCOUNTNUMBER, newGasReading);
                    //Adding to the already retrieved list so that the next new reading is checked against this too.
                    gasReadings.add(newGasReading);
                    break;
                default:
                    return new ResponseEntity<>("Unknown Meter Type", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
