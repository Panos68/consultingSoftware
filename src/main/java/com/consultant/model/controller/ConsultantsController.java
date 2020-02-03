package com.consultant.model.controller;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.services.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {

    private ConsultantService consultantService;

    @Autowired
    public ConsultantsController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public ResponseEntity<Set<ConsultantDTO>> getAllConsultants(){
        return ResponseEntity.ok(consultantService.getAllConsultants());
    }

    @PostMapping
    public void createConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantService.createConsultant(consultantDTO);
    }

    @PutMapping
    public void editConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantService.editConsultant(consultantDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id) throws Exception {
        consultantService.deleteConsultant(id);
    }
}
