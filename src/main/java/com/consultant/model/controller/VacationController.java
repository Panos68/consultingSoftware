package com.consultant.model.controller;

import com.consultant.model.dto.VacationDTO;
import com.consultant.model.services.impl.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/vacations")
public class VacationController {

    VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping
    public ResponseEntity<Set<VacationDTO>> getAllVacations() {
        return ResponseEntity.ok(vacationService.getAll());
    }

    @PostMapping
    public void createVacation(@RequestBody VacationDTO vacationDTO) throws Exception {
        vacationService.create(vacationDTO);
    }

    @PutMapping
    public void editVacation(@RequestBody VacationDTO vacationDTO) throws Exception {
        vacationService.edit(vacationDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCandidate(@PathVariable Long id) throws Exception {
        vacationService.delete(id);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<Set<VacationDTO>> getUserVacations(@PathVariable Long userId) {
        return ResponseEntity.ok(vacationService.getVacationsOfUser(userId));
    }
}
