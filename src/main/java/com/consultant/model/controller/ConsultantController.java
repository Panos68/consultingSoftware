package com.consultant.model.controller;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.requests.ContractRequest;
import com.consultant.model.services.impl.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/consultants")
public class ConsultantController {

    private ConsultantService consultantService;

    @Autowired
    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public ResponseEntity<Set<ConsultantDTO>> getAllConsultants() {
        return ResponseEntity.ok(consultantService.getAll());
    }

    @PostMapping
    public void createConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantService.create(consultantDTO);
    }

    @PostMapping(value = "/contract/create")
    public void createNewContract(@RequestBody ContractRequest contractRequest) throws Exception {
        consultantService.createNewContract(contractRequest);
    }

    @PostMapping(value = "contract/terminate")
    public void terminate(@RequestBody ContractRequest contractRequest) throws Exception {
        consultantService.terminateContract(contractRequest);
    }

    @PutMapping
    public void editConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantService.edit(consultantDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id) throws Exception {
        consultantService.delete(id);
    }
}
