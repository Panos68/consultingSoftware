package com.consultant.model.controller;

import com.consultant.model.exception.NoMatchException;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.services.impl.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private ConsultantService consultantService;

    @Autowired
    public ContractController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @PostMapping(value = "/create")
    public void createNewContract(@RequestBody ContractDTO contractDTO) throws NoMatchException {
        consultantService.createNewContractForExistingConsultant(contractDTO);
    }

    @PostMapping(value = "/terminate")
    public void terminate(@RequestParam(value = "consultantId") Long consultantId,
                          @RequestParam(value = "terminatedDate",required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate terminatedDate) throws NoMatchException {
        consultantService.terminateContract(consultantId,terminatedDate);
    }
}
