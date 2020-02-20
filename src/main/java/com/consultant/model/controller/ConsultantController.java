package com.consultant.model.controller;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.exception.NoMatchException;
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
    public void createConsultant(@RequestBody ConsultantDTO consultantDTO) throws NoMatchException {
        consultantService.create(consultantDTO);
    }

    @PutMapping
    public void editConsultant(@RequestBody ConsultantDTO consultantDTO) throws NoMatchException {
        consultantService.edit(consultantDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id) throws NoMatchException {
        consultantService.delete(id);
    }
}
