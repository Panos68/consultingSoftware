package com.consultant.model.controller;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.services.ConsultantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {

    private ConsultantsService consultantsService;

    @Autowired
    public ConsultantsController(ConsultantsService consultantsService) {
        this.consultantsService = consultantsService;
    }

    @GetMapping
    public ResponseEntity<Set<ConsultantDTO>> getAllConsultants(){
        return ResponseEntity.ok(consultantsService.getAllConsultants());
    }

    @PostMapping
    public void createConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantsService.createConsultant(consultantDTO);
    }

    @PutMapping
    public void editConsultant(@RequestBody ConsultantDTO consultantDTO) throws Exception {
        consultantsService.editConsultant(consultantDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id) throws Exception {
        consultantsService.deleteConsultant(id);
    }
}
